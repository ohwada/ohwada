package jp.ohwada.android.osm2.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.ohwada.android.osm2.Constant;

/**
 * Manage File
 */
public class ManageFile extends CommonFile { 
   	   
	private static final int MAX_FILES = Constant.MAX_FILES;
   	    
	/**
	 * === constractor ===
	 */
    public ManageFile() {
    	super();
    }

	/**
	 * init
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
	 * clear all Cache
	 */
	public void clearAllCache() {
		// get file list
		File dir = new File( getDir() );
		File[] files = dir.listFiles();
		// delete file 
		for ( int i=0; i<files.length; i++ ) {
			File f = files[ i ];
			if ( f != null ) {
				f.delete();
			}
		}
	}

	/**
	 * clear old Cache
	 */
	public void clearOldCache() {
		// get file list
		File dir = new File( getDir() );
		File[] files = dir.listFiles();
		if ( files.length <= MAX_FILES ) return;
		// create file list
		List<File> list = new ArrayList<File>();
		for ( int i=0; i<files.length; i++ ) {
			File f = files[ i ];
			if ( f != null ) {
				list.add( f );
			}
		}
		if ( list.size() <= MAX_FILES ) return;
		// sort by lastModified
		Collections.sort( list, new FileComparator() );		        
		// delete file 
        for ( int i=MAX_FILES; i<list.size(); i++ ) {
        	File f = list.get( i );
        	if ( f != null ) {
				f.delete();
			}
        }
	}

	/**
	 * === class FileComparator ===
	 */	
	private class FileComparator implements Comparator<File> {
        public FileComparator() {
			// dummy
        }

		/**
	 	 * compare: sort in new order by lastModified
	 	 */
        @Override
        public int compare( File file1, File file2 ) {
            long time1 = file1.lastModified();
            long time2 = file2.lastModified();
            if ( time1 >  time2 ) {
                return -1;
            } else if ( time1 == time2 ) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}