package ydeb.hack.migatte.util;

/**
 * 改行タグを「<br/>」をスペース文字に変換する
 */
public class StringUtils {

	/**
	 * 改行タグを「<br/>」をスペース文字に変換する
	 * @param s 文字列
	 * @return 変換された文字列
	 */
	public static String replaceEnter(String s) {
		if (s != null && !"".equals(s)) {
			return s.replaceAll("<[bB][rR][ ]*(/>|>)", " ");
		}
		return s;
	}
	
}
