package jp.ohwada.android.yag1.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * PlaceList JSON Parser
 */
public class PlaceListParser extends CommonParser {
		    		
    /**
	 * === constarctor ===
	 */
    public PlaceListParser() {
        // dummy
    }

    /**
	 * parse PlaceList
	 * @param String str
	 * @return PlaceList
	 */     
	public PlaceList parse( String str ) {
		PlaceList list = new PlaceList();
		Map<String, Boolean> map = new HashMap<String, Boolean>(); 

		// parse bindings		
		JSONArray bindings = getBindings( str );
		if (( bindings == null )||( bindings.length() == 0 )) {
			// can not parse
			return null;
		}

		// each binding
		for ( int i=0; i<bindings.length(); i++ ) {

			// parse binding
			JSONObject obj = getBinding( bindings, i );		
			if ( obj != null ) {
				PlaceRecord r = new PlaceRecord();				
				String url =  getString1( obj, "place" );
				
				// check double url
				if ( !map.containsKey( url ) ) {
					map.put( url, true );
					r.url = url;
					r.label = getString1( obj, "label" );
					r.reading = getString1( obj, "hrkt" );
					r.address = getString1( obj, "address" );
					r.telephone = getString1( obj, "telephone" );								
					r.homepage = getString1( obj, "homepage" );
					r.dbpedia = getString1( obj, "same_as" );
					r.is_part_of = getString1( obj, "is_part_of" );
					r.lat = getString1( obj, "lat" );
					r.lng = getString1( obj, "long" );				
					list.add( r );
				}
			}
		}
		list.initHash();
		return list;
	}

}