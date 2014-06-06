package jp.ohwada.android.satellite;

/**
 * Satellite
 */
public class Satellite {
	
	// rectangular coordinates system
	public double x = 0;
	public double y = 0;
	public double z = 0;
	
	// spherical coordinates system
	public double radius = 0;
	public double theta = 0;
	public double phi = 0;
		
    /** 
	 * === constructor ===
	 */
	public Satellite() {
		// dummy
	}

    /** 
	 * === constructor ===
	 */	
	public Satellite( double x, double y, double z ) {
		setSatellite( x, y, z );
	}

    /** 
	 * === constructor ===
	 */	
	public Satellite( double[] xyz ) {
		setSatellite( xyz[0], xyz[1], xyz[2] );
	}

    /** 
	 * setSatellite
	 */	
	public void setSatellite( double _x, double _y, double _z ) {
		x = _x;
		y = _y;
		z = _z;
	}

    /** 
	 * setSpherical
	 */	
	public void setSpherical( double r, double t, double p ) {
		radius = r;
		theta = t;
		phi = p;
	}

	/** 
	 * toString
	 */	
	public String toString() {	
		String s = toStringRectangular() + " " + toStringSpherical();
		return s;
	}
		
	/** 
	 * toStringRectangular
	 */	
	public String toStringRectangular() {	
		String s = "x=" + x + " y=" + y + " z=" + z;
		return s;
	}

	/** 
	 * toStringSpherical
	 */	
	public String toStringSpherical() {	
		String s = "radius=" + radius + " theta=" + theta + " phi=" + phi;
		return s;
	}
				
}
