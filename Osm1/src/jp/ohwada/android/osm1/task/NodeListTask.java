package jp.ohwada.android.osm1.task;

import java.io.File;

import jp.ohwada.android.osm1.Constant;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * NodeListTask
 */
public class NodeListTask {  

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private String TAG_SUB = "NodeListTask";

	// handler
	private int MSG_WHAT = Constant.MSG_WHAT_TASK_NODE_LIST;
	
	// timer
    private static final int TIMER_MSG_WHAT = 100;
    private static final int TIMER_INTERVAL = 500;  // 0.5 sec

    //  handler 
   	private Handler msgHandler;

	// object        
   	private NodeListAsync mAsync;
	private NodeListParser mParser;
   	private NodeListFile mFileClass;

	// variable
	private File mFileTarget = null;
	private NodeList mList = null;

	// timer
    private boolean isStart = false;
    private boolean isRunning = false;
    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public NodeListTask( Handler handler ) {
     	msgHandler = handler;		
		mParser = new NodeListParser();
		mFileClass = new NodeListFile(); 
    }

	/**
	 * exists Node file
	 * @return boolean
	 */
    public boolean existsFile() {
    	return mFileTarget.exists();
	}
	
	/**
	 * execute
	 * @param int lat
	 * @param int lng	 
	 * @return boolean
	 */         
    public boolean execute( int lat, int lng ) {
    	log_d( "execute " + lat + " " + lng );
    	mFileTarget = mFileClass.getFile( lat, lng );
		if ( mFileClass.isExpired( mFileTarget ) ) {
		 	// create async task each time
		    mAsync = new NodeListAsync();	
		    mAsync.setGeo( lat, lng );		
			mAsync.execute();
			startHandler();
			return false;
		} 
		// read file 
		mList = mFileClass.read( mFileTarget );
		return true;
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
		log_d( "cancel" );  	
		if ( mAsync != null ) {
			mAsync.cancel( true );
			mAsync.shutdown();
		}	
	}
	
	/**
	 * saveFile
	 * @return int
	 */ 		
	private int saveFile() {		
		// get 			
		String result = getAsyncResult();
		if (( result == null )|| result.equals("") ) {
			log_d( "No result" );
        	return Constant.MSG_ARG1_TASK_ERR_RESULT;			
		}				
		// parse 			
		NodeList list = mParser.parse( result );
		if (( list == null )||( list.size() == 0 )) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save 
		mFileClass.write( mFileTarget, list );		
		// read same file to set addtional
		mList = mFileClass.read( mFileTarget );
		log_d( "SUCCESS" );
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	private boolean readFile() {
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
    private AsyncTask.Status getStatus() {
    	if ( mAsync != null ) {
    		return mAsync.getStatus();
    	}
    	return AsyncTask.Status.RUNNING;
    }

// -- handler ---
	/**
	 * start Handler
	 */    
	 private void startHandler() {
		isStart = true; 
		updateRunning();
	}
	
	/**
	 * stop Handler
	 */ 
	 private void stopHandler() {	    
		isStart = false;
		updateRunning();
	}

	/**
	 * updateRunning 
	 */		
    private void updateRunning() {
        boolean running = isStart;
        if ( running != isRunning ) {
			// restart running    
            if ( running ) {
                timerHandler.sendMessageDelayed( Message.obtain( timerHandler, TIMER_MSG_WHAT ), TIMER_INTERVAL );              
             // stop running             
             } else {
                timerHandler.removeMessages( TIMER_MSG_WHAT );
            }
            isRunning = running;
        }
    }

	/**
	 * message handler class
	 */	    
    private Handler timerHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isRunning ) {
				updateStatus();
                sendMessageDelayed( Message.obtain( this, TIMER_MSG_WHAT ), TIMER_INTERVAL );
            }
        }
    };
    	
	/**
	 * update Status
	 */	
    private synchronized void updateStatus() { 
		if ( getStatus() == AsyncTask.Status.FINISHED ) {
			execPost();
		}
	}

	/**
	 * execPost
	 */ 
	private void execPost() {
		stopHandler();	
		int arg1 = saveFile();
		int arg2 = 0;
		if ( arg1 != Constant.MSG_ARG1_TASK_SUCCESS ) {
			boolean ret = readFile();
			if ( ret ) {
				arg1 = Constant.MSG_ARG1_TASK_CACHE;
			}
		}
		sendMessage( MSG_WHAT, arg1, arg2 );		
	}
				
	/**
	 * sendMessage
	 * @param int what
	 * @param int arg1
	 * @param int arg2
	 */	
    private void sendMessage( int what, int arg1, int arg2 ) {
    	Message msg = msgHandler.obtainMessage( what, arg1, arg2 );	        
    	msgHandler.sendMessage( msg );
    }
    
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}

}