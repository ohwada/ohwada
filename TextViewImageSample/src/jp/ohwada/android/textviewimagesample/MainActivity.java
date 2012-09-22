package jp.ohwada.android.textviewimagesample;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.widget.TextView;

/*
 * Main Activity
 */
public class MainActivity extends Activity {

	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */	
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
		// Sample 1 :  : setCompoundDrawablesWithIntrinsicBounds( Resource ID )
		TextView tv1 = (TextView) findViewById( R.id.textview_sample_1 );
		tv1.setText( R.string.hello_world );
		tv1.setCompoundDrawablesWithIntrinsicBounds( R.drawable.icon, 0, 0, 0 );

        // Sample 2 : setCompoundDrawablesWithIntrinsicBounds( Drawable )
		TextView tv2 = (TextView) findViewById( R.id.textview_sample_2 );		
		Drawable icon = getDrawableIcon( R.drawable.icon );
		tv2.setText( R.string.hello_world );
		tv2.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );

        // Sample 3 : setCompoundDrawables( Drawable )
		TextView tv3 = (TextView) findViewById( R.id.textview_sample_3 );		
		tv3.setText( R.string.hello_world );
		tv3.setCompoundDrawables( icon, null, null, null );		        

        // Sample 4 : setText( Spanned )
		TextView tv4 = (TextView) findViewById( R.id.textview_sample_4 );
		Spanned spanned = getSpannedIcon( R.drawable.icon, R.string.hello_world );		
		tv4.setText( spanned );
    } 

	/**
	 * getDrawableIcon
	 * @param int res_id
	 * @return Drawable
	 */
	private Drawable getDrawableIcon( int res_id ) {  
		Drawable d = getResources().getDrawable( res_id );
        d.setBounds( 0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight() );
        return d;
	}

	/**
	 * getSpannedIcon
	 * @param int img_id
	 * @param int text_id
	 * @return Spanned 
	 */	        
	private Spanned getSpannedIcon( int img_id, int text_id )  { 
        String html = "<img src=\"" + img_id + "\">";
        html += " ";        
        html += getResources().getString( text_id );  
		Spanned s = Html.fromHtml( html, mImageGetter, null );
		return s;
	}

	/**
	 * class ImageGetter
	 */	 
	private ImageGetter mImageGetter = new ImageGetter() { 
		/**
	 	 * getDrawable
	 	 * @param String source
	 	 * @return Drawable 
	 	 */	
		@Override 
		public Drawable getDrawable( String source ) {
			int id = Integer.parseInt( source ); 
			Drawable d = getDrawableIcon( id );
			return d; 
		} 
	};	 
}
