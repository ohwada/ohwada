package jp.ohwada.android.yag1.task;

import android.os.AsyncTask;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
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

import jp.ohwada.android.yag1.Constant;

/**
 * Common Async Task
 */
public class CommonAsyncTask extends AsyncTask<Void, Void, Boolean> {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	protected String TAG_SUB = "AsyncTask";
	
	private final static String SCHEME = "http";
	private final static String ENCODING = "UTF-8";	
	private final static String AUTHORITY = "archive.yafjp.org";
	private final static String PATH = "/artsearch/test_newlod/inspection.php";	
	private final static String OUTPUT = "json";
	private final static String FORMAT_DATE = "T00:00:00+09:00\"^^xsd:dateTime";

	protected final static int LIMIT_EVENT = 50; 
			
    // class object    	
	private DefaultHttpClient mClient = null;
   	protected DateUtility mDateUtility;
   		
	// local variable
	private Map<String, String> mQueries = null;  
	private String mScheme = SCHEME;
	private String mAuthority = AUTHORITY;
	private String mPath = PATH;
			
	// local variable
	protected Date mDateStart = null;
	protected Date mDateEnd = null;
	protected String mUrl = "";
	protected String mResult = "";
	
	/**
	 * === constructor ===
	 */			 
    public CommonAsyncTask() {
        super();
        mClient = new DefaultHttpClient();
        mDateUtility = new DateUtility();
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
	 * execBackground
	 */  	
	protected void execBackground() {
		// dummy
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
	 * get Result
	 * @return String 
	 */  	
	public String getResult() {
		return mResult;
	}

    /**
	 * getPage 
	 * @param String url
	 * @return String
	 */     
	protected String getPage( String url ) {
		String str = url + "."  + OUTPUT;
		return excuteGetRequest( str );
	}

    /**
	 * getResult
	 * @param String query
	 * @return String
	 */
	protected String getResult( String query ) {
		initQuery();
		addGetQuery( "query", query );
		addGetQuery( "output",  OUTPUT );
		return excuteGetRequest( buildUrl() );
	}
			
    /**
	 * getFilterDate
	 * @param Date first
	 * @param Date last
	 * @return String
	 */  
	protected String getFilterDate( Date firstDate, Date lastDate ) {
		String first = mDateUtility.formatDate( firstDate );
		String last = mDateUtility.formatDate( lastDate );		
		first = "\"" + first + FORMAT_DATE; 
		last = "\"" + last + FORMAT_DATE; 
		String query = "FILTER ((?dtstart > " + first + " && ";
		query += "?dtstart < " + last + " ) || ";
		query += "(?dtend > " + first + " && ";
		query += "?dtend < " + last + " ) || ";
		query += "(?dtstart < " + first + " && ";
		query += "?dtend > " + last + " )) ";
		return query;
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
    protected void shutdown() {
    	if ( mClient != null ) {
    		mClient.getConnectionManager().shutdown();
    	}
    	mClient = null;
    }
   		
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			    		   
}