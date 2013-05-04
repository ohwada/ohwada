package jp.ohwada.android.nfccconcentration;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;

/*
 * Image Utility
 */
public class ImageUtility  {
	// customize 
	private boolean isUseAssetsFile = Constant.USE_ASSETS_FILE; 
	
	private final static int DRAWABLE_COFF = 2 ;
	
	private PreferenceUtility mPreference;
	private FileUtility mFileUtility;
	private String mMainPath = "";
	private String mSubPath = "";
		
	/**
	 * ImageUtility
	 * @param Context context  
	 */	
	public ImageUtility( Context context ) {
		mPreference = new PreferenceUtility( context );
		mFileUtility = new FileUtility( context );
		mMainPath = mFileUtility.getMainPath();
		restart();
	}

	/**
	 * restart
	 */	
	public void restart() {
		mSubPath = mMainPath + "/" + mPreference.getDir() ;
	}

	/**
	 * isDefualtDir
	 * @return boolean
	 */	
	public boolean isDefualtDir() {
		return mPreference.isDefualtDir();
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
	 * @param String name 
	 * @return boolean
	 */	
	public boolean existsFile( String name ) {
		boolean ret = false;
		if ( isUseAssetsFile && isDefualtDir() ) {
			ret = mFileUtility.existsAssetsFile( name );
		} else {
			ret = mFileUtility.existsFile( getPath( name ) );
		}
		return ret;
	}

	/**
	 * getBitmap 
	 * @param String name
	 * @return Bitmap
	 */	
	public Bitmap getBitmap( String name ) {
		Bitmap bitmap = null;
		if ( isUseAssetsFile && isDefualtDir() ) {
			bitmap = mFileUtility.getAssetsBitmap( name );
		} else {
			bitmap = mFileUtility.getBitmap( getPath( name ) );
		}
		return bitmap;
	}

	/**
	 * getBitmapByNum
	 * @param int num
	 * @return Bitmap
	 */		
	public Bitmap getBitmapByNum( int num ) {
		return getBitmap( getNameByNum( num ) );
	}

	/**
	 * getImagePath
	 * @param String file
	 * @return String
	 */	
	public String getPath( String file ) {
		String path = mSubPath  + "/" + file ;
		return path;
	}

	/**
	 * getImagePath
	 * @param int num
	 * @return String
	 */		
	public String getPathByNum( int num ) {
		return getPath( getNameByNum( num ) );
	}
	
	/**
	 * get Name
	 * @param int num
	 * @return String
	 */		
	public String getNameByNum( int num ) {
		return mFileUtility.getNameByNum( num );
	}

	/**
	 * show dialog
	 * @param CallLogRecord record
	 * @return void	 
	 */	
	public Spanned getHtmlImage( String msg, int num ) {
		ImageGetter imageGetter = new ImageGetter() { 
			@Override 
			public Drawable getDrawable( String source ) {
				Drawable d = Drawable.createFromPath( source );
				int w = DRAWABLE_COFF * d.getIntrinsicWidth();
				int h = DRAWABLE_COFF * d.getIntrinsicHeight();
				d.setBounds( 0, 0, w, h ); 
				return d; 
			} 
		}; 

		String path = getPathByNum( num );
        String html = msg + "<img src=\"" + path + "\">";
		Spanned spanned = Html.fromHtml( html, imageGetter, null );
		return spanned;
	}

}
