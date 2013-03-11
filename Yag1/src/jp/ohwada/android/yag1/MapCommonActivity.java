package jp.ohwada.android.yag1;

import jp.ohwada.android.yag1.task.PlaceRecord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/*
 * Map Common Activity
 */
public class MapCommonActivity extends MapActivity 
	implements LocationListener {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	protected String TAG_SUB = "MapCommonActivity";
	
	// gps
    private static final long LOCATION_MIN_TIME = 0L; 
    private static final float LOCATION_MIN_DISTANCE = 0f;
    private static final int DEFAULT_ACCURACY = 300;	// 300 m
	    
	// object
	private LocationManager mLocationManager;
	private ActivityUtility mActivityUtility;
	 	
	// view conponent
	protected MapView mMapView;
	protected MapController mMapController;
    protected GpsItemizedOverlay mGpsOverlay;
    protected MenuView mMenuView;
    protected ErrorView mErrorView;

	// variable
    protected PlaceRecord ｍPlaceRecord = null; 
	private GeoPoint ｍGeoPointDefault = null; 

	/*
	 * createView
	 * @return LinearLayout
	 */
	protected LinearLayout createViewRoot() {
		// main view
        LinearLayout ll = new LinearLayout( this );
        ll.setOrientation( LinearLayout.VERTICAL );
        return ll;
	}

	/*
	 * createView
	 * @param int id
	 * @return View
	 */
	protected View createViewHeader( int id ) {	        
        View view = getLayoutInflater().inflate( id, null ); 

		mMenuView = new MenuView( this, view );
		mMenuView.enableEvent();
		mMenuView.enablePlace();
		
		mErrorView = new ErrorView( this, view );

		Button btnOption = (Button) view.findViewById( R.id.map_button_option );
		btnOption.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				showOptionDialog();
			}
		});

		return view;
	}

	/*
	 * createView
	 */
	protected void createViewMap() {	 	
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( this );
    	int lat = pref.getInt( 
    		Constant.PREF_NAME_GEO_LAT, Constant.GEO_LAT );
    	int lng = pref.getInt( 
    		Constant.PREF_NAME_GEO_LONG, Constant.GEO_LONG ); 
		ｍGeoPointDefault = new GeoPoint( lat, lng );
		    				
    	// map
		mMapView = new MapView( this, ApiKey.GOOGLE_MAP_API_KEY );
    	mMapView.setClickable( true );
		mMapView.setBuiltInZoomControls( true ); 
		mMapController = mMapView.getController();
		mMapController.setZoom( Constant.GEO_ZOOM );
		mMapController.setCenter( ｍGeoPointDefault ) ; 

    	// gps
    	Drawable gps_marker = getResources().getDrawable( R.drawable.gps_blue_dot );
    	mGpsOverlay = new GpsItemizedOverlay( gps_marker );
    	mGpsOverlay.setDefaultAccuracy( DEFAULT_ACCURACY );
    	mMapView.getOverlays().add( mGpsOverlay );
	}
	
	/*
	 * createObject
	 */
	protected void createObject() {
		mLocationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
		mActivityUtility = new ActivityUtility( this );
	}

	/**
	 * setCenter
	 * @param GeoPoint point
     */
	protected void setCenter( GeoPoint point ) {
		if ( point == null ) return; 
		mMapController.setCenter( point );
		mMapView.invalidate(); 
	}
						
	/*
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
        mMenuView.execResume(); 
        if ( mLocationManager != null ) {
        	// update GPS manager
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                LOCATION_MIN_TIME,
                LOCATION_MIN_DISTANCE,
                this );
            // show Location  
			showGps( getLastLocation() );    
        } else {
        	// if NOT acquire GPS manager
        	toast_show( R.string.gps_no_manager );
		}
        super.onResume();
    }

	/*
	 * === onPause ===
	 */
   @Override
    protected void onPause() {	
        if ( mLocationManager != null ) {
            mLocationManager.removeUpdates( this );
        }
        super.onPause();
    }

	/*
	 * === isRouteDisplayed ===
	 * for MapAvtivity
	 * @return boolean
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/*
	 * === onLocationChanged ===
	 * for LocationListener
	 * @param Location location
	 */
    @Override
    public void onLocationChanged( Location location ) {
     	showGps( location ) ;
    }
            
	/*
	 * === onProviderDisabled ===
	 * for LocationListener
	 * @param String provider
	 */
    @Override
    public void onProviderDisabled( String provider ) {
		// dummy
    }

	/*
	 * === onProviderEnabled ===
	 * for LocationListener
	 * @param String provider
	 */
    @Override
    public void onProviderEnabled( String provider ) {
		// dummy
    }

	/*
	 * === onStatusChanged ===
	 * for LocationListener
	 * @param String provider
	 * @param int status
	 * @param Bundle extras
	 */
	@Override
	public void onStatusChanged( String provider, int status, Bundle extras ) {
		// dummy	
	}
        
	/**
	 * === onActivityResult ===
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
	@Override
    public void onActivityResult( int request, int result, Intent data ) {
        mMenuView.execActivityResult( request, result, data );
    }
	 
// --- GPS ---
	/**
	 * get Last Location. 
	 * @return Location
	 * <pre>
	 * This is cache value, NOT present location. 
	 * when get a location at A point, and NOT get a location at B point, value is a location of A point.
	 * </pre>
     */
    private Location getLastLocation() {
		if ( mLocationManager == null ) return null;
    	return mLocationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
	}

	/**
	 * showGps 
	 * @param Location
     */
    private void showGps( Location location ) {
    	if ( location == null ) return ;
		mGpsOverlay.setLocation( location ); 
		// redraw map, In order to show marker	
		mMapView.invalidate();  
    }
    
    /**
	 * moveGps 
     */
    private void moveGps() {
    	Location location = getLastLocation();
    	if ( location == null ) {
			toast_show( R.string.gps_not_found );
    		return ;
    	}	
    	GeoPoint point = new GeoPoint( 
			doubleToE6( location.getLatitude() ), 
			doubleToE6( location.getLongitude() ) );
		setCenter( point );
		showGps( location );
    }
// --- GPS end ---

// --- Dialog ---
	/**
	 * showOptionDialog
     */
	protected void showOptionDialog() {
		// dummy
	}
// --- Dialog end ---
	
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
            case Constant.MSG_WHAT_DIALOG_MAP_LIST:
            case Constant.MSG_WHAT_DIALOG_MAP_PLACE:
            	execHandlerOption( msg );
                break;
            case Constant.MSG_WHAT_TASK_GEOCODER:
            	execHandlerGeocoder();
                break;
        }
	}

	/**
	 * execHandlerOption
	 * @param Message msg
	 */
	private void execHandlerOption( Message msg ) {
    	switch ( msg.arg1 ) {
            case Constant.MSG_ARG1_DIALOG_MAP_DEFAULT:
				setCenter( ｍGeoPointDefault );
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_GPS:
				moveGps();
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_MARKER:
            	if ( ｍPlaceRecord != null ) {
					setCenter( new GeoPoint( ｍPlaceRecord.map_lat, ｍPlaceRecord.map_lng ) );
				}
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_MAP:
				mMenuView.forcedFinishMap();
                break;
             case Constant.MSG_ARG1_DIALOG_MAP_APP:
				execHandlerMapApp();
                break;
             case Constant.MSG_ARG1_DIALOG_MAP_NAVICON:
				execHandlerMapNavicon();
                break;
        }
	}

	/**
	 * execHandlerGeocoder
	 */
    protected void execHandlerGeocoder() {
    	// dummy
    }

	/**
	 * startMapApp
	 */
	protected void execHandlerMapApp() {
		// dummy
	}

	/**
	 * startMapNavicon
	 */
	protected void execHandlerMapNavicon() {
		// dummy
	}
		
	/**
	 * startMapApp
	 * @param GeoPoint point
	 */
	protected void startMapApp( GeoPoint point ) {
		mActivityUtility.startApp( point );
	}

	/**
	 * startMapApp
	 * @param PlaceRecord record
	 */
	protected void startMapApp( PlaceRecord record ) {
		mActivityUtility.startApp( record );
	}

	/**
	 * startMapNavicon
	 * @param PlaceRecord record
	 */
	protected void startMapNavicon( PlaceRecord record ) {
		mActivityUtility.startNavicon( record );	
	}

// --- utility ---
	/**
	 * convert real number to integer
	 * @param Double : location( floating point format )
	 * @return int : location( E6 format )
	 */
    protected int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}
	
	/**
	 * toast_show
	 * @param int id
     */
	protected void toast_show( int id ) {
		Toast.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}	
		
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
}
