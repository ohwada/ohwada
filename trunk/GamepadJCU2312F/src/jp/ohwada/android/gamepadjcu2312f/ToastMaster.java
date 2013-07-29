package jp.ohwada.android.gamepadjcu2312f;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ToastMaster
 */ 
public class ToastMaster {

    private static Toast sToast = null;

	/**
	 * constractor
	 */ 
    public ToastMaster() {
		// dummy
    }

	/**
	 * setToast
	 * @param Toast toast
	 */ 
    public static void setToast( Toast toast ) {
        if (sToast != null) {
            sToast.cancel();
        }    
        sToast = toast;
    }

	/**
	 * cancelToast
	 */ 
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }    
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
        showTextCenter( toast );
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
        showTextCenter( toast );
	} 

	/**
	 * showText
	 * @param Toast toast
	 */
	private static void showTextCenter( Toast toast ) {
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, -50 );
        setToast( toast );
        toast.show();
	}         
}
