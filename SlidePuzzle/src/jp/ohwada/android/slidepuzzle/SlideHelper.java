package jp.ohwada.android.slidepuzzle;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SQLite helper for score
public class SlideHelper extends SQLiteOpenHelper 
{
	// database
	private static final String DB_NAME = "slidepuzzle.db";
	private static final int DB_VERSION = 1;

	// table	
	private static final String TBL_NAME = "score";

	// column	
	private static final String COL_ID = "_id";
	private static final String COL_GAME = "game";
	private static final String COL_TIME = "time";
	private static final String COL_MOVE = "move";
	private static final String COL_RESULT = "result";
	private static final String COL_DATE = "date";
		
	private	String[] COLUMNS =
			new String[]
			{ COL_ID, COL_GAME, COL_TIME, COL_MOVE , COL_RESULT, COL_DATE  } ;

	// query 
	private String ORDER_BY =  "game asc, move asc, time asc, _id desc" ;
			
	// === Constrtactor  ===		
	public SlideHelper( Context context )
	{
		super( context.getApplicationContext(), DB_NAME, null, DB_VERSION );		
	}
	
	// === onCreate ===
	@Override
	public void onCreate( SQLiteDatabase db ) 
	{
		db.execSQL( build_sql_on_create() );	
	}

	private String build_sql_on_create() 
	{
		String s = "CREATE TABLE IF NOT EXISTS " ;
		s += TBL_NAME ;
		s += " ( " ;
		s += " _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," ;
		s += " game INTEGER ," ;
		s += " time INTEGER ," ;
		s += " move INTEGER ," ;
		s += " result TEXT ," ;
		s += " date TEXT" ;
		s += " )" ;
		return s;
	}
	
	// === onUpgrade ===
	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) 
	{
		// noting to do
	}

	// ===  insert ===	
	public void insert( SlideScore score )
	{
		SQLiteDatabase db = getWritableDatabase();
		db.insert( 
			TBL_NAME, 
			null, 
			build_values_insert( score ) );
	}

	private ContentValues build_values_insert( SlideScore score )
	{
		ContentValues v = new ContentValues();	
		v.put( COL_GAME, score.getIntGame() );
		v.put( COL_TIME, score.getIntTime() );
		v.put( COL_MOVE, score.getIntMove() );
		v.put( COL_RESULT, score.getStringResult() );
		v.put( COL_DATE, score.getStringDate() );		
		return v;
	}
	
	// === update ===
	public void update( SlideScore score ) 
	{
		// noting to do
	}

	// === delete ===
	public void delete( SlideScore score ) 
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(
			TBL_NAME, 
			build_where_delete( score ), 
			null );
	}

	private String build_where_delete( SlideScore score ) 
	{
		String w = COL_ID + "=" + score.getStringId() ;
		return w;
	}

	// === get score ===
	public SlideScore getScore( int game )
	{
        Cursor c = get_query_game( game );
		if ( c == null ) return null;

		SlideScore score = null;
		int count = c.getCount();
		c.moveToFirst();
		
		if ( count  > 0 ) {
			score = build_score( c );
		}
		
		c.close();
		return score;
	}
			
	// === get all scores ===
	public List<SlideScore> getAll( int start, int limit )
	{
        Cursor c = get_query_all( start, limit );
		if ( c == null ) return null;
		return build_score_list( c );
	}

	// buile score
	private SlideScore build_score( Cursor c )
	{
		SlideScore score = new SlideScore();
		score.setId( c.getInt(0) );
		score.setGame( c.getInt(1) );
		score.setTime( c.getInt(2) );
		score.setMove( c.getInt(3) );
		score.setResult( c.getString(4) );
		score.setDate( c.getString(5) );
		return score;
	}
	
	private List<SlideScore> build_score_list( Cursor c )
	{
		int count = c.getCount();
        c.moveToFirst();   
        List<SlideScore> scoreList = new ArrayList<SlideScore>();
        
		for ( int i = 0; i < count; i++ )
		{
			SlideScore score = build_score( c );
			scoreList.add( score );
			c.moveToNext();
 		}
 		
		c.close();		
		return scoreList;
	}

	// query
	private Cursor get_query_game( int game )
	{
		SQLiteDatabase db = getWritableDatabase();
		if ( db == null ) return null;
		
		String[] param = null;
		String groupby = null;
		String having = null;
		String limit = null;
		String where = COL_GAME + "=" + game ;

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

	private Cursor get_query_all( int start, int int_limit )
	{
		SQLiteDatabase db = getWritableDatabase();
		if ( db == null ) return null;
		
		String[] param = null;
		String groupby = null;
		String having = null;
		String limit = null;
		String where = COL_GAME + ">=" + start ;

		// set limit
		// no limit when init_limit = 0		
		if ( int_limit > 0 ) {
			limit = Integer.toString( int_limit );
		}
		
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
	
}
