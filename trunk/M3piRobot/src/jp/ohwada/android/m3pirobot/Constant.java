package jp.ohwada.android.m3pirobot;

/**
 * Constant
 */
public class Constant {

	/* Debug flag */
	public static final boolean DEBUG_BT_SERVICE = false;
	public static final boolean DEBUG_BT_CONNECT  = false;
	public static final boolean DEBUG_BT_RECV_MSG  = false;
	public static final boolean DEBUG_LOG_SERVICE = true;
	public static final boolean DEBUG_LOG_DEVICE = true;
	public static final boolean DEBUG_LOG_BT = true;
				    
    /* Message types sent from the BluetoothChatService Handler */
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    /* Key names received from the BluetoothChatService Handler */
    public static final String BUNDLE_DEVICE_NAME = "device_name";
    public static final String BUNDLE_TOAST = "toast";
    
	/* Constants that indicate the current connection state */
    public static final int BT_STATE_NONE = 0;       // we're doing nothing
    public static final int BT_STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int BT_STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int BT_STATE_CONNECTED = 3;  // now connected to a remote device
}
