package jp.ohwada.android.inputmanagersample2;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.Handler;
import android.view.InputDevice;

/**
 * InputDeviceManager
 */ 
public class InputDeviceManager {

	/**
	 * interface OnInputDeviceListener
	 */        
    public interface OnInputDeviceListener {
        void onInputDeviceAdded( InputDeviceRecord record );
        void onInputDeviceRemoved( InputDeviceRecord record );
		void onInputDeviceChanged( InputDeviceRecord record );
    }
		
	private InputManager mInputManager;
    private OnInputDeviceListener mOnInputDeviceListener;

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
	 * setOnInputDeviceListener
	 * @param OnInputDeviceListener listener
	 */
    public void setOnInputDeviceListener( OnInputDeviceListener listener ) {
        mOnInputDeviceListener = listener;
    }

	/**
	 * InputDeviceListener
	 */    
	private InputDeviceListener mInputDeviceListener = new InputDeviceListener() {
        @Override
        public void onInputDeviceAdded( int deviceId ) {
        	if ( mOnInputDeviceListener != null ) {
            	mOnInputDeviceListener.onInputDeviceAdded( getRercord( deviceId ) );
        	}
        }
        @Override
        public void onInputDeviceRemoved( int deviceId ) {
        	if ( mOnInputDeviceListener != null ) {
            	mOnInputDeviceListener.onInputDeviceRemoved( getRercord( deviceId ) );
        	}         
        }
        @Override
        public void onInputDeviceChanged( int deviceId ) {
        	if ( mOnInputDeviceListener != null ) {
            	mOnInputDeviceListener.onInputDeviceChanged( getRercord( deviceId ) );
        	}
        }
    };  

	/**
	 * getRercord
	 * @param int deviceId
	 * @return InputDeviceRecord
	 */ 
    private InputDeviceRecord getRercord( int deviceId ) {
    	InputDeviceRecord record = new InputDeviceRecord();
		record.deviceId = deviceId;
		record.inputDevice = getInputDevice( deviceId );
	 	record.deviceInfo = getInfo( record.inputDevice );
        return record;
    }
    
	/**
	 * getInputDevice
	 * @param int deviceId
	 * @return InputDevice
	 */ 
	private InputDevice getInputDevice( int deviceId ) {
		return mInputManager.getInputDevice( deviceId );
	}

	/**
	 * getgetInfo
	 * @param InputDevice device
	 * @return String
	 */ 
	private String getInfo( InputDevice device ) {
		String str = null;
		if ( device != null ) {
			str = device.toString();
		}
		return str;
	}
		     
}
