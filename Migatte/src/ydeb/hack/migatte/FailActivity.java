package ydeb.hack.migatte;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import ydeb.hack.migatte.util.Constant;

/**
 * 検索失敗画面を表示する
 *
 */
public class FailActivity extends Activity {

	/**
	 * <pre>
	 * 最初に
	 * 検索失敗画面を表示する
	 * </pre>
	 * @param savedInstanceState Bundle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fail);

		/* 呼出し側のパラメータを取得する */
        String keyword = getIntent().getStringExtra(Constant.EXTRA_NAME_KEYWORD);

		TextView message = (TextView) findViewById(R.id.fail_message);
		
		/* キーワードがあるとき、ないときで、メッセージを変える */
		String msg;
    	if ( keyword.length() > 0 ) {
    		msg = keyword + " " + getString( R.string.fail_keyword );
    	} else {
			msg = getString( R.string.fail_location );
		}
  	
		message.setText( msg );
	}

	/**
	 * 画面がタッチされたときは、
	 * finish()にて終了する
	 * @param event イベント
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
 		/* 終了する */
		super.finish();
		return true;	
	}

}