package jp.ohwada.android.slidepuzzle;

import android.widget.EditText;

// check input parameter
public class SlideInput 
{
	// game limit
	public final static int MIN_GAME  = 1;
	public final static int MAX_GAME  = 5000;
	
	// getter
	private int game = 0;
	private String err = "";
	
    /** 
    	* === constractor ===
    	*/
    public SlideInput()
	{
		// dummy
	}

	// === getter ===
	public int getGame() 
	{
		return game;
	}
	
	public String getErrorMsg() 
	{
		return err;
	}

	// === check edittext ===
	public boolean checkEditGame( EditText edit ) 
	{
		return checkStringGame( edit.getText().toString() );
	}

	// check string value
	public boolean checkStringGame( String str ) 
	{
		if ( str.length() == 0 ) {
			err = "Please enter Game number";
			return false;
		}
	
		return checkIntGame( Integer.valueOf( str ) );
	}

	// === check prev game ===
	public boolean checkPrevGame( int num ) 
	{
		return checkIntGame( num - 1 );
	}

	// === check next game ===
	public boolean checkNextGame( int num ) 
	{
		return checkIntGame( num + 1 );
	}

	// === check int value ===		
	public boolean checkIntGame( int num ) 
	{
		game = 0;
		err = "";
	
		if ( num < MIN_GAME ) {
			game = MIN_GAME;
			err =  "Out of renge: min = " + MIN_GAME;
			return false;
		}

		if ( num > MAX_GAME ) {
			game = MAX_GAME;
			err = "Out of renge: max = " + MAX_GAME;
			return false;
		}

		game = num;		
		return true;
	}	
}