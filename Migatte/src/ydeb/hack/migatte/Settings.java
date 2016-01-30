package ydeb.hack.migatte;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * 設定画面を表示する
 *
 */
public class Settings extends PreferenceActivity{

	/** TAG */
	private static final String TAG = "Settings";
	
	/**
	 * 設定画面を表示する
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}