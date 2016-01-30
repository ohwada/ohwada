package jp.ohwada.android.osm2;

import jp.ohwada.android.osm2.task.ManageFile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/*
 * Main Activity
 */	
public class MainActivity extends Activity {

	// object
	private SharedPreferences mPreferences;

	// variable
	private int mLat = 0;
	private int mLong = 0;	
		
	/*
	 * === onCreate ===
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// file initial
		ManageFile manageFile = new ManageFile();
		manageFile.init();
		manageFile.clearOldCache();

		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
    	execTaskFromIntent( getIntent() );
    }

	/**
	 * === onNewIntent ===
	 */
    @Override
    public void onNewIntent( Intent intent ) {
    	execTaskFromIntent( intent );
    }

	/**
	 * execTaskFromIntent
	 * @param Intent intent
	 */
    private void execTaskFromIntent( Intent intent ) {
    	mLat = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LAT, Constant.GEO_LAT );
    	mLong = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LONG, Constant.GEO_LONG );   

		// get intent value
		if ( intent != null ) {
			UriGeo point = new UriGeo( intent );
			if (( point != null )&& point.flag ) {
				mLat = point.e6_lat;
				mLong = point.e6_long;
			}
		}	
	
	}

	/**
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
        super.onResume();
		startMap();
	}

	/**
	 * startMap
	 */
	private void startMap() {
    	String map = mPreferences.getString( 
    		Constant.PREF_NAME_MAP_KIND, Constant.MAP_OSM );  

		if ( Constant.MAP_GOOGLE.equals( map ) ) {
			// start goolge map
			Intent intent1 = new Intent( this, GoogleMapActivity.class );
			intent1.putExtra( Constant.EXTRA_GEO_LAT, mLat );
			intent1.putExtra( Constant.EXTRA_GEO_LONG, mLong );
			startActivityForResult( intent1, Constant.REQUEST_MAP_GOOGLE );

		} else {
			// start yahoo map
			Intent intent2 = new Intent( this, YahooMapActivity.class );
			intent2.putExtra( Constant.EXTRA_GEO_LAT, mLat );
			intent2.putExtra( Constant.EXTRA_GEO_LONG, mLong );
			startActivityForResult( intent2, Constant.REQUEST_MAP_YAHOO );
		}
	}
	
	/**
	 * === onActivityResult ===
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
	@Override
    public void onActivityResult( int request, int result, Intent data ) {
        int code = Constant.RESULT_NONE;
        // get return code
		if ( result == RESULT_OK ) {
			code = data.getIntExtra( Constant.EXTRA_CODE, Constant.RESULT_NONE );
		}
		// finish if no return code
        if ( code == Constant.RESULT_NONE ) {
        	finish();
        }
        // onResume if return code
    }	
}
