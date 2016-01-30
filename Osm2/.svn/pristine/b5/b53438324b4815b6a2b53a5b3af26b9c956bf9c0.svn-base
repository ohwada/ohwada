package jp.ohwada.android.osm2.task;

import java.io.File;

import jp.ohwada.android.osm2.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * NodeListTask
 */
public class NodeListTask extends CommonTask {  

	// object        
   	private NodeListAsync mAsync;
	private NodeListParser mParser;
   	private NodeListFile mFileClass;
   	private NodeFile mNodeFile;
   	
	// variable
	private NodeList mList = null;
	private File mFileTargetSummary = null;
	private File mFileTargetDetail = null;
		    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public NodeListTask( Context context, Handler handler ) {
        super( context, handler, 
        	Constant.MSG_WHAT_TASK_NODE_LIST,
        	Constant.MSG_WHAT_TIMER_NODE_LIST );
		mParser = new NodeListParser();
		mFileClass = new NodeListFile(); 
		mNodeFile = new NodeFile(); 
		TAG_SUB = "NodeListTask";
    }
	
	/**
	 * execute
	 * @param int lat
	 * @param int lng	 
	 * @return boolean
	 */         
    public boolean execute( int lat, int lng ) {
    	mFileTargetSummary = mFileClass.getFileSummary( lat, lng );
    	mFileTargetDetail = mFileClass.getFileDetail( lat, lng );

    	// if detail is valid
    	if ( !mFileClass.isExpiredDetail( mFileTargetDetail ) ) {
    		mList = mFileClass.read( mFileTargetDetail );
    		return true;

        // if summray is valid
    	} else if ( !mFileClass.isExpiredSummary( mFileTargetSummary ) ) {
    		mList = mFileClass.read( mFileTargetSummary );    	
    		return true;
		}
		
		 // create async task each time
		mAsync = new NodeListAsync( mContext );	
		mAsync.setList( lat, lng );		
		mAsync.execute();
		startHandler();
		return false;

	}

	/**
	 * getList
	 * @return NodeList
	 */ 
	public NodeList getList() {	
		return mList;
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
		NodeList list = mParser.parse( result );
		if (( list == null )||( list.sizeHash() == 0 )) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save 
		File file_list = null;
		if ( list.hasDetail() ) {
			file_list = mFileTargetDetail;	
		} else {
			file_list = mFileTargetSummary;
		}
		mFileClass.write( file_list, list );		
		// read same file to set addtional
		mList = mFileClass.read( file_list );
		for ( NodeHash hash : list.getListHash() ) {
			if ( hash.hasDetail() ) {
				File file_node = mNodeFile.getFile( hash.node_id ); 
				mNodeFile.write( file_node, hash ); 
			}
		}
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		// if detail exists
		if ( mFileTargetDetail.exists() ) { 
			mList = mFileClass.read( mFileTargetDetail );
			return true;
		}
		// if summary exists
		if ( mFileTargetSummary.exists() ) { 
			mList = mFileClass.read( mFileTargetSummary );
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