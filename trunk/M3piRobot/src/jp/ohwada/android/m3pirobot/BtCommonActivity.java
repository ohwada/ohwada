package jp.ohwada.android.m3pirobot;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Bt Common Activity
 */
public class BtCommonActivity extends Activity {

	/** TAG_BT(Debug) */
	protected String TAG_BT= "BtCommonActivity";
	
	/* Debug flag */
    protected static final boolean DEBUG_BT_SERVICE    = Constant.DEBUG_BT_SERVICE;
    protected static final boolean DEBUG_BT_CONNECT  = Constant.DEBUG_BT_CONNECT;
    protected static final boolean DEBUG_BT_RECV_MSG = Constant.DEBUG_BT_RECV_MSG;
    protected static final boolean D = Constant.DEBUG_LOG_BT;    
    
    /* Message types sent from the BluetoothChatService Handler */
    protected static final int MESSAGE_STATE_CHANGE = Constant.MESSAGE_STATE_CHANGE;
    protected static final int MESSAGE_READ = Constant.MESSAGE_READ;
	protected static final int MESSAGE_WRITE = Constant.MESSAGE_WRITE;
    protected static final int MESSAGE_DEVICE_NAME = Constant.MESSAGE_DEVICE_NAME;
    protected static final int MESSAGE_TOAST = Constant.MESSAGE_TOAST;

    /* Key names received from the BluetoothChatService Handler */
    protected static final String BUNDLE_DEVICE_NAME = Constant.BUNDLE_DEVICE_NAME;
    protected static final String BUNDLE_TOAST =  Constant.BUNDLE_TOAST;

	/* Constants that indicate the current connection state */
    protected static final int BT_STATE_NONE = Constant.BT_STATE_NONE;  
    protected static final int BT_STATE_LISTEN = Constant.BT_STATE_LISTEN;   
    protected static final int BT_STATE_CONNECTING = Constant.BT_STATE_CONNECTING; 
    protected static final int BT_STATE_CONNECTED = Constant.BT_STATE_CONNECTED;  
    
    /* Intent request codes */
    protected static final int REQUEST_CONNECT_DEVICE = 1;
    protected static final int REQUEST_ENABLE_BT = 2;
    protected static final int REQUEST_LCD = 3;
    protected static final int REQUEST_REPORT = 4;
    
	/* SharedPreferences */
	protected static final String PREF_NAME = "m3pi_control";
    protected static final String PREF_ADDR = "device_addr";

	/** Number to show debug messages */
	protected final int MAX_TOUCH_MSG = 10;
	protected final int MAX_RECV_MSG  = 20;

	/* m3pi LED */
	protected static final int MIN_IMAGE_LED = 1;
	protected static final int MAX_IMAGE_LED = 8;

	/** view component */
	protected ImageView[] mImageViewLeds =  new ImageView[ MAX_IMAGE_LED + 1 ];

	/* status of views */
	protected boolean[] ledToggles = new boolean[ MAX_IMAGE_LED + 1 ];
	
    /* Name of the connected device */
    protected String strConnectedDeviceName = null;

    /* singlton: Local Bluetooth adapter */
    private static BluetoothAdapter mBluetoothAdapter = null;

    /* singlton: Member object for the chat services */
    private static BluetoothService mBluetoothService = null;

	/** view component */
    protected TextView mTextViewTitleLeft;
    protected TextView mTextViewTitleRight;
    protected TextView mTextViewMsg;
    protected Button mButtonConnect;

	/** List for debug message (Debug) */
	protected List<String> listTouchMsg = new ArrayList<String>();
	protected List<String> listRecvMsg  = new ArrayList<String>();

// --- onCreate ---
 	/**
	 * setTag
	 * @param String tag
	 */
	protected void setTag( String tag ) {
		TAG_BT = tag;
	}

 	/**
	 * create CustomTitle
	 */	
	protected void createCustomTitle() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}

 	/**
	 * create TitleBar
	 */		
	protected void createTitleBar() {
		/* set the layout on the title bar */
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        mTextViewTitleLeft = (TextView) findViewById(R.id.text_title_left);
        mTextViewTitleLeft.setText(R.string.app_name);
        mTextViewTitleRight = (TextView) findViewById(R.id.text_title_right);
	}

 	/**
	 * create TextView Msg
	 */		
	protected void 	createTextViewMsg() {
		mTextViewMsg  = (TextView) findViewById(R.id.text_msg);
	}

 	/**
	 * create Button Connect
	 */	
	protected void createButtonConnect() {		
		/* Initialization of button */
        mButtonConnect = (Button) findViewById(R.id.button_connect);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				connectBT();
			}
        });
	} 

 	/**
	 * create BluetoothAdapter
	 */		       
    protected void createBluetoothAdapter() {
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastMaster.showLong(this, "Bluetooth is not available");

			// for debug
			if ( ! DEBUG_BT_SERVICE ) {
            	finish();
			}

            return;
        }
    }

	/**
	 * init ImageView LEDs
	 * @param none
	 * @return void
	 */
    protected void initImageViewLeds() {
		Resources r = getResources();
		String pkg = getPackageName();
		for ( int i = MIN_IMAGE_LED ; i <= MAX_IMAGE_LED; i++ ) {
			String name = "image_led_" + i ;
			int id = r.getIdentifier( name, "id", pkg );
			mImageViewLeds[ i ] = (ImageView) findViewById( id );
			mImageViewLeds[ i ].setOnClickListener(new View.OnClickListener() {
            	@Override
            	public void onClick(View v) {
            		image_leds_click(v);
				}
 			});
		}
    }

	/**
	 * image_leds_click
	 * @param View v
	 * @return void
	 */    	
	protected void image_leds_click( View v ) {
	  	int id = v.getId();
	  	int n = 0;
	  	switch (id) {
	  		case R.id.image_led_1:
	  			n = 1;
	  			break;
	  		case R.id.image_led_2:
	  			n = 2;
	  			break;
	  		case R.id.image_led_3:
	  			n = 3;
	  			break;
	  		case R.id.image_led_4:
	  			n = 4;
	  			break;
	  		case R.id.image_led_5:
	  			n = 5;
	  			break;
	  		case R.id.image_led_6:
	  			n = 6;
	  			break;
	  		case R.id.image_led_7:
	  			n = 7;
	  			break;
	  		case R.id.image_led_8:
	  			n = 8;
	  			break;
	  	}

		if ( n == 0 ) {
			return;
		}
		
		ledToggles[n] = ledToggles[n] ? false : true;
		if ( ledToggles[n] ) {
			mImageViewLeds[n].setImageResource( R.drawable.led_red );
		} else {
			mImageViewLeds[n].setImageResource( R.drawable.led_white );
		}
		cmdSendLed( n, ledToggles[n] );
	}    
// --- onCreate end ---

// --- onStart  ---	
	/**
	 * start BluetoothService
	 */
    protected void startBluetoothService() {
        if (D) Log.d( TAG_BT,"startBluetoothService()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            if (D) Log.d( TAG_BT,"startActivity )REQUEST_ENABLE_BT");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        // Otherwise, setup the chat session
        } else {
            setupBT();
            setHandlerBT( msgHandler );
        }
    }
// --- onStart end  ---

// --- comand send ---
	/**
	 * cmd Send Forward
	 */
    protected void cmdSendForward() {
		sendMessageBT( "fw05" ); 
	}

	/**
	 * cmd Send Backward
	 */	
    protected void cmdSendBackward() {
		sendMessageBT( "bw05" ); 
	}

	/**
	 * cmd Send Left
	 */		
    protected void cmdSendLeft() {
		sendMessageBT( "lt05" ); 
	}

	/**
	 * cmd Send Right
	 */		
    protected void cmdSendRight() {
		sendMessageBT( "rt05" ); 
	}

	/**
	 * cmd Send Stop
	 */		
    protected void cmdSendStop() {
		sendMessageBT( "st" ); 
	}

	/**
	 * cmd Send Led
	 * @param int n
	 * @param boolean status
	 */	
    protected void cmdSendLed( int n, boolean status ) {
		if ( n < MIN_IMAGE_LED ||  n > MAX_IMAGE_LED ) {
			return;
		}
		int s = status ? 1 : 0;
		String msg = "le" + n + s ;
		sendMessageBT( msg ); 
	}

	/**
	 * cmd Send Clear
	 */	
    protected void cmdSendClear() {
		sendMessageBT( "cl" ); 
	}

	/**
	 * cmd Send Locate
	 * @param int x
	 * @param int y 
	 */	
    protected void cmdSendLocate( int x, int y ) {
		String msg = "lc" + x + y ;
		sendMessageBT( msg );
	}

	/**
	 * cmd Send Print
	 * @param String str 
	 */	
    protected void cmdSendPrint( String str ) {
		String msg = "pr" + str ;
		sendMessageBT( msg );  
	}

	/**
	 * cmd Send Battery
	 */							
    protected void cmdSendBattery() {
		sendMessageBT( "bt" );  
	}

	/**
	 * cmd Send Voltage
	 */	
    protected void cmdSendVoltage( ) {
		sendMessageBT( "pv" );  
	}	
// --- comand send end ---
	
// === onActivityResult ===
	/**
	 * Processing when connecting BT device
	 * @param int resultCode
	 * @param Intent data
	 */
    protected void execActivityResultDevice(int resultCode, Intent data) {
    	if (D) Log.d( TAG_BT,"execActivityResultDevice()" );
    
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

		// When DeviceListActivity returns with a device to connect
		if (resultCode == Activity.RESULT_OK) {
		    if (D) Log.d( TAG_BT,"RESULT OK" );
			// Get the device MAC address
			String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			// Get the BLuetoothDevice object
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			if ( mBluetoothService != null ) {
				if (D) Log.d( TAG_BT,"connect " + address);
				// Attempt to connect to the device
				mBluetoothService.connect(device);
				// save Device Address
				setPrefDeviceAddress( address );
			}
		}
	}

	/**
	 * Processing when BT service is enable
	 * @param int resultCode
	 * @param Intent data
	 */
    protected void execActivityResultEnable(int resultCode, Intent data) {
        if (D) Log.d( TAG_BT,"execActivityResultEnable()" );
        	
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

		// When the request to enable Bluetooth returns
		if (resultCode == Activity.RESULT_OK) {
			if (D) Log.d( TAG_BT,"RESULT OK" );
			// Bluetooth is now enabled, so set up a chat session
			setupBT();
			setHandlerBT( msgHandler );
			
		} else {
			if (D) Log.d( TAG_BT,"RESULT NG" );
			// User did not enable Bluetooth or an error occured
			ToastMaster.showShort( this, R.string.bt_not_enabled_leaving);
           	finish();
		}
	}
// --- onActivityResult end ---

// --- Message Handler ---
	/**
	 * Message Handler
	 * The Handler that gets information back from the BluetoothChatService
	 */
    protected Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
			execHandleMessage(msg) ;
        }
    };

	/**
	 * Message Handler ( handle message )
	 * @param Message msg
	 */
	protected void execHandleMessage( Message msg ) {
    	switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
				execMessageChange( msg );
                break;

            case MESSAGE_WRITE:
                break;

            case MESSAGE_READ:
				execMessageRead( msg );
                break;

            case MESSAGE_DEVICE_NAME:
				execMessageDevice( msg );
                break;

            case MESSAGE_TOAST:
				execMessageToast( msg );
                break;
        }
	}
           
	/**
	 * Message Handler ( state change )
	 * @param Message msg
	 */
	protected void execMessageChange( Message msg ){
		switch (msg.arg1) {
			case BT_STATE_CONNECTED:
				setTitleConnected();
				break;
 
			case BT_STATE_CONNECTING:
				setTitleConnecting();
				break;

			case BT_STATE_LISTEN:
			case BT_STATE_NONE:
				setTitleNotConnected();
				break;
		}
	}
								
	/**
	 * Message Handler ( read )
	 * @param Message msg
	 */
	 protected void execMessageRead( Message msg ) {
		String str = MessageToString( msg );
		String cmd = "";
		String value = "";
		int len = str.length();
		if ( len >= 2 ) {
			cmd = str.substring(0,2);
			if ( len > 2 ) {
				value = str.substring(2, len);
			}
		}

		if ( cmd.equals("pn") ) {
			cmdRecvPin( value );
			
		} else if ( cmd.equals("bt") ) {
			cmdRecvBattery( value );

		} else if ( cmd.equals("pv") ) {
			cmdRecvVoltage( value );
		}
				
		showMessageRead( str );
	}

	/**
	 * cmdRecv Pin
	 * @param String str
	 */
	protected void cmdRecvPin( String str ) {	
		// dummy
	}

	/**
	 * cmdRecv Battery
	 * @param String str
	 */	
	protected void cmdRecvBattery( String str ) {	
		// dummy
	}

	/**
	 * cmdRecv Voltage
	 * @param String str
	 */	
	protected void cmdRecvVoltage( String str ) {	
		// dummy
	}

	/**
	 * show MessageRead
	 * @param String str
	 */				
	protected void showMessageRead( String str ) {
		// debug
		if ( DEBUG_BT_RECV_MSG ) {
			if (D) Log.d(TAG_BT,str);
			addRecvMsg( str );
			showRecvMsgs();
		}
	}

	/**
	 * Message Handler ( device name )
	 * @param Message msg
	 */
	protected void execMessageDevice( Message msg ) {
		 // get the connected device's name
		String name = msg.getData().getString(BUNDLE_DEVICE_NAME);
		ToastMaster.showShort(
			getApplicationContext(), 
			"Connected to "	 + name );

		setTitleConnected( name );
		
		// hide button if show
		hideButtonConnect();
	}

	/**
	 * Message Handler ( toast )
	 * @param Message msg
	 */
	protected void execMessageToast( Message msg ) {
		ToastMaster.showShort(
			getApplicationContext(), 
			msg.getData().getString(BUNDLE_TOAST) );
	}

	/**
	 * convert message to string.
	 * convert the characters that can be displayed , 
	 * because message are binary
	 * @param msg Message Object
	 */
	protected String MessageToString( Message msg ) {
		byte buf[] = (byte[]) msg.obj; 
		String str = "";

		for (int i = 0; i < msg.arg1; i++) 
		{
			int b = buf[i] & 0xff;

			// if asccii character, chage asccii style
			if (( b >= 0x20 )&&( b <= 0x7e )) {
        		str += (char)b;

			// otherwise, chage hex style
			} else {
        		str += "[" + Integer.toHexString(b) + "]";
			}
    	}

		return str;
	}
// --- Message Handler end ---

// --- BT service and device ---
	/**
	 * Initialization of BT service
	 */
    protected void setupBT() {
    	if (D) Log.d( TAG_BT,"setupBT()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

        // Initialize the BluetoothChatService to perform bluetooth connections
        if (mBluetoothService == null) {
            if (D) Log.d( TAG_BT,"new BluetoothService" );
    		mBluetoothService = new BluetoothService(this);
	    }	
    }

	/**
	 * setHandlerBT
	 * @param Handler mHandler
	 */
    protected void setHandlerBT( Handler mHandler ) {
    	if (D) Log.d( TAG_BT,"setBTHandler()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

        // Initialize the BluetoothChatService to perform bluetooth connections
        if (mBluetoothService != null) {
            if (D) Log.d( TAG_BT,"set Handler" );
    		mBluetoothService.setHandler(mHandler);
	    }	
    }
    
	/**
	 * connect BT device
	 */
    protected void connectBT() {
        if (D) Log.d( TAG_BT,"connectBT()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
            ToastMaster.showShort(this, "No Action in debug");
        	return;
        }

		// connect the BT device at once
		// if there is a device address. 
		String address = getPrefDeviceAddress();
		if ( DEBUG_BT_CONNECT && ( address != null) && !address.equals("") ) {
        	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        	if ( mBluetoothService != null ) {
        	    if (D) Log.d( TAG_BT,"connect " + address );
            	mBluetoothService.connect(device);
            }

		// otherwise
		// call the intent of the BT device list
		} else {
            // Launch the DeviceListActivity to see devices and do scan
           	Intent serverIntent = new Intent(this, DeviceListActivity.class);
           	startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		}
	}

	/**
	 * disconnect BT device
	 */
    protected void disconnectBT() {
    	if (D) Log.d( TAG_BT,"disconnectBT()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }
		// Stop the Bluetooth chat services
		if (mBluetoothService != null) {
		    if (D) Log.d( TAG_BT,"BluetoothService stop" );
			mBluetoothService.stop();
		}
	}

	/**
	 * startBT
	 */
    protected void startBT() {
        if (D) Log.d( TAG_BT,"startBT()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            int state = mBluetoothService.getState();
            switch( state ) {
            	case BT_STATE_NONE:
               	// Start the Bluetooth service
              		if (D) Log.d( TAG_BT,"BluetoothService start" );
              		setTitleNotConnected();
            		mBluetoothService.start();
            		break;
            	case BT_STATE_CONNECTED:
                	// show connected
	            	setTitleConnected();
	            	hideButtonConnect();
	            	break;
				default:
                	// show not connected
            		setTitleNotConnected();
            		break;
            }	
        }
    }
    
	/**
	 * check the status of BT service
	 */
    protected boolean checkStateBT() {
        if (D) Log.d( TAG_BT,"checkStateBT()" );
		// true if debug
        if ( DEBUG_BT_SERVICE ) {
        	return true;
        }

		// if connected
        if ( mBluetoothService != null ) {
            if ( mBluetoothService.getState() == BT_STATE_CONNECTED ) {
                if (D) Log.d( TAG_BT,"true" );
				return true;
			}
        }

		// otherwise
		if (D) Log.d( TAG_BT,"false" );
		return false;
	}

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    protected void sendMessageBT(String message) {
        if (D) Log.d( TAG_BT,"sendMessageBT()" );
		// no action if debug
        if ( DEBUG_BT_SERVICE ) {
        	return;
        }

        // Check that we're actually connected before trying anything
        if ( !checkStateBT() ) {
            ToastMaster.showShort(this, R.string.not_connected);
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            if ( mBluetoothService != null ) {
                if (D) Log.d( TAG_BT,message );
            	mBluetoothService.write(send);
        	}
        }
    }
// --- BT service and device end ---

// --- Shared Preferences ---
	/**
	 * get the device address
	 */
    protected String getPrefDeviceAddress() {
		SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		return pref.getString(PREF_ADDR, "");
	}

	/**
	 * save the device address
	 * @param String addr
	 */
    protected void setPrefDeviceAddress( String addr ) {
		SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		pref.edit().putString(PREF_ADDR, addr).commit();
	}

	/**
	 * clear the device address
	 */
    protected void clearPrefDeviceAddress() {
		setPrefDeviceAddress( "" );
	}

// --- title bar ---
	/**
	 * set TitleConnected
	 */
	protected void setTitleConnected() {
		if ( mBluetoothService != null ) {
            String name = mBluetoothService.getDeviceName();
             if ( name != null ) {
				setTitleConnected( name );
			}
		}	
	}

	/**
	 * set TitleConnected
	 * @param String name
	 */
	protected void setTitleConnected( String name )  {
		if ( mTextViewTitleRight != null ) {
			mTextViewTitleRight.setText(R.string.title_connected_to);
			mTextViewTitleRight.append( name );
		}	
	}

	/**
	 * set TitleConnecting
	 */	
	protected void setTitleConnecting() {	
		if ( mTextViewTitleRight != null ) {
			mTextViewTitleRight.setText(R.string.title_connecting);
		}
	}

	/**
	 * set TitleNotConnected
	 */	
	protected void setTitleNotConnected() {		
		if ( mTextViewTitleRight != null ) {
			mTextViewTitleRight.setText(R.string.title_not_connected);
		}
	}

	/**
	 * hide ButtonConnect
	 */	
	protected void hideButtonConnect() {
		// hide button if show
		if (mButtonConnect != null ) {  
			if (mButtonConnect.getVisibility() == View.VISIBLE) {  
				mButtonConnect.setVisibility(View.INVISIBLE);  
			} 
		}
	}

// --- sleep ---
	/**
	 * sleepMs
	 * @param int ms
	 */
	protected void sleepMs( int ms ) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    		
// --- debug ---
	/**
	 * add the message to the list (touch)
	 * @param msg String
	 */
    protected void addTouchMsg( String msg ) {
		listTouchMsg.add( msg );
	}

	/**
	 * add the message to the list (recv)
	 * @param msg String
	 */
    protected void addRecvMsg( String msg ) {
		listRecvMsg.add( msg );
	}

	/**
	 * show the messages (touch)
	 */
    protected void showTouchMsgs() {
		String msg = buildMsgs( listTouchMsg, MAX_TOUCH_MSG, "\n" );

		/* show message */
		if ( mTextViewMsg != null ) {
   			mTextViewMsg.setText(msg);
   		}
	}

	/**
	 * show the messages (recv)
	 */
    protected void showRecvMsgs() {
		String msg = buildMsgs( listRecvMsg, MAX_RECV_MSG, " " );

		/* show message */
		if ( mTextViewMsg != null ) {
   			mTextViewMsg.setText(msg);
   		}	
	}

	/**
	 * build the messages
	 * @param msgs : message list
	 * @param max  : max of showing messages
	 * @param del  : delmita of messages
	 */
    protected String buildMsgs( List<String> msgs, int max, String del ) {
		// set 'start' and 'end' , showing only 'max' messages
		int start = 0;
		int end = msgs.size();
		if ( end > max ) {
			start = end - max;
		}

		/* combine the messages */
		String msg = "";
		for ( int i = start; i < end; i++ ) {
			msg += msgs.get( i );
			msg += del;
		}

		return msg;
	}			    
// --- debug end ---

}
