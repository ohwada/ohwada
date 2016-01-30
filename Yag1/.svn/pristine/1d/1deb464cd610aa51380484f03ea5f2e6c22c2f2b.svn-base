package jp.ohwada.android.yag1.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

import jp.ohwada.android.yag1.Constant;

/**
 * Common file utility
 */
public class CommonFile  { 

	// dubug
    private final static boolean D = Constant.DEBUG; 
	
	// constant
	protected final static long TIME_MSEC_ONE_DAY = Constant.TIME_MSEC_ONE_DAY; 
	protected final static String LF = Constant.LF ;
	private final static String DIR_NAME = Constant.DIR_NAME;
	private final static String EXT_TXT = "txt";
			
	// object
	protected DateUtility mDateUtility;
   	    
	/**
	 * === constractor ===
	 */
    public CommonFile() {
		mDateUtility = new DateUtility();
    }

	/**
	 * getFile
	 * @param String name
	 * @return File
	 */
	protected String getFilenameWithTxt( String name ) {
		String filename = name + "." + EXT_TXT;	
		return filename;
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
		long today_msec = mDateUtility.getTimeToday();

		// if not expired	
		if ( today_msec > ( file_msec + expire_msec ) ) return true;
		return false;
	}
						
	/**
	 * get SD dirctory
	 * @return String
	 */ 
	private String getSdDir() {
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
	private boolean writeData( File file, String data, boolean append ) {
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
	 * --- parseExt ---
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
}