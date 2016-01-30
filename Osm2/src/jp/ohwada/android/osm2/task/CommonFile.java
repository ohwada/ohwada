package jp.ohwada.android.osm2.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import jp.ohwada.android.osm2.Constant;
import android.os.Environment;
import android.util.Log;

/**
 * Common File 
 */
public class CommonFile { 

	// dubug
	protected final static boolean D = Constant.DEBUG; 
	private static final String TAG = Constant.TAG;
	protected String TAG_SUB = "CommonFile";

	// constant
	protected final static long TIME_MSEC_ONE_DAY = Constant.TIME_MSEC_ONE_DAY; 
	protected final static String DIR_NAME = Constant.DIR_NAME;
	protected final static String LF = Constant.LF ;
    	
	/**
	 * === constractor ===
	 */
    public CommonFile() {
    	// dummy
    }
			
	/**
	 * getFile
	 * @param String name
	 * @return File
	 */
	protected File getFileWithTxt( String name ) {
		return getFileFromName( name + ".txt" );
	}

	/**
	 * getFile
	 * @param String name
	 * @return File
	 */
	protected File getFileFromName( String name ) {
		File file = new File( getPath( name ) );
		return file;
	}
	
	/**
	 * isExpiredFile
	 * @param File file
	 * @param long expire_msec
	 * @return boolean
	 */
    protected boolean isExpiredFile( File file, long expire_msec ) {
		// if not exists
		if ( !file.exists() ) return true;		
		long file_msec = file.lastModified();		
		long today_msec = getTimeToday();

		// if not expired	
		if ( today_msec > ( file_msec + expire_msec ) ) return true;
		return false;
	}
						
	/**
	 * get SD dirctory
	 * @return String
	 */ 
	protected String getSdDir() {
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * get dirctory
	 * @return String
	 */ 
	protected String getDir() {
		String dir = getSdDir() + "/" + DIR_NAME  ;
		return dir;
	}

	/**
	 * getPath
	 * @param String name
	 * @return File
	 */
	protected String getPath( String name ) {
		String path = getDir() + "/" + name ;
		return path;
	}
    	

	/**
	 * parseExt
	 * @param String name 
	 * @return String
	 */ 	
	public String parseExt( String name ) {
		int point = name.lastIndexOf(".");
		if ( point != -1 ) {
			String str = name.substring( point + 1 );
			return str.toLowerCase();
		}
		return "";
	}  

    /**
     * getTimeToday   
	 * @return long	 
     */
	private long getTimeToday() {
		Calendar cal= Calendar.getInstance();
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * writeData
	 * @param File file
	 * @param String data
	 */
    protected void writeAfterDelete( File file, String data ) {
		// delete if exists
		if ( file.exists() ) { 
			file.delete();
		}
		writeData( file, data, false );
	}
		
	/**
	 * write
	 * @param File file
	 * @param String data
	 * @param boolean append
	 * @return boolean
	 */ 
	protected boolean writeData( File file, String data, boolean append ) {
		boolean ret = false;
		OutputStream os = null;
		try {
			os = new FileOutputStream( file, append );
			os.write( data.getBytes() );
		} catch ( FileNotFoundException e ) {
			if (D) e.printStackTrace();
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
		}
		if ( os != null ) {
			try {
				os.close();
				ret = true;
			} catch ( IOException e ) {
				if (D) e.printStackTrace();
			}
		}
		return ret;
	}
		
	/**
	 * read
	 * @param File file
	 * @return String
	 */ 
	protected String readData( File file ) {
		String data = null;
		InputStream is = null;
		try {
			is = new FileInputStream( file );
			byte[] bytes = new byte[ is.available() ];
			is.read( bytes );
			data = new String( bytes );
		} catch ( FileNotFoundException e ) {
			if (D) e.printStackTrace();
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
		}
		if ( is != null ) {
			try {
				is.close();
			} catch ( IOException e ) {
				if (D) e.printStackTrace();
			}
		}
		return data;
	}
			
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
 				
}