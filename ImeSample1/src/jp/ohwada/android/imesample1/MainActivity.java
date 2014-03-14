package jp.ohwada.android.imesample1;

import android.os.Bundle;

/**
 * MainActivity
 */
public class MainActivity extends CommonActivity {
	/**
	 * === onCreate ===
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		execCreate();
		setText( R.string.action_default );
	}
}
