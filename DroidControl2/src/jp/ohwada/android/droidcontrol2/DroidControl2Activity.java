package jp.ohwada.android.droidcontrol2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Main Activity
 */
public class DroidControl2Activity extends Activity 
{
	/* Flags (Debug) */
    private static final boolean DEBUG_BT    = false;
    private static final boolean DEBUG_TOUCH = false;
    private static final boolean DEBUG_RECV  = false;

	/** TAG (Debug) */
	private static final String TAG = "DroidControl2Activity";

    /* Message types sent from the BluetoothChatService Handler */
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    /* Key names received from the BluetoothChatService Handler */
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    /* Intent request codes */
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

	/* timer handler */
    private static int TIMER_MSG = 1;
    private static int TIMER_INTERVAL = 1;	// 1 sec

	/* SharedPreferences */
	private static final String PREF_NAME = "droid_control";
    private static final String PREF_ADDR = "device_addr";

	/** Number to show debug messages */
	private final int MAX_TOUCH_MSG = 10;
	private final int MAX_RECV_MSG  = 20;

    /* Name of the connected device */
    private String mConnectedDeviceName = null;

    /* Local Bluetooth adapter */
    private BluetoothAdapter mBluetoothAdapter = null;

    /* Member object for the chat services */
    private BluetoothDroidControlService mCmdSendService = null;

	/* Resources */
	private Resources resources;

	/** view component */
    private View viewContent;
    private LinearLayout layout;
	private ImageView imgLeft;
	private ImageView imgRight;
	private ImageView imgBody;
    private Button btnConnect;
    private TextView txtTitle;
    private TextView txtPinch;
    private TextView txtMsg;

	/* status of views */
    private boolean ledAutoStatus  = false;
    private boolean ledAutoToggle  = false;
    private boolean imgBodyToggle  = false;

	/* class object that processes touch event */
   	private MultiTouchEvent mEvent;

	/* class object that processes the image view */
	private DroidImageView mImgLeft;
	private DroidImageView mImgRight;

	/** Offset from orign point of screen */
    private int	offsetX = 0;
    private int	offsetY = 0;

	/** pinch status */
	private int	pinchStatus = 0;

	/* Action name (it is originally nameed) */
	private String actionName = "";

	/** List for debug message (Debug) */
	private List<String> touchMsgs = new ArrayList<String>();
	private List<String> recvMsgs  = new ArrayList<String>();

// --- onCreate ---
 	/**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

		/* Customizing the title bar */
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		/* set the layout on the screen */
        setContentView(R.layout.main);

		/* set the layout on the title bar */
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

		/* get resources */
		resources = getResources();

		/* class object that processes touch event */
    	mEvent = new MultiTouchEvent();

		/* view component */
    	viewContent = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        layout      = (LinearLayout) findViewById(R.id.layout_horizontal);
		imgLeft     = (ImageView) findViewById(R.id.image_left);
		imgRight    = (ImageView) findViewById(R.id.image_right);
		imgBody     = (ImageView) findViewById(R.id.image_body);
		txtPinch    = (TextView)  findViewById(R.id.text_pinch);
		txtMsg      = (TextView)  findViewById(R.id.text_msg);

		/* Initialization of button */
        btnConnect = (Button) findViewById(R.id.button_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() 
		{
    		/**
	 		* Processing when button is clicked
	 		* @param v View
     		*/
            @Override
            public void onClick(View v) 
			{
				// connect BT device
				connectBT();
			}
        });

        // Set up the custom title
        TextView txtTitleLeft = (TextView) findViewById(R.id.title_left_text);
        txtTitleLeft.setText(R.string.app_name);
        txtTitle = (TextView) findViewById(R.id.title_right_text);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastMaster.showLong(this, "Bluetooth is not available");

			// for debug
			if ( ! DEBUG_BT ) {
            	finish();
			}

            return;
        }
    }
// --- end ---

	/**
	 * === onStart ===
	 */
    @Override
    public void onStart() 
	{
        super.onStart();

		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        // Otherwise, setup the chat session
        } else {
            if (mCmdSendService == null) setupBT();
        }
    }

	/**
	 * === onResume ===
	 */
    @Override
    public synchronized void onResume() 
	{
        super.onResume();

		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mCmdSendService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mCmdSendService.getState() == BluetoothDroidControlService.STATE_NONE) {
              // Start the Bluetooth chat services
            	mCmdSendService.start();
            }
        }
    }

	/**
	 * === onPause ===
	 */
    @Override
    public synchronized void onPause() 
	{
        super.onPause();
    }

	/**
	 * === onStop ===
	 */
    @Override
    public void onStop() 
	{
        super.onStop();
    }

	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() 
	{
        super.onDestroy();

		// disconnect BT
		disconnectBT();
    }

// --- onTouchEvent ---
    /**
	 * === onTouchEvent ===
	 * @param  event MotionEvent
	 * @return always true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) 
	{
    	int action = event.getAction();
 
		/* processes touch event */
   		actionName    = mEvent.getEventName( action );
		boolean flag  = mEvent.ckeckEventPointer();
 		                mEvent.checkPinch( event );

   		switch (action) 
		{
    	case MotionEvent.ACTION_DOWN:
    	case MotionEvent.ACTION_UP:
    	case MotionEvent.ACTION_MOVE:
			execTouchEvent( event );
    		break;

       	default:
			/* If the action is pointer */
			if ( flag ) {
				execTouchEvent( event );

			/* otherwise */
			} else {
				// debug
		        if ( DEBUG_TOUCH ) {
					Log.d(TAG, actionName );
	       			addTouchMsg( actionName );
				}
       		}
        	break;
    	}

		// debug
		if ( DEBUG_TOUCH ) {
	   		showTouchMsgs();
		}

		return true;
	}

    /**
	 * Processing when image is touched
	 * @param  event MotionEvent
     */
    private void execTouchEvent( MotionEvent event )
	{
		String msg = "";
		boolean flag = false;

		calcOffset();

		/* if there is no class object, generates them */
		createViewLeftRight();

		boolean flagLeft = mImgLeft.execTouchEvent( event );

		/* if left image is touched */
		if ( flagLeft ) {
			flag = true;

			// if status is ON
			if ( mImgLeft.getImageToggle() ) {
				onLedLeft();

			// if status is OFF
			} else {
				offLedLeft();
			}

			// debug
			if ( DEBUG_TOUCH ) {
				msg = mImgLeft.getDebugMessage();
				Log.d(TAG, msg );
				addTouchMsg( msg );
			}
		}

		boolean flagRight = mImgRight.execTouchEvent( event );

		/* if right image is touched */
		if ( flagRight ) {
			flag = true;

			// if status is ON
			if ( mImgRight.getImageToggle() ) {
				onLedRight();

			// if status is OFF
			} else {
				offLedRight();
			}

			// debug
			if ( DEBUG_TOUCH ) {
				msg = mImgRight.getDebugMessage();
				Log.d(TAG, msg );
				addTouchMsg( msg );
			}
		}

		/* if any image are not touched */
		if ( flag == false ) {

			/* if pinch */
			if ( checkPinch() ) {
				execPinch();

			/* if not move */
			} else if (actionName != "move") {
				// debug
				if ( DEBUG_TOUCH ) {
					msg = actionName + " " + 
						(int)event.getX() + " " +
						(int)event.getY() ;
					Log.d(TAG, msg);
					addTouchMsg( msg );
				}
			}
		}

	}

    /**
	 * create class object that processes the image view
     */
    private void createViewLeftRight()
	{
		/* if there is no class object, generates this */
		if ( mImgLeft == null ) {
			mImgLeft = createView( imgLeft, R.drawable.left_off, R.drawable.left_on, "left" );
		}

		/* if there is no class object, generates this */
		if ( mImgRight == null ) {
			mImgRight = createView( imgRight, R.drawable.right_off, R.drawable.right_on, "right" );
		}
	}

	/**
	 * create class object that processes the image view
	 * @param  image : ImageView
	 * @param  offsetX offsetY : Offset from orign point of screen
	 * @param  name : image name (debug)
	 */
    private DroidImageView createView( ImageView image, int id_off, int id_on, String name ) 
	{
    	DroidImageView view = new DroidImageView();
    	view.setImageView( image, offsetX, offsetY );
		view.setResources( resources );
		view.setImageOff( id_off );
		view.setImageOn(  id_on );
		view.setViewName( name );
		return view;
	}

    /**
	 * calculate offset from orign point of screen
     */
    private void calcOffset()
	{
		// orign point of content, without status bar and title bar
		int viewLeft  = viewContent.getLeft();
		int viewTop   = viewContent.getTop();

    	offsetX = viewLeft + layout.getLeft();
    	offsetY = viewTop  + layout.getTop();
	}

	/**
	 * check the state of pinch
	 * @return state
	 */
    private boolean checkPinch() 
	{
		// if left image is touched 
		if ( mImgLeft.checkImageStatusInTouch() ) {
			return false;
		}

		// if right image is touched 
		if ( mImgRight.checkImageStatusInTouch() ) {
			return false;
		}

		// if the state of pinch is none
		if ( mEvent.checkPinchStatusNone() ) {
			return false;
		}

		// otherwise
		return true;
	}

	/**
	 * excute pinch process
	 */
    private void execPinch() 
	{
		String msg = mEvent.getDebugMessage();

		// if pinch status is UP
		if ( mEvent.checkPinchStatusUp() ) {

			// if not same status already
			if ( pinchStatus != 1 ) {
				pinchStatus = 1;

				// debug
				if ( DEBUG_TOUCH ) {
					addTouchMsg( msg + " 1" );
				}
			}
		} else {

			// if not same status already
			if ( pinchStatus != 2 ) {
				pinchStatus = 2;

	 			// if pinch status is OUT
				if( mEvent.checkPinchStatusOut() ) {
					ledAutoStatus = true;
					txtPinch.setText( R.string.pinch_in );

		 			/* start timerHandler */
					timerHandler.sendEmptyMessage(TIMER_MSG);

	 			// if pinch status is IN
				} else if( mEvent.checkPinchStatusIn() ) {
					ledAutoStatus = false;
					txtPinch.setText( R.string.pinch_out );
				}

				// debug
				if ( DEBUG_TOUCH ) {
					addTouchMsg( msg + " 2" );
				}
			}
		}
	}
// --- end ---

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
		/* Initial of menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId()) 
		{
        case R.id.connect:
			// connect BT device
			connectBT();
            return true;

        case R.id.disconnect:
			// disconnect BT device
			disconnectBT();
       		return true;

        case R.id.clear:
			// clear address of BT device
			clearPrefDeviceAddress();
        	return true;
        }
        return false;
    }

// --- onActivityResult ---
	/**
	 * === onActivityResult ===
	 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        switch (requestCode) 
		{
        case REQUEST_CONNECT_DEVICE:
			execActivityResultDevice(resultCode, data);
            break;

        case REQUEST_ENABLE_BT:
			execActivityResultEnable(resultCode, data);
            break;
        }
    }

	/**
	 * Processing when connecting BT device
	 */
    private void execActivityResultDevice(int resultCode, Intent data) 
	{
		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

		// When DeviceListActivity returns with a device to connect
		if (resultCode == Activity.RESULT_OK) {
			// Get the device MAC address
			String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			// Get the BLuetoothDevice object
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			// Attempt to connect to the device
			mCmdSendService.connect(device);
			// save Device Address
			setPrefDeviceAddress( address );
		}
	}

	/**
	 * Processing when BT service is enable
	 */
    private void execActivityResultEnable(int resultCode, Intent data) 
	{
		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

		// When the request to enable Bluetooth returns
		if (resultCode == Activity.RESULT_OK) {
			// Bluetooth is now enabled, so set up a chat session
			setupBT();

		} else {
			// User did not enable Bluetooth or an error occured
			ToastMaster.showShort( this, R.string.bt_not_enabled_leaving);
           	finish();
		}
	}
// --- end ---

// --- Message Handler ---
	/**
	 * Message Handler
	 * The Handler that gets information back from the BluetoothChatService
	 */
    private final Handler mHandler = new Handler() 
	{
		/**
	 	 * Processing Message Handler
	 	 */
        @Override
        public void handleMessage(Message msg) 
		{
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
    };

	/**
	 * Message Handler ( state change )
	 */
	private void execMessageChange( Message msg )
	{
		switch (msg.arg1) 
		{
		case BluetoothDroidControlService.STATE_CONNECTED:
			// change title bar
			txtTitle.setText(R.string.title_connected_to);
			txtTitle.append(mConnectedDeviceName);
			break;
 
		case BluetoothDroidControlService.STATE_CONNECTING:
			// change title bar
			txtTitle.setText(R.string.title_connecting);
			break;

		case BluetoothDroidControlService.STATE_LISTEN:
		case BluetoothDroidControlService.STATE_NONE:
			// change title bar
			txtTitle.setText(R.string.title_not_connected);
			break;
		}
	}

	/**
	 * Message Handler ( read )
	 */
	private void execMessageRead( Message msg )
	{
		String str = MessageToString( msg );

		// if OFF command
		if ( str.equals("b10") ) {

			// if status is ON
			if ( imgBodyToggle ) {
				// show no circle (normal)
				imgBody.setImageBitmap( idToBitmap( R.drawable.body_off ) );
				imgBodyToggle = false;

			// if status is OFF
			} else {
				// show red circle
				imgBody.setImageBitmap( idToBitmap( R.drawable.body_red ) );
				imgBodyToggle = true;
			}

		// if ON command
		} else if ( str.equals("b11") ) {
			// show yellow circle
			imgBody.setImageBitmap( idToBitmap( R.drawable.body_yellow ) );
		}

		// debug
		if ( DEBUG_RECV ) {
			Log.d(TAG, str);
			addRecvMsg( str );
			showRecvMsgs();
		}
	}

	/**
	 * Message Handler ( device name )
	 */
	private void execMessageDevice( Message msg )
	{
		 // save the connected device's name
		mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
		ToastMaster.showShort(
			getApplicationContext(), 
			"Connected to "	+ mConnectedDeviceName );

		// hide button if show
		if (btnConnect.getVisibility() == View.VISIBLE) {  
			btnConnect.setVisibility(View.INVISIBLE);  
		} 
	}

	/**
	 * Message Handler ( toast )
	 */
	private void execMessageToast( Message msg )
	{
		ToastMaster.showShort(
			getApplicationContext(), 
			msg.getData().getString(TOAST) );
	}

	/**
	 * convert message to string.
	 * convert the characters that can be displayed , 
	 * because message are binary
	 * @param msg Message Object
	 */
	private String MessageToString( Message msg )
	{
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
// --- end ---

// --- Timer Handler ---
	/**
	 * Timer Handler
	 * turn on and off LED alternately , one second priod
	 */
 	private Handler timerHandler = new Handler() 
 	{
		/**
	 	 * Processing Timer Handler
	 	 */
 		@Override
 		public void dispatchMessage(Message msg) 
 		{
			/* if there is no class object, generates them */
			createViewLeftRight();

			/* if timer mode */
			// transmit the command,
			// after check the state of the BT device,
			// because Toast is generated at one second priod,
			// if turned off the power supply of the BT device
 			if ((msg.what == TIMER_MSG) &&
				ledAutoStatus && checkStateBT() ) {

				// if status is ON
				if (ledAutoToggle) {
					ledAutoToggle = false;
					onLedLeft();
					offLedRight();

				// if status is OFF
				}else{
					ledAutoToggle = true;
					offLedLeft();
					onLedRight();
				}

				// restart timer handler
  				timerHandler.sendEmptyMessageDelayed(TIMER_MSG, TIMER_INTERVAL * 1000);

			// otherwise
 			} else {
 				super.dispatchMessage(msg);
 			}
 		}
 	};
// --- end ---

// --- BT service and device ---
	/**
	 * Initialization of BT service
	 */
    private void setupBT() 
	{
		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

        // Initialize the BluetoothChatService to perform bluetooth connections
    	mCmdSendService = new BluetoothDroidControlService(this, mHandler);
    }

	/**
	 * connect BT device
	 */
    private void connectBT() 
	{
		// no action if debug
        if ( DEBUG_BT ) {
            ToastMaster.showShort(this, "No Action in debug");
        	return;
        }

		// connect the BT device at once
		// if there is a device address. 
		String address = getPrefDeviceAddress();
		if (( address != null) && !address.equals("") ) {
        	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            mCmdSendService.connect(device);

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
    private void disconnectBT() 
	{
		// no action if debug
        if ( DEBUG_BT ) {
        	return;
        }

		// Stop the Bluetooth chat services
		if (mCmdSendService != null) mCmdSendService.stop();
	}

	/**
	 * check the status of BT service
	 */
    private boolean checkStateBT() 
	{
		// true if debug
        if ( DEBUG_BT ) {
        	return true;
        }

		// if connected
    	if ( mCmdSendService.getState() == BluetoothDroidControlService.STATE_CONNECTED ) {
			return true;
		}

		// otherwise
		return false;
	}

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessageBT(String message) 
	{
		// no action if debug
        if ( DEBUG_BT ) {
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
            mCmdSendService.write(send);
        }
    }

// --- LED control ---
    /**
     * turn on left LED
     */
    private void onLedLeft() 
	{
		mImgLeft.setImageBitmapOn();
       	sendMessageBT("l11");
	}

    /**
     * turn off left LED
     */
    private void offLedLeft() 
	{
		mImgLeft.setImageBitmapOff();
       	sendMessageBT("l10");
	}

    /**
     * turn on right LED
     */
    private void onLedRight() 
	{
		mImgRight.setImageBitmapOn();
       	sendMessageBT("l21");
	}

    /**
     * turn off right LED
     */
    private void offLedRight() 
	{
		mImgRight.setImageBitmapOff();
       	sendMessageBT("l20");
	}

    /**
     * convert Resource ID to Bitmap
     */
    private Bitmap idToBitmap( int id ) 
	{
		return BitmapFactory.decodeResource( getResources(), id );
	}

// --- Shared Preferences ---
	/**
	 * get the device address
	 */
    private String getPrefDeviceAddress() 
	{
		SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		return pref.getString(PREF_ADDR, "");
	}

	/**
	 * save the device address
	 */
    private void setPrefDeviceAddress( String addr ) 
	{
		SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		pref.edit().putString(PREF_ADDR, addr).commit();
	}

	/**
	 * clear the device address
	 */
    private void clearPrefDeviceAddress() 
	{
		setPrefDeviceAddress( "" );
	}

// --- debug ---
	/**
	 * add the message to the list (touch)
	 * @param msg String
	 */
    private void addTouchMsg( String msg ) 
	{
		touchMsgs.add( msg );
	}

	/**
	 * add the message to the list (recv)
	 * @param msg String
	 */
    private void addRecvMsg( String msg ) 
	{
		recvMsgs.add( msg );
	}

	/**
	 * show the messages (touch)
	 */
    private void showTouchMsgs() 
	{
		String msg = buildMsgs( touchMsgs, MAX_TOUCH_MSG, "\n" );

		/* show message */
   		txtMsg.setText(msg);
	}

	/**
	 * show the messages (recv)
	 */
    private void showRecvMsgs() 
	{
		String msg = buildMsgs( recvMsgs, MAX_RECV_MSG, " " );

		/* show message */
   		txtMsg.setText(msg);
	}

	/**
	 * build the messages
	 * @param msgs : message list
	 * @param max  : max of showing messages
	 * @param del  : delmita of messages
	 */
    private String buildMsgs( List<String> msgs, int max, String del ) 
	{
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

// --- end ---

}
