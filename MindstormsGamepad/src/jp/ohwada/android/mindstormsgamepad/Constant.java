package jp.ohwada.android.mindstormsgamepad;

/**
 * Constant
 */
public class Constant {

// === Debug ===
    public final static Boolean DEBUG = true; 
	public static final boolean BT_DEBUG_LOG_ACTIVITY = true;   
	public static final boolean BT_DEBUG_LOG_MANAGER = true;    
    public static final boolean BT_DEBUG_LOG_SERVICE = true;
	public static final boolean BT_DEBUG_LOG_DEVICE = true;

	// for emulator	
	public static final boolean BT_DEBUG_SERVICE = false;
	public static final boolean DEBUG_ORIENTATION_SENSOR = false;

	public final static String TAG = "MindStorms";

    /* new line */
	public static final String LF = "\n";
	
    /* Intent request codes */	
	public static final int REQUEST_ORIENTATION = 2;
	public static final int REQUEST_VOICE = 3;
	public static final int REQUEST_JOYSTICK = 4;
	public static final int REQUEST_TWO_JOYSTICK = 5;
	public static final int REQUEST_GAMEPAD = 6;
	public static final int REQUEST_SOUND = 11;
	public static final int REQUEST_REPORT = 12;
	public static final int REQUEST_PROGRAM = 13;
	public static final int REQUEST_RECOGNIZER = 14;
    public static final int REQUEST_SETTING = 21;

	// Bluetooth            
    public static final int REQUEST_BT_DEVICE_CONNECT = 101;
    public static final int REQUEST_BT_ADAPTER_ENABLE = 102;

	/* Message handler type */
    public static final int WHAT_BT_MSG_STATE_CHANGE = 21;
    public static final int WHAT_BT_MSG_READ = 22;
    public static final int WHAT_BT_MSG_WRITE = 23;
    public static final int WHAT_BT_MSG_DEVICE_NAME = 24;
    public static final int WHAT_BT_MSG_FAILED = 25;
	public static final int WHAT_BT_MSG_LOST = 26;
	
    /* Call Intent extra */
	public static final String EXTRA_PARENT = "parent";
	
	/* Return Intent extra */
    public static final String BT_EXTRA_DEVICE_ADDRESS = "bt_device_address";

    /* Extra value */ 
    public static final int PARENT_DEFAULT = 0;
    public static final int PARENT_CALL = 1;

	/* SharedPreferences */
    public static final String PREF_BT_ADDR = "bt_addr";
	public static final String PREF_BT_USE_DEVICE = "bt_use_device";
	public static final String PREF_BT_SHOW_DEBUG = "bt_show_debug";

	public static final String PREF_AXIS_X = "axis_x";	
	public static final String PREF_AXIS_Y = "axis_y";	
	public static final String PREF_AXIS_SIGN_X = "axis_sign_x";	
	public static final String PREF_AXIS_SIGN_Y = "axis_sign_y";	
	public static final String PREF_AXIS_LEFT = "axis_left";	
	public static final String PREF_AXIS_RIGHT = "axis_right";	
	public static final String PREF_AXIS_SIGN_LEFT = "axis_sign_left";	
	public static final String PREF_AXIS_SIGN_RIGHT = "axis_sign_right";	
	public static final String PREF_KEYCODE_LEFT_FORWARD = "keycode_left_forward";
	public static final String PREF_KEYCODE_LEFT_BACK = "keycode_left_back";
	public static final String PREF_KEYCODE_RIGHT_FORWARD = "keycode_right_forward";
	public static final String PREF_KEYCODE_RIGHT_BACK = "keycode_right_back";
				
	public final static int DIRECTION_NONE = 0;
	public final static int DIRECTION_FORWARD_LEFT = 1;
	public final static int DIRECTION_FORWARD = 2;
	public final static int DIRECTION_FORWARD_RIGHT = 3;
	public final static int DIRECTION_LEFT = 4;
	public final static int DIRECTION_CENTER = 5;
	public final static int DIRECTION_RIGHT = 6;
	public final static int DIRECTION_BACK_LEFT = 7;
	public final static int DIRECTION_BACK = 8;
	public final static int DIRECTION_BACK_RIGHT = 9;

	public static final int BUTTON_SAVE = 21;
	public static final int BUTTON_CANCEL = 22;
	  
 }
