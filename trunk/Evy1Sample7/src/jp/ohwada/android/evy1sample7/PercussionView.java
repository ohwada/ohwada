package jp.ohwada.android.evy1sample7;

import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * PercussionView
 */	
public class PercussionView {

	// MIDI
	private static final int MIDI_CABLE = 0;

	// channel
	private static final int CH_PERCUSSION = MidiSoundConstants.CH_PERCUSSION;  // 9

	// note
	private static final int PERC_KEY_MIN = MidiSoundConstants.PERC_KEY_MIN;
	private static final int PERC_KEY_MAX = MidiSoundConstants.PERC_KEY_MAX;
	private static final int NOTE_VELOCITY = 127;

	// tag
	private static final int TAG_MIN = 0;
	private static final int TAG_MAX = 12;
	private static final int BUTTON_NUM = TAG_MAX + 1;	

	// object
	private Activity mActivity;
	private Context mContext;
	
	// UI
	private Button mButtonPerct;
	private Button[] mButtons = new Button[ BUTTON_NUM ];
	
	private int[] mKeys = new int[ BUTTON_NUM ];	

	// callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onTouch( View view, MotionEvent event );
    }

	/**
	 * === PercussionView ===
	 * @param Activity activity
	 */ 
	public PercussionView( Activity activity ) {
		mActivity = activity;
		mContext = activity;
	}

	/**
	 * initView
	 * @param View view
	 */ 
	public void initView( View view ) {	
		initButton( view, 0, R.id.Button_perc0 );
		initButton( view, 1, R.id.Button_perc1 );
		initButton( view, 2, R.id.Button_perc2 );
		initButton( view, 3, R.id.Button_perc3 );
		initButton( view, 4, R.id.Button_perc4 );
		initButton( view, 5, R.id.Button_perc5 );
		initButton( view, 6, R.id.Button_perc6 );
		initButton( view, 7, R.id.Button_perc7 );

		mButtonPerct = (Button) view.findViewById( R.id.Button_perc_select );
		mButtonPerct.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickPerc( view );
			}
		});	
	}

	/**
	 * initButton
	 * @param int id
	 */    
    private void initButton( View view, int num, int id ) {
    	int key = MidiSoundConstants.PERC_ACOUSTIC_BASS_DRUM + 5 * num;
		String name = MidiSoundConstants.getPercussionName( key );
		Button btn = (Button) view.findViewById( id );
		btn.setText( name );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				notifyTouch( view, event );
				return true;
			}
		});		
		mButtons[ num ] = btn;	
		mKeys[ num ] = key;
    }
 
 	/**
	 * 楽器選択ボタンを押したときの処理
	 * @param View view
	 */ 
	private void execClickPerc( View view ) {
		PercussionDialog dialog = new PercussionDialog( mContext );
		dialog.initView( mKeys );
		dialog.setOnChangedListener( 
			new PercussionDialog.OnChangedListener() {
			@Override
			public void onClicked( int parent, int key ) {
				execClickChild( parent, key );		
			}
		});
		dialog.show();
	}

	/**
	 * 楽器を選択したときの処理
	 * @param int num
	 */ 
	private void execClickChild( int parent, int key ) {
		String name = MidiSoundConstants.getPercussionName( key );
		mButtons[ parent ].setText( name );
		mKeys[ parent ] = key; 
//		toast_show( name );	
	}
				
	/**
	 * ドラムを押したときの処理
	 * @param View view
	 * @param MotionEvent event
	 */
	public void execTouch( UsbMidiOutput output, View view, MotionEvent event ) {
		if ( output == null ) return;
		int note = getNote( view );
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				output.sendNoteOn( 
					MIDI_CABLE, CH_PERCUSSION, note, NOTE_VELOCITY );
				break;
			case MotionEvent.ACTION_UP:
				output.sendNoteOff( 
					MIDI_CABLE, CH_PERCUSSION, note, NOTE_VELOCITY );
				break;
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
		int note = mKeys[ tag ];
		if ( note < PERC_KEY_MIN ) {
			note = PERC_KEY_MIN;
		} else if ( note > PERC_KEY_MAX ) {
			note = PERC_KEY_MAX;
		}
		return note;
	}	

	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
		ToastMaster.makeText( mActivity, msg, Toast.LENGTH_SHORT ).show();
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
}
