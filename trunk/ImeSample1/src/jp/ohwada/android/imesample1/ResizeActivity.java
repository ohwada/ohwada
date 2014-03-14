package jp.ohwada.android.imesample1;

import android.os.Bundle;

/**
 * ResizeActivity
 */
public class ResizeActivity extends CommonActivity {
	/**
	 * === onCreate ===
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		execCreate();
		setText( R.string.action_resize );
	}
}
