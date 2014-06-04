package jp.ohwada.android.satellitetracking1;

import java.util.Calendar;

import android.util.Log;

/**
 * SiderealTime
 */
public class SiderealTime  {

	private final static boolean D = true;
	private final static String TAG = "SiderealTime";

    /** 
	 * === constractor ===
	 */
	public SiderealTime() {
		// dummy
	}

    /** 
	 * getSiderealTime
	 */
	static public double getSiderealTime( Calendar cal ) {
		return getSiderealTime( 
			cal.get( Calendar.YEAR ),
			cal.get( Calendar.MONTH ) + 1,
			cal.get( Calendar.DAY_OF_MONTH ) );
	}

    /** 
	 * getSiderealTime
	 * http://hoshizora.yokochou.com/calculation/sidereal_time.html
	 */	
	static public double getSiderealTime( int year, int month, int day ) {
		double jd = getJulianDay( year, month, day ) ;
		double t = (jd - 2451545.0) / 36525.0;
		double t2 = t * t;
		double t3 = t2 * t;
		double st = ( 24110.54841 + 8640184.812866 * t + 0.093104 * t2 - 0.0000062 * t3 ) / 86400.0 ; 
 		double ret = st - (int)st;
 		return ret;
	}

    /** 
	 * getJulianDay
	 * http://ja.wikipedia.org/wiki/%E6%81%92%E6%98%9F%E6%99%82
	 */	 
	static public double getJulianDay( int year, int month, int day ) {
		if ( month < 3 ) {
			year --;
			month += 12;
		}
		double jd = 365.25 * year + year / 400.0 - year / 100.0 + ( 30.59 * ( month - 2.0 ) + day + 1721088.5 ) ;
		return jd;
	}
	
	/**
	 * log_d
	 */			
	@SuppressWarnings("unused")
	static private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}
}
