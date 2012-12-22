package jp.ohwada.android.lodsample2;

import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Event Async Task
 */
public class EventAsyncTask extends AsyncTask<Void, Void, Boolean> {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG_SUB = "FontAsync: ";
	
	// local variable
	private Date mDateToday;
	private String mResult;

	/**
	 * === constructor ===
	 * @param Context context
	 */			 
    public EventAsyncTask() {
        super();
    }

	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
		log_d( "onPreExecute" );		
    }

	/**
	 * === doInBackground ===
	 * @return Boolean
	 */	
    @Override
    protected Boolean doInBackground( Void... params ) {
        log_d( "doInBackground" );
		return getByHttp();
    }

	/**
	 * === onProgressUpdate ===
	 */	
    @Override
    protected void onProgressUpdate( Void... params ) {
		log_d( "onProgressUpdate" );
    }

	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
		log_d( "onPostExecute" );
    }
    
	/**
	 * getByHttp
	 */  	
	private boolean getByHttp() {
		log_d( "getEvent" );
		// get today's events		
		EventHttpClient client = new EventHttpClient();
		mResult = client.getEvents( mDateToday, mDateToday );
		return true;
	}

	/**
	 * setToday
	 * @param Date today
	 */  	
	public void setToday( Date today ) {
		mDateToday = today;
	}
	
	/**
	 * getList
	 */  	
	public String getResult() {
		return mResult;
	}
		
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}			    		   
}