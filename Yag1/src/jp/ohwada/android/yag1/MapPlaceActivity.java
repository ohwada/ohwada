package jp.ohwada.android.yag1;

import jp.ohwada.android.yag1.task.PlaceListFile;
import jp.ohwada.android.yag1.task.PlaceRecord;
import jp.ohwada.android.yag1.task.PlaceList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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
    	View view = getLayoutInflater().inflate( R.layout.activity_place_map, null );
		setContentView( view ); 

		// view object
		createMenu( view );
		createMap();
		mMenuView.enableEvent();
		mMenuView.enablePlace();
			
		// view conponent
		mTextViewTitle = (TextView) findViewById( R.id.map_textview_title );
            	
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
		mErrorView.hideText();
		mTextViewTitle.setText( record.label );
  			
		// map
		ｍGeoPointPlace = new GeoPoint( record.map_lat, record.map_lng );
    	setCenter( ｍGeoPointPlace );
        mMarkerOverlay.addPoint( record );
	}
	
// --- Dialog ---
	protected void showDialog() {
		MapPlaceDialog dialog = new MapPlaceDialog( this );
		dialog.setHandler( msgHandler );
		dialog.create();
		dialog.show();
	}
// --- Dialog end ---

}
