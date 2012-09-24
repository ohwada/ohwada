package jp.ohwada.android.imagesizesample;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
		iv1.setImageResource( R.drawable.sample );

        // Sample 2 : assets orignal
		ImageView iv2 = (ImageView) findViewById( R.id.imageview_sample_2 );
		Bitmap bm2 = getAssetsImage( "sample.png", false );
		iv2.setImageBitmap( bm2 );

        // Sample 3 : assets adjust
		ImageView iv3 = (ImageView) findViewById( R.id.imageview_sample_3 );
		Bitmap bm3 = getAssetsImage( "sample.png", true );
		iv3.setImageBitmap( bm3 );
		
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
	 * get Assets Image
	 * @param String name
	 * @param boolean density
	 * @return String
	 */     
    private Bitmap getAssetsImage( String name, boolean density ) {
    	Bitmap bm = null;
		try {
    		InputStream is = getResources().getAssets().open( name );
    		bm = BitmapFactory.decodeStream( is );
 			if ( density && ( bm != null )) {
				bm.setDensity( DisplayMetrics.DENSITY_MEDIUM );
			}	
		} catch (IOException e) {
		}
		return bm;
    }	
}
