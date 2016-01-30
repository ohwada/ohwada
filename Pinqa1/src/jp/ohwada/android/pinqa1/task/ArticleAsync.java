package jp.ohwada.android.pinqa1.task;

import jp.ohwada.android.pinqa1.R;
import android.content.Context;


/**
 * Article Async Task
 */
public class ArticleAsync extends CommonAsyncTask {

	// object
	private PinqaHttpClient mClient;
				
	// local variable
	private String mUrl = "";
	private String mResult = "";
	
	/**
	 * === constructor ===
	 */			 
    public ArticleAsync( Context context ) {
        super( context );
    }

	/**
	 * setUrl
	 * @return String url
	 */ 
	public void setUrl( String url ) {
		mUrl = url;
	}

	/**
	 * get Result
	 * @return String 
	 */  	
	public String getResult() {
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
    
	/**
	 * execBackground
	 */  	
	protected void execPreExecute() {
    	showProgress( R.string.loading );
    	mResult = null;
	}

	/**
	 * execBackground
	 */
    protected void execBackground() {
        mClient = new PinqaHttpClient();
		mResult = mClient.execute( getQuery() );
    }

	/**
	 * execProgressUpdate
	 */  	
	protected void execPostExecute() {
        hideProgress();
	}
    	
	/**
	 * getQuery
	 * @return String
	 */  	
	 private String getQuery() {
		String query = "";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "<" + mUrl + "> ?p ?o ";
		query += "} ";
		return query;	
	}	    		   

}