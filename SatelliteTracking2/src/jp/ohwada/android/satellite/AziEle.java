package jp.ohwada.android.satellite;

/**
 * AziEle
 */
public class AziEle {

	private double RAD_TO_DEG = 180 / Math.PI;

	public double azimuth = 0;
	public double elevation = 0;

    /** 
	 * === constructor ===
	 */	
	public AziEle( double _azimuth, double _elevation ) {
		azimuth = _azimuth;
		elevation = _elevation;
	}

    /** 
	 * getAzimuthDeg
	 */
	public double getAzimuthDeg() {
		return RAD_TO_DEG * azimuth;	
	}

    /** 
	 * getElevationDeg
	 */
	public double getElevationDeg() {
		return RAD_TO_DEG * elevation;	
	}

	/** 
	 * toString
	 */	
	public String toString() {	
		String s = "azimuth=" + azimuth + " elevation=" + elevation;
		return s;
	}
				
}
