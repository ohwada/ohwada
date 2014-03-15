package jp.ohwada.android.usbcdcsample1;

import java.io.UnsupportedEncodingException;

import jp.ohwada.android.usbcdc.UsbCdcManager;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// debug
	private final static boolean D = true;
	
	// object
	private UsbCdcManager mUsbCdcManager;

	// UI 
	private TextView mTextViewConnect;
	private EditText mEditTextBaudrate;
	private EditText mEditTextSend;
	private ArrayAdapter<String> mAdapter;
	
	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mTextViewConnect = (TextView) findViewById( R.id.TextView_connect );
		mEditTextBaudrate = (EditText) findViewById( R.id.EditText_baudrate);
		mEditTextSend = (EditText) findViewById( R.id.EditText_send );

		Button btnBaudrate = (Button) findViewById( R.id.Button_baudrate );
		btnBaudrate.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execBaudrate();
			}
		});	
						
		Button btnSend = (Button) findViewById( R.id.Button_send );
		btnSend.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execSend();
			}
		});	
		
		ListView lv = (ListView) findViewById( R.id.ListView_report );
		mAdapter = new ArrayAdapter<String>( 
			this, R.layout.report, R.id.TextView_report );
		lv.setAdapter( mAdapter );
		
		mUsbCdcManager = new UsbCdcManager( this );
		mUsbCdcManager.setOnChangedListener( new UsbCdcManager.OnChangedListener() {
			@Override
			public void onAttached( UsbDevice device ) {
				execAttached( device );
			}
			@Override
        	public void onDetached( UsbDevice device ) {
        		execDetached( device );
        	}
		});
		mUsbCdcManager.setOnRecieveListener( new UsbCdcManager.OnRecieveListener() {
			@Override
			public void onRecieve( byte[] bytes ) {
				execRecieve( bytes );
			}
			@Override
			public void onRecieve(String str) {
				// dummy				
			}
		});
	
	}
		
	/**
	 * === onResume ===
	 */
    @Override
    public void onResume() {
		super.onResume();
		mUsbCdcManager.open();
		UsbDevice device = mUsbCdcManager.findDevice();
		if ( device != null ) {
			String name = device.getDeviceName();
			showConnected( name );
			toast_show( "Found : " + name );
		} else {
			showNotConnect();
		}	
    }

	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	mUsbCdcManager.close();
    }

	/**
	 * execBaudrate
	 */
	private void execBaudrate() {
		String str = mEditTextBaudrate.getText().toString();
		if ( str.length() > 0 ) {
			int baudrate = Integer.parseInt( str );
			boolean ret = mUsbCdcManager.setBaudrate( baudrate );
			if ( ret ) {
				toast_show( "Baudrate " + baudrate );
			} else {
				toast_show( "cannot set Baudrate" );
			}
		} else {
			toast_show( "Enter Baudrate" );
		}
	}

	/**
	 * execSend
	 */
	private void execSend() {
		String str = mEditTextSend.getText().toString();
		if ( str.length() > 0 ) {
			mUsbCdcManager.sendBulkTransfer( str.getBytes() );
		} else {
			toast_show( "Enter Text" );
		}
	}

	/**
	 * execRecieve
	 * @param byte[] bytes
	 */	
	private void execRecieve( byte[] bytes ) {
	    String str = bytesToString( bytes );
		if ( str != null ) {
			mAdapter.add( str );
		}
	}

	/**
	 * bytesToString
	 * @param byte[] bytes
	 * @return String
	 */	
	private String bytesToString( byte[] bytes ) {
	    String str = null;
		try {
			str = new String( bytes, "UTF-8" );
		} catch ( UnsupportedEncodingException e ) {
			if (D) e.printStackTrace();
		}
		return str;
	}
  					
	/**
	 * execAttached
	 * @param UsbDevice device
	 */	  
	private void execAttached( UsbDevice device ) {
		String name = device.getDeviceName();
		showConnected( name );
		toast_show( "Attached : " + name );
	}

	/**
	 * execDetached
	 * @param UsbDevice device
	 */	 
	private void execDetached( UsbDevice device ) {
		showNotConnect();
		toast_show( "Detached : " + device.getDeviceName() );  
	}

	/**
	 * showNotConnect
	 */	 
	private void showNotConnect() {
		mTextViewConnect.setText( R.string.not_connect );
	}

	/**
	 * showNotConnect
	 */	 
	private void showConnected( String name ) {
		mTextViewConnect.setText( "Connected : " + name );
	}
				
	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
		Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}
		
}
