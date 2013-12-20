package jp.ohwada.android.evy1sample3;

import java.util.List;

import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * MainActivity
 */
public class MainActivity extends AbstractSingleMidiActivity {

	// debug
	private final static boolean D = true;
	private final static String TAG = "MainActivity";

	// Parse Timer
	private static final int MSG_WHAT_PARSE = 1;
    private static final int PARSE_DELAY = 500;  // 0.5 sec

	// MIDIファイルを解析をするクラス    
    private ParserAsyncTask mAsync;

   	// MIDIメッセージを演奏するクラス
   	private MidiPlayer mMidiPlayer = null;
   	
   	// UI
	private ArrayAdapter<String> mAdapter;
 
	// Parse Timer
    private boolean isParseStart = false;
    private boolean isParseRunning = false;
       		
	/*
	 * === onCreate ===
	 */
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		Button btnStart1 = (Button) findViewById(R.id.Button_start_1);
		btnStart1.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				execStart( "furusato.mid" );
			}
		});

		Button btnStart2 = (Button) findViewById(R.id.Button_start_2);
		btnStart2.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				execStart( "We_wish_you_a_Merry_Christmas.mid" );
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
		mAdapter = new ArrayAdapter<String>( this, R.layout.report, R.id.TextView_report );
		lv.setAdapter( mAdapter );
  		
		LogFile.mkdir();
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
	public void onDeviceAttached( final UsbDevice device ) {
		toast_show( "USB MIDI Device " + device.getDeviceName() + " has been attached." );
	}

	/*
	 * === onDeviceDetached ===
	 */
	@Override
	public void onDeviceDetached( final UsbDevice device ) {
		toast_show(  "USB MIDI Device " + device.getDeviceName() + " has been detached." );
	}

    /**
	 * execStart
	 */     
    private void execStart( String filename ) {
		mAdapter.clear();
		// ファイルを読み込む
		addMessage( "File Read" );
		log_d( "File Read" );	
		FileBinary file = new FileBinary( this );
		byte[] bytes = file.readAssetBinaryFile( filename );
		if ( bytes == null ) {
			toast_show( "File not read" );
			return;
		}
		addMessage( "Parse" );
		log_d( "Parse" );	
		// 実行中であれば、中断する
		if ( mAsync != null ) {
        	mAsync.cancel( true );
        	mAsync = null;
        }
		mAsync = new ParserAsyncTask( this );
		mAsync.setBytes( bytes );
		mAsync.execute();
		startParseTimer();
	}

    /**
	 * execStop
	 */     
    private void execStop() {
    	addMessage( "Stop" );
    	log_d( "Stop" );    	
    	stopParseTimer();
    	if ( mAsync != null ) {
        	mAsync.cancel( true );
        }
        if ( mMidiPlayer != null ) {
        	mMidiPlayer.stop();
        }
    }

	/*
	 * MIDI メッセージを送信する
	 * @param MidiMessage mes
	 */ 
    private void sendMidiMessage( MidiMessage mes ) {
    	if ( mes == null ) return;
    	int track = mes.track ;
    	int status = mes.status ;
    	byte[] bytes = mes.bytes ;
    	showMessage( track, bytes );
    	
    	MidiOutputDevice midiOutputDevice = getMidiOutputDevice();
		if ( midiOutputDevice == null ) {
			return;
		}
		
		// MidiOutputDevice の引数が byte でなく int であるため、変換する
    	int len = bytes.length;
    	int[] int_bytes = new int[ 256 ];
    	for ( int i=0; i<256; i++ ){
			int_bytes[ i ] = 0;
		} 
        for ( int i=0; i<len; i++ ){
			int_bytes[ i ] = bytes[ i ] & 0x00ff;
		} 

		int cable = 0;
    	int byte0 = int_bytes[0];
    	int byte1 = int_bytes[1];
    	int byte2 = int_bytes[2];

    	switch ( status ) {
			case MidiMessage.STATUS_NOTE_OFF:
				midiOutputDevice.sendMidiNoteOff( cable, byte0, byte1, byte2 );
				break;
			case MidiMessage.STATUS_NOTE_ON:
				midiOutputDevice.sendMidiNoteOn( cable, byte0, byte1, byte2 );
				break;
			case MidiMessage.STATUS_POLYPHONIC_AFTERTOUCH:
				midiOutputDevice.sendMidiPolyphonicAftertouch( cable, byte0, byte1, byte2 ) ;
				break;
			case MidiMessage.STATUS_CONTROL_CHANGE:
				midiOutputDevice.sendMidiControlChange( cable, byte0, byte1, byte2 );
				break;
			case MidiMessage.STATUS_PROGRAM_CHANGE:
				midiOutputDevice.sendMidiProgramChange( cable, byte0, byte1 );
				break;
			case MidiMessage.STATUS_CHANNEL_AFTERTOUCH:
				midiOutputDevice.sendMidiChannelAftertouch( cable, byte0, byte1 );
				break;
			case MidiMessage.STATUS_PITCH_WHEEL:
				int amount = (byte1 << 7) + byte2;
				midiOutputDevice.sendMidiPitchWheel( cable, byte0, amount );
				break;
    		case MidiMessage.STATUS_SYSTEM_EXCLUSIVE:
    			midiOutputDevice.sendMidiSystemExclusive( cable, bytes );
    			break;
    	}
    }

	/*
	 * メッセージを表示する
	 * @param int track
	 * @param byte[] bytes
	 */     
    private void showMessage( int track, byte[] bytes ) {
    	String str = track + " : ";
    	for ( byte b: bytes ) {
			str += toHexString( b ) + " ";
		} 
		addMessage( str );
	}

	/*
	 * HEX文字列に変換する
	 * @param byte b
	 * @return String
	 */ 
	private String toHexString( byte b ) {
		int n = b & 0x00ff;
		String str = Integer.toHexString( n );
		if ( n < 0x0010 ) {
			str = "0" + str;
		}
    	return str;
    }

	/**
	 * addMessage
	 * @param String msg
	 */	
	private void addMessage( String msg ) {
		mAdapter.add( msg );
	}

// -- Parse Timer Handler ---
	/**
	 * startParseTimer
	 */    
	 private void startParseTimer() {
		isParseStart = true; 
		updateParseRunning();
	}
	
	/**
	 * stopParseTimer
	 */ 
	 protected void stopParseTimer() {	    
		isParseStart = false;
		updateParseRunning();
	}

	/**
	 * updateParseRunning 
	 */		
    private void updateParseRunning() {
        boolean running = isParseStart;
        if ( running != isParseRunning ) {
            if ( running ) {
            	// start
                parseHandler.sendMessageDelayed( 
                	Message.obtain( parseHandler, MSG_WHAT_PARSE ), 
                	PARSE_DELAY );                         
             } else {
                // stop 
                parseHandler.removeMessages( MSG_WHAT_PARSE );
            }
			isParseRunning = running;
        }
    }

	/**
	 * Parse Timer Handler
	 */	    
    private Handler parseHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isParseRunning ) {
				checkParseStatus();
                sendMessageDelayed( 
                	Message.obtain( this, MSG_WHAT_PARSE ), 
                	PARSE_DELAY );
            }
        }
    };
    	
	/**
	 * 周期的に解析の終了をチェックする
	 */	
    private synchronized void checkParseStatus() { 
    	if ( mAsync == null ) return;
		if ( mAsync.getStatus() != AsyncTask.Status.FINISHED ) return;
		stopParseTimer();

		int ret = mAsync.getRetCode();
		if ( ret == MidiParser.RET_SUCCESS ) {
			playMidi( mAsync.getList(), mAsync.getTimebase() );
		} else {
			toast_show( "Parser Error " + ret );
		}
	}

	/**
	 * MIDIメッセージを演奏する
	 */		
    private void playMidi( List<MidiMessage> list, long timebase ) { 
    	if ( list == null ) {
			toast_show( "Parser Error null" );
			return;
    	}
    	if ( list.size() == 0 ) {
			toast_show( "Parser Error zero" );
			return;
    	}
		mMidiPlayer = new MidiPlayer( list, timebase );
		mMidiPlayer.setOnChangedListener( new MidiPlayer.OnChangedListener() {
			@Override
			public void onChanged( MidiMessage m ) {
				sendMidiMessage( m );				
			}
			@Override
			public void onFinished() {
				addMessage( "Finish" );				
			}
		});
		
    	addMessage( "Play" );
		mMidiPlayer.start();
	}

// -- 未使用の listener ---
	/*
	 * onMidiNoteOff
	 */
	@Override
	public void onMidiNoteOff( final MidiInputDevice sender, int cable, int channel, int note, int velocity ) {
		// dummy
	}

	/*
	 * onMidiNoteOn
	 */
	@Override
	public void onMidiNoteOn( final MidiInputDevice sender, int cable, int channel, int note, int velocity ) {
		// dummy
	}

	/*
	 * onMidiPolyphonicAftertouch
	 */
	@Override
	public void onMidiPolyphonicAftertouch( final MidiInputDevice sender, int cable, int channel, int note, int pressure ) {
		// dummy
	}

	/*
	 * onMidiControlChange
	 */
	@Override
	public void onMidiControlChange( final MidiInputDevice sender, int cable, int channel, int function, int value ) {
		// dummy
	}

	/*
	 * onMidiProgramChange
	 */
	@Override
	public void onMidiProgramChange( final MidiInputDevice sender, int cable, int channel, int program ) {
		// dummy
	}

	/*
	 * onMidiChannelAftertouch
	 */
	@Override
	public void onMidiChannelAftertouch( final MidiInputDevice sender, int cable, int channel, int pressure ) {
		// dummy
	}

	/*
	 * onMidiPitchWheel
	 */
	@Override
	public void onMidiPitchWheel( final MidiInputDevice sender, int cable, int channel, int amount ) {
		// dummy
	}

	/*
	 * onMidiSystemExclusive
	 */
	@Override
	public void onMidiSystemExclusive( final MidiInputDevice sender, int cable, final byte[] systemExclusive ) {
		// dummy
	}

	/*
	 * onMidiSystemCommonMessage
	 */
	@Override
	public void onMidiSystemCommonMessage( final MidiInputDevice sender, int cable, final byte[] bytes ) {
		// dummy
	}

	/*
	 * onMidiSingleByte
	 */
	@Override
	public void onMidiSingleByte( final MidiInputDevice sender, int cable, int byte1 ) {
		// dummy
	}

	/*
	 * onMidiMiscellaneousFunctionCodes
	 */
	@Override
	public void onMidiMiscellaneousFunctionCodes( final MidiInputDevice sender, int cable, int byte1, int byte2, int byte3 ) {
		// dummy
	}

	/*
	 * onMidiCableEvents
	 */
	@Override
	public void onMidiCableEvents( final MidiInputDevice sender, int cable, int byte1, int byte2, int byte3 ) {
		// dummy
	}

// -- debug ---
	/**
	 * toast_show
	 * @param String msg
	 */ 	
	private void toast_show( String msg ) { 
 		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
 	}
 		   	
	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	}
}
