package jp.ohwada.android.slidepuzzle2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

public class SlideGame
{
	private final static String TAG  = "SlideGame";

	// limit
    private final static int MAX_WIDTH = SlideConstant.MAX_WIDTH ;
    private final static int MAX_HEIGHT = SlideConstant.MAX_HEIGHT ;
    private final static int MAX_PIECE_2 = SlideConstant.MAX_PIECE + 2;

	// board
    public final static int B_BLANK = SlideConstant.B_BLANK ;
    public final static int B_WALL = SlideConstant.B_WALL ;
    
    // wall
	private final static boolean W_WALL = true;
	private final static boolean W_NORMAL = false;
	    
	// SharedPreferences
	public static final String PREF_GAME = "game";
	
	// class object
	private Handler mHandler = null;
	private Context mContext = null;
	private AssetManager mAsset = null;
	private SharedPreferences mPreferences = null;

	private SlideHelper mHelper = null; 
	private SlideInput mInput = null;
	
	// hash char <-> int
	private HashMap<String, Integer> hash_s_i = new HashMap<String, Integer>();
	private HashMap<Integer, String> hash_i_s = new HashMap<Integer, String>();
	
	// board
	private int[][] board = new int[ MAX_HEIGHT ][ MAX_WIDTH ];
	private int[][] goal = new int[ MAX_HEIGHT ][ MAX_WIDTH ];
	private boolean[][] wall  = new boolean[ MAX_HEIGHT ][ MAX_WIDTH ];
	private boolean[] piece = new boolean[ MAX_PIECE_2 ];	

	// gme data
	private int b_width  = 0;
	private int b_height = 0;
	private int[] b_initial = new int[ MAX_PIECE_2 ];
			
	// game
	private int game_num  = 0;
	private boolean start_flag = false;
	private long start_time = 0;
	private int move_num  = 0;
	private String move_result  = ""; 
	
    public SlideGame( Context c )
    {
    	mContext = c;
        mHelper = new SlideHelper( c );
        mInput = new SlideInput();
    }

// === init === 
    public void setAssets( AssetManager a )
    {
    	mAsset = a;
    }
    
    public void setSharedPreferences( SharedPreferences p )
    {
    	mPreferences = p;
    }

    public void setHandler( Handler h ) 
    {
        mHandler = h;
    }
   
// === getter ===
    public HashMap<String, Integer> getHashSi()
    {
    	return hash_s_i;
    }
    
    public HashMap<Integer, String> getHashIs()
    {
    	return hash_i_s;
    }

    public int getBoardWidth()
    {
    	return b_width;
    }
 
    public int getBoardHeight()
    {
    	return b_height;
    }

    public int[] getBoardInitial()
    {
    	return b_initial ;
    }

    public String getMoveResult()
    {
    	return move_result ;
    }

    public int getGameNum()
    {
    	return game_num ;
    }
        
// === init game ===
    public void initGame()
    {
    	init_hash();
    }

 // hash    			
    private void init_hash() 
	{
    	// 0 -> 9
		for (int i=0; i<10; i++) {
			String s = Integer.toString(i);
			hash_s_i.put( s, i );
			hash_i_s.put( i, s );
		}
		
		// A -> Z
		for (int j=0; j<26; j++ ) {
			// A : 65 0x41
			int j1 = j + 65;
			int j2 = j + 10;
			String s1 = Character.toString( (char)j1 );
			hash_s_i.put( s1, j2 );
			hash_i_s.put( j2, s1 );
		}
		
		// =
		hash_s_i.put( "=",  B_WALL );
		hash_i_s.put( B_WALL,  "=" );
		
//		hash_debug(); 
	}

    private void hash_debug( ) 
    {
    	Set keySet = hash_s_i.keySet(); 
    	Iterator keyIte = keySet.iterator();  
    	while(keyIte.hasNext()) {    
    		Object key = keyIte.next();  
    		Object value = hash_s_i.get(key);   
    		log_d( key + " = " + value ); 
    	}	
    } 

// === start game ===
	public void startGame( int num ) 
	{
		start_flag = false;
		game_num = num;
		move_result = "";
		move_num  = 0;

		setPreferences( num );		

		init_piece() ;
		init_board() ;
		set_board() ;
		set_goal() ;

//		debug_board();
//		debug_goal();		
	}

	private void init_piece()
	{	
		for ( int i=0; i < MAX_PIECE_2; i++ ) {
			piece[i] = false;
		}
	}

	private void init_board()
	{
		for ( int y=0; y < MAX_HEIGHT ; y++ ) {
			for ( int x=0; x < MAX_WIDTH ; x++ ) {
				board[y][x] = B_WALL ;
				wall[y][x] = W_WALL;
			}
		}		
	}

	public void set_board() 
	{
		int i = 0;
		for ( int y=0; y < b_height; y++ ) {
			for ( int x=0; x < b_width; x++ ) {
							
				int n = b_initial[ i ];
				boolean w = W_NORMAL;
														
				switch (n)
				{
					case B_WALL :
						w = W_WALL;
						break;
					
					case B_BLANK :
						break;
						 
					default:
						piece[n] = true;
						break;
				}
				
				board[y][x] = n;
				wall[y][x] = w;
				i ++;		
			}
		}
	}

	private void set_goal()
	{
		int i = SlideConstant.MIN_PIECE;
		for ( int y=0; y < b_height; y++ ) {
			for ( int x=0; x < b_width; x++ ) {
				int val = B_BLANK ;
				if ( piece[ i ] ) {				
					val = i;
				} else if ( board[y][x] == B_WALL ) {
					val = B_WALL ;
				}
				goal[y][x] = val ;
				i ++;		
			}
		}
	}
				    					
	private void debug_board()
	{
		log_d( "debug_board" );
		log_d_array( board );
	}
	
	private void debug_goal()
	{
		log_d(  "debug_goal"  );
		log_d_array( goal );
	}

	private void log_d_array( int[][] a )
	{
		for ( int y=0; y < b_height; y++ ) {
				String msg = "";
			for ( int x=0; x < b_width; x++ ) {
				msg += " " + a[y][x] ;	
			}
			log_d( msg );
		}
	}
		
// === swap ===
    public void swapPiece( int move, int py, int px, int by, int bx )
    {
     	move_num ++;
    	move_result += SlideConstant.MOVE_CHAR[ move ];

		// swap borad
		int tmp = board[py][px];
		board[py][px] = board[by][bx];
    	board[by][bx] = tmp;
    		
    	// at once when first move
    	if ( ! start_flag ) {
    		start_flag = true;
    		start_time = System.currentTimeMillis();
    	}

		if ( check_goal() ) {
			exec_goal();
		}
    }
    
// goal
    private boolean check_goal() 
	{
		for (int y = 0; y < b_height; y++ ) {
			for (int x = 0; x < b_width; x++ ) {
				if ( board[y][x] != goal[y][x] ) {
					return false;
				}
			}
		}
		return true;
	}

// goal
	private void exec_goal()
	{
		// logging
    	log_d( "Game " + game_num );
    	log_d( move_result );
    	
		int time = (int)(( System.currentTimeMillis() - start_time ) / 1000 );
		add_score( game_num, time, move_num, move_result );
		alert_dialog( game_num, time, move_num, move_result );
	}

// === check game ===
    public boolean checkPrevGame() 
	{
		boolean check = mInput.checkPrevGame( game_num );
		if ( check ) {
			game_num = mInput.getGame();
		}
		return check;	
 	}

    public boolean checkNextGame() 
	{
		boolean check = mInput.checkNextGame( game_num );
		if ( check ) {
			game_num = mInput.getGame();
		}
		return check;	
 	}

    public boolean checkAnyGame( int num ) 
	{
		boolean check = mInput.checkIntGame( num );
		if ( check ) {
			game_num = num;
		}
		return check;	
 	}

    public boolean checkEditGame( EditText edit ) 
	{
		boolean check = mInput.checkEditGame( edit );
		if ( check ) {
			game_num = mInput.getGame();
		}
		return check;
	}

    public String getErrorMsg()
	{
		return mInput.getErrorMsg();
	}	
			 	 	 	
// --- alert ---
	private void alert_dialog(  int game, int time, int move ,String result )
	{
		String title = "GOAL !!";
		String message = "Game " + game + "\n" ;
		message += move + " moves \n" ;
		message += time + " sec \n" ;
		message += result + "\n" ;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( mContext );
        alertDialogBuilder.setTitle( title );
        alertDialogBuilder.setMessage( message );

        alertDialogBuilder.setPositiveButton("<< Prev",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	sendMassage( SlideConstant.MESSAGE_PREV );
                    }
                });

        alertDialogBuilder.setNeutralButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMassage( SlideConstant.MESSAGE_CLOSE );
                    }
                });

        alertDialogBuilder.setNegativeButton("Next >>",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMassage( SlideConstant.MESSAGE_NEXT );
                    }
                });

        alertDialogBuilder.setCancelable(true);
        Dialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
				
// === preferences ===
    private void setPreferences( int num ) 
	{
            mPreferences.edit().putInt( PREF_GAME, num ).commit(); 
    }

    public int getPreferences() 
	{
        return mPreferences.getInt( PREF_GAME, SlideConstant.MIN_GAME );
 	}
 	                       
// === file read ===
    public boolean readBoard( int num ) 
	{
		String[] columns = read_problem( num );
		if ( columns == null ) {
    		log_d("columns is null");
			return false;
		}
		if ( columns[0] == null ) {
    		log_d( "columns 0 is null");
			return false;
		}
		if ( columns[1] == null ) {
    		log_d( "columns 1 is null");
			return false;
		}
		if ( columns[2] == null ) {
    		log_d( "columns 2 is null");
			return false;
		}

		b_width  = Integer.valueOf( columns[0] ).intValue();
		b_height = Integer.valueOf( columns[1] ).intValue();
		String initial = columns[2];
		
		if (( b_width < SlideConstant.MIN_WIDTH  )||
			( b_width > SlideConstant.MAX_WIDTH )) {
    		log_d( "width " + b_width);
			return false;
		}
		if (( b_height < SlideConstant.MIN_HEIGHT )||
			( b_height > SlideConstant.MAX_HEIGHT )) {
	  		log_d( "height " +  b_height );
			return false;
		}
		if ( initial.length() == 0 ) {
    		log_d( "initial empty");
			return false;
		}

		return initial_to_array( initial );
	}

	private boolean initial_to_array( String initial ) 
	{
		for ( int i = 0; i < initial.length(); i++ ) {
			String c = initial.substring(i, i+1);
			if (( c == null )||( c.length() == 0 )) {
		    	log_d("char empty");
				return false;
			}
			if ( !hash_s_i.containsKey( c ) ) {
		    	log_d( "char " + c );
				return false;
			}
			b_initial[i] = hash_s_i.get( c );
		}
		return true;
	}
	
	private String[] read_problem( int num )
	{
		String file = "p" + num + ".txt";

		// The number of lines of each file is one line.
		String[] lines = read_file( file, 10 );
		if (( lines == null )||( lines[0] == null )) {
			log_d( "NO data " + file );
			return null;
		}

		String[] columns = lines[0].split(",");
		if (( columns == null )||( columns[0] == null )) {
			log_d( "bad format " + file );
			return null;
		}

		return columns;
	}

	private String[] read_file( String file, int max_line )
	{
		String[] data = new String[ max_line ];
		InputStream is = null;
		String line;
		int i = 0;

		try {
			is = mAsset.open(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			while ( (( line = reader.readLine() ) != null ) && 
			        (i < max_line) ) {
				data[i] = line.trim();
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data;
	}

// --- sqlite ---
    private void add_score( int game, int time, int move ,String result )
    { 
		SlideScore score = new SlideScore();
            
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		String date = year + "-" + (month + 1) + "-" + day +  " " +
			hour + ":" + minute + ":" + second ;
    	    	
        score.setGame( game );
        score.setDate( date );
        score.setTime( time );
        score.setMove( move );
        score.setResult( result );
        mHelper.insert( score );        	
    }

    public String getScore( int game )
    {
    	String str = "---";
        SlideScore score = mHelper.getScore( game );
        
        if ( score != null ) {
            str  = score.getStringMove();
            str += " moves "; 
            str += score.getStringTime();
			str += " sec";            	
		}

		return str; 
    }

// --- message ---	
	private void sendMassage( int what ) 
    {
		Message msg = mHandler.obtainMessage( what );
        mHandler.sendMessage( msg );
    }
    
// --- others ---    
    private void log_d( String s, int y, int x) 
    {
    	Log.d( TAG, s + " " + y + " " + x);  
	}
    
    private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}
}