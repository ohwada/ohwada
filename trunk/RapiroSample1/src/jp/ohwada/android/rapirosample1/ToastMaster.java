package jp.ohwada.android.rapirosample1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/*
 * ToastMaster
 */ 
public class ToastMaster {

    private static Toast sToast = null;

	/*
	 * ToastMaster
	 */
    private ToastMaster() {
		// dummy
    }

	/*
	 * makeText
     * @param Context context 
     * @param CharSequence text
     * @param int duration  
	 * @return Toast
	 */
	@SuppressLint("ShowToast")
	public static Toast makeText( Context context, CharSequence text, int duration ) {
		Toast toast = Toast.makeText( context, text, duration );
        setToast( toast );
        return toast;
    }

	/*
	 * makeText
     * @param Context context 
     * @param int resId
     * @param int duration  
	 * @return Toast
	 */
     public static Toast makeText( Context context, int resId, int duration )
                                throws Resources.NotFoundException {
        return makeText( context, context.getResources().getText(resId), duration );
    }
    
	/*
	 * setToast
	 * @param Toast toast
	 */
    public static void setToast( Toast toast ) {
        if ( sToast != null ) {
            sToast.cancel();
		}  
        sToast = toast;
    }

	/*
	 * cancelToast
	 */
    public static void cancelToast() {
        if ( sToast != null ) {
            sToast.cancel();
		}            
        sToast = null;
    }

}
