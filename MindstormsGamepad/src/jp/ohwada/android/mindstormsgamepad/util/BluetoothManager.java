package jp.ohwada.android.mindstormsgamepad.util;

import jp.ohwada.android.mindstormsgamepad.Constant;
import jp.ohwada.android.mindstormsgamepad.view.ToastMaster;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Bluetooth Manager
 */
public class BluetoothManager {

	/** Debug */
	private static final boolean D = Constant.BT_DEBUG_LOG_MANAGER;
	private static final boolean BT_DEBUG_SERVICE  = Constant.BT_DEBUG_SERVICE; 
    private static final String TAG = Constant.TAG; 
	private static String TAG_SUB = "BluetoothManager ";

    /* Intent action codes */
    public static final String ACTION_ADAPTER_ENABLE =  BluetoothAdapter.ACTION_REQUEST_ENABLE;
    
    /* Intent request codes */
    public static final int REQUEST_DEVICE_CONNECT = Constant.REQUEST_BT_DEVICE_CONNECT;
    public static final int REQUEST_ADAPTER_ENABLE = Constant.REQUEST_BT_ADAPTER_ENABLE;
     
	/* Constants that indicate the current connection state */
    public static final int STATE_NONE = BluetoothService.STATE_NONE;  
    public static final int STATE_LISTEN = BluetoothService.STATE_LISTEN;   
    public static final int STATE_CONNECTING = BluetoothService.STATE_CONNECTING; 
    public static final int STATE_CONNECTED = BluetoothService.STATE_CONNECTED; 

    /* Return Intent extra */
    private static final String BT_EXTRA_DEVICE_ADDRESS = Constant.BT_EXTRA_DEVICE_ADDRESS;
    
    /* Key names received from the BluetoothChatService Handler */
    private static final String BUNDLE_DEVICE_NAME = BluetoothService.BUNDLE_DEVICE_NAME;

	/* SharedPreferences */
    private static final String PREF_ADDR = Constant.PREF_BT_ADDR;
	private static final String PREF_USE_DEVICE = Constant.PREF_BT_USE_DEVICE;
	private static final String PREF_SHOW_DEBUG = Constant.PREF_BT_SHOW_DEBUG;
    private static final String DEFAULT_ADDR = "";
	private static final boolean DEFAULT_USE_DEVICE = true;
	private static final boolean DEFAULT_SHOW_DEBUG = false;

	/* debug */
    private static final int DEBUG_MAX_MSG = 50;
    private static final String DEBUG_GLUE = " ";

	// callback
    private OnChangedListener mOnListener;
    
    /* Singlton: Local Bluetooth adapter */
    private static BluetoothAdapter mBluetoothAdapter = null;

    /* Singlton: Member object for the chat services */
    private static BluetoothService mBluetoothService = null;

	/* class object */ 
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences mPreferences;
    private ByteUtility mByteUtility;
    private DebugMsg mDebugMsg;
    
	/* view component */
	private Button mButtonConnect;
    private TextView mTextViewDebug;

	/* Title Toast */
	private boolean isTitleMsg = false;
	private boolean isToastMsg = false;
	private String mTitleName = "";
	private String mTitleConnecting = "";
	private String mTitleConnected = "";
	private String mTitleNotConnected = "";
	private String mMsgFailed = "";
	private String mMsgLost = "";

	/* local variable */
    private int mDebugMaxMsg = DEBUG_MAX_MSG;	
     	    		
	/**
	 * interface OnChangedListener
	 */
    public interface OnChangedListener {
        void onDeviceList();
        void onRead( byte[] bytes );
    }
     
	/*
	 * === Constractor ===
	 * @param Context context
	 */
	public BluetoothManager( Activity activity ) {
		mActivity = activity;
		mContext = activity;
		mPreferences = PreferenceManager.getDefaultSharedPreferences( mContext );
		mByteUtility = new ByteUtility();
		mDebugMsg = new DebugMsg();
	}

	/**
	 * setOnClickListener
	 * @param OnButtonsClickListener listener
	 */
    public void setOnChangedListener( OnChangedListener listener ) {
        mOnListener = listener;
    }

	/*
	 * initTitle
	 * @param id_title
	 * @param id_connecting
	 * @param id_connected
	 * @param id_not_connected
	 */
    public void initTitle( int id_title, int id_connecting, int id_connected, int id_not_connected ) {
    	isTitleMsg = true;
    	mTitleName = getStringFromResources( id_title );
     	mTitleConnecting = getStringFromResources( id_connecting );
    	mTitleConnected = getStringFromResources( id_connected );
    	mTitleNotConnected = getStringFromResources( id_not_connected );
	}

	/*
	 * setTitleName
	 * @param int resId
	 */
    public void setTitleName( int resId ) {
    	mTitleName = getStringFromResources( resId );
	}

	/*
	 * initToast
	 * @param id_failed
	 * @param id_lost
	 */
    public void initToast( int id_failed, int id_lost ) {
    	isToastMsg = true;
     	mMsgFailed = getStringFromResources( id_failed );
     	mMsgLost = getStringFromResources( id_lost );
	}
	        	
 	/**
 	 * init BluetoothAdapter
	 * @return boolean
	 */		       
    public boolean initAdapter() {
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if ( mBluetoothAdapter != null ) return true;
		// return true if debug
		if ( BT_DEBUG_SERVICE ) return true;
		return false;
    }

// --- ButtonConnect ---
	public void initButtonConnect( View view, int id ) {
        mButtonConnect = (Button) view.findViewById( id );
        mButtonConnect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
				connectService();
			}
        });
	}

	/**
	 * show ButtonConnect
	 */	
	public void showButtonConnect() {
		// show button if hidden
		if ( mButtonConnect != null ) {
			if ( mButtonConnect.getVisibility() == View.GONE ) {  
				mButtonConnect.setVisibility( View.VISIBLE );		 
			}
		}
	}
	
	/**
	 * hide ButtonConnect
	 */	
	public void hideButtonConnect() {
		// hide button if show
		if ( mButtonConnect != null ) {
			if ( mButtonConnect.getVisibility() == View.VISIBLE ) {  
				mButtonConnect.setVisibility( View.GONE );		 
			}
		}
	}
// --- ButtonConnect end ---
           	
// --- Manager Control ---
	/**
	 * enabled BluetoothService when onStart
	 * @return  boolean
	 */
    public boolean enableService() {
    	log_d( "enabledService()" );
    	showTitleNotConnected();
		// no action if debug
        if ( BT_DEBUG_SERVICE ) return false;
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if ( !mBluetoothAdapter.isEnabled() ) {
			// startActivity Adapter Enable
			return true;
        // Otherwise, setup the chat session
        } else {
            initService();
        }
        return false;
    }

	/**
	 * Initialization of Bluetooth service
	 */
    private void initService() {
    	log_d( "initService()" );
		// no action if debug
        if ( BT_DEBUG_SERVICE ) return;
        // Initialize the BluetoothChatService to perform bluetooth connections
        if ( mBluetoothService == null ) {
            log_d( "new BluetoothService" );
    		mBluetoothService = new BluetoothService( mContext );
	    }
		if ( mBluetoothService != null ) {
            log_d( "set Handler" );
    		mBluetoothService.setHandler( sendHandler );
	    }		
    }
    
	/**
	 * connect Bluetooth Device when touch button
	 */
    public void connectService() {
        log_d( "connectService()" );
		// no action if debug
        if ( BT_DEBUG_SERVICE ) {
			toast_short( "No Action in debug" );
        	return;
        }
		// connect the BT device at once
		// if there is a device address. 
		String address = getPrefAddress();
		if ( isPrefUseDevice() && ( address != null) && !address.equals("") ) {
	    	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice( address );
	    	if ( mBluetoothService != null ) {
	    	    log_d( "connect " + address );
	        	mBluetoothService.connect( device );
	        }
		// otherwise
		// send message for the intent of the BT device list
		} else {
			notifyDeviceList();
		}
	}
				
	/**
	 * start Bluetooth Service when onResume
	 * @return int
	 */
    public void startService() {
        log_d( "startService()" );
    	int state = execStartService();
		switch( state ) {
			case STATE_CONNECTED:
				showTitleConnected( getDeviceName() );
				hideButtonConnect();
				break;
			default:
				showTitleNotConnected();
				break;
		}
	}
		
	/**
	 * execStartService
	 * @return int
	 */
    private int execStartService() {
		// no action if debug
        if ( BT_DEBUG_SERVICE ) return STATE_NONE;
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_BT_ADAPTER_ENABLE activity returns.
        if ( mBluetoothService == null ) return STATE_NONE;
        // Only if the state is STATE_NONE, do we know that we haven't started already
		int state = mBluetoothService.getState();
		switch( state ) {
			case STATE_NONE:
				// Start the Bluetooth service
				log_d( "BluetoothService start" );
				mBluetoothService.start();
				break;	
        }
        return state;
    }

	/**
	 * stop Bluetooth Service when onDestroy
	 */
    public void stopService() {
    	log_d( "stopService()" );
		// no action if debug
        if ( BT_DEBUG_SERVICE ) return;
		// Stop the Bluetooth chat services
		if ( mBluetoothService != null ) {
		    log_d( "BluetoothService stop" );
			mBluetoothService.stop();
		}
		showButtonConnect();
	}
    
	/**
	 * check the status of BT service
	 * @return boolean
	 */
    public boolean isServiceConnected() {
        log_d( "isServiceConnected()" );
		// true if debug
        if ( BT_DEBUG_SERVICE ) return true;
		// if connected
        if ( mBluetoothService != null ) {
            if ( mBluetoothService.getState() == STATE_CONNECTED ) {
                log_d( "true" );
				return true;
			}
        }
		// otherwise
		log_d( "false" );
		return false;
	}
// --- Manager Control end ---

// --- Command ---
    /**
     * Sends a message.
     * @param message  A string of text to send.
	 * @return boolean
     */
    public boolean writeString( String str ) {
        log_d( "writeString() " + str );
        // Check that there's actually something to send
        if ( str.length() == 0 )  return false;
		// Get the message bytes and tell the BluetoothChatService to write
		return writeBytes( str.getBytes() );
    }
    
	 /**
     * Sends a message.
     * @param byte[]  A string of text to send.
     * @return boolean
     */
    public boolean writeBytes( byte[] bytes ) {
		String msg = buildMsgBytes( "w ", bytes );
		log_d( msg );
		addAndShowTextViewDebug( msg );
		// no action if debug
        if ( BT_DEBUG_SERVICE ) {
			toast_short( "No Action in debug" );
        	return true;
        }
        // Check that we're actually connected before trying anything
        if ( !isServiceConnected() ) return false;
        // Check that there's actually something to send
        if ( bytes.length == 0 ) return false;
		// Get the message bytes and tell the BluetoothChatService to write
		if ( mBluetoothService != null ) {
			mBluetoothService.write( bytes );
		}
		return true;
    }		               
// --- Command end ---
	           			
// --- onActivityResult ---
	/**
	 * Processing when ActivityResult
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
    public boolean execActivityResult( int request, int result, Intent data ) {
		boolean ret = true;
        switch ( request) {
			case REQUEST_DEVICE_CONNECT:
				execActivityResultDevice( result, data );
            	break;
        	case REQUEST_ADAPTER_ENABLE:
				ret = execActivityResultAdapter( result, data );
            	break; 
    	}
        return ret;
    }

	/**
	 * Processing when connecting Bluetooth Device
	 * @param int result
	 * @param Intent data
	 */
    public void execActivityResultDevice( int result, Intent data ) {
    	log_d( "execActivityResultDevice()" );
    	// no action if debug
        if ( BT_DEBUG_SERVICE ) return;
		// When DeviceListActivity returns with a device to connect
		if ( result == Activity.RESULT_OK ) {
		    log_d( "RESULT OK" );
			// Get the device MAC address
			String address = data.getExtras().getString( BT_EXTRA_DEVICE_ADDRESS );
			// Get the BluetoothDevice object
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice( address );
			if ( mBluetoothService != null ) {
				log_d( "connect " + address ) ;
				// Attempt to connect to the device
				mBluetoothService.connect( device );
			}
		}
	}

	/**
	 * Processing when Bluetooth Adapter is enable
	 * @param int result
	 * @param Intent data
	 * @return boolean
	 */
    public boolean execActivityResultAdapter( int result, Intent data ) {
        log_d( "execActivityResultAdapter()" );        	
		// no action if debug
        if ( BT_DEBUG_SERVICE ) return true;
		// When the request to enable Bluetooth returns
		if ( result == Activity.RESULT_OK ) {
			log_d( "RESULT OK" );
			// Bluetooth is now enabled, so set up a chat session
			initService();
		// If user did not enable Bluetooth or an error occured			
		} else {
			log_d( "RESULT NG" );
			return false;			          	
		}
		return true;	
	}
// --- onActivityResult end ---

// --- Message Handler ---
	/**
	 * The Handler that gets information back from the BluetoothService
	 */
    private Handler sendHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
			execServiceHandler( msg );
        }
    };
    
	/**
	 * Message Handler ( handle message )
	 * @param Message msg
	 */
	private void execServiceHandler( Message msg ) {
    	switch ( msg.what ) {
			case BluetoothService.WHAT_READ:
				execHandlerRead( msg );
                break;
            case BluetoothService.WHAT_WRITE:
				execHandlerWrite( msg );
                break;
			case BluetoothService.WHAT_STATE_CHANGE:
				execHandlerChange( msg );
                break;
            case BluetoothService.WHAT_DEVICE_NAME:
				execHandlerDevice( msg );
                break;
            case BluetoothService.WHAT_FAILED:
				execHandlerFailed( msg );
                break;
			case BluetoothService.WHAT_LOST:
				execHandlerLost( msg );
                break;
        }
	}

	/**
	 * Message Handler ( state change )
	 * @param Message msg
	 * @return byte[] 
	 */
	private void execHandlerChange( Message msg ){
		switch ( msg.arg1 ) {
			case STATE_CONNECTED:
				execHandlerConnected( msg );
				break; 
			case STATE_CONNECTING:
				execHandlerConnecting( msg );
				break;
			default:
				execHandlerNotConnected( msg );
				break;
		}
	}

	/**
	 * Message Handler ( read )
	 * @param Message msg
	 * @return byte[] 
	 */
	 public void execHandlerRead( Message msg ) {
	 	// create valid data bytes
		byte[] buffer = (byte[]) msg.obj;
		int length = (int) msg.arg1;
		byte[] bytes = new byte[ length ];
		for ( int i=0; i < length; i++ ) {
			bytes[ i ] = buffer[ i ];
		}
		// debug
		String read = buildMsgBytes(  "r ", bytes );
		log_d( read );
		addAndShowTextViewDebug( read );
		notifyRead( bytes );
	}
		
	/**
	 * Message Handler ( write )
	 * @param Message msg
	 * @return byte[] 
	 */
	 private byte[] execHandlerWrite( Message msg ) {
		byte[] bytes = (byte[]) msg.obj;
    	String data = mByteUtility.bytesToHexString( bytes ); 
		log_d( "EventWrite " + data );
		return bytes;
	}

	/**
	 * Message Handler ( device name )
	 * @param Message msg
	 */
	private void execHandlerDevice( Message msg ) {
		 // get the connected device's name
		String name = msg.getData().getString( BUNDLE_DEVICE_NAME );
		log_d( "EventDevice " + name );
		hideButtonConnect();	
		String str = getTitleConnected( name );
		showTitle( str );
		toast_short( str );
	}

	/**
	 * Message Handler ( failed )
	 * @param Message msg
	 */
	 private void execHandlerFailed( Message msg ) {
		toast_short( mMsgFailed );
	}

	/**
	 * Message Handler ( lost )
	 * @param Message msg 
	 */
	 private void execHandlerLost( Message msg ) {
		showButtonConnect();
		toast_short( mMsgLost );
	}

	/**
	 * Message Handler ( connected )
	 * @param Message msg
	 */	
	 private void execHandlerConnected( Message msg ) {	
		// save Device Address
		if ( mBluetoothService != null ) {
			String address = mBluetoothService.getDeviceAddress();
			setPrefAddress( address );
		}	
		showTitleConnected( getDeviceName() );
		hideButtonConnect();
	}

	/**
	 * execHandlerNotConnected
	 * @param Message msg
	 */
	 private void execHandlerNotConnected( Message msg ) {	
		showTitleNotConnected();
	 }

	/**
	 * execHandlerConnecting
	 * @param Message msg
	 */
	 private void execHandlerConnecting( Message msg ) {	
		showTitleConnecting();
	 }
	 	
	/**
	 * get DeviceName
	 * @return String  
	 */
	 private String getDeviceName() {
		if ( mBluetoothService != null ) {	
			return mBluetoothService.getDeviceName();
		}
		return "";	
	}
	
	/**
	 * notifyDeviceList
	 */
    private void notifyDeviceList() {
		if ( mOnListener != null ) {
			mOnListener.onDeviceList();
		}
	}

	/**
	 * notifyRead
	 * @param byte[] bytes
	 */	
	private void notifyRead( byte[] bytes ) {
		if ( mOnListener != null ) {
			mOnListener.onRead( bytes );
		}
	}
// --- Message Handler end ---

// --- Shared Preferences ---
   	/**
	 * get the device address
	 * @return String
	 */
    private String getPrefAddress() {
		return mPreferences.getString( PREF_ADDR, DEFAULT_ADDR );
	}

	/**
	 * isPrefUseDevice
	 * @return boolean
	 */ 
    private boolean isPrefUseDevice() {
		return mPreferences.getBoolean( PREF_USE_DEVICE, DEFAULT_USE_DEVICE );
	}

	/**
	 * save the device address
	 * @param String addr
	 */
    private void setPrefAddress( String addr ) {
		mPreferences.edit().putString( PREF_ADDR, addr ).commit();
	}

	/**
	 * clear the device address
	 */
    public void clearPrefAddress() {
		setPrefAddress( "" );
	}
	
	/**
	 * isPrefShowDebug
	 * @return boolean
	 */ 
	private boolean isPrefShowDebug() {
		return mPreferences.getBoolean( PREF_SHOW_DEBUG, DEFAULT_SHOW_DEBUG );
	}
// --- Shared Preferences end ---

// --- title bar ---
	/**
	 * showTitle Connected
	 * @param String name
	 */
	private void showTitleConnected( String name )  {
		showTitle( getTitleConnected( name ) );	
	}

	/**
	 * getTitleConnected
	 * @param String name
	 * @return String 
	 */
	private String getTitleConnected( String name )  {
		String str = mTitleConnected + " " + name;
		return str;
	}
		
	/**
	 * showTitle Connecting
	 */	
	private void showTitleConnecting() {	
		showTitle( mTitleConnecting );	
	}

	/**
	 * showTitle NotConnected
	 */	
	private void showTitleNotConnected() {	
		showTitle( mTitleNotConnected );	
	}

	/**
	 * showTitle
	 * @param String name
	 */	
	private void showTitle( String str ) {
	    if ( !isTitleMsg ) return;
		String msg = mTitleName + " : " + str;
		mActivity.setTitle( msg );	
	}

	/**
	 * getString From Resources
	 * @param int id
	 * @param String  
	 */	
	private String getStringFromResources( int id ) {	
		return mContext.getResources().getString( id );
	}	 
// --- title bar end ---
	    		
// --- Debug --
 	/**
	 * init TextView Debug when onCreate
	 * @param View view
	 * @param int id
	 */	
	public void initTextViewDebug( View view, int id ) {
		mTextViewDebug = (TextView) view.findViewById( id );
	}

 	/**
	 * DebugMaxMsg
	 * @param int max
	 */	
	public void setDebugMaxMsg( int max ) {
		mDebugMaxMsg = max;
	}
	
	/**
	 * show dubug msg
	 * @param String str
	 */				
	private void addAndShowTextViewDebug( String str ) {
		// if debug, show message
		if ( ! isPrefShowDebug() ) return;
		mDebugMsg.add( str );
		String msg = mDebugMsg.build( mDebugMaxMsg, DEBUG_GLUE );
		showTextViewDebug( msg );
	}

	/**
	 * show dubug msg
	 * @param String str
	 */				
	public void showTextViewDebug( String str ) {
		if ( mTextViewDebug != null ) {
			mTextViewDebug.setText( str );
		}	
	}
	
	/**
	 * buildMsgBytes
	 * @param String str
	 * @param byte[] bytes
	 * @return String 
	 */				
    private String buildMsgBytes( String str, byte[] bytes ) {
		String msg = str + mByteUtility.bytesToHexString( bytes ); 
		return msg;
	}

	/**
	 * show toast
	 * @param String msg
	 */				
    private void toast_short( String msg ) {
        if ( !isToastMsg ) return;
    	ToastMaster.showShort( mContext, msg );
    }
      
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}	
// --- Debug end ---

}
