package jp.ohwada.android.slidepuzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// main activity
public class SlidePuzzleActivity extends Activity 
	implements View.OnClickListener
{
	// tag
	private final static String TAG = "SlidePuzzleActivity";

	// SharedPreferences
	public static final String PREF_NAME  = "slidepuzzle";
	public static final String PREF_GAME = "game";
	
	// char for output	
	private final static String[] MOVE_CHAR = {"L", "R", "U", "D"};

	// renge
	private final static int MIN_WIDTH  = 3;
	private final static int MAX_WIDTH  = 6;
	private final static int MIN_HEIGHT = 3;
	private final static int MAX_HEIGHT = 6;

	// color
	private final static int C_BLACK  = 0xff000000;
	private final static int C_GRAY   = 0xff333333;
	private final static int C_WHITE  = 0xffffffff;
	private final static int C_BLUE   = 0xff0000ff;
	private final static int C_YELLOW = 0xffffff00;

	// borad
	private final static int B_MIN = 1;
	private final static int B_BLANK = 0;
	private final static int B_WALL = -1;
	private final static boolean W_WALL = true;
	private final static boolean W_NORMAL = false;
				
	// class object
	SharedPreferences preferences = null;
	private SlideHelper helper = null; 
	private SlideInput input = null;

	// hash char <-> int
	private HashMap<String, Integer> hash_s_i = new HashMap<String, Integer>();
	private HashMap<Integer, String> hash_i_s = new HashMap<Integer, String>();

	// view component
	private TextView[][] text_board = new TextView[ MAX_HEIGHT ][ MAX_WIDTH ];
	private int[][] id_board = new int[ MAX_HEIGHT ][ MAX_WIDTH ];
	
	private TextView text_game   = null;
	private TextView text_score   = null;
	private TextView text_result = null;
	private EditText edit_start    = null;
	private Button button_prev   = null;
	private Button button_next   = null;
	private Button button_start    = null;

	// board
	private int[][] board = new int[ MAX_HEIGHT ][ MAX_WIDTH ];
	private boolean[][] wall  = new boolean[ MAX_HEIGHT ][ MAX_WIDTH ];
	
	private int b_width  = 0;
	private int b_height = 0;
	private	String b_initial = "";

	// game
	private int game_num  = 0;
	private boolean start_flag = false;
	private long start_time = 0;
	private int move_num  = 0;
	private String move_result  = ""; 

    /** 
    	* === onCreate ===
    	*/
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        // Preferences
		preferences = getSharedPreferences( PREF_NAME, MODE_PRIVATE);
        game_num = preferences.getInt( PREF_GAME, SlideInput.MIN_GAME );
    
        // class object
        helper = new SlideHelper(this);
		input = new SlideInput();
		
		text_game   = (TextView) findViewById(R.id.text_game);
		text_score   = (TextView) findViewById(R.id.text_score);
		text_result = (TextView) findViewById(R.id.text_result);
		edit_start    = (EditText) findViewById(R.id.edit_start);
		button_prev = (Button) findViewById(R.id.button_prev);
		button_next = (Button) findViewById(R.id.button_next);
		button_start  = (Button) findViewById(R.id.button_start);

		button_prev.setOnClickListener( this );
		button_next.setOnClickListener( this );
		button_start.setOnClickListener( this );

		init_table();
		init_hash();

		start_any_game( game_num );
	}

    private void init_table()
    {
		Resources r = getResources();
		String pkg = getPackageName();
		String name = "";
		int id = 0;
				
		for (int y=0; y < MAX_HEIGHT; y++ ) {
			for (int x=0; x < MAX_WIDTH; x++ ) {
				// init board
				name = "text_" + y + "_"  + x ;
			    id = r.getIdentifier( name, "id", pkg );
			    id_board[y][x] = id;
    			text_board[y][x] = (TextView) findViewById( id );
        		text_board[y][x].setOnClickListener( this ); 
        	}
        }
    }

    // === onClick ===
    @Override
	public void onClick( View v )
	 {
	  	int id = v.getId();
	   	switch( id )
	   	{
	   	 	case R.id.button_prev:
				click_prev();
   				break;

	   	 	case R.id.button_next:
				click_next();
   				break;

	   	 	case R.id.button_start:
				click_start();
   				break;
   				
   			default:	
   				on_click_board( id );
   				break;
   		}
	}

	private void on_click_board( int id )
	 {
	 	for (int y=0; y < MAX_HEIGHT ; y++ ) {
			for (int x=0; x < MAX_WIDTH ; x++ ) {
				// id match
			    if ( id == id_board[y][x] ) {
			    	click_board(y,x);
			    	return;
			    }
        	}
        }
	}
	   		
	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
		/* Initial of menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.option_menu, menu );
        return true;
    }

	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId()) 
		{
        case R.id.menu_list:
       		Intent intent = new Intent( this, SlideListActivity.class );
			startActivity( intent );  
            return true;
        }
        return false;
    }

    // private
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

	// action
    private void click_prev() 
	{
    	start_prev_game();
	}
    
    private void click_next() 
	{
    	start_next_game();
	}

    private void click_start() 
	{
		boolean check = input.checkEditGame( edit_start );
		if ( ! check ) {
			toast( input.getErrorMsg() );
			return;
		}

		start_any_game( input.getGame() );
	}

    private void click_board(int y, int x) 
	{
		if ( wall[y][x] == W_WALL ) {
			return ;
		}

		// check blank in left right up down
		move_board(y, x, 0);
		move_board(y, x, 1);
		move_board(y, x, 2);
		move_board(y, x, 3);

		if ( check_goal() ) {
			exec_goal();
		}
	}

    private void click_alert_prev() 
	{
		click_prev();
	}

    private void click_alert_next() 
	{
		click_next();
	}
		
    private void click_alert_close() 
	{
		// do noting
		// close AlertDialog
	}
	
	// start game
    private void start_prev_game() 
	{
		boolean check = input.checkPrevGame( game_num );
		if ( ! check ) {
			toast(  "NO Prev Game" );
			return;
		}
		
		// game num --
		start_new_game( input.getGame() );
	}

    private void start_next_game() 
	{
		boolean check = input.checkNextGame( game_num );					
		if ( ! check ) {
			toast( "NO Next Game" );
			return;
		}

		// game num ++
		start_new_game( input.getGame() );
	}

	private void start_any_game( int num ) 
	{
		boolean check = input.checkIntGame( num );
		if ( ! check ) {
			toast( input.getErrorMsg() );
			return;
		}
		
		start_new_game(  num  );
	}

    private void start_new_game( int num ) 
	{
    	boolean ret1 = false;
    	boolean ret2 = false;

		ret1 = read_board( num );

   		if ( ret1 ) {
   			ret2 = set_board();
   		}
   		
   		if ( ret1 && ret2 ) {
   			start_flag = false;
			game_num = num;
			move_result = "";
			move_num  = 0;

			// show 
			text_game.setText("Game " + num );
			text_score.setText("Score " + get_score( num ) );
			text_result.setText("");
			
			// set preferences
            preferences.edit().putInt( PREF_GAME, num ).commit(); 			

		// failed
   		} else {
    		log_d( "NOT get new game " + num );
    		toast( " Sorry !\n NOT get new game" );
    	}
	}

	// board
	// return false if invalid char
	private boolean set_board() 
	{
		int i = 0;

		for (int y=0; y < MAX_HEIGHT; y++ ) {
			for (int x=0; x < MAX_WIDTH; x++ ) {
				
				// outside
				if (( x >= b_width )||( y >= b_height )) {
					set_board_outside(y, x);
					continue;
				}
				
				// inside
				String c = b_initial.substring(i, i+1);
				i ++;
				if (( c == null )||( c.length() == 0 )) {
		    		log_d("char empty");
					return false;
				}
				if ( !hash_s_i.containsKey( c ) ) {
		    		log_d( "char " + c );
					return false;
				}
				int n = hash_s_i.get( c );

				board[y][x] = n;
				wall[y][x] = W_NORMAL ;
				text_board[y][x].setTextColor( C_YELLOW );
				text_board[y][x].setBackgroundColor( C_BLUE );
				text_board[y][x].setVisibility( View.VISIBLE ); 
				text_board[y][x].setClickable( true );
				
				switch (n)
				{
					case B_BLANK :
						set_board_blank(y, x);
						break;
						
					case B_WALL :
						set_board_wall(y, x);
						break;
						 
					default:
						set_board_normal(y, x, n );
						break;
				}

			}
		}

		return true;

	}

    private void set_board_normal(int y, int x, int n) 
	{
//    	log_d( "normal", y, x); 
		board[y][x] = n ;
		text_board[y][x].setText( hash_i_s.get( n ) );
		text_board[y][x].setClickable( true );
		text_board[y][x].setBackgroundColor( C_BLUE );
	}

    private void set_board_blank(int y, int x) 
	{
//    	log_d( "blank", y, x); 
		board[y][x] = B_BLANK ;
		text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( C_WHITE );
	}

    private void set_board_wall(int y, int x) 
    {
//    	log_d( "wall", y, x); 
		board[y][x] = B_WALL ;
    	wall[y][x] = W_WALL ;
    	text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( C_GRAY );
	}

    private void set_board_outside(int y, int x) 
    {
//    	log_d( "outside", y, x); 
		board[y][x] = B_WALL ;
    	wall[y][x] = W_WALL;
    	text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( C_BLACK );
		text_board[y][x].setVisibility( View.GONE );  
	}

    private void move_board(int y, int x, int move) 
	{
    	int x0 = x;
    	int y0 = y;

    	switch (move)
		{
			// left
			case 0:
				x0 = x + 1;
				break;
			
			// right
    		case 1:
    			x0 = x - 1;
    			break;
    		
    		// up	
    		case 2:
    			y0 = y + 1;
    			break;
    		
    		// down	
    		case 3:
    			y0 = y - 1;
    			break;
		}
    			
    	if ( swap_board(y, x, y0, x0) ) {
			move_num ++;
    		move_result += MOVE_CHAR[ move ];
    		text_result.setText( move_result ); 
    		
    		// at once when first move
    		if ( ! start_flag ) {
    			start_flag = true;
    			start_time = System.currentTimeMillis();
    		}
		}
	}
    
    private boolean swap_board(int y, int x, int y0, int x0) 
	{
		if ( check_blank(y0, x0) ) {
			int n = board[y][x] ;
			set_board_normal(y0, x0, n);
			set_board_blank(y, x);
			return true;
		}
		return false;
	}

    private boolean check_blank(int y, int x) 
	{
//    	log_d("check_blank ", y, x );

		// out of renge
		if (( x < 0 )||( x >= b_width )||( y < 0 )||( y >= b_height )) {
			return false;
		}
		
		// blank
		if ( board[y][x] == B_BLANK ) {
			return true;
		}
		
		return false;
	}
    
    private boolean check_goal() 
	{
    	// blank finded
		boolean flag_blank = false;
		
		// slide number
    	int n = B_MIN ;
		
		for (int y = 0; y < b_height; y++ ) {
			for (int x = 0; x < b_width; x++ ) {
				int b = board[y][x] ;
				boolean w = wall[y][x] ;

				// must be all walls after 'blank'
				if ( flag_blank ) {
					if ( w != W_WALL ) {
						return false;
					}
				} else {
					// blank
					if ( b == B_BLANK ) {
						flag_blank = true;

					// must be suitable 'slide' or 'wall' 
					} else if (( b != n )&&( w != W_WALL )) {
						return false;
					}
				}
				
				// increment slide number
				n ++;
			}
		}
		return true;
	}

	private void exec_goal()
	{
		int time = (int)(( System.currentTimeMillis() - start_time ) / 1000 );
		add_score( game_num, time, move_num, move_result );
		alert_dialog( game_num, time, move_num, move_result );
	}
			
	// alert
	private void alert_dialog(  int game, int time, int move ,String result )
	{
		String title = "GOAL !!";
		String message = "Game " + game + "\n" ;
		message += move + " moves \n" ;
		message += time + " sec \n" ;
		message += result + "\n" ;

		// logging
    	log_d( "Game " + game );
    	log_d( result );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( title );
        alertDialogBuilder.setMessage( message );

        alertDialogBuilder.setPositiveButton("<< Prev",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_prev();
                    }
                });

        alertDialogBuilder.setNeutralButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_close();
                    }
                });

        alertDialogBuilder.setNegativeButton("Next >>",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_next();
                    }
                });

        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

	// file read
    private boolean read_board( int num ) 
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
		b_initial = columns[2];
		
		if (( b_width < MIN_WIDTH  )||( b_width > MAX_WIDTH )) {
    		log_d( "width " + b_width);
			return false;
		}
		if (( b_height < MIN_HEIGHT )||( b_height > MAX_HEIGHT )) {
	  		log_d( "height " +  b_height );
			return false;
		}
		if ( b_initial.length() == 0 ) {
    		log_d( "initial empty");
			return false;
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
			is = getAssets().open(file);
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

	// sqlite
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
        helper.insert( score );        	
    }

    private String get_score( int game )
    {
    	String str = "---";
        SlideScore score = helper.getScore( game );
        
        if ( score != null ) {
            str  = score.getStringMove();
            str += " moves "; 
            str += score.getStringTime();
			str += " sec";            	
		}

		return str; 
    }
        
	// others
    private void toast( String str ) 
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
    
    private void log_d( String s, int y, int x) 
    {
    	Log.d( TAG, s + " " + y + " " + x);  
	}
    
    private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}
    
}