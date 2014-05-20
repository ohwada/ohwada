package jp.ohwada.android.nmea;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Nmea Manager
 */ 
public class NmeaManager {

	private static final long MIN_TIME = 1000;	// 1 sec
	private static final float MIN_DISTANCE = 0;
			
	private LocationManager mLocationManager;
	private LocationListener mLocationListener = null;
	private GpsStatus.NmeaListener mNmeaListener = null;

    /** 
	 * === constructor ===
	 */	     
 	public NmeaManager( Context context ) {                          
		mLocationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
	}

    /** 
	 * addNmeaListener
	 */	
 	public void addNmeaListener( GpsStatus.NmeaListener listener ) {	
 		mNmeaListener = listener;
		mLocationManager.addNmeaListener( mNmeaListener );
	}

    /** 
	 * removeNmeaListener
	 */	
 	public void removeNmeaListener() {	
 		if ( mNmeaListener != null ) {
			mLocationManager.removeNmeaListener( mNmeaListener );
		}
	}

    /** 
	 * requestLocationUpdates
	 */		
 	public void requestLocationUpdates() {	
 		mLocationListener = new DummyLocationListener();
		mLocationManager.requestLocationUpdates( 
			LocationManager.GPS_PROVIDER, 
			MIN_TIME, 
			MIN_DISTANCE, 
			mLocationListener );
	}

    /** 
	 * removeUpdates
	 */
 	public void removeUpdates() {		
 		if ( mLocationListener != null ) {
   			mLocationManager.removeUpdates( mLocationListener );
		}
	}	
  	
	/**
	 * class DummyLocationListener
	 */    
	private class DummyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location mLocation) {
			// dummy
		}
		@Override
		public void onProviderDisabled(String provider) {
			// dummy
		}
		@Override
		public void onProviderEnabled(String provider) { 
			// dummy
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { 
			// dummy
		}
	};
    	        
}
