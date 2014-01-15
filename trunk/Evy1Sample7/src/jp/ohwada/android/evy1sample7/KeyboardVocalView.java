package jp.ohwada.android.evy1sample7;

import jp.ohwada.android.evy1.Evy1Manager;
import jp.ohwada.android.evy1.Evy1Phonetic;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * KeyboardVocalView
 */	
public class KeyboardVocalView extends KeyboardView{

	// channel
	private static final int CH_VOCALOID = Evy1Manager.CH_VOCALOID;  // 0
				
	// object
	private Evy1Phonetic mPhonetic;
	
	// UI 
	private EditText mEditTextInput;
	private TextView mTextViewLylic;
		
	// variable
	private boolean isInstrument = true;
	private boolean isVocal = false;
	
	// 歌詞
	private String[] mLyicStrings = null;
	private int mLyicPointer = 0;

    /**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int num
	 * @param int inst	 
     */
	public KeyboardVocalView( Activity activity, int num, int inst ) {
		super( activity, num, inst );
		TAG_SUB = "KeyboardVocalView";
		mPhonetic = new Evy1Phonetic();
	}

    /**
	 * initView
	 * @param View view 
     */	
	public void initView( View view ) {
		super.initView( view );	

		mEditTextInput = (EditText) view.findViewById( R.id.EditText_input );
		
		mTextViewLylic = (TextView) view.findViewById( R.id.TextView_lylic_1 );
		// EditView からフォーカスを外すため、ここにフォーカスが当たるようにする
		mTextViewLylic.setFocusable( true );
        mTextViewLylic.setFocusableInTouchMode( true );  
        		
		Button btn_set = (Button) view.findViewById( R.id.Button_set );
		btn_set.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickSet( view );
			}
		});	

		CheckBox cb_instrument = (CheckBox) view.findViewById( R.id.CheckBox_instrument_1 );
		cb_instrument.setChecked( true );
		cb_instrument.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isInstrument = checkBox.isChecked();
            }
        });
        		
		CheckBox cb_vocal = (CheckBox) view.findViewById( R.id.CheckBox_vocal_1 );
		cb_vocal.setChecked( false );
		cb_vocal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isVocal= checkBox.isChecked();
            }
        });

	}

    /**
	 * setLylic
	 * @param String str 
     */	
	public void setLylic( String str ) {
		mEditTextInput.setText( str );
	}

	/**
	 * Set ボタンを押したときの処理
	 * @param View view
	 */
	private void execClickSet( View view ) {
		// EditView からフォーカスを外すため、TextView にフォーカスを当てる
		mTextViewLylic.requestFocus();
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
	 * @param UsbMidiOutput output
	 * @param View view
	 * @param MotionEvent event
	 */
	public void execTouch( UsbMidiOutput output, View view, MotionEvent event ) {
		if ( output == null ) return;
		if ( isInstrument ) {
			execTouchKey( output, view, event );
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
		if ( isVocal ) {
			String lylic = getNextLylic();
			// 歌詞があれば
			if (( lylic != null )&&( lylic.length() > 0 )) {
				mTextViewLylic.setText( lylic );
				String pa = mPhonetic.getAlphabet( lylic );
				// 該当する PA があれば、設定する
				if ( pa != null ) {
					byte[] bytes = mPhonetic.getSymbols( pa );
					output.sendSystemExclusive( MIDI_CABLE, bytes );
				}
			}
			sendNoteOn( output, CH_VOCALOID, note );
		}
	}

	/**
	 * execTouchKeyUp
	 * @param View view
	 */
	private void execTouchKeyUp( UsbMidiOutput output, View view  ) {
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		if ( isVocal) {
			sendNoteOff( output, CH_VOCALOID, note );		
		}		
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

}
