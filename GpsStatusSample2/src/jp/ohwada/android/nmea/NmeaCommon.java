package jp.ohwada.android.nmea;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * Nmea Common
 */ 
public class NmeaCommon {

	// debug
	private static final boolean D = true;
	private static final String TAG = "Nmea";

	// char	
	protected static final String LF = "\n";
	
	// regular expression
	private static final String HHMMSS_REGEX = "([0-9][0-9])([0-9][0-9])([0-9][0-9])";
	private static final String LAT_REGEX = "([0-9]+)([0-9][0-9]\\.[0-9]+)";

	public String nmea = "";

    /** 
	 * === constructor ===
	 */	     
 	public NmeaCommon() {
 		// dummy
 	}

    /** 
	 * toStringWithNmea
	 */	
	public String toStringWithNmea() { 
		String str = nmea + toString();
		return str;
	}

    /** 
	 * toString
	 */	
	public String toString() { 
		return "";
	}

    /** 
	 * splitNmea
	 */	  
 	protected String[] splitNmea( String str ) { 
 		nmea = str;		  
 		String[] d = str.split(",");                        
		int last = d.length - 1;
		d[ last ] = removeChecksum( d[ last ] );
		return d;
	}	

    /** 
	 * parseInt
	 */	
 	protected int parseInt( String str ) { 
 	 	if ( !isValid( str ) ) return 0;
 		int ret = 0;
 		try {		  
 			ret = Integer.parseInt( str ); 
 		} catch (Exception e) {
 		 	if (D) e.printStackTrace(); 
 		}	                       
		return ret;
	}	

    /** 
	 * parseInt
	 */	
 	protected int parseInt( String str, int def ) {
 		if ( !isValid( str ) ) return def;
 		return parseInt( str );
	}

    /** 
	 * parseFloat
	 */	
 	protected float parseFloat( String str ) { 
 	 	if ( !isValid( str ) ) return 0;
 		float ret = 0;
 		try {		  
 			ret = Float.parseFloat( str ); 
 		} catch (Exception e) {
 		 	if (D) e.printStackTrace(); 
 		}	                       
		return ret;
	}

    /** 
	 * parseFloat
	 */	
 	protected float parseFloat( String str, float def ) {
 	 	if ( !isValid( str ) ) return def;
 		return parseInt( str );
	}
 
    /** 
	 * parseHhmmss
	 */	
	protected float parseHhmmss( String str ) {
		float sec = 0;
		Pattern p = Pattern.compile( HHMMSS_REGEX );
 		Matcher m = p.matcher( str );
		if (m.find()) {
			String hh = m.group(1);
			String mm = m.group(2);
			String ss = m.group(3);
			String target = hh + mm + ss;
			String msec = "0" + str.replaceAll( target, "" );
			sec = 3600 * parseInt( hh ) + 60 * parseInt( mm ) + parseInt( ss ) + parseFloat( msec );			
 		}
 		return sec;
	}

    /** 
	 * parseDdmmyy
	 */	
	protected long parseDdmmyy( String str ) {
		long time = 0;
		Pattern p = Pattern.compile( HHMMSS_REGEX );
 		Matcher m = p.matcher( str );
		if (m.find()) {
			int dd = parseInt( m.group(1 ));
			int mm = parseInt( m.group(2));
			int yy = 2000 + parseInt( m.group(3));
			Calendar cal = Calendar.getInstance();
			cal.set( Calendar.YEAR, yy );
			cal.set( Calendar.MONTH, mm );
			cal.set( Calendar.DAY_OF_MONTH, dd );
			cal.set( Calendar.HOUR_OF_DAY, 0 );
			cal.set( Calendar.MINUTE, 0 );
			cal.set( Calendar.SECOND, 0 );
			cal.set( Calendar.MILLISECOND, 0 );
			time = cal.getTimeInMillis();			
 		}
 		return time;
	}

    /** 
	 * parseLatLng
	 */	
	protected float parseLatLng( String deg, String dir ) {
		float ret = parseDegree( deg );
		if ( "S".equals( dir ) || "E".equals( dir )) {
			ret = - ret;
		}
 		return ret;
	}

    /** 
	 * parseDegree
	 */	
	protected float parseDegree( String deg ) {
		float ret = 0;
		Pattern p = Pattern.compile( LAT_REGEX );
 		Matcher m = p.matcher( deg );
		if (m.find()) {
  			ret = parseFloat( m.group(1) )
  				+ parseFloat( m.group(2) ) / 60; 			
 		}
 		return ret;
	}

    /** 
	 * removeChecksum
	 */				
	protected String removeChecksum( String str ) {
	 	String[] d = str.split("\\*");    
 		return d[0];
	} 

    /** 
	 * isValid
	 */	
	protected boolean isValid( String str ) {
		if ( "".equals( str ) ) return false;
		return true;
	}

    /** 
	 * log_d
	 */					
	protected void log_d( String msg ) {
	 	if (D) Log.d( TAG, msg );  
	}    	        
}
