package jp.ohwada.android.osm1;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

/*
 * Map Geocoder
 * http://developer.android.com/reference/android/location/Geocoder.html
 */
public class MapGeocoder {

	// debug
	private static final boolean D = Constant.DEBUG;
		
	// object
	private Geocoder mGeocoder;

	// varibale
	private int mMaxRresults = 1;
	private int mMaxRetry = 3;
		
	/*
	 * === Constructor ===
	 */
	public MapGeocoder( Context context ) {
	    mGeocoder = new Geocoder( context, Locale.getDefault() ) ;	
	}

	/*
	 * setMaxResults
	 * @param int results
	 */
	public void setMaxResults( int results ) {
		mMaxRresults = results;
	}

	/*
	 * setMaxRetry
	 * @param int retry
	 */
	public void setMaxRetry( int retry ) {
		mMaxRetry = retry ;
	}
			
	/**
	 * <pre>
	 * search latitude and longitude  from location name 
	 * repeats 3 times until get latitude and longitude
	 * </pre>
	 * @param String location
	 * @return List<Address>
	 */
	public List<Address> getAddressListRetry( String location ) {
		if ( "".equals( location ) ) return null;
		List<Address> list = null;
		// repeats 3 times until get latitude and longitude 
		for ( int i=0; i < mMaxRetry; i ++ ) {
			list = getAddressList(location);
			// break, if get latitude and longitude
			if (( list != null ) && !list.isEmpty() ) break;
		}
		return list;
	}
	
	/**
	 * search latitude and longitude  from location name 
	 * @param String location
	 * @return List<Address>
	 */
	public List<Address> getAddressList( String location ) {
		if ( "".equals( location ) ) return null;
		List<Address> list = null;
		try {
			list = mGeocoder.getFromLocationName( location, mMaxRresults );
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
		}
		return list;
	}
}
