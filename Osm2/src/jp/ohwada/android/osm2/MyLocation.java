package jp.ohwada.android.osm2;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/*
 * MyLocation
 */		
public class MyLocation {
		
	// constant
    private static final long LOCATION_MIN_TIME = 0L; 
    private static final float LOCATION_MIN_DISTANCE = 0f;
    
	// object
	private LocationListener mLocationListener;
	private LocationManager mLocationManager;

	/**
	 * === constractor ===
	 */
	public MyLocation( Activity activity, LocationListener listener ) {
		mLocationListener = listener;
		mLocationManager = (LocationManager) activity.getSystemService( Activity.LOCATION_SERVICE );
	}

	/*
	 * requestLocationUpdates
	 * @return boolean
	 */
    public boolean requestLocationUpdates() {
        if ( mLocationManager == null ) return false;
		mLocationManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER, 
			LOCATION_MIN_TIME,
			LOCATION_MIN_DISTANCE,
			mLocationListener ); 
		return true;
    }
	
	/*
	 * removeUpdates
	 */
   public void removeUpdates() {	
        if ( mLocationManager != null ) {
            mLocationManager.removeUpdates( mLocationListener );
        }
    }

	/**
	 * get Last Location 
	 * @return Location
	 * <pre>
	 * This is cache value, NOT present location. 
	 * when get a location at A point, and NOT get a location at B point, value is a location of A point.
	 * </pre>
     */
    public Location getLastLocation() {
		if ( mLocationManager == null ) return null;
    	return mLocationManager.getLastKnownLocation( 
    		LocationManager.GPS_PROVIDER );
	}

	/**
	 * get Last Point
	 * @return MyGeoPoint
     */
    public MyGeoPoint getLastPoint() {
    	Location location = getLastLocation();
    	if ( location == null ) return null;
    	MyGeoPoint point = new MyGeoPoint( location );
		return point;	
	}	
}
