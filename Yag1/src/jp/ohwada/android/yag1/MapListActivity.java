package jp.ohwada.android.yag1;

import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.task.DateUtility;
import jp.ohwada.android.yag1.task.PlaceListEventFile;
import jp.ohwada.android.yag1.task.PlaceRecord;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.maps.GeoPoint;

/*
 * Map List Activity
 */
public class MapListActivity extends MapCommonActivity { 
	
	// constant
    private static final int DELAY_TIME = 100; 	// 0.1 sec
	 	
	// view conponent	
	private View mView;
	private MarkerItemizedOverlay mEventOverlay;
	private MarkerItemizedOverlay mOtherOverlay;
	private MapListDialog mOptionDialog;
				
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
		TAG_SUB = "MapListActivity";
		
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
		List<PlaceRecord> list = getPlaceList();
		// no marker
		if (( list == null )||( list.size() == 0 )) {
			mErrorView.showNoPlace();
			return;
		}
		// show marker
		log_d( "showMarker start " + list.size() );	
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

// --- Search --
	/**
	 * showLocation
	 * @param GeoPoint point
	 */
	private void showLocation( GeoPoint point ) {		
		// if NOT found
		if ( point == null ) {
			toast_show( R.string.search_not_found );
			return;
		}
											 	
		// set center of map
		setCenter( point );
		mMapController.setZoom( Constant.GEO_ZOOM );
		toast_show( R.string.search_found );
	}
// --- Search end ---

// --- Dialog ---
	protected void showOptionDialog() {
		mOptionDialog = new MapListDialog( this );
		mOptionDialog.setHandler( msgHandler );
		mOptionDialog.create( mGeoName );
		mOptionDialog.show();
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

// --- Message Handler ---
    protected void execHandlerGeocoder() {
    	if ( mOptionDialog != null ) {
    		showLocation( mOptionDialog.getPoint() );
    	}
    }
    
	/**
	 * execHandlerMapApp
	 */
	protected void execHandlerMapApp() {
	 	startMapApp( mMapView.getMapCenter() );
	}
// --- Message Handler end ---

}
