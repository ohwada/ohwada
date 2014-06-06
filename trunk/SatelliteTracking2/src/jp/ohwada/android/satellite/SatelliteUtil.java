package jp.ohwada.android.satellite;

import android.util.Log;

/**
 * SatelliteUtil
 */
public class SatelliteUtil {

	private final static boolean D = SatelliteConstant.DEBUG;
	private final static String TAG = SatelliteConstant.TAG;
	private final static String TAG_SUB = "SatelliteUtil";

	private static final double TWO_PI = 2 * Math.PI;
	private final static double HALF_PI = Math.PI / 2;

    /** 
	 * === constructor ===
	 */	
	public  SatelliteUtil() {
		// dummy
	}

    /** 
     * Theta to Latitude
	 * @param theta : 0 (Notth) ... pi (South)
	 * @return latitude : - pi (South) ... pi (Notth)
     */	  
	public static double thetaToLat( double theta ) {
		double ret =  HALF_PI - theta ;
		return ret;
	}

    /** 
     * Phi to Longitude
	 * @param phi : 0 ... 2pi
	 * @return longitude :  - pi ... pi
     */	
	public static double phiToLng( double phi ) {
		// rad : 0 ... 2pi
		// - pi ... pi
		double ret = phi ;
		if ( phi > Math.PI ) {
			ret = phi - TWO_PI;
		} 
		return ret;
	}

    /** 
     * Latitude to Theta
	 * @param lat : - pi (South) ... pi (Notth)
	 * @return theta : 0 (Notth) ... pi (South)
     */	  
	public static double latToTheta( double lat ) {
		double ret = HALF_PI - lat;
		return ret;
	}

	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private static void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			
}
