package jp.ohwada.android.imesample1;

import android.os.Bundle;

/**
 * NoFullscreenActivity
 */
public class NoFullscreenActivity extends CommonActivity {
	/**
	 * === onCreate ===
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		execCreate();
		setNoFullscreen();
		setText( R.string.action_no_fullscreen );
	}
}
