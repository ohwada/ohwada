package ydeb.hack.migatte.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import ydeb.hack.migatte.util.MarkerOverlayItem;

/**
 * 地図のマーカーの配列を設定する
 * http://code.google.com/intl/ja/android/add-ons/google-apis/reference/
 */
public class MarkerItemizedOverlay extends ItemizedOverlay<MarkerOverlayItem> {

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

	/**
	 * コンストラクタ
	 * @param marker マーカーの画像
	 */
    public MarkerItemizedOverlay( Drawable marker ) {
        super( boundCenterBottom( marker ) );
    }

	/**
	 * ポイントを生成する
	 * @param i ポイントの番号
	 */
	@Override
    protected MarkerOverlayItem createItem(int i) {
        GeoPoint point = points.get(i);
        return new MarkerOverlayItem(point);
    }

	/**
	 * ポイントの数を返す
	 * @return ポイントの数
	 */
    @Override
    public int size() {
        return points.size();
    }

	/**
	 * ポイントを設定する
	 * @param point ジオポイント
	 */
    public void addPoint(GeoPoint point) {
        this.points.add(point);
        populate();
    }

	/**
	 * 全て野のポイントをクリアする
	 */
    public void clearPoint() {
        this.points.clear();
        populate();
    }
}
