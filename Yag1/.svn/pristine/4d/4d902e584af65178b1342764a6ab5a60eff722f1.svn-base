package jp.ohwada.android.yag1.task;

import java.io.File;

import jp.ohwada.android.yag1.Constant;

/**
 * Event file
 */
public class EventFile extends CommonFile { 

    private static final long TIME_EXPIRE = Constant.EXPIRE_DAYS_EVENT * TIME_MSEC_ONE_DAY; 
    	
	/**
	 * === constractor ===
	 */
    public EventFile() {
        // dummy
    }

	/**
	 * getFile
	 * @param String url
	 * @return File
	 */
    public File getFile( String url ) {
		String name = getFilename( url );
		return getFileFromName( name );
	}
	
	/**
	 * getFilename
	 * @param String url
	 * @return String
	 */
    public String getFilename( String url ) {
        EventRecord record = new EventRecord();
    	// http://yan.yafjp.org/event/event_xxx -> event_xxx
		String name = record.getEventName( url );		
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