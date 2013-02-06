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
			JSONObject obj = getObjectFromArray( bindings, i );		
			if ( obj != null ) {
				PlaceRecord r = new PlaceRecord();				
				String url =  getStringValue( obj, "place" );
				
				// check double url
				if ( !map.containsKey( url ) ) {
					map.put( url, true );
					r.url = url;
					r.label = getStringValue( obj, "label" );
					r.reading = getStringValue( obj, "hrkt" );
					r.address = getStringValue( obj, "address" );
					r.telephone = getStringValue( obj, "telephone" );			
					r.homepage = getStringValue( obj, "homepage" );
					r.dbpedia = getStringValue( obj, "same_as" );
					r.is_part_of = getStringValue( obj, "is_part_of" );
					r.lat = getStringValue( obj, "lat" );
					r.lng = getStringValue( obj, "long" );				
					list.add( r );
				}
			}
		}
		list.initHash();
		return list;
	}

}