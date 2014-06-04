package jp.ohwada.android.satellitetracking1;

import android.util.Log;

/**
 * Spherical coordinates
 */
public class Spherical {

	private final static boolean D = true;
	private final static String TAG = "Spherical";
	
	private static final double TWO_PI = 2 * Math.PI;
	private final static double HALF_PI = Math.PI / 2;

    /** 
	 * === constructor ===
	 */	
	public  Spherical() {
		// dummy
	}

    /** 
	 * calcX
	 */	    
    static public double calcX( double radius, double  theta, double phi ) {
		double x = radius * Math.sin( theta ) * Math.cos( phi );
		return x;
	}

    /** 
	 * calcY
	 */	
	static public double calcY( double radius, double theta, double phi ) {
		double y = radius * Math.sin( theta ) * Math.sin( phi );
		return y;
	}

    /** 
	 * calcZ
	 */	
	static public double calcZ( double radius, double theta ) {	
		double z = radius * Math.cos( theta );
		return z;
	}
			
	/** 
	 * calcRadius
	 */	
	static public double calcRadius( double x, double y, double z ) {	
		double r = Math.sqrt( x*x + y*y + z*z );
		return r;
	}

    /** 
	 * calcTheta
	 */	
	static public double calcTheta( double x, double y, double z ) {	
		double t = Math.acos( z / calcRadius( x, y, z ) );
		return t;
	}

    /** 
	 * calcPhi
	 */			
	static public double calcPhi( double x, double y ) {	
		// 0 ... pi
		double p = Math.acos( x / Math.sqrt( x*x + y*y ) );
		// 0 ... 2 * pi
		if ( y < 0 && p > 0 ) {
			p = TWO_PI - p;
		}
		return adjustPhi( p );
	}

    /** 
     * adjustPhi
     */	  
	static public double adjustPhi( double rad ) {
		// 0 ... 2 * pi
		double p = rad % TWO_PI ;
		if ( p < 0 ) {
			p += TWO_PI;		
		}
		if ( p > TWO_PI ) {
			p -= TWO_PI;		
		}
		return p;
	}

    /** 
     * adjustLatitude
     */	  
	static public double adjustLatitude( double rad ) {
		double t =  rad - HALF_PI ;
		return t;
	}

    /** 
     * adjustLongitude
     */	
	static public double adjustLongitude( double rad ) {
		// - pi ... pi
		double p = rad ;
		if ( rad > Math.PI ) {
			p = rad - TWO_PI;
		} 
		return p;
	}

	/** 
	 * rotateX
	 */	
	static public double[] rotateX( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		ret[0] = x;
		ret[1] = calcRotateX( y, z, rad );
		ret[2] = calcRotateY( y, z, rad );
		return ret;
	}

	/** 
	 * rotateY
	 */	
	static public double[] rotateY( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		ret[0] = calcRotateY( z, x, rad );
		ret[1] = y; 
		ret[2] = calcRotateX( z, x, rad );
		return ret;
	}

	/** 
	 * rotateZ
	 */	
	static public double[] rotateZ( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		ret[0] = calcRotateX( x, y, rad );
		ret[1] = calcRotateY( x, y, rad );
		ret[2] = z; 
		return ret;
	}	

	/** 
	 * calcRotateX
	 */	
	static private double calcRotateX( double x, double y, double rad ) {
		double ret = x * Math.cos( rad ) + y * Math.sin( rad );
		return ret;
	}

	/** 
	 * calcRotateY
	 */	
	static private double calcRotateY( double x, double y, double rad ) {
		double ret = - x * Math.sin( rad ) + y * Math.cos( rad );
		return ret;
	}

	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}
			
}
