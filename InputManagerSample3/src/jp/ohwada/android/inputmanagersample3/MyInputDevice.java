package jp.ohwada.android.inputmanagersample3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.InputDevice;

/**
 * MyInputDevice
 */
public class MyInputDevice {

	private InputDevice mInputDevice;
	private Class<? extends InputDevice> mClass;

	/**
	 * constractor
	 * @param InputDevice device
	 */				
	public MyInputDevice( InputDevice device ) {
		mInputDevice = device;
		mClass = device.getClass();
	}

	/**
     * Returns true if the device is external (connected to USB or Bluetooth or some other
     * peripheral bus), otherwise it is built-in.
	 * execute hide method using reflection
     *
     * @return True if the device is external.
	 */					
	public boolean isExternal() {
		boolean ret = false;
		try {
			Method method = mClass.getMethod( "isExternal" );
			if ( method != null ) {
				ret = (Boolean) method.invoke( mInputDevice );
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * isExternalKeyboard
	 * @return boolean
	 */	
	public boolean isExternalKeyboard() {
		if ( mInputDevice.getSources() == InputDevice.SOURCE_KEYBOARD ) {
			if ( isExternal() ) {
				return true;
			}
		}	
		return false;
	}

}
