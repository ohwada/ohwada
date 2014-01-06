package jp.ohwada.android.evy1;

import java.util.HashMap;

/*
 * Phonetic Symbol for Evy1
 *
 * YMW820 アプリケーションノート
 * http://yamaha-webmusic.github.io/nsx1-apps/specs/ANMW820A-001-10-j.pdf
 */ 
public class Evy1Phonetic {
				
	private final static byte[] PHONETIC_HEADER = new byte[]{
		(byte)0xF0, (byte)0x43, (byte)0x79, (byte)0x09, (byte)0x00, (byte)0x50, (byte) 0x10 };
	private final static byte[] PHONETIC_FOOTER = new byte[]{ 
		(byte)0x00, (byte)0xF7 };
		
	private static HashMap<String,String> HASH_MAP = new HashMap<String,String>();
		
	/*
	 * constructor
	 */ 		
	public Evy1Phonetic() {
		// 日本語 eVocaloi Phonetic Alphabet (PA)
		// \ -> \\
		HASH_MAP.put( "あ", "a" );
		HASH_MAP.put( "あ", "a" );
		HASH_MAP.put( "い", "i" );
		HASH_MAP.put( "う", "M" );
		HASH_MAP.put( "え", "e" );
		HASH_MAP.put( "お", "o" );		
		HASH_MAP.put( "か", "k a" );
		HASH_MAP.put( "き", "k' i" );
		HASH_MAP.put( "く", "k M" );
		HASH_MAP.put( "け", "k e" );
		HASH_MAP.put( "こ", "k o" );		
		HASH_MAP.put( "さ", "s a" );
		HASH_MAP.put( "し", "S i" );
		HASH_MAP.put( "す", "s M" );
		HASH_MAP.put( "せ", "s e" );
		HASH_MAP.put( "そ", "s o" );
		HASH_MAP.put( "た", "t a" );
		HASH_MAP.put( "ち", "tS i" );
		HASH_MAP.put( "つ", "ts M" );
		HASH_MAP.put( "て", "t e" );
		HASH_MAP.put( "と", "t o" );
		HASH_MAP.put( "な", "n a" );
		HASH_MAP.put( "に", "J i" );
		HASH_MAP.put( "ぬ", "n M" );
		HASH_MAP.put( "ね", "n e" );
		HASH_MAP.put( "の", "n o" );		
		HASH_MAP.put( "は", "h a" );
		HASH_MAP.put( "ひ", "C i" );
		HASH_MAP.put( "ふ", "p\\ M" );
		HASH_MAP.put( "へ", "h e" );
		HASH_MAP.put( "ほ", "h o" );
		HASH_MAP.put( "ま", "m a" );
		HASH_MAP.put( "み", "m' i" );
		HASH_MAP.put( "む", "m M" );
		HASH_MAP.put( "め", "m e" );
		HASH_MAP.put( "も", "m o" );
		HASH_MAP.put( "や", "j a" );
		HASH_MAP.put( "ゆ", "j M" );
		HASH_MAP.put( "よ", "j o" );
		HASH_MAP.put( "ら", "4 a" );
		HASH_MAP.put( "り", "4' i" );
		HASH_MAP.put( "る", "4 M" );
		HASH_MAP.put( "れ", "4 e" );
		HASH_MAP.put( "ろ", "4 o" );					
		HASH_MAP.put( "わ", "w a" );
		HASH_MAP.put( "ゐ", "w i" );
		HASH_MAP.put( "ゑ", "w e" );
		HASH_MAP.put( "わ", "w a" );
		HASH_MAP.put( "ん", "N\\" );
		HASH_MAP.put( "が", "g a" );
		HASH_MAP.put( "ぎ", "g' i" );
		HASH_MAP.put( "ぐ", "g M" );
		HASH_MAP.put( "げ", "g e" );
		HASH_MAP.put( "ご", "g o" );
		HASH_MAP.put( "ざ", "dz a" );
		HASH_MAP.put( "じ", "dZ i" );
		HASH_MAP.put( "ず", "dz M" );
		HASH_MAP.put( "ぜ", "dz e" );
		HASH_MAP.put( "ぞ", "dz o" );
		HASH_MAP.put( "だ", "d a" );
		HASH_MAP.put( "ぢ", "dZ i" );
		HASH_MAP.put( "づ", "dz M" );
		HASH_MAP.put( "で", "d e" );
		HASH_MAP.put( "ど", "d o" );
		HASH_MAP.put( "ば", "b a" );
		HASH_MAP.put( "び", "b' i" );
		HASH_MAP.put( "ぶ", "b M" );
		HASH_MAP.put( "べ", "b e" );
		HASH_MAP.put( "ぼ", "b o" );
		HASH_MAP.put( "ぱ", "p a" );
		HASH_MAP.put( "ぴ", "p i" );
		HASH_MAP.put( "ぷ", "p' i" );
		HASH_MAP.put( "ぺ", "p e" );
		HASH_MAP.put( "ぽ", "p o" );
		HASH_MAP.put( "ふぁ", "p\\ a" );
		HASH_MAP.put( "つぁ", "ts a" );
		HASH_MAP.put( "うぃ", "w i" );
		HASH_MAP.put( "すぃ", "s i" );
		HASH_MAP.put( "ずぃ", "dz i" );
		HASH_MAP.put( "つぃ", "ts i" );
		HASH_MAP.put( "てぃ", "t' i" );
		HASH_MAP.put( "でぃ", "d' i"  );
		HASH_MAP.put( "ふぃ", "p\' i" );
		HASH_MAP.put( "とぅ", "t M" );	
		HASH_MAP.put( "どぅ", "d M" );
		HASH_MAP.put( "いぇ", "j e" );
		HASH_MAP.put( "うぇ", "w e" );
		HASH_MAP.put( "きぇ", "k' e" );
		HASH_MAP.put( "しぇ", "S e" );							
		HASH_MAP.put( "ちぇ", "tS e" );		
		HASH_MAP.put( "つぇ", "ts e" );		
		HASH_MAP.put( "てぇ", "t' e" );		
		HASH_MAP.put( "にぇ", "J e " );		
		HASH_MAP.put( "ひぇ", "C e" );		
		HASH_MAP.put( "みぇ", "m' e" );		
		HASH_MAP.put( "りぇ", "4' e" );	
		HASH_MAP.put( "ぎぇ", "g' e" );	
		HASH_MAP.put( "じぇ", "dZ e" );	
		HASH_MAP.put( "でぇ", "d' e" );	
		HASH_MAP.put( "びぇ", "b' e" );													
		HASH_MAP.put( "ぴぇ", "p' e" );	
		HASH_MAP.put( "ふぇ", "p\\ e" );	
		HASH_MAP.put( "うぉ", "w o" );	
		HASH_MAP.put( "つぉ", "ts o" );	
		HASH_MAP.put( "ふぉ", "p\\ o" );		
		HASH_MAP.put( "きゃ", "k' a" );	
		HASH_MAP.put( "しゃ", "S a" );	
		HASH_MAP.put( "ちゃ", "tS a" );	
		HASH_MAP.put( "てゃ", "t' a" );	
		HASH_MAP.put( "にゃ", "J a" );		
		HASH_MAP.put( "ひゃ", "C a" );	
		HASH_MAP.put( "みゃ", "m' a" );	
		HASH_MAP.put( "りゃ", "4' a" );	
		HASH_MAP.put( "ぎゃ", "N' a" );	
		HASH_MAP.put( "じゃ", "dZ a" );		
		HASH_MAP.put( "でゃ", "d' a" );		
		HASH_MAP.put( "びゃ", "b' a" );		
		HASH_MAP.put( "ぴゃ", "p' a" );		
		HASH_MAP.put( "ふゃ", "p\' a" );		
		HASH_MAP.put( "きゅ", "k' M" );		
		HASH_MAP.put( "しゅ", "S M" );		
		HASH_MAP.put( "ちゅ", "tS M" );		
		HASH_MAP.put( "てゅ", "t' M" );		
		HASH_MAP.put( "にゅ", "J M" );		
		HASH_MAP.put( "ひゅ", "C M" );		
		HASH_MAP.put( "みゅ", "m' M" );		
		HASH_MAP.put( "りゅ", "4' M" );		
		HASH_MAP.put( "ぎゅ", "g' M" );		
		HASH_MAP.put( "じゅ", "dZ M" );		
		HASH_MAP.put( "でゅ", "d' M" );		
		HASH_MAP.put( "びゅ", "b' M" );		
		HASH_MAP.put( "ぴゅ", "p' M" );		
		HASH_MAP.put( "ふゅ", "p\' M" );		
		HASH_MAP.put( "きょ", "k' o" );		
		HASH_MAP.put( "しょ", "S o" );		
		HASH_MAP.put( "ちょ", "tS o" );		
		HASH_MAP.put( "てょ", "t' o" );		
		HASH_MAP.put( "にょ", "J o" );		
		HASH_MAP.put( "ひょ", "C o" );		
		HASH_MAP.put( "みょ", "m' o" );		
		HASH_MAP.put( "りょ", "4' o" );		
		HASH_MAP.put( "ぎょ", "N' o" );		
		HASH_MAP.put( "じょ", "dZ o" );		
		HASH_MAP.put( "でょ", "d' o" );		
		HASH_MAP.put( "びょ", "b' o" );		
		HASH_MAP.put( "ぴょ", "p' o" );												
	}

	/*
	 * 歌詞のバイト列を取得する
	 * @param String 歌詞の一文字
	 * @return byte[] 歌詞のバイト列
	 */	
	public byte[] getLylic( String str ) {
		String s = HASH_MAP.get( str );
		if ( s == null ) return null;
 		return s.getBytes();
 	}
 
 	/*
	 * Phonetic Symbols を取得する
	 * @param byte[] 歌詞のバイト列
	 * @return byte[] Phonetic Symbols
	 */			 
	public byte[] getPhonetic( byte[] bytes ) {
		return buildMessage( 
			PHONETIC_HEADER, 
			PHONETIC_FOOTER, 
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
 		
}
