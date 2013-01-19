package jp.ohwada.android.yag1;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * set up the marker of a map
 * https://developers.google.com/maps/documentation/android/v1/reference/com/google/android/maps/OverlayItem
 */
public class MarkerOverlayItem extends OverlayItem {

	// variable
	private String mUrl = "";

	/**
	 * === Constructor ===
	 * @param GeoPoint point
 	 * @param String title
	 * @param String snippet
	 */
    public MarkerOverlayItem( GeoPoint point, String title, String snippet, String url ) {
        super( point, title, snippet );
        mUrl = url;
    }

	/**
	 * getUrl
	 * @return String
	 */    
	public String getUrl() {
		return mUrl;
	}
}
