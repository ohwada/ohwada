package jp.ohwada.android.osm2.task;

import java.util.List;

import jp.ohwada.android.osm2.Constant;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.maps.GeoPoint;

/**
 * Geocoder Task
 */
public class GeocoderTask {  

	// timer
    private static final int TIMER_MSG_WHAT = Constant.MSG_WHAT_TIMER_GEOCODER;
    private static final int TIMER_INTERVAL = 500;  // 0.5 sec
    
    // message 
    private static final int MSG_WHAT = Constant.MSG_WHAT_TASK_GEOCODER;
    
	//  constarctor  
   	private Handler msgHandler;
   	private Context mContext; 
	private View mView;
	private GeocoderAsync mAsync;

	// result
	private List<Address> mResultList = null;

	// timer
    private boolean isStart = false;
    private boolean isRunning = false;
           	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public GeocoderTask( Context context, View view, Handler handler ) {
        mContext = context; 
    	mView = view;
    	msgHandler = handler;
    	mAsync = new GeocoderAsync( mContext, mView );	
    }
	
	/**
	 * execute
	 * @param String location
	 * @return boolean
	 */         
    public void execute( String location ) {
		mAsync = new GeocoderAsync( mContext, mView );	
		mAsync.setLocation( location );	
		mAsync.execute();
		startHandler();
	}

	/**
	 * get Result
	 * @return List<Address>
	 */ 
	public List<Address> getResultList() {	
		return mResultList;
	}

	/**
	 * get Result
	 * @return GeoPoint
	 */  	
	public GeoPoint getResultPoint() {
		if (( mResultList == null )|| mResultList.isEmpty() ) return null;
		Address addr = mResultList.get( 0 );
		int lat = doubleToE6( addr.getLatitude() ); 
		int lng = doubleToE6( addr.getLongitude() );
		GeoPoint point = new GeoPoint( lat, lng );
		return point;
	}
	
	/**
	 * cancel
	 */
	public void cancel() {
		if ( mAsync != null ) {
			mAsync.cancel( true );
		}
		stopHandler();	
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
		if ( mAsync.getStatus() == AsyncTask.Status.FINISHED ) {
			execPost();
		}
	}

	/**
	 * execPost
	 */ 
	private void execPost() {
		stopHandler();	
		mResultList = mAsync.getResult();
    	Message msg = msgHandler.obtainMessage( MSG_WHAT );	        
    	msgHandler.sendMessage( msg );
	}

	/**
	* convert real number to integer
	* @param Double : location( floating point format )
	* @return int : location( E6 format )
	*/
	private int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}

}