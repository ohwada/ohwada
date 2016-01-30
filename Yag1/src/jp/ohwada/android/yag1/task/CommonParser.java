package jp.ohwada.android.yag1.task;

import jp.ohwada.android.yag1.Constant;

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
	protected String TAG_SUB = "Parser";
	    		
    /**
	 * === constarctor ===
	 */
    public CommonParser() {
		// dummy
    }

    /**
	 * get Bindings
	 * @param String str
	 * @return JSONArray
	 */     
	protected JSONArray getBindings( String str ) {
		JSONObject obj_root = getObjectFromString( str );
		if ( obj_root == null ) {
			log_d( "can not parse " + str );
			return null;
		}
		JSONObject obj_results = getObjectFromObject( obj_root, "results" );
		if ( obj_results == null ) {
			log_d( "can not parse " + str );
			return null;
		}								
		JSONArray bindings = getArrayFromObject( obj_results, "bindings" );
		if (( bindings == null )||( bindings.length() == 0 )) {
			log_d( "can not parse " + str );
			return null;
		}		
		return bindings;
	}

    /**
	 * get String from json object
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */   
	protected String getStringValue( JSONObject obj, String name ) {
		JSONObject obj_name = getObjectFromObject( obj, name );
		if ( obj_name == null ) return "";
		return getStringFromObject( obj_name, "value" );
	}

    /**
	 * get String from json object
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */ 
	protected String getStringZeroValue( JSONObject obj, String name ) {
		JSONArray obj_names = getArrayFromObject( obj, name );
		if (( obj_names == null )||( obj_names.length() == 0 )) return "";
		JSONObject obj_zero = getObjectFromArray( obj_names, 0 );
		return getStringFromObject( obj_zero, "value" );
	}

    /**
	 * parseBnodeString
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @param String bnode
	 * @param String name
	 * @return String
	 */ 
	protected String parseBnodeString( JSONObject obj_root, JSONObject obj, String bnode, String name ) {
		JSONObject obj_bnode = parseBnode( obj_root, obj, bnode );
		if ( obj_bnode == null )  return "";
		return getStringZeroValue( obj_bnode, name );			
	}
	
    /**
	 * parseBnode
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @param String name
	 * @return JSONObject
	 */ 
	protected JSONObject parseBnode( JSONObject obj_root, JSONObject obj, String name ) {
		String node = getStringZeroValue( obj, name );
		if (( node == null ) || node.equals("") ) return null;
		return getObjectFromObject( obj_root, node );
	}
	
    /**
	 * get Object From String
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
	 * get Object From Object
	 * @param JSONObject obj
	 * @param String str
	 * @return JSONObject
	 */  
	protected JSONObject getObjectFromObject( JSONObject obj, String str ) {
		JSONObject obj_str = null;
		try {
			obj_str = obj.getJSONObject( str );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj_str;
	}

    /**
	 * get Object From Array
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
	 * get Array From Object
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
	 * get String from json object
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */ 
	protected String getStringFromObject( JSONObject obj, String name ) {
		String str = "";
		try {
			str = obj.getString( name );
			str = str.trim();
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return str;
	}

 	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	} 		
  			
}