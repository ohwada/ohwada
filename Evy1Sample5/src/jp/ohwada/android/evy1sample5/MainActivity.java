package jp.ohwada.android.evy1sample5;

import jp.ohwada.android.evy1.Evy1Manager;
import jp.ohwada.android.evy1.Evy1Phonetic;
import jp.ohwada.android.midi.MidiMsgConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// MIDI
	private static final int MIDI_CABLE = 0;

	// channel
	private static final int CH_VOCALOID = Evy1Manager.CH_VOCALOID;  // 0
	private static final int CH_PIANO = 1;

	// note
	private static final int NOTE_MIN = MidiMsgConstants.NOTE_MIN;
	private static final int NOTE_MAX = MidiMsgConstants.NOTE_MAX;
	private static final int NOTE_VELOCITY = 127;
	
	// tag
	private static final int TAG_MIN = 0;
	private static final int TAG_MAX = 12;

	// 歌詞の初期値
	private static final String LYLIC_DEFAULT = 
		"どれみふぁそらしど どしたらよかんべ";
				
	// object
	private Evy1Manager mEvy1Manager;
	private Evy1Phonetic mPhonetic;
	
	// UI 
    private TextView mTextViewTitle;
	private EditText mEditTextInput;

	// variable
	private boolean isVocal = true;
	private boolean isPiano = false;
	
	// 歌詞
	private String[] mLyicStrings = null;
	private int mLyicPointer = 0;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* Customizing the title bar */
        requestWindowFeature( Window.FEATURE_CUSTOM_TITLE );
		/* set the layout on the screen */
		setContentView( R.layout.activity_main );
		/* set the layout on the title bar */
        getWindow().setFeatureInt( 
        	Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar );
 
         // Set up the custom title
        TextView tvTitleLeft = (TextView) findViewById( R.id.TextView_title_left ) ;
        tvTitleLeft.setText( R.string.app_name );   
        mTextViewTitle = (TextView) findViewById( R.id.TextView_title_right );
		// EditView からフォーカスを外すため、ここにフォーカスが当たるようにする
        mTextViewTitle.setFocusable( true );
        mTextViewTitle.setFocusableInTouchMode( true );  
        
		mEditTextInput = (EditText) findViewById( R.id.EditText_input );
		mEditTextInput.setText( LYLIC_DEFAULT );
		
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

		Button btn_set = (Button) findViewById( R.id.Button_set );
		btn_set.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickSet( view );
			}
		});	

		CheckBox cb_vocal = (CheckBox) findViewById( R.id.CheckBox_vocal );
		cb_vocal.setChecked( true );
		cb_vocal.setOnClickListener( new View.OnClickListener() {
			@Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isVocal = checkBox.isChecked();
            }
		});

		CheckBox cb_piano = (CheckBox) findViewById( R.id.CheckBox_piano );
		cb_piano.setChecked( false );
		cb_piano.setOnClickListener( new View.OnClickListener() {
			@Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isPiano = checkBox.isChecked();
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
	 * Set ボタンを押したときの処理
	 * @param View view
	 */
	private void execClickSet( View view ) {
		// EditView からフォーカスを外すため、TextView にフォーカスを当てる
		mTextViewTitle.requestFocus();
		String str = mEditTextInput.getText().toString();
		if ( str.length() == 0 ) return;
		String[] strings = mPhonetic.splitText( str );
		if ( mPhonetic.checkLylics( strings ) ) {
			mLyicStrings = strings;
			mLyicPointer = 0;
			toast_show( str );		
		} else {
			String msg = mPhonetic.getInaccurateLylics();
			toast_show( "Inaccurate : " + msg );			
		}
	}
				
	/**
	 * キーボードを押したときの処理
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchKey( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
		if ( output == null ) return;
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
		if ( isVocal ) {
			String lylic = getNextLylic();
			// 歌詞があれば
			if (( lylic != null )&&( lylic.length() > 0 )) {
				toast_show( lylic );
				String pa = mPhonetic.getAlphabet( lylic );
				// 該当する PA があれば、設定する
				if ( pa != null ) {
					byte[] bytes = mPhonetic.getSymbols( pa );
					output.sendSystemExclusive( MIDI_CABLE, bytes );
				}
			}
			output.sendNoteOn( 
				MIDI_CABLE, CH_VOCALOID, note, NOTE_VELOCITY );
		}
		if ( isPiano ) {		    
			output.sendNoteOn( 
				MIDI_CABLE, CH_PIANO, note, NOTE_VELOCITY );
		}
	}

	/**
	 * execTouchKeyUp
	 * @param View view
	 */
	private void execTouchKeyUp( UsbMidiOutput output, View view  ) {
		int note = getNote( view );
		if ( isPiano ) {
			output.sendNoteOff( 
				MIDI_CABLE, CH_PIANO, note, NOTE_VELOCITY );
		}
		if ( isVocal ) {		
			output.sendNoteOff( 
				MIDI_CABLE, CH_VOCALOID, note, NOTE_VELOCITY );
		}		
	}
		
	/**
	 * getNote
	 * @param View view
	 * @return int
	 */  
	private int getNote( View view ) {
		int tag = Integer.parseInt( (String) view.getTag() );
		if ( tag < TAG_MIN ) {
			tag = TAG_MIN;
		} else if ( tag > TAG_MAX ) {
			tag = TAG_MAX;
		}		
		int note = 60 + tag; 
		if ( note < NOTE_MIN ) {
			note = NOTE_MIN;
		} else if ( note > NOTE_MAX ) {
			note = NOTE_MAX;
		}
		return note;
	}	
	
	/**
	 *getNextLylic
	 * @return String
	 */  
	private String getNextLylic() {
		if ( mLyicStrings == null ) return null;
		String str = mLyicStrings[ mLyicPointer ];
		mLyicPointer ++;
		if ( mLyicPointer >= mLyicStrings.length ) {
			mLyicPointer = 0;
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
		mTextViewTitle.setText( R.string.not_connect );
	}

	/**
	 * showNotConnect
	 */	 
	private void showConnected( String name ) {
		mTextViewTitle.setText( "Connected : " + name );
	}

	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
		ToastMaster.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}
		
}
