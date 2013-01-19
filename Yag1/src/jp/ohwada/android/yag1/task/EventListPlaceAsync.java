package jp.ohwada.android.yag1.task;

import java.util.Date;
import java.util.List;

/**
 * EventList in PlaceList Async Task
 */
public class EventListPlaceAsync extends CommonAsyncTask {

	private List<String> mList = null;

	/**
	 * === constructor ===
	 */			 
    public EventListPlaceAsync() {
        super();
        TAG_SUB = "EventListPlaceAsync";
    }

	/**
	 * setUrlDate
	 * @param List<String> list
	 * @param Date start
	 * @param Date end
	 */  	
	public void setListDate( List<String> list, Date start, Date end ) {
		mList = list;
		mDateStart = start;
		mDateEnd = end;
	}
	
	/**
	 * execBackground
	 */  	
	protected void execBackground() {
		mResult = getHttp( mList, mDateStart, mDateEnd );
	}
	
    /**
	 * get PlaceEventList
	 * @param List<String> list
	 * @param Date first
	 * @param Date last
	 * @return String
	 */  
	private String getHttp( List<String> list, Date first, Date last ) {
		if (( list == null )||( list.size() == 0 )) return "";		
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		query += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
		query += "PREFIX cal: <http://www.w3.org/2002/12/cal/icaltzd#> ";
		query += "PREFIX place: <http://fp.yafjp.org/terms/place#> ";
		query += "PREFIX event: <http://fp.yafjp.org/terms/event#> ";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "?event rdf:type event:Event ; "; 
		query += "rdfs:label ?event_label ; ";
		query += "event:location ?place_url ; ";
		query += "cal:location ?place_label ; ";
		query += "cal:dtstart ?dtstart ; ";
		query += "cal:dtend ?dtend . ";
		query += getFilterDate( first, last );
		query += getFilterPlaceList( list );
		query += "} ";
		query += "ORDER BY ASC(?dtstart) ";
		query += "LIMIT " + LIMIT_EVENT;
		return getResult( query );	
	}	

    /**
	 * getFilterPlaceList
	 * @param List<String> list
	 * @return String
	 */ 
	private String getFilterPlaceList( List<String> list ) {
		if (( list == null )||( list.size() == 0 )) return "";		
		String str = "FILTER ( ";
		for ( int i=0; i<list.size(); i++ ) {
			if ( i != 0 ) {
				str += "|| ";
			}
			str += "?place_url=\"" + list.get(i) + "\" ";
		}
		str += ") ";	
		return str;
	}			    		   
}