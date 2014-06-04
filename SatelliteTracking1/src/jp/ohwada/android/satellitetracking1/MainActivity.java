package jp.ohwada.android.satellitetracking1;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private final static boolean D = true;
	private String TAG = "SatelliteTracking";

    private final static int MINUTE_DELTA = 10;    
    private final static int POINT_NUM = 24 * 60 / MINUTE_DELTA ;
	private final static double RAD_DIV = 2 * Math.PI / POINT_NUM;
	private final static long TIME_DIV = 24*60*60*1000 /  POINT_NUM;

    private final static String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss z";

    private final static int WHAT_TIMER = 1;
    private final static int TIMER_INTERVAL = 100;

	private DecimalFormat mDecimalFormat = new DecimalFormat("###.###");
	    
	private SatelliteGLSurfaceView mSurfaceView;
	private SatelliteView mSatelliteView;
	private TextView mTextViewTime;
	private TextView mTextViewCurrent;

	private SatelliteData mSatelliteData;	
	private SatelliteSpherical mCurrentSatelliteSpherical = null;

	private int mPointer = 0;
	private int mCurrentNum = 0;
	private long mStartTime = 0;
			
	private boolean isTouchLeft = false;
	private boolean isTouchRight = false;
		   				
	private boolean isTimerStarted = false;
	private boolean isTimerRunning = false;

    /** 
	 * === constructor ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mSurfaceView = (SatelliteGLSurfaceView) findViewById( R.id.SurfaceView );
		mSatelliteView = (SatelliteView) findViewById( R.id.SatelliteView );
		mTextViewTime = (TextView) findViewById( R.id.TextView_time );
		mTextViewCurrent = (TextView) findViewById( R.id.TextView_current );

		Button btnRefresh = (Button) findViewById( R.id.Button_refresh );	
		btnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            	showSatellite();
            }
        });	

 		Button btnLeft = (Button) findViewById( R.id.Button_left );	
		btnLeft.setOnTouchListener( new View.OnTouchListener() {
    		@Override
    		public boolean onTouch( View v, MotionEvent event ) {
    			execTouchLeft( event );
    			return true;
    		}
		});	

 		Button btnRight = (Button) findViewById( R.id.Button_right );	
		btnRight.setOnTouchListener( new View.OnTouchListener() {
    		@Override
    		public boolean onTouch( View v, MotionEvent event ) {
    			execTouchRight( event );
    			return true;
    		}
		});

		mSatelliteData = new SatelliteData( this ); 
	}

	/**
	 * === onResume ===
	 */
	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed( mRunnable, 500 ); 
	}

	/**
	 * === onPause ===
	 */
	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
	}

	/**
	 * Runnable
	 */
	private final Runnable mRunnable = new Runnable() {
    	@Override
    	public void run() {
        	showSatellite();
    	}
	};

	/**
	 * showSatellite
	 */
	private void showSatellite() {
		Calendar cal = mSatelliteData.getCalendarNow( MINUTE_DELTA );
		long now = cal.getTimeInMillis();  
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		mStartTime = cal.getTimeInMillis();  
		mCurrentNum = (int)(( now - mStartTime ) / TIME_DIV );	
		double offset = 2.0 * Math.PI * SiderealTime.getSiderealTime( cal );
		// SatelliteData
		mSatelliteData.initSatElset();
		List<Satellite> list1 = mSatelliteData.getSatelliteList( POINT_NUM, cal );
		// SurfaceView
		mSurfaceView.setList( list1 );
		mSurfaceView.setCurrentNum( mCurrentNum );
		// SatelliteView
		mSatelliteView.setList( list1, RAD_DIV, offset );
		mSatelliteView.setCurrentNum( mCurrentNum );
		mCurrentSatelliteSpherical = mSatelliteView.getCurrentSatelliteSpherical();
		startTimer();
	}

	/**
	 * execTouchLeft
	 */    			
	private void execTouchLeft( MotionEvent event ) {
   		switch( event.getAction() ) {
   			case MotionEvent.ACTION_DOWN:
   				isTouchLeft = true;
           		break;
        	case MotionEvent.ACTION_UP:
   				isTouchLeft = false;;
        		break;
        }
	}

	/**
	 * execTouchRigh
	 */    			
	private void execTouchRight( MotionEvent event ) {
   		switch( event.getAction() ) {
   			case MotionEvent.ACTION_DOWN:
   				isTouchRight = true;
           		break;
        	case MotionEvent.ACTION_UP:
   				isTouchRight = false;;
        		break;
        }
	}

	/**
	 * startTimer
	 */
	protected void startTimer() {
		isTimerStarted = true;
		updateRunning();
	}

	/**
	 * stopTimer
	 */
	public void stopTimer() {
		isTimerStarted = false;
		updateRunning();
	}

	/**
	 * updateRunning
	 */
    private void updateRunning() {
        boolean running = isTimerStarted;
        if ( running != isTimerRunning ) {
            if ( running ) {
            	updateTimer();
                mTimerHandler.sendMessageDelayed(
                	Message.obtain( mTimerHandler, WHAT_TIMER ), TIMER_INTERVAL );                
             } else {
                mTimerHandler.removeMessages( WHAT_TIMER );
            }
            isTimerRunning = running;
        }
    }

	/**
	 * TimerHandler
	 */
    private Handler mTimerHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isTimerRunning ) {
                updateTimer();
                sendMessageDelayed(
                	Message.obtain( this, WHAT_TIMER ), TIMER_INTERVAL );
            }
        }
    };

	/**
	 * updateTimer
	 */  
	private synchronized void updateTimer() {
		mPointer ++;
		if ( mPointer >= POINT_NUM ) {
			mPointer = 0;
		}

		mTextViewTime.setText( getDateFormat( mPointer ) );

		String msg = getDateFormat( mCurrentNum );
		msg += " lng=" + mDecimalFormat.format( mCurrentSatelliteSpherical.longitude_deg );
		msg += " lat=" + mDecimalFormat.format( mCurrentSatelliteSpherical.latitude_deg );
		mTextViewCurrent.setText( msg );
				    
		mSurfaceView.updatePointer( mPointer );
		mSatelliteView.updatePointer( mPointer );
		if ( isTouchLeft ) {
			mSurfaceView.moveLeft();
		}
		if ( isTouchRight ) {
			mSurfaceView.moveRight();
		}
	}

	/**
	 * getDateFormat
	 */  
	private String getDateFormat( int num ) {
		long time = mStartTime + num * TIME_DIV;
		CharSequence date = DateFormat.format( DATE_FORMAT, time );
		return (String)date;
	}

	/**
	 * log_d
	 */ 		 
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );		
	}
}
