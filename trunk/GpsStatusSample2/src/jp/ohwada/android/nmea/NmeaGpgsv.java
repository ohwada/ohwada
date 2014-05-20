package jp.ohwada.android.nmea;

/**
 * Nmea GPGSV
 */ 
public class NmeaGpgsv extends NmeaCommon {

// http://aprs.gids.nl/nmea/
// GPS Satellites in view
//
// $GPGSV
// 1    = Total number of messages of this type in this cycle
// 2    = Message number
// 3    = Total number of SVs in view
// 4    = SV PRN number
// 5    = Elevation in degrees, 90 maximum
// 6    = Azimuth, degrees from true north, 000 to 359
// 7    = SNR, 00-99 dB (null when not tracking)
// 8-11 = Information about second SV, same as field 4-7
// 12-15= Information about third SV, same as field 4-7
// 16-19= Information about fourth SV, same as field 4-7
//
// eg1. $GPGSV,1,1,13,02,02,213,,03,-3,000,,11,00,121,,14,13,172,05*67
// eg2. $GPGSV,3,1,11,03,03,111,00,04,15,270,00,06,01,010,00,13,06,292,00*74
//    $GPGSV,3,2,11,14,25,170,00,16,57,208,39,18,67,296,40,19,40,246,00*74
//    $GPGSV,3,3,11,22,42,067,42,24,14,311,43,27,05,244,00,,,,*4D

	private static final int NUM_SATELLITES = 4;
	
	public String id = "";
	public	 int total_in_cycle = 0;
	public	 int msg_num = 0;
	public	 int total_sv = 0;
	public NmeaSatellite[] satellites = new NmeaSatellite[ NUM_SATELLITES ];
 
    /** 
	 * === constructor ===
	 */	     
 	public NmeaGpgsv( String nmea, boolean is_qz ) {  
 		String[] d = splitNmea( nmea );                        
		id = d[0];
		total_in_cycle = parseInt( d[1] );
		msg_num = parseInt( d[2] );
		total_sv = parseInt( d[3] );
		int len = d.length; 
		if ( len >= 7 && isValid( d[4] ) ) {
			satellites[ 0 ] = new NmeaSatellite( d[4], d[5], d[6], d[7], is_qz );
		}
		if ( len >= 11 && isValid( d[8] ) ) {
			satellites[ 1 ] = new NmeaSatellite( d[8], d[9], d[10], d[11], is_qz );
		}
		if ( len >= 15 && isValid( d[12] ) ) {
			satellites[ 2 ] = new NmeaSatellite( d[12], d[13], d[14], d[15], is_qz );
		}
		if ( len >= 19 && isValid( d[16] ) ) {
			satellites[ 3 ] = new NmeaSatellite( d[16], d[17], d[18], d[19], is_qz );
		}
	}

    /** 
	 * toString
	 */	
	@Override 
	public String toString() { 
		String str = "id=" + id;
		str += " total_in_cycle=" + total_in_cycle;
		str += " msg_num=" + msg_num;
		str += " total_sv=" + total_sv;
		for ( int i = 0; i < NUM_SATELLITES; i++ ) {
			if ( satellites[ i ] != null ) {
				str += " s" + i + ": " + satellites[ i ].toString();
			}
		}
		return str;
	} 
  	        
}
