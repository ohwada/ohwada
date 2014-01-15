package jp.ohwada.android.evy1sample7;

import jp.ohwada.android.evy1.Evy1Manager;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// 歌詞の初期値
	private static final String LYLIC_DEFAULT = 
		"どれみふぁそらしど どしたらよかんべ";

	// object
	private Evy1Manager mEvy1Manager;
	
	// UI 
    private KeyboardVocalView mKeyboardView1;
    private KeyboardView mKeyboardView2;
    private PercussionView mPercussionView;
    private TextView mTextViewTitle;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* Customizing the title bar */
        requestWindowFeature( Window.FEATURE_CUSTOM_TITLE );

		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_main, null );  
        setContentView( view );

		/* set the layout on the title bar */
        getWindow().setFeatureInt( 
        	Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar );
 
         // Set up the custom title
        TextView tvTitleLeft = (TextView) findViewById( R.id.TextView_title_left ) ;
        tvTitleLeft.setText( R.string.app_name );   
        mTextViewTitle = (TextView) findViewById( R.id.TextView_title_right );
		
		mKeyboardView1 = new KeyboardVocalView( 
			this, 1, MidiSoundConstants.INST_ACOUSTIC_PIANO );
		mKeyboardView1.initView( view );
		mKeyboardView1.setLylic( LYLIC_DEFAULT );
		mKeyboardView1.setOnChangedListener( 
			new KeyboardView.OnChangedListener() {
			@Override
			public void onTouch( View view, MotionEvent event ) {
				execTouchKey1( view, event );
			}
		});

		mKeyboardView2 = new KeyboardView( 
			this, 2, MidiSoundConstants.INST_STRING_ENSEMBLE_1 );
		mKeyboardView2.initView( view );
		mKeyboardView2.setOnChangedListener( 
			new KeyboardView.OnChangedListener() {
			@Override
			public void onTouch( View view, MotionEvent event ) {
				execTouchKey2( view, event );
			}
		});
		
		mPercussionView = new PercussionView( this );
		mPercussionView.initView( view );
		mPercussionView.setOnChangedListener( 
			new PercussionView.OnChangedListener() {
			@Override
			public void onTouch( View view, MotionEvent event ) {
				execTouchPerc( view, event );
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
	 * キーボードを押したときの処理
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchKey1( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
		if ( output == null ) return;
		mKeyboardView1.execTouch( output, view, event );
	}

	/**
	 * キーボードを押したときの処理
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchKey2( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
		if ( output == null ) return;
		mKeyboardView2.execTouch( output, view, event );
	}
										
	/**
	 * パーカッションを押したときの処理
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouchPerc( View view, MotionEvent event ) {
		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();
		if ( output == null ) return;
		mPercussionView.execTouch( output, view, event );
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
