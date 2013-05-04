package jp.ohwada.android.nfccconcentration;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * helper for DB
 */
public class CardHelper extends SQLiteOpenHelper {
    // database
    private static final String DB_NAME = "card.db";
    private static final int DB_VERSION = 1;

	// table
    private static final String TBL_NAME = "card";

    // column	
	private static final String COL_ID = "_id";
	private static final String COL_TAG = "tag";
	private static final String COL_NUM = "num";
	private static final String COL_SET = "_set";	// "set" is reserved word

	private	static final String[] COLUMNS =
			new String[]
			{ COL_ID, COL_TAG, COL_NUM, COL_SET  } ;

	// query 
	private static final String ORDER_BY =  "_id desc" ;

    private static final String CREATE_SQL =
    	"CREATE TABLE IF NOT EXISTS " 
		+ TBL_NAME 
		+ " ( " 
		+ COL_ID
		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
		+ COL_TAG
		+ " TEXT, " 
		+ COL_NUM
		+ " INTEGER, " 
		+ COL_SET
		+ " INTEGER )" ;

    private static final String DROP_SQL =
		"DROP TABLE IF EXISTS " + TBL_NAME ;
    
    /**
     * === constractor ===
     * @param Context context   
     */		
    public CardHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

	/**
     * === onCreate ===
     * @param SQLiteDatabase db 
     */
    @Override
    public void onCreate( SQLiteDatabase db ) {
    	createDb( db );
    }

	/**
     * create DB
     * @param SQLiteDatabase db 
     */
    private void createDb( SQLiteDatabase db ) {
        db.execSQL( CREATE_SQL );
    }

	/**
     * === onUpgrade ===
     * @param SQLiteDatabase db
     * @param int oldVersion
     * @param int newVersion 
     */
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )  {
        db.execSQL( DROP_SQL );
        createDb( db );
    }

	/**
     * --- insert  ---
     * @param CardRecord record
	 * @return long : the row ID of the newly inserted row
     */
    public long insert( CardRecord record ) {
    	SQLiteDatabase db = getWritableDatabase();
    	if ( db == null ) return 0;
		long ret = db.insert( 
			TBL_NAME, 
			null, 
			buildValues( record ) );
		db.close();
		return ret;
    }

	/**
     * --- update  ---
     * @param CardRecord record
	 * @return int : the number of rows affected
     */    	
	public int update( CardRecord record  ) {
    	SQLiteDatabase db = getWritableDatabase();
    	if ( db == null ) return 0;
		int ret = db.update(
			TBL_NAME, 
			buildValues( record ),
			buildWhereId( record ), 
			null );
		db.close();
		return ret;
	}

	/**
     * --- delete  ---
     * @param CardRecord record
	 * @return int : the number of rows affected
     */ 			
	public int delete( CardRecord record ) {
		return delete( buildWhereId( record ) );
	}

	/**
     * --- delete  ---
     * @param int id
	 * @return int : the number of rows affected
     */ 	
	public int delete( int id  ) {
		return delete( buildWhereId( id ) );
	}	

	/**
     * delete all
	 * @return int : the number of rows affected
     */ 	
	public int deleteAll()  {
		String where = null;
		return delete( where ); 
	}
	
	/**
     * delete  for common
     * @param int id
	 * @return int : the number of rows affected
     */ 	
	public int delete( String where )  {
		SQLiteDatabase db = getWritableDatabase();
		if ( db == null ) return 0;
		int ret = db.delete(
			TBL_NAME, 
			where, 
			null );		
		db.close();
		return ret;	
	}
	
	/**
     * build ContentValues
     * @param CardRecord r    
	 * @return ContentValues
     */        
    private ContentValues buildValues( CardRecord r ) {
		ContentValues v = new ContentValues();	
		v.put( COL_TAG, r.tag );
		v.put( COL_NUM, r.num );
		v.put( COL_SET, r.set );
		return v;
	}

	/**
     * build where
     * @param CardRecord r
	 * @return String : where
     */ 
	private String buildWhereId( CardRecord r ) {
		return buildWhereId( r.id );
	}

	/**
     * build where
     * @param int id
	 * @return String : where
     */ 
	private String buildWhereId( int id ) {
		String s = COL_ID + "=" + id ;
		return s;
	}

	/**
     * build where
     * @param int num
	 * @return String : where
     */ 
	private String buildWhereNum( int num ) {
		String s = COL_NUM + "=" + num;
		return s;
	}

	/**
     * build where
     * @param String tag
	 * @return String : where
     */ 
	private String buildWhereTag( String tag ) {
		String s = COL_TAG + "= '" + tag + "'";
		return s;
	}
			
	/**
     * build limit
     * @param int limit
     * @param int offset
	 * @return String : limit
     */ 
	private String buildLimit( int limit, int offset ) {
		String limit_str = Integer.toString( limit );
		String offset_str = Integer.toString( offset );
		String str = null;
		if (( limit > 0 )&&( offset > 0 )) {
			str = limit_str+ ", " + offset_str;
		} else if (( limit > 0 )&&( offset== 0 )) {
			str = limit_str;
		} else if (( limit == 0 )&&( offset > 0 )) {
			str = "0, " + offset_str;
		}
		return str;
	}	
		
	/**
     * --- get record   ---
     * @param int id
	 * @return CardRecord
     */ 			
	public CardRecord getRecordById( int id ) {
		return getRecordCommon( buildWhereId( id ) );
	}

	/**
     * --- get record   ---
     * @param int id
	 * @return CardRecord
     */ 			
	public CardRecord getRecordByNum( int num ) {
		return getRecordCommon( buildWhereNum( num ) );
	}
	
	/**
     * --- get record   ---
     * @param String tag
	 * @return CardRecord
     */ 			
	public CardRecord getRecordByTag( String tag ) {
		return getRecordCommon( buildWhereTag( tag ) );
	}

	/**
     * get record
     * @param String where
	 * @return CardRecord
     */ 			
	private CardRecord getRecordCommon( String where ) {
		SQLiteDatabase db = getReadableDatabase();
		if ( db == null ) return null;
        Cursor c = getCursorCommon( db, where, null );
		if (( c == null )||( c.getCount() == 0 )) {
			db.close();
			return null;
		}
		c.moveToFirst();   
		CardRecord r = buildRecord( c );
		db.close();
		return r;
	}
		
	/**
     * --- get record list   ---
     * @param int limit
	 * @return List<CardRecord>
     */ 
	public List<CardRecord> getRecordList( int limit ) {
		return getRecordList( limit, 0 );
	}
	
	/**
     * --- get record list   ---
     * @param int limit
     * @param int offset
	 * @return List<CardRecord>
     */ 
	public List<CardRecord> getRecordList( int limit, int offset ) {
		SQLiteDatabase db = getReadableDatabase();
		if ( db == null ) return null;
        Cursor c = getCursorCommon( 
			db, null,  buildLimit( limit, offset ) );
		if (( c == null )||( c.getCount() == 0 )) {
			db.close();
			return null;
		}
		List<CardRecord> list = buildRecordList( c );
		db.close();
		return list;
	}

	/**
     * get cursor for common
     * @param SQLiteDatabase db
     * @param String where
     * @param String limit  
	 * @return Cursor
     */ 
	private Cursor getCursorCommon( SQLiteDatabase db, String where, String limit ) {		
		String[] param = null;
		String groupby = null;
		String having = null;
				
       return db.query( 
        	TBL_NAME,
        	COLUMNS,
			where , 
			param, 
			groupby , 
			having, 
			ORDER_BY , 
			limit );
	}
	
	/**
     * buile record
     * @param Cursor c 
	 * @return CardRecord
     */ 	
	private CardRecord buildRecord( Cursor c ) {
		CardRecord r = new CardRecord();
		r.id = c.getInt(0);
		r.tag = c.getString(1);
		r.num = c.getInt(2);
		r.set = c.getInt(3);
		return r;
	}

	/**
     * buile record list
     * @param Cursor c 
	 * @return List<CardRecord>
     */ 			
	private List<CardRecord> buildRecordList( Cursor c ) {
		 List<CardRecord> list = new ArrayList<CardRecord>();		        
		int count = c.getCount();
		if ( count == 0 ) return list;		
        c.moveToFirst();   
		for ( int i = 0; i < count; i++ ) {
			list.add( buildRecord( c ) );
			c.moveToNext();
 		} 		
		c.close();		
		return list;
	}

	/**
     * --- getRecordCoun ---
	 * @return long
     */ 
    public long getRecordCount() {
        long count = 0;
		SQLiteDatabase db = getReadableDatabase();
		if ( db == null ) return count;
        String sql = "select count(*) from " + TBL_NAME;
        Cursor c = db.rawQuery( sql, null );
        if ( c != null ) {
        	c.moveToLast();
        	count = c.getLong( 0 );
        }	
        c.close();
        return count;
    }

}
