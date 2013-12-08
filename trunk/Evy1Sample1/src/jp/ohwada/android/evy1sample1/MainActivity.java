package jp.ohwada.android.evy1sample1;

import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Sample Activity for MIDI Driver library
 * 
 * @author K.Shoji
 */
public class MainActivity extends AbstractSingleMidiActivity {

	// debug
	private final static boolean D = true;
	private final static String TAG = "MainActivity";

	// --- 歌詞とMML ---
	private final static String LYLIC = "か,え,る,の,う,た,が,き,こ,え,て,く,る,よ";
	private final static String MML = "CDEFEDC EFGAGFE";

	// MML
	private final static String CHAR_SPACE =	" ";

	// MIDI
    private static final int MIDI_CABLE = 0;
    private static final int MIDI_CHANNEL = 0;
    private static final int MIDI_VELOCITY = 0x007f;
    	
	// timer 500ms
    private static final int TIMER_WHAT = 1;
    private static final int TIMER_DELAY = 500;

	// object
	private Evy1Mml mEvy1Mml;
	
	// ui
	private TextView mTextViewScale;
	private TextView mTextViewLylic;
	private ArrayAdapter<String> mAdapter;
	
	// midi note 
	private int mMidiNote = 0;
	
	// timer 
    private boolean isTimerRunning = false;
   		
	/*
	 * === onCreate ===
	 */
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mTextViewScale = (TextView) findViewById( R.id.TextView_scale );
		mTextViewLylic = (TextView) findViewById( R.id.TextView_lylic );
		
		Button btnStart = (Button) findViewById(R.id.Button_start);
		btnStart.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				execStart();
			}
		});

		Button btnStop = (Button) findViewById(R.id.Button_stop);
		btnStop.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				execStop();
			}
		});

		ListView lv = (ListView) findViewById( R.id.ListView_report );
		mAdapter = new ArrayAdapter<String>( this, R.layout.midi_event, R.id.TextView_report );
		lv.setAdapter( mAdapter );
 
 		mEvy1Mml = new Evy1Mml();

	}

	/*
	 * === onDestroy ===
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		execStop();
	}

	/*
	 * === onDeviceAttached ===
	 */
	@Override
	public void onDeviceAttached( final UsbDevice usbDevice ) {
		toast_show( "USB MIDI Device " + usbDevice.getDeviceName() + " has been attached." );
	}

	/*
	 * === onDeviceDetached ===
	 */
	@Override
	public void onDeviceDetached( final UsbDevice usbDevice ) {
		toast_show( "USB MIDI Device " + usbDevice.getDeviceName() + " has been detached." );
	}

    /**
	 * execStart
	 */     
    private void execStart() {
    	mTextViewLylic.setText( "" );
    	mAdapter.clear();
		mAdapter.add( "start" );
		mEvy1Mml.setMml( MML );
		mEvy1Mml.setLylic( LYLIC );
		byte[] bytes = mEvy1Mml.getPhoneticBytes();
    	sendMidiSystemExclusive( bytes );
		startTimer();	
	}
    	
    /**
	 * execStop
	 */     
    private void execStop() {
		mAdapter.add( "stop" );
		stopTimer();
    }

// --- Timer Handler ---
     /**
	 * startTimer
	 */	
     public void startTimer() {
        isTimerRunning = true;
        updateTimer();
    }

     /**
	 * stopTimer
	 */	    
    public void stopTimer() {
        isTimerRunning = false;
        updateTimer();
    }

     /**
	 * updateTimer
	 */	
    private void updateTimer() {
    	if ( isTimerRunning ) {
        	porcTimer();
            timerHandler.sendMessageDelayed( Message.obtain( timerHandler, TIMER_WHAT ), TIMER_DELAY );               
		} else {
			timerHandler.removeMessages( TIMER_WHAT );
		}
    }

    /**
	 * timer Handler
	 */	
    private Handler timerHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isTimerRunning ) {
                porcTimer();
                sendMessageDelayed( Message.obtain( this, TIMER_WHAT), TIMER_DELAY );
            }
        }
    };

    /**
	 * 500ms 毎に音を鳴らす
	 */	
    private void porcTimer() {
    	// 次の音階が無くなれば終了する
        if ( ! mEvy1Mml.isNextScale() ) {
    		if ( mMidiNote > 0 ) { 
				sendMidiNoteOff( mMidiNote );
			}	
        	execStop();
        	return;
        }
        // 次の音階を取得する
    	String scale = mEvy1Mml.getNextScale();
    	if (( scale.length() > 0 )&&( !scale.equals( CHAR_SPACE ) )) { 
    		String lylic = mEvy1Mml.getNextLylic();
    		mTextViewScale.setText( scale );
    		mTextViewLylic.setText( lylic );
    	    // 音を鳴らす
    	    mMidiNote = mEvy1Mml.scaleToNote( scale );
    		sendMidiNoteOn( mMidiNote ); 
    	} else if ( mMidiNote > 0 ) { 
			// 無音であれば、音を止める
			sendMidiNoteOff( mMidiNote ); 	
    	}  	
    }

// --- MIDI ---
    /**
	 * System Exclusive
	 * @param byte[] 送信するバイト列
	 */	
    private void sendMidiSystemExclusive( byte[] bytes ) {
    	log_d( "sendMidiSystemExclusive" );
    	addAdapter( "SystemExclusive" );
        MidiOutputDevice device = getMidiOutputDevice();
		if (device == null) {
			return;
		}		
		int cable = 0;
    	device.sendMidiSystemExclusive( cable, bytes );
    }

    /**
	 * NoteOn
	 * @param int 鳴らす音
	 */	
	private void sendMidiNoteOn( int note ) { 
	    log_d( "sendMidiNoteOn " + note  );
	    addAdapter( "NoteOn " + note );
     	MidiOutputDevice device = getMidiOutputDevice();
		if ( device == null ) return;
    	device.sendMidiNoteOn( MIDI_CABLE, MIDI_CHANNEL, note,  MIDI_VELOCITY );
	}

    /**
	 * NoteOff
	 * @param int 止める音
	 */	
	private void sendMidiNoteOff( int note ) { 
	    log_d( "sendMidiNoteOff " + note );
	    addAdapter( "NoteOff " + note );
     	MidiOutputDevice device = getMidiOutputDevice();
		if ( device == null ) return;
    	device.sendMidiNoteOff( MIDI_CABLE, MIDI_CHANNEL, note,  MIDI_VELOCITY );
	}

	/**
	 * addAdapter
	 * @param String msg
	 */	
	private void addAdapter( String msg ) {
		mAdapter.add( msg );
	}
			   
	@Override
	public void onMidiMiscellaneousFunctionCodes(MidiInputDevice sender,
			int cable, int byte1, int byte2, int byte3) {
		// dummy			
	}

	@Override
	public void onMidiCableEvents(MidiInputDevice sender, int cable, int byte1,
			int byte2, int byte3) {
		// dummy			
	}

	@Override
	public void onMidiSystemCommonMessage(MidiInputDevice sender, int cable,
			byte[] bytes) {
		// dummy			
	}

	@Override
	public void onMidiSystemExclusive(MidiInputDevice sender, int cable,
			byte[] systemExclusive) {
		// dummy			
	}

	@Override
	public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel,
			int note, int velocity) {
		// dummy			
	}

	@Override
	public void onMidiNoteOn(MidiInputDevice sender, int cable, int channel,
			int note, int velocity) {
		// dummy			
	}

	@Override
	public void onMidiPolyphonicAftertouch(MidiInputDevice sender, int cable,
			int channel, int note, int pressure) {
		// dummy			
	}

	@Override
	public void onMidiControlChange(MidiInputDevice sender, int cable,
			int channel, int function, int value) {
		// dummy			
	}

	@Override
	public void onMidiProgramChange(MidiInputDevice sender, int cable,
			int channel, int program) {
		// dummy			
	}

	@Override
	public void onMidiChannelAftertouch(MidiInputDevice sender, int cable,
			int channel, int pressure) {
		// dummy			
	}

	@Override
	public void onMidiPitchWheel(MidiInputDevice sender, int cable,
			int channel, int amount) {
		// dummy			
	}

	@Override
	public void onMidiSingleByte(MidiInputDevice sender, int cable, int byte1) {
		// dummy		
	}

	/**
	 * toast_show
	 * @param String msg
	 */	
	private void toast_show( String msg ) { 
 		Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
 	}
 		   	
	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	}
}
