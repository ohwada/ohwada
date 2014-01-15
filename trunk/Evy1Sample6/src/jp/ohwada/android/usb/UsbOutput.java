package jp.ohwada.android.usb;

import java.nio.ByteBuffer;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.util.Log;

/**
 * Output Device
 */
public class UsbOutput {
	// debug
	protected String TAG_SUB = "UsbOutput";
	
	// interval		
	private final static int SLEEP_TIME = 1;	

	// parameter														
	private UsbInterface mInterface;
	private UsbDeviceConnection mConnection;
	private UsbEndpoint mEndpoint;
	private UsbRequest mRequest;

	/**
	 * constructor
	 * @param UsbInterface _interface
	 * @param UsbEndpoint endpoint
	 * @param UsbDeviceConnection connection	 
	 */
	public UsbOutput( UsbInterface _interface, UsbEndpoint endpoint, UsbDeviceConnection connection ) {
		log_d( "UsbOutput " + connection );
		mConnection = connection;
		mInterface = _interface;
		mEndpoint = endpoint;
	}

	/**
	 * openConnection
	 */
	protected boolean openConnection() {
		mConnection.claimInterface( mInterface, true );
		return true;
	}

	/**
	 * closeConnection
	 */
	protected void closeConnection() {
		if (mRequest != null) {
			mRequest.close();
		}
		mConnection.releaseInterface( mInterface );
		mConnection.close();
	}
				
	/**
	 * sendByteBuffer
	 * @param ByteBuffer buffer
	 * @param int length
	 */
	protected void sendByteBuffer( ByteBuffer buffer, int length ) {
		// UsbRequest.queue() is not thread-safe
		synchronized (mConnection) {
			if (mRequest == null) {
				mRequest = new UsbRequest();
				mRequest.initialize( mConnection, mEndpoint );
			}
			while (mRequest.queue( buffer, length ) == false) {
				// loop until queue completed
				sleepOneMilli();
			}
			while (mRequest.equals( mConnection.requestWait() ) == false) {
				// loop until result received
				sleepOneMilli();
			}
		}
	}

	/**
	 * sleepOneMilli
	 */		
	private void sleepOneMilli() {
		try {
			Thread.sleep( SLEEP_TIME );
		} catch (InterruptedException e) {
			if (UsbConstant.D) e.printStackTrace();
		}
	}

	/**
	 * logcat
	 * @param String msg
	 */
	protected void log_d( String msg ) {
		if (UsbConstant.D) Log.d( UsbConstant.TAG, TAG_SUB + " " + msg );	
	}
}
