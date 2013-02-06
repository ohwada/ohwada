package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * EventList in PlaceList Task
 */
public class EventListPlaceTask extends CommonTask {  

	// object 
   	private Context mContext; 
   	private EventListPlaceAsync mAsync;
	private EventListParser mParser;
   	private EventListFile mFileClass;

   	private DateUtility mDateUtility;
   	
 	// variable
 	private Date mDateStart = null;
 	private EventList mList = null;
 	private EventList mListAsync = null;
 	private List<String> mListUrl = null;

    // variable
    protected File mFileTarget = null;
    
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public EventListPlaceTask( Context context, Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_EVENT_LIST_PLACE );
        mContext = context; 
		TAG_SUB = "EventListPlaceListTask";			
		mParser = new EventListParser();
		mFileClass = new EventListFile(); 
		mDateUtility = new DateUtility();
    }

	/**
	 * execute
	 * @param String place_url
	 * @return boolean
	 */ 
    public boolean execute( PlaceRecord record ) {
        // http://yan.yafjp.org/place-info/place_xxx -> place_xxx
        String place_name = record.getPlaceName( record.url );  	
		File file = mFileClass.getFileForPlace( place_name );
		List<String> list = record.getListChildUrls();
	  	Date today = mDateUtility.getDateToday();	
	  	Date end = mDateUtility.addMonth( today, Constant.EVENT_LIST_PLACE_MONTHS );		
		return execute( file, list, today, end );
    }
    
	/**
	 * execute
	 * @param File file
	 * @param String place_url
	 * @param Date start
	 * @param Date end
	 * @return boolean
	 */ 
    public boolean execute( File file, List<String> list, Date start, Date end ) {
    	mFileTarget = file;
    	mListUrl = list;
    	mDateStart = start;
    	// get from server if expired
		if ( mFileClass.isExpired( file ) ) {	
			// create async task each time
		    mAsync = new EventListPlaceAsync( mContext );							
			mAsync.setListDate( list, start, end );
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
		stopHandler();	
		int arg1 = setFileList();
		int arg2 = 0;
		
		// sometime zero event result, although there must be event data
		// substitute today event list
		File file = mFileClass.getFileForList( mDateStart );
		EventList event_list = mFileClass.read( file );
		List<EventRecord> list_event = event_list.getListPlaceUrlList( mListUrl ); 
		if (( list_event != null )&&( list_event.size()> 0 )) {
			if ( arg1 == Constant.MSG_ARG1_TASK_SUCCESS ) {
				// merge event list and new events from network
				List<EventRecord> list_new = event_list.merge( list_event, mListAsync.getList() ); 
				mFileClass.write( mFileTarget, list_new );
			} else {
				// use event list
				arg1 = Constant.MSG_ARG1_TASK_CACHE;	
				mFileClass.write( mFileTarget, list_event );
			}	
		} else {
			if ( arg1 == Constant.MSG_ARG1_TASK_SUCCESS ) {
				// use new events from network		
				mFileClass.write( mFileTarget, mListAsync );
			}
		}
		// read same file to set addtional
		mList = mFileClass.read( mFileTarget );
		sendMessage( MSG_WHAT, arg1, arg2 );		
	}
	
	/**
	 * saveFile
	 * @return int
	 */ 	
	private int setFileList() {	
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
		mListAsync = list;
		return Constant.MSG_ARG1_TASK_SUCCESS;
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