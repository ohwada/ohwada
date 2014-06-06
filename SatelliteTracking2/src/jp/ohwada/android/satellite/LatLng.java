package jp.ohwada.android.satellite;

/**
 * LatLng
 */
public class LatLng {

	private double RAD_TO_DEG = 180 / Math.PI;

	public double latitude = 0;
	public double longitude = 0;
	
	public LatLng( Satellite sat ) {
		latitude = SatelliteUtil.thetaToLat( sat.theta );
		longitude = SatelliteUtil.phiToLng( sat.phi );
	}

    /** 
	 * getLatitudeDeg
	 */	
	public double getLatitudeDeg() {
		return RAD_TO_DEG * latitude;	
	}

    /** 
	 * getLongitudeDeg
	 */
	public double getLongitudeDeg() {
		return RAD_TO_DEG * longitude;	
	}
	
	/** 
	 * toString
	 */	
	public String toString() {	
		String s = "latitude=" + latitude + " longitude=" + longitude;
		return s;
	}

}
