package jp.ohwada.android.yag1;

import jp.ohwada.android.yag1.task.PlaceList;
import jp.ohwada.android.yag1.task.PlaceListFile;
import jp.ohwada.android.yag1.task.PlaceRecord;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

/*
 * Map Place Activity
 */
public class MapPlaceActivity extends MapCommonActivity {
   	
	// view conponent
	private TextView mTextViewTitle;
	private MarkerItemizedOverlay mMarkerOverlay;
						
	/*
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 */
	@Override 
	public void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );

		// main view
		LinearLayout ll_root = createViewRoot(); 
		View view_header = createViewHeader( R.layout.map_place_header ); 
		mTextViewTitle = (TextView) view_header.findViewById( R.id.map_textview_title );
		ll_root.addView( view_header );
		createViewMap();
		ll_root.addView( mMapView );
		setContentView( ll_root );
		createObject();
		TAG_SUB = "MapPlaceActivity";

		// get record
		Intent intent = getIntent();
		String url = intent.getStringExtra( Constant.EXTRA_PLACE_URL );
		if ( "".equals( url ) ) {
			mErrorView.showNotSpecifyPlace();
			return;
		}

    	// marker
       	Activity activty = this;
       	Drawable marker = getResources().getDrawable( R.drawable.marker_default );
    	mMarkerOverlay = new MarkerItemizedOverlay( activty, marker );
        mMapView.getOverlays().add( mMarkerOverlay );
        
		// get file
		PlaceListFile file = new PlaceListFile(); 
		PlaceList list = file.read();
		PlaceRecord record = list.getPlace( url );
		showMap( record );
	}

    /**
     * showMap
     * @param PlaceRecord record 
     */		
	private void showMap( PlaceRecord record ) {        
		if ( record == null ) {
			mErrorView.showNoPlace();
			return;
		} 

		// title
		ｍPlaceRecord = record;
		mErrorView.hideText();
		mTextViewTitle.setText( record.label );
  			
		// map
    	setCenter( new GeoPoint( record.map_lat, record.map_lng ) );
        mMarkerOverlay.addPoint( record );
	}

// --- Dialog ---
	protected void showOptionDialog() {
		MapPlaceDialog dialog = new MapPlaceDialog( this );
		dialog.setHandler( msgHandler );
		dialog.create();
		dialog.show();
	}
// --- Dialog end ---

// --- Message Handler ---  
	/**
	 * execHandlerMapApp
	 */
	protected void execHandlerMapApp() {
	 	startMapApp( ｍPlaceRecord );
	} 
	
	/**
	 * execHandlerMapNavicon
	 */
	protected void execHandlerMapNavicon() {
	 	startMapNavicon( ｍPlaceRecord );
	} 
// --- Message Handler end ---
	
}
