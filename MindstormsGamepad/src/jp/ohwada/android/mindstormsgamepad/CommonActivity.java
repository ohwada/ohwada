package jp.ohwada.android.mindstormsgamepad;

import java.util.Iterator;
import java.util.List;

import jp.ohwada.android.mindstormsgamepad.util.BluetoothManager;
import jp.ohwada.android.mindstormsgamepad.util.ByteUtility;
import jp.ohwada.android.mindstormsgamepad.util.InputDeviceManager;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.PowerSeekbar;
import jp.ohwada.android.mindstormsgamepad.view.SeekbarPower;
import jp.ohwada.android.mindstormsgamepad.view.ToastMaster;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Common Activity
 */
public class CommonActivity extends Activity {

	/** Debug */
	private static final boolean D = Constant.DEBUG;
    private static final String TAG = Constant.TAG; 
    protected String TAG_SUB = "CommonActivity";

	protected static final String LF = Constant.LF;

	private int MAX_DEBUG_MSG = 20;
	
	protected Resources mResources;
	protected ByteUtility mByteUtility;
	
	// class object
	protected BtCommand mCommand;	
	protected MindstormsCommand mMindstormsCommand;
	protected InputDeviceManager mInputDeviceManager;	
	
	// class object		
	protected SeekbarPower mSeekbarPower;
	protected PowerSeekbar mPowerSeekbar;
	
	/* view component */
	private Button mButtonBack;
    
	// local varibale
	protected  boolean isLejosMindDroid = false;
	private CharSequence[] mFileNames; 
	
// --- onCreate ---	
 	/**
	 * initTabSub
	 * @param String tag
	 */		
    protected void initTabSub( String tag ) {
    	TAG_SUB = tag;         
    }

 	/**
	 * initManager
	 * @param View view
	 */		
    protected void initManager( View view ) {
		mResources = getResources();   
		mByteUtility = new ByteUtility();  
    		
		mCommand = new BtCommand( this, view );
    	mCommand.setMaxDebugMsg( MAX_DEBUG_MSG );
		mCommand.setOnChangedListener( new BtCommand.OnChangedListener() { 
	      	@Override 
        	public void onDeviceList() {
        		startDeviceList();
        	} 
	      	@Override 
        	public void onRead( List<byte[]> list ) {
        		execRead( list );	
        	} 
	      	@Override 
        	public void onFindEnd( List<String> list ) {
        		execRecvFindEnd( list );
        	} 
	      	@Override 
        	public void onRecvViaHandler( Bundle bundle, int message, int value1, int value2, String name ) {
        		execRecvViaHandler( message, value1, value2  );
        		execRecvViaHandler( message, name );
        	}
        });

    	mMindstormsCommand = mCommand.initMindstormsCommand();         

		/* finish if NOT get Bluetooth Adapter */       
		boolean ret = mCommand.initAdapter();
		if ( !ret ) {
			toast_short( R.string.bt_not_available );
        	finish();
        }		
    }

 	/**
	 * setTitleName
	 * @param int resId
	 */
    protected void setTitleName( int resId ) {
    	mCommand.setTitleName( resId );  
	}
    
 	/**
	 * initSeekbarPower
	 */	
    protected void initSeekbarPower( View view ) {
		mSeekbarPower = new SeekbarPower();
		mSeekbarPower.setParam( 
			MindstormsCommand.MAX_POWER,
			MindstormsCommand.SEEK_POWER_DEFAULT );	
		mSeekbarPower.initSeekbarPower( view, R.id.seekbar_power );	                  
		mPowerSeekbar = new PowerSeekbar(
			MindstormsCommand.MAX_POWER,
			MindstormsCommand.ORIENTATION_MIN_POWER );
    }

 	/**
	 * initButtonBack
	 */	
    protected void initButtonBack() {    
		/* Back button */       
        mButtonBack = (Button) findViewById( R.id.button_back );
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	execClickButtonBack();
			}
        });

	}

 	/**
	 * execClickButtonBack
	 */	
	protected void execClickButtonBack() {
		finish();
	}
			
 	/**
	 * initInputDeviceManager
	 */	
	protected void initInputDeviceManager() {			
        mInputDeviceManager = new InputDeviceManager( this );
		mInputDeviceManager.getInputDevices(); 
        mInputDeviceManager.setOnInputDeviceListener( new InputDeviceManager.OnInputDeviceListener() {
            public void onInputDeviceAdded( int deviceId ) {
				String name = mInputDeviceManager.addInputDevice( deviceId );
				toast_short( "Added " + name );       
            }
            public void onInputDeviceRemoved( int deviceId ) {
				String name = mInputDeviceManager.addInputDevice( deviceId );
				toast_short( "Changed " + name );          
            }
			public void onInputDeviceChanged( int deviceId ) {
				String name = mInputDeviceManager.removeInputDevice( deviceId );
				toast_short( "Removed " + name + " " + deviceId );      
            }
        });
                                
    }
					        
// --- onCreate end ---

	/**
	 * === onStart ===
	 */
	@Override
	public void onStart() {
		log_d( "cmd onStart()" );
		super.onStart();
		enableService();	
	}

 	/**
	 * enableService
	 */
    protected void enableService() {
		boolean ret = mCommand.enableService();
		if ( ret ) {
			Intent intent = new Intent( BluetoothManager.ACTION_ADAPTER_ENABLE ) ;
			startActivityForResult( intent, BluetoothManager.REQUEST_ADAPTER_ENABLE );	
		}
    }
    
// --- onResume ---

 
	/**
	 * === onResume ===
	 */
	@Override
	public void onResume() {
		log_d( "cmd onResume()" );
		super.onResume();
		startService();
	}

 	/**
	 * startService
	 */
    protected void startService() {
		mCommand.startService();
    }
  
 	/**
	 * === onPause ===
	 */
	@Override
	public void onPause() {
		log_d( "cmd onPause()" );
		super.onPause();
		sendStop();
	}

	/**
	 * === onStop ===
	 */
	@Override
	public void onStop() {
		log_d( "cmd onStop()" );
		super.onStop();
		sendStop();
	}

	/**
	 * === onDestroy ===
	 */
	@Override
	public void onDestroy() {
		log_d( "cmd onDestroy()" );
		super.onDestroy();
		sendStop();
	}

 	/**
	 * stopService
	 */
   protected void stopService() {
		mCommand.stopService();
    }

	/**
	 * === onCreateOptionsMenu ===
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		log_d( "cmd onCreateOptionsMenu()" );
		/* Initial of menu */
		getMenuInflater().inflate( R.menu.bt, menu );
		return true;
	}
 
 	/**
	 * === onOptionsItemSelected ===
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		log_d( "cmd onOptionsItemSelected()" );
		return execOptionsItemSelected( item );		
	}

 	/**
	 * execOptionsItemSelected
	 * @param MenuItem item
	 * @return boolean
	 */
    protected boolean execOptionsItemSelected( MenuItem item ) {
    	return mCommand.execOptionsItemSelected( item );
    }
    
	/**
	 * === onActivityResult ===
	 */
	@Override
	public void onActivityResult( int request, int result, Intent data ) {
		log_d( "cmd onActivityResult()" );
		execActivityResult( request, result, data );
	}

 	/**
	 * execActivityResult
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
    protected void execActivityResult( int request, int result, Intent data ) {
		int ret = mCommand.execActivityResult( request, result, data );
		if ( ret == BtCommand.ACTIVITY_RESULT_ADAPTER_FAILED ) {
			toast_short( R.string.bt_not_enabled );
			finish();
		}
    }
    
 // -- Bluetooth callback --- 
 	/**
 	 * startDeviceList
 	 */
 	private void startDeviceList() {
 		Intent intent = new Intent( this, BtDeviceListActivity.class );
 		startActivityForResult( intent, Constant.REQUEST_BT_DEVICE_CONNECT );	
 	}
 			
// --- comand send ---
	/**
	 * sendMove_seekbar
	 * @param int direction
	 */		
    protected void sendMove_seekbar( int direction ) {
    	int main = mSeekbarPower.getPowerMain();
		mPowerSeekbar.calc( direction, main );
		sendMove( mPowerSeekbar.getLeft(), mPowerSeekbar.getRight() );
	}

	/**
	 * cmd Send Move
	 */	
	protected void sendMove( int left, int right ) {
		mCommand.sendMove( left, right ) ;
	}
	
	/**
	 * cmd Send Move
	 */	
	protected void sendStop() {
		mCommand.sendStop( ) ;
	}

	/**
	 * sendCmdMassage
	 * @param byte[] cmd
	 */
	protected void sendCmdMassage( byte[] cmd ) {
		mCommand.writeBytes( cmd ); 
	}
// --- comand send end ---

// --- comand recv ---
	 private void execRead( List<byte[]> list ) {
		for ( int i=0; i < list.size(); i++ ) { 
	 		execRecvCmdMessage( list.get( i ) );	
		}
	}

	/**
	 * execRecvCmdMessage ( overwrite )
	 * @param byte[] bytes  
	 */
	protected void execRecvCmdMessage( byte[] bytes ) {
		// dummy
	}

	/**
	 * findEnd ( overwrite )
	 */
	protected void execRecvFindEnd( List<String> list ) {
		// dummy
	}

	/**
     * execRecvViaHandler ( overwrite )
     * @param int message
     * @param int value1
	 * @param int value2
     */	
	protected void execRecvViaHandler( int message, int value1, int value2 ) {
		// dummy
	}

    /**
     * execRecvViaHandler ( overwrite )
     * @param int message
     * @param String name
     */	
	protected void execRecvViaHandler( int message, String name ) {
		// dummy
	}

	/**
	 * receive : Firmware
	 * @param byte[] bytes
	 */
	protected String getRecvFirmwareVersion( byte[] bytes ) {
		String msg = "";  
	 	MindstormsCommand.RecvMessage recv = mMindstormsCommand.getRecvFirmwareVersion( bytes ); 
		if ( mMindstormsCommand.isStatusSuccess( recv ) ) {
			msg = recv.FirmwareVersion_protocol + " " + recv.FirmwareVersion_firmware;
			isLejosMindDroid = recv.isLejosMindDroid;	
	 	} else {
			msg = mResources.getString( R.string.firmware_version_error );
	 	}
	 	return msg;
	}
	
// --- comand end ---

// --- dialog ---
    /**
     * Shows the dialog
     * @param startStop when true shows another title (for leJOSMINDdroid)
     */     
	protected void showDialog( List<String> list, String title ) {    
        // copy Strings from list to CharSequence array
        mFileNames = new CharSequence[ list.size()] ;
        Iterator<String> iterator = list.iterator(); 
        int position = 0;
        // build program list
        while( iterator.hasNext() ) {
            mFileNames[ position++ ] = iterator.next();
        }         
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( title );
        builder.setItems( mFileNames, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) {
            	String name = (String) mFileNames[ item ];
                startCmdFile( name );
            }
        });
        builder.create().show();        
	}

	/**
	 * startCmdFile ( overwrite )
	 * @param String name 
	 */	
	protected void startCmdFile( String name ) {
		// dummy
	}

// utility
	/**
	 * getString From Resources
	 * @param int id
	 * @param String  
	 */	
	protected String getStringFromResources( int id ) {	
		return mResources.getString( id );
	}
 
 	/**
	 * show toast
	 * @param String msg
	 */				
    protected void toast_short( String msg ) {
    	ToastMaster.showShort( this, msg );
    }
   
	/**
	 * show toast
	 * @param int id
	 */				
    protected void toast_short( int id ) {
    	ToastMaster.showShort( this, id );
    }
  	
	/**
	 * sendCmdMassage log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
}
