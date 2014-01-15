package jp.ohwada.android.evy1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

/*
 * Phonetic Symbol for Evy1
 *
 * YMW820 アプリケーションノート
 * http://yamaha-webmusic.github.io/nsx1-apps/specs/ANMW820A-001-10-j.pdf
 */ 
public class Evy1Phonetic {

	// debug
	private final static String TAG_SUB = "Evy1Phonetic";
	
	// Phonetic Symbols のバイト形式			
	private final static byte[] PS_HEADER = new byte[]{
		(byte)0xF0, (byte)0x43, (byte)0x79, (byte)0x09, (byte)0x00, (byte)0x50, (byte) 0x10 };
	private final static byte[] PS_FOOTER = new byte[]{ 
		(byte)0x00, (byte)0xF7 };

	// PA の最大のバイト数
	private final static int PA_MAX = 128;
	
	// PA の区切り					
	private final static String CHAR_COMMA = ",";
	
	// 除外する文字
	private final static String CHAR_SPACE = " ";
	private final static String CHAR_ZENKAKU_SPACE = "　";
	private final static String CHAR_LF = "\n";
	
	// 捨て仮名 : 前の文字とくっつく拗音
	private static final String[] PA_SUTEGANA_STRINGS = new String[]{
		"ぁ", "ぃ", "ぅ", "ぇ", "ぉ", "ゃ", "ゅ", "ょ" };
		
	// PA の配列	
	private static HashMap<String,String> PA_HASH = new HashMap<String,String>();

	// PA に変換できない文字列のリスト
	private List<String> mPaInaccurateList = new ArrayList<String>();
				
	// PA の配列の初期化	
	static {
		// 日本語 eVocaloi PA ( Phonetic Alphabet )
		// \ -> \\
		PA_HASH.put( "あ", "a" );
		PA_HASH.put( "あ", "a" );
		PA_HASH.put( "い", "i" );
		PA_HASH.put( "う", "M" );
		PA_HASH.put( "え", "e" );
		PA_HASH.put( "お", "o" );		
		PA_HASH.put( "か", "k a" );
		PA_HASH.put( "き", "k' i" );
		PA_HASH.put( "く", "k M" );
		PA_HASH.put( "け", "k e" );
		PA_HASH.put( "こ", "k o" );		
		PA_HASH.put( "さ", "s a" );
		PA_HASH.put( "し", "S i" );
		PA_HASH.put( "す", "s M" );
		PA_HASH.put( "せ", "s e" );
		PA_HASH.put( "そ", "s o" );
		PA_HASH.put( "た", "t a" );
		PA_HASH.put( "ち", "tS i" );
		PA_HASH.put( "つ", "ts M" );
		PA_HASH.put( "て", "t e" );
		PA_HASH.put( "と", "t o" );
		PA_HASH.put( "な", "n a" );
		PA_HASH.put( "に", "J i" );
		PA_HASH.put( "ぬ", "n M" );
		PA_HASH.put( "ね", "n e" );
		PA_HASH.put( "の", "n o" );		
		PA_HASH.put( "は", "h a" );
		PA_HASH.put( "ひ", "C i" );
		PA_HASH.put( "ふ", "p\\ M" );
		PA_HASH.put( "へ", "h e" );
		PA_HASH.put( "ほ", "h o" );
		PA_HASH.put( "ま", "m a" );
		PA_HASH.put( "み", "m' i" );
		PA_HASH.put( "む", "m M" );
		PA_HASH.put( "め", "m e" );
		PA_HASH.put( "も", "m o" );
		PA_HASH.put( "や", "j a" );
		PA_HASH.put( "ゆ", "j M" );
		PA_HASH.put( "よ", "j o" );
		PA_HASH.put( "ら", "4 a" );
		PA_HASH.put( "り", "4' i" );
		PA_HASH.put( "る", "4 M" );
		PA_HASH.put( "れ", "4 e" );
		PA_HASH.put( "ろ", "4 o" );					
		PA_HASH.put( "わ", "w a" );
		PA_HASH.put( "ゐ", "w i" );
		PA_HASH.put( "ゑ", "w e" );
		PA_HASH.put( "わ", "w a" );
		PA_HASH.put( "ん", "N\\" );
		PA_HASH.put( "が", "g a" );
		PA_HASH.put( "ぎ", "g' i" );
		PA_HASH.put( "ぐ", "g M" );
		PA_HASH.put( "げ", "g e" );
		PA_HASH.put( "ご", "g o" );
		PA_HASH.put( "ざ", "dz a" );
		PA_HASH.put( "じ", "dZ i" );
		PA_HASH.put( "ず", "dz M" );
		PA_HASH.put( "ぜ", "dz e" );
		PA_HASH.put( "ぞ", "dz o" );
		PA_HASH.put( "だ", "d a" );
		PA_HASH.put( "ぢ", "dZ i" );
		PA_HASH.put( "づ", "dz M" );
		PA_HASH.put( "で", "d e" );
		PA_HASH.put( "ど", "d o" );
		PA_HASH.put( "ば", "b a" );
		PA_HASH.put( "び", "b' i" );
		PA_HASH.put( "ぶ", "b M" );
		PA_HASH.put( "べ", "b e" );
		PA_HASH.put( "ぼ", "b o" );
		PA_HASH.put( "ぱ", "p a" );
		PA_HASH.put( "ぴ", "p i" );
		PA_HASH.put( "ぷ", "p' i" );
		PA_HASH.put( "ぺ", "p e" );
		PA_HASH.put( "ぽ", "p o" );
		PA_HASH.put( "ふぁ", "p\\ a" );
		PA_HASH.put( "つぁ", "ts a" );
		PA_HASH.put( "うぃ", "w i" );
		PA_HASH.put( "すぃ", "s i" );
		PA_HASH.put( "ずぃ", "dz i" );
		PA_HASH.put( "つぃ", "ts i" );
		PA_HASH.put( "てぃ", "t' i" );
		PA_HASH.put( "でぃ", "d' i"  );
		PA_HASH.put( "ふぃ", "p\' i" );
		PA_HASH.put( "とぅ", "t M" );	
		PA_HASH.put( "どぅ", "d M" );
		PA_HASH.put( "いぇ", "j e" );
		PA_HASH.put( "うぇ", "w e" );
		PA_HASH.put( "きぇ", "k' e" );
		PA_HASH.put( "しぇ", "S e" );							
		PA_HASH.put( "ちぇ", "tS e" );		
		PA_HASH.put( "つぇ", "ts e" );		
		PA_HASH.put( "てぇ", "t' e" );		
		PA_HASH.put( "にぇ", "J e " );		
		PA_HASH.put( "ひぇ", "C e" );		
		PA_HASH.put( "みぇ", "m' e" );		
		PA_HASH.put( "りぇ", "4' e" );	
		PA_HASH.put( "ぎぇ", "g' e" );	
		PA_HASH.put( "じぇ", "dZ e" );	
		PA_HASH.put( "でぇ", "d' e" );	
		PA_HASH.put( "びぇ", "b' e" );													
		PA_HASH.put( "ぴぇ", "p' e" );	
		PA_HASH.put( "ふぇ", "p\\ e" );	
		PA_HASH.put( "うぉ", "w o" );	
		PA_HASH.put( "つぉ", "ts o" );	
		PA_HASH.put( "ふぉ", "p\\ o" );		
		PA_HASH.put( "きゃ", "k' a" );	
		PA_HASH.put( "しゃ", "S a" );	
		PA_HASH.put( "ちゃ", "tS a" );	
		PA_HASH.put( "てゃ", "t' a" );	
		PA_HASH.put( "にゃ", "J a" );		
		PA_HASH.put( "ひゃ", "C a" );	
		PA_HASH.put( "みゃ", "m' a" );	
		PA_HASH.put( "りゃ", "4' a" );	
		PA_HASH.put( "ぎゃ", "N' a" );	
		PA_HASH.put( "じゃ", "dZ a" );		
		PA_HASH.put( "でゃ", "d' a" );		
		PA_HASH.put( "びゃ", "b' a" );		
		PA_HASH.put( "ぴゃ", "p' a" );		
		PA_HASH.put( "ふゃ", "p\' a" );		
		PA_HASH.put( "きゅ", "k' M" );		
		PA_HASH.put( "しゅ", "S M" );		
		PA_HASH.put( "ちゅ", "tS M" );		
		PA_HASH.put( "てゅ", "t' M" );		
		PA_HASH.put( "にゅ", "J M" );		
		PA_HASH.put( "ひゅ", "C M" );		
		PA_HASH.put( "みゅ", "m' M" );		
		PA_HASH.put( "りゅ", "4' M" );		
		PA_HASH.put( "ぎゅ", "g' M" );		
		PA_HASH.put( "じゅ", "dZ M" );		
		PA_HASH.put( "でゅ", "d' M" );		
		PA_HASH.put( "びゅ", "b' M" );		
		PA_HASH.put( "ぴゅ", "p' M" );		
		PA_HASH.put( "ふゅ", "p\' M" );		
		PA_HASH.put( "きょ", "k' o" );		
		PA_HASH.put( "しょ", "S o" );		
		PA_HASH.put( "ちょ", "tS o" );		
		PA_HASH.put( "てょ", "t' o" );		
		PA_HASH.put( "にょ", "J o" );		
		PA_HASH.put( "ひょ", "C o" );		
		PA_HASH.put( "みょ", "m' o" );		
		PA_HASH.put( "りょ", "4' o" );		
		PA_HASH.put( "ぎょ", "N' o" );		
		PA_HASH.put( "じょ", "dZ o" );		
		PA_HASH.put( "でょ", "d' o" );		
		PA_HASH.put( "びょ", "b' o" );		
		PA_HASH.put( "ぴょ", "p' o" );												
	}

	/*
	 * constructor
	 */ 
	public Evy1Phonetic() {
		// dummy
	}

	/*
	 * 文字列を発音する単位に分割する
	 * @param String str
	 * @return String[]
	 */ 
	public String[] splitText( String str ) {
		log_d( "splitText " + str );
		int count = 0;
		String s = "";
		int len = str.length();
		String[] strings = new String[ PA_MAX ];
		for ( int i = 0; i < len; i++ ) {
			s =  str.substring( i, i + 1 );
			if ( matchExcept( s ) ) continue;
			if ( matchSutegana( s ) ) {
				// 捨て仮名なら、１つ前の文字にくっつける
				if ( count > 0 ) {
					strings[ count - 1 ] += s;
				}
			} else {
				// 普通の仮名なら、配列に入れる
				strings[ count ] = s;
				count ++;
			}
			// 文字数オーバーなら、そこで終了する
			if ( count >= PA_MAX ) {
				log_d( "splitText over lenght" );
				break;
			}
		}
		String[] ret = packStrings( strings, count );
		log_d( "splitText " + getMsg( ret ) );
		return ret;
	}

	/*
	 * 除外する文字に一致するか
	 * @param String str
	 * @return boolean
	 */ 
	private boolean matchExcept( String str ) {
		if ( CHAR_SPACE.equals( str ) ) return true; 
		if ( CHAR_ZENKAKU_SPACE.equals( str ) ) return true; 
		if ( CHAR_LF.equals( str ) ) return true; 
		return false;
	}
					
	/*
	 * 捨て仮名に一致するか
	 * @param String str
	 * @return boolean
	 */ 		
	private boolean matchSutegana( String str ) {
		for ( int i = 0; i < PA_SUTEGANA_STRINGS.length; i++ ) {
			if ( str.equals( PA_SUTEGANA_STRINGS[ i ] ) ) return true;
		}
		return false;
	}

	/*
	 * 文字の配列が PA に変換できるかチェックする
	 * @param String[] texts
	 * @return boolean
	 */
	public boolean checkLylics( String[] texts ) {
		getAlphabets( texts );
		if ( mPaInaccurateList.size() == 0 ) return true;
		return false;
	}

	/*
	 * PA に変換できない文字列を取得する
	 * @return String
	 */
	public String getInaccurateLylics(){
		String str = "";
		for ( int i = 0; i < mPaInaccurateList.size(); i++ ) {
			str += mPaInaccurateList.get( i ) + CHAR_SPACE;
		}
		return str;
	}
	
	/*
	 * 文字の配列を PA に変換する
	 * @param String str
	 * @return boolean
	 */
	public String getAlphabets( String[] texts ) {
		log_d( "getAlphabets" );
		mPaInaccurateList.clear();
		int len = texts.length;
		if ( len == 0 ) return null;
		String ret = "";
		String text = "";
		String pa = "";
		for ( int i = 0; i < len; i++ ) {
			text = texts[ i ]; 
			pa = getAlphabet( text );
			if ( pa == null ) {
				// 変換できなければ、リストの追加する
				mPaInaccurateList.add( text );
				log_d( "getAlphabets Illegall " + text );				
			} else {
				// 変換できれば、文字列に追加する
				if ( i > 0 ) {
					ret += CHAR_COMMA;
				}
				ret += pa;
			}
			// 文字数オーバーなら、そこで終了する
			if ( ret.length() >= PA_MAX ) {
				log_d( "getAlphabets over lenght" );
				break;
			}	
		}
		log_d( "getAlphabets " + ret );
 		return ret;
	}

	/*
	 * 文字を PA に変換する
	 * @param String str
	 * @return boolean
	 */
	public String getAlphabet( String str ) {
		String ret =	PA_HASH.get( str );
		log_d( "getAlphabet " + str + " " + ret );
		return ret;
 	}

 	/*
	 * Phonetic Symbols を取得する
	 * @param String str
	 * @return byte[]
	 */	
	public byte[] getSymbols( String str ) {
		return getSymbols( str.getBytes() );
	}

 	/*
	 * Phonetic Symbols を取得する
	 * @param byte[] bytes
	 * @return byte[]
	 */			 
	public byte[] getSymbols( byte[] bytes ) {
		return buildMessage( 
			PS_HEADER, 
			PS_FOOTER, 
			bytes );
 	}

 	/*
	 * buildMessage
	 * @param byte[] header
	 * @param byte[] footer
	 * @param byte[] body
	 * @return byte[] 
	 */
	private byte[] buildMessage( byte[] header, byte[] footer, byte[] body ) {
		int header_len = header.length;
		int boby_len = body.length;
		int footer_len = footer.length;
		int len = header_len + boby_len + footer_len ;
		byte[] bytes = new byte[ len ];
 		for ( int i=0; i<header_len; i++ ) {
 			bytes[ i ] = header[ i ]; 
 		}		
 		for ( int j=0; j<boby_len; j++ ) {
 			bytes[ header_len + j ] = body[ j ]; 
 		}
 		for ( int k=0; k<footer_len; k++ ) {
 			bytes[ header_len + boby_len + k ] = footer[ k ]; 
 		}
 		return bytes;
 	}

	/**
	 * packStrings
	 * @param String[] strings
	 * @param int length
	 * @return String[]
	 */
	private String[] packStrings( String[] strings, int length ) {
		String[] ret = new String[ length ];
		for ( int i = 0; i < length; i++ ) {
			ret[ i ] = strings[ i ];
		}
		return ret;
	}
		
	/**
	 * getMsg
	 * @param String[] str
	 * @return String
	 */
	private String getMsg( String[] str ) {
		String msg = "";
		for ( int i = 0; i < str.length; i++ ) {
			msg += str[ i ] + " ";
		}
		return msg;
	}
		
	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (Evy1Constant.D) Log.d( Evy1Constant.TAG, TAG_SUB + " " + msg );	
	} 		
}
