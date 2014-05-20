package jp.ohwada.android.gpsstatussample1;

import android.app.Activity;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Bundle;
import android.widget.TextView;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

//	private static final String TAG = "GpsStatus";
	
	private static final String LF = "\n";
	
	private SatelliteManager mSatelliteManager;
	private TextView mTextViewResult;
	private SatelliteView mSatelliteView;
 
	/**
	 * === onCreate ===
	 */ 	 
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

        mTextViewResult = (TextView) findViewById( R.id.TextView1 );
        mSatelliteView = (SatelliteView) findViewById( R.id.SatelliteView );

		mSatelliteManager = new SatelliteManager( this );

	}

	/**
	 * === onResume ===
	 */ 	 
 	@Override
	protected void onResume() {
		super.onResume();
		GpsStatus.Listener listener = new GpsStatus.Listener() {
			@Override
			public void onGpsStatusChanged( int event ) {
				execGpsStatus( event );
			}
    	};
    	mSatelliteManager.addGpsStatusListener( listener );
    	mSatelliteManager.requestLocationUpdates();
    }

	/**
	 * === onPause ===
	 */ 	 
 	@Override
	protected void onPause() {
		super.onPause();
    	mSatelliteManager.removeGpsStatusListener();
    	mSatelliteManager.removeUpdates();
    }
 
	/**
	 * execGpsStatus
	 */     
	private void execGpsStatus( int event ) {
		Iterable<GpsSatellite> satellites = mSatelliteManager.getSatellites();    
		String msg = mSatelliteManager.getEvent( event ) + LF;
		msg += mSatelliteManager.getSatelliteMsg( satellites );	
 		mTextViewResult.setText( msg );
 		mSatelliteView.setList( satellites );
	}
	        
}
