package jp.ohwada.android.preferencefragmentsample;

import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

/**
 * SettingsFragment
 */
public class SettingsFragment extends PreferenceFragment {

	private MyPreference mPreference = null;
	private EditTextPreference mEditTextPreference1 = null;
	private ListPreference mListPreference1 = null;
	private MultiSelectListPreference mMultiSelectListPreference1 = null;
	private RingtonePreference mRingtonePreference = null;
			
    /**
	 * === onCreate ===
	 */
	@Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.settings );
        
        mPreference = new MyPreference( getActivity() );
                
        mEditTextPreference1 = (EditTextPreference) findPreference( "preference_key_edittext_1" );
        mEditTextPreference1.setOnPreferenceChangeListener(
        		new OnPreferenceChangeListener() {        	
        	@Override
        	public boolean onPreferenceChange(Preference p, Object o) {
        		setSummaryEditText1(p, o);
        		return true;
        	}	
        });
        
        mListPreference1 = (ListPreference) findPreference( "preference_key_list_1" );
        mListPreference1.setOnPreferenceChangeListener(
        		new OnPreferenceChangeListener() {        	
        	@Override
        	public boolean onPreferenceChange(Preference p, Object o) {
        		setSummaryList1(p, o);
        		return true;
        	}	
        });

        mMultiSelectListPreference1 = (MultiSelectListPreference) findPreference( "preference_key_multiselectlist_1" );
        mMultiSelectListPreference1.setOnPreferenceChangeListener(
        		new OnPreferenceChangeListener() {        	
        	@Override
        	public boolean onPreferenceChange(Preference p, Object o) {
        		setSummaryMultiSelectList1(p, o);
        		return true;
        	}	
        });

        mRingtonePreference = (RingtonePreference) findPreference( "preference_key_ringtone_1" );
        mRingtonePreference.setOnPreferenceChangeListener(
        		new OnPreferenceChangeListener() {        	
        	@Override
        	public boolean onPreferenceChange(Preference p, Object o) {
        		setSummaryRingtone1(p, o);
        		return true;
        	}	
        });

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences( getActivity() );
        
        String value_edittext_1 = preference.getString( "preference_key_edittext_1", "" );
        mEditTextPreference1.setSummary( value_edittext_1 );        
                
        String value_list_1 = preference.getString( "preference_key_list_1", "" );
        String entry_list_1 = mPreference.getListEntry( mListPreference1, value_list_1 );
        mListPreference1.setSummary( entry_list_1 );  

        Set<String> set_multiselectlist_1 = mPreference.getStringSet( "preference_key_multiselectlist_1" );
        String entry_multiselectlist_1 = mPreference.getListEntry( mMultiSelectListPreference1, set_multiselectlist_1 );
        mMultiSelectListPreference1.setSummary( entry_multiselectlist_1 );  
 
        String value_ringtone_1 = preference.getString( "preference_key_ringtone_1", "" );
    	String title = mPreference.getRingtoneTitle( value_ringtone_1 );
        mRingtonePreference.setSummary( title );  
	}

    /**
	 * setSummaryEditText1
	 * @param Preference p
	 * @param Object object
	 */     
	private void setSummaryEditText1(Preference p, Object object) {
        mEditTextPreference1.setSummary( (String) object );  
	}

    /**
	 * setSummaryList1
	 * @param Preference p
	 * @param Object object
	 */
	private void setSummaryList1(Preference p, Object object) {
        String entry = mPreference.getListEntry( mListPreference1, object );
        mListPreference1.setSummary( entry );  
	}

    /**
	 * setSummaryMultiSelectList1
	 * @param Preference p
	 * @param Object object
	 */
	private void setSummaryMultiSelectList1(Preference p, Object object) {
        String entry = mPreference.getListEntry( mMultiSelectListPreference1, object );
		mMultiSelectListPreference1.setSummary( entry );  
	}

    /**
	 * setSummaryRingtone1
	 * @param Preference p
	 * @param Object object
	 */ 			
	private void setSummaryRingtone1(Preference p, Object object ) {
        String title = mPreference.getRingtoneTitle( object );
        mRingtonePreference.setSummary( title ); 
		mPreference.playRingtone( object );  
	}

    /**
	 * === onResume ===
	 */
    @Override
	public void onResume() {
		super.onResume();
		stopRingtone();
	}
	
    /**
	 * === onStop ===
	 */
    @Override
    public void onStop() {
    	super.onStop();
    	stopRingtone();
    }

    /**
	 * === onDestroy ===
	 */    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	stopRingtone();
	}

    /**
	 * stopRingtone
	 */		
	private void stopRingtone() {
		mPreference.stopRingtone();  	
	}

}