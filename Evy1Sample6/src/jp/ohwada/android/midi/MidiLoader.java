package jp.ohwada.android.midi;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/*
 * MIDIファイルを解析するローダー
 */ 
public class MidiLoader extends AsyncTaskLoader<MidiLoaderResult> {
 
	// debug
	private static final String TAG_SUB = "MidiLoader";
	
	// object
 	private MidiParser mParser; 	
 	private MidiMultiToSingle mConverter;	
 	
 	// 解析するバイト列		
 	private byte[] mBytes = null;

	/*
	 * コンストラクタ
	 * @param Context context
	 * @param byte[] bytes
	 */ 
    public MidiLoader( Context context, byte[] bytes ) {
        super( context );
		mParser = new MidiParser( context );
		mConverter = new MidiMultiToSingle();
		mBytes = bytes;
	}

	/*
	 * === loadInBackground ===
	 */ 	 
    @Override
    public MidiLoaderResult loadInBackground() {
    	log_d( "loadInBackground" );
		int code = mParser.parse( mBytes );
		if ( code != MidiParser.RET_SUCCESS ) {
			// 失敗したときは、リターンコードを返送する
			MidiLoaderResult res1 = new MidiLoaderResult( code );
			return res1;	
		}
		mConverter.convert( mParser.getList() ); 
		// 成功したときは、解析結果を返送する
		MidiLoaderResult res2 = new MidiLoaderResult( 
			code, mParser.getTimebase(), mConverter.getList() );
		return res2;
    }

	/**
	 * logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (MidiConstant.D) Log.d( MidiConstant.TAG, TAG_SUB + " " + msg );	
	}
    	
}