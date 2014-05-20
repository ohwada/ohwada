package jp.ohwada.android.nmea;

/**
 * Nmea Satellite
 */ 
public class NmeaSatellite extends NmeaCommon {

	private int mPrn = 0;
	private float mElevation = 0;
	private float mAzimuth = 0;
	private float mSnr = 0;
	private boolean isQz = false;

    /** 
	 * === constructor ===
	 */	     
 	public NmeaSatellite( String p, String e, String a, String s, boolean q ) {
		mPrn = parseInt( p );
		mElevation = parseFloat( e );
		mAzimuth = parseFloat( a );
		mSnr = parseFloat( s );
		isQz = q;
	}

    /** 
	 * isQz
	 */	 
 	public boolean isQz() {  
		return isQz;
	}

    /** 
	 * getPrn
	 */	 
 	public int getPrn() {  
		return mPrn;
	}

    /** 
	 * getElevation
	 */	 
 	public float getElevation() {  
		return mElevation;
	}

    /** 
	 * getAzimuth
	 */	 
 	public float getAzimuth() {  
		return mAzimuth;
	}

    /** 
	 * getSnr
	 */	 
 	public float getSnr() {  
		return mSnr;
	}

    /** 
	 * toString
	 */	
	@Override 
	public String toString() { 
		String str = "mPrn=" + mPrn;
		str += " mElevation=" + mElevation;
		str += " mAzimuth=" + mAzimuth;
		str += " mSnr=" + mSnr;
		return str;
	}  		   	        
}
