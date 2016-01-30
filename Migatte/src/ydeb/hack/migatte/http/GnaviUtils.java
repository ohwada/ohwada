package ydeb.hack.migatte.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import ydeb.hack.migatte.http.dto.GnaviDto;
import ydeb.hack.migatte.http.handler.SearchHandler;
import ydeb.hack.migatte.http.response.Response;
import ydeb.hack.migatte.util.DebugUtils;
import android.util.Log;

/**
 * <pre>
 * キーワード、緯度、経度より、ぐるなび情報のリストを取得し、ランダムに１つ選ぶ。
 * http://api.gnavi.co.jp/api/manual.htm
 * </pre>
 */
public class GnaviUtils {
	private static final String TAG = "GnaviUtils";
	private static final String URI = "http://api.gnavi.co.jp/ver1/RestSearchAPI/"; 
	public static String key;
	
	/**
	 * <pre>
	 * RestfulClient と SearchHandler により、ぐるなび情報のリストを取得する
	 * ランダムに１つ選ぶ
	 * </pre>
	 * @param keyword キーワード
	 * @param latitude　緯度
	 * @param longitude　経度
	 * @return ぐるなび情報
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ClientProtocolException 
	 */
	public static GnaviDto getGnavi(String keyword, String latitude, String longitude)
	throws ClientProtocolException, IllegalStateException, IOException, 
	SAXException, ParserConfigurationException {

		SearchHandler handler = new SearchHandler();
		RestfulClient.Get(URI, createRequestMap(keyword, latitude, longitude), handler);
		Response response =  handler.getResponse();
		GnaviDto gDto = shuffle(response.resultRest);
		if ( gDto != null ) {
			gDto.search_keyword.append(keyword);
			gDto.search_latitude.append(latitude);
			gDto.search_longitude.append(longitude);
		}
		return gDto;
	}
	
	/**
	 * ぐるなび情報のリストからランダムに１つ選ぶ
	 * @param lists ぐるなび情報のリスト
	 * @return ぐるなび情報
	 */
	private static GnaviDto shuffle(ArrayList<GnaviDto> lists) {
		if (lists != null && lists.size() > 0) {
			Random rnd = new Random();
			int index = rnd.nextInt(lists.size());
			return lists.get(index);
		}
		return null;
	}

	/**
	 * ぐるなび情報のHashMapを生成する
	 * @param keyword キーワード
	 * @param latitude　緯度
	 * @param longitude　経度
	 * @return ぐるなび情報のHashMap
	 */
	private static HashMap<String,String> createRequestMap(
			String keyword, String latitude, String longitude) {
		HashMap<String,String> requestMap = new HashMap<String,String>();

		// アクセスキー
		requestMap.put("keyid", key);
		// レスポンス取得件数
		requestMap.put("hit_per_page", "50");

		if (DebugUtils.isDebug()) {		
			Log.d(TAG, "keyword:" + keyword);
			Log.d(TAG, "latitude:" + latitude);
			Log.d(TAG, "longitude:" + longitude);
		}

		/* 緯度・経度があれば、指定する */
		if (latitude != null && !"".equals(latitude)) {
			// 緯度
			requestMap.put("latitude", latitude);
			// 経度
			requestMap.put("longitude", longitude);
			// 入力する緯度/経度の測地系のタイプを指定
			// 2:世界測地系
			requestMap.put("input_coordinates_mode", "2");
			// レスポンスに含まれる緯度/経度の測地系
			requestMap.put("coordinates_mode", "2");
			// 距離 3:1000m
			requestMap.put("range", "3");
		}

		/* キーワードがあれば、指定する */
		if ( keyword.length() > 0 ) {
			// フリーワード検索
			requestMap.put("freeword", keyword);
			// 1: AND検索
			requestMap.put("freeword_condition", "1");
		}

		return requestMap;
	}
}
