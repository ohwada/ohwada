package jp.ohwada.android.mindstormsgamepad;

import java.util.ArrayList;
import java.util.List;

import jp.ohwada.android.mindstormsgamepad.util.BluetoothManager;
import jp.ohwada.android.mindstormsgamepad.util.ByteUtility;
import jp.ohwada.android.mindstormsgamepad.util.DebugInfo;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsMessage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

/**
 * Bluetooth Command
 */
public class BtCommand {

	/** Debug */
	private static final boolean D = Constant.DEBUG;
    private static final String TAG = Constant.TAG; 
    private String TAG_SUB = "BtCommand";

	public static final int ACTIVITY_RESULT_NONE = 0;
	public static final int ACTIVITY_RESULT_SUCCESS = 1;	
	public static final int ACTIVITY_RESULT_ADAPTER_FAILED = 2;	
	
	private static final int FILE_MODE_ALL = 0;
	private static final int FILE_MODE_PROGRAM = 1;
	private static final int FILE_MODE_SOUND = 2;
	private static final int MAX_RECV_FILES = 20;
	private static final String FILE_SEARCH_PROGRAM = "*.*";
	private static final String FILE_SEARCH_SOUND = "*.rso";

	private static final int PLAYTONE_WAIT_TIME = 20;
		
	private static final String BUNDLE_CMD_MESSAGE = "message";
	private static final String BUNDLE_CMD_NAME= "name";
	private static final String BUNDLE_CMD_VALUE1 = "value1";
	private static final String BUNDLE_CMD_VALUE2 = "value2";
		
	// callback
	private OnChangedListener mOnListener;
	
	// class object
	private ByteUtility mByteUtility;
	private BluetoothManager mBluetoothManager;
	private MindstormsCommand mMindstormsCommand;
    
	// local varibale
    private boolean isMotorReply = false;
	private  boolean isLejosMindDroid = false;
    private List<String> mFileList = null;
    private int mFileMode = FILE_MODE_ALL ;
	
	/**
	 * interface OnChangedListener
	 */
    public interface OnChangedListener {
    	void onDeviceList();
        void onRead( List<byte[]> list );
        void onFindEnd( List<String> list );
        void onRecvViaHandler( Bundle bundle, int message, int value1, int value2, String name );
    }

	/**
	 * setOnClickListener
	 * @param OnButtonsClickListener listener
	 */
    public void setOnChangedListener( OnChangedListener listener ) {
        mOnListener = listener;
    }

	/*
	 * === Constractor ===
	 * @param Context context
	 */
	public BtCommand( Activity activity, View view ) {
    	Context context = activity;     
		/* Initialization of Bluetooth */
		mBluetoothManager = new BluetoothManager( activity ); 
		mBluetoothManager.initTitle( 
			R.string.app_name,
			R.string.bt_title_connecting,
			R.string.bt_title_connected,
			R.string.bt_title_not_connected
		 );  
		mBluetoothManager.initToast( 
			R.string.bt_connection_failed,
			R.string.bt_connection_lost
		);
		mBluetoothManager.initButtonConnect( view, R.id.button_connect ); 
		mBluetoothManager.initTextViewDebug( view, R.id.textview_debug );
		mBluetoothManager.setOnChangedListener( new BluetoothManager.OnChangedListener() { 
	      	@Override 
        	public void onDeviceList() {
				notifyDeviceList();
        	} 
	      	@Override 
        	public void onRead( byte[] bytes ) {
        		recvRead( bytes );
        	}
        });		
		
		/* Initialization of DebugInfo */
		DebugInfo.writePackageInfo( context ); 				
		mByteUtility = new ByteUtility();
	}

// --- onCreate ---
 	/**
	 * initAdapter
	 */
     public boolean initAdapter() {
    	return mBluetoothManager.initAdapter();
    }

 	/**
	 * setTitleName
	 * @param int resId
	 */
    public void setTitleName( int resId ) {
		mBluetoothManager.setTitleName( resId );  
	}

	/**
	 * isServiceConnected
	 * @return boolean 
	 */
	public boolean isServiceConnected()  {	
		return mBluetoothManager.isServiceConnected();
	}

 	/**
	 * initMindstormsCommand
	 */		
    public MindstormsCommand initMindstormsCommand() {
       mMindstormsCommand = new MindstormsCommand();
       return mMindstormsCommand;         
    }
 
// --- onStart ---    
 	/**
	 * enableService
	 */
    public boolean enableService() {
        return mBluetoothManager.enableService();
    }

// --- onResume ---
 	/**
	 * startService
	 */
    public void startService() {
		mBluetoothManager.startService();		
    }

// -- onDestroy ---
 	/**
	 * stopService
	 */
   public void stopService() {
		mBluetoothManager.stopService();
    }

// --- onOptionsItemSelected ---
 	/**
	 * execOptionsItemSelected
	 * @param MenuItem item
	 * @return boolean
	 */
    public boolean execOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_bt_connect:
				mBluetoothManager.connectService();
				return true;
			case R.id.menu_bt_disconnect:
				mBluetoothManager.stopService();
				return true;
			case R.id.menu_bt_clear:
				mBluetoothManager.clearPrefAddress();
				return true;
        }
        return false;
    }

// --- onActivityResult ---
 	/**
	 * execActivityResult
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
    public int execActivityResult( int request, int result, Intent data ) {
    	int ret = ACTIVITY_RESULT_NONE;
        switch ( request) {
			case BluetoothManager.REQUEST_DEVICE_CONNECT:
				mBluetoothManager.execActivityResultDevice( result, data );
				ret = ACTIVITY_RESULT_SUCCESS;
            	break;
        	case BluetoothManager.REQUEST_ADAPTER_ENABLE:
				boolean ret2 = mBluetoothManager.execActivityResultAdapter( result, data );
				ret = ACTIVITY_RESULT_SUCCESS;
				/* finish if NOT get Bluetooth Adapter */   
				if ( !ret2 ) {
					ret = ACTIVITY_RESULT_ADAPTER_FAILED;
				}	
            	break; 
			case Constant.REQUEST_SETTING:
         		// onResume
				ret = ACTIVITY_RESULT_SUCCESS;
            	break;   
    	}
    	return ret;
    }
	
// --- Bluetooth send ---
	/**
	 * Send Move
	 * @param int left
	 * @param int right
	 */	
	public void sendMove( int left, int right ) {
		log_d( "sendMove " + left + " " + right );
		 // don't send motor stop twice
		if (( Math.abs(left) < 10 ) && ( Math.abs(right) < 10)) {
			sendStop();
			return;
		}		
		isMotorReply = false;
		byte[] cmd = MindstormsMessage.cmdMove( left, right );
		writeBytes( cmd ); 
	}
	
	/**
	 * Send Move
	 */	
	public void sendStop() {
		 // don't send motor stop twice
		if ( isMotorReply ) return;
		isMotorReply = false;		
		byte[] cmd = MindstormsMessage.cmdStop();
		writeBytes( cmd ); 
	}

	/**
	 * Send Move
	 * @param int left
	 * @param int right
	 * @param long duration 
	 */	
	public void sendMoveDuration( int left, int right, long duration ) {
		sendMove( left, right );
		delayStop( duration );
	}
	
	/**
	 * Send FindFirst
	 */	
	public void sendFindFirstProgram() {
		sendFindFirst( FILE_SEARCH_PROGRAM, FILE_MODE_PROGRAM );
	}
	
	/**
	 * Send FindFirst
	 */	
	public void sendFindFirstSound() {
		sendFindFirst( FILE_SEARCH_SOUND, FILE_MODE_SOUND );
	}
	
	/**
	 * Send FindFirst
	 */	
	public void sendFindFirst( String search, int mode ) {
		mFileMode = mode;
		mFileList = new ArrayList<String>();
		byte[] cmd = MindstormsMessage.cmdFindFirst( search );
		writeBytes( cmd ); 
	}

	/**
	 * Send FindNext
	 */	
	private void sendFindNext( int handle ) {
		byte[] cmd = MindstormsMessage.cmdFindNext( handle );
		writeBytes( cmd ); 
	}

	/**
	 * send PlayTone
	 */	
    public void sendPlayTone( int frequency, int duration ) {
		byte[] cmd = MindstormsMessage.cmdPlayTone( frequency, duration );
		writeBytes( cmd );
		waitTime( PLAYTONE_WAIT_TIME );
    }

	/**
	 * write
	 * @param byte[] bytes
	 */
	public void writeBytes( byte[] bytes ) {	
		mBluetoothManager.writeBytes( bytes );
	}

// --- Bluetooth recv ---
	/**
	 * read 
	 * @param byte[] bytes  
	 */
	 private void recvRead( byte[] read_bytes ) {
	 	List<byte[]> list = mMindstormsCommand.getReceiveMessageList( read_bytes );
		for ( int i=0; i < list.size(); i++ ) { 
	 		byte[] each_bytes = list.get( i );
			String msg = mByteUtility.bytesToHexString( each_bytes ); 
	 		log_d( "r " + msg ); 
	 		execSetOutputState( each_bytes );
	 		execFindFiles( each_bytes );
		}
		notifyRead( list ); 			
	}

	/**
	 * execSetOutputState
	 * check replay to stop command
	 * @param byte[] bytes
	 */
	private void execSetOutputState( byte[] bytes ) {
		int reply = mMindstormsCommand.getReply( bytes );
		if ( reply == MindstormsCommand.REPLY_SET_OUTPUT_STATE ) {
			isMotorReply = true;
		}
	}
	
	/**
	 * execFindFiles
	 * @param byte[] bytes  
	 */
	private void execFindFiles( byte[] bytes ) {
		int reply = mMindstormsCommand.getReply( bytes );
	 	switch ( reply ) {
			case MindstormsCommand.REPLY_FIND_FILES:
				execRecvFindFiles( bytes );	
				break;
			case MindstormsCommand.REPLY_FIND_FILES_ERROR:
				notifyFindEnd( mFileList );
				break;				
		}
	}

	/**
	 * receive : FindFiles
	 */
	private void execRecvFindFiles( byte[] bytes ) {
		MindstormsCommand.RecvMessage recv = mMindstormsCommand.getRecvFindFiles( bytes );
		List<String> list = getRecvFindList( recv );
		if ( list.size() <= MAX_RECV_FILES ) {
			sendFindNext( recv.FindFiles_handle_number );
		} else {
			notifyFindEnd( list );
		}
	}

	/**
	 * getRecvFindList
	 * @param MindstormsCommand.RecvMessage recv
	 * @return List<String>
	 */
	private List<String> getRecvFindList( MindstormsCommand.RecvMessage recv ) { 
		String name = recv.FindFiles_name;
		boolean isSave = false;
		if ( mFileMode == FILE_MODE_PROGRAM ) {
			if ( isLejosMindDroid || name.endsWith(".nxj") || name.endsWith(".rxe") ) {
				isSave = true;
			}
		} else if ( mFileMode == FILE_MODE_SOUND ) {
			if ( name.endsWith(".rso") ) {
				isSave = true;
			}
		} else {
			isSave = true;
		}
		if ( isSave ) {
			mFileList.add( name );
		}
		return mFileList;	
	}
		
// --- comand end ---
	/**
	 * notifyDeviceList
	 * @param byte[] bytes
	 */
	private void notifyDeviceList() {
		if ( mOnListener != null ) {
			mOnListener.onDeviceList();
		}
	}

	/**
	 * notifyRead
	 * @param List<byte[]> list 
	 */
	private void notifyRead( List<byte[]> list ) {
		if ( mOnListener != null ) {
			mOnListener.onRead( list );
		}
	}

	/**
	 * notifyFindEnd
	 * @param List<String> list
	 */		
	private void notifyFindEnd( List<String> list) {
		if ( mOnListener != null ) {
			mOnListener.onFindEnd( list );
		}
	}

	/**
	 * notifyRecvViaHandler(
	 */
    private void notifyRecvViaHandler( Bundle bundle ) {
    	int message = bundle.getInt( BUNDLE_CMD_MESSAGE );
		int value1 = bundle.getInt( BUNDLE_CMD_VALUE1 );
		int value2 = bundle.getInt( BUNDLE_CMD_VALUE2 );
		String name = bundle.getString( BUNDLE_CMD_NAME );
		if ( mOnListener != null ) {
			mOnListener.onRecvViaHandler( bundle, message, value1, value2, name );
		}
	}
						
// --- sendHandler ---
    /**
     * Sends the message via handler to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type 
     * @param String a String parameter
     */       
    public void sendViaHandler( int delay, int message, String name ) {
        Bundle bundle = new Bundle();
        bundle.putInt( BUNDLE_CMD_MESSAGE, message );
        bundle.putString( BUNDLE_CMD_NAME, name );
        sendMessageViaHandler( delay, bundle );  
	}

	/**
     * Sends the message via handler to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type 
     * @param value1 first parameter
     * @param value2 second parameter
     */
    public void sendViaHandler( int delay, int message, int value1, int value2 ) {
        Bundle bundle = new Bundle();
        bundle.putInt( BUNDLE_CMD_MESSAGE, message );
        bundle.putInt( BUNDLE_CMD_VALUE1, value1 );
        bundle.putInt( BUNDLE_CMD_VALUE2, value2 );
        sendMessageViaHandler( delay, bundle ); 
    }
  
    /**
     * sendMessage
     * @param int delay
     * @param Bundle bundle
     * @param int delay
     */	
	private void sendMessageViaHandler( int delay, Bundle bundle ) {
        Message message = sendHandler.obtainMessage();
        message.setData( bundle );
        if ( delay == 0 ) {
            sendHandler.sendMessage( message );
        } else {
            sendHandler.sendMessageDelayed( message, delay );
         } 
	}
	
    /**
     * sendHandler
     */	
	private final Handler sendHandler = new Handler() {
		@Override
		public void handleMessage( Message message ) {
			Bundle bundle = message.getData();
			notifyRecvViaHandler( bundle );
		}
	};
	         
// --- stopHandler ---
	/**
	 * delayStop
	 */	 
	public void delayStop( long time ) {
        stopHandler.postDelayed( stopTask, time );
	} 

	/**
	 * Handler class
	 */	
    private final Handler stopHandler = new Handler();

	/**
	 * Runnable class
	 */	 
    private final Runnable stopTask = new Runnable() {
        @Override
        public void run() {
			sendStop();
        }
    };      
// --- handler end ---

    /**
     * waitTime
     */	
	public void waitTime( int millis ) {
        try {
			Thread.sleep( millis );
        } catch ( InterruptedException e ) {
			if (D) e.printStackTrace();
        }
    }

// --- debug ---
 	/**
	 * setMaxDebugMsg
	 * @param int max
	 */			
    public void setMaxDebugMsg( int max ) {
    	mBluetoothManager.setDebugMaxMsg( max );
    }

	/**
	 * show Debug
	 * @param String msg
	 */				
	public void showDebug( String msg ) {
    	mBluetoothManager.showTextViewDebug( msg );
	}
   	
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
  
}
