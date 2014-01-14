package jp.ohwada.android.evy1;

import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.hardware.usb.UsbDevice;

/**
 * Evy1Manager
 */	
public class Evy1Manager extends UsbMidiManager {

	// special channel
	public static final int CH_VOCALOID = 0;
	
	// vendor
	private static final int EVY1_VENDOR_ID = 10552;
	private static final int EVY1_PRODUCT_ID = 258;

    /**
	 * constructor
	 * @param Context context
     */	    
	public Evy1Manager( Context context ) {
		super( context ); 
	}

	/**
	 * setDevice
	 * @param UsbDevice device
	 * @return boolean 
	 */  
	protected boolean setDevice( UsbDevice device ) {
		if ( device == null ) return false;
		if ( !checkVendor( device, EVY1_VENDOR_ID )) return false;
		if ( !checkProduct( device, EVY1_PRODUCT_ID )) return false;
		return super.setDevice( device );
	}
			
}
