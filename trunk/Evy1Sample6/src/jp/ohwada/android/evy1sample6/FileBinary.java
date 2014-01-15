package jp.ohwada.android.evy1sample6;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

/**
 * FileBinary
 */
public class FileBinary {

	// debug
	private final static String TAG_SUB = "FileBinary";

	private final static String CHAR_DOT = ".";
	private final static String EXT = "mid";
	
	/**
	 * constractor
	 */						
	public FileBinary() {
		// dummy
	}

	/**
	 * getFiles
	 * @return File[]
	 */	
	public File[] getFiles() {
    	File dir = new File( getDir() );
    	File[] files = dir.listFiles( getFilter( EXT ) ); 
    	return files;
	}

	/**
	 * readBinaryFile
	 * @param File file
     * @return byte[]
	 */	
	public byte[] readBinaryFile( File file ) {  
		log_d( "readBinaryFile" );
		InputStream is = null;
		try {
			is = new FileInputStream( file );
		} catch (IOException e) {
			if (Constant.D) e.printStackTrace();
		} 
		if ( is == null )	return null; 
		return readBinaryFile( is );
	}

    /** 
     * readBinaryFile
     * @param InputStream in
     * @return byte[]
     */  
    private byte[] readBinaryFile( InputStream in ) {  
    	int size = 0;
        byte[] data = new byte[1024];  
        ByteArrayOutputStream out = null;    
        try {  
            out = new ByteArrayOutputStream();  
            while ((size = in.read(data)) != -1 ) {  
                out.write(data, 0, size);  
            }
            return out.toByteArray();
        } catch (Exception e) {  
			if (Constant.D) e.printStackTrace();
        } finally {  
            try {  
                if (in != null) in.close();  
                if (out != null) out.close();  
            } catch (Exception e) {
    			if (Constant.D) e.printStackTrace();
            }  
        }
		return null;
    } 

    /** 
     * getDir
     * @return String
     */  
	private String getDir() {
		String path = Environment.getExternalStorageDirectory().getPath();
		String dir = path + File.separator + Constant.MAIN_DIR;
		return dir;
	}
	
	/**
	 * getFilter
	 * @param String _ext
	 * @return FilenameFilter
	 */ 
	private static FilenameFilter getFilter( String _ext ) {  
        final String ext = CHAR_DOT + _ext;  
        return new FilenameFilter() {  
            public boolean accept( File file, String name ) {  
                boolean ret = name.endsWith( ext );   
                return ret;  
            }  
        };  
    }  

	/**
	 * logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );	
	}  
}
