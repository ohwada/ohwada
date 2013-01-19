package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * PlaceList file
 */
public class PlaceListFile extends CommonFile { 

	// constant
	private final static String FILE_NAME = "places.txt";
    private static final long EXPIRE_PLACE = 30L * TIME_MSEC_ONE_DAY;  // 30 day

	// varibale
	private File mFileTarget;
	    	
	/**
	 * === constractor ===
	 */
    public PlaceListFile() {
        mFileTarget = getFileFromName( FILE_NAME );
    }

	/**
	 * getFileTarge
	 * @return File
	 */
    public File getFileTarget() {
		return mFileTarget;
	}
	  
	/**
	 * isExpired Place
	 * @return boolean
	 */
    public boolean isExpiredList() {
		return isExpiredFile( mFileTarget, EXPIRE_PLACE );
	}

	/**
	 * isExpired Place
	 * @return boolean
	 */
    public boolean existsList() {
		return mFileTarget.exists();
	}
	
	/**
	 * write PlaceList
	 * @param PlaceList list
	 */
    public void write( PlaceList list ) {
		String data = list.buildWriteData();
		writeAfterDelete( mFileTarget, data );
	}

	/**
	 * read PlaceList
	 * @return PlaceList
	 */
    public PlaceList read() {
		PlaceList list = new PlaceList();
		String data = readData( mFileTarget );
		// if no data
		if (( data == null )|| data.equals( "" ) ) return list;
		String[] lines = data.split( LF );
		// build read data list
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( !"".equals( line ) ) { 
				list.add( new PlaceRecord( line ) );		
			}	
		}
		list.initHash();
		return list;
	}

	/**
	 * get list included in event list
	 * @param List<PlaceRecord> list_orig
	 * @param List<PlaceRecord> list_new
	 * @param Map<String, Boolean> map
	 * @return ArrayList<PlaceRecord> 
	 */	
	public List<PlaceRecord> getListWithEvent( 
		List<PlaceRecord> list_orig, 
		List<PlaceRecord> list_new, 
		Map<String, Boolean> map ) {

		for ( int i=0; i<list_orig.size(); i++ ) {
			PlaceRecord r = list_orig.get( i );
			// if include
			if ( map.containsKey( r.url ) ) {
				r.event_flag = true;
				list_new.add( r );
			}
		}
		return list_new;
	}
	
	/**
	 * get list NOT included in event list
	 * @param List<PlaceRecord> list_orig
	 * @param List<PlaceRecord> list_new
	 * @param Map<String, Boolean> map
	 * @return ArrayList<PlaceRecord> 
	 */	
	public List<PlaceRecord> getListWithoutEvent( 
		List<PlaceRecord> list_orig, 
		List<PlaceRecord> list_new, 
		Map<String, Boolean> map ) {

		for ( int i=0; i<list_orig.size(); i++ ) {
			PlaceRecord r = list_orig.get( i );
			// if NOT include
			if ( !map.containsKey( r.url ) ) {
				list_new.add( r );
			}
		}
		return list_new;
	}

}