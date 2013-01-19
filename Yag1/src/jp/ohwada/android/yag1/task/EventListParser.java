package jp.ohwada.android.yag1.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * EventList JSON Parser
 */
public class EventListParser extends CommonParser {
    		
    /**
	 * === constarctor ===
	 */
    public EventListParser() {
        // dummy
    }
		
    /**
	 * parse EventList
	 * @param String str
	 * @return EventList
	 */     
	public EventList parse( String str ) {
		EventList list = new EventList();
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
				EventRecord r = new EventRecord();				
				String url =  getString1( obj, "event" );
				
				// check double url
				if ( !map.containsKey( url ) ) {
					map.put( url, true );
					r.event_url = url;
					r.event_label = getString1( obj, "event_label" );
					r.place_url = getString1( obj, "place_url" );
					r.place_label = getString1( obj, "place_label" );
					r.dtstart = getString1( obj, "dtstart" );
					r.dtend = getString1( obj, "dtend" );				
					list.add( r );
				}
			}
		}

		return list;
	}		
}