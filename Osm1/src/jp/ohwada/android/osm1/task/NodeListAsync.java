package jp.ohwada.android.osm1.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ohwada.android.osm1.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * NodeList Async Task
 */
public class NodeListAsync extends AsyncTask<Void, Void, Boolean> {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private String TAG_SUB = "NodeListAsync";
	
	private final static String SCHEME = "http";
	private final static String ENCODING = "UTF-8";	
	private final static String AUTHORITY = "lgd.ohwada.jp";
	private final static String PATH = "/index.php";	
	private final static String OUTPUT = "json";
			
    // class object    	
	private DefaultHttpClient mClient = null;
   		
	// local variable
	private Map<String, String> mQueries = null;  
	private String mScheme = SCHEME;
	private String mAuthority = AUTHORITY;
	private String mPath = PATH;
			
	// local variable
	private String mResult = "";
	private String mLat = "";
	private String mLong = "";

	/**
	 * === constructor ===
	 */			 
    public NodeListAsync() {
        super();
        mClient = new DefaultHttpClient();
    }

	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
		// dummy	
    }

	/**
	 * === doInBackground ===
	 * @return Boolean
	 */	
    @Override
    protected Boolean doInBackground( Void... params ) {
        execBackground();
		return true;
    }

	/**
	 * === onProgressUpdate ===
	 */	
    @Override
    protected void onProgressUpdate( Void... params ) {
		// dummy
    }

	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
		// dummy
    }

	/**
	 * setGeo
	 * @return int lat 
	 * @return int lng 
	 */ 
	public void setGeo( int lat, int lng ) {
		mLat = e6ToString( lat );
		mLong = e6ToString( lng );
	}
	    		
	/**
	 * get Result
	 * @return String 
	 */  	
	public String getResult() {
		return mResult;
	}

	/**
	 * execBackground
	 */  	
	private void execBackground() {
		initQuery();
		addGetQuery( "lat", mLat );
		addGetQuery( "long", mLong );
		addGetQuery( "output",  OUTPUT );
		mResult =  excuteGetRequest( buildUrl() );
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
    private  void initQuery() {
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
		builder.scheme( mScheme );
		builder.encodedAuthority( mAuthority );
		builder.path( mPath );
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
	 * e6ToString
	 * @param int e6
	 * @return String
	 */ 
	private String e6ToString( int e6 ) {
		double d = (double) e6 / 1E6;
		String str = Double.toString( d );
		return str;
	}
	   		
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			    		   
	    		   
}