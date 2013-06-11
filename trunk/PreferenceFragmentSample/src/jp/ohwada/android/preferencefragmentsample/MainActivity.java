package jp.ohwada.android.preferencefragmentsample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */
public class MainActivity extends Activity {
	private MyPreference mPreference = null;
	private TextView mTextView1 = null;
	private String mRingtone1 = null;

    /**
	 * === onCreate ===
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mPreference = new MyPreference( this );
        
        mTextView1 = (TextView) findViewById( R.id.textview_1 );

        Button button_play = (Button) findViewById( R.id.button_play );
        button_play.setOnClickListener(
        		new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				playRingtone();
			}
        });
        
        Button button_stop = (Button) findViewById( R.id.button_stop );
        button_stop.setOnClickListener(
        		new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				stopRingtone();
			}
        });
    }

    /**
	 * === onResume ===
	 */
    @Override
	public void onResume() {
		super.onResume();
		getPreferences();
	}

    /**
	 * getPreferences
	 */		
	private void getPreferences() {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences( this );

        boolean checkbox_1 = preference.getBoolean( "preference_key_checkbox_1" , false );
        String edittext_1 = preference.getString( "preference_key_edittext_1", "" );
        String edittext_2 = preference.getString( "preference_key_edittext_2", "" );
        String list_1 = preference.getString( "preference_key_list_1", "" );

        mRingtone1 = preference.getString( "preference_key_ringtone_1", "" );
        String mRingtone1_title = mPreference.getRingtoneTitle( mRingtone1 ); 

        boolean category_1_checkbox_1 = preference.getBoolean( "preference_key_category_1_checkbox_1", false);
        boolean screen_1_checkbox_1 = preference.getBoolean( "preference_key_screen_1_checkbox_1", false);
	
		String msg = "";
			msg += "CheckBox 1 : " + checkbox_1 + "\n";
			msg += "EditText 1 : " + edittext_1 + "\n";
			msg += "EditText 2 : " + edittext_2 + "\n";
			msg += "List 1 : " + list_1 + "\n";
			msg += "Ringtone 1 : " + mRingtone1_title + " " + mRingtone1 + "\n";
			msg += "Category 1 CheckBox 1 : " + category_1_checkbox_1 + "\n";
			msg += "Screen 1 CheckBox 1 : " + screen_1_checkbox_1 + "\n";
		
	    mTextView1.setText( msg );
	}

    /**
	 * playRingtone
	 */	
	private void playRingtone() {
		boolean ret = mPreference.playRingtone( mRingtone1 );
		if ( !ret ) {
		   	Toast.makeText( this, "Silent", Toast.LENGTH_SHORT).show();
		}
	}

    /**
	 * playRingtone
	 */	
	private void stopRingtone() {
		mPreference.stopRingtone();  	
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
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		/* Initial of menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return true;
    }

	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent intent = new Intent( this, SettingsActivity.class );
				startActivity( intent );  
				return true;
        }
        return false;
    }

}