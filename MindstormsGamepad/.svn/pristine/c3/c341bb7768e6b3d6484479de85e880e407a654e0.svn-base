package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.OrientationSensorManager;
import jp.ohwada.android.mindstormsgamepad.util.PowerOrientation;
import jp.ohwada.android.mindstormsgamepad.view.NineButtonsView;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Orientation Activity
 */
public class OrientationActivity extends CommonActivity {
	
	/** Debug */
	protected String TAG_SUB = "OrientationActivity";

	// class object	
	private OrientationSensorManager mOrientationSensorManager;			
    private PowerOrientation mPowerOrientation;
    
	// view
	private NineButtonsView mNineButtonsView;

	// local variable				
	private boolean isStatus = false; 

 	/**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
    	log_d( "onCreate" );   
		super.onCreate( savedInstanceState );
		/* no status bar */
		getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ); 
		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_orientation, null );
        setContentView( view );

        /* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_orientation );
		
		/* MindstormsCommand */
		initTabSub( TAG_SUB );	
		initButtonBack();

		/* PowerJoystick */
		mPowerOrientation = new PowerOrientation(
			MindstormsCommand.MAX_POWER,
			MindstormsCommand.ORIENTATION_MIN_POWER );
						
		/* OrientationSensor */
		mOrientationSensorManager = new OrientationSensorManager( this );
		mOrientationSensorManager.setOnSensorListener( new OrientationSensorManager.OnSensorListener() { 
	      	@Override 
        	public void onSensorChanged( SensorEvent event, int pitch, int roll ) {
				calcRobot( pitch, roll );
        	} 
        });		
					
		/* view */		 
		mNineButtonsView = new NineButtonsView( this, view );
		mNineButtonsView.setOnTouchListener( new NineButtonsView.OnButtonTouchListener() { 
			@Override 
        	public void onTouch( View view, MotionEvent event, int code ) {
				execTouch( event, code ); 
        	} 
		});
	}


	/**
	 * === onResume ===
	 */
	@Override
	public void onResume() {
		log_d( "onResume()" );
		super.onResume();
		startService();
		mOrientationSensorManager.registerListener();
    }

 	/**
	 * === onPause ===
	 */
	@Override
	public void onPause() {
		log_d( "onPause()" );
		super.onPause();
		sendStop();
		mOrientationSensorManager.unregisterListener();
	}
    
// --- Touch ---
 	/**
	 * execTouch
	 * @param MotionEvent event
	 * @param int code
	 */	
	private void execTouch( MotionEvent event, int code ) {
		if (( event.getAction() == MotionEvent.ACTION_DOWN ) &&
		    ( code == NineButtonsView.BUTTON_CENTER )) { 
			if ( isStatus ) {
				isStatus = false;
				mNineButtonsView.setCenterStop();
        	} else {
				isStatus = true;
				mNineButtonsView.setCenterRun();	
        	}
			sendStop();
		} 
	}

	/**
	 * calcRobot
	 * @param int pitch 
	 * @param int roll
	 */ 
 	private void calcRobot( int pitch, int roll ) {
        if ( !isStatus ) return;	
		mPowerOrientation.calc( pitch, roll );
		mNineButtonsView.showImageDirection( mPowerOrientation.getDirection() );
		sendMove( mPowerOrientation.getLeft(), mPowerOrientation.getRight() ); 
	}
	
}
