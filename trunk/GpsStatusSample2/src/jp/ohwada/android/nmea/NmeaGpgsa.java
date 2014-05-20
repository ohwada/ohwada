package jp.ohwada.android.nmea;

/**
 * Nmea GPGSA
 */ 
public class NmeaGpgsa extends NmeaCommon {

// http://aprs.gids.nl/nmea/
// GPS DOP and active satellites
//
// $GPGSA
//1    = Mode:
//       M=Manual, forced to operate in 2D or 3D
//       A=Automatic, 3D/2D
//2    = Mode:
//       1=Fix not available
//       2=2D
//       3=3D
//3-14 = IDs of SVs used in position fix (null for unused fields)
//15   = PDOP
//16   = HDOP
//17   = VDOP
//
// eg1. $GPGSA,A,3,,,,,,16,18,,22,24,,,3.6,2.1,2.2*3C
// eg2. $GPGSA,A,3,19,28,14,18,27,22,31,39,,,,,1.7,1.0,1.3*35

	public String id = "";
	public	 String mode1 = "";
	public	 int mode2 = 0;
	public	 int[] ids = new int[ 12 ];
	public	 float pdop = 0;
	public	 float hdop = 0;
	public	 float vdop = 0;
 
    /** 
	 * === constructor ===
	 */	     
 	public NmeaGpgsa( String nmea ) {  
 		String[] d = splitNmea( nmea );                        
		id = d[0];
		mode1 = d[1];
		mode2 = parseInt( d[2] );
		for ( int i = 0; i < 12; i++ ) {
			ids[ i ] = parseInt( d[ i + 3 ], -1 );
		}
		pdop = parseFloat( d[15] );
		hdop = parseFloat( d[16] );
		vdop = parseFloat( d[17] );
	}

    /** 
	 * toString
	 */	
	@Override 
	public String toString() { 
		String str = "id=" + id;
		str += " mode1=" + mode1;
		str += " mode2=" + mode2;
		str += " pdop=" + pdop;
		str += " hdop=" + hdop;
		str += " vdop=" + vdop;
		for ( int i = 0; i < 12; i++ ) {
			str += " id" + i + "=" + ids[ i ];
		}
		return str;
	}								
 		   	        
}
