package jp.ohwada.android.inputmanagersample2;

import android.view.InputDevice;

/**
 * InputDeviceRecord
 */ 
public class InputDeviceRecord {

	private static final String LF = "\n";

	public InputDevice inputDevice = null;
	public int deviceId = 0;
	public String deviceInfo = "";

	/**
	 * constractor
	 */ 		
	public InputDeviceRecord() {
		// dummy
	}

	/**
	 * toString
	 * @return String
	 */
	public String toString() {
		String text = "";
		text += "device ID: " + deviceId + LF;
		text += deviceInfo + LF;
		return text;
	}
		     
}
