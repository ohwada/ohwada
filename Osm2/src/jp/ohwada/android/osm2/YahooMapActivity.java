package jp.ohwada.android.osm2;

import java.util.List;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.OverlayItem;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;

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
import android.widget.LinearLayout;
import android.widget.Toast;

/*
 * YahooMap Activity
 * http://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/androidsdk/
 */		
public class YahooMapActivity extends MapActivity
	implements LocationListener {
	
	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private static final String TAG_SUB = "YahooMapActivity";

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
	private PopupOverlay mPopupOverlay;
	private PinOverlay mPinOverlayAqua;
	private PinOverlay mPinOverlayBlack;
	private PinOverlay mPinOverlayBlue;
	private PinOverlay mPinOverlayFuchsia;
	private PinOverlay mPinOverlayGray;
	private PinOverlay mPinOverlayGreen;
	private PinOverlay mPinOverlayLime;
	private PinOverlay mPinOverlayMaroon;
	private PinOverlay mPinOverlayNavy;
	private PinOverlay mPinOverlayOlive;
	private PinOverlay mPinOverlayPurple;	
	private PinOverlay mPinOverlayRed;
	private PinOverlay mPinOverlaySilver;
	private PinOverlay mPinOverlayTeal;
	private PinOverlay mPinOverlayWhite;	
	private PinOverlay mPinOverlayYellow;
	private PinOverlay mPinOverlayGps;

	// variable
	private NodeList mNodeList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPreferences = PreferenceManager.getDefaultSharedPreferences( this );
		    		   		    	
		Intent intent = getIntent();
		int lat = intent.getIntExtra( 
			Constant.EXTRA_GEO_LAT, Constant.GEO_LAT );
		int lng = intent.getIntExtra( 
			Constant.EXTRA_GEO_LONG, Constant.GEO_LONG );

		// main view
        LinearLayout ll_root = new LinearLayout( this );
        ll_root.setOrientation( LinearLayout.VERTICAL );
        View view_menu = getLayoutInflater().inflate( R.layout.main_menu, null ); 
        ll_root.addView( view_menu );

		// button option
		Button btnOption = (Button) view_menu.findViewById( R.id.main_button_option );
		btnOption.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				showOptionDialog();
			}
		});

		// button get
		Button btnGet = (Button) view_menu.findViewById( R.id.main_button_get );
		btnGet.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				getNewPoint();
			}
		});

		// map view		            	       						
		mMapView = new MapView( this, YahooKey.APP_ID );
		mMapView.setBuiltInZoomControls( true );	
		mMapController = mMapView.getMapController();
		setCenter( new MyGeoPoint( lat, lng ) ); 
		mMapController.setZoom( Constant.GEO_ZOOM_YAHOO ); 

        ll_root.addView( mMapView );
        setContentView( ll_root );
				
		// marker
		mPinOverlayAqua = createPinOverlay( R.drawable.marker_aqua );
		mPinOverlayBlack = createPinOverlay( R.drawable.marker_black );
		mPinOverlayBlue = createPinOverlay( R.drawable.marker_blue);
		mPinOverlayFuchsia = createPinOverlay( R.drawable.marker_fuchsia );
		mPinOverlayGray = createPinOverlay( R.drawable.marker_gray );
		mPinOverlayGreen = createPinOverlay( R.drawable.marker_green );
		mPinOverlayLime = createPinOverlay( R.drawable.marker_lime );
		mPinOverlayMaroon = createPinOverlay( R.drawable.marker_maroon );
		mPinOverlayNavy = createPinOverlay( R.drawable.marker_navy );
		mPinOverlayOlive = createPinOverlay( R.drawable.marker_olive );
		mPinOverlayPurple = createPinOverlay( R.drawable.marker_purple );		
		mPinOverlayRed = createPinOverlay( R.drawable.marker_red );		
		mPinOverlaySilver = createPinOverlay( R.drawable.marker_silver );		
		mPinOverlayTeal = createPinOverlay( R.drawable.marker_teal );		
		mPinOverlayWhite = createPinOverlay( R.drawable.marker_white );		
		mPinOverlayYellow = createPinOverlay( R.drawable.marker_yellow );		
		mPinOverlayGps = createPinOverlay( R.drawable.gps_blue_dot );

		mPopupOverlay = new PopupOverlay() {
			@Override
			public void onTap( OverlayItem item ) {
				startNode( item.getSnippet() );
			}
		};

		mMapView.getOverlays().add( mPopupOverlay );

		mPinOverlayAqua.setOnFocusChangeListener( mPopupOverlay );						mPinOverlayBlack.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayBlue.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayFuchsia.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayGray.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayGreen.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayLime.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayMaroon.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayNavy.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayOlive.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayPurple.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayRed.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlaySilver.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayTeal.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayWhite.setOnFocusChangeListener( mPopupOverlay );	
		mPinOverlayYellow.setOnFocusChangeListener( mPopupOverlay );	

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
 
	/**
	 * createPinOverlay
	 * @param int id 
	 * @return PinOverlay
	 */
	private PinOverlay createPinOverlay( int id ) {
		Drawable icon = getResources().getDrawable( id );
		PinOverlay pin = new PinOverlay( icon );
		mMapView.getOverlays().add( pin );
		return pin;
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

    	// set OSM
    	if ( Constant.MAP_OSM.equals( map ) ) {
			mMapView.setMapType( MapView.MapTypeOSM );
			return false;

	    // set Yahoo
    	} else if ( Constant.MAP_YAHOO.equals( map ) ) {
			mMapView.setMapType( MapView.MapTypeStandard ); 
			return false;	
		}

    	// map change
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

		mPinOverlayAqua.clearPoint();
		mPinOverlayBlack.clearPoint();
		mPinOverlayBlue.clearPoint();
		mPinOverlayFuchsia.clearPoint();
		mPinOverlayGray.clearPoint();
		mPinOverlayGreen.clearPoint();
		mPinOverlayLime.clearPoint();
		mPinOverlayMaroon.clearPoint();
		mPinOverlayNavy.clearPoint();
		mPinOverlayOlive.clearPoint();
		mPinOverlayPurple.clearPoint();
		mPinOverlayRed.clearPoint();
		mPinOverlaySilver.clearPoint();
		mPinOverlayTeal.clearPoint();
		mPinOverlayWhite.clearPoint();
		mPinOverlayYellow.clearPoint();
					
        for ( int i=0; i<list.size(); i++ ) {
        	addPoint( list.get( i ) );
		}

		// redraw map, In order to show markers
		redrawMap();	
		log_d( "showMarker end" );
	}

    /**
     * redrawMap
     */
	private void redrawMap() {
		// set center as same place  
		mMapController.setCenter( mMapView.getMapCenter() );
	}
		
    /**
     * addPoint
     * @param NodeRecord r
     */	
	private void addPoint( NodeRecord r ) {
		String color = r.map_color;
		GeoPoint point = new GeoPoint( r.map_lat, r.map_lng );
		String title = r.getLabeleJa();
		String snippet = r.node;
		if ( color == "aqua" )	{
			mPinOverlayAqua.addPoint( point, title, snippet );	
		} else if ( color == "black" )	{
			mPinOverlayBlack.addPoint( point, title, snippet );	
		} else if ( color == "blue" )	{
			mPinOverlayBlue.addPoint( point, title, snippet );	
		} else if ( color == "fuchsia" )	{
			mPinOverlayFuchsia.addPoint( point, title, snippet );	
		} else if ( color == "gray" )	{
			mPinOverlayGray.addPoint( point, title, snippet );	
		} else if ( color == "green" )	{
			mPinOverlayGreen.addPoint( point, title, snippet );	
		} else if ( color == "lime" )	{
			mPinOverlayLime.addPoint( point, title, snippet );	
		} else if ( color == "maroon" )	{
			mPinOverlayMaroon.addPoint( point, title, snippet );	
		} else if ( color == "navy" )	{
			mPinOverlayNavy.addPoint( point, title, snippet );	
		} else if ( color == "olive" )	{
			mPinOverlayOlive.addPoint( point, title, snippet );	
		} else if ( color == "purple" )	{
			mPinOverlayPurple.addPoint( point, title, snippet );	
		} else if ( color == "red" )	{
			mPinOverlayRed.addPoint( point, title, snippet );	
		} else if ( color == "silver" )	{
			mPinOverlaySilver.addPoint( point, title, snippet );	
		} else if ( color == "teal" )	{
			mPinOverlayTeal.addPoint( point, title, snippet );	
		} else if ( color == "yellow" )	{
			mPinOverlayYellow.addPoint( point, title, snippet );	
		} else	{
			mPinOverlayWhite.addPoint( point, title, snippet );	
		}
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
		GeoPoint geo = new GeoPoint( point.lat, point.lng );
		mPinOverlayGps.addPoint( geo, null );
		redrawMap();
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
		mMapController.setZoom( Constant.GEO_ZOOM_YAHOO ); 
		toast_show( R.string.search_found );
	}
// --- Search end ---

// --- Dialog ---
	private void showOptionDialog() {
		mOptionDialog = new OptionDialog( this );
		mOptionDialog.setHandler( msgHandler );
		mOptionDialog.create();
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

	/**
	 * startNode
	 */
    private void startNode( String url ) {
    	if (( url == null )|| "".equals( url ) ) return;
		Intent intent = new Intent( this, NodeActivity.class );
		intent.putExtra( Constant.EXTRA_NODE_URL, url );
		startActivityForResult( intent, Constant.REQUEST_NODE );    
	}
	
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
