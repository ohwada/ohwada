package jp.ohwada.android.slidepuzzle2;

import android.graphics.Rect;
import android.util.Log;
  
public class SlideSquare 
{ 
	// tag
	private final static String TAG  = "SlideSquare";

	// limit
    private final static int MAX_WIDTH = SlideConstant.MAX_WIDTH ;
    private final static int MAX_HEIGHT = SlideConstant.MAX_HEIGHT ;
    private final static int MAX_SLIDE = SlideConstant.MAX_SLIDE ;
      
    private final static int MAX_DERECTION = 4;
    
    // margin  	
    private static final int BOARD_MARGIN = 20;
    private static final int PIECE_MARGIN = 5; 
    private static final int CENTER_MARGIN = 5; 
    private static final int MOVE_MARGIN = 1; 
    
    // square
    private final static int X0 = 0;
    private final static int X1 = 1;
    private final static int Y0 = 2;
    private final static int Y1 = 3;

    private final static int TOUCH_UNDEFINED = -1;

	// setter
	private float displayWidth = 0f;
	private float displayHeight = 0f;	
	private int boardWidth = 0;
	private int boardHeight = 0;
	
    // getter
	private int touchX = 0;
	private int touchY = 0; 
		
	//  square   		
	private int[][][] square = new int[ MAX_HEIGHT ][ MAX_WIDTH ][ MAX_DERECTION ];
	
	// board
	private int boardLeft = 0;
	private int boardRight = 0;
	private int boardTop = 0;
	private int boardBottom = 0;
	private int squareDiv = 0 ;
	
	public SlideSquare( ) 
	{
		// dummy
	}

	public void setDisplaySize( float w, float h) 
	{
		displayWidth = w;
        displayHeight = h;
        
        squareDiv = ( (int)displayWidth - 2 * BOARD_MARGIN ) / MAX_SLIDE ;
	}
// === getter ===
    public int getTouchX()
	{
		return touchX;
    }
    
    public int getTouchY()
	{
		return touchY;
    }
    	
	public int getX0( int y, int x )
	{
		return square[y][x][X0];
    }

	public int getX1( int y, int x )
	{
		return square[y][x][X1];
    }
    
    public int getY0(  int y, int x )
	{
		return square[y][x][Y0];
    }
    
    public int getY1(  int y, int x )
	{
		return square[y][x][Y1];
    }

	public int getCenterX(  int y, int x )
	{
    	int p = ( square[y][x][X0] + square[y][x][X1] ) / 2 ;
    	return p; 
    }

	public int getCenterY( int y, int x )
	{
    	int p = ( square[y][x][Y0] + square[y][x][Y1] ) / 2 ;
    	return p; 
    }
   
	public int getPieceSize()
	{    
		int s = squareDiv - 2 * PIECE_MARGIN ;
    	return s;
	}

	public int getMoveMargin()
	{    
		int s = squareDiv / 2 - MOVE_MARGIN ;
    	return s;
	}
	
// === start game ===	    
    public void initBoard( int w, int h )     
	{
		boardWidth = w;
		boardHeight = h;

		clear_square();
		set_square( w, h ) ;

//		debug_square();
	}	
		
	private void clear_square()
	{
        for ( int y = 0; y < MAX_HEIGHT ; y++ ) {
            for ( int x = 0; x < MAX_WIDTH ; x++ ) {
            	square[y][x][X0] = 0 ;
            	square[y][x][X1] = 0 ;
            	square[y][x][Y0] = 0 ;
            	square[y][x][Y1] = 0 ;
            }
        }
     } 
      	
    public void set_square( int w, int h  )   
	{
		int width = w * squareDiv;
		int height = h * squareDiv;
		
		boardLeft = ( (int)displayWidth - width ) / 2 ;
		boardRight = boardLeft + width ;
		boardTop = BOARD_MARGIN ;
		boardBottom = BOARD_MARGIN + height;		
        		
        for ( int y = 0; y < h ; y++ ) {
            for ( int x = 0; x < w ; x++ ) {
            	square[y][x][X0] = boardLeft + squareDiv * x;
            	square[y][x][X1] = boardLeft + squareDiv * ( x + 1 );
            	square[y][x][Y0] = boardTop + squareDiv * y;
            	square[y][x][Y1] = boardTop + squareDiv * ( y + 1 );
            }
        }
        
//        debug_square();
    }

	private void debug_square()
	{
		log_d("debug_square");
    	for ( int y = 0; y < MAX_HEIGHT ; y++ ) {
    		for ( int x = 0; x < MAX_WIDTH ; x++ ) {
    			String msg = y + " " + x + " "; 
    			msg += square[y][x][X0] + " ";
    			msg += square[y][x][X1] + " ";
    			msg += square[y][x][Y0] + " ";
    			msg += square[y][x][Y1] + " ";
    			log_d(msg);
        	}
    	}
	}
	
// === onDraw ===
	public Rect createBoardRect()
	{  
        Rect r = new Rect( boardLeft, boardTop, boardRight, boardBottom );
        return r;
     }

	public Rect createSquareRect( int y, int x ) 
    {
		int x0 = square[y][x][X0] + PIECE_MARGIN;
		int x1 = square[y][x][X1] - PIECE_MARGIN;
		int y0 = square[y][x][Y0] + PIECE_MARGIN;
		int y1 = square[y][x][Y1] - PIECE_MARGIN;            			
		Rect r = new Rect( x0, y0, x1, y1 );
		return r;
	}

// === onTouchEvent ===
	public boolean checkTouchAny( float tocuh_y, float tocuh_x ) 
	{
		touchX = TOUCH_UNDEFINED;
		touchY = TOUCH_UNDEFINED;  
        for ( int y = 0; y < boardHeight ; y++ ) {
            for ( int x = 0; x < boardWidth ; x++ ) {
            
            	if ( checkTouch( tocuh_y, tocuh_x, y, x )) {
            		touchX = x;
            		touchY = y;            		
            		return true;
            	}
            }
        }    
        return false;
    }
	
	public boolean checkTouch( float tocuh_y, float tocuh_x, int y, int x ) 
	{
        if (( tocuh_x > square[y][x][X0] ) &&
        	( tocuh_x < square[y][x][X1] ) &&
        	( tocuh_y > square[y][x][Y0] ) &&
        	( tocuh_y < square[y][x][Y1] )) {

        	return true;
        }
        return false;
	}
	
	public boolean checkTouchCenter( float ty, float tx, int sy, int sx ) 
	{
		int tocuh_x = (int) tx;	
		int tocuh_y = (int) ty;
		int center_x = (int) getCenterX( sy, sx );
		int center_y = (int) getCenterY( sy, sx );		

        if (( tocuh_x > center_x - CENTER_MARGIN ) &&
        	( tocuh_x < center_x + CENTER_MARGIN ) &&
        	( tocuh_y > center_y - CENTER_MARGIN ) &&
        	( tocuh_y < center_y + CENTER_MARGIN )) {

        	return true;
        }
        return false;
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