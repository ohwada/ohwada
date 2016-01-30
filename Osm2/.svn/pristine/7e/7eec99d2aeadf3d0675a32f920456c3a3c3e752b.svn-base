package jp.ohwada.android.osm2;

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
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public MarkerDialog( Activity activity, int theme ) {
		super( activity, theme );
		mActivity = activity; 
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
	 * @param String url
	 */ 
	public void setUrl( String url ) {
		mUrl = url ;
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

		Button btnDetail = (Button) findViewById( R.id.dialog_marker_button_detail );
		btnDetail.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startNode();
			}
		});			
	}

	/**
	 * startNode
	 */
    private void startNode() {
    	if (( mUrl == null )|| "".equals( mUrl ) ) return;
		Intent intent = new Intent( mActivity, NodeActivity.class );
		intent.putExtra( Constant.EXTRA_NODE_URL, mUrl );
		mActivity.startActivityForResult( intent, Constant.REQUEST_NODE );    
	}		
}
