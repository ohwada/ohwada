package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.view.JoystickTwoView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * TwoJoystick Activity
 */
public class TwoJoystickActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "TwoJoystickActivity";

    private SharedPreferences mPreferences;
 
	/** view component */
	private JoystickTwoView mJoystickTwoView;

	private int mAxisLeft = 0 ;
	private int mAxisRight = 0 ;
	private boolean isAxisSignLeft = false ;
	private boolean isAxisSignRight = false ;

	private int mSettingAxisLeft = 0 ;
	private int mSettingAxisRight = 0 ;
	private boolean isSettingAxisSignLeft = false ;
	private boolean isSettingAxisSignRight = false ;
		
	private boolean isSetting = false;
	private int mSettingNum = 0; 
								            
// --- onCreate ---
 	/**
	 * === onCreate ===
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
    	log_d( "onCreate" );   		
        super.onCreate( savedInstanceState );
		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_two_joystick, null );
		setContentView( view ); 

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_two_joystick );
		initButtonBack();
		initInputDeviceManager();	
		initSeekbarPower( view ); 
		
		/* SharedPreferences */
		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
		getPref();
						       
		/* view component */		
		mJoystickTwoView = new JoystickTwoView( view );
		mJoystickTwoView.setOnClickListener( new JoystickTwoView.OnButtonClickListener() { 
	      	@Override 
        	public void onClick( View view, int code ) {
        		execClick( code );
        	} 
        });
			                                
    }

	/**
	 * execClick
	 * @param int code
	 */
	private void execClick( int code ) {
		if ( !isSetting ) return;
		switch( code ) {
			case JoystickTwoView.BUTTON_SAVE:
				closeSetting();
				savePref();
			case JoystickTwoView.BUTTON_CANCEL:
				closeSetting();
				break;
		} 
	}

	/**
	 * closeSetting
	 */	
	private void closeSetting() {
		isSetting = false;
		mJoystickTwoView.hideSetting();
		mJoystickTwoView.showImageIndividual( false, false, false, false );
	}
// --- onCreate end ---

	/**
	 * === onResume ===
	 */
	@Override
	public void onResume() {
		log_d( "onResume()" );
		super.onResume();
		startService();
		mInputDeviceManager.register();	
    }

 	/**
	 * === onPause ===
	 */
	@Override
	public void onPause() {
		log_d( "onPause()" );
		super.onPause();
        sendStop();
		mInputDeviceManager.unregister();
	}

// --- dispatchKeyEvent ---
	/**
	 * === dispatchGenericMotionEvent ===
	 */
	@Override
    public boolean dispatchGenericMotionEvent( MotionEvent event ) {
		boolean ret = mInputDeviceManager.dispatchMotionEvent( event );
		if ( ret ) {
			if ( isSetting ) {
				if ( mInputDeviceManager.isFirstMove() ) {
	    			setAxis( 
	    				mInputDeviceManager.getFirstMoveAxis(),
	    				mInputDeviceManager.getFirstMoveAxisValue() );
	    		}
	    	} else {	
				execCommand();
			}	
			return true;			
        }
        return super.dispatchGenericMotionEvent( event );
    }

 	/**
	 * execCommand
	 */
    private void execCommand( ) {
		float left =  mInputDeviceManager.getAxisValue( mAxisLeft, isAxisSignLeft );
		float right = mInputDeviceManager.getAxisValue( mAxisRight, isAxisSignRight );
				
		boolean is_left_forward = false;
		boolean is_left_back = false;
		boolean is_right_forward = false;
		boolean is_right_back = false;

		int POWER = mSeekbarPower.getPowerMain();
		int left_power = 0;
		int right_power = 0;	

		if ( left < -0.5 ) {
			is_left_forward = true;
			left_power = POWER;
		} else if ( left > 0.5 ) {
			is_left_back = true;
			left_power = -POWER;
		}
		if ( right < -0.5 ) {
			is_right_forward = true;
			right_power = POWER;	
		} else if ( right > 0.5 ) {
			is_right_back = true;
			right_power = -POWER;	
		}

		mJoystickTwoView.showImage( is_left_forward, is_left_back, is_right_forward, is_right_back );
		sendMove( left_power, right_power ); 
	}

 	/**
	 * setAxis
	 * @param int axis
	 * @param float value 
	 */ 
	private void setAxis( int axis, float value ) {
		boolean sign = value > 0 ? true : false ;
		switch ( mSettingNum ) {
			case 1:
				mSettingNum = 2;
				mSettingAxisLeft = axis ;
				isSettingAxisSignLeft = sign ;
				mJoystickTwoView.showImageIndividual( false, false, true, false );
				break;	
			case 2:
				mSettingNum = 0;
				mSettingAxisRight = axis ;
				isSettingAxisSignRight = sign ;
				mJoystickTwoView.showImageIndividual( true, true, true, true );
				break;	
		}
		showSettingLabel();

   	}

 	/**
	 * showSettingLabel
	 */ 
	private void showSettingLabel() {
		mJoystickTwoView.setSettingLabel( 
			mInputDeviceManager.getAxisLable( mSettingAxisLeft, isSettingAxisSignLeft ),  "",
			mInputDeviceManager.getAxisLable( mSettingAxisRight, isSettingAxisSignRight ), "" );
	}
					
// --- dispatchKeyEvent end ---

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.two_joystick, menu );
        return true;
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.two_joystick:
				isSetting = true;
				mSettingNum = 1;
				// set current value
				mSettingAxisLeft = mAxisLeft ;
				mSettingAxisRight = mAxisRight ;
				isSettingAxisSignLeft = isAxisSignLeft ;
				isSettingAxisSignRight = isAxisSignRight ;
				// show setting
				mJoystickTwoView.showSetting();
				mJoystickTwoView.showImageIndividual( true, false, false, false );
				showSettingLabel();
				mInputDeviceManager.clearFirstMove();
				return true;
		}
		execOptionsItemSelected( item );
        return false;
    }

// SharedPreferences
	/**
	 * getPref
	 */
	private void getPref() {	
		mAxisLeft = mPreferences.getInt( 
			Constant.PREF_AXIS_LEFT, MotionEvent.AXIS_Y );
		mAxisRight = mPreferences.getInt( 
			Constant.PREF_AXIS_RIGHT, MotionEvent.AXIS_Z );
		isAxisSignLeft = mPreferences.getBoolean( 
			Constant.PREF_AXIS_SIGN_LEFT, false );
		isAxisSignRight = mPreferences.getBoolean( 
			Constant.PREF_AXIS_SIGN_RIGHT, false );
	}

	/**
	 * savePref
	 */
	private void savePref() {	
		// set new value
		mAxisLeft = mSettingAxisLeft ;
		mAxisRight = mSettingAxisRight ;
		isAxisSignLeft = isSettingAxisSignLeft ;
		isAxisSignRight = isSettingAxisSignRight ;
		// save
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt( Constant.PREF_AXIS_LEFT, mAxisLeft );
		editor.putInt( Constant.PREF_AXIS_RIGHT, mAxisRight );
		editor.putBoolean( Constant.PREF_AXIS_SIGN_LEFT, isAxisSignLeft );
		editor.putBoolean( Constant.PREF_AXIS_SIGN_RIGHT, isAxisSignRight );
		editor.commit();
	}

}
