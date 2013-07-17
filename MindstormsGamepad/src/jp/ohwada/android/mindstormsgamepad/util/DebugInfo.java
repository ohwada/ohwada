package jp.ohwada.android.mindstormsgamepad.util;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/*
 * DebugInfo
 */
public class DebugInfo {

	/** Debug */
    private static final String TAG = Constant.TAG; 
	private static final boolean D = Constant.DEBUG;    
	private static final String TAG_SUB = "DebugInfo: ";

    /* new line */
	private static final String LF = "\n";
		
	/*
	 * Constractor
	 */			
	public DebugInfo() {
		// dummy
	}
	
	/*
	 * write PackageInfo
	 */	
	public static void writePackageInfo( Context context ) {
		try {
	        PackageManager manager = context.getPackageManager();
	        String name = context.getPackageName();
	        PackageInfo info = manager.getPackageInfo( name, 0) ;
	        String msg = "package name:" + info.packageName + LF;
	        msg += "version code: " + info.versionCode + LF;
	        msg += "version nem: " + info.versionName ;
			log_d( msg );
		} catch ( NameNotFoundException e ) {
			 if (D) e.printStackTrace();
		}
	}

	/**
	 * write log
	 * @param String msg
	 */ 
	private static void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}	
} 