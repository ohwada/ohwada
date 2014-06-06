package jp.ohwada.android.satellite;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * LatLngController 
 */
public class LatLngController {

	private final static boolean D = SatelliteConstant.DEBUG;
	private final static String TAG = SatelliteConstant.TAG;
	private String TAG_SUB = "LatLngController";

    /** 
	 * === constructor ===
	 */	
	public LatLngController() {
		// dummy
	}

	/**
	 * convList
	 */
	public List<LatLng> convList( List<Satellite> list1 ) {
		List<LatLng> list2 = new ArrayList<LatLng>();
		for ( Satellite sat: list1 ) {
			list2.add( new LatLng ( sat ) );
		}
		return list2;
	}

	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
			
}
