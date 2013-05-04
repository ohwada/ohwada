package jp.ohwada.android.nfccconcentration;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;

/**
 * File Utility
 */
public class FileUtility {
	// dubug
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
	 public FileUtility( Context context  ) {
		mManager = context.getAssets();
		String sd = Environment.getExternalStorageDirectory().getPath();
		mMainPath = sd + "/" + DIR_MAIN ;
	} 

	/**
	 * --- init  ---
	 */ 
	public void init() {
		// make dir if not exists
		File main_dir = new File( mMainPath  );
		if ( !main_dir.exists() ) { 
			main_dir.mkdir();
		}
		// make dir & copy files if not exists
		File sub_dir = new File( getDefaultPath()  );
		if ( !sub_dir.exists() ) { 
			sub_dir.mkdir();
		}
		if ( Constant.USE_COPY_FILES ) {
			String path = getDefaultPath() + "/" +  getNameByNum( 1 );
			if ( !existsFile( path )) {
				copyFiles();
			}
		}
	}

	/**
	 * getNameByNum
	 * @param int num
	 * @return String
	 */		
	public String getNameByNum( int num ) {
		String name = Constant.IMAGE_PREFIX + num + "." + Constant.IMAGE_EXT ;
		return name;
	}

	/**
	 * getMainPath
	 * @return String
	 */  
	public String getMainPath() {
		return mMainPath ;
	}
		
	/**
	 * getDefaultPath
	 * @return String
	 */  
	private String getDefaultPath() {
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
		if ( files == null ) return false;
		int length = files.length;
		if ( length == 0 ) return false;
		// copy all files
		for ( int i=0; i < length; i ++ ) {
			String src_file = files[i];
			// ignore images, sounds, webkit
			if ( matchFile( src_file ) ) continue;
			String dst_file = getDefaultPath() + "/" + src_file ;
			boolean ret = copyAssetsToSd( src_file, dst_file );
			if ( !ret ) {
				is_error = true;	
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
			if (D) e.printStackTrace();
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
			if (D) e.printStackTrace();

		} finally {		
			// close if input file is open
			if ( is != null ) {
				try {
					is.close();
				} catch ( IOException e ) {
					is_error = true;	
					if (D) e.printStackTrace();
				}
			}
			// close if output file is open
			if ( os != null ) {
				try {
					os.close();
				} catch ( IOException e ) {
					is_error = true;	
					if (D) e.printStackTrace();
				}
			}
		}
		
		return !is_error;
	}

	/**
	 * existsAssetsFile
	 * @param String name
	 * @return boolean
	 */     
    public boolean existsAssetsFile( String name ) {
		try {
    		InputStream is = mManager.open( name );
			if ( is != null ) return true;
		} catch (IOException e) {
			if (D) e.printStackTrace();
		}
		return false;
    }

	/**
	 * existsFile
	 * @param String path
	 * @return boolean
	 */	
	public boolean existsFile( String path ) {
		File file = new File( path );
		return file.exists();
	}

	/**
	 * get Assets Image
	 * @param String name
	 * @return String
	 */     
    public Bitmap getAssetsBitmap( String name ) {
    	Bitmap bitmap = null;
		try {
    		InputStream is = mManager.open( name );
    		bitmap = BitmapFactory.decodeStream( is );
 			if ( bitmap != null ) {
				bitmap.setDensity( DisplayMetrics.DENSITY_MEDIUM );
			}	
		} catch (IOException e) {
			if (D) e.printStackTrace();
		}
		return bitmap;
    }

	/**
	 * getBitmap
	 * @param String path
	 * @return Bitmap
	 */	
	public Bitmap getBitmap( String path ) {
		Bitmap bitmap = BitmapFactory.decodeFile( path);
		if ( bitmap != null ) {
			bitmap.setDensity( DisplayMetrics.DENSITY_MEDIUM );
		}
		return bitmap;
	}

	/**
	 * getAssetFileDescriptor
	 * @param String name
	 * @return AssetFileDescriptor
	 */
	public AssetFileDescriptor getAssetFileDescriptor( String name ) {
		AssetFileDescriptor afd = null;
		try {
			afd = mManager.openFd( name );
		} catch (IOException e) {
			if (D) e.printStackTrace();
		}
		return afd;
	}

	/**
	 * getFileDescriptor
	 * @param String path
	 * @return FileDescriptor
	 */
	public FileDescriptor getFileDescriptor( String path ) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream( new File( path ) );
			return fis.getFD();
		} catch (FileNotFoundException e) {
			if (D) e.printStackTrace();
		} catch (IOException e) {
			if (D) e.printStackTrace();
		}
		return null;
	}

}