package jp.ohwada.android.yag1;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * MapList Dialog
 */
public class MapListDialog extends CommonDialog {

	// view
	private EditText mEditAddress;
	
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
		setContentView( R.layout.dialog_map_list );
		createButtonClose() ;
		setLayout();	
		setGravity();

		mEditAddress = (EditText) findViewById( R.id.dialog_map_list_edittext_address );

		Button btnSearch = (Button) findViewById( R.id.dialog_map_list_button_search );
		btnSearch.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				searchLocation();
			}
		});
						
		Button btnDefault = (Button) findViewById( R.id.dialog_map_list_button_default );
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
	 * searchLocation
	 */
	private void searchLocation() {
		String location = mEditAddress.getText().toString();
		// nothig if no input
		if ( location.length() == 0 ) return;
		if ( location.equals("") ) return;
		Bundle bundle = new Bundle();
        bundle.putString( Constant.BUNDLE_DIALOG_MAP_LOCATION, location );
        sendMessage( Constant.MSG_ARG1_DIALOG_MAP_SEARCH, bundle );	        
	}
		
}
