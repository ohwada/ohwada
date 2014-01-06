package jp.ohwada.android.usbmidisample2;

import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
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

	// debug
	private  final static String TAG = "MainActivity";
	private  final static boolean D = true;
	
	// MIDI note
	private static final int MIDI_CABLE = 0;
	private static final int NOTE_VELOCITY = 127;
	private static final int NOTE_OUTSIDE = -1;
	private static final int CH_PIANO = 1;
	private static final int CH_DRUMS = MidiSoundConstants.CH_DRUMS;
	private static final int CH_OUTSIDE = -1;
	private static final int INST_DEFAULT = -1;

	private static final int[] INSTRUMENTS = new int[]{
		INST_DEFAULT, // ch 0
		MidiSoundConstants.INST_ACOUSTIC_PIANO,
		MidiSoundConstants.INST_VIBRAPHONE,
		MidiSoundConstants.INST_DRAWBAR_ORGAN,
		MidiSoundConstants.INST_ACOUSTIC_GUITAR_STEEL,
		MidiSoundConstants.INST_ACOUSTIC_BASS,
		MidiSoundConstants.INST_VIOLIN,
		MidiSoundConstants.INST_STRING_ENSEMBLE_1,
		MidiSoundConstants.INST_TRUMPET,
		INST_DEFAULT, // ch 9 (drums)
		MidiSoundConstants.INST_TENOR_SAX,
		MidiSoundConstants.INST_FLUTE,
		MidiSoundConstants.INST_BANJO,
		MidiSoundConstants.INST_TINKLE_BELL,
		MidiSoundConstants.INST_LEAD_CALLIOPE,
		MidiSoundConstants.INST_PAD_FANTASIA };
					
	// object
	private UsbMidiManager mUsbMidiManager;

	// UI 
	private TextView mTextViewConnect;
	private Spinner mSpinnerChannel;
	
	// variable
	private boolean isFirst = true;	
				
	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		log_d( "onCreate" );
		setContentView( R.layout.activity_main );

		mTextViewConnect = (TextView) findViewById( R.id.TextView_connect );
		mSpinnerChannel = (Spinner) findViewById( R.id.Spinner_channel );
		
		initButtonKey( R.id.ButtonC );
		initButtonKey( R.id.ButtonCis );
		initButtonKey( R.id.ButtonD );
		initButtonKey( R.id.ButtonDis );
		initButtonKey( R.id.ButtonE );
		initButtonKey( R.id.ButtonF );
		initButtonKey( R.id.ButtonFis );
		initButtonKey( R.id.ButtonG );
		initButtonKey( R.id.ButtonGis );
		initButtonKey( R.id.ButtonA );
		initButtonKey( R.id.ButtonAis );	
		initButtonKey( R.id.ButtonB );
		initButtonKey( R.id.ButtonC2 );			

		initButtonDrum( R.id.Button_drum60 );
		initButtonDrum( R.id.Button_drum61 );
		initButtonDrum( R.id.Button_drum62 );
		initButtonDrum( R.id.Button_drum63 );
		initButtonDrum( R.id.Button_drum64 );
		initButtonDrum( R.id.Button_drum65 );
		initButtonDrum( R.id.Button_drum66 );
		initButtonDrum( R.id.Button_drum67 );
		initButtonDrum( R.id.Button_drum68 );
		initButtonDrum( R.id.Button_drum69 );
		initButtonDrum( R.id.Button_drum70 );
		initButtonDrum( R.id.Button_drum71 );
		initButtonDrum( R.id.Button_drum72 );

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
	 * initButtonKey
	 * @param int id
	 */    
    private void initButtonKey( int id ) {
		Button btn = (Button) findViewById( id );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouchKey( view, event );
				return true;
			}
		});	
    }	

	/**
	 * initButtonDrum
	 * @param int id
	 */    
    private void initButtonDrum( int id ) {
		Button btn = (Button) findViewById( id );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouchDrum( view, event );
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
		isFirst = true;		
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
	 * execTouchKey
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchKey( View view, MotionEvent event ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		if ( isFirst ) {
			// Only first time 
			isFirst = false;
			setProgramChange( output );
		}
		int ch = getChannel();
		if ( ch == CH_OUTSIDE ) return;
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				output.sendNoteOn( 
					MIDI_CABLE, ch, note, NOTE_VELOCITY );
				break;
			case MotionEvent.ACTION_UP:
				output.sendNoteOff( 
					MIDI_CABLE, ch, note, NOTE_VELOCITY );
				break;
		}
	}

	/**
	 * execTouchDrum
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchDrum( View view, MotionEvent event ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;		
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				output.sendNoteOn( 
					MIDI_CABLE, CH_DRUMS, note, NOTE_VELOCITY );
				break;
			case MotionEvent.ACTION_UP:
				output.sendNoteOff( 
					MIDI_CABLE, CH_DRUMS, note, NOTE_VELOCITY );
				break;
		}
	}

	/**
	 * setProgramChange
	 * @param UsbMidiOutput output
	 */  
	private void setProgramChange( UsbMidiOutput output ) {
		for ( int ch = 0; ch < 16; ch++ ) {
			int inst = getInstrument( ch );
			if ( inst != INST_DEFAULT ) {
				output.sendProgramChange( MIDI_CABLE, ch, inst );
			}		
		}
	}

	/**
	 * getInstrument
	 * @param int ch
	 * @return int
	 */  
	private int getInstrument( int ch ) {
		if (( ch >= 0 )&&( ch <= 15 )) {
			return INSTRUMENTS[ ch ];
		}
		return INST_DEFAULT;
	}	
			
	/**
	 * getChannel
	 * @return int
	 */  
	private int getChannel() {
		int ch = mSpinnerChannel.getSelectedItemPosition();
		if ( ch == CH_DRUMS ) {
			return CH_PIANO;
		} else if (( ch >= 0 )&&( ch <= 15 )) {
			return ch;
		}
		return CH_OUTSIDE;
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

	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );	
	}		
}
