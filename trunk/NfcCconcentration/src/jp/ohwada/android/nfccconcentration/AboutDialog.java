package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * About Dialog
 */
public class AboutDialog extends Dialog {

	// url
	private final static String URL_CREDIT = "http://android.ohwada.jp/";

	// constant
	private final static float WIDTH_RATIO_FULL = 0.95f;
	
	// object
	protected Context mContext;
	protected Activity mActivity;

	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public AboutDialog( Activity activity ) {
		super( activity );
		create( activity );
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public AboutDialog( Activity activity, int theme ) {
		super( activity, theme ); 
		create( activity );
	}

	/**
	 * create
	 */ 
	private void create( Activity activity ) {
		mActivity = activity;
		mContext = activity;

		requestWindowFeature( Window.FEATURE_NO_TITLE ); 		
		setContentView( R.layout.dialog_about );
		getWindow().setLayout( getWidthFull(), ViewGroup.LayoutParams.WRAP_CONTENT );
		setTitle( R.string.dialog_about_title );

		ImageView ivBanner = (ImageView) findViewById( R.id.dialog_about_imageview_banner );
		 ivBanner.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_CREDIT );
			}
		});
		
		TextView tvCredit = (TextView) findViewById( R.id.dialog_about_textview_credit );
		tvCredit.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_CREDIT );
			}
		});

		Button btnClose = (Button) findViewById( R.id.dialog_about_button_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});
		
	}

	/**
	 * getWidth
	 */
	private int getWidthFull() {
		WindowManager wm = (WindowManager) getContext().getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		int width = (int)( display.getWidth() * WIDTH_RATIO_FULL );
		return width;
	}

    /**
     * startBrawser
     * @param String url
     */
	private void startBrawser( String url ) {
		dismiss();
		Uri uri = Uri.parse( url );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		mActivity.startActivity( intent );
	}
	
}
