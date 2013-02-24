package jp.ohwada.android.osm2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.osm2.task.NodeRecord;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

/**
 * set up the marker list of a map 
 */
public class MarkerItemizedOverlay extends ItemizedOverlay<MarkerOverlayItem> {
	
	// object
	private Activity mActivity;
	private Context mContext;
	private Resources mResources;
        
	// list of MarkerOverlayItem
    private List<MarkerOverlayItem> items = new ArrayList<MarkerOverlayItem>();    

	// variable
	private Map<String, Drawable> mHashMarker = null;
	private Drawable mMarkerAqua = null;
	private Drawable mMarkerBlack = null;
	private Drawable mMarkerBlue = null;
	private Drawable mMarkerFuchsia = null;
	private Drawable mMarkerGray = null;
	private Drawable mMarkerGreen = null;
	private Drawable mMarkerLime = null;
	private Drawable mMarkerMaroon = null;
	private Drawable mMarkerNavy = null;
	private Drawable mMarkerOlive = null;
	private Drawable mMarkerPurple = null;
	private Drawable mMarkerRed = null;
	private Drawable mMarkerSilver = null;
	private Drawable mMarkerTeal = null;
	private Drawable mMarkerWhite = null;
	private Drawable mMarkerYellow = null;
		
	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param Drawable marker
	 */
    public MarkerItemizedOverlay( Activity activity, Drawable marker ) {
        super( boundCenterBottom( marker ) );
        mActivity = activity;
        mContext = activity; 
    	mResources = mContext.getResources();
        initMarker();
        populate();
    }

	/**
	 * initMarker
	 */
	private void initMarker() {
       	mMarkerAqua = createMarker( R.drawable.marker_aqua );
       	mMarkerBlack = createMarker( R.drawable.marker_black );
       	mMarkerBlue = createMarker( R.drawable.marker_blue );
       	mMarkerFuchsia = createMarker( R.drawable.marker_fuchsia );
       	mMarkerGray = createMarker( R.drawable.marker_gray );
       	mMarkerGreen = createMarker( R.drawable.marker_green );
       	mMarkerLime = createMarker( R.drawable.marker_lime );
       	mMarkerMaroon = createMarker( R.drawable.marker_maroon );
       	mMarkerNavy = createMarker( R.drawable.marker_navy );
       	mMarkerOlive = createMarker( R.drawable.marker_olive );
       	mMarkerPurple = createMarker( R.drawable.marker_purple );
       	mMarkerRed = createMarker( R.drawable.marker_red );
       	mMarkerSilver = createMarker( R.drawable.marker_silver );
       	mMarkerTeal = createMarker( R.drawable.marker_teal ); 
       	mMarkerWhite = createMarker( R.drawable.marker_white );
       	mMarkerYellow = createMarker( R.drawable.marker_yellow );

		Map<String, Drawable> hash = new HashMap<String, Drawable>();
		hash.put( "aqua", mMarkerAqua );
		hash.put( "black", mMarkerBlack );
		hash.put( "blue", mMarkerBlue );
		hash.put( "fuchsia", mMarkerFuchsia );
		hash.put( "gray", mMarkerGray );
		hash.put( "green", mMarkerGreen );
		hash.put( "lime", mMarkerLime );
		hash.put( "maroon", mMarkerMaroon );
		hash.put( "navy", mMarkerNavy );
		hash.put( "olive", mMarkerOlive );
		hash.put( "purple", mMarkerPurple );
		hash.put( "red", mMarkerRed );
		hash.put( "silver", mMarkerSilver );
		hash.put( "teal", mMarkerTeal );
		hash.put( "white", mMarkerWhite );
		hash.put( "yellow", mMarkerYellow );
		mHashMarker = hash;
	}

	/**
	 * createMarker
	 * @param int id 
	 * @return Drawable
	 */
	private Drawable createMarker( int id ) {
       	return boundCenterBottom( mResources.getDrawable( id ) );
    }
       	
	/**
	 * --- create the actual Items ---
	 * @param int : The number of a point 
	 */
	@Override
    protected MarkerOverlayItem createItem( int index ) {
    	if (( index < 0 )||( index >= items.size() )) return null;
    	return items.get( index );
    }

	/**
	 * --- size ---
	 * @return int : the number of points
	 */
    @Override
    public int size() {
		return items.size();
    }
    
	/**
	 * --- onTap ---
	 * @param int index 
	 * @return boolean
	 */
	@Override
	protected boolean onTap( int index ) {
		if (( index < 0 )||( index >= items.size() )) return true;
		MarkerOverlayItem item = items.get( index );
		MarkerDialog dialog = new MarkerDialog( mActivity );		
		dialog.setCustomTitle( item.getTitle() );
		dialog.setMessage( item.getSnippet()  );
		dialog.setUrl( item.getUrl() );
		dialog.create();
		dialog.show();
    	return true;
	}
      			
	/**
	 * add point
	 * @param NodeRecord r
	 */
    public void addPoint( NodeRecord r ) {
    	addPoint( getMarker( r ) );
    }
    
	/**
	 * add point
	 * @param MarkerOverlayItem item
	 */
    public void addPoint( MarkerOverlayItem item ) {
		items.add( item );
        populate();
    }

	/**
	 * getMarker
	 * @param NodeRecord r
	 * @return MarkerOverlayItem item
	 */
	private MarkerOverlayItem getMarker( NodeRecord r ) {
		// get marker from color	
		Drawable marker = mMarkerWhite;
        if ( mHashMarker.containsKey( r.map_color ) ) {
        	marker = mHashMarker.get( r.map_color );
        }
		// create MarkerOverlayItem
		GeoPoint point = new GeoPoint( r.map_lat, r.map_lng );
		String label_ja = r.getLabeleJa();
		String snippet = r.getMarkerSnippet();
    	MarkerOverlayItem item = new MarkerOverlayItem( point, label_ja, snippet );
    	item.setMarker( marker );
    	item.setUrl( r.node );
    	return item;
	}
	    
	/**
	 * clear all points
	 */
    public void clearPoints() {
		items = new ArrayList<MarkerOverlayItem>();
        populate();
    }

}
