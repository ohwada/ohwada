package jp.ohwada.android.mindstormsgamepad.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {

	/* Debug flag */
    private static final String TAG_SUB = "BluetoothService";
    private static final boolean D = Constant.BT_DEBUG_LOG_SERVICE;
    private static final String TAG = Constant.TAG; 

    /* Message types sent from the BluetoothService Handler */
	public static final int WHAT_READ = Constant.WHAT_BT_MSG_READ;
	public static final int WHAT_WRITE = Constant.WHAT_BT_MSG_WRITE;
    public static final int WHAT_STATE_CHANGE = Constant.WHAT_BT_MSG_STATE_CHANGE;
    public static final int WHAT_DEVICE_NAME = Constant.WHAT_BT_MSG_DEVICE_NAME;
    public static final int WHAT_FAILED = Constant.WHAT_BT_MSG_FAILED;
    public static final int WHAT_LOST = Constant.WHAT_BT_MSG_LOST;
    
	/* Constants that indicate the current connection state */
	public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /* Key names received from the BluetoothChatService Handler */
	public static final String BUNDLE_DEVICE_NAME = "device_name";
 
    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
 
    private Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
	private String mDeviceName = null;
	private String mDeviceAddress = null;
  
    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothService( Context context, Handler handler ) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    	setHandler( handler );
    }

    /**
     * Constructor
     * @param context  The UI Activity Context
     */
    public BluetoothService( Context context ) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    /**
     * setHandler
	 * specify for every activity.
     * @param Handler handler
     */
    public void setHandler( Handler handler ) {
        mHandler = handler;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState( int state ) {
       	log_d( "setState() " + mState + " -> " + state );
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage( WHAT_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
	 * @return int
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * get DeviceName
	 * @return String
     */
    public String getDeviceName() {
        return mDeviceName;
    }

    /**
     * get DeviceAddress
	 * @return String
     */
    public String getDeviceAddress() {
        return mDeviceAddress;
    }
        
    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() 
     */
    public synchronized void start() {
        log_d( "start" );
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
        	mConnectThread.cancel(); 
        	mConnectThread = null; 
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        setState( STATE_LISTEN );
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        log_d( "connect to: " + device );
        // Cancel any thread attempting to make a connection
        if ( mState == STATE_CONNECTING ) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState( STATE_CONNECTING );
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected( BluetoothSocket socket, BluetoothDevice device ) {
        log_d( "connected" );
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        mDeviceName = device.getName();
        mDeviceAddress = device.getAddress();
        Message msg = mHandler.obtainMessage( WHAT_DEVICE_NAME) ;
        Bundle bundle = new Bundle();
        bundle.putString( BUNDLE_DEVICE_NAME, mDeviceName);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState( STATE_CONNECTED );
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        log_d( "stop" );
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        setState( STATE_NONE );
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if ( mState != STATE_CONNECTED ) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState( STATE_LISTEN );
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage( WHAT_FAILED );
        mHandler.sendMessage( msg );
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState( STATE_LISTEN );
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage( WHAT_LOST );
        mHandler.sendMessage( msg );
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

    	/**
     	 * ConnectThread
		 * @param BluetoothDevice device
     	 */
        public ConnectThread( BluetoothDevice device ) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch ( IOException e ) {
				if (D) e.printStackTrace();
            }
            mmSocket = tmp;
        }

    	/**
     	 * run
     	 */
        public void run() {
            log_d( "BEGIN mConnectThread" );
            setName( "ConnectThread" );
            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch ( IOException e ) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch ( IOException e2 ) {
                    if (D) e2.printStackTrace();
                }
                // Start the service over to restart listening mode
                BluetoothService.this.start();
                return;
            }
            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }
            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

    	/**
     	 * cancel
     	 */
        public void cancel() {
            try {
                mmSocket.close();
            } catch ( IOException e ) {
				if (D) e.printStackTrace();
            }
        }
    }  // class end
    
    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

    	/**
     	 * ConnectThread
		 * @param BluetoothSocket socket
     	 */
        public ConnectedThread( BluetoothSocket socket ) {
            log_d( "create ConnectedThread" );
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch ( IOException e ) {
				if (D) e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

    	/**
     	 * run
     	 */
        public void run() {
            log_d( "BEGIN ConnectedThread" );
            byte[] buffer = new byte[1024];
            int bytes;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage( WHAT_READ, bytes, -1, buffer ).sendToTarget();
                } catch ( IOException e ) {
					if (D) e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write( byte[] buffer ) {
            try {
                mmOutStream.write( buffer );
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage( WHAT_WRITE, -1, -1, buffer ).sendToTarget();
            } catch ( IOException e ) {
				if (D) e.printStackTrace();
            }
        }

    	/**
     	 * cancel
     	 */
        public void cancel() {
            try {
                mmSocket.close();
            } catch ( IOException e ) {
				if (D) e.printStackTrace();
            }
        }
    }  // class end
     
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}
}
