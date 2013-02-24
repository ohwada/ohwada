package jp.ohwada.android.osm2;

import android.location.Location;

/**
 * MyGeoPoint
 */
public class MyGeoPoint {
				
	// varibale
	public int lat = 0;
	public int lng = 0;
	public Location location = null;

	/**
	 * === constractor ===
	 */
    public MyGeoPoint() {
    	// dummy
    }

	/**
	 * === constractor ===
	 */
    public MyGeoPoint( int _lat, int _lng ) {
    	lat = _lat;
    	lng = _lng;
    }

	/**
	 * === constractor ===
	 */
//    public MyGeoPoint( double _lat, double _lng ) {
//    	lat = doubleToE6( _lat );
//    	lng = doubleToE6( _lng );
//    }

	/**
	 * === constractor ===
	 */
    public MyGeoPoint( Location loc ) {
    	if ( loc == null ) return;
    	location = loc;
    	lat = doubleToE6( loc.getLatitude() );
    	lng = doubleToE6( loc.getLongitude() );
    }
      
	/**
	 * convert real number to integer
	 * @param double : location( floating point format )
	 * @return int : location( E6 format )
	 */
    private int doubleToE6( double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}
	
}