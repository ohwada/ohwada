package ydeb.hack.migatte;

import ydeb.hack.migatte.http.GnaviUtils;
import ydeb.hack.migatte.util.DebugUtils;
import android.app.Application;

/**
 * アプリが起動されるときに、最初に実行される
 *
 */
public class AppApplication extends Application {

    /**
     * ぐるなびのキーを取得する
     */
    @Override
    public void onCreate() {
    	super.onCreate();
    	DebugUtils.setDebugFlag(this);
    	
    	GnaviUtils.key = getText(R.string.gnavi_api_key).toString();
    }
}
