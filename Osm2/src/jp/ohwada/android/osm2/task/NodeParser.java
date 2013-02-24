package jp.ohwada.android.osm2.task;

import org.json.JSONObject;

/**
 * Node JSON Parser
 */
public class NodeParser extends CommonParser {
		
    /**
	 * === constarctor ===
	 */
    public NodeParser() {
		TAG_SUB = "NodeParser";
    }

	/**
	 * parse NodeHash
	 * @param String str
	 * @return NodeHash
	 */     
	public NodeHash parse( String str ) {

		JSONObject obj_root = getObjectFromString( str );
		if ( obj_root == null ) {
			log_d( "can not parse " + str );
			return null;
		}
		
		JSONObject node = getObjectFromObject( obj_root, "node" );
		if ( node == null ) {
			log_d( "can not parse " + str );
			return null;
		}

		return getHashFromNode( node );
	}

}