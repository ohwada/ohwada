package ydeb.hack.migatte.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 指定されたURLのビットマップを取得する
 */
public class HttpClient {
	
	private static final String TAG = "HttpClient";

	/** コネクションタイムアウト 10秒 = 10000ミリ秒 */
	private static final int TIME_OUT = 10000;

	/**
	 * 指定されたURLのビットマップを取得する
	 * @param url URL
	 * @return ビットマップ
	 */
	public static Bitmap getImage(String url) {  
	    try {
	    	byte[] byteArray = getByteArrayFromURL(url);  
	    	return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	    } catch (Throwable t) {
	    	Log.e(TAG, "error", t);
	    }
	    return null;
	}

	/**
	 * 指定されたURLのバイト列を取得する
	 * @param strUrl URL
	 * @return バイト列
	 */
	private static byte[] getByteArrayFromURL(String strUrl) {  
	    byte[] byteArray = new byte[1024];  
	    byte[] result = null;  
	    HttpURLConnection con = null;  
	    InputStream in = null;  
	    ByteArrayOutputStream out = null;  
	    int size = 0;  
	    try {  
	        URL url = new URL(strUrl);  
	        con = (HttpURLConnection) url.openConnection();  
	        con.setRequestMethod("GET");

	        con.setConnectTimeout(TIME_OUT);
	        con.connect();  
	        in = con.getInputStream();  

	        out = new ByteArrayOutputStream();  
	        while ((size = in.read(byteArray)) != -1) {  
	            out.write(byteArray, 0, size);  
	        }  
	        result = out.toByteArray();  
	    } catch (Exception e) {  
	        Log.e(TAG, "", e);
	    } finally {  
	        try {  
	            if (con != null)  
	                con.disconnect();  
	            if (in != null)  
	                in.close();  
	            if (out != null)  
	                out.close();  
	        } catch (Exception e) {  
	            Log.e(TAG, "", e);
	        }  
	    }  
	    return result;  
	}  
}
