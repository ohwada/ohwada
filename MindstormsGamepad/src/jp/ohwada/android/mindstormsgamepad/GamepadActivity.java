package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.view.JoystickTwoView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Gamepad Activity
 */
public class GamepadActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "GamepadActivity";

    private SharedPreferences mPreferences;

	// view
	private JoystickTwoView mJoystickTwoView;

	private int mKeycodeLeftForward = 0 ;
	private int mKeycodeLeftBack = 0 ;
	private int mKeycodeRightForward = 0 ;
	private int mKeycodeRightBack = 0 ;

	private int mSettingKeycodeLeftForward = 0 ;
	private int mSettingKeycodeLeftBack = 0 ;
	private int mSettingKeycodeRightForward = 0 ;
	private int mSettingKeycodeRightBack = 0 ;
		
	private int mSettingNum = 0; 
	private boolean isSetting = false;

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
		setTitleName( R.string.activity_gamepad );
		
		/* MindstormsCommand */		
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
				break;
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
	 * === dispatchKeyEvent ===
	 */
	@Override
	public boolean dispatchKeyEvent( KeyEvent event ) {
		boolean ret = mInputDeviceManager.dispatchKeyEvent( event );
	    if ( ret ) {
	    	if ( isSetting ) {
	    		if ( mInputDeviceManager.isFirstDown() ) {
	    			setKeyCode( mInputDeviceManager.getFirstDownCode() );
	    		}	
	    	} else {	
				execCommand( mInputDeviceManager.getSparseCode() );
			}
			return true;
		} 
		return super.dispatchKeyEvent( event );
   	}

 	/**
	 * execCommand
	 * @param SparseBooleanArray sparse
	 */ 
	private void execCommand( SparseBooleanArray sparse ) {
		boolean isLeftForward = sparse.get( mKeycodeLeftForward );
		boolean isLeftBack = sparse.get( mKeycodeLeftBack );
		boolean isRightForward = sparse.get( mKeycodeRightForward );
		boolean isRightBack = sparse.get( mKeycodeRightBack );
	
		mJoystickTwoView.showImage( isLeftForward, isLeftBack, isRightForward, isRightBack );

		int POWER = mSeekbarPower.getPowerMain();
		int left_power = 0;
		int right_power = 0;		
		if ( isLeftForward ) {
			left_power = POWER;
		} else if ( isLeftBack ) { 	
			left_power = -POWER;
		} else {		
		}
		if ( isRightForward ) {		
			right_power = POWER;
		} else if ( isRightBack ) {		
			right_power = -POWER;
		} else {
		}
		
		sendMove( left_power, right_power ); 
	}
		
 	/**
	 * setKeyCode
	 * @param int code
	 */ 
	private void setKeyCode( int code ) {
		switch ( mSettingNum ) {
			case 1:
				mSettingNum = 2;
				mSettingKeycodeLeftForward = code ;
				mJoystickTwoView.showImageIndividual( false, true, false, false );
				break;
			case 2:
				mSettingNum = 3;
				mSettingKeycodeLeftBack = code ;
				mJoystickTwoView.showImageIndividual( false, false, true, false );
				break;
			case 3:	
				mSettingNum = 4;
				mSettingKeycodeRightForward = code ;
				mJoystickTwoView.showImageIndividual( false, false, false, true );
				break;						
			case 4:
				mSettingNum = 0;
				mSettingKeycodeRightBack = code ;
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
			getCodeString( mSettingKeycodeLeftForward ), 
			getCodeString( mSettingKeycodeLeftBack ), 
			getCodeString( mSettingKeycodeRightForward ), 
			getCodeString( mSettingKeycodeRightBack ) );
	}

 	/**
	 * getCodeString
	 * @param int code
	 * @return String
	 */ 
    private String getCodeString( int code ) {
		String str = KeyEvent.keyCodeToString( code );
		return str.replace( "KEYCODE_", "" );
	}
		
// --- dispatchKeyEvent end ---

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.gamepad, menu );
        return true;
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_gamepad:
				execOptionsItemGamepad();
				return true;
		}
		execOptionsItemSelected( item );
        return true;
    }

 	/**
	 * execOptionsItemGamepad
	 */
    private void execOptionsItemGamepad() {
		isSetting = true;
		mSettingNum = 1;
		// set current value
		mSettingKeycodeLeftForward = mKeycodeLeftForward ;
		mSettingKeycodeLeftBack = mKeycodeLeftBack ;
		mSettingKeycodeRightForward = mKeycodeRightForward ;
		mSettingKeycodeRightBack = mKeycodeRightBack ;
		// show setting
		mJoystickTwoView.showSetting();
		showSettingLabel();
    }
    
// SharedPreferences
	/**
	 * getPref
	 */
	private void getPref() {	
		mKeycodeLeftForward = mPreferences.getInt( 
			Constant.PREF_KEYCODE_LEFT_FORWARD, KeyEvent.KEYCODE_BUTTON_5 );
		mKeycodeLeftBack = mPreferences.getInt( 
			Constant.PREF_KEYCODE_LEFT_BACK, KeyEvent.KEYCODE_BUTTON_7 );
		mKeycodeRightForward = mPreferences.getInt( 
			Constant.PREF_KEYCODE_RIGHT_FORWARD, KeyEvent.KEYCODE_BUTTON_6 );
		mKeycodeRightBack = mPreferences.getInt( 
			Constant.PREF_KEYCODE_RIGHT_BACK, KeyEvent.KEYCODE_BUTTON_8 );
	}
	
	/**
	 * savePref
	 */
	private void savePref() {	
		// set new value
		mKeycodeLeftForward = mSettingKeycodeLeftForward ;
		mKeycodeLeftBack = mSettingKeycodeLeftBack ;
		mKeycodeRightForward = mSettingKeycodeRightForward ;
		mKeycodeRightBack = mSettingKeycodeRightBack ;
		// save
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt( Constant.PREF_KEYCODE_LEFT_FORWARD, mKeycodeLeftForward );
		editor.putInt( Constant.PREF_KEYCODE_LEFT_BACK, mKeycodeLeftBack );
		editor.putInt( Constant.PREF_KEYCODE_RIGHT_FORWARD, mKeycodeRightForward );
		editor.putInt( Constant.PREF_KEYCODE_RIGHT_BACK, mKeycodeRightBack );
		editor.commit();
	}
	
}
