package jp.ohwada.android.satellitetracking1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import sgp4v.ObjectDecayed;
import sgp4v.SatElset;
import sgp4v.SatElsetException;
import sgp4v.Sgp4Data;
import sgp4v.Sgp4Unit;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * SatelliteData
 */
public class SatelliteData  {

	private final static boolean D = true;
	private final static String TAG = "SatelliteData";

	private Context mContext;
	private SatElset mSatElset;

    /** 
	 * === constractor ===
	 */
	public SatelliteData( Context context ) {
		mContext = context;
	}

	/**
	 * getCalendarNow
	 * @return : today x0m 00s
	 */
	public Calendar getCalendarNow( int minute_delta ) {
		Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) );
		cal.setTimeInMillis( System.currentTimeMillis() );
		int min = minute_delta * ( cal.get( Calendar.MINUTE ) / minute_delta ) ;
		cal.set( Calendar.MINUTE, min );						
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );
		return cal;
	}

    /** 
	 * initSatElset
	 */    
    public void initSatElset() {
		List<String> list = readAssetsFile( "qzs1.txt" );
		mSatElset = getSatElset( list.get(1), list.get(2) );
	}

    /** 
	 * getSatelliteList
	 */    
    public List<Satellite> getSatelliteList( int num, Calendar cal ) {
    	int year = getYear( cal );
    	double day = getDay( cal );   
		double step = 1.0 / num;
		return getSatelliteListSgp4( mSatElset, num, year, day, step );
	}

    /** 
	 * getYear
	 */ 
    private int getYear( Calendar cal ) {
   		return cal.get( Calendar.YEAR );
   	}	

    /** 
	 * getDay
	 */    	
    private double getDay( Calendar cal ) {
    	int dat_of_year = cal.get( Calendar.DAY_OF_YEAR );
    	int hour = cal.get( Calendar.HOUR_OF_DAY );
    	int min = cal.get( Calendar.MINUTE );
    	int sec = cal.get( Calendar.SECOND );
    	double start = dat_of_year + ( 3600.0 * hour + 60.0 * min + sec ) / 86400.0; 
    	return start;   
	}

    /** 
	 * getSatElset
	 */ 		
    private SatElset getSatElset( String card1, String card2 ) {
		SatElset elset = null;
		try {
			elset = new SatElset( card1, card2 );
		} catch (SatElsetException e) {
			if (D) e.printStackTrace();
		}
		return elset;
	}

    /** 
	 * getSatelliteListSgp4
	 */	
    private List<Satellite> getSatelliteListSgp4( SatElset satElset, int num, int year, double start, double step ) {
    	List<Satellite> list = new ArrayList<Satellite>();
        try {
            Sgp4Unit sgp4 = new Sgp4Unit( satElset );
            for ( int i = 0; i < num; i++ ) {
            	double day = start + i * step;
            	Sgp4Data data = sgp4.runSgp4( year, day );
            	list.add( new Satellite( data ) );
            }
        } catch ( ObjectDecayed e ) {
            if (D) e.printStackTrace();
        } catch ( SatElsetException e ) {
            if (D) e.printStackTrace();
        }
		return list;
    }

	/**
	 * readAssetsFile
	 */			
	private List<String> readAssetsFile( String name ) {
		AssetManager manager = mContext.getResources().getAssets();
		List<String> list = new ArrayList<String>();
        String str = "";
		try{
            InputStream is = manager.open( name );
        	BufferedReader buf = new BufferedReader( new InputStreamReader( is ) );
            while (((str = buf.readLine()) != null) && (str.length() > 1)) {
			    list.add( str );
			}
		} catch (IOException e) {
			if (D) e.printStackTrace();
		}
		return list;
    }

	/**
	 * log_d
	 */			
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}
}
