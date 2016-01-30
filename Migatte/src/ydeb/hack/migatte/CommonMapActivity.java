package ydeb.hack.migatte;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import ydeb.hack.migatte.util.MarkerItemizedOverlay;
import ydeb.hack.migatte.util.LocationSerializer;
import ydeb.hack.migatte.util.Constant;

/**
 * 地図を表示する
 * http://code.google.com/intl/ja/android/add-ons/google-apis/reference/index.html
 */
public class CommonMapActivity
	extends MapActivity implements OnGestureListener, OnClickListener {

	/** TAG */
	private static final String TAG = "CommonMapActivity";

	/** マップビュー */
	private MapView map;

	/** デフォルトのズームサイズ */
	private static int map_zoom = 15;

	/** アドレス入力 */
	private EditText editAddress;

	/** 検索ボタン */
	private Button buttonSearch;

	/** 取得ボタン */
	private Button buttonNext;

	/** ボタンのロック */
	private boolean lockButton = false;

	/** プログレス・ダイアログ */
    private ProgressDialog progressDialog;

	/** ジェスチャー */
	private GestureDetector mGDetector;

	/* プレファレンス */
	private	SharedPreferences preferences;

	/** プレファレンス名 */
	private static final String PREFERENCES_NAME = "migatte";

	/** ジオコーダー */
	private Geocoder geocoder;

	/** 地名から検索する候補数 */
	private static final int MAX_BY_NAME = 1;

	/** 地名から検索するときのリトライ回数 */
	private static final int MAX_RETRY = 3;

	/** マーカー */
    private MarkerItemizedOverlay markerOverlay;

	/** ソフトキーボードのパラメータ */
	private static final int INPUT_METHOD_FLAGS = 0;

	/** キーワード */
	protected String keyword;

	/** シリアライズされた位置情報 */
	private String locationSerial = "";

	/**
	 * クリックされたときの処理
	 * @param v ビュー
	 */
	@Override
	public void onClick(View v) {
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
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
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
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
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

	/**
	 * ルート情報を表示しない
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Create の処理
 	 * 地図を表示する
 	 * @param latitude  緯度
 	 * @param longitude 経度
 	 * @param zoom      ズーム
	 */
    protected void execCreate( String latitude, String longitude, String zoom ) {
        setContentView(R.layout.map);

		/* ジェスチャー */
        mGDetector = new GestureDetector(this, this);

		map_zoom = Integer.parseInt(zoom);

		GeoPoint point = new GeoPoint( 
			strToE6( latitude ), strToE6( longitude ) );

		/* マップビュー */
		map = (MapView) findViewById(R.id.map_mapview);
        map.setBuiltInZoomControls(true);
		map.getController().setCenter(point);
		map.getController().setZoom( map_zoom );

		/* マーカー */
        Drawable Marker = getResources().getDrawable( R.drawable.marker );
		markerOverlay = new MarkerItemizedOverlay( Marker );
		map.getOverlays().add( markerOverlay );
		setCenterMarker();

		/* ジオコーダー */
		geocoder = new Geocoder( getBaseContext() ); 

		/* アドレス入力 */
		editAddress = (EditText) findViewById(R.id.map_edittext_address);

		/* 検索ボタン */
		buttonSearch = (Button) findViewById(R.id.map_button_search);
		buttonSearch.setOnClickListener( this );

		/* 取得ボタン */
		buttonNext = (Button) findViewById(R.id.map_button_next);
		buttonNext.setOnClickListener( this );

		/* プログレス・ダイアログ */
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

		lockButton = false;
	}

	/**
	 * 呼出し側の位置情報を取得する
	 */
    protected String[] getLocationParam() {

		String keyword   = "";
		String latitude  = "";
		String longitude = "";
		String flag      = "";

		/* 呼出し側のパラメータを取得する */
        String locationSerial = getIntent().getStringExtra(Constant.EXTRA_NAME_LOCATION);

		/* パラメータが取得できたときは */
		if ( locationSerial != null ) {
			setLocationSerial(locationSerial);
	        List<String> list = LocationSerializer.deserialize( locationSerial );

			/* パラメータが取得できたときは */
			if ( list != null ) {
				keyword   = list.get(0);
				latitude  = list.get(1);
				longitude = list.get(2);
			}
		}

		setKeyword( keyword );

		/* プレファレンスから位置情報を取得する */
		String pref[] = getPreferencesLocation();
		String pref_latitude  = pref[0];
		String pref_longitude = pref[1];
		String pref_zoom      = pref[2];

		/* 呼出し側の位置情報を取得できたときは、フラグをセットする */
		if (( latitude != null) && !latitude.equals("") ) {
			flag = "OK";	// OK

		/* できないときは、デフォルト値を設定する */
		} else {
			latitude  = pref_latitude;
			longitude = pref_longitude;
		}

		String[] str = new String[4];
		str[0] = latitude;
		str[1] = longitude;
		str[2] = pref_zoom;
		str[3] = flag;
		return str;
	}

	/**
	 * 地図の中心にマーカーを表示する
	 * @param v ビュー
	 */
    private void setCenterMarker() {
		/* マーカーをクリアする */
		markerOverlay.clearPoint();

		/* 中心の緯度・経度を取得する */
		GeoPoint center = map.getMapCenter();

		/* マーカーを追加する */
		markerOverlay.addPoint( center );
  	}

	/**
	 * クリックされたときの処理
	 * @param v ビュー
	 */
	protected void execClick(View v) {

		/* ロックされていなければ */
        if ( !lockButton ) {
			/* ロックする */
			lockButton = true;

			/* 検索ボタンなら */
    	    if (v == buttonSearch) {
				execClickSearch(v);

			/* 取得ボタンなら */
        	} else if (v == buttonNext) {
            	execClickNext();
			}

			/* ロックを解除する */
			lockButton = false;
		}
	}

	/**
	 * 検索ボタンをクリックされたときの処理
	 * @param v ビュー
	 */
	private void execClickSearch(View v) {

		/* アドレス入力の値を取得する */
		String location = editAddress.getText().toString();

		/* 地名が入力されていなければ */
		if ( location.length() == 0 ) {
			return;
		}
		if ( location.equals("") ) {
			return;
		}

		/* ソフトキーボードを非表示にする */
		hideInputMethod(v);

		/* 
		 * プログレス・ダイアログを表示する
		 * これが表示されるよりも、ジオコードの応答の方が早いようで、
		 * 表示されていないように見える 
		*/
		showProgress( getString(R.string.map_searching) );
                                
		/* 地名から緯度・経度を取得する */
		List<Address> list = getLocationAddressRetry(location);

	 	/* プログレス・ダイアログを非表示にする */
		hideProgress();

		/* 緯度・経度が取得できなければ */
		if (( list == null ) || list.isEmpty() ) {
			showToast( R.string.map_not_found );

		/* 緯度・経度が取得できたら */
		} else {
			Address address  = list.get(0);
			GeoPoint point = new GeoPoint( 
				doubleToE6( address.getLatitude() ), 
				doubleToE6( address.getLongitude() ) );

			/* 地図の中心に設定する */
			map.getController().setCenter(point);
			map.getController().setZoom( map_zoom );
			setCenterMarker();

			showToast( R.string.map_found );
		}
	}

	/**
	 * ソフトキーボードを非表示にする
	 * @param v ビュー
	 */
	private void hideInputMethod(View v) {
        InputMethodManager input = (InputMethodManager)
        	getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), INPUT_METHOD_FLAGS); 
	}

	/**
	 * <pre>
	 * 地名から緯度・経度を取得する
	 * 取得できるまで、３回繰り返す
	 * たまに取得できないことがあるため
	 * </pre>
	 * @param location 地名
	 * @return 緯度・経度のリスト
	 */
	private List<Address> getLocationAddressRetry(String location) {

		List<Address> list = null;

		/* 緯度・経度が取得できるまで、３回繰り返す */
		for ( int i=0; i < MAX_RETRY; i ++ ) {
			list = getLocationAddress(location);

			/* 緯度・経度が取得できなければ */
			if (( list != null ) && !list.isEmpty() ) {
				break;
			}
		}

		return list;
	}

	/**
	 * 地名から緯度・経度を取得する
	 * @param location 地名
	 * @return 緯度・経度のリスト
	 */
	private List<Address> getLocationAddress(String location) {

		List<Address> list = null;
		try {
			/* 地名から緯度・経度を取得する */
			list = geocoder.getFromLocationName( location, MAX_BY_NAME );

		} catch (IOException e) {
//			e.printStackTrace();
		}

		return list;
	}

	/**
	 * プログレス・ダイアログを表示する
	 */
	private void showProgress( String message ) {
        progressDialog.setMessage(message);
        progressDialog.show();
	}

	/**
	 * プログレス・ダイアログを非表示にする
	 */
	private void hideProgress() {
        progressDialog.dismiss();
	}

	/**
	 * ポップアップを表示する
	 * @param id リソースID
	 */
	protected void showToast( int id ) {
		showToast( getString(id), Toast.LENGTH_SHORT );
	}

	/**
	 * ポップアップを表示する
	 * @param text メッセージ
	 */
	protected void showToast( String text ) {
		showToast( text, Toast.LENGTH_SHORT );
	}

	/**
	 * ポップアップを表示する
	 * @param text メッセージ
	 * @param duration 表示する時間
	 */
	protected void showToast( String text, int duration ) {
		Toast.makeText(this, text, duration).show();
	}

	/**
	 * ダイアログで表示する
	 * @param title タイトル
	 * @param message 本文
	 * @param vbutton ボタン
	 */
	protected void showDialog( int title, int message, int button ) {
		showDialog( getString(title), getString(message), getString(button) );
	}

	/**
	 * ダイアログで表示する
	 * @param title タイトル
	 * @param message 本文
	 * @param vbutton ボタン
	 */
	protected void showDialog( String title, String message, String button ) {
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		ad.setTitle( title );
		ad.setMessage( message );
		ad.setNegativeButton( button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int whichButton) {
				setResult(RESULT_OK);
			}
		});
		ad.create();
		ad.show();
	}

	/**
	 * 取得ボタンをクリックされたときの処理
	 */
	protected void execClickNext() {
	}

	/**
	 * 取得ボタンをクリックされたときの処理
	 */
	protected String[] getMapCenter() {

		/* 地図の中心の緯度・経度を取得する */
    	GeoPoint point = map.getMapCenter();
		int latitude   = point.getLatitudeE6();
		int longitude  = point.getLongitudeE6();
		int zoom       = map.getZoomLevel();

		String[] str = new String[3];
		str[0] = e6ToStr(latitude);
		str[1] = e6ToStr(longitude);
		str[2] = Integer.toString(zoom);
		return str;
	}

	/**
	 * 文字列をE6形式に変換する
	 * @param str 位置情報 (少数点形式の文字列)
	 * @return 位置情報 (E6形式の整数)
	 */
    private int strToE6( String str ) {

		return doubleToE6( Double.parseDouble(str) );
	}

	/**
	 * 実数をE6形式に変換する
	 * @param d1 位置情報 (少数点形式の実数)
	 * @return 位置情報 (E6形式の整数)
	 */
    private int doubleToE6( Double d1 ) {

		Double d2 = d1 * 1E6;
		return d2.intValue();
	}

	/**
	 * E6形式を文字列に変換する
	 * @param e6 位置情報 (E6形式の整数)
	 * @return 位置情報 (少数点形式の文字列)
	 */
    private String e6ToStr( int e6 ) {

		Double d = e6 / 1E6;
		return d.toString();
	}

	/**
	 * タッチ・イベントの処理
	 * @param event モーション・イベント
	 * @return 真・偽
	 */
    protected boolean execTouchEvent(MotionEvent event) {
		boolean ret = execTouchEvent2(event);

		/* 地図の中心にマーカーを表示する */
		setCenterMarker();

        return ret;
    }

	/**
	 * タッチ・イベントの処理
	 * @param event モーション・イベント
	 * @return 真・偽
	 */
    private boolean execTouchEvent2(MotionEvent event) {
		/* ジェスチャーがあれば */
        if (mGDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

	/**
	 * キーワードを設定する
	 * @param str キーワード
	 */
	protected void setKeyword( String str ) {
		keyword = str;
	}

	/**
	 * キーワードを取得する
	 * @return キーワード
	 */
	protected String getKeyword() {
		return keyword;
	}

	/**
	 * シリアライズされた位置情報 を設定する
	 * @param str 位置情報
	 */
	protected void setLocationSerial( String str ) {
		locationSerial = str;
	}

	/**
	 * シリアライズされた位置情報 を取得する
	 * @return 位置情報
	 */
	protected String getLocationSerial() {
		return locationSerial;
	}

	/**
	 * プレファレンスから位置情報を取得する
	 * @return  緯度・経度・ズーム の配列
	 */
	protected String[] getPreferencesLocation() {
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		return getPreferences();
	}

	/**
	 * プレファレンスの初期化
	 */
	protected void initPreferences() {
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
	}

	/**
	 * プレファレンスから位置情報を保存する
	 * @param latitude  緯度
	 * @param longitude 経度
	 * @param zoom ズーム
	 */
	protected void savePreferences( String latitude, String longitude, String zoom  ) {
		preferences.edit().putString("map_latitude",  latitude  ).commit();
		preferences.edit().putString("map_longitude", longitude ).commit();
		preferences.edit().putString("map_zoom",      zoom      ).commit();
	}

	/**
	 * プレファレンスから位置情報を取得する
	 * @return  緯度・経度・ズーム の配列
	 */
	protected String[] getPreferences() {

		String[] str = new String[3];
		str[0] = preferences.getString("map_latitude",  getString(R.string.map_latitude) );
		str[1] = preferences.getString("map_longitude", getString(R.string.map_longitude) );
		str[2] = preferences.getString("map_zoom",      getString(R.string.map_zoom) );
		return str;
	}

}