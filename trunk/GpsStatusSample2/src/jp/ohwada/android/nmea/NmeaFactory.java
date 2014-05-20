package jp.ohwada.android.nmea;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

/**
 * Nmea Factory
 */ 
public class NmeaFactory extends NmeaCommon {

	// timestamp
	public long timestamp = 0;	

	// pair 
	Pair<String, String> mPair = null;

	// nmea object
	private NmeaGpgsvList mGpList = null;
	private NmeaGpgsa mGsa = null;	
	private NmeaGpgga mGga = null;
	private NmeaGprmc mRmc = null;

	// Glonass
	private NmeaGpgsa mGnGsa = null;

	// QZ
	private NmeaGpgsvList mQzList = null;
	private NmeaGpgsa mQzGsa = null;

    /** 
	 * === constructor ===
	 */
 	public NmeaFactory() { 
 		mGpList = new NmeaGpgsvList( 10, 255, false );
 		mQzList = new NmeaGpgsvList( 4, 4, true );
 	} 

    /** 
	 * setTimestamp
	 */	
 	public void setTimestamp( long time ) { 
 		timestamp = time;
 	}

    /** 
	 * getSatellites
	 */	
 	public List<NmeaSatellite> getAllSatellites() { 
 		List<NmeaSatellite> list = new ArrayList<NmeaSatellite>();
 		list.addAll( mGpList.getSatellites() );
		list.addAll( mQzList.getSatellites() );
		return list;
 	}
  	
    /** 
	 * parse
	 */	
 	public void parse( String nmea ) { 
		String[] d = splitNmea( nmea );
		String id = d[0];
		String str = "";
		if ( "$GPGSA".equals( id ) ) {
			str = timestamp + LF + parseGsa( nmea );
		} else if ( "$GPGGA".equals( id ) )  {
			str = timestamp + LF + parseGga( nmea );
		} else if ( "$GPGSV".equals( id ) )  {
			str = timestamp + LF + parseGsv( nmea );		
		} else if ( "$GPRMC".equals( id ) )  {
			str = timestamp + LF + parseRmc( nmea );
		} else if ( "$GNGSA".equals( id ) )  {
			str = timestamp + LF + parseGnGsa( nmea );
		} else if ( "$QZGSA".equals( id ) )  {
			str = timestamp + LF + parseQzGsa( nmea );
		} else if ( "$QZGSV".equals( id ) )  {
			str = timestamp + LF + parseQzGsv( nmea );		
		} else if ( "$PGLOR".equals( id ) )  {
			// product information ?
			str = timestamp + LF + nmea;	
		} else {
			str = timestamp + LF + nmea;
		}
		mPair = new Pair<String, String>( id, str );
 	} 

    /** 
	 * getPair
	 */	
 	public Pair<String, String> getPair() { 
 		return mPair;
 	}
 		
    /** 
	 * parseGsa
	 */
	private String parseGsa( String nmea ) { 
		mGsa = new NmeaGpgsa( nmea );
		return mGsa.toStringWithNmea();
	}

    /** 
	 * parseGga
	 */
	private String parseGga( String nmea ) { 
		mGga = new NmeaGpgga( nmea );
		return mGga.toStringWithNmea();
	}

    /** 
	 * parseRmc
	 */
	private String parseRmc( String nmea ) { 
		mRmc = new NmeaGprmc( nmea );
		return mRmc.toStringWithNmea();
	}

    /** 
	 * parseGsv
	 */						
	private String parseGsv( String nmea ) { 
		mGpList.parse( nmea );
		return mGpList.toStringWithNmea();
	}

    /** 
	 * parseGnGsa
	 */
	private String parseGnGsa( String nmea ) { 
		mGnGsa = new NmeaGpgsa( nmea );
		return mGnGsa.toStringWithNmea();
	}

    /** 
	 * parseQzGsa
	 */
	private String parseQzGsa( String nmea ) { 
		mQzGsa = new NmeaGpgsa( nmea );
		return mQzGsa.toStringWithNmea();
	}

    /** 
	 * parseQzGsv
	 */						
	private String parseQzGsv( String nmea ) { 
		mQzList.parse( nmea );
		return mQzList.toStringWithNmea();
	}
		 		   	        
}
