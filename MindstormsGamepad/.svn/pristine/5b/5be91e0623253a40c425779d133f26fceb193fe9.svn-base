package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.PowerJoystick;
import jp.ohwada.android.mindstormsgamepad.view.NineButtonsView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * Joystick Activity
 */
public class JoystickActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "JoystickActivity";

    private SharedPreferences mPreferences;
    private PowerJoystick mPowerJoystick;
    
	// view
	private NineButtonsView mNineButtonsView;

	private int mAxisX = 0 ;
	private int mAxisY = 0 ;
	private boolean isAxisSignX = false ;
	private boolean isAxisSignY = false ;

	private int mSettingAxisX = 0 ;
	private int mSettingAxisY = 0 ;
	private boolean isSettingAxisSignX = false ;
	private boolean isSettingAxisSignY = false ;
		
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
		View view = getLayoutInflater().inflate( R.layout.activity_joystick, null );
		setContentView( view ); 

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_joystick );
		initButtonBack();
		initInputDeviceManager();
		initSeekbarPower( view ); 
		
		/* SharedPreferences */
		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
		getPref();

		/* PowerJoystick */
		mPowerJoystick = new PowerJoystick(
			MindstormsCommand.MAX_POWER,
			MindstormsCommand.ORIENTATION_MIN_POWER );
		
		/* view component */
		mNineButtonsView = new NineButtonsView( this, view );
		mNineButtonsView.hideImage();
		mNineButtonsView.setOnTouchListener( new NineButtonsView.OnButtonTouchListener() { 
	      	@Override 
        	public void onTouch( View view, MotionEvent event, int code ) {
        		execTouch( code );
        	} 
        });
		              
    }

	/**
	 * execTouch
	 * @param int code
	 */
	private void execTouch( int code ) {
		if ( !isSetting ) return;
		switch( code ) {
			case NineButtonsView.BUTTON_SAVE:
				closeSetting();
				savePref();
				break;
			case NineButtonsView.BUTTON_CANCEL:
				closeSetting();
				break;
		} 
	}

	/**
	 * closeSetting
	 */	
	private void closeSetting() {
		isSetting = false;
		mNineButtonsView.hideSetting();
		mNineButtonsView.hideImage();
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
		float x = mInputDeviceManager.getAxisValue( mAxisX, isAxisSignX );
		float y = mInputDeviceManager.getAxisValue( mAxisY, isAxisSignY );
		mPowerJoystick.setMainPower( mSeekbarPower.getPowerMain() );
		mPowerJoystick.calc( x, y );
		mNineButtonsView.showImageDirection( mPowerJoystick.getDirection() );
		sendMove( mPowerJoystick.getLeft(), mPowerJoystick.getRight() ); 
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
				mSettingAxisY = axis ;
				isSettingAxisSignY = sign ;
				mNineButtonsView.showImageCode( NineButtonsView.BUTTON_LEFT );
				break;	
			case 2:
				mSettingNum = 0;
				mSettingAxisX = axis ;
				isSettingAxisSignX = sign ;
				mNineButtonsView.setOnEightButons();
				break;	
		}
		showSettingLabel();

   	}

 	/**
	 * showSettingLabel
	 */ 
	private void showSettingLabel() {
		mNineButtonsView.setSettingLabel( 
			mInputDeviceManager.getAxisLable( mSettingAxisY, isSettingAxisSignY ),
			mInputDeviceManager.getAxisLable( mSettingAxisX, isSettingAxisSignX ) );
	}

// --- dispatchKeyEvent end ---

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.joystick, menu );
        return true;
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_joystick:
				execOptionsItemJoystick();
				return true;
		}
		execOptionsItemSelected( item );
        return true;
    }

 	/**
	 * execOptionsItemJoystick
	 */
    private void execOptionsItemJoystick() {
		isSetting = true;
		mSettingNum = 1;
		// set current value
		mSettingAxisX = mAxisX ;
		mSettingAxisY = mAxisY ;
		isSettingAxisSignX = isAxisSignX ;
		isSettingAxisSignY = isAxisSignY ;
		// show setting
		mNineButtonsView.showSetting();
		mNineButtonsView.showImageCode( NineButtonsView.BUTTON_FORWARD );
		showSettingLabel();
		mInputDeviceManager.clearFirstMove();
	}
				
// SharedPreferences
	/**
	 * getPref
	 */
	private void getPref() {	
		mAxisX = mPreferences.getInt( 
			Constant.PREF_AXIS_X, MotionEvent.AXIS_X );
		mAxisY = mPreferences.getInt( 
			Constant.PREF_AXIS_Y, MotionEvent.AXIS_Y );
		isAxisSignX = mPreferences.getBoolean( 
			Constant.PREF_AXIS_SIGN_X, false );
		isAxisSignY = mPreferences.getBoolean( 
			Constant.PREF_AXIS_SIGN_Y, false );
	}

	/**
	 * savePref
	 */
	private void savePref() {	
		// set new value
		mAxisX = mSettingAxisX ;
		mAxisY = mSettingAxisY ;
		isAxisSignX = isSettingAxisSignX ;
		isAxisSignY = isSettingAxisSignY ;
		// save
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt( Constant.PREF_AXIS_X, mAxisX );
		editor.putInt( Constant.PREF_AXIS_Y, mAxisY );
		editor.putBoolean( Constant.PREF_AXIS_SIGN_X, isAxisSignX );
		editor.putBoolean( Constant.PREF_AXIS_SIGN_Y, isAxisSignY );
		editor.commit();
	}

}
