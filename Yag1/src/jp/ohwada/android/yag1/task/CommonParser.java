package jp.ohwada.android.yag1.task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.ohwada.android.yag1.Constant;

/**
 * Common JSON Parser
 */
public class CommonParser {

	// dubug
    private final static boolean D = Constant.DEBUG; 
	    		
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
		// parse bindings		
		JSONArray bindings = null;
		try {
			JSONObject obj_root = new JSONObject( str );
			if ( obj_root != null ) {
				JSONObject obj_results = obj_root.getJSONObject( "results" );
				if ( obj_results != null ) {
					bindings = obj_results.getJSONArray("bindings");
				}	
			}	
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return bindings;
	}

    /**
	 * get Binding
	 * @param JSONArray bindings
	 * @param int i 
	 * @return JSONObject
	 */ 
	protected JSONObject getBinding( JSONArray bindings, int i ) {
		// parse binding
		JSONObject obj = null;
		try {
			obj = bindings.getJSONObject( i );
		} catch (JSONException e) {
			if (D) e.printStackTrace();
		}
		return obj;
	}
					
    /**
	 * get String from json object
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */   
	protected String getString1( JSONObject obj, String name ) {
		String str = "";
		try {
			JSONObject obj_name = obj.getJSONObject( name );
			if ( obj_name != null ) {
				str = obj_name.getString( "value" );
				str = str.trim();
			}
		} catch ( JSONException e ) {
			if (D) e.printStackTrace();
		}
		return str;
	}

    /**
	 * get String from index of json array
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */ 
	protected String getString2( JSONObject obj, String name ) {
		String str = "";
		try {
			JSONArray obj_names = obj.getJSONArray( name );
			if (( obj_names != null )&&( obj_names.length() > 0 )) {
				JSONObject obj2 = obj_names.getJSONObject( 0 );
				if ( obj2 != null ) {
					str = obj2.getString( "value" );
					str = str.trim();
				}
			}
		} catch ( JSONException e ) {
			if (D) e.printStackTrace();
		}
		return str;
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
		return getString2( obj_bnode, name );			
	}
	
    /**
	 * parseBnode
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @param String name
	 * @return JSONObject
	 */ 
	protected JSONObject parseBnode( JSONObject obj_root, JSONObject obj, String name ) {
		String node = getString2( obj, name );
		if (( node == null ) || node.equals("") ) return null;
		JSONObject obj_node = null;
		try {
			obj_node = obj_root.getJSONObject( node );
		} catch ( JSONException e ) {
			if (D) e.printStackTrace();
		}
		return obj_node;
	}
  			
}