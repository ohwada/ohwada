package jp.ohwada.android.gamepadjcu2312f;

import java.util.List;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.MotionEvent;

/**
 * InputDeviceManager
 */ 
public class InputDeviceManager {
	
	private static final boolean D = true;
    private static final String TAG = "InputDeviceManager"; 

	private static final String LF = "\n";
								
	/**
	 * interface OnInputDeviceListener
	 */        
    public interface OnInputDeviceListener {
        void onInputDeviceAdded( int deviceId );
        void onInputDeviceRemoved( int deviceId );
		void onInputDeviceChanged( int deviceId );
    }
		
	private InputManager mInputManager;
    private OnInputDeviceListener mOnInputDeviceListener;
    private InputDevice mInputDevice; 
        								
	/**
	 * constractor
	 * @param Context context
	 */ 
	public InputDeviceManager( Context context ) {
		mInputManager = (InputManager) context.getSystemService( Context.INPUT_SERVICE ) ; 
	}

	/**
	 * register
	 */ 
    public void register() {
		mInputManager.registerInputDeviceListener( mInputDeviceListener, new Handler() );
   }

	/**
	 * unregister
	 */ 
    public void unregister() {
		mInputManager.unregisterInputDeviceListener( mInputDeviceListener );
   }

	/**
	 * getInputDevices
	 */ 
	public String getInputDevices() {
    	String str = "";
		int[] ids = mInputManager.getInputDeviceIds();
        for ( int i = 0; i < ids.length; i++ ) {
            str += addInputDevice( ids[i] );
        }
        log_d( str );
        return str;
    }

	/**
	 * isJoystickClass
	 * @param int source
	 * @return boolean
	 */	
	private boolean isJoystickClass( int source ) {
		return isSourceClass( source, InputDevice.SOURCE_CLASS_JOYSTICK );
    }

	/**
	 * isSourceClass
	 * @param int source
	 * @param int kind
	 * @return boolean
	 */	
	private boolean isSourceClass( int source, int kind ) {
        return ( source & kind & InputDevice.SOURCE_CLASS_MASK ) != 0;
    }

	/**
	 * getSourceString
	 * @param int source
	 * @return String
	 */	
    private String getSourceString( int source ) {
		String str = "source: 0x" + String.format( "%08X", source ) + " " ;
		return str;
	}
	           
	/**
	 * setOnInputDeviceListener
	 * @param OnInputDeviceListener listener
	 */
    public void setOnInputDeviceListener( OnInputDeviceListener listener ) {
        mOnInputDeviceListener = listener;
    }

// ==== InputDeviceListener ====
	/**
	 * InputDeviceListener
	 */    
	private InputDeviceListener mInputDeviceListener = new InputDeviceListener() {
        @Override
        public void onInputDeviceAdded( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceAdded( deviceId );
        	}
        }
        @Override
        public void onInputDeviceRemoved( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceRemoved( deviceId );
        	}
        }
        @Override
        public void onInputDeviceChanged( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceChanged( deviceId );
        	}
        }
    };  

// --- addInputDevice ---
	/**
	 * addInputDevice
	 * @param int deviceId
	 * @return String device name
	 */
	public String addInputDevice( int deviceId ) {    
    	String msg = "";
		InputDevice device = mInputManager.getInputDevice( deviceId );
		if ( device == null ) return "";
		msg += device.toString() + LF;
		if ( isJoystickClass( device.getSources() ) ) {
            if ( !isDeviceMatch( device ) ) {
                msg += setJoystickDevice( device );
            }    
		}
		log_d( msg );
		return msg;
    }

	/**
	 * isDeviceMatch
	 * @param InputDevice device
	 * @return  boolean
	 */
	private boolean isDeviceMatch( InputDevice device ) {
		if (( mInputDevice != null )&&( mInputDevice.getId() != device.getId() )) {
			return true;
		}
		return false;
    }

	/**
	 * getDeviceName
	 * @return String
	 */
	private String getDeviceName() {
		String str = "";
		if ( mInputDevice != null ) {
			str = mInputDevice.getName();
		}
		return str;
    }

	/**
	 * setJoystickDevice
	 * @param InputDevice device
	 * @return String
	 */    
	private String setJoystickDevice( InputDevice device ) {
		String str = "";
		mInputDevice = device;

		List<MotionRange> ranges = device.getMotionRanges();
		for ( MotionRange range : ranges ) {
			int source = range.getSource();
			int axis = range.getAxis();
			str += getSourceString( source ) + LF;
			str += "axis: " + MotionEvent.axisToString( axis ) + LF;
		}
		return str;
	}

// --- removeInputDevice ---
	/**
	 * removeInputDevice
	 * @param int deviceId
	 * @return String device name
	 */		 	
    public String removeInputDevice( int deviceId ) {
    	String str = "";
    	InputDevice device = mInputManager.getInputDevice( deviceId );
		if ( device == null ) return "";
		if ( isJoystickClass( device.getSources() ) ) {
            if ( isDeviceMatch( device ) ) {
            	str = getDeviceName();
                mInputDevice = null;
            }    
		}
		return str;
    }

	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}

}
