package jp.ohwada.android.osm2.task;

import java.io.File;

import jp.ohwada.android.osm2.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * Node Task
 */
public class NodeTask extends CommonTask {  

	// object        
  	private NodeAsync mAsync;
	private NodeParser mParser;
   	private NodeFile mFileClass;

	// variable
	private NodeHash mHash= null;
	private File mFileTarget = null;
	    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public NodeTask( Context context, Handler handler ) {
    	super( context, handler, 
    		Constant.MSG_WHAT_TASK_NODE,
    		Constant.MSG_WHAT_TIMER_NODE );
    	TAG_SUB = "NodeTask";	
		mParser = new NodeParser();
		mFileClass = new NodeFile(); 
    }
	
	/**
	 * execute
	 * @param int lat
	 * @param int lng	 
	 * @return boolean
	 */         
    public boolean execute( String url ) {
    	NodeRecord record = new NodeRecord();
    	int id = record.getNodeId( url );
    	mFileTarget = mFileClass.getFile( id );
		if ( mFileClass.isExpired( mFileTarget ) ) {
		 	// create async task each time
		    mAsync = new NodeAsync( mContext );	
		    mAsync.setNode( id );		
			mAsync.execute();
			startHandler();
			return false;
		} 
		// read file 
		mHash= mFileClass.read( mFileTarget );
		return true;
	}
    
	/**
	 * get NodeHash
	 * @return NodeHash
	 */ 
	public NodeHash getHash() {	
		return mHash;
	}

	/**
	 * cancel
	 */
	public void cancel() {
		if ( mAsync != null ) {
			mAsync.cancel( true );
			mAsync.shutdown();
		}	
	}
	
	/**
	 * saveFile
	 * @return int
	 */ 		
	protected int saveFile() {		
		// get 			
		String result = getAsyncResult();
		if (( result == null )|| result.equals("") ) {
			log_d( "No result" );
        	return Constant.MSG_ARG1_TASK_ERR_RESULT;			
		}				
		// parse 			
		NodeHash hash = mParser.parse( result );
		if (( hash == null )||( hash.size() == 0 )|| !hash.isValidNode() ) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save 
		mFileClass.write( mFileTarget, hash );		
		// read same file to set addtional
		mHash= mFileClass.read( mFileTarget );
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mHash= mFileClass.read( mFileTarget );
			return true;
		}
		return false;	
	}

	/**
	 * getAsyncResult()
	 * @return  String 
	 */
	private String getAsyncResult() {
		String result = "";
		if ( mAsync != null ) {			
			result = mAsync.getResult();
			mAsync = null;
		}
		return result; 
	}
	
	/**
	 * getStatus
	 * @return AsyncTask.Status
	 */		
	protected AsyncTask.Status getStatus() {
    	if ( mAsync != null ) {
    		return mAsync.getStatus();
    	}
    	return AsyncTask.Status.RUNNING;
    }

}