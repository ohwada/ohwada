package jp.ohwada.android.satellitetracking1;

import android.util.Log;

/**
 * Satellite : Spherical coordinates system
 */
public class SatelliteSpherical {

	private final static boolean D = true;
	private final static String TAG = "SatelliteSpherical";
	
	private final static double RAD_TO_DEG = 180.0 / Math.PI;

	public double radius = 0;
	public double theta = 0;
	public double phi = 0;
	public double latitude_rad = 0;
	public double longitude_rad = 0;
	public double latitude_deg = 0;
	public double longitude_deg = 0;

    /** 
	 * === constructor ===
	 */	
	public SatelliteSpherical( double r, double t, double p ) {
		radius = r;
		theta = t;
		phi = p;
	}

    /** 
	 * setPhi
	 */	
	public void setPhi( double rad ) {
		phi = Spherical.adjustPhi( rad );
	}

    /** 
	 * setLatLng
	 */	
	public void setLatLng() {
		latitude_rad = Spherical.adjustLatitude( theta );
		longitude_rad = Spherical.adjustLongitude( phi );
		latitude_deg = RAD_TO_DEG * latitude_rad;
		longitude_deg = RAD_TO_DEG * longitude_rad;
	}

    /** 
	 * getSatellite
	 */	
	public Satellite getSatellite() {
		Satellite sat = new Satellite( 
			Spherical.calcX( radius, theta, phi ),
			Spherical.calcY( radius, theta, phi ),
			Spherical.calcZ( radius, theta ) );
		return sat;	
	}
	
	/** 
	 * toString
	 */	
	public String toString() {	
		String s = "radius=" + radius + " theta=" + theta + " phi=" + phi;
		return s;
	}
	
	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}

}
