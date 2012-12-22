package jp.ohwada.android.lodsample2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.util.Log;

/**
 * Event Http Client 
 */
public class EventHttpClient  {

	private final static String TAG = Constant.TAG;
	private final static String TAG_SUB = "EventClient : ";
    private final static boolean D = Constant.DEBUG; 

	private final static String SCHEME = "http";
	private final static String ENCODING = "UTF-8";	
	private final static String ENCODED_AUTHORITY = "archive.yafjp.org";
	private final static String PATH = "/artsearch/test_newlod/inspection.php";	
	private final static String OUTPUT = "json";

    // class object    	
	private DefaultHttpClient mClient = null;
	private SimpleDateFormat mSdf = null;
	
	// local variable
	private Map<String, String> mQueries;  
				 
    /**
	 * === constarctor ===
	 */
    public EventHttpClient() {
    	mClient = new DefaultHttpClient();
		mSdf = new SimpleDateFormat( "yyyy-MM-dd" );
    }

    /**
	 * get from archive.yafjp.org
	 * @param Date first
	 * @param Date last
	 * @return String
	 */     
	public String getEvents( Date first, Date last ) {					
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		query += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
		query += "PREFIX schema: <http://schema.org/> ";
		query += "PREFIX dc: <http://purl.org/dc/elements/1.1/> ";
		query += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ";
		query += "PREFIX cal: <http://www.w3.org/2002/12/cal/icaltzd#> ";
		query += "PREFIX place: <http://fp.yafjp.org/terms/place#> ";
		query += "PREFIX event: <http://fp.yafjp.org/terms/event#> ";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "?place rdf:type place:Place ; ";
		query += "rdfs:label ?place_name ; ";
		query += "schema:address ?address ; ";
		query += "geo:lat ?lat ; ";
		query += "geo:long ?long . ";
		query += "?event event:location ?place ; ";
		query += "rdfs:label ?event_name ; ";
		query += "cal:dtstart ?dtstart ; ";
		query += "cal:dtend ?dtend ; ";
		query += "dc:abstract ?abstract . ";
		query += "FILTER (lang(?place_name) =\"ja\" ) ";
		query += getFilterDate( first, last );
		query += "} ";
		query += "ORDER BY ASC(?dtstart) ";
	
		initQuery();
		addGetQuery( "query", query );
		addGetQuery( "output",  OUTPUT );
		return get( buildUrl() );
	}

    /**
	 * getFilterDate
	 * @param Date first
	 * @param Date last
	 * @return String
	 */  
	private String getFilterDate( Date first, Date last ) {
		String strFirst = "\"" + mSdf.format( first ) + "T00:00:00+09:00\"^^xsd:dateTime"; 
		String strLast = "\"" + mSdf.format( last ) + "T00:00:00+09:00\"^^xsd:dateTime"; 
		String query = "FILTER ((?dtstart > " + strFirst + " && ";
		query += "?dtstart < " + strLast + " ) || ";
		query += "(?dtend > " + strFirst + " && ";
		query += "?dtend < " + strLast + " ) || ";
		query += "(?dtstart < " + strLast + " && ";
		query += "?dtend > " + strLast + " )) ";
		return query;
	}
	
	/**
	 * execute get metod
	 * @param String url
	 * @return String : result
	 */  		
    private String get( String url ) {
    	log_d( "get : " + url );		
		// get http response
		HttpGet request = new HttpGet( url );
		String result = null;
		try {
    		result = mClient.execute( request, new ResponseHandler<String>() {
        		public String handleResponse( HttpResponse response ) {
                	return parseResponse( response );
            	}
    		});    		
		} catch ( ClientProtocolException e ) {
		    e.printStackTrace();
		} catch ( IOException e ) {
		    e.printStackTrace();
		}
    	return result;	
	}

	/**
	 * init Query
	 */
    private void initQuery() {
		mQueries = new HashMap<String, String>();
    }

	/**
	 * add Get Query
	 * @param String key
	 * @param String value
	 * @return void
	 */    
    private void addGetQuery( String key, String value ) {
		mQueries.put( key, value );
    }

	/**
	 * build url
	 * @return String
	 */        	
    private String buildUrl() {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme( SCHEME );
		builder.encodedAuthority( ENCODED_AUTHORITY );
		builder.path( PATH );
		// build queries
		if ( mQueries.size() > 0 ) {
			for ( String key : mQueries.keySet() ) {
				builder.appendQueryParameter( key, mQueries.get( key ) );
			}
		}		
		return builder.build().toString();
	}

	/**
	 * parse Response
	 * @param HttpResponse response
	 * @return String : result
	 */ 
    private String parseResponse( HttpResponse response ) {
		String result = null;
		switch ( response.getStatusLine().getStatusCode() ) {
       		case HttpStatus.SC_OK:
       			try {
       				result = EntityUtils.toString( response.getEntity(), ENCODING );
       			} catch (ParseException e) {
       				e.printStackTrace();
       			} catch (IOException e) {
       				e.printStackTrace();
       			}
       			break;       			
            case HttpStatus.SC_NOT_FOUND:
            	log_d( "not found ");
				break;            
            default:
                log_d( "unknown error");
				break;
		}		
		log_d( "result : " + result );
        return result;    
	} 

	/**
	 * --- shutdown  ---
	 * @param none
	 * @return void
	 */ 
    public void shutdown() {
    	if ( mClient != null ) {
    		mClient.getConnectionManager().shutdown();
    	}
    	mClient = null;
    }
	    				
	/**
	 * write log
	 * @param String msg
	 * @return void
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}           
}