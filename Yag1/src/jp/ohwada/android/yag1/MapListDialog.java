package jp.ohwada.android.yag1;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;

/**
 * MapList Dialog
 */
public class MapListDialog extends CommonDialog {

	// Search
	private SearchTask mSearchTask; 

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public MapListDialog( Context context ) {
		super( context, R.style.Theme_MapDialog );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public MapListDialog( Context context, int theme ) {
		super( context, theme ); 
	}
				
	/**
	 * create
	 */ 	
	public void create() {
	    View view = getLayoutInflater().inflate( R.layout.dialog_map_list, null );
		setContentView( view );
		
		createButtonClose() ;
		setLayoutFull();	
		setGravityTop();
    		
		mSearchTask = new SearchTask( getContext(), view, msgHandler );
		mSearchTask.create();

		Button btnDefault = (Button) findViewById( R.id.dialog_map_list_button_default );
		btnDefault.setText( getGeoName() );
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

		Button btnApp = (Button) findViewById( R.id.dialog_map_list_button_app );
		btnApp.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_APP );
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
    	sendMessage( Constant.MSG_WHAT_DIALOG_MAP_LIST, arg1 );
    }
			
}
