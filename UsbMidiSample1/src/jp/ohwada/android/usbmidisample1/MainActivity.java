package jp.ohwada.android.usbmidisample1;

import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// MIDI note
	private static final int MIDI_CABLE = 0;
	private static final int NOTE_CHANNEL = 0;
	private static final int NOTE_VELOCITY = 127;
	private static final int NOTE_OUTSIDE = -1;

	// object
	private UsbMidiManager mUsbMidiManager;

	// UI 
	private TextView mTextViewConnect;
				
	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mTextViewConnect = (TextView) findViewById( R.id.TextView_connect );
		
		initButton( R.id.ButtonC );
		initButton( R.id.ButtonCis );
		initButton( R.id.ButtonD );
		initButton( R.id.ButtonDis );
		initButton( R.id.ButtonE );
		initButton( R.id.ButtonF );
		initButton( R.id.ButtonFis );
		initButton( R.id.ButtonG );
		initButton( R.id.ButtonGis );
		initButton( R.id.ButtonA );
		initButton( R.id.ButtonAis );	
		initButton( R.id.ButtonB );
		initButton( R.id.ButtonC2 );			

		mUsbMidiManager = new UsbMidiManager( this );
		mUsbMidiManager.setOnChangedListener( new UsbMidiManager.OnChangedListener() {
			@Override
			public void onAttached( UsbDevice device ) {
				execAttached( device );
			}
			@Override
        	public void onDetached( UsbDevice device ) {
        		execDetached( device );
        	}
		});

	}

	/**
	 * initButton
	 * @param int id
	 */    
    private void initButton( int id ) {
		Button btn = (Button) findViewById( id );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouch( view, event );
				return true;
			}
		});	
    }	
		
	/**
	 * === onResume ===
	 */
    @Override
    public void onResume() {
		super.onResume();
		mUsbMidiManager.open();
		UsbDevice device = mUsbMidiManager.findDevice();
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
    	mUsbMidiManager.close();
    }

	/**
	 * execTouch
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouch( View view, MotionEvent event ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				output.sendNoteOn( 
					MIDI_CABLE, NOTE_CHANNEL, note, NOTE_VELOCITY );
				break;
			case MotionEvent.ACTION_UP:
				output.sendNoteOff( 
					MIDI_CABLE, NOTE_CHANNEL, note, NOTE_VELOCITY );
				break;
		}
	}

	/**
	 * getNote
	 * @param View view
	 * @return int
	 */  
	private int getNote( View view ) {
		int note = Integer.parseInt( (String) view.getTag() );
		if (( note >= 60 )&&( note <= 72 )) {
			return note;
		}
		return NOTE_OUTSIDE;
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
