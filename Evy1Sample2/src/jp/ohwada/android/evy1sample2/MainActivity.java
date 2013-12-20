package jp.ohwada.android.evy1sample2;

import java.util.List;

import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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

    // Play Timer
    private static final int MSG_WHAT_PLAY = 1;
    private static final int PLAY_DELAY = 100; // 0.1 sec

	// Parse Timer
	private static final int MSG_WHAT_PARSE = 2;
    private static final int PARSE_DELAY = 500;  // 0.5 sec

	// バイト列の解析をするクラス    
    private ParserAsyncTask mAsync;
   	
   	// UI
	private ArrayAdapter<String> mAdapter;

	// ベース時間
	private long mTimebase = 0;
	
	// 解析されたMIDIのリスト
	private List<MidiMessage> mList = null;

    // リストの処理中の位置を示すポインタ
	private int mReadPointer = 0;

	// 演奏時刻
	private long mPlayTime = 0;

	// 演奏の開始時刻
	private long mStartTime = 0;

    // 演奏を行うかどうかのフラグ
    private boolean isPlayLoop = false;  
    
    // Play Timer
    private boolean isPlayStart = false;
    private boolean isPlayRunning = false;
 
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
    private void execStart() {
		mAdapter.clear();
 		// リストがあれば、実行する
    	if ( mList != null ) {
			playMusic();
			return;
		}
		// ファイルを読み込む
		addMessage( "File Read" );
		log_d( "File Read" );	
		MidiFile mf = new MidiFile( this );
		byte[] bytes = mf.readAssetFile( "furusato.txt" );
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
    	stopPlayTimer();
    	stopParseTimer();
    	if ( mAsync != null ) {
        	mAsync.cancel( true );
        }
    }

// --- Play Timer Handler ---
	/*
	 * startPlayTimer
	 */ 
	private void startPlayTimer() {
        isPlayStart = true;
        updatePlayRunning();
    }

	/*
	 * stopPlayTimer
	 */     
    private void stopPlayTimer() {
        isPlayStart = false;
        updatePlayRunning();
    }

	/*
	 * updatePlayRunning
	 */ 
    private void updatePlayRunning() {
        boolean running = isPlayStart;
        if ( running != isPlayRunning ) {
			if ( running ) {
				if ( isPlayLoop ) {
					procPlayLoop();
				}
				// start
				playHandler.sendMessageDelayed(
					Message.obtain( playHandler, MSG_WHAT_PLAY ), 
					PLAY_DELAY );               
			} else {
				// stop
				playHandler.removeMessages( MSG_WHAT_PLAY );
			}
			isPlayRunning = running;
        }
    }

	/*
	 * Play Timer Handler
	 */ 
    private Handler playHandler = new Handler() {
        public void handleMessage(Message m) {
            if ( isPlayRunning ) {
            	if ( isPlayLoop ) {
            		procPlayLoop();
            	}
                sendMessageDelayed(
                	Message.obtain( this, MSG_WHAT_PLAY ), 
                	PLAY_DELAY );
            }
        }
    };

	/*
	 * タイマー起動で、MIDIのリストを処理する
	 */ 
    private synchronized void procPlayLoop() {
		// リストを最後まで処理したときは、タイマーを停止する
    	if ( isListEnd() ) {
			addMessage( "Stop" );
			log_d( "Stop" );	
			stopPlayTimer();
			return;
		}
    	// 演奏時間にならないときは、いったん終了する
    	if ( isWaiting() ) return;
    	isPlayLoop = false;
    	while ( true ) {
    		// リストを最後まで処理したときは、終了する
			if ( isListEnd() ) break;
			// 演奏時間にならないときは、いったん終了する
			if ( isWaiting() ) break;
    		MidiMessage r = mList.get( mReadPointer ); 
    		mReadPointer ++;
			sendMidiMessage( r.status, r.bytes );
			long time = r.time / ( 1000 * mTimebase );
			mPlayTime = mStartTime + time;
		}
		isPlayLoop = true;			
    }

	/*
	 * リストを最後まで処理したかの判定
	 */ 
	private boolean isListEnd() {
    	if ( mReadPointer >= mList.size() ) {
			return true;
		}
		return false;
    }	
    	
	/*
	 * 演奏時間になったかの判定
	 */ 
	private boolean isWaiting() {
		if ( SystemClock.elapsedRealtime() < mPlayTime ) {
			return true;
		}
		return false;
	}

	/*
	 * MIDI メッセージを送信する
	 * @param int code
	 * @param byte[] bytes
	 */ 
    private void sendMidiMessage( int status, byte[] bytes ) {
    	showMessage( bytes );
    	
    	MidiOutputDevice midiOutputDevice = getMidiOutputDevice();
		if ( midiOutputDevice == null ) {
			return;
		}
		
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
    		case 0x0000:
    		case 0x0007:
    			midiOutputDevice.sendMidiSystemExclusive( cable, bytes );
    			break;
			case 0x0080:
				midiOutputDevice.sendMidiNoteOff( cable, byte0, byte1, byte2 );
				break;
			case 0x0090:
				midiOutputDevice.sendMidiNoteOn( cable, byte0, byte1, byte2 );
				break;
			case 0x00A0:
				midiOutputDevice.sendMidiPolyphonicAftertouch( cable, byte0, byte1, byte2 ) ;
				break;
			case 0x00B0:
				midiOutputDevice.sendMidiControlChange( cable, byte0, byte1, byte2 );
				break;
			case 0x00C0:
				midiOutputDevice.sendMidiProgramChange( cable, byte0, byte1 );
				break;
			case 0x00D0:
				midiOutputDevice.sendMidiChannelAftertouch( cable, byte0, byte1 );
				break;
			case 0x00E0:
				int amount = (byte1 << 7) + byte2;
				midiOutputDevice.sendMidiPitchWheel( cable, byte0, amount );
				break;
    	}
    }

	/*
	 * メッセージを表示する
	 * @param int code
	 * @param byte[] bytes
	 */     
    private void showMessage( byte[] bytes ) {
    	String str = "";
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
	 * タイマー起動で、解析の終了をチェックする
	 */	
    private synchronized void checkParseStatus() { 
    	if (( mAsync != null )&&
		    ( mAsync.getStatus() == AsyncTask.Status.FINISHED )) {
			stopParseTimer();
			mList = mAsync.getList();
			mTimebase = mAsync.getTimebase();
			playMusic();
		}
	}

	/**
	 * 演奏を開始する
	 */	
    private void playMusic() { 
    	mAdapter.add( "Play" );
    	mReadPointer = 0;
		mStartTime = SystemClock.elapsedRealtime();
		mPlayTime = mStartTime;
		isPlayLoop = true;
		startPlayTimer();
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
