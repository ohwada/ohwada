package jp.ohwada.android.yag1.task;

import java.io.File;

import jp.ohwada.android.yag1.Constant;

import android.os.AsyncTask;
import android.os.Handler;

/**
 * PlaceListTask
 */
public class PlaceListTask extends CommonTask {  

	// object        
   	private PlaceListAsync mAsync;
	private PlaceListParser mParser;
   	private PlaceListFile mFileClass;

	// variable
	private PlaceList mList = null;

    // variable
    protected File mFileTarget = null;
    
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public PlaceListTask( Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_PLACE_LIST );
        TAG_SUB = "PlaceListTask";				
		mParser = new PlaceListParser();
		mFileClass = new PlaceListFile(); 
		mFileTarget = mFileClass.getFileTarget();
    }

	/**
	 * exists Place file
	 * @return boolean
	 */
    public boolean existsFile() {
		return mFileClass.existsList();
	}
	
	/**
	 * execute
	 * @return boolean
	 */         
    public boolean execute() {
		if ( mFileClass.isExpiredList() ) {
		 	// create async task each time
		    mAsync = new PlaceListAsync();								
			mAsync.execute();
			startHandler();
			return false;
		} 
		// read file 
		mList = mFileClass.read();
		return true;
	}

	/**
	 * getList
	 * @return PlaceList
	 */ 
	public PlaceList getList() {	
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
	 * execPost
	 */ 
	protected void execPost() {
		execPostFile();
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
		PlaceList list = mParser.parse( result );
		if (( list == null )||( list.size() == 0 )) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save 
		list.addExtension();
		mFileClass.write( list );		
		// read same file to set addtional
		mList = mFileClass.read();
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mList = mFileClass.read();
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