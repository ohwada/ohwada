package jp.ohwada.android.calllogcalls;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.provider.CallLog;
import android.provider.ContactsContract;

/**
 * Call Log Record
 */
public class CallLogRecord {

 	/** object */
	private SimpleDateFormat mDateFormat = 
		new SimpleDateFormat( "yyyy-MM-dd HH-mm-ss" );

 	/** variable */
	public int type = 0;
	public String number = "";
	public Long date = 0L;
	public Long duration = 0L;
	public int call_new = 0;
	public String cachedName = "";
	public int cachedNumberType = 0;
	public String cachedNumberLabel = "";

	/**
	 * === constructor ===
	 * @param none
	 */	
	public CallLogRecord() {
		// dummy
	}

	/**
	 * get formated Date 
	 * @param none
	 * @return String date
	 */
	public String getDateString() {
		String d = mDateFormat.format(new Date(date));
		return d;
	}

	/**
	 * get formated type 
	 * @param none
	 * @return String type
	 */	
	public String getTypeString() {
		String s = "unknown";
		switch (type) {
			case CallLog.Calls.INCOMING_TYPE:
				s = "in";
				break;
			case CallLog.Calls.OUTGOING_TYPE:
				s = "out";
				break;
			case CallLog.Calls.MISSED_TYPE:
				s = "miss";
				break;
		}					
		return s;
	}
	
	/**
	 * get formated New 
	 * @param none
	 * @return String new
	 */	
	public String getNewString() {
		String s = "";
		if ( call_new == 1 ) {
			s = "New";		
		}					
		return s;
	}
	
	/**
	 * get formated cachedNumberType 
	 * @param none
	 * @return String cachedNumberType
	 */	
	public String getCachedNumberTypeString( Context context ) {	
		// API 5
		int id = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource( cachedNumberType );

		Resources r = context.getResources();
		String s = r.getString( id );				
		return s;
	}
}
