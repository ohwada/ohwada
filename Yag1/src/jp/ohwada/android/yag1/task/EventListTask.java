package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.Constant;

import android.os.AsyncTask;
import android.os.Handler;

/**
 * EventListTask
 */
public class EventListTask extends CommonTask {  
   	
	// object    
   	private EventListAsync mAsync;
	private EventListParser mParser;
   	private EventListFile mFileClass; 	
   	private DateUtility mDateUtility;
   	
	// variable
	private EventList mList = null;

    // variable
    protected File mFileTarget = null;
    
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public EventListTask( Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_EVENT_LIST );
		TAG_SUB = "EventListTask";		
		mParser = new EventListParser();
		mFileClass = new EventListFile();		
		mDateUtility = new DateUtility();
    }

	/**
	 * execute
	 * @param Date date
	 * @return boolean
	 */ 
    public boolean execute( Date date ) {
		Date end = mDateUtility.addDay( date, Constant.EVENT_DAYS );
		File file = mFileClass.getFileForList( date );
		return execute( file, date, end );
    }
    
	/**
	 * execute
	 * @param File file
	 * @param Date start
	 * @param Date end
	 * @return boolean
	 */ 
    public boolean execute( File file, Date start, Date end ) {
    	mFileTarget = file;
    	// get from server if expired
		if ( mFileClass.isExpired( file ) ) {
			// create async task each time
		    mAsync = new EventListAsync();									
			mAsync.setDate( start, end );
			mAsync.execute();
			startHandler();
			return false;
		}	
		// read file 
		mList = mFileClass.read( file );
		return true;
	}
	
	/**
	 * getList
	 * @return List<EventRecord>
	 */ 
	public List<EventRecord> getList() {
		if ( mList == null ) return null;
		return mList.getList();
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
		// get events
		String result = getAsyncResult();
		if (( result == null )|| result.equals("") ) {
			log_d( "No result" );
        	return Constant.MSG_ARG1_TASK_ERR_RESULT;		
		}				
		// parse events				
		EventList list = mParser.parse( result );
		if (( list == null )||( list.size() == 0 )) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;	
		}
		// save events		
		mFileClass.write( mFileTarget, list );
		// read same file to set addtional
		mList = mFileClass.read( mFileTarget );
        return Constant.MSG_ARG1_TASK_SUCCESS;
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mList = mFileClass.read( mFileTarget );
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