package jp.ohwada.android.mindstormsgamepad;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * SettingsActivity
 */
public class SettingsActivity extends PreferenceActivity
{			
    /**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
      
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SettingsFragment fragment = new SettingsFragment();
		ft.replace( android.R.id.content, fragment );	
		ft.commit();
	}
	
	/**
	 * SettingsFragment
	 */
	private class SettingsFragment extends PreferenceFragment {
		@Override
    	public void onCreate( Bundle savedInstanceState ) {
        	super.onCreate( savedInstanceState );
        	addPreferencesFromResource( R.xml.settings );
		}
	}	
}