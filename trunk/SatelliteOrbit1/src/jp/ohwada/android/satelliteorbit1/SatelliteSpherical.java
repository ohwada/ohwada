package jp.ohwada.android.satelliteorbit1;

/**
 * Satellite : Spherical coordinates system
 */
public class SatelliteSpherical {
	public double radius = 0;
	public double theta = 0;
	public double phi = 0;

    /** 
	 * === constructor ===
	 */	
	public SatelliteSpherical( double r, double t, double p ) {
		radius = r;
		theta = t;
		phi = p;
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

}
