package jp.ohwada.android.yag1.task;

import org.json.JSONObject;

/**
 * Event JSON Parser
 */
public class EventParser extends CommonParser {
    		
    /**
	 * === constarctor ===
	 */
    public EventParser() {
        // dummy
    }

    /**
	 * parse Event
	 * @param String str
	 * @return String str
	 */     
	public EventRecord parse( String url, String str ) {
		JSONObject obj_root = getObjectFromString( str );
		if ( obj_root == null ) return null;
		JSONObject obj = getObjectFromObject( obj_root, url );
		if ( obj == null ) return null;
									
		EventRecord r = new EventRecord();
		r.event_url = url;
		r.event_label = getStringZeroValue( obj, "http://www.w3.org/2000/01/rdf-schema#label" );
		r.place_label = getStringZeroValue( obj, "http://www.w3.org/2002/12/cal/icaltzd#location" );
		r.place_url = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#location" );
		r.created = getStringZeroValue( obj, "http://purl.org/dc/terms/created" );
		r.modified = getStringZeroValue( obj, "http://purl.org/dc/terms/modified" );
		r.dtstart = getStringZeroValue( obj, "http://www.w3.org/2002/12/cal/icaltzd#dtstart" );
		r.dtend = getStringZeroValue( obj, "http://www.w3.org/2002/12/cal/icaltzd#dtend" );
		r.event_abstract = getStringZeroValue( obj, "http://purl.org/dc/elements/1.1/abstract" );
		r.category = getStringZeroValue( obj, "http://www.w3.org/2002/12/cal/icaltzd#categories" );
		r.schedule = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#scheduleNote" );
		r.fee = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#fee" );		
		r.care = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#day-care" );
		r.apinfo = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#apinfo" );
		r.apstart = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#apstart" );
		r.apend = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#apend" );	
		r.homepage = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#homepage" );
		r.image_url = getStringZeroValue( obj, "http://fp.yafjp.org/terms/event#stillImage" );
		r.participant = parseEventParticipant( obj_root, obj );
		r.status = parseEventStatus( obj_root, obj );		
		EventContact contact = parseEventContact( obj_root, obj );
		if ( contact != null ) {			
			r.contact =  contact.name;
			r.phone = contact.phone;
		}	
		return r;
	}

    /**
	 * parseEventContact
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @return EventContact
	 */ 
	private EventContact parseEventContact( JSONObject obj_root, JSONObject obj ) {
		JSONObject obj_bnode = parseBnode( obj_root, obj, "http://fp.yafjp.org/terms/event#contact" );
		if ( obj_bnode == null )  return null;
		EventContact r = new EventContact();			
		r.name = getStringZeroValue( obj_bnode, "http://xmlns.com/foaf/0.1/name" );
		r.phone = getStringZeroValue( obj_bnode, "http://xmlns.com/foaf/0.1/phone" );	
		return r;
	}

    /**
	 * parseEventParticipant
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @return String
	 */ 
	private String parseEventParticipant( JSONObject obj_root, JSONObject obj ) {
		return parseBnodeString( 
			obj_root, obj, 
			"http://fp.yafjp.org/terms/event#participant", 
			"http://xmlns.com/foaf/0.1/name"  );
	}

    /**
	 * parseEventStatus
	 * @param JSONObject obj_root
	 * @param JSONObject obj
	 * @return String
	 */ 
	private String parseEventStatus( JSONObject obj_root, JSONObject obj ) {
		return parseBnodeString( 
			obj_root, obj, 
			"http://fp.yafjp.org/terms/event#status", 
			"http://purl.org/dc/elements/1.1/title" );
	}
	
    /**
	 * === class EventContact ===
	 */ 
    private class EventContact {
		public String name = "";
		public String phone = "";
	}; 
			
}