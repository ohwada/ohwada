package jp.ohwada.android.yag1;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/*
 * MapSetting Activity
 */
public class MapSettingActivity extends MapActivity 
	implements OnGestureListener { 

	// constant
    private static final int DELAY_TIME = 1000; 	// 1 sec
    
	// object
	private MapController mMapController;
	private CenterItemizedOverlay mOverlay;
	private SearchTask mSearchTask; 
	private SharedPreferences mPreferences;
		
	// view 
	private View mView;		
	private MapView mMapView;
	
	// delay timer
	private boolean isRunnig = false;
			
	/*
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 */
	@Override 
	public void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	mView = getLayoutInflater().inflate( R.layout.activity_map_setting, null );
		setContentView( mView ); 

		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
    	String name = mPreferences.getString( 
    		Constant.PREF_NAME_GEO_NAME, 
    		getResources().getString( R.string.geo_name ) );
    	int lat = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LAT, Constant.GEO_LAT );
    	int lng = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LONG, Constant.GEO_LONG );    

		// search
		mSearchTask = new SearchTask( this, mView, msgHandler );
		mSearchTask.create();
		mSearchTask.setAddressText( name );

		Button btnSet = (Button) mView.findViewById( R.id.map_setting_button_set );
		btnSet.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				setLocation();
			}
		});
		        
        // scroll & zoom
		mMapView = (MapView) findViewById( R.id.mapview );
    	mMapView.setClickable( true );
		mMapView.setBuiltInZoomControls( true );
       	
    	// Akashi Municipal Planetarium
    	// http://homepage2.nifty.com/yamakatsu/keiido_shigosen.html
    	GeoPoint point = new GeoPoint( lat, lng );
    			
		// set center
		mMapController = mMapView.getController();
    	mMapController.setCenter( point );
    	mMapController.setZoom( Constant.GEO_ZOOM );
    	
    	// marker
    	Drawable marker = getResources().getDrawable( R.drawable.marker_cross );
    	mOverlay = new CenterItemizedOverlay( marker );
        mMapView.getOverlays().add( mOverlay );
		showCenterMarker();
	}

	/**
	 * showCenterMarker
	 */
    private void showCenterMarker() {
		mOverlay.setPoint( mMapView.getMapCenter() );
  	}

	/**
	 * setLocation
	 */
    private void setLocation() {
    	String name = mSearchTask.getAddressEdit();
		// nothig if no input
		if (( name.length() == 0 )||( name.equals("") )) {
			toast_show( R.string.search_please_name );
			return;
		} 

		// save center point		
		GeoPoint point = mMapView.getMapCenter();
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString( Constant.PREF_NAME_GEO_NAME, name );
		editor.putInt( Constant.PREF_NAME_GEO_LAT, point.getLatitudeE6() );
		editor.putInt( Constant.PREF_NAME_GEO_LONG, point.getLongitudeE6() );
		editor.commit(); 
		finish();
  	}

	/*
	 * === isRouteDisplayed ===
	 * for MapActivity
	 * @return boolean
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// show marker in center, if the timer is not running
	    if ( !isRunnig ) {
		    showCenterMarker();
		}
		return false;
	}

	/**
	 * dispatchTouchEvent
	 * @param MotionEvent event
	 * @return boolean
	 */
    @Override
    public boolean dispatchTouchEvent( MotionEvent event ) {
    	// show marker in center, if finger is move up from a screen
    	showCenterMarker();
    	// start delay timer, if not running
    	if ( !isRunnig ) {
    		isRunnig = true;
    		delayHandler.postDelayed( delayRunnable, DELAY_TIME ); 
    	}
        return super.dispatchTouchEvent( event );
    }

	@Override
	public boolean onDown( MotionEvent arg0 ) {
		return false;
	}

	@Override
	public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress( MotionEvent e ) {
		// dummu		
	}

	@Override
	public boolean onScroll( MotionEvent e1, MotionEvent e2, float distanceX, float distanceY ) {
		return false;
	}

	@Override
	public void onShowPress( MotionEvent e ) {
		// dummy		
	}

	@Override
	public boolean onSingleTapUp( MotionEvent e ) {
		return false;
	}	

// --- Message Handler ---
	/**
	 * Message Handler
	 */
	protected Handler msgHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
			execHandler( msg );
        }
    };

	/**
	 * execHandler(
	 * @param Message msg
	 */
	private void execHandler( Message msg ) {
    	switch ( msg.what ) {
            case Constant.MSG_WHAT_TASK_GEOCODER:
    			showLocation( mSearchTask.getPoint() );
                break;
        }
	}

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
		mMapController.setCenter( point );
		mMapController.setZoom( Constant.GEO_ZOOM );
		toast_show( R.string.search_found );
	}
// --- Message Handler end ---

// --- Handler class ----
    private final Handler delayHandler = new Handler();
    
// --- Runnable class ----  	
	private final Runnable delayRunnable = new Runnable() {
    	@Override
    	public void run() {
    	    // After 1 second, 
    	    // clear timer status
    	    // and show marker in center
			isRunnig = false;
			showCenterMarker();
    	}
	};

	/**
	 * toast_show
	 * @param int id
     */
	private void toast_show( int id ) {
		Toast.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}
}
