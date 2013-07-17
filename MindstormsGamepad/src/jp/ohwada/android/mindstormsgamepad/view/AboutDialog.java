package jp.ohwada.android.mindstormsgamepad.view;

import jp.ohwada.android.mindstormsgamepad.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * About Dialog
 */
public class AboutDialog extends Dialog {

	// constant
	private final static float WIDTH_RATIO_FULL = 0.95f;

	// url
	private final static String URL_NXT = "http://en.wikipedia.org/wiki/Lego_Mindstorms_NXT";
	private final static String URL_CREDIT = "http://android.ohwada.jp/";

	// object
	private Activity mActivity;

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

		setContentView( R.layout.dialog_about );
		setLayoutFull();	
		setTitle( R.string.dialog_about_title );

		ImageView ivOsm = (ImageView) findViewById( R.id.ImageView_dialog_about_nxt );
		ivOsm.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( URL_NXT );
			}
		});
			
		TextView tvCredit = (TextView) findViewById( R.id.TextView_dialog_about_credit );
		tvCredit.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( URL_CREDIT );
			}
		});

		Button btnClose = (Button) findViewById( R.id.Button_dialog_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});

	}

	/**
	 * setLayout
	 */ 
	private void setLayoutFull() {
		int width = (int)( getWindowWidth() * WIDTH_RATIO_FULL );
		setLayout( width );
	}

	/**
	 * setLayout
	 * @param int width
	 */ 
	protected void setLayout( int width ) {
		getWindow().setLayout( width, ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	/**
	 * getWindowWidth
	 * @return int 
	 */ 
	private int getWindowWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize( point );
		return point.x;
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
