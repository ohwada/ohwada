package jp.ohwada.android.ntpclientsample2;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ntp.TimeInfo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity 
	implements LoaderManager.LoaderCallbacks<TimeInfo> { 
	
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
	public Loader<TimeInfo> onCreateLoader( int id, Bundle bundle ) {
        NtpLoader loader = new NtpLoader( this );
        loader.forceLoad();
        return loader;
    }

	/*
	 * === onLoadFinished ===
	 */
    @Override
    public void onLoadFinished( Loader<TimeInfo> loader, TimeInfo info ) {
    	if ( info == null ) {
    		mTextView1.setText( "result is null" );
    		return;
    	}
		long offset = - NtpResult.getOffset( info ); 
		float offset_sec = (float)offset / 1000;	
		String offset_str = String.format( "%1$.3f", offset_sec );
		long system = System.currentTimeMillis();
		String str_system = formatTime( system );
		String str_ntp = formatTime( system - offset );
    	String str = "";
    	str += "NTP time " + str_ntp + LF;
		str += "System time " + str_system + LF;
		str += "Offset : " + offset_str + LF + LF; 
		str += NtpResult.getDetail( info );
    	mTextView1.setText( str );
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
	public void onLoaderReset( Loader<TimeInfo> loader ) {
		// dummy		
	}

}
