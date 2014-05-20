package jp.ohwada.android.nmea;

/**
 * Nmea GPRMC
 */ 
public class NmeaGprmc extends NmeaCommon {

// http://aprs.gids.nl/nmea/
// Recommended minimum specific GPS/Transit data
//
// $GPRMC
// 1    = UTC of position fix : hhmmss
// 2    = Data status : A = OK, V = warning
// 3    = Latitude of fix
// 4    = N or S
// 5    = Longitude of fix
// 6    = E or W
// 7    = Speed over ground in knots
// 8    = Track made good in degrees True
// 9    = UT date : ddmmyy
// 10   = Magnetic variation degrees (Easterly var. subtracts from true course)
// 11   = E or W
// A	＝ mode；A = alone，D = DGPS，N = none
//
// eg1. $GPRMC,081836,A,3751.65,S,14507.36,E,000.0,360.0,130998,011.3,E*62

	public String id = "";
	public	 float time = 0;
	public	 String status = "";
	public	 float latitude = 0;
	public	 float longitude = 0;
	public	 float speed = 0;
	public	 float track = 0;
	public	 long date = 0;
	public	 float magnetic = 0;
	public String mode = "";
	 
    /** 
	 * === constructor ===
	 */	     
 	public NmeaGprmc( String nmea ) {  
 		String[] d = splitNmea( nmea );                        
		id = d[0];
		time = parseHhmmss( d[1] );
		status = d[2];
		latitude = parseLatLng( d[3], d[4] );
		longitude = parseLatLng( d[5], d[6] );
		speed = parseFloat( d[7] );
		track = parseFloat( d[8] );
		date = parseDdmmyy( d[9] );
		magnetic = parseMagnetic( d[10], d[11] );
		mode = d[12];
	}

    /** 
	 * toString
	 */	
	@Override 
	public String toString() { 
		String str = "id=" + id;
		str += " time=" + time;
		str += " status=" + status;
		str += " latitude=" + latitude;
		str += " longitude=" + longitude;
		str += " speed=" + speed;
		str += " track=" + track;
		str += " date=" + date;
		str += " magnetic=" + magnetic;
		str += " mode=" + mode;
		return str;
	}								

    /** 
	 * parseMagnetic
	 */
	private float parseMagnetic( String deg, String dir ) {
		if ( isValid(deg) && isValid(deg) ) {
 			return parseLatLng( deg, dir );
 		}
 		return -1000;
 	}
 					   	        
}
