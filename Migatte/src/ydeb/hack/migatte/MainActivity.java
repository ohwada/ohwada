package ydeb.hack.migatte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

import ydeb.hack.migatte.util.Constant;
import ydeb.hack.migatte.util.DebugUtils;
import ydeb.hack.migatte.util.LocationSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * 初期画面を表示する
 *
 */
public class MainActivity 
	extends Activity 
	implements LocationListener {

	/** TAG */
	private static final String TAG = "MainActivity";

	/** ホーム画面のオブジェクト */
	private Home homeRes = null;

	/** ２重タッチを防止するフラグ */
	private boolean lockLaunch;

	/** 要求したキーワード */
	private String requestKeyword;

	/** 失敗したキーワードのリスト */
	private List<String> keywordFailList = new ArrayList<String>();

	/** GPS管理 */
	private LocationManager locManager;

	/** 1回目の位置情報 */
	private	String latitude_1st  = "";
	private	String longitude_1st = "";

	/**
	 * <pre>
	 * Called when the activity is first created
	 * start() 実行開始
	 * </pre>
	 * @param savedInstanceState Bundle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /* GPS管理 */
    	locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

		start();
	}

	/**
	 * <pre>
	 * 実行開始
	 * setDisplaysize() 画面の大きさを設定する
	 * setHomeRes() ホーム画面のオブジェクトを生成する
	 * </pre>
	 * @param savedInstanceState Bundle
	 */
	public void start() {

		/* 変数の初期化 */
		clearParam();

		/* main.xml に対応したビューを取得する */
		View v = this.getLayoutInflater().inflate(R.layout.main, null);

		setDisplaysize();
		setHomeRes();

		/* 背景IDがあれば、ビューに設定する */
		int id = homeRes.getBackgroundId();
		if ( id != 0 ) {
			v.setBackgroundResource( id );
		}

		setContentView(v);
		showToast( R.string.main_message );

		/* GPS管理が取得できなければ、その旨を表示する */
       	if (locManager == null) {
    		showToast( R.string.main_no_loc_manager );

		/* GPS管理が取得できれば、位置情報を更新する */
     	} else {
			updateLocation();
		}
	}

	/**
	 * 変数の初期化
	 */
	public void clearParam() {

    	latitude_1st  = "";
    	longitude_1st = "";
		lockLaunch = false;
    	keywordFailList = new ArrayList<String>();
	}

	/**
	 * <pre>
	 * ホーム画面のオブジェクト homeRes を生成する
	 * 22日なら、SimpleHome を使用し、カレーの画像(home_curry.png)とキーワードに「カレー」を設定する。
	 * 29日なら、SimpleHome を使用し、肉の画像(home_meat.png)とキーワードに「肉」を設定する。
	 * その他なら、SelectHome を使用し、ホーム画像(home.png)を設定する。
	 * </pre>
	 */
	private void setHomeRes() {

		String[] md = getMonthDay();
		String month = md[0];
		String day   = md[1];

		/* 日付に対応したキーワードを取得する */
		String keywords = getKeywords( month, day );
		int id = getBackgroundId( month, day );

		if (keywords != null) {
			/* キーワードを取得できたときは、simple */
			homeRes = new SimpleHome();
			homeRes.addKeywords( keywords );
			homeRes.setBackgroundId( id );

		} else {
			/* 取得できないときは、select */
			homeRes = new SelectHome( width, height, getResources() );
		}

	}

	/**
	 * <pre>
	 * 日付を取得する
	 * デバックモードなら、デバック用の日付を使用する
	 * </pre>
	 */
	private String[] getMonthDay() {

		/* 日付を取得する */
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat DateFormatMonth = new SimpleDateFormat("MM");
		SimpleDateFormat DateFormatDay   = new SimpleDateFormat("dd");
		String month = DateFormatMonth.format(date);
		String day   = DateFormatDay.format(date);

	 	/* デバックモードなら、デバック用の日付を使用する */
		if (DebugUtils.isDebug()) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			String month_debug = sharedPreferences.getString("debug_month", "");
			String day_debug   = sharedPreferences.getString("debug_day", "");

	 		/* デバック用の日付が空でなければ */
			if ( day_debug.length() != 0 ) {
				month = month_debug;
				day   = day_debug;
			}
		}

		String[] s = new String[2];
		s[0] = month;
		s[1] = day;
		return s;
	}

	/**
	 * 日付に対応したキーワードを取得する
	 * @param month 月
	 * @param day   日
	 * @return キーワード
	 */
	private String getKeywords( String month, String day ) {

		/* keyword_01_02 */
		String name1 = "keyword_" + month + "_" + day;
		String keywords = getStringByName( name1 );
		if (keywords != null) {
			return keywords;
		}

		/* keyword_03 */
		String name2 = "keyword_" + day;
		return getStringByName( name2 );
	}

	/**
	 * 日付に対応した背景IDを取得する
	 * @param month 月
	 * @param day   日
	 * @return 背景ID
	 */
	private int getBackgroundId( String month, String day ) {

		/* home_01_02 */
		String name1 = "home_" + month + "_" + day;
		int id = getId(name1, "drawable");
		if (id != 0) {
			return id;
		}

		/* keyword_03 */
		String name2 = "home_" + day;
		return getId(name2, "drawable");
	}

	/**
	 * リソース名に対応した文字列を取得する
	 * @param name リソース名
	 * @return 文字列
	 */
	private String getStringByName( String name ) {
		int id = getId(name, "string");

		/* リソースIDが取得できないときは、null にする */
		if (id == 0) {
			return null;
		}
		return getString( id );
	}
	
	/**
	 * リソース名に対応したリソースIDを取得する
	 * @param name リソース名
	 * @param type リソース・タイプ
	 * @return リソースID
	 */
	private int getId( String name, String type ) {
		return getResources().getIdentifier(name, type, getPackageName());
	}

	/** 画面の幅 */
	private int width = 0;

	/** 画面の高さ */
	private int height = 0;

	/**
	 * 画面の大きさを設定する
	 */
	private void setDisplaysize() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = (int) display.getWidth();
		height = (int) display.getHeight();

	 	/* デバックモードなら、画面の大きさを表示する */
		if (DebugUtils.isDebug()) {
			String s = width + " x " + height;
			showToast( s );
		}

	}

	/**
	 * <pre>
	 * 画面にタッチすると、
	 * getKeyword() によりキーワードの配列を取得し、
	 * ランダムに１つ選ぶ
	 * ２重タッチを防止するフラグを設定する
	 * SearchActivity を起動する
	 * </pre>
	 * @param event 動作イベント
	 * @return 常に真
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(lockLaunch){
			return true;
		}

		/* ロック設定 */
		lockLaunch = true;

		/* ぐるなびに渡すキーワードを取得する */
		List<String> Keywords = homeRes.getKeyword((int) event.getX(),
				(int) event.getY());
		String keyword = getSearchKeyword(Keywords);

		/* 1回目の位置情報があれば、SearchActivity を起動する */
		if (( latitude_1st != null) && !latitude_1st.equals("") ) {
			execTouchEventSecond(keyword, latitude_1st, longitude_1st);

		/* なければ、MigatteMapActivity を起動する */
		} else {
			execTouchEventFirst(keyword);
		}

		return true;
	}

	/**
	 * <pre>
	 * GPSから緯度・経度を取得して、
	 * MigatteMapActivity を起動する
	 * </pre>
	 * @param keyword キーワード
	 */
	private void execTouchEventFirst(String keyword) {

		/* GPSから緯度・経度を取得する */
		List<String> list_loc = getLocation();
   		String latitude  = list_loc.get(0);
   		String longitude = list_loc.get(1);

		/* MigatteMapActivity を起動する */
	   	String str = LocationSerializer.serialize(keyword, latitude, longitude);
		Intent i1 = new Intent(MainActivity.this, MigatteMapActivity.class);
		i1.putExtra(Constant.EXTRA_NAME_LOCATION, str);
		startActivityForResult(i1, Constant.REQUEST_CODE_MAP);
	}

	/**
	 * SearchActivity を起動する
	 * @param keyword   キーワード
	 * @param latitude  緯度
	 * @param longitude 経度
	 */
	private void execTouchEventSecond(String keyword, String latitude, String longitude) {

		/* SearchActivity を起動する */
	   	String str = LocationSerializer.serialize(keyword, latitude, longitude);
		Intent i = new Intent(MainActivity.this, SearchActivity.class);
		i.putExtra(Constant.EXTRA_NAME_LOCATION, str);
		startActivityForResult(i, Constant.REQUEST_CODE_SEARCH);
	}

	/**
	 * ぐるなびに渡すキーワードを取得する
	 * @return キーワード
	 */
	private String getSearchKeyword(List<String> keywords) {

		/* 失敗したリストのキーワードを削除する */
		List<String> newKeywords = getRequestKeywordList( keywords );

		/* ランダムに１つ選ぶ */
		requestKeyword = getRandomKeyword(newKeywords);
		return requestKeyword;
	}

	/**
	 * キーワードをランダムに１つ選ぶ
	 * @param list キーワード・リスト
	 * @return キーワード
	 */
	private String getRandomKeyword( List<String> list ) {

		/* リストが空なら、空を返す */
		int size = list.size();
		if ( size == 0 ) {
			return "";
		}

		/* ランダムに１つ選ぶ */
		Random rnd = new Random();
		int ran = rnd.nextInt( size );
		return list.get(ran);
	}

	/**
	 * <pre>
	 * キーワード・リストから、
	 * 失敗したリストのキーワードを削除して、
	 * 新しいリストを作る
	 * </pre>
	 * @param list キーワード・リスト
	 * @return 文字列リスト
	 */
	private List<String> getRequestKeywordList(List<String> list) {

		/* リストが空ならそのまま返す */
		int size = list.size();
		if ( size == 0 ) {
			return list;
		}

		/* 失敗したリストが空ならそのまま返す */
		int sizeFail = keywordFailList.size();
		if ( sizeFail == 0 ) {
			return list;
		}

		/* 失敗したリストのキーワードを削除して、新しいリストを作る */
		List<String> ret = new ArrayList<String>();
		String s;
		for (int i=0; i<list.size(); i++) {  
			s = list.get(i).toString();
			if ( ! inKeywordFailList(s) ) {
				ret.add(s);
//				Log.d(TAG, "keyword=" + s);
			}
		}
		return ret;
 	}

	/**
	 * <pre>
	 * キーワードが失敗したリストにあれば、true を返す
	 * なければ、false を返す
	 * </pre>
	 * @param str キーワード
	 * @return true / false
	 */
	private boolean inKeywordFailList(String str) {

		String s;
		for (int i=0; i<keywordFailList.size(); i++) {  
			s = keywordFailList.get(i);
			if ( str.equals(s) ) {
				return true;
			}
		}
		return false;
 	}

	/**
	 * ポップアップを表示する
	 * @param id リソースID
	 */
	private void showToast( int id ) {
		showToast( getString(id) );
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
	 * 詳細画面から戻ってきたときは、
	 * ２重タッチを防止するフラグを解除する
	 * @param requestCode 起動したアクテビティの番号
	 * @param resultCode  アクテビティからの戻り値
	 * @param intent 親のインテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, intent);  

		/* 失敗画面からの戻りを示すフラグ */
		boolean flag_fail = false;

		/* 地図画面からの戻りであれば */
		if (requestCode == Constant.REQUEST_CODE_MAP) { 

			/* 失敗画面からの戻りであれば */
        	if (resultCode == RESULT_CANCELED ) { 
				flag_fail = true;
			}

			/* 1回目の位置情報がなければ、戻り値を設定する */
			if (( latitude_1st == null) || latitude_1st.equals("") ) {
				if ( intent != null ) {
					String str = intent.getStringExtra(Constant.EXTRA_NAME_SUB_LOCATION);
    	    		List<String> list = LocationSerializer.deserialize( str );
					latitude_1st  = list.get(1);
					longitude_1st = list.get(2);
				}
			}

		/* 検索画面からの戻りであれば */
		} else if (requestCode == Constant.REQUEST_CODE_SEARCH) { 

			/* 失敗画面からの戻りであれば */
        	if (resultCode == RESULT_CANCELED ) { 
				flag_fail = true;
			}

		/* 地図設定画面からの戻りであれば */
		} else if (requestCode == Constant.REQUEST_CODE_MENU_MAP) { 
			/* 変数の初期化 */
			clearParam();

		/* デバック設定画面からの戻りであれば */
		} else if (requestCode == Constant.REQUEST_CODE_MENU_DEBUG) { 
			/* 開始 */
			start();
		}

		/* 失敗画面からの戻りであれば、失敗したキーワードに追加する */
		if ( flag_fail ) {
			keywordFailList.add( requestKeyword );
		}

		/* ロック解除 */
		lockLaunch = false;
	}

	/** 
	 * メニュー画面を生成する
	 * @param menu メニュー
	 * @return 真・偽
	 */
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {

		super.onCreateOptionsMenu(menu);

		int id;

	 	/* デバックモードなら、デバック用画面を表示する */
		if (DebugUtils.isDebug()) {
			id = R.menu.debug;

	 	/* そうでなければ、通常のメニュー画面を表示する */
		} else {
			id = R.menu.menu;
		}

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(id, menu);
		return true;
	}

	/** 
	 * メニュー画面がクリックされたときの処理
	 * @param item メニュー項目
	 * @return 真・偽
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(lockLaunch){
			return true;
		}

		/* ロック設定 */
		lockLaunch = true;

		switch (item.getItemId()) {
		 	/* 開始なら、実行開始する */
			case R.id.menu_start:
				/* == TODO 再開はこれでいいか == */
			 	/* デバックモードなら、開始する */
				if (DebugUtils.isDebug()) {
					start();
				/* 通常なら、変数の初期化 */
				} else {
					clearParam();
					showToast( R.string.menu_start_title );
				}
				return true;

		 	/* 地図なら、地図設定画面を表示する */
			case R.id.menu_map:
				startActivityForResult(
					new Intent(this, SettingMapActivity.class),
					Constant.REQUEST_CODE_MENU_MAP);
				return true;

//		 	/* 設定なら、設定画面を表示する */
			case R.id.menu_settings:
				/* 将来の予備*/
				showToast( R.string.menu_reserved );
//				startActivityForResult(
//					new Intent(this, Settings.class),
//					Constant.REQUEST_CODE_MENU_SETTING);
				return true;

		 	/* デバックなら、デバック設定画面を表示する */
			case R.id.menu_debug:
				startActivityForResult(
					new Intent(this, Settings.class),
					Constant.REQUEST_CODE_MENU_DEBUG);
				return true;
		}
		return false;
	}

	/** GPS処理 */
	@Override
	public void onLocationChanged(Location arg0) {
	}

	/** GPS処理 */
	@Override
	public void onProviderDisabled(String arg0) {
	}

	/** GPS処理 */
	@Override
	public void onProviderEnabled(String arg0) {
	}

	/** GPS処理 */
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	/**
	 * 再開するときは、
	 * 位置情報を更新する
     */
	@Override
    protected void onResume() {
		Log.d(TAG, "onResume");
		updateLocation();

		/* ロック解除 */
		lockLaunch = false;

        super.onResume();
    }

	/**
	 * 中断するときは、
	 * 位置情報を更新を停止する
     */
    @Override
    protected void onPause() {
		Log.d(TAG, "onPause");
		removeUpdateLocation();
        super.onPause();
    }

	/**
	 * <pre>
	 * 位置情報を取得する
	 * この値はキャッシュであり、必ずしも現在地を示すものではない
	 * A地点でキャッシュされ、B地点で位置が取得できないと、A地点の位置が戻る
	 * </pre>
     */
    private List<String> getLocation() {

		/* 緯度・経度を取得する */
    	String latitude  = "";
    	String longitude = "";

       	if (locManager != null) {
    		Location loc = locManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
    		if (loc != null) {
    			latitude  = "" + loc.getLatitude();
    			longitude = "" + loc.getLongitude();
    		}
    	}

		List<String> list = new ArrayList<String>();
		list.add(latitude);
		list.add(longitude);
		return list;
	}

	/**
	 * 位置情報を更新する
     */
    private void updateLocation() {

		if (locManager != null) {
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
//                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                this);
        }
    }

	/**
	 * 位置情報を更新を停止する
     */
    private void removeUpdateLocation() {

		if (locManager != null) {
           locManager.removeUpdates(this);
		}
    }

}