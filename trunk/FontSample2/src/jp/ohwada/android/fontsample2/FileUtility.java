package jp.ohwada.android.fontsample2;

import java.io.File;
import java.io.FilenameFilter;

/**
 * FileUtility
 */ 
public class FileUtility {

	/**
	 * constractor
	 */ 
	public FileUtility() {
		// dummy
	}

	/**
	 * getFiles
	 * @param String path
	 * @param String ext
	 * @return File[]	 
	 */	
	public static File[] getFiles( String path, String ext ) {
		File file = new File( path );
		return file.listFiles( getFilenameFilter( ext ) );
	}

	/**
	 * getFilenameFilter
	 * @param String ext	 
	 */		
	private static FilenameFilter getFilenameFilter( String ext ) {
		final String _ext = "." + ext;
		return new FilenameFilter() {
			public boolean accept( File file, String name ) {
				boolean ret = name.endsWith( _ext ); 
				return ret;
			}
		};
	}
	
}
