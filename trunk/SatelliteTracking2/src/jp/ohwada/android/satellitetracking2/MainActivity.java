package jp.ohwada.android.satellitetracking2;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import jp.ohwada.android.satellite.AziEle;
import jp.ohwada.android.satellite.AziEleController;
import jp.ohwada.android.satellite.LatLng;
import jp.ohwada.android.satellite.LatLngController;
import jp.ohwada.android.satellite.Satellite;
import jp.ohwada.android.satellite.SatelliteController;
import jp.ohwada.android.satellite.SatelliteData;
import jp.ohwada.android.satellite.SiderealTime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private final static boolean D = true;
	private String TAG = "SatelliteLatLng";

    private final static int MINUTE_DELTA = 10;    
    private final static int POINT_NUM = 24 * 60 / MINUTE_DELTA ;
	private final static double RAD_DIV = 2 * Math.PI / POINT_NUM;
	private final static long TIME_DIV = 24*60*60*1000 /  POINT_NUM;

    private final static String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss z";

	private final static double GEO_LAT = 35.695;
	private final static double GEO_LNG = 139.740;

    private final static int WHAT_TIMER = 1;
    private final static int TIMER_INTERVAL = 100;

	private DecimalFormat mDecimalFormat = new DecimalFormat("###.#");

	private LatLngView mLatLngView;
	private AziEleView mAziEleView;
	private TextView mTextViewTime;
	private TextView mTextViewLatLng;
	private TextView mTextViewAziEle;

	private SatelliteController mSatelliteController;
	private LatLngController mLatLngController;
	private AziEleController mAziEleController;
	private SatelliteData mSatelliteData;	

	private LatLng mCurrentLatLng = null;
	private AziEle mCurrentAziEle = null;

	private int mPointer = 0;
	private int mCurrentNum = 0;
	private long mStartTime = 0;
		   				
	private boolean isTimerStarted = false;
	private boolean isTimerRunning = false;

    /** 
	 * === constructor ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mLatLngView = (LatLngView) findViewById( R.id.LatLngView );
		mAziEleView = (AziEleView) findViewById( R.id.AziEleView );
		mTextViewTime = (TextView) findViewById( R.id.TextView_time );
		mTextViewLatLng = (TextView) findViewById( R.id.TextView_latlng );
		mTextViewAziEle = (TextView) findViewById( R.id.TextView_aziele );
		
		Button btnRefresh = (Button) findViewById( R.id.Button_refresh );	
		btnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            	showSatellite();
            }
        });	

		mSatelliteController = new SatelliteController();
		mLatLngController = new LatLngController();
		mAziEleController = new AziEleController();
		mSatelliteData = new SatelliteData( this ); 
	}

	/**
	 * === onResume ===
	 */
	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed( mRunnable, 100 ); 
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
		List<Satellite> list_data = mSatelliteData.getSatelliteList( POINT_NUM, cal );
		List<Satellite> list_sat = mSatelliteController.rotationList( list_data, RAD_DIV, offset );
		// LatLngView
		List<LatLng> list_lat = mLatLngController.convList( list_sat );
		mCurrentLatLng = list_lat.get( mCurrentNum );
		mLatLngView.setList( list_lat );
		mLatLngView.setCurrentNum( mCurrentNum );	
		// AziEleView
		mAziEleController.setOrigin( GEO_LAT, GEO_LNG );
		List<AziEle> list_azi = mAziEleController.convList( list_sat );
		mCurrentAziEle = list_azi.get( mCurrentNum );
		mAziEleView.setList( list_azi );
		mAziEleView.setCurrentNum( mCurrentNum );
		// timer
		startTimer();
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

		String msg1 = getDateFormat( mCurrentNum );		
		msg1 += " lng=" + mDecimalFormat.format( mCurrentLatLng.getLongitudeDeg() );
		msg1 += " lat=" + mDecimalFormat.format( mCurrentLatLng.getLatitudeDeg() );
		mTextViewLatLng.setText( msg1 );

		String msg2 = getDateFormat( mCurrentNum );		
		msg2 += " azimuth=" + mDecimalFormat.format( mCurrentAziEle.getAzimuthDeg() );
		msg2 += " elevation=" + mDecimalFormat.format( mCurrentAziEle.getElevationDeg() );
		mTextViewAziEle.setText( msg2 );

		mLatLngView.updatePointer( mPointer );
		mAziEleView.updatePointer( mPointer );
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
