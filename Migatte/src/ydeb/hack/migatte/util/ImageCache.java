package ydeb.hack.migatte.util;

import java.util.LinkedHashMap;

import android.graphics.Bitmap;

/**
 * 画像のキャッシュを操作する
 */
public class ImageCache<K, V> extends LinkedHashMap<K, V> {

	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;

	private static ImageCache<String, Bitmap> imageMap = new ImageCache<String,Bitmap>();

	/** キャッシュの上限数 */
	private static final int MAX_CACHE = 10;

	/**
	 * uriをキーとしキャッシュからビットマップを取得する
	 * @param uri URI
	 * @return ビットマップ
	 */
	public static Bitmap getImage(String uri) {
		if (imageMap.containsKey(uri)) {
			return imageMap.get(uri);
		}
		return null;
	}

	/**
	 * uriに対応するキャッシュがあるか
	 * @param key URI
	 * @return あり・なし
	 */	
	public static boolean isKey(String key) {
		return imageMap.containsKey(key);
	}

	/**
	 * uriをキーとしてビットマップをキャッシュに保存する
	 * @param uri URI
	 */
	public static void putImage(String uri, Bitmap image) {
		imageMap.put(uri, image);
	}

	/**
	 * <pre>
	 * キャッシュの数が 10 を越えたら、真を返す
	 * そのとき一番古いキャッシュが削除される
	 * </pre>
	 * @param eldest もっとも前にキャッシュに挿入されたエントリ
	 * @return 真・偽
	 */
	@Override
	protected boolean removeEldestEntry(Entry<K,V> eldest) {
		return MAX_CACHE < size();
	}
}
