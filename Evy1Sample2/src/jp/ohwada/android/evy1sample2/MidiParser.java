package jp.ohwada.android.evy1sample2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

/*
 * MidiParser for single track
 */ 
public class MidiParser {

	// debug
	private final static boolean D = true;
	private final static int DEBUG_LEVEL = 0;
	private final static String TAG = "MidiPaser";
    
    // MIDI バイトの解析の状態
	private final static int STATE_INIT = 0;
	private final static int STATE_HEAD_CHUNK = 1;
	private final static int STATE_HEADER = 2;
	private final static int STATE_BODY = 3;
	private final static int STATE_END = 4;

    // MIDI メッセージの種別
	private final static int MSG_NORMAL = 0;
	private final static int MSG_DEVICE = 1;
	private final static int MSG_END = 2;

	// 空白
	private final static String CHAR_SPACE = " ";
	
	// 上位から引き継いだ object
	private Context mContext;

	// 解析対象のMIDIのバイト列
	private byte[] mMidiBytes = null;

	// バイト列の処理中の位置を示すポインタ		
	private int mReadPointer = 0;

	// チャンク単位のバイト列の最後を示すポインタ
	private int mReadEnd = 0;

	// バイト列の最後になったことを示すフラグ
	private boolean isEndOfBytes = false;
	
	// デルタタイムのベースとなる時間
	private long mTimebase = 0;
		
	// 解析されたMIDIのリスト
	private List<MidiMessage> mResultiList = null;	
	
	// 曲のテンポ ( デルタタイムの補正に使用する )
	private long mTempo = 0;

	// 演奏時間 (テンポで補正されたデルタタイムの累積)
	private long mPlayTime = 0;

	// ステータスバイト (MIDIメッセージの種別を示す)
	private byte mStatusByte = 0;
	
	// MIDI バイトの解析の状態
	private int mParseState = STATE_INIT;

	// リストに追加するステータムバイトの一時保管
	private int mTmpStatusByte = 0;

	// リストに追加するバイト列の一時保管
	private byte[] mTmpBytes = null;

	// ログに残すために、読み込んだデータの一時保管
    private String mLogText = "";

	/*
	 * コンストラクタ
	 * @param Context context
	 */ 		
	public MidiParser( Context context ) {
		mContext = context;
	}

	/*
	 * バイト列を解析する
	 * @param byte[] bytes
	 */ 	
	public void parse( byte[] bytes ) {
		mMidiBytes = bytes;
		isEndOfBytes = false;	
		mReadPointer = 0;
		mReadEnd = 0;
		mResultiList = new ArrayList<MidiMessage>();		 				
		mStatusByte = 0x00;
		mPlayTime = 0;

		boolean ret1 = procHead();
		// 形式が異なれば、処理を中断する
		if ( !ret1 ) return;

		mParseState = STATE_HEADER;	
		while (true) {
			// バイト列の最後になれば、処理を中断する
			if ( isEndOfBytes ) return;
			// 状態が変われば、処理を中断する
			if ( mParseState != STATE_HEADER ) return;		
			boolean ret2 = procTrackHeader();
			// ヘッダが見つかれば、本体の処理に進む
			if ( ret2 ) break;	
		}

		mParseState = STATE_BODY;		
		while (true) {
			// バイト列の最後になれば、終了する
			if ( isEndOfBytes ) break;
			// 状態が変われば、終了する
			if ( mParseState != STATE_BODY ) break;
			boolean ret3 = procTrackBody();
			// 本体の最後まで処理したときは、終了する
			if ( ret3 ) break;
		}
		
	}

	/*
	 * 解析されたMIDIのリストを取得する
	 * @return List<MidiMessage>
	 */ 
	public List<MidiMessage> getList() {
		return mResultiList;
	}

	/*
	 * ベース時間を取得する
	 * @return long
	 */ 
	public long getTimebase() {	
		return mTimebase;
	}	

	/*
	 * ヘッダチャンクの処理
	 * @return boolean	 
	 */
	private boolean procHead() {
		clearLogText();
		byte b1 = getOneByte();
		byte b2 = getOneByte();
		byte b3 = getOneByte();
		byte b4 = getOneByte();
		if (( b1 == 0x4d )&&( b2 == 0x54 )&&( b3 == 0x68 )&&( b4 == 0x64 )) {
			log_write_with_text( "head chunk" );
			int len = getLength4();
			if ( len == 6 ) {
				mParseState = STATE_HEAD_CHUNK;
				procHeadBody( len );
				return true; // 正常
			} else {
				log_d( "invalid header length" );				
			}	
		} else {
			log_d( "invalid header" );		
		}
		return false; // エラー
	}

	/*
	 * ヘッダチャンクの本体の処理
	 * @param int len 
	 */      		
	private void procHeadBody( int len ) {	
		mReadEnd = mReadPointer + len;
		// format
		getOneByte();
		getOneByte();
		log_write_with_text( "format" );
		// tracks
		getOneByte();
		getOneByte();
		log_write_with_text( "number of tracks" );
		// timebase
		byte b1 = getOneByte();
		byte b2 = getOneByte();
		mTimebase = getValue2( b1, b2 );												
		log_write_with_text( "timebase" );
	}

	/*
	 * トラックチャンクのヘッダを検索する
	 * @return boolean
	 */ 				
	private boolean procTrackHeader() {
		clearLogText();
		byte b1 = 0;
		byte b2 = 0;
		byte b3 = 0;
		byte b4 = 0;					
		while( true ) {
			// バイト列の最後になれば、終了する
			if ( isEndOfBytes ) break;
			// ヘッダが見つかれば、次の処理に進む
			if (( b1 == 0x4d )&&( b2 == 0x54 )&&( b3 == 0x72 )&&( b4 == 0x6b )) {
				log_write_with_text(  "track" );
				int len = getLength4();
				mReadEnd = mReadPointer + len;
				// dummy
				addPlayTime();
				return true;
			} 
			// ヘッダが見つかるまで、１バイトづつ読み進める
			// 正しい形式であれば、最初の４バイトがヘッダである
			b1 = b2;
			b2 = b3;
			b3 = b4;
			b4 = getOneByte();
		}
		return false;
	}

	/*
	 * トラックチャンクの本体の処理
	 * @return int
	 */ 
	private boolean procTrackBody() {
      	int ret = procTrackMessage();
      	switch ( ret ) {
      		case MSG_NORMAL:
      			// 通常のときは、演奏時間（デルタタイム）を加算する
      			addPlayTime();
      			break;
      		case MSG_DEVICE:
      		    // MIDI機器に送信するときは
      			// 演奏時間（デルタタイム）を取得して、リストに追加する
      			mResultiList.add( 
      				new MidiMessage( mTmpStatusByte, mTmpBytes, mPlayTime ) );
      			// 演奏時間（デルタタイム）を加算する
      			addPlayTime();
      			break; 
      		case MSG_END:
      		     // 本体の最後まで処理したときは、終了する
      		     return true;     			
      	}
      	// 処理を継続する
     	return false;	
     }

	/*
	 * トラックチャンクのメッセージの処理
	 * @return int
	 */ 
	private int procTrackMessage() {
		clearLogText();
		int ret = MSG_NORMAL;
		byte[] buf = new byte[256];
		buf[0] = getOneByte();
		if ( (buf[0] & 0x80) > 0 ) {
			// 最初の１バイト目が 0x8* から 0xF* のときは、普通に処理する
			mStatusByte = buf[0];
			buf[1] = getOneByte();
		} else {
			// それ以外のときは、同じステータスバイトが継続されていると見なす
			buf[1] = buf[0];
			buf[0] = mStatusByte;
		}

		int status = mStatusByte & 0x00f0;
		switch ( status ) {
			case 0x0080:
			case 0x0090:
			case 0x00A0:
			case 0x00B0:
			case 0x00E0:												
				buf[2] = getOneByte();
				procMsgDevice( status, buf, 3 );
				ret = MSG_DEVICE;
				break;
			case 0x00C0:
			case 0x00D0:
	    		procMsgDevice( status, buf, 2 );
				ret = MSG_DEVICE;
				break;
			case 0x00F0:
				ret = procTrackMessageF0( mStatusByte, buf );
				break;
			default:
				procMsgOthersF0( buf );	
				break;			
		}
		return ret;
	}

	/*
	 * トラックチャンクのメッセージの処理
	 * ステータスが F0 のとき
	 * @return int
	 */ 
	private int procTrackMessageF0( byte first_byte, byte[] buf ) {	
		int ret = MSG_NORMAL;
		int status = first_byte & 0x000f;			
		switch( status ) {
    		case 0x0000 : // sysex
    		case 0x0007 : // Sysex2
				// １バイト目はデータ長になっているので、
				// これを除いて機器に送信する形式にする
      			int len = buf[1];
      			for ( int i=1; i<len+1; i++ ) {
      				buf[i] = getOneByte();
      			}
      			procMsgDevice( status, buf, len + 1 );
      			ret = MSG_DEVICE;
      			break;
    		case 0x000f:
      			switch ( buf[1] ) {
      				case 0x0051:
      					procMsgTempo( buf );
        				break;
      				case 0x002f:
      					procMsgEndOfTrack( buf );
      					ret = MSG_END;
      					break;
      				default:
      					procMsgOthersF0( buf );
        				break;
				} // switch end
				break;
		} // switch end
		return ret;
	}

	/*
	 * MIDI機器に送信するメッセージの処理
	 * @param int
	 */	
	private void procMsgDevice( int status, byte[] buf , int num ) {
		log_write_with_text( "device" );
		String str = "";
		byte[] bytes = new byte[ num ];
		for ( int i=0; i<num; i++ ){
			bytes[ i ] = buf[ i ];
			str += toHexString( buf[ i ] ) + CHAR_SPACE;			
		} 
		// 一時保管する	
		mTmpStatusByte = status;
		mTmpBytes = bytes;
		log_write_1( mPlayTime + " : " + str + " : " + getMsgName( bytes ) );
	}

	/*
	 * テンポ・メッセージの処理
	 * @param byte[] buf
	 */ 
	private void procMsgTempo( byte[] buf ) {
       	getOneByte(); // len
    	byte t1 = getOneByte();
    	byte t2 = getOneByte();
    	byte t3 = getOneByte(); 	    	    	    	
		long tempo = ( t1 & 0x00ff );
        tempo = ( tempo << 8 ) | ( t2 & 0x00ff );
        tempo = ( tempo << 8 ) | ( t3 & 0x00ff );
        mTempo = tempo ;
		log_write_with_text( "tempo" );
    }

	/*
	 * エンド・メッセージの処理
	 * @param byte[] buf
	 */         					
	private void procMsgEndOfTrack( byte[] buf ) {
    	getOneByte(); // len
    	log_write_with_text( "end of track" );	
    }

	/*
	 * その他のメッセージの処理
	 * @param byte[] buf
	 */       					
	private void procMsgOthersF0( byte[] buf ) {
		int len = getOneByte(); // len
       for ( int i=0; i<len; i++ ) {
        	getOneByte();
        }
        log_write_with_text( getMsgNameOthers( buf[1] ) );	
    }

	/*
	 * 演奏時間を加算する
	 */	
	private void addPlayTime() {
      	mPlayTime += procDeltaTime() * mTempo;
	}

	/*
	 * デルタタイムの処理
	 * @return long
	 */  
	private long procDeltaTime() {
		byte b;
		long ret = 0;
		while (true) {
			// バイト列の最後になれば、終了する
			if ( isEndOfBytes ) return 0;
			b = getOneByte();
			ret = (ret <<7) | (b & 0x7f);
			// ８ビット目が0のときは、デルタタイムを終了する
			if((b & 0x80) == 0) break;
		}
		log_write_with_text( "DeltaTime" );
		return ret;
	}

	/*
	 * ４バイト単位のバイト長を取得する
	 */ 
	private int getLength4() {
		byte b1 = getOneByte();
		byte b2 = getOneByte();
		byte b3 = getOneByte();
		byte b4 = getOneByte();
		log_write_with_text( "length" );
      	return getValue4( b1, b2, b3, b4 );
     }

	/*
	 * バイト列から１バイトを取得する
	 * @return byte
	 */ 
	private byte getOneByte() {
		byte b =  getOneByteByPointer( mReadPointer ); 
		mReadPointer ++;
		// チャンクの最後になったら、終了する
		if ( mReadPointer >= mReadEnd ) {
			mParseState = STATE_END;
		}
		addHexLogText( b );
		return b;
	}

	/*
	 * バイト列から１バイトを取得する
	 * @param int ptr 
	 * @return byte
	 */       		    			
	private byte getOneByteByPointer( int ptr ) {
		int len = mMidiBytes.length;
		if ( len > ptr ) {
			return mMidiBytes[ ptr ];
		}
		// バイト列の最後になったときは。終了フラグを立てる
		isEndOfBytes = true;
		return 0x00;
	}

	/*
	 * ２バイト単位の値を取得する
	 */ 
	private int getValue2( byte b1, byte b2 ) {
		int v = b1 & 0x00ff;
        v = (v << 8) | (b2 & 0x00ff);
      	return v;
    }
 
 	/*
	 * ４バイト単位の値を取得する
	 */    
	private int getValue4( byte b1, byte b2, byte b3, byte b4 ) {
		int v = b1 & 0x00ff;
        v = (v << 8 ) | (b2 & 0x00ff);
      	v = (v << 8 ) | (b3 & 0x00ff);
      	v = (v << 8 ) | (b4 & 0x00ff);
      	return v;
    }
			
// --- デバック ---
	/*
	 * メッセージの名称を取得する
	 * @param byte[] buf 
	 */
	private String getMsgName( byte[] buf ) {
		String str = "その他";
		switch ( buf[0] & 0xf0 ) {
			case 0x80:
				str = "Key Off";
				break;
			case 0x90:
				str = "Key On";
				break;
			case 0xA0:
				str = "Polyphonic After Touch";
				break;
			case 0xB0:
				str = "Mode Message";
				break;
			case 0xC0:
				str = "Program Change";
				break;
			case 0xD0:
				str = "Channnel After Touch";
				break;
			case 0xE0:	
				str = "Pitch Bend Change";
				break;
			case 0xF0:					
				str = getMsgNameF0( buf );
				break;
		}
		return str;	
	}

	/*
	 * メッセージの名称を取得する
	 * @param byte[] buf 
	 */
	private String getMsgNameF0( byte[] buf  ) {
	 	int num = buf.length;
		String str = "その他";
		if ( num < 5 ) return str;
		switch ( buf[1] ) {
			case 0x7E:
				if (( buf[3] == 0x09 )&&( buf[4] == 0x01 )) {
					str = "GM1 System On";
				} else if (( buf[3] == 0x09 )&&( buf[4] == 0x02 )) {
					str = "GM1 System Off";
				} else {
					str = "F* 7E";				
				}
				break;
			case 0x43:			
				if ( buf[3] == 0x4c ) {
					str = "XG Parameter Changes";
				} else if (( buf[2] == 0x79 )&&( buf[3] == 0x09 )) {
					str = "Phonetic symbols";
				} else {
					str = "F* 43";				
				}
				break;	
		}
		return str;	
	}

	/*
	 * メッセージの名称を取得する
	 * @param byte b
	 */     					
	private String getMsgNameOthers( byte b ) {
		String str = "その他";
    	switch( b ) {
    	    case 0x00:
				str = "シーケンス番号";
				break;
    	    case 0x01:
				str = "テキスト";
				break;
    	    case 0x02:
				str = "著作権表示";
				break;
    	    case 0x03:
				str = "シーケンス名";
				break;
    	    case 0x04:
				str = "楽器名";
				break; 
    	    case 0x05:
				str = "歌詞";
				break;  
    	    case 0x06:
				str = "マーカー";
				break;   
    	    case 0x07:
				str = "キュー・ポイント";
				break;   
    	    case 0x20:
				str = "チャンネル・プリフィクス ";
				break;
    	    case 0x21:
				str = "エンド・オブ・トラック";
				break;
    	    case 0x51:
				str = "セット・テンポ";
				break;
    	    case 0x54:
				str = "SMPTEオフセット";
				break;
    	    case 0x58:
				str = "拍子";
				break;
    	    case 0x59:
				str = "調";
				break;
    	    case 0x7F:
				str = "シーケンサー固有メタ・イベント";
				break;
		}		
		return str;
	}

	/*
	 * ログデータのクリア
	 */ 
	public void clearLogText() {	
		mLogText = "";
	}

	/*
	 * ログデータに追加する
	 * @param byte
	 */ 
	public void addHexLogText( byte b ) {	
		mLogText += toHexString( b ) + CHAR_SPACE;
	}	
		
	/*
	 * ログデータの取得
	 */ 
	public String getLogText() {	
		String str = mLogText;
		mLogText = "";
		return str;
	}

	/*
	 * HEX文字列に変換する
	 * @param byte b
	 * @return String
	 */ 
	private String toHexString( byte b ) {
		int n = b & 0x00ff;
		String str = Integer.toHexString( n );
		if ( n < 0x0010 ) {
			str = "0" + str;
		}
    	return str;
    }
       
	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	}

	/**
	 * write into log file
	 * @param String msg
	 */
	@SuppressWarnings("unused")
	private void log_write_1( String msg ) {
	    if (DEBUG_LEVEL > 0) log_write( msg );
	}

	/**
	 * write into log file
	 * @param String msg
	 */
	@SuppressWarnings("unused")
	private void log_write_with_text( String msg ) {
	    if (DEBUG_LEVEL > 1) log_write( getLogText() + " :  " + msg );
	}

	/**
	 * write into log file
	 * @param String msg
	 */	
	@SuppressWarnings("unused")
	private void log_write( String msg ) {
	    if (DEBUG_LEVEL > 0) LogFile.write( mContext, msg );
	}

}

