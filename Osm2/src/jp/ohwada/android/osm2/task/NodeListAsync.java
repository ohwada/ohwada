package jp.ohwada.android.osm2.task;

import android.content.Context;

/**
 * NodeList Async Task
 */
public class NodeListAsync extends CommonAsyncTask {
			
	// local variable
	private int mLat = 0;
	private int mLong = 0;

	/**
	 * === constructor ===
	 */			 
    public NodeListAsync( Context context ) {
        super( context );
    }

	/**
	 * execBackground
	 */	
    protected void execBackground() {
    	mResult = mClient.executeList( mLat, mLong );
    }

	/**
	 * setList
	 * @param int lat 
	 * @param int lng 
	 */ 
	public void setList( int lat, int lng ) {
		mLat = lat;
		mLong = lng;
	}			   	    		   
}