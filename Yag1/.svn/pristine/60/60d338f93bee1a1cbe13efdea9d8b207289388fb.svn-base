package jp.ohwada.android.yag1;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ohwada.android.yag1.task.PlaceRecord;

/**
 * Marker Dialog
 */
public class MarkerDialog extends CommonDialog {

	// object
	private ActivityUtility mActivityUtility;
		
	// variable
	private String mTitle = "";
	private String mMessage = "";
	private PlaceRecord mPlaceRecord = null;
	private View mView = null;
				
	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public MarkerDialog( Activity activity ) {
		super( activity, R.style.Theme_MarkerDialog );
		mActivityUtility = new ActivityUtility( activity );
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public MarkerDialog( Activity activity, int theme ) {
		super( activity, theme );
		mActivityUtility = new ActivityUtility( activity );
	}

	/**
	 * === onWindowFocusChanged ===
	 */ 
    @Override
    public void onWindowFocusChanged( boolean hasFocus ) {
        super.onWindowFocusChanged( hasFocus );
        if ( mView == null ) return;

        // enlarge width, if screen is small			
		if ( mView.getWidth() < getWidthHalf() ) {
			setLayoutHalf();
		}
    }

	/**
	 * Title
	 * @param String str
	 */ 
	public void setCustomTitle( String str ) {
		mTitle = str ;
	}
	
	/**
	 * Message
	 * @param String str
	 */ 
	public void setMessage( String str ) {
		mMessage = str ;
	}
	
	/**
	 * setRecord
	 * @param PlaceRecord record
	 */ 
	public void setRecord( PlaceRecord record ) {
		mPlaceRecord = record ;
	}
				
	/**
	 * create
	 */ 	
	public void create() {
		mView = getLayoutInflater().inflate( R.layout.dialog_marker, null );
		setContentView( mView ); 

		createButtonClose() ;

		TextView tvTitle = (TextView) findViewById( R.id.dialog_marker_textview_title );
		tvTitle.setText( mTitle );
		
		TextView tvMessage = (TextView) findViewById( R.id.dialog_marker_textview_message );
		tvMessage.setText( mMessage );
 								
		Button btnPlace = (Button) findViewById( R.id.dialog_marker_button_place );
		btnPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				mActivityUtility.startPlace( mPlaceRecord );
			}
		});

		Button btnApp = (Button) findViewById( R.id.dialog_marker_button_app );
		btnApp.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				mActivityUtility.startApp( mPlaceRecord );
			}
		});

		Button btnNavicon = (Button) findViewById( R.id.dialog_marker_button_navicon );
		btnNavicon.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				mActivityUtility.startNavicon( mPlaceRecord );
			}
		});
						
	}
			
}
