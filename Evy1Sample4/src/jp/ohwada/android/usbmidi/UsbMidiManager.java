package jp.ohwada.android.usbmidi;

import java.util.List;

import jp.ohwada.android.usb.UsbBaseManager;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

/**
 * UsbMidiManager
 */	
public class UsbMidiManager extends UsbBaseManager {

	// interface parameter
	private static final int SUBCLASS_MIDI_STREAMING = 3;	
	private static final int[] TYPES = new int[]{ 
		UsbConstants.USB_ENDPOINT_XFER_INT, 
		UsbConstants.USB_ENDPOINT_XFER_BULK };

	// variable
	private UsbDevice mDevice;
	private UsbMidiOutput mOutput;

    /**
	 * constructor
	 * @param Context context
     */	    
	public UsbMidiManager( Context context ) {
		super( context );
		TAG_SUB = "UsbMidiManager";
	}

	/**
	 * get MIDI output device
	 * @return UsbMidiOutput
	 */
	public UsbMidiOutput getUsbMidiOutput() {
		return mOutput;
	}

    /**
	 * open
     */	
	public void open() {
		registerAttached();
		registerDetached();
		registerPermission();
	}

    /**
	 * close
     */	
	public void close() {
		unregister();
		closeDevice();
	}

	/**
	 * findDevice
	 * @return UsbDevice
	 */	
	public UsbDevice findDevice() {
		List<UsbDevice> list = getDeviceList();
		for ( int i = 0; i < list.size(); i++ ) {
			UsbDevice device = list.get( i );;
			boolean ret = setDevice( list.get( i ) );
			if ( ret ) {
				return device;
			}
		}
		return null;
	}
	
	/**
	 * setDevice
	 * @param UsbDevice device
	 * @return boolean 
	 */  
	protected boolean setDevice( UsbDevice device ) {
		log_d( "setDevice " + device );
		if ( device == null ) return false;
		if ( mOutput != null ) return false;
	   	List<UsbInterface> list_intf = findInterface( 
	   		device, 
	   		UsbConstants.USB_CLASS_AUDIO, 
	   		SUBCLASS_MIDI_STREAMING );
	   	if ( list_intf .size() == 0 ) {
            log_d( "could not find interface" );
	   		return false;
	   	}
	   	UsbInterface usbInterface = list_intf.get(0);
	   	List<UsbEndpoint> list_ep = findEndpoint( 
	   		usbInterface, 
	   		UsbConstants.USB_DIR_OUT, 
	   		TYPES );
	   	if ( list_ep.size() == 0 ) {
			log_d( "could not find endpoint" );
	   		return false;
	   	}
	   	UsbEndpoint endpoint = list_ep.get(0);
	   	UsbDeviceConnection connection = openDevice( device );
        if ( connection == null ) {
            log_d( "could not open device" );
        	return false;
        }
		mDevice = device ;
		mOutput = new UsbMidiOutput( usbInterface, endpoint, connection );
        return true;
	}

	/**
	 * closeDevice
	 */
	private void closeDevice() {
		if (mOutput != null) {
			mOutput.close();
			mOutput = null;
		}
		mDevice = null;
	}

	/**
	 * receiveDetached
	 * @param UsbDevice device
	 */
   	protected void receiveDetached( UsbDevice device ) {
	   	log_d( "receiveDetached" );
		if ( !matchDevice( device, mDevice ) ) return;
		closeDevice();
		notifyDetached( device );
   	}

	/**
	 * receivePermission
	 * @param UsbDevice device
	 */ 
	protected void	receivePermission( UsbDevice device ) {
	   	log_d( "receivePermission" );
	   	boolean ret = setDevice( device );
	   	if ( ret ) {
        	notifyAttached( device );
        }
   	}
   	
}
