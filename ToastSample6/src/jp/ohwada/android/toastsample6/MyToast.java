package jp.ohwada.android.toastsample6;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MyToast
 */
public class MyToast {

	/**
	 * constractor
	 */ 
	public MyToast() {
		// dummy
	}

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
	public static Toast makeText( Context context, CharSequence text, int duration ) {
        LayoutInflater inflater = (LayoutInflater)
        		context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.toast_layout, null );
		ImageView iv = (ImageView) layout.findViewById( R.id.image );
		iv.setImageResource( R.drawable.android );
		TextView tv = (TextView) layout.findViewById( R.id.text );
		tv.setText( text );

		Toast toast = new Toast( context );
		toast.setView( layout );
		toast.setDuration( duration );
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0 );
		return toast;
	}

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
	public static Toast makeText( Context context, int resId, int duration )
    		throws Resources.NotFoundException {
        return makeText( context, context.getResources().getText(resId), duration );		
	} 
 
}
