package jp.ohwada.android.nfccconcentration;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * View for elapsed time
 */
public class TimeView {
    private final static int MSG_WHAT = Constant.MSG_WHAT_TIME;
 
    private final static int INTERVAL = 100; // 0.1 sec;

	private PreferenceUtility mPreference;
    private TextView mTextView;    

    private long mStartTime = 0L;
    private boolean isStart = false;
    private boolean runningFlag = false;

    /**
     * === constractor ===
     * @param Context c
     * @param TextView v   
     */	
    public TimeView( Context context, TextView view ) {
        mTextView = view;
		mPreference = new PreferenceUtility( context );
    }

    /**
     * save elapsed time
     */	
   public void saveTime() {
   		mPreference.setTime( getFormatedTime() );
	}

    /**
     * show previous elapsed time
     */	
   public void showTime() {
		mTextView.setText(  mPreference.getTime() );	
	}

    /**
     * get FormatedTime
     * @retuen String
     */	
   private String getFormatedTime() {	
        long msec = System.currentTimeMillis() - mStartTime ;
        long sec = (long) ( msec / 1000 );
        long msec_r = msec - ( sec * 1000 );
        long msec_v = (long) ( msec_r / 100 );
        String time = sec + "." + msec_v + " sec" ;
        return time;
   }       

// --- handler ---	
    /**
     * start handler
     */	        	        	    
    public void start() {
        isStart = true;
        mStartTime = System.currentTimeMillis();
        updateRunning();
    }

    /**
     * stop handler
     */	  
    public void stop() {
        isStart = false;
        updateRunning();
    }


    /**
     * update Running
     */	
    private void updateRunning() {
        boolean running = isStart;
        if (running != runningFlag) {
            if (running) {
                mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_WHAT), INTERVAL );                
             } else {
                mHandler.removeMessages(MSG_WHAT);
            }
            runningFlag = running;
        }
    }

    /**
     * handler class
     */	    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (runningFlag) {
                updateText();
                sendMessageDelayed(Message.obtain(this, MSG_WHAT), INTERVAL );
            }
        }
    };

    /**
     * update TextView
     */	
    private synchronized void updateText() {   
		mTextView.setText( getFormatedTime() );	
    }
  
}
