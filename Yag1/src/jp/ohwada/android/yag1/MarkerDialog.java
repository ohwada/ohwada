package jp.ohwada.android.yag1;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Marker Dialog
 */
public class MarkerDialog extends CommonDialog {

	// variable
	private String mTitle = "";
	private String mMessage = "";
	private String mUrl = "";
			
	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public MarkerDialog( Activity activity ) {
		super( activity, R.style.Theme_MarkerDialog );
		mActivity = activity;
		create();
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public MarkerDialog( Activity activity, int theme ) {
		super( activity, theme );
		mActivity = activity;
		create(); 
	}

	/**
	 * Title
	 * @param String str
	 */ 
	public void setCustomTitle( String str ) {
		mTitle= str ;
	}
	
	/**
	 * Message
	 * @param String str
	 */ 
	public void setMessage( String str ) {
		mMessage = str ;
	}
	
	/**
	 * setUrl
	 * @param String str
	 */ 
	public void setUrl( String str ) {
		mUrl = str ;
	}
				
	/**
	 * create
	 */ 	
	public void create() {
		setContentView( R.layout.dialog_marker );
		createButtonClose() ;

		TextView tvTitle = (TextView) findViewById( R.id.dialog_marker_textview_title );
		tvTitle.setText( mTitle );
		
		TextView tvMessage = (TextView) findViewById( R.id.dialog_marker_textview_message );
		tvMessage.setText( mMessage );
 								
		Button btnPlace = (Button) findViewById( R.id.dialog_marker_button_place );
		btnPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startPlace();
			}
		});
		
	}

	/**
	 * startPlace
	 */
    private void startPlace() {
    	if (( mUrl == null )|| "".equals( mUrl ) ) return;
		Intent intent = new Intent( mActivity, PlaceActivity.class );
		intent.putExtra( Constant.EXTRA_PLACE_URL, mUrl );
		mActivity.startActivityForResult( intent, Constant.REQUEST_PLACE );    
	}
		
}
