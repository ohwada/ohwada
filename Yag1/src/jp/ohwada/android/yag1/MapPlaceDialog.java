package jp.ohwada.android.yag1;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * MapPlace Dialog
 */
public class MapPlaceDialog extends CommonDialog {
	
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public MapPlaceDialog( Context context ) {
		super( context, R.style.Theme_MapDialog  );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public MapPlaceDialog( Context context, int theme ) {
		super( context, theme ); 
	}
				
	/**
	 * create
	 * @param String name 
	 */ 	
	public void create( String name ) {
		setContentView( R.layout.dialog_map_place );
		createButtonClose() ;
		setLayout();	
		setGravityBottom();
						
		Button btnDefault = (Button) findViewById( R.id.dialog_map_place_button_default );
		btnDefault.setText( name );
		btnDefault.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_DEFAULT );
			}
		});

		Button btnGps = (Button) findViewById( R.id.dialog_map_place_button_gps );
		btnGps.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_GPS );
			}
		});

		Button btnMarker = (Button) findViewById( R.id.dialog_map_place_button_marker );
		btnMarker.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_MARKER );
			}
		});

		Button btnMap = (Button) findViewById( R.id.dialog_map_place_button_map );
		btnMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_MAP );
			}
		});

		Button btnApp = (Button) findViewById( R.id.dialog_map_place_button_app );
		btnApp.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				sendMessage( Constant.MSG_ARG1_DIALOG_MAP_APP );
			}
		});
						
	}

}
