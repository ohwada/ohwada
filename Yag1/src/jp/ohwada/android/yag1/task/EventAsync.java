package jp.ohwada.android.yag1.task;

/**
 * Event Async Task
 */
public class EventAsync extends CommonAsyncTask {
	
	/**
	 * === constructor ===
	 */			 
    public  EventAsync() {
        super();
        TAG_SUB = "EventAsync";
    }

	/**
	 * setUrl
	 * @param String url
	 */  	
	public void setUrl( String url ) {
		mUrl = url;
	}
	
	/**
	 * execBackground
	 */  	
	protected void execBackground() {
		mResult = getPage( mUrl );
	}
	    		   
}