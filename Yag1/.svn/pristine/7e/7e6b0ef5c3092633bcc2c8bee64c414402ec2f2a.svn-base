package jp.ohwada.android.yag1.task;

import java.util.Date;
import java.util.List;

import android.content.Context;

import jp.ohwada.android.yag1.Constant;

/**
 * EventList in PlaceList Async Task
 */
public class EventListPlaceAsync extends CommonAsyncTask {

	// object
	private Context mContext;
		
	// local variable	
	private List<String> mList = null;
	private Date mDateStart = null;
	private Date mDateEnd = null;
	
	/**
	 * === constructor ===
	 */			 
    public EventListPlaceAsync( Context context ) {
        super();
        mContext = context;
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
	 * execPreExecute()
	 */	
    protected void execPreExecute() {
		showDialog( mContext );
    	mResult = null;
    }
    
	/**
	 * execBackground
	 */	
	protected void execBackground() {
		mResult = mClient.executeQuery( getQuery() );
	}

	/**
	 * execPostExecute
	 */	 
    protected void execPostExecute() {
		hideDialog();
    }
	
    /**
	 * getQuery
	 * @param List<String> list
	 * @param Date first
	 * @param Date last
	 * @return String
	 */  
	private String getQuery() {
		if (( mList == null )||( mList.size() == 0 )) return "";		
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
		query += getFilterDate( mDateStart, mDateEnd );
		query += getFilterPlaceList( mList );
		query += "} ";
		query += "ORDER BY ASC(?dtstart) ";
		query += "LIMIT " + Constant.LIMIT_EVENT_LIST_PLACE;
		return query;	
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