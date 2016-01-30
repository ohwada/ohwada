package jp.ohwada.android.mindstormsgamepad.view;

import android.content.Context;
import android.widget.Toast;

/**
 * prevent to freeze Toast
 */
public class ToastMaster {
	private static Toast sToast = null;

	/**
	 * Constructor
	 */
    public ToastMaster() {
		// dummy
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
     * Show a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
	public static void showText( Context context, CharSequence text, int duration ) {
        Toast toast = Toast.makeText( context, text, duration );
		setToast( toast );
		toast.show();
	}        

    /**
     * Show a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
	public static void showText( Context context, int resId, int duration ) {
        Toast toast = Toast.makeText( context, resId, duration );
		setToast( toast );
		toast.show();
	} 

	/**
	 * show Toast long
	 * @param context Context
	 * @param int resId
	 */
    public static void showLong( Context context, int resId ) {
		showText( context, resId, Toast.LENGTH_LONG );
	}

	/**
	 * show Toast long
	 * @param context Context
	 * @param CharSequence text
	 */
    public static void showLong( Context context, CharSequence text ) {
    	showText( context, text, Toast.LENGTH_LONG );
	}

	/**
	 * show Toast short
	 * @param context Context
	 * @param int resId
	 */
    public static void showShort( Context context, int resId ) {
		showText( context, resId, Toast.LENGTH_SHORT );
	}

	/**
	 * show Toast short
	 * @param context Context
	 * @param CharSequence text
	 */
    public static void showShort( Context context, CharSequence text ) {
		showText( context, text, Toast.LENGTH_SHORT );
	}
}
