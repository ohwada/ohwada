package jp.ohwada.android.osm2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * About Dialog
 */
public class AboutDialog extends CommonDialog {

	// url
	private final static String URL_OSM = "http://openstreetmap.jp/";
	private final static String URL_LGD = "http://linkedgeodata.org/";
	private final static String URL_LOD = "http://lod.sfc.keio.ac.jp/challenge2012/";
	private final static String URL_CREDIT = "http://android.ohwada.jp/";

	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public AboutDialog( Activity activity ) {
		super( activity );
		mActivity = activity;
		create();
	}

	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param int theme
	 */ 
	public AboutDialog( Activity activity, int theme ) {
		super( activity, theme ); 
		mActivity = activity;
		create();
	}

	/**
	 * create
	 */ 
	private void create() {
		setContentView( R.layout.dialog_about );
		createButtonClose() ;
		setLayoutFull();	
		setTitle( R.string.dialog_about_title );
	
		TextView tvOsm = (TextView) findViewById( R.id.dialog_about_textview_osm );
		tvOsm.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_OSM );
			}
		});

		TextView tvLgd = (TextView) findViewById( R.id.dialog_about_textview_lgd );
		tvLgd.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_OSM );
			}
		});
				
		TextView tvLod = (TextView) findViewById( R.id.dialog_about_textview_lod );
		tvLod.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_LOD );
			}
		});
		
		TextView tvCredit = (TextView) findViewById( R.id.dialog_about_textview_credit );
		tvCredit.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_CREDIT );
			}
		});

		ImageView ivOsm = (ImageView) findViewById( R.id.dialog_about_imageview_osm );
		ivOsm.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_OSM );
			}
		});

		ImageView ivLgd = (ImageView) findViewById( R.id.dialog_about_imageview_lgd );
		ivLgd.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_LGD );
			}
		});
		
		ImageView ivLod = (ImageView) findViewById( R.id.dialog_about_imageview_lod );
		ivLod.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startBrawser( URL_LOD );
			}
		});

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
