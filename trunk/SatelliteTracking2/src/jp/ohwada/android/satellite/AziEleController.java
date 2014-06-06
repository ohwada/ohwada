package jp.ohwada.android.satellite;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * AziEleController 
 */
public class AziEleController extends SatelliteController {

	private final static double DEG_TO_RAD = Math.PI / 180.0;
	
	private final static double GEO_RADIUS = 1.0;

	private Satellite mSatelliteOrigin = null;
	private double mOriginLat = 0;
	private double mOriginLng = 0;	

    /** 
	 * === constructor ===
	 */	
	public AziEleController() {
		TAG_SUB = "AziEleController";
	}

	/**
	 * setOrigin
	 */
	public void setOrigin( double lat_deg, double lng_deg ) {
		mOriginLat = DEG_TO_RAD * lat_deg;
		mOriginLng = DEG_TO_RAD * lng_deg;
		double theta = SatelliteUtil.latToTheta( mOriginLat ); 
		mSatelliteOrigin = createSpherical( GEO_RADIUS, theta, mOriginLng );
	}

	/**
	 * convList
	 */
	public List<AziEle> convList( List<Satellite> list1 ) {
		List<AziEle> list2 = new ArrayList<AziEle>();
		for ( Satellite sat: list1 ) {
			AziEle r = convAziEle( sat );
			list2.add( r );
		}
		return list2;
	}

	/**
	 * calcAzimuthElevation
	 * http://homepage1.nifty.com/aida/jr1huo_calsat32/Calsat32AzEl.htm
	 */
	private AziEle convAziEle( Satellite sat ) {
		Satellite sat1 = moveOrigin( sat, mSatelliteOrigin );  
		double r = calcRadius( sat1 );
		Satellite sat2 = rotateZ( sat1, mOriginLng );
		Satellite sat3 = rotateLat( sat2, mOriginLat );
		AziEle azi = calcAziEle( sat3, r );
		return azi;		
	}

	/**
	 * calcAziEle
	 */
	private AziEle calcAziEle( Satellite sat, double r ) { 
		double[] val = calcAziEle( sat.x, sat.y, sat.z, r );
		AziEle azi = new AziEle( val[0], val[1] );
		return azi;	
	}
		
	/**
	 * calcAziEle
	 */
	private double[] calcAziEle( double x, double y, double z, double r ) { 
		double[] ret = new double[ 2 ];		
		// azimuth
		ret[ 0 ] = adjustPhi( calcPhi( x, y ) + Math.PI );
		// elevation
		double rr = z / r;
		ret[ 1 ] = Math.atan( rr / Math.sqrt( 1 - rr * rr )) ;
		return ret;
	}

	/**
	 * rotateLat
	 */
	private Satellite rotateLat( Satellite sat, double rad ) {
		double[] xyz = rotateLat( sat.x, sat.y, sat.z, rad );
		Satellite ret = new Satellite( xyz );
		return ret;
	}

	/**
	 * rotateLat
	 */	
	private double[] rotateLat( double x, double y, double z, double rad ) {
		double[] ret = new double[ 3 ];
		double sin = Math.sin( rad );
		double cos = Math.cos( rad );	
		ret[ 0 ]  = x * sin - z * cos;
		ret[ 1 ]  = y;
		ret[ 2 ]  =  x * cos + z * sin;
		return ret;
	}
			
}
