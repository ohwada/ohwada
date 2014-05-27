package jp.ohwada.android.satelliteorbit1;

/**
 * Satellite : Rectangular coordinate system
 */
public class Satellite {

	public double x = 0;
	public double y = 0;
	public double z = 0;

    /** 
	 * === constructor ===
	 */	
	public Satellite( double[] xyz ) {
		x = xyz[0];
		y = xyz[1];
		z = xyz[2];
	}

    /** 
	 * === constructor ===
	 */	
	public Satellite( double _x, double _y, double _z ) {
		x = _x;
		y = _y;
		z = _z;
	}

    /** 
	 * get SatelliteSpherical
	 */	
	public SatelliteSpherical getSpherical() {
		// Spherical coordinates system
		SatelliteSpherical sat = new SatelliteSpherical( 
			Spherical.calcRadius( x, y, z ),
			Spherical.calcTheta( x, y, z ),
			Spherical.calcPhi( x, y ) );
		return sat;	
	}

	/** 
	 * rotate
	 */		
	public Satellite rotate( double rad_x, double rad_y, double rad_z ) {
		Satellite sat1 = this.rotateX( rad_x );
		Satellite sat2 = sat1.rotateY( rad_y );
		Satellite sat3 = sat2.rotateZ( rad_z );
		return sat3;		
	}

	/** 
	 * rotateX
	 */	
	public Satellite rotateX( double rad ) {
		double[] xyz = Spherical.rotateX( x, y, z, rad );
		Satellite sat = new Satellite( xyz );
		return sat;
	}

	/** 
	 * rotateY
	 */	
	public Satellite rotateY( double rad ) {
		double[] xyz = Spherical.rotateY( x, y, z, rad );
		Satellite sat = new Satellite( xyz );
		return sat;
	}

	/** 
	 * rotateZ
	 */	
	public Satellite rotateZ( double rad ) {
		double[] xyz = Spherical.rotateZ( x, y, z, rad );
		Satellite sat = new Satellite( xyz );
		return sat;
	}	

	/** 
	 * toString
	 */	
	public String toString() {	
		String s = "x=" + x + " y=" + y + " z=" + z;
		return s;
	}
			
}
