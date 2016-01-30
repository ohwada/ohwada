package jp.ohwada.android.pinqa1.task;

import android.os.AsyncTask;

import java.io.File;

/**
 * Binary Async Task
 */
public class BinaryAsyncTask extends AsyncTask<Void, Void, Boolean> {
			
    // class object    	
	private BinaryHttpClient mClient = null;

	// local variable
	private File mFile = null;
	private String mUrl = "";
	private boolean mResult = false;
	
	/**
	 * === constructor ===
	 */			 
    public BinaryAsyncTask() {
        super();
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
    	mClient = new BinaryHttpClient();
		mResult = mClient.excute( mFile, mUrl ); 
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
	 * setUrl
	 * @param String url
	 */  	
	public void setFileUrl( File file, String url ) {
		mFile = file;
		mUrl = url;
	}
	    		
	/**
	 * get Result
	 * @return boolean
	 */  	
	public boolean getResult() {
		return mResult;
	}

	/**
	 * shutdown
	 */ 
    public void shutdown() {
		if ( mClient != null ) {
    		mClient.shutdown();
    	}
    }			    		   
}