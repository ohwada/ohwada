package jp.ohwada.android.yag1;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * LoadingView
 */
public class LoadingView {  
 	  	
	// view conponent
	private ProgressBar mProgressBar;
	private ImageView mImageView;
	private TextView mTextView;
	
	/**
	 * === Constructor ===
	 * @param View view
	 */ 
    public LoadingView( View view  ) {
    	mProgressBar = (ProgressBar) view.findViewById( R.id.loading_progressbar );
		mImageView = (ImageView) view.findViewById( R.id.loading_imageview );
		mTextView = (TextView) view.findViewById( R.id.loading_textview );	
	}

	/**
	 * hide
	 */ 
	public void hideProgressBar() {
		mProgressBar.setVisibility( View.GONE );
		mTextView.setVisibility( View.GONE );
	}

	/**
	 * hide
	 */ 
	public void hideImage() {
		mImageView.setVisibility( View.GONE );
	}
	
	/**
	 * show
	 */ 
	public void show() {
		mProgressBar.setVisibility( View.VISIBLE );
		mImageView.setVisibility( View.VISIBLE );
		mTextView.setVisibility( View.VISIBLE);
	}		

}