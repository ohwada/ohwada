package ydeb.hack.migatte.util;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * 地図のマーカーを設定する
 * http://code.google.com/intl/ja/android/add-ons/google-apis/reference/
 */
public class MarkerOverlayItem extends OverlayItem {

	/**
	 * コンストラクタ
	 * @param point ジオポイント
	 */
    public MarkerOverlayItem(GeoPoint point){
        super(point, "", "");
    }
}
