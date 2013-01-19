package jp.ohwada.android.yag1.task;

import java.io.File;

import jp.ohwada.android.yag1.Constant;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * EventTask
 */
public class EventTask extends CommonTask {  
	
	// object        
   	private EventAsync mAsync;
	private EventParser mParser;
   	private EventFile mFileClass;
 	
	// variable
	private EventRecord mEventRecord = null;	
	private String mUrl = null;
	
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public EventTask( Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_EVENT );
        TAG_SUB = "EventTask";			
		mParser = new EventParser();
		mFileClass = new EventFile(); 
    }

	/**
	 * execute
	 * @param String name
	 * @param String url
	 * @return boolean
	 */
    public boolean execute( String url ) {
    	EventRecord record = new EventRecord();
    	String name = record.getEventName( url );
        File file = mFileClass.getFileWithTxt( name );
		return execute( file, url );
    }
    	 
	/**
	 * execute
	 * @param File file
	 * @param String url
	 * @return boolean
	 */
    public boolean execute( File file, String url ) {
    	mFileTarget = file ;
    	mUrl = url;
		if ( mFileClass.isExpired( file ) ) {
			// create async task each time	
		    mAsync = new EventAsync();							
			mAsync.setUrl( url );
			mAsync.execute();
			startHandler();
			return false;
		}
		// read file 
		mEventRecord = mFileClass.read( file );
		return true;
	}

	/**
	 * getList
	 * @return EventRecord
	 */ 
	public EventRecord getEvent() {	
		return mEventRecord;
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
		// get events			
		String result = getAsyncResult();
		if (( result == null )|| result.equals("") ) {
			log_d( "No result" );
        	return Constant.MSG_ARG1_TASK_ERR_RESULT;				
		}				
		// parse events				
		EventRecord record = mParser.parse( mUrl, result );
		if ( record == null ) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save events		
		mFileClass.write( mFileTarget, record );
		// read same file to set addtional
		mEventRecord = mFileClass.read( mFileTarget );
		return Constant.MSG_ARG1_TASK_SUCCESS;
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mEventRecord = mFileClass.read( mFileTarget );
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