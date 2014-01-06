package jp.ohwada.android.evy1sample4;

import jp.ohwada.android.evy1.Evy1Manager;
import jp.ohwada.android.evy1.Evy1Phonetic;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// MIDI note
	private static final int MIDI_CABLE = 0;
	private static final int NOTE_VELOCITY = 127;
	private static final int NOTE_OUTSIDE = -1;
	private static final int CH_VOCALOID = Evy1Manager.CH_VOCALOID;  // 0
	private static final int CH_DRUMS = MidiSoundConstants.CH_DRUMS;  // 9
	private static final int CH_PIANO = 1;
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
	private Evy1Manager mEvy1Manager;
	private Evy1Phonetic mPhonetic;
	
	// UI 
	private TextView mTextViewConnect;
	private Spinner mSpinnerChannel;

	// variable
	private boolean isFirst = true;	
	private boolean isVocalChecked = false;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
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

		CheckBox cb_vocal = (CheckBox) findViewById( R.id.CheckBox_vocal );
		cb_vocal.setChecked( false );
		cb_vocal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isVocalChecked = checkBox.isChecked();
            }
        });

		mEvy1Manager = new Evy1Manager( this );
		mEvy1Manager.setOnChangedListener( new UsbMidiManager.OnChangedListener() {
			@Override
			public void onAttached( UsbDevice device ) {
				execAttached( device );
			}
			@Override
        	public void onDetached( UsbDevice device ) {
        		execDetached( device );
        	}
		});

		mPhonetic = new Evy1Phonetic();
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
		mEvy1Manager.open();
		UsbDevice device = mEvy1Manager.findDevice();
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
    	mEvy1Manager.close();
    }

	/**
	 * execTouchKey
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchKey( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
		if ( output == null ) return;
		if ( isFirst ) {
			// Only first time 
			isFirst = false;
			setProgramChange( output );
		}
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				execTouchKeyDown( output, view ); 	
				break;
			case MotionEvent.ACTION_UP:
				execTouchKeyUp( output, view ); 	
				break;
		}
	}

	/**
	 * execTouchKeyDown
	 * @param View view 
	 */
	private void execTouchKeyDown( UsbMidiOutput output, View view ) {
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		int ch = getChannel();
		if ( isVocalCh( ch ) ) {
			String lylic = getLylic( view );
			if ( lylic.length() > 0 ) {
				byte[] bytes = mPhonetic.getPhonetic( mPhonetic.getLylic( lylic ) );
				output.sendSystemExclusive( MIDI_CABLE, bytes );
			}
			output.sendNoteOn( 
				MIDI_CABLE, CH_VOCALOID, note, NOTE_VELOCITY );
		}
		if ( isKeyboardCh( ch ) ) {		    
			output.sendNoteOn( 
				MIDI_CABLE, ch, note, NOTE_VELOCITY );
		}
	}

	/**
	 * execTouchKeyUp
	 * @param View view
	 */
	private void execTouchKeyUp( UsbMidiOutput output, View view  ) {
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		int ch = getChannel();
		if ( isKeyboardCh( ch ) ) {	
			output.sendNoteOff( 
				MIDI_CABLE, ch, note, NOTE_VELOCITY );
		}
		if ( isVocalCh( ch ) ) {	
			output.sendNoteOff( 
				MIDI_CABLE, CH_VOCALOID, note, NOTE_VELOCITY );
		}
	}
										
	/**
	 * execTouchDrum
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchDrum( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
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
	 * isVocalCh
	 * @param int ch 
	 * @return boolean
	 */
	private boolean isVocalCh( int ch ) {	
		if ( isVocalChecked ) return true;
		if ( ch == CH_VOCALOID ) return true;
		return false;
	}

	/**
	 * isKeyboardCh
	 * @param int ch 
	 * @return boolean
	 */
	private boolean isKeyboardCh( int ch ) {	
		if ( ch == CH_OUTSIDE ) return false;
		if ( ch == CH_VOCALOID ) return false;
		return true;
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
	 * getLylic
	 * @param View view
	 * @return byte[]
	 */  
	private String getLylic( View view ) {
		String str = "";
		int id = view.getId();
		if ( id == R.id.ButtonC ) {
			str = "ど";
		} else if ( id == R.id.ButtonD ) {
			str = "れ";	
		} else if ( id == R.id.ButtonE ) {
			str = "み";	
		} else if ( id == R.id.ButtonF ) {
			str = "ふぁ";	
		} else if ( id == R.id.ButtonG ) {
			str = "そ";	
		} else if ( id == R.id.ButtonA ) {
			str = "ら";	
		} else if ( id == R.id.ButtonB ) {
			str = "し";		
		} else if ( id == R.id.ButtonC2 ) {
			str = "ど";						
		} else if ( id == R.id.ButtonCis ) {
			str = "あ";
		} else if ( id == R.id.ButtonDis ) {
			str = "い";
		} else if ( id == R.id.ButtonFis ) {
			str = "う";
		} else if ( id == R.id.ButtonGis ) {
			str = "え";
		} else if ( id == R.id.ButtonAis ) {
			str = "お";
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
