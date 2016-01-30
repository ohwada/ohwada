package ydeb.hack.migatte;

import java.util.List;

import ydeb.hack.migatte.http.GnaviUtils;
import ydeb.hack.migatte.http.GnaviDtoSerializer;
import ydeb.hack.migatte.http.dto.GnaviDto;
import ydeb.hack.migatte.util.Constant;
import ydeb.hack.migatte.util.LocationSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * 検索中画面を表示する
 *
 */
public class SearchActivity extends Activity {

	/** TAG */
	private static final String TAG = "SearchActivity";

	/** キーワード */
	String keyword;

	/**
	 * <pre>
	 * GPSから位置情報を取得する
	 * search() ぐるなび検索を実行する
	 * </pre>
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

		/* 呼出し側のパラメータを取得する */
        String str = getIntent().getStringExtra(Constant.EXTRA_NAME_LOCATION);
        List<String> list = LocationSerializer.deserialize( str );
		keyword   = list.get(0);
		String latitude  = list.get(1);
		String longitude = list.get(2);

		/* ぐるなび検索を実行する */
    	GnaviAsync gnaviTask = new GnaviAsync(this);
    	gnaviTask.execute(keyword, latitude, longitude);
    }

    /**
     * ぐるなびからデータの取得が終わると、
	 * 詳細画面を表示する
	 * @param gDto ぐるなび情報
     */
    private void startDetailActivity(GnaviDto gDto) {

		/* 詳細画面のアクテイビティ */
		Intent i = new Intent(SearchActivity.this, DetailActivity.class);

    	byte[] byteArray = GnaviDtoSerializer.serialize(gDto);
		i.putExtra(Constant.EXTRA_NAME_GNAVI, byteArray);
		startActivityForResult(i, Constant.REQUEST_CODE_DETAIL);
    }

    /**
     * ぐるなびからデータの取得が失敗すると、
	 * 検索失敗画面を表示する
     */
    private void startFailActivity() {
		Intent i = new Intent(SearchActivity.this, FailActivity.class);
		i.putExtra(Constant.EXTRA_NAME_KEYWORD, keyword);
		startActivityForResult(i, Constant.REQUEST_CODE_FAIL);
    }

	/** 
	 * 詳細画面から戻ってきたときは、
	 * 終了する
	 * @param requestCode 起動したアクテビティの番号
	 * @param resultCode  アクテビティからの戻り値
	 * @param data 親のインテント
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int r = 0;
		super.onActivityResult(requestCode, resultCode, data);  

		/* 詳細画面からの戻りであれば、OK を返す */
		if (requestCode == Constant.REQUEST_CODE_DETAIL) {  
			r = RESULT_OK;	// -1

		/* 失敗画面からの戻りであれば、CANCEL を返す */
        } else if (requestCode == Constant.REQUEST_CODE_FAIL ) { 
			r = RESULT_CANCELED;	// 0  

		/* その他であれば、FIRST_USER を返す */
		} else {
			r = RESULT_FIRST_USER;	// 1
		}

		setResult(r); 
		finish();
	}

    /**
     * ぐるなびの検索の非同期処理
     * @author yamazaki
     *
     */
    class GnaviAsync extends AsyncTask<String, Void, GnaviDto> {

    	/** コンテキスト・クラスの一時保存 */
    	private Context c;

		/**
		 * コンストラクタにて、
		 * コンテキスト・クラスを設定する
		 * @param _c コンテキスト(Context)
		 */ 
    	public GnaviAsync(Context _c) {
    		c = _c;
    	}

		/**
		 * 前処理：何もしない
		 */ 
    	@Override
    	protected void onPreExecute() {
    		
    	}
    	
		/**
		 * バックグランドにて、
		 * ぐるなびの検索を実行する
		 * @param args 0:キーワード 1:緯度 2:経度
		 * @return ぐるなび情報のリスト
		 */
		@Override
		protected GnaviDto doInBackground(String... args) {
			String keyword   = args[0];
			String latitude  = args[1];
			String longitude = args[2];
			try {
				GnaviUtils.key = getString( R.string.gnavi_api_key );
				return GnaviUtils.getGnavi(keyword, latitude, longitude);
			} catch (Exception e) {
				Log.e(TAG, "", e);
			}
			return null;
		}
		
		/**
		 * バックグランド処理が終わると、
		 * ぐるなびの検索結果を表示する
		 * 取得できなければ、検索失敗を表示する
		 * @param dto ぐるなび情報
		 */
    	@Override
		protected void onPostExecute(GnaviDto dto) {
			if (dto != null) {
				startDetailActivity(dto);
			} else {
				startFailActivity();
			}
		}
    }

}