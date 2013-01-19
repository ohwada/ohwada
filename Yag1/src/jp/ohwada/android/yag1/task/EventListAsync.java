package jp.ohwada.android.yag1.task;

import java.util.Date;

/**
 * EventList Async Task
 */
public class EventListAsync extends CommonAsyncTask {
	
	/**
	 * === constructor ===
	 */			 
    public EventListAsync() {
        super();
        TAG_SUB = "EventListAsync";
    }

	/**
	 * setDate
	 * @param Date today
	 */  	
	public void setDate( Date start, Date end ) {
		mDateStart = start;
		mDateEnd = end;
	}
		
	/**
	 * getEventList
	 */  	
	protected void execBackground() {
		mResult = getHttp( mDateStart, mDateEnd );
	}
	
	/**
	 * get EventList
	 * @param Date first
	 * @param Date last
	 * @return String
	 */     
	private String getHttp( Date first, Date last ) {					
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		query += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
		query += "PREFIX cal: <http://www.w3.org/2002/12/cal/icaltzd#> ";
		query += "PREFIX event: <http://fp.yafjp.org/terms/event#> ";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "?event rdf:type event:Event ; ";
		query += "rdfs:label ?event_label ; ";
		query += "cal:location ?place_label ; ";
		query += "event:location ?place_url ; ";
		query += "cal:dtstart ?dtstart ; ";
		query += "cal:dtend ?dtend . ";
		query += getFilterDate( first, last );
		query += "} ";
		query += "ORDER BY ASC(?dtstart) ";
		return getResult( query );
	}  
}