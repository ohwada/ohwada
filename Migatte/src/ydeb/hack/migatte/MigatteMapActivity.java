package ydeb.hack.migatte;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import ydeb.hack.migatte.CommonMapActivity;
import ydeb.hack.migatte.util.LocationSerializer;
import ydeb.hack.migatte.util.Constant;

/**
 * 地図を表示する
 */
public class MigatteMapActivity
	extends CommonMapActivity 
	implements OnClickListener, android.view.GestureDetector.OnGestureListener {

	/** TAG */
	private static final String TAG = "MigatteMapActivity";

	/**
	 * 最初は、
 	 * 地図を表示する
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/* 呼出し側の位置情報を取得する */
		String[] loc = getLocationParam();

	 	/* 地図を表示する */
		execCreate( loc[0], loc[1], loc[2] );

		/* 呼出し側の位置情報が取得できれば、メッセージを表示する */
		int id = 0;
		if ( loc[3].equals("OK") ) {
			id = R.string.map_message ;

		/* できなければ、その旨を表示する */
		} else {
			id = R.string.map_no_location ;
		}
		showToast( id );
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

   		String loc = LocationSerializer.serialize(
			keyword, str[0], str[1] );
		setLocationSerial(loc);

		/* SearchActivity を起動する */
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra(Constant.EXTRA_NAME_LOCATION, loc);
		startActivityForResult(i, Constant.REQUEST_CODE_SEARCH);
	}

	/** 
	 * 検索中画面から戻ってきたときの処理
	 * @param requestCode 起動したアクテビティの番号
	 * @param resultCode  アクテビティからの戻り値
	 * @param intent 親のインテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		/* 位置情報の返送 */
        Intent i = new Intent();
		i.putExtra(Constant.EXTRA_NAME_SUB_LOCATION, getLocationSerial() );

		/* 検索中画面からのコードをそのまま使う */
		setResult( resultCode, i ); 

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