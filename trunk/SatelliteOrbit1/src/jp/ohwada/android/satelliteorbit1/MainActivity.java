package jp.ohwada.android.satelliteorbit1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private static final String TAG = "satellite";

	private final static double DEG_TO_RAD = Math.PI / 180;
    private final static int POINT_NUM = 100;
	private final static double POINT_DIV = 2 * Math.PI / POINT_NUM;
	private final static double SEEK_ECCENTRICITY_RATIO = 1000;
			
    private final static double QZ_INCLINATION = 45.0;
    private final static double QZ_ECCENTRICITY = 0.099;

    private final static int MAX_SEEK_INCLINATION = 90;
    private final static int MAX_SEEK_ECCENTRICITY = 200;
    
    private final static int MODE_QZ = 1;
    private final static int MODE_STATIONARY = 2;

    private final static int WHAT_TIMER = 1;
    private final static int TIMER_INTERVAL = 100;

	private SatelliteGLSurfaceView mSurfaceView;
	private SatelliteView mSatelliteView;

	private TextView mTextViewInclination;
	private TextView mTextViewEccentricity;
	private SeekBar mSeekBarInclination;
	private SeekBar mSeekBarEccentricity;

	private double mInclination = QZ_INCLINATION;	
	private double mEccentricity = QZ_ECCENTRICITY;

	private int mCurrentNum = 0;
		
	private boolean isTouchLeft = false;
	private boolean isTouchRight = false;
		   				
	private boolean isTimerStarted = false;
	private boolean isTimerRunning = false;
	    
	/**
	 * === onCreate ===
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mSurfaceView = (SatelliteGLSurfaceView) findViewById( R.id.SurfaceView );
		mSatelliteView = (SatelliteView) findViewById( R.id.SatelliteView );
				
 		Button btnQz = (Button) findViewById( R.id.Button_qz );	
		btnQz.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            	execMode( MODE_QZ );
            }
        });	
 
 		Button btnStationary = (Button) findViewById( R.id.Button_stationary );	
		btnStationary.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
            	execMode( MODE_STATIONARY );
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
  
    	mTextViewInclination = (TextView) findViewById( R.id.TextView_inclination );
    	mTextViewEccentricity = (TextView) findViewById( R.id.TextView_eccentricity );
      
		mSeekBarInclination = (SeekBar) findViewById( R.id.SeekBar_inclination );
        mSeekBarInclination.setMax( MAX_SEEK_INCLINATION );
        mSeekBarInclination.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
            @Override
            public void onProgressChanged( SeekBar seekBar, int progress, boolean fromTouch ) {
				execInclination( progress );
            }
            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
        });
       
		mSeekBarEccentricity = (SeekBar) findViewById( R.id.SeekBar_eccentricity );
        mSeekBarEccentricity.setMax( MAX_SEEK_ECCENTRICITY );
        mSeekBarEccentricity.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
            @Override
            public void onProgressChanged( SeekBar seekBar, int progress, boolean fromTouch ) {
				execEccentricity( progress );
            }
            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
        });
                       
        new Handler().postDelayed( mRunnableDelay, 100 ); 
	}

	/**
	 * === onResume ===
	 */
	@Override
	protected void onResume() {
		super.onResume();
		stopTimer();
	}
	
	/**
	 * Runnable Delay
	 */
	private final Runnable mRunnableDelay = new Runnable() {
    	@Override
    	public void run() {
        	execMode( MODE_QZ );
    	}
	};

	/**
	 * execMode
	 */
	private void execMode( int mode ) {
		// MODE_STATIONARY:
		double inc = 0;
		double e = 0;		
		switch ( mode ) {
			case MODE_QZ :
				inc = QZ_INCLINATION;
				e = QZ_ECCENTRICITY;
				break;
		}
		mInclination = inc;
		mEccentricity = e;
		mSurfaceView.initEye();
		showSatellite();
	}

	/**
	 * execInclination
	 */
	private void execInclination( int progress ) {
		mInclination = (double)progress;
		showSatellite();
	}

	/**
	 * execEccentricity
	 */
	private void execEccentricity( int progress ) {
		mEccentricity = (double)progress / SEEK_ECCENTRICITY_RATIO ;
		showSatellite();
	}

	/**
	 * showSatellite
	 */
	private void showSatellite() {	
		mTextViewInclination.setText( Double.toString( mInclination ) );
		mTextViewEccentricity.setText( Double.toString( mEccentricity ) );
		mSeekBarInclination.setProgress( (int)mInclination );
		mSeekBarEccentricity.setProgress( (int)( SEEK_ECCENTRICITY_RATIO * mEccentricity ) );
		List<Satellite> list = getSatelliteList( mInclination, mEccentricity, POINT_NUM, POINT_DIV );
		mSurfaceView.setList( list );
		mSatelliteView.setList( list, POINT_DIV );
		startTimer();
	}

	/**
	 * getSatelliteList
	 */	
	private List<Satellite> getSatelliteList( double inc, double e, int num, double div ) {
		List<Satellite> list = new ArrayList<Satellite>();
		
		double Z = 0;
		double a = 1;
		double b = Math.sqrt( 1 - e * e );			
		double x0 = Math.sqrt( a * a - b * b );

		for ( int i = 0; i < num; i++ ) {
			double t = i * div;
			double x = a * Math.cos( t ) + x0;
			double y = b * Math.sin( t );
			Satellite sat1 = new Satellite( x, y, Z );
			Satellite sat2 = sat1;
			if ( inc > 0 ) {
				sat2 = sat1.rotateY( inc * DEG_TO_RAD );
			}
			list.add( sat2 );
		}
		return list;
	}

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
		mCurrentNum ++;
		if ( mCurrentNum >= POINT_NUM ) {
			mCurrentNum = 0;
		}
		mSurfaceView.updateNum( mCurrentNum );
		mSatelliteView.updateNum( mCurrentNum );
		if ( isTouchLeft ) {
			mSurfaceView.moveLeft();
		}
		if ( isTouchRight ) {
			mSurfaceView.moveRight();
		}
	}
			
	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String str ) {
		Log.d( TAG, str );
	}
	       
}
