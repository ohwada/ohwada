package jp.ohwada.android.yag1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

import jp.ohwada.android.yag1.task.PlaceRecord;

/**
 * set up the marker list of a map 
 */
public class MarkerItemizedOverlay extends ItemizedOverlay<MarkerOverlayItem> {

	// object
	private Activity mActivity;
        
	// list of OverlayItem
    private List<MarkerOverlayItem> items = new ArrayList<MarkerOverlayItem>();    
		
	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param Drawable marker
	 */
    public MarkerItemizedOverlay( Activity activity, Drawable marker ) {
        super( boundCenterBottom( marker ) );
        mActivity = activity;
        populate();
    }

	/**
	 * --- create the actual Items ---
	 * @param int : The number of a point 
	 */
	@Override
    protected MarkerOverlayItem createItem( int index ) {
        if (( index < 0 )||( index >= items.size() )) return null;
    	MarkerOverlayItem item = items.get( index );
        return new MarkerOverlayItem( 
        	item.getPoint(), item.getTitle(), item.getSnippet(), item.getRecord() );
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
		dialog.setMessage( item.getSnippet() );
		dialog.setRecord( item.getRecord() );
		dialog.create();
		dialog.show();
    	return true;
	}
      			
	/**
	 * add point
	 * @param PlaceRecord record
	 */
    public void addPoint( PlaceRecord r ) {
		GeoPoint point = new GeoPoint( r.map_lat, r.map_lng );
		addPoint( new MarkerOverlayItem( point, r.label, r.address, r ) );
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
	 * clear all points
	 */
    public void clearPoints() {
		items.clear();
        populate();
    }
}
