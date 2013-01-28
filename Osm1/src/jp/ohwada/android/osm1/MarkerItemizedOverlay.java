package jp.ohwada.android.osm1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.osm1.task.NodeRecord;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * set up the marker list of a map 
 * https://developers.google.com/maps/documentation/android/v1/reference/com/google/android/maps/ItemizedOverlay
 */
public class MarkerItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	// object
	private Activity mActivity;
	private Context mContext;
	private Resources mResources;
        
	// list of OverlayItem
    private List<OverlayItem> items = new ArrayList<OverlayItem>();    
    private OverlayItem mItem = null;

	// variable
	private Map<String, Drawable> mHashMarker = null;
	private Drawable mMarkerAqua = null;
	private Drawable mMarkerBlue = null;
	private Drawable mMarkerGray = null;
	private Drawable mMarkerGreen = null;
	private Drawable mMarkerMaroon = null;
	private Drawable mMarkerPink = null;
	private Drawable mMarkerPurple = null;
	private Drawable mMarkerRed = null;
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
       	mMarkerBlue = createMarker( R.drawable.marker_blue );
       	mMarkerGray = createMarker( R.drawable.marker_gray );
       	mMarkerGreen = createMarker( R.drawable.marker_green );
       	mMarkerMaroon = createMarker( R.drawable.marker_maroon );
       	mMarkerPink = createMarker( R.drawable.marker_pink );
       	mMarkerPurple = createMarker( R.drawable.marker_purple );
       	mMarkerRed = createMarker( R.drawable.marker_red );
       	mMarkerWhite = createMarker( R.drawable.marker_white );
       	mMarkerYellow = createMarker( R.drawable.marker_yellow );

		Map<String, Drawable> hash = new HashMap<String, Drawable>();
		hash.put( "aqua", mMarkerAqua );
		hash.put( "blue", mMarkerBlue );
		hash.put( "gray", mMarkerGray );
		hash.put( "green", mMarkerGreen );
		hash.put( "maroon", mMarkerMaroon );
		hash.put( "pink", mMarkerPink );
		hash.put( "purple", mMarkerPurple );
		hash.put( "red", mMarkerRed );
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
    protected OverlayItem createItem( int index ) {
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
		mItem = items.get( index );
		MarkerDialog dialog = new MarkerDialog( mActivity );		
		dialog.setCustomTitle( mItem.getTitle() );
		dialog.setMessage( mItem.getSnippet()  );
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
	 * @param OverlayItem item
	 */
    public void addPoint( OverlayItem item ) {
		items.add( item );
        populate();
    }

	/**
	 * getMarker
	 * @param NodeRecord r
	 * @return OverlayItem item
	 */
	private OverlayItem getMarker( NodeRecord r ) {
		// get marker from color	
		Drawable marker = mMarkerWhite;
        if ( mHashMarker.containsKey( r.map_color ) ) {
        	marker = mHashMarker.get( r.map_color );
        }
		// create OverlayItem
		GeoPoint point = new GeoPoint( r.map_lat, r.map_lng );
		String label_ja = r.getLabeleJa();
		String direct_label_ja = r.getDirectLabeleJa();
    	OverlayItem item = new OverlayItem( point, label_ja, direct_label_ja );
    	item.setMarker( marker );
    	return item;
	}
    
	/**
	 * clear all points
	 */
    public void clearPoints() {
		items = new ArrayList<OverlayItem>();
        populate();
    }

}
