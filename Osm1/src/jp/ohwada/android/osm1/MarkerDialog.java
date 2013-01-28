package jp.ohwada.android.osm1;

import android.app.Activity;
import android.widget.TextView;

/**
 * Marker Dialog
 */
public class MarkerDialog extends CommonDialog {

	// variable
	private String mTitle = "";
	private String mMessage = "";
			
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
	 * create
	 */ 	
	public void create() {
		setContentView( R.layout.dialog_marker );
		createButtonClose() ;

		TextView tvTitle = (TextView) findViewById( R.id.dialog_marker_textview_title );
		tvTitle.setText( mTitle );
		
		TextView tvMessage = (TextView) findViewById( R.id.dialog_marker_textview_message );
		tvMessage.setText( mMessage );
		
	}
		
}
