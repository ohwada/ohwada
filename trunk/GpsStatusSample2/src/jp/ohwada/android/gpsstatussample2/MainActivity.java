package jp.ohwada.android.gpsstatussample2;

import java.util.HashMap;
import java.util.Iterator;

import jp.ohwada.android.nmea.NmeaFactory;
import jp.ohwada.android.nmea.NmeaManager;

import android.app.Activity;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

	private static final String TAG = "GpsStatus";

	private static final String LF = "\n";

	private NmeaManager mNmeaManager;
	private NmeaFactory mNmeaFactory;
	
	private SatelliteView mSatelliteView;
	private ScrollView mScrollView;
	private TextView mTextViewResult;
	
	private HashMap<String,String> mHash = new HashMap<String,String>();
			
	/**
	 * === onCreate ===
	 */ 	 
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);

        mSatelliteView = (SatelliteView) findViewById( R.id.SatelliteView );
        mScrollView = (ScrollView) findViewById( R.id.ScrollView );
        mTextViewResult = (TextView) findViewById( R.id.TextView1 );

        Button btnRadar = (Button) findViewById( R.id.Button_radar );
		btnRadar.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
            	execRadar();
			}
		});

        Button btnNmea = (Button) findViewById( R.id.Button_nmea );
		btnNmea.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
            	execNmea();
			}
		});
		
		mNmeaManager = new NmeaManager( this );
		mNmeaFactory = new NmeaFactory();
		execRadar();
	}

	/**
	 * === onResume ===
	 */ 	 
 	@Override
	protected void onResume() {
		super.onResume();
		GpsStatus.NmeaListener listener = new GpsStatus.NmeaListener() {
			@Override
			public void onNmeaReceived( long timestamp, String nmea ) {
				execNmea( timestamp, nmea );
			}
    	};
    	mNmeaManager.addNmeaListener( listener );
    	mNmeaManager.requestLocationUpdates();
    }

	/**
	 * === onPause ===
	 */ 	 
 	@Override
	protected void onPause() {
		super.onPause();
    	mNmeaManager.removeNmeaListener();
    	mNmeaManager.removeUpdates();
    }

	/**
	 * execRadar
	 */ 
 	private void execRadar() {
 		mSatelliteView.setVisibility( View.VISIBLE );
 		mScrollView.setVisibility( View.GONE );
 	}

	/**
	 * execNmea
	 */ 
 	private void execNmea() {
 		mSatelliteView.setVisibility( View.GONE );
 		mScrollView.setVisibility( View.VISIBLE  );
 	}
             	    	
	/**
	 * execNmea
	 */     
	private void execNmea( long timestamp, String nmea ) {
		mNmeaFactory.setTimestamp( timestamp );
		mNmeaFactory.parse( nmea );	
		mSatelliteView.setSatellites( mNmeaFactory.getAllSatellites() );

		Pair<String, String> p = mNmeaFactory.getPair();
		mHash.put( p.first, p.second );	
        Iterator<String> iterator = mHash.keySet().iterator();
        String msg = "";        
        while ( iterator.hasNext() ) {
            String key = (String) iterator.next();
            msg += mHash.get( key ) + LF + LF;
        }
		mTextViewResult.setText( msg );
	}

	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		Log.d( TAG, msg );
	}	        
}
