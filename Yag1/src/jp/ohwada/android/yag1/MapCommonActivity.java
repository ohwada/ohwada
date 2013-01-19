package jp.ohwada.android.yag1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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

	// gps
    private static final long LOCATION_MIN_TIME = 0L; 
    private static final float LOCATION_MIN_DISTANCE = 0f;
    private static final int DEFAULT_ACCURACY = 300;	// 300 m
    
	// gps
	private LocationManager mLocationManager;
 	
	// view conponent	
	protected MapView mMapView;
	protected MapController mMapController;
    protected GpsItemizedOverlay mGpsOverlay;
    protected MenuView mMenuView;
    protected ErrorView mErrorView;

	// variable
    protected GeoPoint ｍGeoPointPlace = null; 
   
	/*
	 * createMenu
	 * @param View view
	 */
	protected void createMenu( View view ) {	
		mMenuView = new MenuView( this, view );
		mErrorView = new ErrorView( this, view );
	}
			
	/*
	 * createMap()
	 */
	protected void createMap() {				
		Button btnMove = (Button) findViewById( R.id.map_button_move );
		btnMove.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				showDialog();
			}
		});
		
    	// map
		mMapView = (MapView) findViewById( R.id.mapview );
    	mMapView.setClickable( true );
		mMapView.setBuiltInZoomControls( true ); 
		mMapController = mMapView.getController();
    	// gps
    	Drawable gps_marker = getResources().getDrawable( R.drawable.gps_blue_dot );
    	mGpsOverlay = new GpsItemizedOverlay( gps_marker );
    	mGpsOverlay.setDefaultAccuracy( DEFAULT_ACCURACY );
    	mMapView.getOverlays().add( mGpsOverlay );
     	// default
		mMapController.setZoom( Constant.GEO_ZOOM );
		setCenter( new GeoPoint( Constant.GEO_LAT, Constant.GEO_LONG ) ) ; 
				   	
		mLocationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
	}

	/**
	 * setCenter
	 * @param GeoPoint point
     */
	protected void setCenter( GeoPoint point ) {
		if ( point == null ) return; 
		mMapController.setCenter( point );
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
        	Toast.makeText( this, R.string.gps_no_manager, Toast.LENGTH_SHORT ).show();
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
			Toast.makeText( this,  R.string.gps_not_found, Toast.LENGTH_SHORT ).show();
    		return ;
    	}	
    	GeoPoint point = new GeoPoint( 
			doubleToE6( location.getLatitude() ), 
			doubleToE6( location.getLongitude() ) );
		setCenter( point );
		showGps( location );
    }

	/**
	 * convert real number to integer
	 * @param Double : location( floating point format )
	 * @return int : location( E6 format )
	 */
    protected int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}
// --- GPS end ---

// --- Dialog ---
	/**
	 * showDialog
     */
	protected void showDialog() {
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
            case Constant.MSG_WHAT_DIALOG_MAP:
            	execHandlerMove( msg );
                break;
        }
	}

	/**
	 * execHandlerMove
	 * @param Message msg
	 */
	private void execHandlerMove( Message msg ) {
    	switch ( msg.arg1 ) {
            case Constant.MSG_ARG1_DIALOG_MAP_DEFAULT:
				setCenter( new GeoPoint( Constant.GEO_LAT, Constant.GEO_LONG ) );
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_GPS:
				moveGps();
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_MARKER:
            	if ( ｍGeoPointPlace != null ) {
					setCenter( ｍGeoPointPlace );
				}
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_SEARCH:
            	String location = msg.getData().getString( Constant.BUNDLE_DIALOG_MAP_LOCATION );
				serachLocation( location );
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_MAP:
				mMenuView.forcedFinishMap();
                break;
        }
	}

	/**
	 * serachLocation
	 * @param String location
	 */
	protected void 	serachLocation( String location ) {
		// dummy
	}

}
