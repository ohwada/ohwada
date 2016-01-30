package jp.ohwada.android.osm2;

import java.util.List;

import jp.ohwada.android.osm2.task.NodeList;
import jp.ohwada.android.osm2.task.NodeListTask;
import jp.ohwada.android.osm2.task.NodeRecord;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/*
 * GoogleMap Activity
 */
public class GoogleMapActivity extends MapActivity
	implements LocationListener {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG_SUB = "GoogleMapActivity";
		
	// gps
    private static final int DEFAULT_ACCURACY = 300;	// 300 m

	// marker
    private static final int DELAY_TIME = 100; 	// 0.1 sec
        
	// object
	private SharedPreferences mPreferences;
	private MyLocation mLocation;
   	private MainMenu mMainMenu;
   	private NodeListTask mNodeTask;

	// view conponent
    private OptionDialog mOptionDialog = null;
       	 	
	// map view conponent
	private MapView mMapView;
	private MapController mMapController;
	private MarkerItemizedOverlay mNodeOverlay;
    private GpsItemizedOverlay mGpsOverlay;
   	
	// variable
	private NodeList mNodeList = null;
	    			
	/*
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 */
	@Override 
	public void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
    	setContentView( R.layout.activity_map_google ); 
		
		// button option
		Button btnOption = (Button) findViewById( R.id.main_button_option );
		btnOption.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				showOptionDialog();
			}
		});

		// button get
		Button btnGet = (Button) findViewById( R.id.main_button_get );
		btnGet.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				getNewPoint();
			}
		});

		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
			
		Intent intent = getIntent();
		int lat = intent.getIntExtra( 
			Constant.EXTRA_GEO_LAT, Constant.GEO_LAT );
		int lng = intent.getIntExtra( 
			Constant.EXTRA_GEO_LONG, Constant.GEO_LONG );

    	// map
		mMapView = (MapView) findViewById( R.id.mapview );
    	mMapView.setClickable( true );
		mMapView.setBuiltInZoomControls( true ); 
		mMapController = mMapView.getController();

    	// marker
    	Drawable node_marker = getResources().getDrawable( R.drawable.marker_white );
    	mNodeOverlay = new MarkerItemizedOverlay( this, node_marker );
        mMapView.getOverlays().add( mNodeOverlay );
        
    	// gps
    	Drawable gps_marker = getResources().getDrawable( R.drawable.gps_blue_dot );
    	mGpsOverlay = new GpsItemizedOverlay( gps_marker );
    	mGpsOverlay.setDefaultAccuracy( DEFAULT_ACCURACY );
    	mMapView.getOverlays().add( mGpsOverlay );

     	// center
		mMapController.setZoom( Constant.GEO_ZOOM_GOOGLE );
		setCenter( new MyGeoPoint( lat, lng ) ) ; 
				
		// object
		mMainMenu = new MainMenu( this );  
		mLocation = new MyLocation( this, this );   	

		// task	
		mNodeTask = new NodeListTask( this, msgHandler );		
    	execTask( lat, lng );
	}
	    
    /**
	 * execTask
	 */  						
	private void execTask( int lat, int lng ) {
		// get nodes
		boolean ret = mNodeTask.execute( lat, lng );
		if ( ret ) {
			mNodeList = mNodeTask.getList();
    		// delayed execution, since showing markers takes time
			delayHandler.postDelayed( delayRunnable, DELAY_TIME ); 
		}
    }
	
	/*
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
    	super.onResume();
    	    
    	// finish if map change
    	if ( checkMapChange() ) {
			Intent intent = new Intent();
			intent.putExtra( Constant.EXTRA_CODE, Constant.RESULT_MAP_CHANGE );
			setResult( Activity.RESULT_OK, intent );
			finish(); 
		}

		// update GPS manager
		boolean ret = mLocation.requestLocationUpdates();
		if ( ret ) {
			showGps( mLocation.getLastPoint() ) ;
		} else {
			toast_show( R.string.gps_no_manager );
		}
    }

	/*
	 * checkMapChange
	 * @return boolean
	 */
	private boolean checkMapChange() {
    	String map = mPreferences.getString( 
    		Constant.PREF_NAME_MAP_KIND, Constant.MAP_OSM ); 
    	if ( Constant.MAP_GOOGLE.equals( map ) ) return false;
    	return true;	
	}

	/*
	 * === onPause ===
	 */
   @Override
    protected void onPause() {	
        mLocation.removeUpdates();
        super.onPause();
    }

	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mNodeTask.cancel();
        cancelOptionDialog();
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
     	showGps( new MyGeoPoint( location ) ) ;
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
		super.onActivityResult( request, result, data );
    }

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
		mMainMenu.execCreateOptionsMenu( menu );
        return super.onCreateOptionsMenu( menu );
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		boolean ret = mMainMenu.execOptionsItemSelected( item );
		if ( ret ) return true;
        return super.onOptionsItemSelected( item );
    }
	
// --- main ---
	/**
	 * setCenter
	 * @param MyGeoPoint point
     */
	private void setCenter( MyGeoPoint point ) {
		if ( point == null ) return; 
		GeoPoint geo = new GeoPoint( point.lat, point.lng );
		mMapController.setCenter( geo );
	}
	
    /**
     * showMarker
     * This process is slow, it takes about 7 seconds in Android 1.6 emulator
     */	
	private void showMarker() {		
		// no marker
		if ( mNodeList == null ) {
			toast_show( R.string.error_not_get_node );
			return;
		}
		
		List<NodeRecord> list = mNodeList.getListRecord();
		// no marker
		if (( list == null )||( list.size() == 0 )) {
			toast_show( R.string.error_not_get_node );
			return;
		}

		// show marker
		log_d( "showMarker start " + list.size() );	
		mNodeOverlay.clearPoints();
        for ( int i=0; i<list.size(); i++ ) {
			mNodeOverlay.addPoint( list.get( i ) );		
		}
		// redraw map, In order to show markers	
		mMapView.invalidate();  
		log_d( "showMarker end" );
	}

	/**
	 * getNewPoint
     */
	private void getNewPoint() {
		GeoPoint point = mMapView.getMapCenter();
		execTask( point.getLatitudeE6(),  point.getLongitudeE6() );
	}			

// --- GPS ---
	/**
	 * showGps 
	 * @param MyGeoPoint point
     */
    private void showGps( MyGeoPoint point ) {
    	if ( point == null ) return ;
    	if ( point.location == null ) return ;
		mGpsOverlay.setLocation( point.location ); 
		// redraw map, In order to show marker	
		mMapView.invalidate();  
    }
    
    /**
	 * moveGps 
     */
    private void moveGps() {
    	MyGeoPoint point = mLocation.getLastPoint();
    	if ( point == null ) {
			toast_show( R.string.gps_not_found );
    		return ;
    	}	
		setCenter( point );
		showGps( point );
    }
// --- GPS end ---
				
// --- Search ---
	/**
	 * showLocation
	 * @param MyGeoPoint point
	 */
	private void showLocation( MyGeoPoint point ) {		
		// if NOT found
		if ( point == null ) {
			toast_show( R.string.search_not_found );
			return;
		}
		// set center of map
		setCenter( point );
		mMapController.setZoom( Constant.GEO_ZOOM_GOOGLE );
		toast_show( R.string.search_found );
	}
// --- Search end ---

// --- Dialog ---
	private void showOptionDialog() {
		mOptionDialog = new OptionDialog( this );
		mOptionDialog.setHandler( msgHandler );
		mOptionDialog.create( );
		mOptionDialog.show();
	}

	/**
	 * cancelOptionDialog
	 */
    public void cancelOptionDialog() {
        if ( mOptionDialog != null ) {
        	mOptionDialog.cancel();
        }
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
	/**
	 * Message Handler
	 */
	private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
			execHandler( msg );
        }
    };

	/**
	 * execHandler
	 * @param Message msg
	 */
	private void execHandler( Message msg ) {
		switch ( msg.what ) {
            case Constant.MSG_WHAT_DIALOG_MAP:
            	execHandlerOption( msg );
                break;
            case Constant.MSG_WHAT_TASK_NODE_LIST:
                mNodeList= mNodeTask.getList();
    			showMarker();
                break;  
			case Constant.MSG_WHAT_TASK_GEOCODER:
    			showLocation( mOptionDialog.getPoint() );
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
				setCenterDefault();
                break;
            case Constant.MSG_ARG1_DIALOG_MAP_GPS:
				moveGps();
                break;
        }
	}

	/**
	 * setCenterDefault
	 */
	private void setCenterDefault() {
     	int lat = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LAT, Constant.GEO_LAT );
    	int lng = mPreferences.getInt( 
    		Constant.PREF_NAME_GEO_LONG, Constant.GEO_LONG );  
		setCenter( new MyGeoPoint( lat, lng ) );		
	}
	
	/**
	 * toast_show
	 * @param int id
	 */ 
	private void toast_show( int id ) {
		Toast.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}
		
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
}
