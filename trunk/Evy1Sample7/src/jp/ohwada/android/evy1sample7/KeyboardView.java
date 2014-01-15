package jp.ohwada.android.evy1sample7;

import jp.ohwada.android.midi.MidiMsgConstants;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * KeyboardView
 */	
public class KeyboardView {

	// debug
	private static final boolean D = true;
	private static final String TAG = "Evy1";
	protected String TAG_SUB = "KeyboardView";
	
	// MIDI
	protected static final int MIDI_CABLE = 0;

	// note
	private static final int NOTE_MIN = MidiMsgConstants.NOTE_MIN;
	private static final int NOTE_MAX = MidiMsgConstants.NOTE_MAX;
	protected static final int NOTE_OUTSIDE = -1;
	protected static final int NOTE_VELOCITY = 127;

	// octave	
	private static final int OCTAVE = 12;
	private static final int OCTAVE_MIN = 0;
	private static final int OCTAVE_MAX = 10;
	private static final int OCTAVE_DEFAULT = 5;
	
	// tag
	private static final int TAG_MIN = 0;
	private static final int TAG_MAX = 12;
				
	// object
	private Activity mActivity;
	private Context mContext;
	private Resources mResources;
	
	// UI 
	private Button	mButtonInstrument;
	private Spinner mSpinnerOctave;
		
	// variable
	private String mPackageName = "";
	private int mOctave = OCTAVE_DEFAULT;
	private int mViewNum = 0;
	private int mChannel = 0;
	private int mInstrumentNum = 0;
	private int mPrevInstrumentNum =  -1;

	// callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onTouch( View view, MotionEvent event );
    }

    /**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int num
	 * @param int inst	 
     */
	public KeyboardView( Activity activity, int num, int inst ) {
		log_d( "KeyboardView" );
		mActivity = activity;
		mContext = activity;
		mViewNum = num;
		mChannel = num;
		mInstrumentNum = inst;
		mResources = mContext.getResources();
		mPackageName = mContext.getPackageName();
	}

    /**
	 * initView
	 * @param View view 
     */	
	public void initView( View view ) {	
		
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_c" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_cis" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_d" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_dis" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_e" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_f" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_fis" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_g" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_gis" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_a" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_ais" ));	
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_b" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_upper_c2" ));

		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_c" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_d" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_e" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_f" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_g" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_a" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_b" ));
		initButtonKey( view, getIdentifier( "Button_" + mViewNum + "_lower_c2" ));

		mButtonInstrument = (Button) view.findViewById( 
			getIdentifier( "Button_instrument_" + mViewNum ) );
		mButtonInstrument.setText( getInstrumentName( mInstrumentNum ) );
		mButtonInstrument.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickInstrument( view );
			}
		});	
		
		mSpinnerOctave = (Spinner) view.findViewById( 
			getIdentifier( "Spinner_octave_" + mViewNum ) );
		mSpinnerOctave.setSelection( OCTAVE_DEFAULT );
        mSpinnerOctave.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
            	execOctaveItemSelected( position );
            }
            @Override
            public void onNothingSelected( AdapterView<?> arg0 ) {
            	// dummy
            }
        });
	}

    /**
	 * getIdentifier
	 * @param String name
	 * @return int
     */	
	private int getIdentifier( String name ) {
		return mResources.getIdentifier( name, "id", mPackageName );	
	}
	
    /**
	 * getInstrumentName
	 * @param int num
	 * @return String
     */
	private String getInstrumentName( int num ) {
		if ( num < MidiSoundConstants.INST_KEY_MIN ) return "";
		if ( num > MidiSoundConstants.INST_KEY_MAX ) return "";
		return MidiSoundConstants.INSTRUMENT_NAMES[ num ];
	}

	/**
	 * initButtonKey
	 * @param int id
	 */    
    private void initButtonKey( View view, int id ) {
		Button btn = (Button) view.findViewById( id );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				notifyTouch( view, event );
				return true;
			}
		});	
    }

	/**
	 * 楽器ボタンを押したときの処理
	 * @param View view
	 */ 
	private void execClickInstrument( View view ) {
		InstrumentDialog dialog = new InstrumentDialog( mContext );
		dialog.initExpandableListView( mInstrumentNum );
		dialog.setOnChangedListener( 
			new InstrumentDialog.OnChangedListener() {
			@Override
			public void onClicked( int num ) {
				execClickChild( num );		
			}
		});
		dialog.show();
	}

	/**
	 * 楽器を選択したときの処理
	 * @param int num
	 */ 
	private void execClickChild( int num ) {
		mInstrumentNum = num;
		String name = getInstrumentName( num );
		mButtonInstrument.setText( name );
		toast_show( name );	
	}
			
	/**
	 * キーボードを押したときの処理
	 * @param UsbMidiOutput output
	 * @param View view
	 * @param MotionEvent event
	 */
	public void execTouch( UsbMidiOutput output, View view, MotionEvent event ) {
		if ( output == null ) return;
		execTouchKey( output, view, event );
	}

	/**
	 * キーボードを押したときの処理
	 * @param UsbMidiOutput output
	 * @param View view
	 * @param MotionEvent event
	 */
	protected void execTouchKey( UsbMidiOutput output, View view, MotionEvent event ) {	
		if ( output == null ) return;
		// 楽器が変更されたとき、MIDI機器に送信する
		if ( mInstrumentNum !=  mPrevInstrumentNum ) {
			mPrevInstrumentNum = mInstrumentNum;
			output.sendProgramChange( MIDI_CABLE, mChannel, mInstrumentNum );
		}
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				sendNoteOn( output, mChannel, note ) ;
				break;
			case MotionEvent.ACTION_UP:
				sendNoteOff( output, mChannel, note ) ;
				break;
		}
	}

	/*
	 * sendNoteOn
	 * @param UsbMidiOutput output
	 * @param int ch
	 * @param int note 
	 */
	protected void sendNoteOn( UsbMidiOutput output, int ch, int note ) {
		output.sendNoteOn( 
			MIDI_CABLE, ch, note, NOTE_VELOCITY );	
	}

	/*
	 * sendNoteOff
	 * @param UsbMidiOutput output
	 * @param int ch
	 * @param int note 
	 */	
	protected void sendNoteOff( UsbMidiOutput output, int ch, int note ) {
		output.sendNoteOff( 
			MIDI_CABLE, ch, note, NOTE_VELOCITY );	
	}

	/**
	 * Octave が選択されたときの処理
	 * @param int position
	 */ 
	private void execOctaveItemSelected( int position ) {
		if ( position < OCTAVE_MIN ) {
			position = OCTAVE_MIN;
		} else if ( position > OCTAVE_MAX ) {
			position = OCTAVE_MAX;
		}
		mOctave = position;		
	}
		
	/**
	 * getNote
	 * @param View view
	 * @return int
	 */  
	protected int getNote( View view ) {
		int tag = Integer.parseInt( (String) view.getTag() );
		if ( tag < TAG_MIN ) {
			tag = TAG_MIN;
		} else if ( tag > TAG_MAX ) {
			tag = TAG_MAX;
		}		
		int note = OCTAVE * mOctave + tag; 
		// 範囲外なら音を鳴らさない
		if ( note < NOTE_MIN ) {
			note = NOTE_OUTSIDE;
		} else if ( note > NOTE_MAX ) {
			note = NOTE_OUTSIDE;
		}
		return note;
	}	

// --- OnChangedListener ---
     /**
     * setOnChangedListener
     * @param OnChangedListener listener
     */
    public void setOnChangedListener( OnChangedListener listener ) {
        mListener = listener;
    }
  
	/**
	 * notifyDetached
	 * @param UsbDevice device
	 */
	private void notifyTouch( View view, MotionEvent event ) {
		if ( mListener != null ) {
			mListener.onTouch( view, event );
		}
	}	

	/**
	 * toast_show
	 * @param String msg
	 */
	protected void toast_show( String msg ) {
		ToastMaster.makeText( mActivity, msg, Toast.LENGTH_SHORT ).show();
	}
	
	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );	
	}	
}
