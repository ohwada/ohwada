package jp.ohwada.android.pinqa1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.ohwada.android.pinqa1.task.ArticleRecord;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * ActivityUtility
 */
public class ActivityUtility {
	
	// debug
	private static final boolean D = Constant.DEBUG;
	
	// navicon
	private static final String NAVICON_VERSION = "1.4";
	private static final String NAVICON_PKG = "jp.co.denso.navicon.view";

	// object	
	private Activity mActivity;
	private Context mContext;
	
	/**
	 * === Constructor ===
	 * @param Activity activity
	 */ 	
	public ActivityUtility( Activity activity ) {
		mActivity = activity;
		mContext = activity;
	}

	/**
	 * startArticle
	 * @param ArticleRecord record
	 */
    public void startArticle( ArticleRecord record ) {
    	if ( record == null ) return;
    	String url = record.article_url;
    	if (( url == null )|| "".equals( url ) ) return;
		Intent intent = new Intent( mActivity, ArticleActivity.class );
		intent.putExtra( Constant.EXTRA_ARTICLE_URL, url );
		mActivity.startActivityForResult( intent, Constant.REQUEST_ARTICLE ); 
	}

	/**
	 * startApp
	 * @param ArticleRecord record
	 */
	public void startApp( ArticleRecord record ) {
    	if ( record == null ) return;	
		String lat = e6ToStr( record.map_lat ); 
		String lng = e6ToStr( record.map_lng ); 
		if (( lat == null )|| "".equals( lat ) ) return;	
		if (( lng == null )|| "".equals( lng ) ) return;	
		String uri = "geo:" + lat + "," + lng ;		
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) );
		mActivity.startActivity( intent );
	}
		
	/**
	 * startNavicon
	 * @param ArticleRecord record
	 */
	public void startNavicon( ArticleRecord record ) {
		if ( record == null ) return;	
		String lat = record.lat;
		String lng = record.lng;	
		String label = record.article_label;	
		if (( lat == null )|| "".equals( lat ) ) return;	
		if (( lng == null )|| "".equals( lng ) ) return;			
		// do not work, except the following turn
		String uri = "navicon://navicon.denso.co.jp/setPOI?";
		uri += "ver=" + NAVICON_VERSION;
		uri += "&ll=" + lat + "," + lng ;
		uri += "&appName=" + ApiKey.NAVICON_APP_NAME ;	
		if ( label.length() > 0 ) {
			uri += "&title=" +  encodeUrl( label ) ;
		}
		// start intent
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) );
		try {
			mActivity.startActivity( intent );
		} catch ( ActivityNotFoundException e ) {
			toast_show_long( R.string.navicon_please_install );
			startInstall( NAVICON_PKG );
		}	
	}

	/**
	 * startInstall
	 * @param String name
	 */
	private void startInstall( String name ) {
		String uri = "market://details?id=" + name;
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) );
		try {
			mActivity.startActivity( intent );
		} catch ( ActivityNotFoundException e ) {
			toast_show_long( R.string.market_please_install );
		}
	}

	/**
	 * convert  integer to string
	 * @param integer : location( E6 format )
	 * @return String : location( floating point format )
	 */	
	private String e6ToStr( int e6 ) {
		Double d = (double)e6 / 1E6;
		return Double.toString( d );
	}

	/**
	 * encodeUrl
	 * @param String str
	 * @return String
	 */	
	private String encodeUrl( String str ) {
		String ret = "";
		try {
			// spaces are substituted by '+'.
			ret = URLEncoder.encode( str , "UTF-8" );
		} catch ( UnsupportedEncodingException e ) {
			if (D) e.printStackTrace();
		}
		// replace '+' to space	
		return ret.replaceAll( "\\+", "%20" );
	}
	
	/**
	 * toast_show
	 * @param int id
     */
	private void toast_show_long( int id ) {
		Toast.makeText( mContext, id, Toast.LENGTH_LONG ).show();
	}

}
