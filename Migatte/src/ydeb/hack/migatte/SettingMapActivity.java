package ydeb.hack.migatte;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import ydeb.hack.migatte.CommonMapActivity;

/**
 * 地図を表示する
 */
public class SettingMapActivity
	extends CommonMapActivity 
	implements OnClickListener, android.view.GestureDetector.OnGestureListener {

	/** TAG */
	private static final String TAG = "SettingMapActivity";

	/**
	 * 最初は、
 	 * 地図を表示する
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/* プレファレンスから位置情報を取得する */
		String pref[] = getPreferencesLocation();

	 	/* 地図を表示する */
		execCreate( pref[0], pref[1], pref[2] );

		showToast( R.string.map_setting_message );
	}

	/**
	 * クリックされたときの処理
	 * @param v ビュー
	 */
    @Override
	public void onClick(View v) {
        execClick(v);
	}

	/**
	 * 取得ボタンをクリックされたときの処理
	 */
    @Override
	protected void execClickNext() {

		/* 地図の中心の緯度・経度を取得する */
		String str[] = getMapCenter();

		/* 緯度・経度を保存する */
		savePreferences( str[0], str[1], str[2] );

		showToast( R.string.map_set );

		finish();
	}

	/**
	 * タッチ・イベントを解除する
	 * @param event モーション・イベント
	 * @return 真・偽
	 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return onTouchEvent(event);
    }

	/**
	 * タッチ・イベントの処理
	 * @param event モーション・イベント
	 * @return 真・偽
	 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return execTouchEvent(event);
    }

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public boolean onFling(
		MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public void onLongPress(MotionEvent e) {
	}

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public boolean onScroll(
		MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	/**
	 * タッチ・イベントの処理
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}