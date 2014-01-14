package jp.ohwada.android.usb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Usb Base Manager
 */	
public class UsbBaseManager {

	// debug
	protected String TAG_SUB = "UsbBaseManager";

	// action
	private static final String ACTION_USB_PERMISSION =
    	"jp.ohwada.android.usb.USB_PERMISSION";

	// PendingIntent
	private static final int PERM_REQUEST_CODE = 0;
	private static final int PERM_FLAGS = 0;
   
	// object
	private Context mContext;
	private UsbManager mUsbManager;
					
	// callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onAttached( UsbDevice device );
        public void onDetached( UsbDevice device );
    }

    /**
	 * constructor
	 * @param Context context
     */	    
	public UsbBaseManager( Context context ) {
		mContext = context;
		mUsbManager = (UsbManager) context.getSystemService( Context.USB_SERVICE );	
	}
					
    /**
	 * registerAttached
     */	
	protected void registerAttached() {
		IntentFilter filter = new IntentFilter( 
			UsbManager.ACTION_USB_DEVICE_ATTACHED );
		mContext.registerReceiver( mReceiver, filter );
	}

    /**
	 * registerDetached
     */	
	protected void registerDetached() {
		IntentFilter filter = new IntentFilter( 
			UsbManager.ACTION_USB_DEVICE_DETACHED );
		mContext.registerReceiver( mReceiver, filter );
	}

    /**
	 * registerPermission
     */	
	protected void registerPermission() {
		IntentFilter filter = new IntentFilter(
			ACTION_USB_PERMISSION );
		mContext.registerReceiver( mReceiver, filter );
	}

    /**
	 * unregister
     */	
	protected void unregister() {
		mContext.unregisterReceiver( mReceiver );
	}

	/**
	 * getDeviceList
	 * @return List<UsbDevice>
	 */	
	protected List<UsbDevice> getDeviceList() {
		List<UsbDevice> list = new ArrayList<UsbDevice>();
		UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
	  	HashMap<String, UsbDevice>	hash = manager.getDeviceList();
	  	if ( hash == null ) return null;
	  	String key = "";
	  	UsbDevice device = null;
	  	Iterator<String> it = hash.keySet().iterator();
        while ( it.hasNext() ) {
            key = it.next();
            device = hash.get( key );
            if ( device != null ) {
            	list.add( device );
            }
        }
        return list;
	}

	/**
	 * execAttached
	 * @param UsbDevice device 
	 */  
	protected void execAttached( UsbDevice device ) {
	   	log_d( "execAttached" );
	 	if ( device == null ) return;
	 	Intent intent = new Intent( ACTION_USB_PERMISSION );
	 	PendingIntent pending = PendingIntent.getBroadcast( mContext, PERM_REQUEST_CODE, intent, PERM_FLAGS );
		mUsbManager.requestPermission( device, pending );
	}

	/**
	 * checkVendor
	 * @param UsbDevice device
	 * @param int vendor
	 * @return boolean 
	 */ 
	protected boolean checkVendor( UsbDevice device, int vendor ) {
		if ( device == null ) return false;
	 	if ( checkValue( device.getVendorId(), vendor )) {
			return true;
		}
		return false;	
	}

	/**
	 * checkProduct
	 * @param UsbDevice device
	 * @param int product	 
	 * @return boolean 
	 */ 
	protected boolean checkProduct( UsbDevice device, int product ) {
		if ( device == null ) return false;
	 	if ( checkValue( device.getProductId(), product )) {
			return true;
		}
		return false;	
	}

	/**
	 * findInterface
	 * @param UsbDevice device
	 * @param int cls
	 * @param int sub
	 * @return List<UsbInterface>
	 */
	protected List<UsbInterface> findInterface( UsbDevice device, int cls, int sub ) {
		List<UsbInterface> list = new ArrayList<UsbInterface>();
        int count = device.getInterfaceCount();
        for ( int i = 0; i < count; i++ ) {
            UsbInterface usbInterface = device.getInterface( i );
            log_d( usbInterface.toString() ); 
            if ( checkValue( usbInterface.getInterfaceClass(), cls ) &&
			     checkValue( usbInterface.getInterfaceSubclass(), sub )) { 
				list.add( usbInterface );
 			}
		}
		return list;	
 	}

	/**
	 * findEndpoint
	 * @param UsbInterface usbInterface
	 * @param int dir
	 * @param int[] types
	 * @return List<UsbEndpoint>
	 */
	protected List<UsbEndpoint> findEndpoint( UsbInterface usbInterface, int dir, int[] types ) {
		List<UsbEndpoint> list = new ArrayList<UsbEndpoint>();
		int count = usbInterface.getEndpointCount();
		for ( int i = 0;  i < count; i++ ) {
			UsbEndpoint endpoint = usbInterface.getEndpoint( i );
			log_d( endpoint.toString() ); 
            if ( checkValue( endpoint.getDirection(), dir ) &&
			     checkType( endpoint.getType(), types )) {  
				list.add( endpoint );
			}
		}
		return list;	
	}

	/**
	 * openDevice
	 * @param UsbDevice device
	 * @return UsbDeviceConnection 
	 */ 
	protected UsbDeviceConnection openDevice( UsbDevice device ) {
		return mUsbManager.openDevice( device );
	}
		   	
	/**
	 * checkValue
	 * @param int current
	 * @param int target
	 * @return boolean 
	 */ 
	protected boolean checkValue( int current, int target ) {
		if ( target == current ) return true;
		return false;
	}

	/**
	 * checkType(
	 * @param int current
	 * @param int[] types
	 * @return boolean 
	 */ 
	protected boolean checkType( int current, int[] types ) {
		if ( types == null ) return true;
		for ( int i = 0; i < types.length; i ++ ) {
			if ( current == types[ i ] ) {
				 return true;
			}	 
		}
		return false;
	}
				
	/**
	 * matchDevice
	 * @param UsbDevice device1
	 * @param UsbDevice device2
	 * @return boolean 
	 */    	
	protected boolean matchDevice( UsbDevice device1, UsbDevice device2 ) {
		if ( device1 == null ) return false;
		if ( device2 == null ) return false;
		boolean ret = device1.getDeviceName().equals( device2.getDeviceName() );
		return ret;
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
	protected void notifyAttached( UsbDevice device ) {
		if (( mListener != null )&&( device != null )) {
			mListener.onAttached( device );
		}
	}

	/**
	 * notifyDetached
	 * @param UsbDevice device
	 */
	protected void notifyDetached( UsbDevice device ) {
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
   				receiveAttached( device );
   			} else if ( UsbManager.ACTION_USB_DEVICE_DETACHED.equals( action )) {
   				receiveDetached( device );
			} else if ( ACTION_USB_PERMISSION.equals( action )) {
			    synchronized ( this ) {
					boolean perm = intent.getBooleanExtra( UsbManager.EXTRA_PERMISSION_GRANTED, false );
					if ( perm ) {
   						receivePermission( device );
   					}	
   				}	
   			} 
    	}
	};

	/**
	 * receiveAttached
	 * @param UsbDevice device
	 */
   	protected void	receiveAttached( UsbDevice device ) {
   		execAttached( device );
   	}

	/**
	 * receiveDetached
	 * @param UsbDevice device
	 */
   	protected void	receiveDetached( UsbDevice device ) {
   		// dummy
   	}

	/**
	 * receivePermission
	 * @param UsbDevice device
	 */ 
	protected void	receivePermission( UsbDevice device ) {
   		// dummy
   	}
   	  	   				
	/**
	 * logcat
	 * @param String msg
	 */
	protected void log_d( String msg ) {
		if (UsbConstant.D) Log.d( UsbConstant.TAG, TAG_SUB + " " + msg );	
	}
	
}
