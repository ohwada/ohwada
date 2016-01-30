package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsMessage;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Report Activity
 */
public class ReportActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "ReportActivity";
			    
	/** view component */
	private TextView mTextViewFirmware;
	private TextView mTextViewBattery;
	private TextView mTextViewDevice;
	private Button mButtonReport;
	
// --- onCreate ---
 	/**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
		log_d( "onCreate" );   
        super.onCreate( savedInstanceState );       
        /* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_report, null );
		setContentView( view ); 		

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_report );
		initButtonBack();
		
		/* view component */
        mTextViewFirmware = (TextView) findViewById( R.id.text_firmware );
        mTextViewBattery = (TextView) findViewById( R.id.text_battery );                
        mTextViewDevice = (TextView) findViewById( R.id.text_device );   
                        
		/* Report button */
        mButtonReport = (Button) findViewById( R.id.button_report );
        mButtonReport.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// first comannd
            	mTextViewFirmware.setText("");
				sendGetFirmwareVersion();
			}
        });

		// first comannd
		mTextViewFirmware.setText("");
		sendGetFirmwareVersion();                
    }

// --- onCreate end --

// --- comand send ---      
	/**
	 * Send Firmware
	 */	
	private void sendGetFirmwareVersion() {	
		byte[] cmd = MindstormsMessage.cmdGetFirmwareVersion();
		sendCmdMassage( cmd ); 
	}

	/**
	 * Send Battery
	 */	
	private void sendGetBatteryLevel() {	
		byte[] cmd = MindstormsMessage.cmdGetBatteryLevel();
		sendCmdMassage( cmd ); 
	}

	/**
	 * Send DeviceInfo
	 */	
	private void sendGetDeviceInfo() {	
		byte[] cmd = MindstormsMessage.cmdGetDeviceInfo();
		sendCmdMassage( cmd ); 
	}
 
 	/**
	 * Send GetInputValues
	 */	
	private void sendGetInputValues( int port ) {	
		byte[] cmd = MindstormsMessage.cmdGetInputValues( port );
		sendCmdMassage( cmd ); 
	}

// --- comand send end ---
	
// --- comand recv ---	
	/**
	 * execRecvCmdMessage ( overwrite )
	 * @param byte[] bytes  
	 */
	 protected void execRecvCmdMessage( byte[] bytes ) {
		int reply = mMindstormsCommand.getReply( bytes );
	 	switch ( reply ) {
			case MindstormsCommand.REPLY_GET_FIRMWARE_VERSION:
				execRecvFirmwareVersion( bytes );	
				break;
			case MindstormsCommand.REPLY_GET_DEVICE_INFO:
				execRecvDeviceInfo( bytes );	
				break;
			case MindstormsCommand.REPLY_GET_BATTERY_LEVEL:
				execRecvBatteryLevel( bytes );	
				break;
			case MindstormsCommand.REPLY_GET_INPUT_VALUES:
				execRecvInputValues( bytes );	
				break;
		}
	}

	/**
	 * receive Firmware 
	 * @param byte[] bytes
	 */
	private void execRecvFirmwareVersion( byte[] bytes ) {
		String msg = getRecvFirmwareVersion( bytes );  
		mTextViewFirmware.setText( msg );	
		// next command
		mTextViewBattery.setText("");
		sendGetBatteryLevel(); 	
	}
	
	/**
	 * receive Battery 
	 * @param byte[] bytes
	 */
	private void execRecvBatteryLevel( byte[] bytes ) {
		MindstormsCommand.RecvMessage recv = mMindstormsCommand.getRecvBatteryLevel( bytes );
		String msg = recv.BatteryLevel_status + " " + recv.BatteryLevel_voltage;
		mTextViewBattery.setText( msg );
		// next command
		mTextViewDevice.setText("");
		sendGetDeviceInfo();	
	}

	/**
	 * receive DeviceInfo 
	 * @param byte[] bytes
	 */
	private void execRecvDeviceInfo( byte[] bytes ) {
		MindstormsCommand.RecvMessage recv = mMindstormsCommand.getRecvDeviceInfo( bytes );
		String msg = "";
		msg += "NTX Name: " + recv.DeviceInfo_name + LF;
		msg += "Bluetooth Address: " + recv.DeviceInfo_address + LF;
		msg += "Bluetooth Signal Strength: " + recv.DeviceInfo_signal + LF;
		msg += "Flash Free Use: " + recv.DeviceInfo_flash + LF;
		mTextViewDevice.setText( msg );	
// for future
//		sendGetInputValues( 0 );
	}

	/**
	 * receive InputValues 
	 * @param byte[] bytes
	 */
	private void execRecvInputValues( byte[] bytes ) {
		int port = bytes[ 3 ];
		if ( port < 3 ) {
			sendGetInputValues( port + 1  );
		}		
	}
			
// --- comand recv end ---

}
