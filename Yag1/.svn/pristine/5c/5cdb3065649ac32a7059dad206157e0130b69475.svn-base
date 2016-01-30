package jp.ohwada.android.yag1;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import jp.ohwada.android.yag1.task.PlaceRecord;

/**
 * set up the marker of a map
 */
public class MarkerOverlayItem extends OverlayItem {

	// variable
	private PlaceRecord mPlaceRecord = null;

	/**
	 * === Constructor ===
	 * @param GeoPoint point
 	 * @param String title
	 * @param String snippet
	 * @param PlaceRecord record
	 */
    public MarkerOverlayItem( GeoPoint point, String title, String snippet, PlaceRecord record ) {
        super( point, title, snippet );
        mPlaceRecord = record ;
    }

	/**
	 * getRecord
	 * @return PlaceRecord
	 */    
	public PlaceRecord getRecord() {
		return mPlaceRecord;
	}
}
