package jp.ohwada.android.osm2.task;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * NodeList JSON Parser
 */
public class NodeListParser extends CommonParser {
		
    /**
	 * === constarctor ===
	 */
    public NodeListParser() {
		TAG_SUB = "NodeListParser";
    }

	/**
	 * parse NodeList
	 * @param String str
	 * @return NodeList
	 */     
	public NodeList parse( String str ) {
		JSONObject obj_root = getObjectFromString( str );
		if ( obj_root == null ) {
			log_d( "can not parse " + str );
			return null;
		}
					
		JSONArray nodes = getArrayFromObject( obj_root, "nodes" );
		if (( nodes == null )||( nodes.length() == 0 )) {
			log_d( "can not parse " + str );
			return null;
		}

		NodeList list = new NodeList();
		String status = getString( obj_root, "status" );
		list.status = Integer.parseInt( status );

		// each node
		for ( int i=0; i<nodes.length(); i++ ) {
			JSONObject node = getObjectFromArray( nodes, i );		
			if ( node != null ) {
				NodeHash hash = getHashFromNode( node );
				if (( hash != null )&& hash.isValidNode() ) {
					list.addHash( hash );
				}
			}
		}
		return list;
	}

}