package jp.ohwada.android.nfccconcentration;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/*
 * Image Utility
 */
public class ImageUtility  {

	// dubug
	private String TAG_SUB = "ImageUtility : ";
	private final static String TAG = Constant.TAG;
	private final static boolean D = Constant.DEBUG; 

	private final static String DIR_MAIN = Constant.DIR_MAIN;
	private final static String IMAGE_PREFIX = Constant.IMAGE_PREFIX ; 
	private final static String IMAGE_EXT = Constant.IMAGE_EXT ;

	private PreferenceUtility mPreference;
	private String mMainPath = "";
	private String mSubPath = "";
		
	/**
	 * ImageUtility
	 * @param Context context  
	 */	
	public ImageUtility( Context context ) {
		mPreference = new PreferenceUtility( context );
		String sd = Environment.getExternalStorageDirectory().getPath();
		mMainPath = sd + "/" + DIR_MAIN ;
		restart();
	}

	/**
	 * restart
	 */	
	public void restart() {
		mSubPath = mMainPath + "/" + mPreference.getDir() ;
	}

	/**
	 * getSubDirs
	 * @return String[]
	 */
	public String[] getSubDirs() {
		ArrayList<String> list = getSubDirList();
		int size = list.size();
		String[] dirs = new String[ size ];
		for ( int i=0; i < size; i++ ) {
			dirs[ i ] = list.get( i ); 
		} 
		return dirs;
	}
	
	/**
	 * getSubDirList(
	 * @return ArrayList<String>
	 */
	private ArrayList<String> getSubDirList() {
		ArrayList<String> list = new ArrayList<String>();
		File main = new File( mMainPath );
		File[] files = main.listFiles();
		for ( File f : files ) {
			if ( f.isDirectory() ) {
				list.add( f.getName() );
			}
		}
		return list;
	}
	
	/**
	 * existsFile
	 * @param String file
	 * @return boolean
	 */	
	public boolean existsFile( String filename ) {
		File file = new File( getPath( filename )  );
		return file.exists();
	}

	/**
	 * show Image 
	 * @param ImageView view
	 * @param int num
	 */		
	public void showImageByNum( ImageView view, int num ) {
		String file = IMAGE_PREFIX + num + "." + IMAGE_EXT ;
		showImage( view, file );
		log_d( "showImageByNum: " + num + " " + file );
	}
		
	/**
	 * show Image 
	 * @param ImageView view
	 * @param String file
	 */	
	public void showImage( ImageView view, String file ) {
		view.setImageBitmap( getBitmap( file ) );
	}

	/**
	 * getBitmap
	 * @param String file
	 * @return Bitmap
	 */	
	private Bitmap getBitmap( String file ) {
		Bitmap bitmap = BitmapFactory.decodeFile( getPath( file ) );
		if ( bitmap != null ) {
			bitmap.setDensity( DisplayMetrics.DENSITY_MEDIUM );
		}
		return bitmap;
	}

	/**
	 * getImagePath
	 * @param String file
	 * @return String
	 */	
	public String getPath( String file ) {
		String path = mSubPath  + "/" + file ;
		log_d("getPath " + path );
		return path;
	}
			           
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + msg );
	} 
}
