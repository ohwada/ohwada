package jp.ohwada.android.mindstormsgamepad.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.content.Context;

/**
 * File Utility
 */
public class FileUtility {
    
	/* Debug flag */
    private static final boolean D = Constant.DEBUG;
    	
    /* new line */
	private static final String LF = "\n";
	
	/* Context */
	private Context mContext;
    
	/**
	 * === Constructor ===
	 */	
	 public FileUtility( Context context ) {
		mContext = context;
	} 

	/**
	 * read Assets file lines
	 * @param String filename 
	 * @return String[]
	 */  	
	public String[] readAssetsFileLines( String filename ) {	
		StringBuilder sb = readAssetsFile( filename );
		String[] lines = getLineArray( sb );
		return lines;
	}
	    
	/**
	 * read Assets file
	 * @param String filename 
	 * @return StringBuilder
	 */  	
	public StringBuilder readAssetsFile( String filename ) {		
        StringBuilder sb = new StringBuilder(); 
		InputStream is = null;
		String line = ""; 			        
		try {
			is = mContext.getAssets().open( filename );
			BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
			// read file is read per line, and store in StringBuilder. 			
			while (( line = reader.readLine() ) != null ) {
			    sb.append( line + LF );   
			}
		} catch (IOException e) {
			if (D) e.printStackTrace();
		} finally {	
			// close file, if file is opened
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if (D) e.printStackTrace();
				}
			}
		}
		return sb;
	}
	
	/**
	 * get array of lines form StringBuilder
	 * @param String lines
	 * @return String
	 */  
	public String[] getLineArray( StringBuilder sb ) {
		String lines = sb.toString();
		// no action if NO content
		if ( lines.equals("") ) return null;
		return lines.split( LF );
	}
}