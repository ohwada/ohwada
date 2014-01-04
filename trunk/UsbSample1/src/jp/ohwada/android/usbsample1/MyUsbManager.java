package jp.ohwada.android.usbsample1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * MainActivity
 */	
public class MyUsbManager {

	private final static String TAG = "MyUsbManager";
	private final static boolean D = true;	
	
	private Context mContext;
	
	private List<String> mList = new ArrayList<String>();

  // callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onAttached( UsbDevice device );
        public void onDetached( UsbDevice device );
    }
   	
	public MyUsbManager( Context context ) {
		mContext = context;
	}
	
	/**
	 * === onCreate ===
	 */	
	public void open() {
		IntentFilter filter1 = new IntentFilter( 
			UsbManager.ACTION_USB_DEVICE_ATTACHED );
		IntentFilter filter2 = new IntentFilter( 
			UsbManager.ACTION_USB_DEVICE_DETACHED );
		mContext.registerReceiver( mReceiver, filter1 );
		mContext.registerReceiver( mReceiver, filter2 );
	}

	/**
	 * === onDestroy ===
	 */
	public void close() {
		mContext.unregisterReceiver( mReceiver );
	}

	/**
	 * getDeviceList
	 * @return List<String>
	 */	
	public List<String> getDeviceList() {
		UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
	  	HashMap<String, UsbDevice>	hash = manager.getDeviceList();
	  	if ( hash == null ) return null;
	  	clearMessage();
	  	String key = "";
	  	UsbDevice device = null;
	  	Iterator<String> it = hash.keySet().iterator();
        while ( it.hasNext() ) {
            key = it.next();
            device = hash.get( key );
            getDeviceMessage( device );
        }
        return mList;
	}
	
	/**
	 * getDeviceInfo
	 * @param UsbDevice device
	 * @return List<String>
	 */	
	public List<String> getDeviceInfo( UsbDevice device ) {  
	   	if (device == null) return null;
	  	clearMessage();
	   	return getDeviceMessage( device );
	}

	/**
	 * getDeviceMessage
	 * @param UsbDevice device
	 * @return List<String>
	 */	
	private List<String> getDeviceMessage( UsbDevice device ) {  	   	
	   	addMessage( "Device" );
	   	addMessage( device.toString() );
	   	// Interface
        int intf_count = device.getInterfaceCount();
        for (int i = 0; i < intf_count; i++) {
            UsbInterface intf = device.getInterface(i);
            addMessage( "Interface " + i );
            addMessage( intf.toString() );
            int cls = intf.getInterfaceClass();
            int sub = intf.getInterfaceSubclass();
            int pro = intf.getInterfaceProtocol();
            addMessage( UsbDeviceInfo.getClass( cls ));
            addMessage( UsbDeviceInfo.getSubclass( cls, sub ));
            addMessage( UsbDeviceInfo.getProtocol( cls, pro ));
	   		// Endpoint
			int ep_count = intf.getEndpointCount();
			for (int j = 0; j < ep_count; j++) {
				UsbEndpoint endpoint = intf.getEndpoint(j);
				addMessage( "Endpoint " + j );
            	addMessage( endpoint.toString() );           	
            	addMessage( UsbDeviceInfo.getDirection( endpoint.getDirection() ) );
            	addMessage( UsbDeviceInfo.getType( endpoint.getType() ) );
			}
		}
        return mList;
 	}

	/**
	 * clearMessage
	 */	
	private void clearMessage() {
		mList.clear();
	}

	/**
	 * addMessage
	 * @param String msg
	 */	
	private void addMessage( String msg ) {
		mList.add( msg );
		log_d( msg );
	}

     /**
     * setOnChangedListener
     * @param OnChangedListener listener
     */
    public void setOnChangedListener( OnChangedListener listener ) {
        mListener = listener;
    }

	/**
	 * notifyAttached
	 * @param UsbDevice device
	 */
	private void notifyAttached( UsbDevice device ) {
		if (( mListener != null )&&( device != null )) {
			mListener.onAttached( device );
		}
	}

	/**
	 * notifyDetached
	 * @param UsbDevice device
	 */
	private void notifyDetached( UsbDevice device ) {
		if (( mListener != null )&&( device != null )) {
			mListener.onDetached( device );
		}
	}

	/**
	 * --- BroadcastReceiver ---
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
    	public void onReceive( Context context, Intent intent ) {
  			String action = intent.getAction();
   			log_d( "onReceive " + action );
   			UsbDevice device = intent.getParcelableExtra( UsbManager.EXTRA_DEVICE );
 	   		if ( device == null ) return;
			if ( UsbManager.ACTION_USB_DEVICE_ATTACHED.equals( action )) {
   				notifyAttached( device );
   			} else if ( UsbManager.ACTION_USB_DEVICE_DETACHED.equals( action )) {
   				notifyDetached( device );
			}
    	}
	};

	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );	
	}	
}
