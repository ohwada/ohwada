package jp.ohwada.android.fontsample4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

/**
 * MyFont
 */
public class MyFont {

	public final static int MODE_INTERNAL = 0;
	public final static int MODE_EXTERNAL = 1;
	
    private final static Boolean D = true; 
	private final static String TAG = "MyFont";
	
	private final static String DIR_NAME = "fonts";
	private final static String EXT_ZIP = ".zip";
	private final static String EXT_TTF = ".ttf";
	
	private AssetManager mAssetManager;
	private String mInternalDir;
	private String mExternalDir;

	private String mZipFileName = "";
	private String mFontFileName = "";
	private int mMode = 0;

	/**
	 * MyFont
	 * @param Context context
	 * @param String name
	 * @param int mode
	 */
	public MyFont( Context context, String name, int mode ) {
		mAssetManager = context.getResources().getAssets();
		mInternalDir = context.getFilesDir().toString();
		mExternalDir = Environment.getExternalStorageDirectory().getPath() + "/" + DIR_NAME ;
		mZipFileName = name + EXT_ZIP;
		mFontFileName = name + EXT_TTF;
		mMode = mode;
	}

	/**
	 * initFile
	 */
	public boolean initFile() {
		return initFile( mZipFileName, mFontFileName );
	}

	/**
	 * initFile
	 * @param String zip
	 * @param String font
	 * @return boolean
	 */
	public boolean initFile( String zip, String font ) {
		boolean ret = existsFile( font );
		if ( ret ) return true;
		makeDir();
		return saveFile( zip, font );
	}

	/**
	 * existsFile
	 * @param String name
	 * @return boolean
	 */
	private boolean existsFile( String name ) {
		String path = getPath( name  );
		File file = new File( path );
		return file.exists();
	}

	/**
	 * makeDir
	 * @return boolean
	 */
	private boolean makeDir() {
		if ( mMode != MODE_EXTERNAL ) return true;
		// make dir if not exists
		File dir = new File( mExternalDir );
		if ( dir.exists() )  return true; 
		return dir.mkdir();
	}
		
	/**
	 * saveFile
	 * @param String zip
	 * @param String font
	 * @return boolean
	 */
	private boolean saveFile( String zip, String font ) {
		ZipInputStream zis = getAssetZipInputStream( zip );
		FileOutputStream fos = getFileOutputStream( font );
		if ( zis == null ) return false;
		if ( fos == null ) return false;
		return saveFile( zis, fos, font );		
	}

	/**
	 * getAssetZipInputStream
	 * @param String name
	 * @return ZipInputStream
	 */	
	private ZipInputStream getAssetZipInputStream( String name ) {
		ZipInputStream zis = null;
		try {
			InputStream is = mAssetManager.open( name, AssetManager.ACCESS_STREAMING );
			zis = new ZipInputStream( is );
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		if ( zis == null ) {
			log_d( "Zip file is null" );	
		}
		return zis;
	}

	/**
	 * getFileOutputStream
	 * @param String name
	 * @return FileOutputStream
	 */	
	private FileOutputStream getFileOutputStream( String name ) {
		FileOutputStream fos = null;
		String path = getPath( name );
		try {
			fos = new FileOutputStream( path, false );
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		if ( fos == null ) {
			log_d( "Output file is null " + path );	
		}
		return fos;
	}

	/**
	 * saveFile
	 * @param ZipInputStream zis
	 * @param FileOutputStream fos
	 * @param String fontName
	 * @return boolean
	 */
	private boolean saveFile( ZipInputStream zis, FileOutputStream fos, String fontName ) {
		if ( zis == null ) return false;
		if ( fos == null ) return false;
		if ( fontName == null ) return false;
		boolean ret = false;
		try {
			ZipEntry	ze	= zis.getNextEntry();
			if ( ze != null ) {
				String font_name = ze.getName() ;
				if ( fontName.equals( font_name )) {
					byte[] buf = new byte[1024];
					int size = 0;
					while ((size = zis.read(buf, 0, buf.length)) > -1) {
						fos.write(buf, 0, size);
					}
					ret = true;
				} else {
					String msg = "filename unmatch: " + fontName + " : " + font_name;
					log_d( msg );
				}	
				fos.close();
				zis.closeEntry();
			} else {
				log_d( "ZipEntry is null" );			
			}	
			zis.close();
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		if ( ret ) {
			log_d( "Success to save " + fontName );
		} else {
			log_d( "Failed to save " + fontName );
		}
		return ret;
	}

	/**
	 * getTypeface
	 * @return Typeface
	 */
	public Typeface getTypeface() {
		return getTypeface( mFontFileName );	
	}

	/**
	 * getTypeface
	 * @param String name
	 * @return Typeface
	 */
	public Typeface getTypeface( String name ) {
		Typeface type = null;
		String path = getPath( name );
		try {
			type = Typeface.createFromFile( path );
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		return type;	
	}

	/**
	 * getPath
	 * @param String name
	 * @return String
	 */
	private String getPath( String name ) {
		String path = getDir() + "/" + name;	
		return path;
	}
		 
	/**
	 * getDir
	 * @return String
	 */ 
	private String getDir() {
		String dir = mInternalDir;
		if ( mMode == MODE_EXTERNAL ) {
			dir = mExternalDir;
		}
		return dir;
	}

	/**
	 * log_d
	 * @param String msg
	 */
	private void log_d( String msg ) {		
		if (D) Log.d( TAG, msg );
    }
    	
}
