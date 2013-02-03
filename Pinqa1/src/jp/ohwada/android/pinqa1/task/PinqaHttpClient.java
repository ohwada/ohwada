package jp.ohwada.android.pinqa1.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ohwada.android.pinqa1.Constant;
import jp.ohwada.android.pinqa1.Username;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.util.Log;

/**
 * Pinqa Http Client
 */
public class PinqaHttpClient {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	protected String TAG_SUB = "HttpCilent";
	
	// constant
	private final static String ENCODING = "UTF-8";	
	private final static String OUTPUT = "json";
	private final static int PORT = 80;
	private final static String SCHEME = "http";
	private final static String AUTHORITY = "lod.pinqa.com";
	private final static String PATH = "/sparql";
			
    // class object    	
	private DefaultHttpClient mClient = null;
   		
	// local variable
	private Map<String, String> mQueries = null;  
	protected String mResult = "";
	
	/**
	 * === constructor ===
	 */			 
    public PinqaHttpClient() {
        mClient = new DefaultHttpClient();
        setCredentials();
    }

	/**
	 * execute
	 * @param String query
	 * @return String 
	 */  	
	public String execute( String query ) {
		initQuery();
		addGetQuery( "query", query );
		addGetQuery( "output", OUTPUT );
		String url = buildUrl();
		return excuteGetRequest( url );
	}

	/**
	 * setCredentials
	 */  
    private void setCredentials() {
 		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( Username.USERNAME, Username.PASSWORD );
		AuthScope scope = new AuthScope( AUTHORITY, PORT );
		mClient.getCredentialsProvider().setCredentials( scope, credentials );
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
		switch ( response.getStatusLine().getStatusCode() ) {
       		case HttpStatus.SC_OK:
       			try {
       				result = EntityUtils.toString( response.getEntity(), ENCODING );
       			} catch (ParseException e) {
       				if (D) e.printStackTrace();
       			} catch (IOException e) {
       				if (D) e.printStackTrace();
       			}
       			break;       			
            case HttpStatus.SC_NOT_FOUND:
            	log_d( "not found ");
				break;            
            default:
                log_d( "unknown error");
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
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			    		   
}