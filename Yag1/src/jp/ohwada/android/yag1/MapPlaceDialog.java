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
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public MapPlaceDialog( Context context, int theme ) {
		super( context, theme ); 
		create();
	}
				
	/**
	 * create
	 */ 	
	public void create() {
		setContentView( R.layout.dialog_map_place );
		createButtonClose() ;
		setLayout();	
		setGravity();
												
		Button btnDefault = (Button) findViewById( R.id.dialog_map_place_button_default );
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
				
	}

}
