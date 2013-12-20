package jp.ohwada.android.evy1sample2;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Parser Async Task
 */
public class ParserAsyncTask extends AsyncTask<Void, Void, Boolean> {

	// debug
	private static final String TAG = "ParserAsyncTask";
	private static final boolean D = true;
	
	// excute
 	private MidiParser mParser;
 				
 	private byte[] mBytes = null;
 	
 	// result
 	private List<MidiMessage> mList = null;
	private long mTimebase = 0;	
	
	/**
	 * === constructor ===
	 */			 
    public ParserAsyncTask( Context context ) {
        super();
        mParser = new MidiParser( context );
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
    protected Boolean doInBackground( Void... param ) {
		mParser.parse( mBytes );
		return true;
    };
	
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
    	mList = mParser.getList();
		mTimebase = mParser.getTimebase();
    }

	/**
	 * setBytes
	 * @param byte[] bytes
	 */  	
	public void setBytes( byte[] bytes ) {
		mBytes = bytes;
	}
	    		
	/**
	 * get List
	 * @return List<MidiRecord>
	 */  	
	public List<MidiMessage> getList() {
		return mList;
	}

	/**
	 * get Timebase
	 * @return long
	 */ 
	public long getTimebase() {
		return mTimebase;
	}

	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG,  msg );
	}


		    		   
}