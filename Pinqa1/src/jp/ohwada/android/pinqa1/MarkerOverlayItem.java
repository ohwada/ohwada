package jp.ohwada.android.pinqa1;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * set up the marker of a map
 * https://developers.google.com/maps/documentation/android/v1/reference/com/google/android/maps/OverlayItem
 */
public class MarkerOverlayItem extends OverlayItem {

	// variable
	private String mUrl = "";
	private int mId = 0;
	
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
	
		/**
	 * setUrl
	 * @return String
	 */    
	public void setId( int id ) {
		mId = id;
	}
	
	/**
	 * getUrl
	 * @return String
	 */    
	public int getId() {
		return mId;
	}
}
