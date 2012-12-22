package jp.ohwada.android.lodsample1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Event JSON Parser
 */
public class EventParser {
		
    /**
	 * === constarctor ===
	 */
    public EventParser() {
		// dummy
    }

    /**
	 * parse
	 * @param String str
	 * @return ArrayList<EventRecord>
	 */     
	public ArrayList<EventRecord> parse( String str ) {
		ArrayList<EventRecord> list = new ArrayList<EventRecord>();
		Map<String, Boolean> map = new HashMap<String, Boolean>(); 

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
			e.printStackTrace();
		}

		// each binding
		for ( int i=0; i<bindings.length(); i++ ) {
		
			// parse binding
			JSONObject obj = null;
			try {
				obj = bindings.getJSONObject( i );
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if ( obj != null ) {
				EventRecord r = new EventRecord();				
				String url =  getString( obj, "event" );
				
				// check double url
				if ( !map.containsKey( url ) ) {
					map.put( url, true );
					String dtstart = getString( obj, "dtstart" );
					String dtend = getString( obj, "dtend" );
					r.dtstart = dtstart;
					r.dtend = dtend;
					r.date_start = parseDate( dtstart );
					if ( !dtstart.equals( dtend ) ) {
						r.date_end = parseDate( dtend );
						r.date_bar = "-";
					}
					r.event_url = url;
					r.event_name = getString( obj, "event_name" );
					r.event_abstract = getString( obj, "abstract" );	
					r.place_url = getString( obj, "place" );
					r.place_name = getString( obj, "place_name" );
					r.address = getString( obj, "address" );
					r.place_lat = getString( obj, "lat" );
					r.place_long = getString( obj, "long" );
					list.add( r );
				}
			}
		}

		return list;
	}

    /**
	 * getString
	 * @param JSONObject obj
	 * @param String name
	 * @return String
	 */   
	private String getString( JSONObject obj, String name ) {
		String str = null;
		// parse name:value
		try {
			JSONObject obj_name = obj.getJSONObject( name );
			if ( obj_name != null ) {
				str = obj_name.getString( "value" );
			}
		} catch ( JSONException e ) {
			e.printStackTrace();
		}
		return str;
	}

    /**
	 * parseDate(
	 * @param String date
	 * @return String
	 */ 	
	private String parseDate( String date ) {
		String str = "";
		if ( !"".equals( date ) ) {
			String[] strAry = date.split( "T" );
			str = strAry[0];
		}
		return str;
	}
			
}