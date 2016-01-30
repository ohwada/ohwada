package jp.ohwada.android.osm2.task;

import jp.ohwada.android.osm2.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * CommonTask
 */
public class CommonTask {  

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	protected String TAG_SUB = "CommonTask";

	// handler
	protected int MSG_WHAT = 0;

	// timer
    protected int TIMER_MSG_WHAT = 100;
    private static final int TIMER_INTERVAL = 500;  // 0.5 sec

    //  handler 
    protected Context mContext;
   	private Handler msgHandler;

	// timer
    private boolean isStart = false;
    private boolean isRunning = false;
    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public CommonTask( Context context, Handler handler, int msg, int timer  ) {
    	mContext = context;
     	msgHandler = handler;	
     	MSG_WHAT = msg;	
     	TIMER_MSG_WHAT = timer;
    }
		
// -- handler ---
	/**
	 * start Handler
	 */    
	 protected void startHandler() {
		isStart = true; 
		updateRunning();
	}
	
	/**
	 * stop Handler
	 */ 
	 protected void stopHandler() {	    
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
	 * getStatus
	 * @return AsyncTask.Status
	 */		
    protected AsyncTask.Status getStatus() {
    	return AsyncTask.Status.RUNNING;
    }
    
	/**
	 * execPost
	 */ 
	protected void execPost() {
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
	 * saveFile
	 * @return int
	 */ 	
	protected int saveFile() {
		return Constant.MSG_ARG1_NONE;
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		return false;
	}
						
	/**
	 * sendMessage
	 * @param int what
	 * @param int arg1
	 * @param int arg2
	 */	
    protected void sendMessage( int what, int arg1, int arg2 ) {
    	Message msg = msgHandler.obtainMessage( what, arg1, arg2 );	        
    	msgHandler.sendMessage( msg );
    }
    
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}

}