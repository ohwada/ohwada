package jp.ohwada.android.pinqa1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;

/**
 * Option Dialog
 */
public class OptionDialog extends CommonDialog {

	// Search
	private SearchTask mSearchTask; 
	
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public OptionDialog( Context context ) {
		super( context, R.style.Theme_OptionDialog );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public OptionDialog( Context context, int theme ) {
		super( context, theme ); 
	}
			
	/**
	 * create
	 */ 	
	public void create() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( getContext() );
    	String name = pref.getString( 
    		Constant.PREF_NAME_GEO_NAME, 
    		getContext().getResources().getString( R.string.geo_name ) );
    		
	    View view = getLayoutInflater().inflate( R.layout.dialog_option, null );
		setContentView( view );

		createButtonClose() ;
		setLayoutFull();	
		setGravityTop();

		mSearchTask = new SearchTask( getContext(), view, msgHandler );
		mSearchTask.create();
						
		Button btnDefault = (Button) findViewById( R.id.dialog_map_list_button_default );
		btnDefault.setText( name );
		btnDefault.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_OPTION_DEFAULT );
			}
		});

		Button btnGps = (Button) findViewById( R.id.dialog_map_list_button_gps );
		btnGps.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_OPTION_GPS );
			}
		});
		
	}

	/**
	 * getPoint
	 * @return GeoPoint
	 */
	public GeoPoint getPoint() {	
		return mSearchTask.getPoint();
	}

	/**
	 * cancel
	 */
	public void cancelDialog() {
		if ( mSearchTask != null ) {
			mSearchTask.cancel();
		}
		cancel();
	}
	
	/**
	 * sendMessage
	 * @param int arg1
	 */	
	private void sendMessage( int arg1 ) {
    	sendMessage( Constant.MSG_WHAT_DIALOG_OPTION, arg1 );
    }
	
}
