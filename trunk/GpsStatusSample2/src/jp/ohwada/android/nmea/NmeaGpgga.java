package jp.ohwada.android.nmea;

/**
 * Nmea GPGGA
 */ 
public class NmeaGpgga extends NmeaCommon {

// http://aprs.gids.nl/nmea/
// Global Positioning System Fix Data
//
// $GPGGA
// Sentence Identifier
// Time	
// Latitude
// Latitude direction	
// Longitude	
// Longitude	 direction
// Fix Quality: 0 = Invalid, 1 = GPS fix, 2 = DGPS fix
// Number of Satellites	
// Horizontal Dilution of Precision (HDOP)	
// Altitude	
// Altitude unit	
// Height of geoid above WGS84 ellipsoid	
// Height unit
// Time since last DGPS update	
// DGPS reference station id	
// Checksum
//
// $GPGGA,170834,4124.8963,N,08151.6838,W,1,05,1.5,280.2,M,34.0,M,,,*75

//	private static final String TIME_REGEX = "([0-9][0-9])([0-9][0-9])([0-9][0-9])";
//	private static final String LAT_REGEX = "([0-9]+)([0-9][0-9]\\.[0-9]+)";
			
	public String id = "";
	public	 float time = 0;
	public	 float latitude = 0;
	public	 float longitude = 0;
	public	 int quality = 0;
	public	 int num_satellites = 0;
	public	 float hdop = 0;
	public	 float altitude = 0;
	public	 float height = 0;
	public	 int time_since = 0;
	public	 int station_id = 0;
			 
    /** 
	 * === constructor ===
	 */	     
 	public NmeaGpgga( String nmea ) {  
 		String[] d = splitNmea( nmea );                        
		id = d[0];
		time = parseHhmmss( d[1] );
		latitude = parseLatLng( d[2], d[3] );
		longitude = parseLatLng( d[4], d[5] );
		quality = parseInt( d[6] );
		num_satellites = parseInt( d[7] );
		hdop = parseFloat( d[8] );
		altitude = parseAltitude( d[9], d[10] );
		height = parseAltitude( d[11], d[12] );
		time_since = parseInt( d[13], -1 );
		station_id = parseInt( d[14], -1 );
	}

    /** 
	 * toString
	 */	
	@Override 	
	public String toString() { 
		String str = " id=" + id;
		str += " time=" + time;
		str += " latitude=" + latitude;
		str += " longitude=" + longitude;
		str += " num_satellites=" + num_satellites;
		str += " hdop=" + hdop;
		str += " altitude=" + altitude;
		str += " height=" + height;
		str += " time_since=" + time_since;		
		str += " station_id=" + station_id;	
		return str;
	}								

    /** 
	 * parseAltitude
	 */	
	private float parseAltitude( String str, String unit ) {
		float alt = 0;
		if ( "M".equals( unit )) {
			alt = parseFloat( str );
		}
 		return alt;
	}
		 		   	        
}
