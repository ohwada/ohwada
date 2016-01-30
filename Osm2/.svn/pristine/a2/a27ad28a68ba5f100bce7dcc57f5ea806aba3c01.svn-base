package jp.ohwada.android.osm2.task;

import java.util.Iterator;

import jp.ohwada.android.osm2.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Common JSON Parser
 */
public class CommonParser {

	// dubug
    private final static boolean D = Constant.DEBUG; 
	private static final String TAG = Constant.TAG;
	protected String TAG_SUB = "CommonParser";
		
    /**
	 * === constarctor ===
	 */
    public CommonParser() {
		// dummy
    }

    /**
	 * getFromString
	 * @param String str
	 * @return JSONObject
	 */     
    protected JSONObject getObjectFromString( String str ) {	
		JSONObject obj = null;
		try {
			obj = new JSONObject( str );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj;
	}

	/**
	 * getArrayFromObject
	 * @param JSONObject obj
	 * @param String str
	 * @return JSONArray
	 */ 	
    protected JSONArray getArrayFromObject( JSONObject obj, String str ) {
		JSONArray array = null;
		try {
			array = obj.getJSONArray( str );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return array;
	}
		
    /**
	 * getObjectFromArray
	 * @param JSONArray obj_array
	 * @param int i 
	 * @return JSONObject
	 */ 
    protected JSONObject getObjectFromArray( JSONArray obj_array, int i ) {
		JSONObject obj = null;
		try {
			obj = obj_array.getJSONObject( i );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj;
	}

    /**
	 * getObjectFromObject
	 * @param JSONObject obj
	 * @param String name
	 * @return JSONObject
	 */ 
    protected JSONObject getObjectFromObject( JSONObject obj, String name ) {
		JSONObject obj_name = null;
		try {
			obj_name = obj.getJSONObject( name );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj_name;
	}
	
    /**
	 * getString
	 * @param JSONObject obj
	 * @param String name 
	 * @return String
	 */ 
    protected String getString( JSONObject obj, String name ) {
		String str = "";
		try {
			str = obj.getString( name );
		} catch ( JSONException e ) {
			if (D) e.printStackTrace();
		}
		return str;
	}

    /**
	 * getHashFromNode
	 * @param JSONObject obj
	 * @return NodeHash
	 */ 
	protected NodeHash getHashFromNode( JSONObject node ) {
		NodeHash hash = new NodeHash();								
		Iterator<?> it = node.keys();
		while ( it.hasNext() ) {	
			String key = (String) it.next();
			String value = getString( node, key );
       		hash.put( key, value );
		} 
		hash.setAddtional();
		return hash;
	}
		
 	/**
	 * write log
	 * @param String msg
	 */ 
    protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	} 		

}