package jp.ohwada.android.osm2;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * CenterItemizedOverlay
 */
public class CenterItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	// variable
    private GeoPoint mPoint = null;    
		
	/**
	 * === Constructor ===
	 * @param Context context
	 * @param Drawable marker
	 */
    public CenterItemizedOverlay( Drawable marker ) {
        super( boundCenterBottom( marker ) );
        populate();
    }

	/**
	 * --- create the actual Items ---
	 * @param int : The number of a point 
	 */
	@Override
    protected OverlayItem createItem( int index ) {
        return new OverlayItem( mPoint, null, null );
    }

	/**
	 * --- size ---
	 * @return int : the number of points
	 */
    @Override
    public int size() {
		return mPoint == null ? 0 : 1; 
    }

	/**
	 * setPoint
	 * @param GeoPoint point
	 */
    public void setPoint( GeoPoint point ) {
		mPoint = point;
		populate();
    }

}
