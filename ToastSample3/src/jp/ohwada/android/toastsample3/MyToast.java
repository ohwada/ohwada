package jp.ohwada.android.toastsample3;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MyToast
 */
public class MyToast {

    /**
     * Construct an empty Toast object. 
     */
    public MyToast() {
		// dummy
	}
         	
	/**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  
	 * @param size  The size of text (sp) 
	 * @return MyToast
     *
     */
    public static Toast makeText( Context context, CharSequence text, int duration, float size ) {
    	Toast result = new Toast( context );
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflate.inflate( R.layout.toast, null );
        TextView tv = (TextView) v.findViewById( R.id.message );
        tv.setText( text );       
        tv.setTextSize( size );    
        result.setView( v );
        result.setDuration( duration );
        return result;
    }

    public static Toast makeText( Context context, CharSequence text, int duration ) {
    	return makeText( context, text, duration, 14f );
    }
            
    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.   
     * @param resId    The resource id of the string resource to use.  
     * @param duration How long to display the message.  
     * @param size  The size of text (sp) 
	 * @return MyToast
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
     public static Toast makeText( Context context, int resId, int duration, float size )
                                throws Resources.NotFoundException {
        return makeText( context, context.getResources().getText(resId), duration, size );
    }
    
	public static Toast makeText( Context context, int resId, int duration ) {
    	return makeText( context, resId, duration, 14f );
    }
}
