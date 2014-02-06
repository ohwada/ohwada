package jp.ohwada.android.ntpclientsample1;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class MainActivity extends Activity
	implements LoaderManager.LoaderCallbacks<NtpResult> {
		
	private final static String LF = "\n" ;

	private final static int LOADER_ID = 1;
	private final static Bundle LOADER_BUNDLE = null;

	private final static SimpleDateFormat mFormat = 
		new SimpleDateFormat( "HH:mm:ss SSS" );
					
	private LoaderManager mLoaderManager;
	
	private TextView mTextView1;

	/*
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_main );
		mTextView1 = (TextView) findViewById( R.id.TextView1 ) ;

  		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader( LOADER_ID, LOADER_BUNDLE, this );
	}

	/*
	 * === onDestroy ===
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLoaderManager.destroyLoader( LOADER_ID );
	}

	/*
	 * === onCreateLoader ===
	 */
    @Override
	public Loader<NtpResult> onCreateLoader( int id, Bundle bundle ) {
        NtpLoader loader = new NtpLoader( this );
        loader.forceLoad();
        return loader;
    }

	/*
	 * === onLoadFinished ===
	 */
    @Override
    public void onLoadFinished( Loader<NtpResult> loader, NtpResult data ) {
     	String msg = "";
    	if ( data == null ) {
    		msg = "result is null";
    	} else if ( !data.ret ) {
    		msg = "request time failed";
    	} else {
			long system = System.currentTimeMillis();
			long ntp = data.time + SystemClock.elapsedRealtime() - data.reference; 
			String str_system = formatTime( system );
			String str_ntp = formatTime( ntp );
			long offset = system - ntp ;	
			float offset_sec = (float)offset / 1000;	
			msg = "NTP time " + str_ntp + LF;
			msg += "System time " + str_system + LF;
			msg += "Offset " + String.format( "%1$.3f", offset_sec ) + LF+ LF;
			msg += "Time " + data.time + LF;
			msg += "Reference " + data.reference + LF;
			msg += "RoundTrip " + data.roundtrip + LF;
		}	
		mTextView1.setText( msg );
	
    }

	/*
	 * formatTime
	 * @param long time
	 * @return String
	 */
	private String formatTime( long time ) {
		Date date = new Date( time );
		String str = mFormat.format( date );
		return str;
	}
		
	/*
	 * === onLoaderReset ===
	 */
	@Override
	public void onLoaderReset( Loader<NtpResult> loader ) {
		// dummy	
	}
}
