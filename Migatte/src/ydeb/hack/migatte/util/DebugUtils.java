package ydeb.hack.migatte.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * デバッグ中を示すフラグの処理
 */
public class DebugUtils {

	/** デバッグ・フラグ */
	private static boolean debug = false;

	/**
	 * デバッグ・フラグを設定する
	 */
	public static void setDebugFlag(Context context) {
		PackageManager manager = context.getPackageManager();
		ApplicationInfo appInfo = null;
		try {
			appInfo = manager.getApplicationInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			debug = false;
			return;
		}
		if((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) 
				== ApplicationInfo.FLAG_DEBUGGABLE) {
			debug = true;
		} else {
			debug = false;
		}
	}

	/**
	 * デバッグ・フラグを返す
	 */
	public static boolean isDebug() {
		return debug;
	}
}
