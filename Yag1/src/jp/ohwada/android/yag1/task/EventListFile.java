package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * EventList file 
 */
public class EventListFile extends CommonFile { 

	private static final long EXPIRE_EVENT = 2L * TIME_MSEC_ONE_DAY;  // 2 day
    	
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
    public File getFile( Date date ) {
		String name = mDateUtility.formatLabel( date );		
		return getFileWithTxt( name );
	}
		
	/**
	 * isExpired Event
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, EXPIRE_EVENT );
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