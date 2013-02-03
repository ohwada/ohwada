package jp.ohwada.android.pinqa1;

import java.io.File;

import jp.ohwada.android.pinqa1.task.ArticleFile;
import jp.ohwada.android.pinqa1.task.ArticleRecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * BitmapUtility
 */
public class BitmapUtility {

	private Context mContext;
	
	private int mDisplayWidth = 0;
	private float mDisplayDensity = 0;
	
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
    public BitmapUtility( Context context ) {
    	mContext = context;	
	}
	
    /**
	 * getBitmap
	 * @param String path
	 * @return Bitmap
	 */     
	public Bitmap getBitmap( String path, float ratio ) {
		if (( path == null )|| "".equals( path ) ) return null;
		Bitmap bitmap = BitmapFactory.decodeFile( path );
		if ( bitmap == null ) return null;
		// adjust image size
		getDisplayParam();	
		int width = (int)( (float)mDisplayWidth * ratio );
		Bitmap resize = resize( bitmap, width );
		return resize;	
	}

	/**
	 * getBitmap
	 * @param  int article_id 
	 * @return Bitmap
	 */ 	
	public Bitmap getBitmap( int article_id, float ratio ) {
		String path = getPath( article_id );
		if (( path == null )|| "".equals( path ) ) return null;
		return getBitmap( path, ratio );
	}

    /**
	 * resize
	 * @param Bitmap src
	 * @param int width 
	 * @return Bitmap
	 */ 
	private Bitmap resize( Bitmap src, int width ) {
        int src_width = src.getWidth();  
        int src_height = src.getHeight(); 
        int view_width = (int)( (float)src_width * mDisplayDensity );
        	
        float scale = 0;
		if ( view_width < width ) {
            // expand density, if fit in a display 
        	scale = mDisplayDensity;
        } else {
            // fit to the width of a screen
        	scale = (float)width / (float)src_width;
        } 
 
        Matrix matrix = new Matrix(); 
        matrix.postScale( scale, scale );
        
        Bitmap dst = Bitmap.createBitmap( src, 0, 0, src_width, src_height, matrix, true );
        return dst;
    }

	/**
	 * getPath
	 * @param  int article_id 
	 * @return String
	 */ 	
	private String getPath( int article_id ) {
		if ( article_id == 0 ) return "";
		ArticleFile file_class = new ArticleFile();
		File file_article = file_class.getFile( article_id );
		if ( !file_article.exists() ) return "";
		// read article file
		ArticleRecord r = file_class.read( file_article );
		if ( "".equals( r.image_name ) ) return "";
		File file_image = file_class.getFileFromName( r.image_name );
		if ( !file_image.exists() ) return "";
		// read image file
		return file_image.getPath();
	}

	/**
	 * getDisplayParam
	 */ 	
	@SuppressWarnings("deprecation")
	private void getDisplayParam() {
		WindowManager wm = (WindowManager) mContext.getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		// Display#getWidth() This method was deprecated in API level 13
		mDisplayWidth = display.getWidth();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics( metrics );
		mDisplayDensity = metrics.density;
	}
}