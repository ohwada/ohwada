package jp.ohwada.android.preferencefragmentsample;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * SettingsActivity
 */
public class SettingsActivity extends Activity {

	/**
	 * === onCreate ===
	 */ 				
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SettingsFragment fragment = new SettingsFragment();
		ft.replace( android.R.id.content, fragment );	
		ft.commit();
	}

}