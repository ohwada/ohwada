package jp.ohwada.android.pinqa1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Marker Dialog
 */
public class MarkerDialog extends CommonDialog {

	private static final float DISPLAY_RATIO = 0.5f;
	
	// object
	private BitmapUtility mBitmapUtility;
	
	// variable
	private String mTitle = "";
	private String mMessage = "";
	private String mUrl = "";
	private int mId = 0;
						
	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public MarkerDialog( Activity activity ) {
		super( activity, R.style.Theme_MarkerDialog );
		initDialod( activity );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public MarkerDialog( Activity activity, int theme ) {
		super( activity, theme );
		initDialod( activity );
		create(); 
	}

	/*
	 * initDialod
	 * @param Activity activity
	 */
	private void initDialod( Activity activity ) {
		mActivity = activity;
		mBitmapUtility = new BitmapUtility( activity );
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
	 * setId
	 * @param int id 
	 */ 
	public void setId( int id ) {
		mId = id ;
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

		ImageView ivArticle = (ImageView) findViewById( R.id.dialog_marker_imageview_article );
		Bitmap bitmap = getBitmap();
		// hide view if no butomap
		if ( bitmap == null ) {
			ivArticle.setVisibility( View.GONE );
		} else {
			// show bitmap
			ivArticle.setImageBitmap( bitmap );
		}
				
		Button btnDetail = (Button) findViewById( R.id.dialog_marker_button_detail );
		btnDetail.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startArticle();
			}
		});		
	}

	/**
	 * getBitmap
	 * @return Bitmap
	 */ 	
	private Bitmap getBitmap() {
		if ( mId == 0 ) return null;
		return mBitmapUtility.getBitmap( mId, DISPLAY_RATIO );
	}
		
	/**
	 * startPlace
	 */
    private void startArticle() {
    	if (( mUrl == null )|| "".equals( mUrl ) ) return;
		Intent intent = new Intent( mActivity, ArticleActivity.class );
		intent.putExtra( Constant.EXTRA_ARTICLE_URL, mUrl );
		mActivity.startActivityForResult( intent, Constant.REQUEST_ARTICLE );    
	}	
		
}
