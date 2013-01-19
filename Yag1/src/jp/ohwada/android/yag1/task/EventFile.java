package jp.ohwada.android.yag1.task;

import java.io.File;

/**
 * Event file
 */
public class EventFile extends CommonFile { 

    private static final long EXPIRE_EVENT = 2L * TIME_MSEC_ONE_DAY;  // 2 day
    	
	/**
	 * === constractor ===
	 */
    public EventFile() {
        // dummy
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
	 * write Event
	 * @param File file
	 * @param EventRecord record
	 */
    public void write( File file, EventRecord record ) {
    	String data = record.getFileData() + LF;	
    	writeAfterDelete( file, data );
	}

	/**
	 * read Event
	 * @param File file
	 * @return EventRecord
	 */
    public EventRecord read( File file ) {
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) return null;
		EventRecord record = new EventRecord( data.trim() );
		return record;
	}

}