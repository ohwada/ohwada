package jp.ohwada.android.nmea;

import java.util.ArrayList;
import java.util.List;

/**
 * Nmea GpgsvLis
 */ 
public class NmeaGpgsvList extends NmeaCommon {
	
	// parameter
	private int mNumGsv = 10;
	private int mNumSatellite = 255;
	private boolean isQz = false;
	
	// timestamp
	public long timestamp = 0;	
		
	// nmea array
	private NmeaGpgsv[] mGsvs = null;	
	private NmeaSatellite[] mSatellites = null;

    /** 
	 * === constructor ===
	 */	
 	public NmeaGpgsvList() {
 		init();
 	}
 	
    /** 
	 * === constructor ===
	 */	
 	public NmeaGpgsvList( int gsv, int satellite, boolean flag ) {
 		mNumGsv = gsv;
 		mNumSatellite = satellite;
 		isQz = flag;
 		init();
 	} 

    /** 
	 * init
	 */	
 	private void init() { 
 		mGsvs = new NmeaGpgsv[ mNumGsv ];			
		mSatellites = new NmeaSatellite[ mNumSatellite ]; 
 	} 
 
    /** 
	 * parse
	 */						
	public void parse( String nmea ) { 
		NmeaGpgsv gsv = new NmeaGpgsv( nmea, isQz );
		log_d( gsv.toStringWithNmea() );

		// when first message	
		if ( gsv.msg_num == 1 ) {
			// clear Gsvs
			for ( int i=0; i < mNumGsv; i++ ) {
				mGsvs[ i ] = null;
			}
			// clear Satellites
			for ( int i=0; i < mNumSatellite; i++ ) {
				mSatellites[ i ] = null;
			}
		}
		
		// set Gsvs
		mGsvs[ gsv.msg_num ] = gsv;

		// set Satellites		
		for ( NmeaSatellite satellite: gsv.satellites ) {
			if ( satellite != null ) {
				int prn = satellite.getPrn();
				if ( prn < mNumSatellite ) {
					mSatellites[ prn ] = satellite;
				} else {
					log_d( "prn " + prn + " >= " + mNumSatellite );
				}
			}
		}
	}

    /** 
	 * getSatellites
	 */	
 	public List<NmeaSatellite> getSatellites() { 
 		List<NmeaSatellite> list = new ArrayList<NmeaSatellite>();
 		for( NmeaSatellite sat : mSatellites ) {
			if ( sat != null ) {
    			list.add( sat );
    		}
		}
		return list;
 	}

    /** 
	 * toStringWithNmea
	 */	
	public String toStringWithNmea() {		
		String str = "";
		for ( int i=0; i < mNumGsv; i++ ) {
			if ( mGsvs[ i ] != null ) {
				str += mGsvs[ i ].toStringWithNmea() + LF + LF;
			}
		}
		return str;
	}
		 		   	        
}
