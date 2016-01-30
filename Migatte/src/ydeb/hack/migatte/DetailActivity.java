package ydeb.hack.migatte;

import java.util.List;

import twitter4j.Tweet;
import twitter4j.TwitterException;

import ydeb.hack.migatte.adapter.TwetterRowAdapter;
import ydeb.hack.migatte.http.GnaviDtoSerializer;
import ydeb.hack.migatte.http.TwitterUtils;
import ydeb.hack.migatte.util.Constant;
import ydeb.hack.migatte.util.DebugUtils;
import ydeb.hack.migatte.util.HttpClient;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 詳細画面を表示する
 *
 */
public class DetailActivity
	extends ListActivity
	implements OnClickListener, OnItemClickListener {
	
	/** TAG */
	private static final String TAG = "DetailActivity";

	/** ぐるなび の URL */
	private static final String GNAVI_URL = "http://mobile.gnavi.co.jp/";

	/** Twitter の URL */
	private static final String TWITTER_URL = "http://twitter.com/";

	/** 身勝手ぐるめ ハッシュタグ */
    private static final String MIGATTEG_HASH = "#migatteg";

	/** ぐるなび情報のオブジェクト */
	private GnaviDtoSerializer gnavi;

	/** ListView */
   	private ListView listView;

	/** ListView Adapter */
   	private TwetterRowAdapter listAdapter;

	/** フッターのビュー */
	private View view_footer;

    /** Called when the activity is first created. */
	/**
	 * <pre>
	 * Called when the activity is first created
	 * GPSから位置情報を取得する
	 * search() ぐるなび検索とTwitter検索を実行する
	 * </pre>
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail);

		/* 呼出し側のパラメータを取得する */
        byte[] byteArray = getIntent().getByteArrayExtra(Constant.EXTRA_NAME_GNAVI);
        gnavi = new GnaviDtoSerializer();
		gnavi.setObject(byteArray);

		/* ListView の初期化 */
		initListView();

		/* ぐるなびの検索結果を表示する */
		showGnavi();

		/* キーワードがあれば */
		if ( gnavi.isSearchKeyword() ) {
			/* ProgressBar を表示する */
			showFooter();

			/* ListView Adapter の初期化 */
			initListAdapter();

			/* Twitter検索を実行する */
    		searchTwitter();
		}
    }

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [戻るボタン]	   finish()にて終了する
	 * [Twitterボタン] Twitterを起動する
	 * [地図ボタン]	   地図を起動する
	 * [店の画像]	   ブラウザを起動し、ぐるなびサイトを表示する
	 * [ぐるなびロゴ]  ブラウザを起動し、ぐるなびサイトを表示する
	 * </pre>
	 * @param v ビュー・クラス
	 */
	@Override
	public void onClick(View v) {
		execClickBack(    v, R.id.header_imageView_back );
		execClickTwitter( v, R.id.header_imageView_twitter );
		execClickMap(     v, R.id.header_imageView_map );
		execClickShop(    v, R.id.header_imageView_image );
		execClickShop(    v, R.id.header_textView_title );
		execClickLogo(    v, R.id.header_imageView_logo );
	}

	/**
	 * <pre>
	 * ListView がクリックされたときの処理を行う
	 * header のときは、ブラウザを起動し、ぐるなびサイトを表示する
	 * item   のときは、Twitter アプリを起動する
	 * </pre>
	 * @param adapter AdapterView
	 * @param view View
	 * @param position ListView の位置
	 * @param id 項目のID
	 */
	@Override
	public void onItemClick(
		AdapterView<?> adapter, View view, int position, long id ) {

		/* header ならば、ぐるなびサイトを表示する */
		if ( position == 0 ) {
			startGnaviSite();

		/* footer ならば、何もしない */
		} else if (id == -1 ) {
			/* dummy */

		/* item ならば、Twitter アプリを起動する */
		} else {
			execItemClickItems(	view );
		}
	}

    /**
	 * ぐるなびの検索結果を表示する
	 * @param gDto ぐるなび情報
     */
    private void showGnavi() {

        View view = getLayoutInflater().inflate(R.layout.header, null);

	   	TextView title = (TextView) view.findViewById(R.id.header_textView_title);
	   	TextView detail = (TextView) view.findViewById(R.id.header_textView_detail);
   		ImageView image = (ImageView) view.findViewById(R.id.header_imageView_image);

        view.findViewById(R.id.header_imageView_back).setOnClickListener(this);
        view.findViewById(R.id.header_imageView_twitter).setOnClickListener(this);
        view.findViewById(R.id.header_imageView_map).setOnClickListener(this);
        view.findViewById(R.id.header_imageView_logo).setOnClickListener(this);
        title.setOnClickListener(this);

    	title.setText(  gnavi.getNameView() );
    	detail.setText( gnavi.getPrLong() );
		image.setImageBitmap( idToBitmap( R.drawable.no_image ) );

		/* url があれば、画像を表示する */
    	if ( gnavi.isShopImageUrl() ) {
    		image.setOnClickListener(this);

			/* 非同期タスク */
			showGnaviImage( image, gnavi.getShopImageUrl() );
    	}

		TextView feeling = (TextView) view.findViewById(R.id.header_textView_feeling);
		feeling.setText( getFeeling() );

		/* ListView のヘッダーを追加する */
		addHeaderView( view );
     }

	/**
	 * ListView のフッターを表示する
	 * ProgressBar を表示する 
	 */
    private void showFooter() {

        view_footer = getLayoutInflater().inflate(R.layout.footer, null);
		addFooterView( view_footer );
    }

	/**
	 * ListView のフッターを非表示にする
	 */
    private void hideFooter() {
		/* フッターのビューがなければ、何もしない */
		if ( view_footer == null ) {
			return;
		}
		removeFooterView( view_footer );
	}

    /**
	 * ListView のフッターを表示する
     * Twitterからデータの取得が失敗した旨を表示する
     */ 
    private void showFooterFail() {
		/* フッターのビューがなければ、何もしない */
		if ( view_footer == null ) {
			return;
		}
    	view_footer.findViewById(R.id.footer_progress).setVisibility(View.GONE);
    	view_footer.findViewById(R.id.footer_fail).setVisibility(View.VISIBLE);
	}

	/**
	 * ListView の初期化 
	 */
	private void initListView() {
		listView = getListView();
	}

	/**
	 * ListView のヘッダーを追加する 
	 */
	private void addHeaderView( View v ) {
		listView.addHeaderView( v );
	}

	/**
	 * ListView のフッターを追加する 
	 */
	private void addFooterView( View v ) {
		listView.addFooterView( v );
	}

	/**
	 * ListView のフッターを削除する 
	 */
	private void removeFooterView( View v ) {
		listView.removeFooterView( v );
	}

	/**
	 * ListView のクリックを設定する 
	 */
	private void setOnItemClickListener() {
        listView.setOnItemClickListener( this );
	}

	/**
	 * ListView Adapter の初期化 
	 */
	private void initListAdapter() {
		listAdapter = new TwetterRowAdapter(this, R.layout.tweet_row);
		listView.setAdapter( listAdapter );
	}

    /**
	 * <pre>
	 * ListView Adapter の設定
     * Twitterからデータの取得が終わると、
	 * Twitterの検索結果を表示する
	 * </pre>
	 * @param tweets Twitter情報のリスト
     */ 
    private void setListAdapter(List<Tweet> tweets) {

    	for(Tweet t:tweets) {
    		listAdapter.add(t);
    	}
    }

	/**
	 * 気分を取得する
	 * @return 気分
	 */
	private String getFeeling() {
		String str = gnavi.getSearchKeyword() + " "
			+ getString( R.string.detail_feeling );
		return str; 
	}

	/**
	 * Twitter検索を実行する。
	 */
    private void searchTwitter() {

		/* キーワードがあれば、Twitter検索を実行する */
    	if ( gnavi.isSearchKeyword() ) {
	    	TwitterAsync tweetTask = new TwitterAsync(this);
    		tweetTask.execute(
				gnavi.getSearchKeyword(), 
				gnavi.getSearchLatitude(), 
				gnavi.getSearchLongitude() );
		}
   }

    /**
	 * リソースID を ビットマップ に変換する
	 * @param  id  リソースID
	 * @return ビットマップ
     */
    private Bitmap idToBitmap( int id ) {

		return BitmapFactory.decodeResource( getResources(), id );
	}

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [戻るボタン] btn_back.png finish()にて終了する
	 * </pre>
	 * @param v  ビュー・クラス
	 * @param id リソースID
	 */
	private void execClickBack(View v, int id ) {

	 	/* リソースID が一致しなければ、何もしない */
		if (v.getId() != id ) {
			return;
		}

		super.finish();
	}

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [Twitterボタン] btn_twitter.png Twitterを起動する
	 * </pre>
	 * @param v  ビュー・クラス
	 * @param id リソースID
	 */
	private void execClickTwitter(View v, int id) {

	 	/* リソースID が一致しなければ、何もしない */
		if (v.getId() != id ) {
			return;
		}

	 	/* gnavi が設定されていなければ、何もしない */
		if (gnavi == null) {
			return;
		}

		String text = 
			getFeeling() + " : "
			+ gnavi.getNameView() + " "
			+ gnavi.getUrlShort() + " "
			+ MIGATTEG_HASH ;

		/* Twitterを起動する */
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT, text );
		startActivity(i);
	}

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [地図ボタン] btn_map.png 地図を起動する
	 * </pre>
	 * @param v  ビュー・クラス
	 * @param id リソースID
	 */
	private void execClickMap(View v, int id ) {

	 	/* リソースID が一致しなければ、何もしない */
		if (v.getId() != id ) {
			return;
		}

	 	/* gnavi が設定されていなければ、何もしない */
		if (gnavi == null) {
			return;
		}

	 	/* 地図を起動する */
		Intent i = new Intent( 
			Intent.ACTION_VIEW, 
			Uri.parse( gnavi.getActionViewMapQuery() ) );
		startActivity(i);
	}

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [店の画像] ブラウザを起動し、ぐるなびサイトを表示する
	 * </pre>
	 * @param v  ビュー・クラス
	 * @param id リソースID
	 */
	private void execClickShop(View v, int id ) {

	 	/* リソースID が一致しなければ、何もしない */
		if (v.getId() != id ) {
			return;
		}

		startGnaviSite();
	}

	/**
	 * ブラウザを起動し、ぐるなびサイトを表示する
	 */
	private void startGnaviSite() {

	 	/* gnavi が設定されていなければ、何もしない */
		if (gnavi == null) {
			return;
		}

	 	/* url が空ならば、何もしない */
		if ( !gnavi.isUrl() ) {
			return;
		}

		/* ブラウザを起動し、ぐるなびサイトを表示する */
		Intent i = new Intent(
			Intent.ACTION_VIEW, 
			Uri.parse( gnavi.getUrlShort() ) );
		startActivity(i);
	}

	/**
	 * <pre>
	 * ボタンが押されたときの処理を行う
	 * [ぐるなびロゴ] ブラウザを起動し、ぐるなびサイトを表示する
	 * </pre>
	 * @param v  ビュー・クラス
	 * @param id リソースID
	 */
	private void execClickLogo(View v, int id ) {

	 	/* リソースID が一致しなければ、何もしない */
		if (v.getId() != id ) {
			return;
		}

		/* ブラウザを起動し、ぐるなびサイトを表示する */
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse( GNAVI_URL ));
		startActivity(i);
	}

	/**
	 * <pre>
	 * ListView がクリックされたときの処理を行う
	 * Twitter アプリを起動する
	 * </pre>
	 * @param adapter AdapterView
	 * @param view View
	 * @param position ListView の位置
	 * @param id 項目のID
	 */
	private void execItemClickItems( View view ) {

		TextView tv_id = (TextView) view.findViewById(R.id.tweetRow_textView_id);
	 	/* ID欄がなければ、何もしない */
		if ( tv_id == null ) {
			return;
		}

		/* 頭の @ を削除する */
		String str_id = tv_id.getText().toString().replaceAll("@","");

	 	/* ID が空ならば、何もしない */
		if ( ( str_id == null ) || str_id.equals("") ) {
			return;
		}

		/* Twitter アプリを起動する */
		String url = TWITTER_URL + str_id ;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * ポップアップを表示する
	 * @param id リソースID
	 */
	private void showToast( int id ) {
		showToast( getString(id), Toast.LENGTH_SHORT );
	}

	/**
	 * ポップアップを表示する
	 * @param text メッセージ
	 */
	private void showToast( String text ) {
		showToast( text, Toast.LENGTH_SHORT );
	}

	/**
	 * ポップアップを表示する
	 * @param text メッセージ
	 * @param duration 表示する時間
	 */
	private void showToast( String text, int duration ) {
		Toast.makeText(this, text, duration).show();
	}

/* === Twitterの検索の非同期処理 === */

    /**
	 * 非同期処理からの呼び出し
	 * Twitterの検索結果を表示する
	 * @param tweets Twitter情報のリスト
     */ 
    private void showTwetter(List<Tweet> tweets) {
 		hideFooter();
		setListAdapter( tweets );
		setOnItemClickListener();
    }

    /**
	 * 非同期処理からの呼び出し
     * Twitterからデータの取得が失敗すると、その旨を表示する
     */ 
    private void showTwitterFail() {
		showToast( R.string.detail_twitter_fail );
		showFooterFail();
	}

    /**
     * Twitterの検索の非同期処理
     * @author yamazaki
     *
     */
    class TwitterAsync extends AsyncTask<String, Void, List<Tweet>> {

    	/** コンテキスト・クラスの一時保存 */
    	private Context c;

		/**
		 * コンストラクタにて、
		 * コンテキスト・クラスを設定する
		 * @param _c コンテキスト(Context)
		 */ 
    	public TwitterAsync(Context _c) {
    		c = _c;
    	}

		/**
		 * バックブランドにて、
		 * Twitterの検索を実行する
		 * @param args 0:キーワード 1:緯度 2:経度
		 * @return Twitter情報のリスト
		 */ 
		@Override
		protected List<Tweet> doInBackground(String... args) {
			String keyword = args[0];
			String latitude = args[1];
			String longitude = args[2];
			try {
				return TwitterUtils.getTwitter(keyword, latitude, longitude);
			} catch (TwitterException e) {
				Log.e(TAG, "", e);
			}
			return null;
		}

		/**
		 * バックグランド処理が終わると、
		 * Twitterの検索結果を表示する
		 * @param lists Twitter情報のリスト
		 */
    	@Override
		protected void onPostExecute(List<Tweet> lists) {

    		/* Twitterからデータの取得が終わると、検索結果を表示する */
    		if (lists != null && lists.size() > 0) {
				showTwetter(lists);

	   		/* 取得できないときは、エラーを表示する */
    		} else {
				showTwitterFail();
			}
		}
		
    }

/* === 画像を取得する非同期処理 === */

    /**
	 * 非同期処理の起動
	 * ぐるなびの画像を表示する
	 * @param iv  ImageView
	 * @param url 画像URL
     */
    private void showGnaviImage( ImageView iv, String url ) {

  		ImageTask task = new ImageTask( iv );
   		task.execute( url );
    }

    /**
     * 画像を取得する非同期処理
     */
    class ImageTask extends AsyncTask<String, Void, Bitmap> {

    	/** 画像クラスの一時保存 */
    	private ImageView image;

		/**
		 * コンストラクタにて、
		 * 画像クラスを設定する
		 * @param iv 画像(ImageView)
		 */
    	public ImageTask(ImageView iv) {
    		image = iv;
    	}

		/**
		 * バックブランドにて、
		 * 画像を取得する
		 * @param params 0:url
		 * @return ビットマップ
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			if (DebugUtils.isDebug()) {
				Log.d(TAG, "image url:" + url);
			}
			return HttpClient.getImage(params[0]);
		}

		/**
		 * バックグランド処理が終わると、
		 * 画像を設定する
		 * @param bitmap ビットマップ
		 */	
    	@Override
		protected void onPostExecute(Bitmap bitmap) {
    		image.setImageBitmap(bitmap);
    	}
    }

}
