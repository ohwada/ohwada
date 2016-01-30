package jp.ohwada.android.osm2;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * set up the marker of a map
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
    public MarkerOverlayItem( GeoPoint point, String title, String snippet ) {
        super( point, title, snippet );
    }

	/**
	 * setUrl
	 * @return String
	 */    
	public void setUrl( String url ) {
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
