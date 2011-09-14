package jp.ohwada.android.slidepuzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlidePuzzle extends Activity 
{
	private final static String TAG = "SlidePuzzle";
	
	private final static String[] MOVE_CHAR = {"L", "R", "U", "D"};

	private final static int MIN_WIDTH  = 3;
	private final static int MAX_WIDTH  = 6;
	private final static int MIN_HEIGHT = 3;
	private final static int MAX_HEIGHT = 6;
	private final static int MIN_GAME   = 1;
	private final static int MAX_GAME   = 5000;

	private final static int BLACK  = 0xff000000;
	private final static int GRAY   = 0xff333333;
	private final static int WHITE  = 0xffffffff;
	private final static int BLUE   = 0xff0000ff;
	private final static int YELLOW = 0xffffff00;

	private HashMap<String, Integer> hash_s_i = new HashMap<String, Integer>();
	private HashMap<Integer, String> hash_i_s = new HashMap<Integer, String>();

	private TextView[][] text_board = new TextView[ MAX_HEIGHT ][ MAX_WIDTH ];
	private int[][] board = new int[ MAX_HEIGHT ][ MAX_WIDTH ];
	private int[][] wall  = new int[ MAX_HEIGHT ][ MAX_WIDTH ];

	private TextView text_name   = null;
	private TextView text_result = null;
	private EditText edit_any    = null;
	private Button button_prev   = null;
	private Button button_next   = null;
	private Button button_any    = null;
	
	private int b_width  = 0;
	private int b_height = 0;
	private	String b_initial = "";

	private int game_num    = MIN_GAME;
	private long start_time = 0;
	private int result_num  = 0;
	private String results  = ""; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text_name   = (TextView) findViewById(R.id.text_name);
		text_result = (TextView) findViewById(R.id.text_result);
		edit_any    = (EditText) findViewById(R.id.edit_any);
		button_prev = (Button) findViewById(R.id.button_prev);
		button_next = (Button) findViewById(R.id.button_next);
		button_any  = (Button) findViewById(R.id.button_any);

        button_prev.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_prev();
            }
        });
        
        button_next.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_next();
            }
        });

        button_any.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_any();
            }
        });

		init_table();
		init_hash();

		start_any_game( game_num );
	}

    private void init_table()
    {
		//  TODO: describe more briefly 
		text_board[0][0] = (TextView) findViewById(R.id.text_0_0);
		text_board[0][1] = (TextView) findViewById(R.id.text_0_1);
		text_board[0][2] = (TextView) findViewById(R.id.text_0_2);
		text_board[0][3] = (TextView) findViewById(R.id.text_0_3);
		text_board[0][4] = (TextView) findViewById(R.id.text_0_4);
		text_board[0][5] = (TextView) findViewById(R.id.text_0_5);
		text_board[1][0] = (TextView) findViewById(R.id.text_1_0);
		text_board[1][1] = (TextView) findViewById(R.id.text_1_1);
		text_board[1][2] = (TextView) findViewById(R.id.text_1_2);
		text_board[1][3] = (TextView) findViewById(R.id.text_1_3);
		text_board[1][4] = (TextView) findViewById(R.id.text_1_4);
		text_board[1][5] = (TextView) findViewById(R.id.text_1_5);
		text_board[2][0] = (TextView) findViewById(R.id.text_2_0);
		text_board[2][1] = (TextView) findViewById(R.id.text_2_1);
		text_board[2][2] = (TextView) findViewById(R.id.text_2_2);
		text_board[2][3] = (TextView) findViewById(R.id.text_2_3);
		text_board[2][4] = (TextView) findViewById(R.id.text_2_4);
		text_board[2][5] = (TextView) findViewById(R.id.text_2_5);
		text_board[3][0] = (TextView) findViewById(R.id.text_3_0);
		text_board[3][1] = (TextView) findViewById(R.id.text_3_1);
		text_board[3][2] = (TextView) findViewById(R.id.text_3_2);
		text_board[3][3] = (TextView) findViewById(R.id.text_3_3);
		text_board[3][4] = (TextView) findViewById(R.id.text_3_4);
		text_board[3][5] = (TextView) findViewById(R.id.text_3_5);
		text_board[4][0] = (TextView) findViewById(R.id.text_4_0);
		text_board[4][1] = (TextView) findViewById(R.id.text_4_1);
		text_board[4][2] = (TextView) findViewById(R.id.text_4_2);
		text_board[4][3] = (TextView) findViewById(R.id.text_4_3);
		text_board[4][4] = (TextView) findViewById(R.id.text_4_4);
		text_board[4][5] = (TextView) findViewById(R.id.text_4_5);
		text_board[5][0] = (TextView) findViewById(R.id.text_5_0);
		text_board[5][1] = (TextView) findViewById(R.id.text_5_1);
		text_board[5][2] = (TextView) findViewById(R.id.text_5_2);
		text_board[5][3] = (TextView) findViewById(R.id.text_5_3);
		text_board[5][4] = (TextView) findViewById(R.id.text_5_4);
		text_board[5][5] = (TextView) findViewById(R.id.text_5_5);

        text_board[0][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,0);
            }
        });

        text_board[0][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,1);
            }
        });

        text_board[0][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,2);
            }
        });

        text_board[0][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,3);
            }
        });

        text_board[0][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,4);
            }
        });

        text_board[0][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(0,5);
            }
        });

        text_board[1][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,0);
            }
        });

        text_board[1][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,1);
            }
        });

        text_board[1][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,2);
            }
        });

        text_board[1][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,3);
            }
        });

        text_board[1][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,4);
            }
        });

        text_board[1][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(1,5);
            }
        });

        text_board[2][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,0);
            }
        });

        text_board[2][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,1);
            }
        });

        text_board[2][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,2);
            }
        });

        text_board[2][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,3);
            }
        });

        text_board[2][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,4);
            }
        });

        text_board[2][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,5);
            }
        });

        text_board[3][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(3,0);
            }
        });

        text_board[3][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(3,1);
            }
        });

        text_board[3][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(3,2);
            }
        });

        text_board[3][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(3,3);
            }
        });

        text_board[3][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(3,4);
            }
        });

        text_board[3][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,5);
            }
        });

        text_board[4][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(4,0);
            }
        });

        text_board[4][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(4,1);
            }
        });

        text_board[4][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(4,2);
            }
        });

        text_board[4][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(4,3);
            }
        });

        text_board[4][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(4,4);
            }
        });

        text_board[4][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(2,5);
            }
        });

        text_board[5][0].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,0);
            }
        });

        text_board[5][1].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,1);
            }
        });

        text_board[5][2].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,2);
            }
        });

        text_board[5][3].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,3);
            }
        });

        text_board[5][4].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,4);
            }
        });

        text_board[5][5].setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                click_board(5,5);
            }
        });

    }

    private void init_hash() 
	{
		for (int i=0; i<10; i++) {
			String s = Integer.toString(i);
			hash_s_i.put( s, i );
			hash_i_s.put( i, s );
		}
		for (int j=0; j<26; j++ ) {
			// A : 65 0x41
			int j1 = j + 65;
			int j2 = j + 10;
			String s1 = Character.toString( (char)j1 );
			hash_s_i.put( s1, j2 );
			hash_i_s.put( j2, s1 );
		}
		hash_s_i.put( "=",  -1 );
		hash_i_s.put( -1,  "=" );
		
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

    private void click_back() 
	{
		// do noting
		// erase AlertDialog
	}

    private void click_any() 
	{
		String any = edit_any.getText().toString();
		if ( any.length() == 0 ) {
			toast( "Please enter Gmae number" );
			return;
		}

		start_any_game( Integer.valueOf( any ) );
	}

    private void click_board(int y, int x) 
	{
		if ( wall[y][x] == 1 ) {
			return ;
		}

		move_board(y, x, 0);
		move_board(y, x, 1);
		move_board(y, x, 2);
		move_board(y, x, 3);

		if ( check_goal() ) {
			int time = (int)(( System.currentTimeMillis() - start_time ) / 1000 );
			alert_dialog( time );
		}
	}

	// start game
    private void start_prev_game() 
	{
		game_num --;
		if ( game_num < MIN_GAME ) {
			game_num = MIN_GAME;
			toast( "NO Prev Game" );
			return;
		}
		
		start_new_game( game_num );
	}

    private void start_next_game() 
	{
		game_num ++;
		if ( game_num > MAX_GAME ) {
			game_num = MAX_GAME;
			toast( "NO Next Game" );
			return;
		}
		
		start_new_game( game_num );
	}

	private void start_any_game( int num ) 
	{
		game_num = num;

		if ( game_num < MIN_GAME ) {
			game_num = MIN_GAME;
			toast( "Out of renge: min = " + MIN_GAME );
			return;
		}

		if ( game_num > MAX_GAME ) {
			game_num = MAX_GAME;
			toast( "Out of renge: max = " + MAX_GAME );
			return;
		}
		
		start_new_game( game_num );
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
			text_name.setText("Game " + num );
			text_result.setText("");
			results = "";
			result_num  = 0; 			
     		start_time = System.currentTimeMillis();
 
   		} else {
    		log_d( "NOT get new game " + num );
    		toast( " Sorry !\n NOT get new game" );
    	}
	}

	// board
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
				wall[y][x] = 0;
				text_board[y][x].setTextColor( YELLOW );
				text_board[y][x].setVisibility( View.VISIBLE ); 

				if ( n == 0 ) {
					set_board_blank(y, x);
				} else if ( n == -1 ) {
					set_board_wall(y, x); 
				} else {
					set_board_normal(y, x, n );
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
		text_board[y][x].setBackgroundColor( BLUE );
	}

    private void set_board_blank(int y, int x) 
	{
//    	log_d( "blank", y, x); 
		board[y][x] = 0 ;
		text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( WHITE );
	}

    private void set_board_wall(int y, int x) 
    {
//    	log_d( "wall", y, x); 
		board[y][x] = -1 ;
    	wall[y][x] = 1;
    	text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( GRAY );
	}

    private void set_board_outside(int y, int x) 
    {
//    	log_d( "outside", y, x); 
		board[y][x] = -1 ;
    	wall[y][x] = 1;
    	text_board[y][x].setText( "" );
		text_board[y][x].setClickable( false );
		text_board[y][x].setBackgroundColor( BLACK );
		text_board[y][x].setVisibility( View.GONE );  
	}

    private void move_board(int y, int x, int move) 
	{
    	int x0 = x;
    	int y0 = y;

    	switch (move)
		{
			case 0:
				x0 = x + 1;
				break;
			
    		case 1:
    			x0 = x - 1;
    			break;
    			
    		case 2:
    			y0 = y + 1;
    			break;
    			
    		case 3:
    			y0 = y - 1;
    			break;
		}
    			
    	if ( swap_board(y, x, y0, x0) ) {
			result_num ++;
    		results += MOVE_CHAR[ move ];
    		text_result.setText( results ); 
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
		if (( x < 0 )||( x >= b_width )||( y < 0 )||( y >= b_height )) {
			return false;
		}
		if ( board[y][x] == 0 ) {
			return true;
		}
			return false;
	}
    
    private boolean check_goal() 
	{
		boolean flag = false;
    	int ii = 1;
    	
		for (int y=0; y<b_height; y++ ) {
			for (int x=0; x<b_width; x++ ) {
				int b = board[y][x];
				int w = wall[y][x] ;

				// must be all walls after 'blank'
				if ( flag ) {
					if ( w == 0 ) {
						return false;
					}
				} else {
					// 'blank'
					if ( b == 0 ) {
						flag = true;

					// must be suitable 'slide' or 'wall' 
					} else if (( b != ii )&&( w == 0)) {
						return false;
					}
				}
				
				ii ++;
			}
		}
		return true;
	}

	// alert
	private void alert_dialog( int time )
	{
		String title = "GOAL !!";
		String message = "Game " + game_num + "\n" + 
			result_num + " steps \n" +
			results + "\n" + 
			time + " sec" ;

    	log_d( "Game " + game_num );
    	log_d( results );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( title );
        alertDialogBuilder.setMessage( message );

        alertDialogBuilder.setPositiveButton("<< Prev",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_prev();
                    }
                });

        alertDialogBuilder.setNeutralButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_back();
                    }
                });

        alertDialogBuilder.setNegativeButton("Next >>",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_next();
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