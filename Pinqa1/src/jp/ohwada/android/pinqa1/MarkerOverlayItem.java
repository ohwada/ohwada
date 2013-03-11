package jp.ohwada.android.pinqa1;

import jp.ohwada.android.pinqa1.task.ArticleRecord;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * set up the marker of a map
 */
public class MarkerOverlayItem extends OverlayItem {

	// variable
	private ArticleRecord mArticleRecord = null;
		
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
	 * setRecord
	 * @param ArticleRecord
	 */    
	public void setRecord( ArticleRecord record ) {
		mArticleRecord = record;
	}
	
	/**
	 * getRecord
	 * @return ArticleRecord
	 */    
	public ArticleRecord getRecord() {
		return mArticleRecord;
	}

}
