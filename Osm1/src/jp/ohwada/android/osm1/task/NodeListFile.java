package jp.ohwada.android.osm1.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jp.ohwada.android.osm1.Constant;

import android.os.Environment;
import android.util.Log;

/**
 * NodeList file 
 */
public class NodeListFile { 

	// dubug
    private final static boolean D = Constant.DEBUG; 
	private static final String TAG = Constant.TAG;
	private String TAG_SUB = "NodeListFile";

	// constant
	private final static long TIME_MSEC_ONE_DAY = DateUtility.TIME_MSEC_ONE_DAY; 
	private static final long EXPIRE_NODE_LST = 2L * TIME_MSEC_ONE_DAY;  // 2 day
	private final static String LF = Constant.LF ;
	private final static String DIR_NAME = Constant.DIR_NAME;
		
	// object
	private DateUtility mDateUtility;
    	
	/**
	 * === constractor ===
	 */
    public NodeListFile() {
    	mDateUtility = new DateUtility();
    }

	/**
	 * --- init  ---
	 * @param none
	 * @return void
	 */ 
	public void init() {
		// make dir if not exists
		File dir = new File( getDir() );
		if ( !dir.exists() ) { 
			dir.mkdir();
		}
	}

	/**
	 * --- clearCache ---
	 */
	public void clearCache() {
		File dir = new File( getDir() );
		File[] files = dir.listFiles();
		for ( int i=0; i<files.length; i++ ) {
			File f = files[ i ];
			if ( f != null ) {
				f.delete();
			}
		}
	}
		
	/**
	 * getFile
	 * @param Date date
	 * @return File
	 */
    public File getFile( int lat, int lng ) {
    	String name = "node_" + e6ToE3( lat ) + "_" + e6ToE3( lng );
		return getFileWithTxt( name );
	}
		
	/**
	 * isExpired Node
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, EXPIRE_NODE_LST );
	}

	/**
	 * write NodeList
	 * @param File file,
	 * @param List<EvenRecord> list
	 */
	public void write( File file, List<NodeRecord> list ) {
		write( file, new NodeList( list ) );
	}
			
	/**
	 * write NodeList
	 * @param File file
	 * @param NodeList list
	 */
	public void write( File file, NodeList list ) {
		String data = list.buildWriteData();
    	writeAfterDelete( file, data );
	}

	/**
	 * read NodeList
	 * @param File file
	 * @return NodeList
	 */
    public NodeList read( File file ) {
		NodeList list = new NodeList();
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) { 
		    log_d( "read no data" );
			return list;
		}	
		String[] lines = data.split( LF );
		// build read data list
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( !"".equals( line ) ) { 
				list.addWithMapColor( new NodeRecord( line ) );
			}	
		}
		return list;
	}

	/**
	 * e6ToE3
	 * @param int e6
	 * @return int
	 */
    private int e6ToE3( int e6 ) {
   		int e3 = e6 / 1000;
   		return e3;
	}
	
	/**
	 * getFile
	 * @param String name
	 * @return File
	 */
	private File getFileWithTxt( String name ) {
		return getFileFromName( name + ".txt" );
	}

	/**
	 * getFile
	 * @param String name
	 * @return File
	 */
	private File getFileFromName( String name ) {
		File file = new File( getPath( name ) );
		return file;
	}
	
	/**
	 * isExpiredFile
	 * @param File file
	 * @param long expire_msec
	 * @return boolean
	 */
    private boolean isExpiredFile( File file, long expire_msec ) {
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
	private String getDir() {
		String dir = getSdDir() + "/" + DIR_NAME  ;
		return dir;
	}

	/**
	 * getPath
	 * @param String name
	 * @return File
	 */
	private String getPath( String name ) {
		String path = getDir() + "/" + name ;
		return path;
	}
    	
	/**
	 * writeData
	 * @param File file
	 * @param String data
	 */
    private void writeAfterDelete( File file, String data ) {
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
	private String readData( File file ) {
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
	
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}
 				
}