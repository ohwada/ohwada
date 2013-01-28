package jp.ohwada.android.osm1.task;

import jp.ohwada.android.osm1.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * NodeList JSON Parser
 */
public class NodeListParser {

	// dubug
    private final static boolean D = Constant.DEBUG; 
	private static final String TAG = Constant.TAG;
	private String TAG_SUB = "NodeListParser";
		
    /**
	 * === constarctor ===
	 */
    public NodeListParser() {
		// dummy
    }

	/**
	 * parse NodeList
	 * @param String str
	 * @return NodeList
	 */     
	public NodeList parse( String str ) {
		NodeList list = new NodeList();

		JSONObject obj_root = getObjectFromString( str );
		if ( obj_root == null ) {
			log_d( "can not parse " + str );
			return null;
		}
		
		JSONArray nodes = getArray( obj_root, "nodes" );
		if (( nodes == null )||( nodes.length() == 0 )) {
			log_d( "can not parse " + str );
			return null;
		}

		// each binding
		for ( int i=0; i<nodes.length(); i++ ) {

			// parse binding
			JSONObject obj = getObjFromArray( nodes, i );		
			if ( obj != null ) {
				NodeRecord r = new NodeRecord();				
				r.node =  getString( obj, "lgdo:Node" );
				r.label = getString( obj, "rdf:label" );
				r.label_ja = getString( obj, "rdf:label:ja" );
				r.direct_type = getString( obj, "lgdo:directType" );
				r.direct_label_ja = getString( obj, "lgdo:directType:label_ja" );
				r.geometry = getString( obj, "geo:geometry" );	
				r.lat = getString( obj, "geo:lat" );	
				r.lng = getString( obj, "geo:long" );				
				list.add( r );
			}
		}
		return list;
	}

    /**
	 * getFromString
	 * @param String str
	 * @return JSONObject
	 */     
	private JSONObject getObjectFromString( String str ) {	
		JSONObject obj = null;
		try {
			obj = new JSONObject( str );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj;
	}

	/**
	 * getArray
	 * @param JSONObject obj
	 * @param String str
	 * @return JSONArray
	 */ 	
	private JSONArray getArray( JSONObject obj, String str ) {
		JSONArray array = null;
		try {
			array = obj.getJSONArray( str );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return array;
	}
		
    /**
	 * getObjFromArray
	 * @param JSONArray obj_array
	 * @param int i 
	 * @return JSONObject
	 */ 
	private JSONObject getObjFromArray( JSONArray obj_array, int i ) {
		// parse binding
		JSONObject obj = null;
		try {
			obj = obj_array.getJSONObject( i );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj;
	}

    /**
	 * getString
	 * @param JSONObject obj
	 * @param String name 
	 * @return String
	 */ 
    private String getString( JSONObject obj, String name ) {
		String str = "";
		try {
			str = obj.getString( name );
		} catch ( JSONException e ) {
			if (D) e.printStackTrace();
		}
		return str;
	}

 	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	} 		

}