package jp.ohwada.android.osm2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Setting Activity
 */
public class SettingActivity extends PreferenceActivity {

	private ListPreference mPrefMapKind = null;

	/**
	 * === onCreate ===
	 */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // This method was deprecated in API level 11.
        addPreferencesFromResource( R.xml.setting );

        // This method was deprecated in API level 11.
        mPrefMapKind = (ListPreference) findPreference( "map_kind" );

        mPrefMapKind.setOnPreferenceChangeListener(
        		new OnPreferenceChangeListener() {        	
        	@Override
        	public boolean onPreferenceChange( Preference p, Object o ) {
        		execMapKindChange( p, o );
        		return true;
        	}	
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( this );
        String value = pref.getString( 
        	Constant.PREF_NAME_MAP_KIND, Constant.MAP_OSM );
        String entry = getListEntry( mPrefMapKind, value );
        mPrefMapKind.setSummary( entry );  
	}

	/**
	 * execMapKindChange
	 * @param Preference p
	 * @param Object o 	 
	 */
	private void execMapKindChange( Preference p, Object o ) {
        String entry = getListEntry( mPrefMapKind, (String) o );
        mPrefMapKind.setSummary( entry );  
	}

	/**
	 * getListEntry
	 * @param ListPreference list
	 * @param String value	 
	 */	
	private String getListEntry( ListPreference list, String value ) {
        int index =  list.findIndexOfValue( value );
        String str = "undefined";
        if ( index >= 0 ) {
        	CharSequence[] cs = list.getEntries();
        	str = (String) cs[ index ];
        }
        return str;
    }
}