package ydeb.hack.migatte.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.util.Log;

import ydeb.hack.migatte.http.dto.GnaviDto;

/**
 * ぐるなび情報のシリアライザ
 *
 */
public class GnaviDtoSerializer {

	/** TAG */
    private static final String TAG = "GnaviDtoSerializer";

	/** ぐるなび情報 */
	private GnaviDto gnavi;

	/**
	 * ぐるなび情報のシリアライザ
	 * @param gDto ぐるなび情報
	 * @return バイト配列
	 */
    public static byte[] serialize(GnaviDto gDto) 	{

    	byte[] ret = {};
      	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

    	ObjectOutputStream out;
    	try {
    		out = new ObjectOutputStream(byteOut);
    		out.writeObject(gDto);
    		ret = byteOut.toByteArray();

    	} catch (IOException e) {
			Log.d(TAG, e.getMessage());
    	}

    	return ret;
    }

	/**
	 * ぐるなび情報のデシリアライザ
	 * @param byteArray バイト配列
	 * @return ぐるなび情報
	 */
    public static GnaviDto deserialize(byte[] byteArray) {

		GnaviDto ret = null;
		ByteArrayInputStream byteIn = new ByteArrayInputStream( byteArray );

    	ObjectInputStream in;
	        try {
				in = new ObjectInputStream(byteIn);

				try {
					ret = (GnaviDto) in.readObject();

				} catch (ClassNotFoundException e) {
					Log.d(TAG, e.getMessage());
				}

	    	    in.close();

	        } catch (StreamCorruptedException e1) {
				Log.d(TAG, e1.getMessage());
			} catch (IOException e2) {
				Log.d(TAG, e2.getMessage());
			}

    	return ret;
    }

	/**
	 * ぐるなび情報の設定
	 * @param byteArray バイト配列
	 */
    public void setObject( byte[] byteArray ) {
		gnavi = deserialize( byteArray );
	}

	/**
	 * ぐるなび情報の設定
	 * @param gDto ぐるなび情報
	 */
    public void setObject(GnaviDto gDto) {
		gnavi = gDto;
	}

	/**
	 * キーワードを取得する
	 * @return キーワード 
	 */
	public String getSearchKeyword() {
		return gnavi.search_keyword.toString();
	}

	/**
	 * 緯度を取得する
	 * @return 緯度
	 */
	public String getSearchLatitude() {
		return gnavi.search_latitude.toString();
	}

	/**
	 * 経度を取得する
	 * @return 経度
	 */
	public String getSearchLongitude() {
		return gnavi.search_longitude.toString();
	}

	/**
	 * キーワードがあるか
	 * @return 真・偽
	 */
	public boolean isSearchKeyword() {
		if ( gnavi.search_keyword.toString().length() > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 緯度を取得する
	 * @return 緯度
	 */
	public String getLatitude() {
		return Double.toString( gnavi.latitude );
	}

	/**
	 * 経度を取得する
	 * @return 経度
	 */
	public String getLongitude() {
		return Double.toString( gnavi.longitude );
	}

	/**
     * <pre>
	 * 店舗URLを取得する
	 * 下記のように ?ak= というパラメータがついている
	 * http://r.gnavi.co.jp/g938004/?ak=xww...
     * </pre>
	 * @return 店舗URL
	 */
	public String getUrl() {
		return gnavi.url.toString();
	}

	/**
	 * 店舗URLを取得する
	 * ?ak= というパラメータを削除したもの
	 * http://r.gnavi.co.jp/g938004/
	 * @return 店舗URL
	 */
	public String getUrlShort() {
		String url = gnavi.url.toString();

		/* ? の手前までの文字列 */
		int i = url.indexOf("?");
		if ( i > 0 ) {
			String str = url.substring( 0, i );
			return str;
		}

		return url;
	}

	/**
	 * 店舗URLがあるか
	 * @return 店舗URL 
	 */
	public boolean isUrl() {
		String str = gnavi.url.toString();
		if ( ( str != null ) && !str.equals("") ) {
			return true;
		}
		return false;
	}

	/**
	 * 住所を取得する
	 * @return 住所L
	 */
	public String getAddress() {
		return gnavi.address.toString();
	}

	/**
	 * PR文を取得する
	 * @return PR文
	 */
	public String getPrLong() {
		return gnavi.pr.pr_long.toString();
	}

	/**
	 * 店舗名を取得する
	 * @return 店舗名 (表示用に整形されたもの)
	 */
	public String getNameView() {
		return replaceEnter( gnavi.name.toString() );
	}

	/**
	 * 店舗画像URLを取得する
	 * @return 店舗画像URL
	 */
	public String getShopImageUrl() {
		/* imageUrl があれば */
    	if (gnavi.imageUrl != null) {
    		return gnavi.imageUrl.shop_image1.toString();
		}
		return "";
	}

	/**
	 * 店舗画像URLがあるか
	 * @return 真・偽
	 */
	public boolean isShopImageUrl() {
   		String str = getShopImageUrl();
   		if ( ( str != null ) && !str.equals("") ) {
			return true;
		}
		return false;
	}

	/**
	 * ACTION_VIEW 用の地図問合せを取得する
	 * @return 地図問合せ 
	 */
	public String getActionViewMapQuery() {
		String str = 
			"geo:"  + getLatitude() 
			+ ","   + getLongitude()
			+ "?q=" + getNameView();
		return str;
	}

	/**
	 * 改行タグを「<br/>」をスペース文字に変換する
	 * @param s 文字列
	 * @return 変換された文字列
	 */
	public String replaceEnter(String s) {
		if (s != null && !"".equals(s)) {
			return s.replaceAll("<[bB][rR][ ]*(/>|>)", " ");
		}
		return s;
	}

}