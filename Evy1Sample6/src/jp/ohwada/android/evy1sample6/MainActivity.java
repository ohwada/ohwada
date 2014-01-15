package jp.ohwada.android.evy1sample6;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.ohwada.android.evy1.Evy1Manager;
import jp.ohwada.android.evy1.Evy1Message;
import jp.ohwada.android.midi.MidiLoader;
import jp.ohwada.android.midi.MidiLoaderResult;
import jp.ohwada.android.midi.MidiLogFile;
import jp.ohwada.android.midi.MidiMessage;
import jp.ohwada.android.midi.MidiMsgConstants;
import jp.ohwada.android.midi.MidiParser;
import jp.ohwada.android.midi.MidiPlayer;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */
public class MainActivity extends Activity
	implements LoaderManager.LoaderCallbacks<MidiLoaderResult> {

	// debug
	private final static String TAG_SUB = "MainActivity";

	private final static int LOADER_ID = 1;
	private final static String LOADER_KEY = "key";	
	private static final int MIDI_CABLE = 0;

   	// MIDIメッセージを解析するクラス	
   	private LoaderManager mLoaderManager;

   	// MIDIメッセージを演奏するクラス
   	private MidiPlayer mMidiPlayer = null;

	// object
	private Evy1Manager mEvy1Manager;
	private FileBinary mFileBinary;
   	
   	// UI
	private TextView mTextViewConnect;
	private Spinner mSpinnerFile;
	private ArrayAdapter<String> mAdapterFile = null;
	private ArrayAdapter<String> mAdapterReport = null;

	// 解析結果
	private List<MidiLoaderResult> mListResult = new ArrayList<MidiLoaderResult>();
	private File[] mMidiFiles = null;
	private int mFilePosition = 0;

	private boolean isFirst = true;

	// 解析中か 
	private boolean isLoaderRunning = false;
	
	// 演奏中か
	private boolean isPlayerRunning = false;

	/*
	 * === onCreate ===
	 */
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mTextViewConnect = (TextView) findViewById( R.id.TextView_connect );	
       	
		Button btnStart = (Button) findViewById( R.id.Button_start );
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
		mAdapterReport = new ArrayAdapter<String>( 
			this, R.layout.report, R.id.TextView_report );
		lv.setAdapter( mAdapterReport );

		mFileBinary = new FileBinary();

        mAdapterFile = new ArrayAdapter<String>(
        	this, android.R.layout.simple_spinner_item );
        mAdapterFile.setDropDownViewResource(
        	android.R.layout.simple_spinner_dropdown_item );
        	
		mSpinnerFile = (Spinner) findViewById( R.id.Spinner_list );
        mSpinnerFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
            	AdapterView<?> parent, View view, int position, long id ) {
                execItemSelected( position );
            }
            @Override
            public void onNothingSelected( AdapterView<?> arg0 ) {
            	// dummy
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

  		mMidiPlayer = new MidiPlayer();
		mMidiPlayer.setOnChangedListener( new MidiPlayer.OnChangedListener() {
			@Override
			public void onChanged( MidiMessage mes ) {
				if ( mes != null ) {
    				byte[] bytes = mes.bytes ;
    				showMessage( mes.track, bytes );
    				sendMessage( mes.status, bytes );
    			}	
			}
			@Override
			public void onFinished() {
			   	isPlayerRunning = false;
				addMessage( "Finish" );				
			}
		});
            	
  		mLoaderManager = getLoaderManager();
		MidiLogFile.mkdir();
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
		setAdapterFile();
    }

	/**
	 * setAdapterFile
	 */
    private void setAdapterFile() {
    	File[] files = mFileBinary.getFiles();
        if ( files == null ) return;
		if ( files.length == 0 ) return; 
		mMidiFiles = files;       
        mAdapterFile.clear();
        for ( File file: files ) { 
        	// spinner に表示するファイル名
        	mAdapterFile.add( file.getName() );
        	// 解析結果のリスト
        	mListResult.add( null );
        }
		mSpinnerFile.setAdapter( mAdapterFile );
	}

	/*
	 * === onDestroy ===
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLoaderManager.destroyLoader( LOADER_ID );
		execStop();
    	mEvy1Manager.close();
	}

	/*
	 * === onCreateLoader ===
	 */
    @Override
	public Loader<MidiLoaderResult> onCreateLoader( int id, Bundle bundle ) {
     	log_d( "onCreateLoader" );
        byte[] bytes = bundle.getByteArray( LOADER_KEY );
        if ( bytes != null ) {
        	isLoaderRunning = true;
            MidiLoader loader = new MidiLoader( this, bytes );
            loader.forceLoad();
            return loader;
        }
        return null;
    }

	/*
	 * === onLoadFinished ===
	 */
    @Override
    public void onLoadFinished( Loader<MidiLoaderResult> loader, MidiLoaderResult data ) {
    	log_d( "onLoadFinished" );
    	// 解析中でなければ、無視する
    	if ( !isLoaderRunning ) return;
    	isLoaderRunning = false;
    	if ( data == null ) {
			toast_show( "Parser Error" );
			return;
    	}
    	int code = data.code;
    	if ( code == MidiParser.RET_SUCCESS ) {
			// 解析結果を設定する。初期状態では null を設定している。
    		mListResult.set( mFilePosition, data );
    		playMidi( data );
		} else {
			toast_show( "Parser Error " + code );
		}	
    }

	/*
	 * === onLoaderReset ===
	 */
	@Override
	public void onLoaderReset( Loader<MidiLoaderResult> loader ) {
		log_d( "onLoaderReset" );	
	}

    /**
	 * ファイルが選択されたときの処理
	 * @param iint position
	 */ 
    private void execItemSelected( int position ) {
    	if ( !checkPosition( position ) ) return;
    	mFilePosition = position;   	
		// アプリを起動したときに、１回は呼ばれる
		if ( isFirst ) {
			isFirst = false;
		} else {
			// ２回目以降を、選択されたファイルを実行する
			execStart();
		}
	}

    /**
	 * checkPosition
	 * @param iint position
	 * @return boolean
	 */
    private boolean checkPosition( int position ) {
    	if ( position < 0 ) return false;
    	if ( mMidiFiles.length < position ) return false; 
    	return true;
    }
    
    /**
	 * Start ボタンを押したときの処理
	 */     
    private void execStart() {
		mAdapterReport.clear();
		// 演奏中であれば、中断する
		if ( isPlayerRunning ) {
			execStop();
		}
		if ( !checkPosition( mFilePosition ) ) return;
		MidiLoaderResult result = mListResult.get( mFilePosition );
		// 解析結果があれば、それを演奏する
		if ( result != null ) {
			playMidi( result );
			return;
		}
		// 解析結果がなければ、ファイルを読み込む
		File file = mMidiFiles[ mFilePosition ];
		if ( file == null ) return;
		readAndParse( file );
	}

    /**
	 * ファイルを読み込み、解析する
	 * @param String filename 
	 */
    private void readAndParse( File file ) {    	
		// ファイルを読み込む
		addMessage( "File Read" );
		log_d( "File Read" );	
		byte[] bytes = mFileBinary.readBinaryFile( file );
		if ( bytes == null ) {
			toast_show( "File not read" );
			return;
		}
		// ローダーを使って解析する
		addMessage( "Parse" );
		log_d( "Parse" );	
		Bundle bundle = new Bundle(1);
		bundle.putByteArray( LOADER_KEY, bytes );
		Loader<MidiLoaderResult> loader = mLoaderManager.getLoader( LOADER_ID );
		if ( loader == null ) {
			// 初回ならば、ローダーを生成する
			mLoaderManager.initLoader( LOADER_ID, bundle, this );
		} else {
			// ２回目以降は、ローダーを再利用する
			mLoaderManager.restartLoader( LOADER_ID, bundle, this );
		}	
	}
		
    /**
	 * Stop ボタンを押したときの処理
	 */     
    private void execStop() {
		mMidiPlayer.stop( true );
    	if ( isPlayerRunning ) {
    		stopEvy1();
    	}
    	isPlayerRunning = false;	
    	addMessage( "Stop" );
    	log_d( "Stop" );
    }

    /**
	 * Evy1 の音を止める
	 */
    private void stopEvy1() {
        Evy1Message evy1 = new Evy1Message();
        // stop Sound
        for ( int i=0; i<16; i++ ) {
        	sendMessageWithShow( 
        		MidiMsgConstants.ST_CONTROL, 
        		evy1.getAllSoundOff( i ) );
        }
	}
	
	/*
	 * MIDI メッセージを送信する
	 * @param int status
	 * @param byte[] bytes 
	 */ 
    private void sendMessageWithShow( int status, byte[] bytes ) { 
        showMessage( bytes );
    	sendMessage( status, bytes );
    }

	/*
	 * MIDI メッセージを送信する
	 * @param int status
	 * @param byte[] bytes 
	 */     
    private void sendMessage( int status, byte[] bytes ) { 
 		UsbMidiOutput output = mEvy1Manager.getUsbMidiOutput();   	
		if ( output == null ) return;
      	switch ( status ) {
			case MidiMsgConstants.ST_NOTE_OFF:
				output.sendNoteOff( MIDI_CABLE, bytes );
				break;
			case MidiMsgConstants.ST_NOTE_ON:
				output.sendNoteOn( MIDI_CABLE, bytes );
				break;
			case MidiMsgConstants.ST_POLYPHONIC:
				output.sendPolyphonicKeyPressure( MIDI_CABLE, bytes ) ;
				break;
			case MidiMsgConstants.ST_CONTROL:
				output.sendControlChange( MIDI_CABLE, bytes );
				break;
			case MidiMsgConstants.ST_PROGRAM:
				output.sendProgramChange( MIDI_CABLE, bytes );
				break;
			case MidiMsgConstants.ST_CHANNEL:
				output.sendChannelPressure( MIDI_CABLE, bytes );
				break;
			case MidiMsgConstants.ST_PITCH:
				output.sendPitchWheelChange( MIDI_CABLE, bytes );
				break;
    		case MidiMsgConstants.ST_SYSEX:
    			output.sendSystemExclusive( MIDI_CABLE, bytes );
    			break;
    	}
    }

	/*
	 * メッセージを表示する
	 * @param int track
	 * @param byte[] bytes
	 */     
    private void showMessage( int track, byte[] bytes ) {
    	String str = track + " : " + toHexString( bytes );
		addMessage( str );
	}

	/*
	 * メッセージを表示する
	 * @param byte[] bytes
	 */   
    private void showMessage( byte[] bytes ) {
    	String str = toHexString( bytes );
		addMessage( str );
	}

	/*
	 * HEX文字列に変換する
	 * @param byte[] bytes
	 * @return String
	 */ 
	private String toHexString( byte[] bytes ) {
    	String str = "";
    	for ( byte b: bytes ) {
			str += toHexString( b ) + " ";
		}
		return str; 
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
		mAdapterReport.add( msg );
	}

	/**
	 * MIDIメッセージを演奏する
	 * @param MidiLoaderResult data
	 */		
    private void playMidi( MidiLoaderResult data ) { 
     	List<MidiMessage> list = data.list;
     	long timebase = data.timebase;
    	if ( list == null ) {
			toast_show( "Parser Error null" );
			return;
    	}
    	if ( list.size() == 0 ) {
			toast_show( "Parser Error zero" );
			return;
    	}
    	isPlayerRunning = true;
    	addMessage( "Play" );
		mMidiPlayer.play( list, timebase );		
	}

	/**
	 * execAttached
	 * @param UsbDevice device
	 */	  
	private void execAttached( UsbDevice device ) {
		String name = device.getDeviceName();
		showConnected( name );
		toast_show( "Attached : " + device.getDeviceName() );
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
 		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
 	}
 		   	
	/**
	 * logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );	
	}

}
