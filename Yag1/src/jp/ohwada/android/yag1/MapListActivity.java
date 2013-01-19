package jp.ohwada.android.yag1;

import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.task.DateUtility;
import jp.ohwada.android.yag1.task.PlaceListEventFile;
import jp.ohwada.android.yag1.task.PlaceRecord;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

/*
 * Map List Activity
 */
public class MapListActivity extends MapCommonActivity { 

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG_SUB = "MapListActivity";
	
	// constant
    private static final int DELAY_TIME = 100; 	// 0.1 sec

	// Geocoder
	private MapGeocoder mGeocoder;
	private InputMethodManager mInputManager;
 	
	// view conponent	
	private View mView;
	private ProgressDialog mProgressDialog;
	private MarkerItemizedOverlay mEventOverlay;
	private MarkerItemizedOverlay mOtherOverlay;
			
	/*
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 */
	@Override 
	public void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
        mView = getLayoutInflater().inflate( R.layout.activity_list_map, null );
		setContentView( mView ); 

		// view object
		createMenu( mView );
		createMap();
		mMenuView.enableEvent();
		mMenuView.enablePlace();
		
		// view conponent						
		mProgressDialog = new ProgressDialog( this );

    	// Geocoder
		mGeocoder = new MapGeocoder( this );
        mInputManager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );

    	// marker
       	Activity activty = this;
       	Drawable marker_other = getResources().getDrawable( R.drawable.marker_yellow );
    	mOtherOverlay = new MarkerItemizedOverlay( activty, marker_other );
        mMapView.getOverlays().add( mOtherOverlay );
		// the end of overlay has an event info, since a marker is overwritten 
       	Drawable marker_event = getResources().getDrawable( R.drawable.marker_red );
    	mEventOverlay = new MarkerItemizedOverlay( activty, marker_event );
        mMapView.getOverlays().add( mEventOverlay );
  
    	// delayed execution, since showing markers takes time
   		delayHandler.postDelayed( delayRunnable, DELAY_TIME ); 
	}

// --- Main ---	
    /**
     * showMarker
     * This process is slow, it takes about 7 seconds in Android 1.6 emulator
     */	
	private void showMarker() {
		log_d( "showMarker end" );	
		List<PlaceRecord> list = getPlaceList();
		// no marker
		if (( list == null )||( list.size() == 0 )) {
			mErrorView.showNoPlace();
			return;
		}
		// show marker
		mErrorView.hideText();
        for ( int i=0; i<list.size(); i++ ) {
        	PlaceRecord r = list.get( i );
        	if ( r.event_flag ) {
				mEventOverlay.addPoint( r );
			} else {
				mOtherOverlay.addPoint( r );
			}				
		}
		// redraw map, In order to show markers	
		mMapView.invalidate();  
		log_d( "showMarker end" );
	}
	
	/**
	 * getPlaceList
	 * @return List<PlaceRecord>
	 */ 
	private List<PlaceRecord> getPlaceList() {
		DateUtility utility = new DateUtility();  
		Date date = utility.getDateToday();
		PlaceListEventFile file = new PlaceListEventFile();
		return file.getListForMap( date );
	}		
// --- Main end ---

// --- Search ---
	/**
	 * searchLocation
	 * @param String location
	 */
	protected void 	serachLocation( String location ) {
		// nothig if no input
		if ( location.length() == 0 ) return;
		if ( location.equals("") ) return;
		
		// search location
		hideInputMethod( mView );
		showProgress();
		List<Address> list = mGeocoder.getAddressListRetry( location );
		hideProgress();
		
		// if NOT found
		if (( list == null ) || list.isEmpty() ) {
			Toast.makeText ( this, R.string.search_not_found, Toast.LENGTH_SHORT ).show();
			return;
		}
											 	
		// get point
		Address addr = list.get( 0 );
		int lat = doubleToE6( addr.getLatitude() ); 
		int lng = doubleToE6( addr.getLongitude() );
		GeoPoint point = new GeoPoint( lat, lng );

		// set center of map
		setCenter( point );
		mMapController.setZoom( Constant.GEO_ZOOM );

		Toast.makeText( this, R.string.search_found, Toast.LENGTH_SHORT ).show();
	}
	 	
	/**
	 * <pre>
	 * show Progress Dialog
	 * It seems not to show,
	 * becuase the response of geocoder is earlier 	 		/* 
	 * </pre>
	 */
	private void showProgress() {
		String msg = getResources().getString( R.string.searching );
        mProgressDialog.setMessage( msg );
        mProgressDialog.show();
	}

	/**
	 * hide Progress Dialog
	 */
	private void hideProgress() {
        mProgressDialog.dismiss();
	}
	
	/**
	 * hide software keyboard
	 * @param View view
	 */
	private void hideInputMethod( View view ) {
        mInputManager.hideSoftInputFromWindow( view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY ); 
	}
// --- Search end ---

// --- Dialog ---
	protected void showDialog() {
		MapListDialog dialog = new MapListDialog( this );
		dialog.setHandler( msgHandler );
		dialog.create();
		dialog.show();
	}
// --- Dialog end ---
		
// --- Handler class ----
    private final Handler delayHandler = new Handler();

// --- Runnable class ----    
	private final Runnable delayRunnable = new Runnable() {
    @Override
    	public void run() {
			showMarker();
    	}
	};

	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
}
