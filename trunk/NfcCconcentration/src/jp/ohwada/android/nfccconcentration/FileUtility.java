package jp.ohwada.android.nfccconcentration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

/**
 * File Utility
 */
public class FileUtility {

	// dubug
	private String TAG_SUB = "FileUtility : ";
	private final static String TAG = Constant.TAG;
	private final static boolean D = Constant.DEBUG; 

	private final static String DIR_MAIN = Constant.DIR_MAIN;
	private final static String DIR_SUB_DEFAULT = Constant.DIR_SUB_DEFAULT;
		 
	private final static String LIST_PATH = "";
	private final static int EOF = -1;
	private final static int BUFFER_SIZE = 1024 * 4;
	
	private final static String[] IGNORE_FILES 
		= new String[] { "images", "sounds", "webkit" };
		 
 	/* Context */
	private AssetManager mManager;

	private String mMainPath;
	
	/**
     * === constractor ===
	 * @param Context context  
	 */	    
	 public FileUtility( Context c ) {
		mManager = c.getAssets();
		String sd = Environment.getExternalStorageDirectory().getPath();
		mMainPath = sd + "/" + DIR_MAIN ;
	} 

	/**
	 * --- init  ---
	 */ 
	public void init() {
		log_d( "init" );
		// make dir if not exists
		File main_dir = new File( mMainPath  );
		if ( !main_dir.exists() ) { 
			main_dir.mkdir();
		}
		// make dir & copy files if not exists
		File sub_dir = new File( getSubPath()  );
		if ( !sub_dir.exists() ) { 
			sub_dir.mkdir();
			copyFiles();
		}
	}
	
	/**
	 * getSubPath
	 * @return String
	 */  
	private String getSubPath() {
		String path = mMainPath + "/" + DIR_SUB_DEFAULT ;
		return path;
	}
			    
	/**
	 * Copy Assets files to SD
	 * @return boolean
	 */  
	private boolean copyFiles() {
		boolean is_error = false;	
		String[] files = getAssetsList();
		if ( files == null ) {
			log_d( "copyFiles file null" );
			return false;
		}
		int length = files.length;
		if ( length == 0 ) {
			log_d( "copyFiles file zero" );
			return false;
		}
		// copy all files
		for ( int i=0; i < length; i ++ ) {
			String src_file = files[i];
			log_d( src_file );
			// ignore images, sounds, webkit
			if ( matchFile( src_file ) ) continue;
			String dst_file = getSubPath() + "/" + src_file ;
			boolean ret = copyAssetsToSd( src_file, dst_file );
			if ( !ret ) {
				is_error = true;	
				log_d( "copy error " + src_file );
			}
		}
		return !is_error;	
	}

	/**
	 * match
	 * @param String file
	 * @return boolean
	 */  
	private boolean matchFile( String file ) {
		for ( int i=0; i < IGNORE_FILES.length; i ++ ) {
			if ( file.equals( IGNORE_FILES[ i ] ) ) return true;
		}
		return false;
	}		

	/**
	 * get AssetsList
	 * @return String[]
	 */  
	public String[] getAssetsList() {
		String[] list = null;
		try {
			list = mManager.list( LIST_PATH );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * copyAssetsToSd
	 * @param src_file
	 * @param dst_file
	 * @return boolean
	 */  	
	private boolean copyAssetsToSd( String src_file, String dst_file ) {
		boolean is_error = false;		
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[ BUFFER_SIZE ];
		int n = 0;
  			        
		try {
			is = mManager.open( src_file );
			os = new FileOutputStream( dst_file );

			// copy input to output		
			while ( EOF != (n = is.read(buffer))) {
				os.write( buffer, 0, n );
			}

		} catch ( IOException e ) {
		 	is_error = true;	
			e.printStackTrace();

		} finally {		
			// close if input file is open
			if ( is != null ) {
				try {
					is.close();
				} catch ( IOException e ) {
					is_error = true;	
					e.printStackTrace();
				}
			}
			// close if output file is open
			if ( os != null ) {
				try {
					os.close();
				} catch ( IOException e ) {
					is_error = true;	
					e.printStackTrace();
				}
			}
		}
		
		return !is_error;
	}
	
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + msg );
	} 
}