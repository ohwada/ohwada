/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.ohwada.android.calllogcalls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

/**
 * The CallLog provider contains information about placed and received calls.
 * Contains the recent calls.
 * base on android.provider.CallLog.java
 */
public class CallLogCalls  {

	/**
	 * The content:// style URL for this table
	*/
	private static final Uri CONTENT_URI = CallLog.Calls.CONTENT_URI ;

	 /**
	 * The default sort order for this table
	 */
	private static final String DEFAULT_SORT_ORDER = CallLog.Calls.DEFAULT_SORT_ORDER;

	/**
	 * The type of the call (incoming, outgoing or missed).
	 * <P>Type: INTEGER (int)</P>
	 */
	private static final String TYPE = CallLog.Calls.TYPE ;

	/** Call log type for outgoing calls. */
	private static final int OUTGOING_TYPE = CallLog.Calls.OUTGOING_TYPE;
        
	/**
	 * The phone number as the user entered it.
	 * <P>Type: TEXT</P>
	 */
	private static final String NUMBER = CallLog.Calls.NUMBER;

	/**
	 * The date the call occured, in milliseconds since the epoch
	 * <P>Type: INTEGER (long)</P>
	 */
	private static final String DATE = CallLog.Calls.DATE;
        
	/**
	 * The duration of the call in seconds
	 * <P>Type: INTEGER (long)</P>
	 */
	private static final String DURATION = CallLog.Calls.DURATION;

	/**
	 * Whether or not the call has been acknowledged
	 * <P>Type: INTEGER (boolean)</P>
	 */
	private static final String NEW = CallLog.Calls.NEW;
        
	/**
	 * The cached name associated with the phone number, if it exists.
	 * This value is not guaranteed to be current, if the contact information
	 * associated with this number has changed.
	 * <P>Type: TEXT</P>
	 */
	private static final String CACHED_NAME = CallLog.Calls.CACHED_NAME;

	/**
	 * The cached number type (Home, Work, etc) associated with the
	 * phone number, if it exists.
	 * This value is not guaranteed to be current, if the contact information
	 * associated with this number has changed.
	 * <P>Type: INTEGER</P>
	 */
	private static final String CACHED_NUMBER_TYPE = CallLog.Calls.CACHED_NUMBER_TYPE;

	/**
	 * The cached number label, for a custom number type, associated with the
	 * phone number, if it exists.
	 * This value is not guaranteed to be current, if the contact information
	 * associated with this number has changed.
	 * <P>Type: TEXT</P>
	 */
	private static final String CACHED_NUMBER_LABEL = CallLog.Calls.CACHED_NUMBER_LABEL;

	/**
	 * Whether this item has been read or otherwise consumed by the user.
	 * <p>
	 * Unlike the {@link #NEW} field, which requires the user to have acknowledged the
	 * existence of the entry, this implies the user has interacted with the entry.
	 * <P>Type: INTEGER (boolean)</P>
	 */
// API 14
//	private static final String IS_READ = "is_read";
	
	/**
	 * Adds a call to the call log.
	 *
	 * @param ci the CallerInfo object to get the target contact from.  Can be null
	 * if the contact is unknown.
	 * @param context the context used to get the ContentResolver
	 * @param number the phone number to be added to the calls db
	 * @param presentation the number presenting rules set by the network for
	 *        "allowed", "payphone", "restricted" or "unknown"
	 * @param callType enumerated values for "incoming", "outgoing", or "missed"
	 * @param start time stamp for the call in milliseconds
	 * @param duration call duration in seconds
	 *
	 * {@hide}
	 */
//	public static Uri addCall(CallerInfo ci, Context context, String number,
//		int presentation, int callType, long start, int duration) {
                                        
	/**
	 * Query the call log database for the last dialed number.
	 * @param context Used to get the content resolver.
	 * @return The last phone number dialed (outgoing) or an empty
	 * string if none exist yet.
	 */
	public static String getLastOutgoingCall(Context context) {
		final ContentResolver resolver = context.getContentResolver();
		Cursor c = null;
		try {
			c = resolver.query(
			CONTENT_URI,
			new String[] {NUMBER},
			TYPE + " = " + OUTGOING_TYPE,
			null,
			DEFAULT_SORT_ORDER + " LIMIT 1");
			if (c == null || !c.moveToFirst()) {
				return "";
			}
			return c.getString(0);
		} finally {
			if (c != null) c.close();
		}
	}

	/**
     * remove Expired Entries
	 * @param context Used to get the content resolver.
	 * @return void
     */ 
	public static void removeExpiredEntries(Context context) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.delete(CONTENT_URI, "_id IN " +
			"(SELECT _id FROM calls ORDER BY " + DEFAULT_SORT_ORDER
				+ " LIMIT -1 OFFSET 500)", null);
	}
		
	/**
     * get record list
	 * @param context Used to get the content resolver.
     * @param int limit
	 * @return List<CallLogRecord>
     */ 	
	public List<CallLogRecord> getRecordList( Context context, int limit ) {
		String sortOrder = buildSortOrder( limit );
		return getRecordList( context, null, sortOrder );
	}

	/**
     * get record list
	 * @param context Used to get the content resolver.
     * @param String selection
     * @param String sortOrder
	 * @return List<CallLogRecord>
     */ 		
	public List<CallLogRecord> getRecordList( Context context, String selection, String sortOrder ) {
		final ContentResolver resolver = context.getContentResolver();	
		Cursor c = resolver.query(
			CONTENT_URI,
			null,
			selection,
			null,
			sortOrder );
		if ( c == null ) return null;
		return buildRecordList( c );
	}

	/**
     * build Selection
     * @param int type
	 * @return String selection
     */ 	
	public String buildSelection( int type ) {
		String selection = TYPE + " = " + type;
		return selection;
	}

	/**
     * build SortOrder
     * @param int limit
	 * @return String sortOrder
     */ 	
	public String buildSortOrder( int limit ) {
		String sortOrder = DEFAULT_SORT_ORDER + " LIMIT " + limit;		
		return sortOrder;
	}

	/**
     * buile record list
     * @param Cursor c 
	 * @return List<CallLogRecord>
     */ 	
	private List<CallLogRecord> buildRecordList( Cursor c ) {
		List<CallLogRecord> list = new ArrayList<CallLogRecord>();
		int count = c.getCount();
		if ( count == 0 ) {
			return list;
		}
		c.moveToFirst();   
		for ( int i = 0; i < count; i++ ) {
			list.add( buildRecord( c ) );
			c.moveToNext();
 		} 		
		c.close();		
		return list;
	}
	
	/**
     * buile record
     * @param Cursor c 
	 * @return CallLogRecord
     */ 	
	private CallLogRecord buildRecord( Cursor c ) {
		CallLogRecord r = new CallLogRecord();
		r.type = c.getInt( c.getColumnIndex(TYPE) );
		r.number = c.getString( c.getColumnIndex(NUMBER) );
		r.date = c.getLong( c.getColumnIndex(DATE) );
		r.duration = c.getLong( c.getColumnIndex(DURATION) ) ;
		r.call_new = c.getInt( c.getColumnIndex(NEW) ) ;
		r.cachedName = c.getString( c.getColumnIndex(CACHED_NAME) );
		r.cachedNumberType = c.getInt( c.getColumnIndex(CACHED_NUMBER_TYPE) );
		r.cachedNumberLabel = c.getString( c.getColumnIndex(CACHED_NUMBER_LABEL) );
		return r;
	}
}
