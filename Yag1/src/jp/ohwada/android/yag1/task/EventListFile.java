package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.Constant;

/**
 * EventList file 
 */
public class EventListFile extends CommonFile { 

	private static final long TIME_EXPIRE = Constant.EXPIRE_DAYS_EVENT_LIST * TIME_MSEC_ONE_DAY; 
    	
	/**
	 * === constractor ===
	 */
    public EventListFile() {
        // dummy
    }
		
	/**
	 * getFile
	 * @param Date date
	 * @return File
	 */
    public File getFileForList( Date date ) {
		String name = getFilenameForList( date );
		return getFileFromName( name );
	}

	/**
	 * getFile
	 * @param String place_name
	 * @return File
	 */
    public File getFileForPlace( String place_name ) {
		String name = getFilenameForPlace( place_name );
		return getFileFromName( name );
	}

	/**
	 * getFilename
	 * @param Date date
	 * @return String
	 */
    public String getFilenameForList( Date date ) {
		String name = Constant.FILE_PREFIX_EVENT_LIST + "_" + mDateUtility.formatLabel( date );		
		return getFilenameWithTxt( name );
	}

	/**
	 * getFilename
	 * @param String place_name : place_xxx
	 * @return String
	 */
    public String getFilenameForPlace( String place_name ) {
		String name = Constant.FILE_PREFIX_EVENT_LIST_PLACE + "_" + place_name;
		return getFilenameWithTxt( name );
	}
				
	/**
	 * isExpired Event
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, TIME_EXPIRE );
	}

	/**
	 * write EventList
	 * @param File file,
	 * @param List<EvenRecord> list
	 */
	public void write( File file, List<EventRecord> list ) {
		write( file, new EventList( list ) );
	}
			
	/**
	 * write EventList
	 * @param File file
	 * @param EventList list
	 */
	public void write( File file, EventList list ) {
		String data = list.buildWriteData();
    	writeAfterDelete( file, data );
	}

	/**
	 * read EventList
	 * @param File file
	 * @return EventList
	 */
    public EventList read( File file ) {
		EventList list = new EventList();
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) return list;
		String[] lines = data.split( LF );
		// build read data list
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( !"".equals( line ) ) { 
				list.add( new EventRecord( line ) );
			}	
		}
		return list;
	}
 				
}