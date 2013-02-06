package jp.ohwada.android.yag1.task;

import android.content.Context;

/**
 * Event Async Task
 */
public class EventAsync extends CommonAsyncTask {
	
	// object
	private Context mContext;
					
	// local variable
	private String mUrl = "";
			
	/**
	 * === constructor ===
	 */			 
    public  EventAsync( Context context ) {
        super();
        mContext = context;
    }

	/**
	 * setUrl
	 * @param String url
	 */  	
	public void setUrl( String url ) {
		mUrl = url;
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
		mResult = mClient.executePage( mUrl );
	}
    
	/**
	 * execPostExecute
	 */	 
    protected void execPostExecute() {
		hideDialog();
    }
		    		   
}