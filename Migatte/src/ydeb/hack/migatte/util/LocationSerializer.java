package ydeb.hack.migatte.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 位置情報のシリアライザ
 *
 */
public class LocationSerializer {

	/** TAG */
	private static final String TAG = "LocationSerializer";

	/** 連結・分離の文字 */
	private static final String DELMITA = ",";

	/**
	 * 位置情報のシリアライザ
	 * @param keyword   キーワード
	 * @param latitude  緯度
	 * @param longitude 経度
	 * @return 文字列
	 */
    public static String serialize(String keyword, String latitude, String longitude ) 	{

    	Log.d(TAG, "keyword=" + keyword);
       	Log.d(TAG, "latitude=" + latitude);
       	Log.d(TAG, "longitude=" + longitude);

		/* カンマ(,) で連結する */
		String str = keyword
			+ DELMITA + latitude
			+ DELMITA + longitude ;
		return str;
    }

	/**
	 * 位置情報のデシリアライザ
	 * @param str 文字列
	 * @return 位置情報のリスト
	 */
    public static List<String> deserialize(String str) {

		/* カンマ(,) で分離する */
		String arr[] = str.split(DELMITA,3);

		List<String> list = new ArrayList<String>();
		list.add( arr[0] );
		list.add( arr[1] );
		list.add( arr[2] );
		return list;
	}

}