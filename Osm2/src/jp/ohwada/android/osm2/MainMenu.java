package jp.ohwada.android.osm2;

import jp.ohwada.android.osm2.task.ManageFile;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/*
 * MainMenu
 */		
public class MainMenu {

	private final static String URL_USAGE = "http://android.ohwada.jp/apr/osm1";

	// object		
	private Activity mActivity;
   	private ManageFile mManageFile;
   		
	public MainMenu( Activity activity ) {
		mActivity = activity;
		mManageFile = new ManageFile();  	
	}

	/**
	 * === onCreateOptionsMenu ===
	 */
    public void execCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = mActivity.getMenuInflater();
        inflater.inflate( R.menu.activity_main, menu );
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    public boolean execOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_about:
				showAboutDialog();
				return true;
			case R.id.menu_usage:
				startBrawser( URL_USAGE );
				return true;
			case R.id.menu_setting:
				startSetting();
				return true;
			case R.id.menu_map_setting:
				startMapSetting();
				return true;
			case R.id.menu_clear:
				mManageFile.clearAllCache();
				return true;
        }
        return false;
    }
		
	/**
     * showAboutDialog
     */
	private void showAboutDialog() {
		AboutDialog dialog = new AboutDialog( mActivity );
		dialog.show();
	}
	
    /**
     * startBrawser
     * @param String url
     */
	private void startBrawser( String url ) {
		if (( url == null )|| url.equals("") ) return;
		Uri uri = Uri.parse( url );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		mActivity.startActivity( intent );
	}

    /**
     * startSetting
     */
	private void startSetting( ) {
		Intent intent = new Intent( mActivity, SettingActivity.class );
		mActivity.startActivityForResult( intent, Constant.REQUEST_SETTING );
	}
	
    /**
     * startMapSetting
     */
	private void startMapSetting( ) {
		Intent intent = new Intent( mActivity, MapSettingActivity.class );
		mActivity.startActivityForResult( intent, Constant.REQUEST_MAP_SETTING );
	}
	
}
