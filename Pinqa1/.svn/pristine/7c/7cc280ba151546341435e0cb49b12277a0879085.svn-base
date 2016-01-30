package jp.ohwada.android.pinqa1;

import jp.ohwada.android.pinqa1.task.ArticleRecord;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Marker Dialog
 */
public class MarkerDialog extends CommonDialog {
	
	// object
	private ActivityUtility mActivityUtility;
	private BitmapUtility mBitmapUtility;
	
	// variable
	private String mTitle = "";
	private String mMessage = "";
	private ArticleRecord mArticleRecord = null;
	private View mView = null;
						
	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public MarkerDialog( Activity activity ) {
		super( activity, R.style.Theme_MarkerDialog );
		initDialod( activity );
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public MarkerDialog( Activity activity, int theme ) {
		super( activity, theme );
		initDialod( activity );
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

	/*
	 * initDialod
	 * @param Activity activity
	 */
	private void initDialod( Activity activity ) {
		mActivity = activity;
		mActivityUtility = new ActivityUtility( activity );
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
	 * setRecord
	 * @param ArticleRecord record
	 */ 
	public void setRecord( ArticleRecord record ) {
		mArticleRecord = record ;
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
				mActivityUtility.startArticle( mArticleRecord );
			}
		});		

		Button btnApp = (Button) findViewById( R.id.dialog_marker_button_app );
		btnApp.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				mActivityUtility.startApp( mArticleRecord );
			}
		});

		Button btnNavicon = (Button) findViewById( R.id.dialog_marker_button_navicon );
		btnNavicon.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				mActivityUtility.startNavicon( mArticleRecord );
			}
		});

	}

	/**
	 * getBitmap
	 * @return Bitmap
	 */ 	
	private Bitmap getBitmap() {
		if ( mArticleRecord == null ) return null;
		int id = mArticleRecord.article_id;
		if ( id == 0 ) return null;
		return mBitmapUtility.getBitmap( id, WIDTH_RATIO_HALF );
	}
		
}
