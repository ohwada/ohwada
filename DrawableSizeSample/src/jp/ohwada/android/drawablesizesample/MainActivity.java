package jp.ohwada.android.drawablesizesample;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Main Activity
 */
public class MainActivity extends Activity {

	// line feed
	private final static String LF = "\n";

	// varibale
	private int mDisplayWidth = 0;
  	private int mDisplayHeight = 0;	
  	private float mDensity = 0;
  			
	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */	
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        // Display Info
		TextView tv_display = (TextView) findViewById( R.id.textview_display );
		tv_display.setText( getDisplayInfo() );
				
        // Sample 1 :  res/drawable
		ImageView iv1 = (ImageView) findViewById( R.id.imageview_sample_1 );
		Drawable d1 = getResources().getDrawable( R.drawable.sample );
		iv1.setImageDrawable( d1 );

        // Sample 2 : assets orignal
		ImageView iv2 = (ImageView) findViewById( R.id.imageview_sample_2 );
		Drawable d2 = getAssetsDrawable( "sample.png", false );
		iv2.setImageDrawable( d2 );

        // Sample 3 : assets bitmap
		ImageView iv3 = (ImageView) findViewById( R.id.imageview_sample_3 );
		Bitmap bm3 = ( (BitmapDrawable) d2 ).getBitmap();
		iv3.setImageBitmap( bm3 );

        // Sample 4 : assets adjust
		ImageView iv4 = (ImageView) findViewById( R.id.imageview_sample_4 );
		Drawable d4 = getAssetsDrawable( "sample.png", true );
		iv4.setImageDrawable( d4 );
				
		// Window Info
		final TextView tv_window = (TextView) findViewById( R.id.textview_window );
		LinearLayout ll_main = (LinearLayout) findViewById( R.id.linearlayout_main );
		ll_main.post( new Runnable() {
  			public void run() {
  				tv_window.setText( getWindowInfo() );
			}
		});
	}

	/**
	 * get display info
	 * @return String
	 */ 
    private String getDisplayInfo() {
 		DisplayMetrics metrics = new DisplayMetrics();  
 		getWindowManager().getDefaultDisplay().getMetrics( metrics );
  		mDisplayWidth = metrics.widthPixels;
  		mDisplayHeight = metrics.heightPixels;	
  		mDensity = metrics.density;
		       
        String str = "";
  		str += "Display: " + mDisplayWidth + " x " + mDisplayHeight + " px " + LF;  
  		str += "Density: " + mDensity ;  
  		return str;     
    }

	/**
	 * get window info
	 * @return String
	 */ 
    private String getWindowInfo() {
		Rect rect= new Rect();
		Window window = getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame( rect );
		int status_bar = rect.top;
		int view_top = window.findViewById( Window.ID_ANDROID_CONTENT ).getTop();
		int title_bar = view_top - status_bar;
		int content_top = status_bar + title_bar ;
		int window_px = mDisplayHeight - content_top;
		int window_dp = (int) ( window_px / mDensity );
						       
        String str = "";
  		str += "StatusBar + TitleBar Height: " + content_top + " px " +  LF;  
  		str += "Window Height: " + window_px + " px, " +  window_dp  + " dp ";  
  		return str;     
    }

	/**
	 * get Assets Drawable
	 * @param String name
	 * @param boolean density
	 * @return Drawable
	 */     
    private Drawable getAssetsDrawable( String name, boolean adjust ) {
    	Drawable d = null;
		try {
    		InputStream is = getResources().getAssets().open( name );
    		d =  Drawable.createFromStream( is, name );
 			if ( adjust && ( d != null )) {
				d = adjustDrawable( d );
			}	
		} catch (IOException e) {
		}
		return d;
    }
    
	/**
	 * adjust Drawable
	 * @param Drawable d
	 * @return Drawable
	 */     
    private Drawable adjustDrawable( Drawable d ) {
    	float density = getResources().getDisplayMetrics().density;
    	float scale = density * density;
    	Bitmap b_orig = ( (BitmapDrawable) d ).getBitmap();
    	Bitmap b_sclae = scaleBitmap( b_orig, scale );
		BitmapDrawable d_adjust = new BitmapDrawable( b_sclae);	
		return d_adjust;
    }
    
	/**
	 * scaleBitmap
	 * @param Bitmap b_orig
	 * @param float sclae
	 * @return Bitmap
	 */     
    private Bitmap scaleBitmap( Bitmap b, float scale ) {
		Matrix m = new Matrix();
		m.postScale( scale, scale );
		Bitmap b_sclae = Bitmap.createBitmap( b, 0, 0, b.getWidth(), b.getHeight(), m, true );
		return b_sclae;
    }	
}
