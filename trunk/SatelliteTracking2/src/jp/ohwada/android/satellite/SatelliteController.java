package jp.ohwada.android.satellite;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * SatelliteController 
 */
public class SatelliteController {

	private final static boolean D = SatelliteConstant.DEBUG;
	private final static String TAG = SatelliteConstant.TAG;
	protected String TAG_SUB = "SatelliteController";

	private static final double TWO_PI = 2 * Math.PI;

    /** 
	 * === constructor ===
	 */	
	public SatelliteController() {
		// dummy
	}

	/**
	 * rotationList
	 */
	public List<Satellite> rotationList( List<Satellite> list1, double div, double offset ) {
		List<Satellite> list2 = new ArrayList<Satellite>();
		for ( int i = 0; i < list1.size(); i++ ) {
			Satellite sat1 = list1.get( i );
			sat1.phi = adjustPhi( sat1.phi - ( i * div ) - offset );
			Satellite sat2 = convRectangular( sat1 );
			list2.add( sat2 );
		}
		return list2;
	}

	/**
	 * create Rectangular coordinates system
	 */
	public Satellite createSatellite( double x, double y, double z ) {
		Satellite sat = new Satellite( x, y, z );
		return convSpherical( sat );	
	}

	/**
	 * create Spherical coordinates system
	 */
	public Satellite createSpherical( double radius, double theta, double phi ) {
		Satellite sat = new Satellite();
		sat.setSpherical( radius, theta, phi );
		return convRectangular( sat );	
	}

    /** 
	 * convert spherical to rectangular
	 */	
	public Satellite convRectangular( Satellite sat ) {
		double radius = sat.radius;
		double theta = sat.theta;
		double phi = sat.phi;
		double x = radius * Math.sin( theta ) * Math.cos( phi );
		double y = radius * Math.sin( theta ) * Math.sin( phi );
		double z = radius * Math.cos( theta );
		sat.setSatellite( x, y, z );
		return sat;	
	}

	/**
	 * convert rectangular to spherical
	 */	
	public Satellite convSpherical( Satellite sat ) {
		double x = sat.x;
		double y = sat.y;
		double z = sat.z;
		double r = calcRadius( x, y, z );
		double t = calcTheta( z, r );
		double p = calcPhi( x, y );	
		sat.setSpherical( r, t, p );
		return sat;	
	}

	/** 
	 * calcRadius
	 */	
	protected double calcRadius( Satellite sat ) {	
		return calcRadius( sat.x, sat.y, sat.z );
	}
			
	/** 
	 * calcRadius
	 */	
	private double calcRadius( double x, double y, double z ) {	
		double r = Math.sqrt( x*x + y*y + z*z );
		return r;
	}

    /** 
	 * calcTheta
	 */	
	private double calcTheta( double z, double r ) {	
		double t = Math.acos( z / r );
		return t;
	}

    /** 
	 * calcPhi
	 */			
	protected double calcPhi( double x, double y ) {	
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
	protected double adjustPhi( double rad ) {
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
	 * rotateX
	 */	
	public Satellite rotateX( Satellite sat, double rad ) {
		double[] xyz = rotateX( sat.x, sat.y, sat.z, rad );
		Satellite ret = new Satellite( xyz );
		return ret;
	}

	/** 
	 * rotateY
	 */	
	public Satellite rotateY( Satellite sat, double rad ) {
		double[] xyz = rotateY( sat.x, sat.y, sat.z, rad );
		Satellite ret = new Satellite( xyz );
		return ret;
	}

	/** 
	 * rotateZ
	 */	
	public Satellite rotateZ( Satellite sat, double rad ) {
		double[] xyz = rotateZ( sat.x, sat.y, sat.z, rad );
		Satellite ret = new Satellite( xyz );
		return ret;
	}	

	/** 
	 * rotateX
	 */	
	private double[] rotateX( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		double sin = Math.sin( rad );
		double cos = Math.cos( rad );	
		ret[0] = x;
		ret[1] = z * sin + y * cos;
		ret[2] = z * cos - y * sin;
		return ret;
	}

	/** 
	 * rotateY
	 */	
	private double[] rotateY( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		double sin = Math.sin( rad );
		double cos = Math.cos( rad );	
		ret[ 0 ]  = x * cos - z * sin;
		ret[ 1 ]  = y;
		ret[ 2 ]  =  x * sin + z * cos;
		return ret;
	}

	/** 
	 * rotateZ
	 */	
	private double[] rotateZ( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		double sin = Math.sin( rad );
		double cos = Math.cos( rad );	
		ret[0] = y * sin + x * cos;
		ret[1] = y * cos - x * sin;
		ret[2] = z; 
		return ret;
	}	

	/**
	 * moveOrigin
	 */	
	public Satellite moveOrigin( Satellite sat1, Satellite sat0 ) {
		double[] ret = moveOrigin( sat1.x, sat1.y, sat1.z, sat0.x, sat0.y, sat0.z );
		Satellite sat = new Satellite( ret );
		return sat;
	}

	/**
	 * moveOrigin
	 */	
	private double[] moveOrigin( double x1, double y1, double z1, double x0, double y0, double z0 ) { 
		double[] ret = new double[ 3 ];
		ret[0] = x1 - x0;
		ret[1] = y1 - y0;
		ret[2] = z1 - z0;
		return ret;
	}

	/**
	 * log_d
	 */	
	protected void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
			
}
