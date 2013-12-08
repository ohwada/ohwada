package jp.ohwada.android.evy1sample1;

import java.util.HashMap;

/*
 * Phonetic Symbol for Evy1
 */ 
public class Evy1Phonetic {

	private static HashMap<String,String> HASH_MAP = new HashMap<String,String>();
		
	/*
	 * コンストラクタ
	 */ 		
	public Evy1Phonetic() {
		HASH_MAP.put( "あ", "a" );
		HASH_MAP.put( "あ", "a" );
		HASH_MAP.put( "い", "i" );
		HASH_MAP.put( "う", "M" );
		HASH_MAP.put( "え", "e" );
		HASH_MAP.put( "お", "o" );		
		HASH_MAP.put( "か", "k a" );
		HASH_MAP.put( "き", "k i" );
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
		HASH_MAP.put( "み", "m i" );
		HASH_MAP.put( "む", "m M" );
		HASH_MAP.put( "め", "m e" );
		HASH_MAP.put( "も", "m o" );
		HASH_MAP.put( "や", "j a" );
		HASH_MAP.put( "ゆ", "j M" );
		HASH_MAP.put( "よ", "j o" );
		HASH_MAP.put( "ら", "4 a" );
		HASH_MAP.put( "り", "4 i" );
		HASH_MAP.put( "る", "4 M" );
		HASH_MAP.put( "れ", "4 e" );
		HASH_MAP.put( "ろ", "4 o" );					
		HASH_MAP.put( "わ", "w a" );
		HASH_MAP.put( "を", "w o" );
		HASH_MAP.put( "ん", "N\\" );
		HASH_MAP.put( "が", "g a" );
		HASH_MAP.put( "ぎ", "g i" );
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
		HASH_MAP.put( "び", "b i" );
		HASH_MAP.put( "ぶ", "b M" );
		HASH_MAP.put( "べ", "b e" );
		HASH_MAP.put( "ぼ", "b o" );
		HASH_MAP.put( "ぱ", "p a" );
		HASH_MAP.put( "ぴ", "p i" );
		HASH_MAP.put( "ぷ", "p M" );
		HASH_MAP.put( "ぺ", "p e" );
		HASH_MAP.put( "ぽ", "p o" );
	}

	/*
	 * 歌詞のバイト列を取得する
	 * @param String 歌詞
	 * @return byte[] 歌詞のバイト列
	 */	
	public byte[] getBytes( String s ) {
    	String str = HASH_MAP.get( s );
    	if ( str == null ) return null;
 		return str.getBytes();
 	}
 	
}
