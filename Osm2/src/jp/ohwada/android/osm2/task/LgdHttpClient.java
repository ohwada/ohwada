package jp.ohwada.android.osm2.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ohwada.android.osm2.Constant;

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
 * LGD Http Client
 */
public class LgdHttpClient {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	protected String TAG_SUB = "LgdHttpCilent";
	
	// constant
	private final static String ENCODING = "UTF-8";	
	private final static String SCHEME = "http";
	private final static String AUTHORITY = Constant.API_AUTHORITY;
	private final static String PATH = Constant.API_PATH;

    // constructor   	
	private DefaultHttpClient mClient = null;
   		
	// local variable
	private Map<String, String> mQueries = null;  
	protected String mResult = "";
	
	/**
	 * === constructor ===
	 */			 
    public LgdHttpClient() {
        mClient = new DefaultHttpClient();
    }

	/**
	 * execute
	 * @param int lat
	 * @param int lng
	 * @return String 
	 */  	
	public String executeList( int lat, int lng ) {
		initQuery();
		addGetQuery( "type", "list" );
		addGetQuery( "lat", e6ToString( lat ) );
		addGetQuery( "long", e6ToString( lng ) );
		String url = buildUrl();
		return excuteGetRequest( url );
	}

	/**
	 * execute
	 * @param int node
	 * @return String 
	 */
	public String executeNode( int node ) {
		initQuery();
		addGetQuery( "type", "node" );
		addGetQuery( "node", Integer.toString( node ) );
		String url = buildUrl();
		return excuteGetRequest( url );
	}
		
	/**
	 * execute get metod
	 * @param String url
	 * @return String : result
	 */  		
    private String excuteGetRequest( String url ) {
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
		    if (D) e.printStackTrace();
		} catch ( IOException e ) {
		    if (D) e.printStackTrace();
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
		builder.encodedAuthority( AUTHORITY );
		builder.path( PATH );
		// build queries
		if ( mQueries.size() > 0 ) {
			for ( String key : mQueries.keySet() ) {
				builder.appendQueryParameter( key, mQueries.get( key ) );
			}
		}
		String url = builder.build().toString();	
		return url;
	}

	/**
	 * parse Response
	 * @param HttpResponse response
	 * @return String : result
	 */ 
    private String parseResponse( HttpResponse response ) {
		String result = null;
		int code = response.getStatusLine().getStatusCode();
		switch ( code ) {
       		case HttpStatus.SC_OK:
       			try {
       				result = EntityUtils.toString( response.getEntity(), ENCODING );
       			} catch (ParseException e) {
       				if (D) e.printStackTrace();
       			} catch (IOException e) {
       				if (D) e.printStackTrace();
       			}
       			break;      
            default:
                log_d( "error code: " + code );
				break;
		}		
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
	 * e6ToString
	 * @param int e6
	 * @return String
	 */ 
	private String e6ToString( int e6 ) {
		double d = (double) e6 / 1E6;
		return Double.toString( d );
	}
		   		
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			    		   
}