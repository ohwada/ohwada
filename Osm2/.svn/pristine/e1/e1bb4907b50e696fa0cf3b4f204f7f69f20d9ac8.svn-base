package jp.ohwada.android.osm2.task;

import android.content.Context;

/**
 * Node Async Task
 */
public class NodeAsync extends CommonAsyncTask {
			
	// local variable
	private int mNode = 0;

	/**
	 * === constructor ===
	 */			 
    public NodeAsync( Context context ) {
        super( context );
    }

	/**
	 * execBackground
	 */	
    protected void execBackground() {
    	mResult = mClient.executeNode( mNode );
    }
    
	/**
	 * setNode
	 * @param int node 
	 */ 
	public void setNode( int node ) {
		mNode = node;
	}

}