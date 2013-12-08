package jp.ohwada.android.evy1sample1;

/*
 * MML for Evy1
 */ 
public class Evy1Mml {

	private final static byte[] PHONETIC_HEADER = new byte[]{
		(byte)0xF0, (byte)0x43, (byte)0x79, (byte)0x09, (byte)0x00, (byte)0x50, (byte) 0x10 };
	private final static byte[] PHONETIC_FOOTER = new byte[]{ 
		(byte)0x00, (byte)0xF7 };
	private final static byte BYTE_COMMA = 0x2c ;
	private final static String CHAR_COMMA = "," ;

	// object
	private Evy1Phonetic mPhonetic;
				
	// 音符 
	private String mMmlString = ""; 
	private int mMmlPointer = 0;
	
	// 歌詞
	private String[] mLylics = null;
	private int mLylicPointer = 0;

	/*
	 * コンストラクタ
	 */ 		
	public Evy1Mml() {
		mPhonetic = new Evy1Phonetic();
	}

	/*
	 * MMLを設定する
	 * @param String MML
	 */ 
	public void setMml( String str ) {
		mMmlString = str;
  		mMmlPointer = 0;
	}

	/*
	 * 次の音があるのか
	 * @return boolean
	 */ 
	public boolean isNextScale() {
		if ( mMmlString.length() > mMmlPointer ) return true;
		return false;
	}

	/*
	 * 次の音階を取得する
	 * @return String
	 */ 
	public String getNextScale() {
   		if ( !isNextScale() ) return "";
   		String str = mMmlString.substring( mMmlPointer, mMmlPointer + 1 );
    	mMmlPointer ++;
    	return str;
    }

	/*
	 * 音階をMIDIの音に変換する
	 * @param tring 音階
	 * @return int MIDIの音
	 */	    
	public int scaleToNote( String str ) {
		byte[] bytes = str.getBytes();
		if (( bytes == null )||( bytes.length == 0 )) return 0;
		int c = bytes[0] & 0x00ff;
    	int note = 0x3C; 
    	if(c >= 'a' && c <='z') c-= 0x20;
    	if((c>='A' && c<='Z')||c=='<'||c=='>') {
        	switch(c) {
            	case 'C': break;
            	case 'D': note+=2; break;
            	case 'E': note+=4; break;
            	case 'F': note+=5; break;
            	case 'G': note+=7; break;
            	case 'A': note+=9; break;
            	case 'B': note+=11; break;
            	default: break;
        	}
        	return note; 
    	}
		return 0;        
	}

	/*
	 * 歌詞を設定する
	 * @param String 歌詞
	 */ 
	public void setLylic( String str ) {
		mLylics = str.split( CHAR_COMMA );
		mLylicPointer = 0;
	}

	/*
	 * 次の歌詞を取得する
	 * @return String
	 */ 
	public String getNextLylic() {
		if ( !isNextLylic() ) return "";
    	String str = mLylics[ mLylicPointer ];
    	mLylicPointer ++;
    	return str;
    }

	/*
	 * 次の歌詞があるのか
	 * @return boolean
	 */ 
	private boolean isNextLylic() {
		if ( mLylics.length > mLylicPointer ) return true;
		return false;
	}
    				
	/*
	 * Phonetic Symbols を取得する
	 * @return byte[] Phonetic Symbols
	 */	
	public byte[] getPhoneticBytes() {
		return getPhoneticBytes( mLylics );
	}

	/*
	 * Phonetic Symbols を取得する
	 * @param String[] 歌詞の配列
	 * @return byte[] Phonetic Symbols
	 */			 
	private byte[] getPhoneticBytes( String[] lylics ) {
		byte[] lylic_bytes = new byte[ 1024 ]; 
		int pointer = 0; 

		int lylic_len = lylics.length;
  		for ( int i=0; i<lylic_len; i++ ) {
  			//２番目以降からカンマで区切る 
    		if ( i != 0 ) { 
    			lylic_bytes[ pointer ] = BYTE_COMMA;
				pointer ++;
    		}
    		// 音に対応する歌詞を設定する
    		byte[] bytes = mPhonetic.getBytes( lylics[ i ] );
    		for ( byte b: bytes ) {
    			lylic_bytes[ pointer ] = b ;
				pointer ++;
			}
  		}

		// MIDIのバイト列に直す
		int header_len = PHONETIC_HEADER.length;
		int boby_len = pointer;
		int footer_len = PHONETIC_FOOTER.length;
		int len = header_len + boby_len + footer_len ;
		byte[] phonetic_bytes = new byte[ len ];
 		for ( int i=0; i<header_len; i++ ) {
 			phonetic_bytes[ i ] = PHONETIC_HEADER[ i ]; 
 		}		
 		for ( int j=0; j<boby_len; j++ ) {
 			phonetic_bytes[ header_len + j ] = lylic_bytes[ j ]; 
 		}
 		for ( int k=0; k<footer_len; k++ ) {
 			phonetic_bytes[ header_len + boby_len + k ] = PHONETIC_FOOTER[ k ]; 
 		}

 		return phonetic_bytes;
 	}
}
