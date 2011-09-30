package jp.ohwada.android.slidepuzzle2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SlideActivity extends Activity
	implements View.OnClickListener
{
	private final static String TAG  = "SlideActivity";

	// SharedPreferences
	public static final String PREF_NAME  = "slidepuzzle";
	
	// object
	private SlideView mView;
    private SlideGame mGame;

	// view component
	private TextView text_game   = null;
	private TextView text_score   = null;
	private EditText edit_start    = null;
	private Button button_prev   = null;
	private Button button_next   = null;
	private Button button_start    = null;

	private	float displayWidth = 0f;
	private	float displayHeight = 0f;
		    	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		text_game   = (TextView) findViewById(R.id.text_game);
		text_score   = (TextView) findViewById(R.id.text_score);
		edit_start    = (EditText) findViewById(R.id.edit_start);
		button_prev = (Button) findViewById(R.id.button_prev);
		button_next = (Button) findViewById(R.id.button_next);
		button_start  = (Button) findViewById(R.id.button_start);
		mView  = (SlideView) findViewById( R.id.slide ); 
		
		button_prev.setOnClickListener( this );
		button_next.setOnClickListener( this );
		button_start.setOnClickListener( this );

		getDisplaySize(); 
           		        
        mGame = new SlideGame( this );
        mGame.setHandler( mHandler );
        mGame.setAssets( getAssets() );
        mGame.setSharedPreferences( getSharedPreferences( PREF_NAME, MODE_PRIVATE) );
        mGame.initGame();
        
		mView.setDisplaySize( displayWidth, displayHeight );
		mView.setResources( getResources() ); 
        mView.setPackageName( getPackageName() ); 
        mView.setHandler( mHandler );
        mView.setHash( mGame.getHashIs() );
        mView.initGame();
                
        start_new_game( mGame.getPreferences() );
    }

    private void getDisplaySize()
    {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
	}

    // === onClick ===
    @Override
	public void onClick( View v )
	 {
	  	int id = v.getId();
	   	switch( id )
	   	{
	   	 	case R.id.button_prev:
				start_prev_game() ;
   				break;

	   	 	case R.id.button_next:
				start_next_game() ;
   				break;

	   	 	case R.id.button_start:
				click_start();
   				break;
   				
   			default:	
   				break;
   		}
	}

    private void click_start() 
	{
		boolean check = mGame.checkEditGame( edit_start );
		if ( ! check ) {
			toast( mGame.getErrorMsg() );
			return;
		}

		start_any_game( mGame.getGameNum() );
	}
		
	// start game
    private void start_prev_game() 
	{
		boolean check = mGame.checkPrevGame();
		if ( ! check ) {
			toast(  "NO Prev Game" );
			return;
		}
		
		// game num --
		start_new_game( mGame.getGameNum() );
	}

    private void start_next_game() 
	{
		boolean check = mGame.checkNextGame();					
		if ( ! check ) {
			toast( "NO Next Game" );
			return;
		}

		// game num ++
		start_new_game( mGame.getGameNum() );
	}

	private void start_any_game( int num ) 
	{
		boolean check = mGame.checkAnyGame( num );
		if ( ! check ) {
			toast( mGame.getErrorMsg() );
			return;
		}
		
		start_new_game(  num  );
	}

    private void start_new_game( int num ) 
	{
		boolean ret = mGame.readBoard( num );

   		if ( ret ) {
   		   	mGame.startGame( num );
   		   	   				
   			mView.startGame( 
   				mGame.getBoardWidth(), 
   				mGame.getBoardHeight(),
   				mGame.getBoardInitial() );
        	
			// show 
			text_game.setText("Game " + num );
			text_score.setText("Score " + mGame.getScore( num ) );		

		// failed
   		} else {
    		log_d( "NOT get new game " + num );
    		toast( " Sorry !\n NOT get new game" );
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

	/**
	 * === Message Handler ===
	 */
    private final Handler mHandler = new Handler() 
	{
		/**
	 	 * Processing Message Handler
	 	 */
        @Override
        public void handleMessage(Message msg) 
		{
            switch (msg.what) 
            {
            case SlideConstant.MESSAGE_SWAP:
            	event_swap();
                break;
                
            case SlideConstant.MESSAGE_PREV:
            	start_prev_game();
                break; 
                
             case SlideConstant.MESSAGE_NEXT:
            	start_next_game();
                break;
            
            case SlideConstant.MESSAGE_CLOSE:
                break;             
            }
        }
    };
    
    private void event_swap()
    {
    	mGame. swapPiece( 
    		mView.getMovePiece(),
    		mView.getSwapPieceY(),
    		mView.getSwapPieceX(),
    		mView.getSwapBlankY(),
    		mView.getSwapBlankX() );	
    }
    
	// others
    private void toast( String str ) 
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

    private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}
}