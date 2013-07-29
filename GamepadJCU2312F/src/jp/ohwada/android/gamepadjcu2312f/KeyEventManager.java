package jp.ohwada.android.gamepadjcu2312f;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.InputDevice;
import android.view.KeyEvent;

/**
 * KeyEventManager
 */ 
public class KeyEventManager {
	
	private static final boolean D = true;
    private static final String TAG = "KeyEventManager"; 

	private SparseBooleanArray mSparseCode = new SparseBooleanArray();
	private boolean isFirstDown = false;
	private int mFirstDownCode = 0;
        								
	/**
	 * constractor
	 */ 
	public KeyEventManager() {
		// dummy
	}

// --- execKeyEvent ---
	/**
	 * execKeyDown
	 * @param int keyCode
	 * @param KeyEvent event
	 * @return boolean
	 */ 
	public boolean execKeyDown( int keyCode, KeyEvent event ) {
		log_d( event.toString() );

		if ( isJoystickGamepad( event ) ) {
			processKeyDown( keyCode, event );
			return true;
		}
		return false;
	}

	/**
	 * execKeyUp
	 * @param int keyCode
	 * @param KeyEvent event
	 * @return boolean
	 */ 
	public boolean execKeyUp( int keyCode, KeyEvent event ) {
		log_d( event.toString() );

		if ( isJoystickGamepad( event ) ) {
			processKeyUp( keyCode, event );
			return true;
		}
		return false;
	}

	/**
	 * getSparseCode
	 * @return SparseBooleanArray 	 
	 */	
	public SparseBooleanArray getSparseCode() {
		return mSparseCode;
	}

	/**
	 * isFirstDown
	 * @return boolean
	 */
	public boolean isFirstDown() {
		return isFirstDown;
	}
	
	/**
	 * getFirstDownCode
	 * @return int 
	 */
	public int getFirstDownCode() {
		return mFirstDownCode;
	}
			
	/**
	 * processKeyDown
	 * @param int code
	 * @param KeyEvent event
	 */ 		
	private void processKeyDown( int code, KeyEvent event ) {
		mSparseCode.put( code, true );
		isFirstDown = false;
		if ( event.getRepeatCount() == 0 ) {
			isFirstDown = true;
			mFirstDownCode = code;
		}	
	}

	/**
	 * processKeyUp
	 * @param int code
	 * @param KeyEvent event
	 */ 		
	private void processKeyUp( int code, KeyEvent event ) {
		mSparseCode.put( code, false );	
	}

	/**
	 * iisJoystickGamepad
	 * @param KeyEvent event
	 * @return boolean
	 */	
	private boolean isJoystickGamepad( KeyEvent event ) {
		int source = event.getSource();
		if ( isJoystick( source ) ) return true;
		if ( isGamepad( source ) ) return true;
		return false;
	}
		
	/**
	 * isJoystick
	 * @param int source
	 * @return boolean
	 */	
	private boolean isJoystick( int source ) {
		return isSource( source, InputDevice.SOURCE_JOYSTICK );
    }

	/**
	 * isGamepad
	 * @param int source
	 * @return boolean
	 */	
	private boolean isGamepad( int source ) {
		return isSource( source, InputDevice.SOURCE_GAMEPAD );
    }

	/**
	 * isSource
	 * @param int source
	 * @param int kind
	 * @return boolean
	 */	
	private boolean isSource( int source, int kind ) {
        return ( source & kind & ~InputDevice.SOURCE_CLASS_MASK ) != 0;
    }

	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}

}
