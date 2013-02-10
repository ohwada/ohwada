package jp.ohwada.android.osm1;

import android.content.Context;
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
		super( context, R.style.Theme_MapDialog );
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
	public void create( String name ) {
	    View view = getLayoutInflater().inflate( R.layout.dialog_option, null );
		setContentView( view );
		createButtonClose() ;
		setLayoutFull();	
		setGravityTop();

		mSearchTask = new SearchTask( mContext, view, msgHandler );
		mSearchTask.create();
						
		Button btnDefault = (Button) findViewById( R.id.dialog_map_list_button_default );
		btnDefault.setText( name );
		btnDefault.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_DEFAULT );
			}
		});

		Button btnGps = (Button) findViewById( R.id.dialog_map_list_button_gps );
		btnGps.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_GPS );
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
	public void cancel() {	
		mSearchTask.cancel();
		dismiss();
	}		
}
