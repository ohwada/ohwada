package jp.ohwada.android.m3pirobot;

import android.content.Context;
import android.widget.Toast;

/**
 * prevent to freeze Toast
 * http://www.taosoftware.co.jp/blog/2009/04/android_toast.html
 * http://developer.android.com/reference/android/widget/Toast.html
 */
public class ToastMaster extends Toast {
	private static Toast sToast = null;

	/**
	 * Constructor
	 * @param Context context
	 */
    public ToastMaster(Context context) {
		super(context);
	}

	/**
	 * show Toast
	 */
    @Override
    public void show() {
    	ToastMaster.setToast(this);
    	super.show();
    }

	/**
	 * set Toast object 
	 * @param Toast toast
	 */
    public static void setToast(Toast toast) {
        if (sToast != null) sToast.cancel();
        sToast = toast;
    }

	/**
	 * cancel to show Toast
	 */
    public static void cancelToast() {
        if (sToast != null) sToast.cancel();
        sToast = null;
    }

	/**
	 * show Toast long
	 * @param context Context
	 * @param int resId
	 */
    public static void showLong( Context context, int resId ) {
		makeText(context, resId, LENGTH_LONG).show();
	}

	/**
	 * show Toast long
	 * @param context Context
	 * @param CharSequence text
	 */
    public static void showLong( Context context, CharSequence text ) {
		makeText(context, text, LENGTH_LONG).show();
	}

	/**
	 * show Toast short
	 * @param context Context
	 * @param int resId
	 */
    public static void showShort( Context context, int resId ) {
		makeText(context, resId, LENGTH_SHORT).show();
	}

	/**
	 * show Toast short
	 * @param context Context
	 * @param CharSequence text
	 */
    public static void showShort( Context context, CharSequence text ) {
		makeText(context, text, LENGTH_SHORT).show();
	}
}
