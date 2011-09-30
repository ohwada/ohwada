package jp.ohwada.android.slidepuzzle2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
  
public class SlidePiece 
{
	// tag 
	private final static String TAG  = "SlidePiece";
    
    // sensitivity 
    private static final float DIFF = 10f;  
    
    // object
    private SlideSquare mSquare = null;
	private Bitmap mBitmap;		

	// setter
	private int squareX = SlideConstant.SQUARE_UNDEFINED;
	private int squareY = SlideConstant.SQUARE_UNDEFINED;
	private boolean squareFlag = false;
	
	// getter
	private float imageCenterX = 0f;
	private float imageCenterY = 0f;	
		
	// image	
	private float imageHalfWidth = 0f;
	private float imageHalfHeight = 0f;
		
	// border
	private float borderLeft = 0f;
	private float borderRight = 0f;
	private float borderTop = 0f;
	private float borderBottom = 0f;
        	
	public SlidePiece() 
	{
		// dummy
	}

// === getter ===
 	public int getSquareX()
  	{
  		return squareX;   			
 	}

 	public int getSquareY() 
  	{
  		return squareY;   			
 	}

	public float getImageCenterX() 
	{
    	return imageCenterX ;     			
    }
    
    public float getImageCenterY() 
	{
    	return imageCenterY ;     			
    }
       
// === init game ===
	public void initSquare( SlideSquare s ) 
 	{
 		mSquare = s;  			
	}
	
	public void initBitmap( Bitmap b ) 
	{
	    int size = mSquare.getPieceSize();	        
        mBitmap = scaleBitmap( b, size );
        imageHalfWidth = mBitmap.getWidth() / 2 ; 
        imageHalfHeight = mBitmap.getHeight() / 2 ;
	}
 	     
	private Bitmap scaleBitmap( Bitmap b, int size ) 
	{
		int w = b.getWidth(); 
        int h = b.getHeight();
        float s = 0;
        if ( w > h ) {
        	s = w ;       		
        } else {
        	s = h ;
        }
        
        float scale = size / s;  
        Matrix m = new Matrix();
        m.postScale( scale, scale );
        return Bitmap.createBitmap( b, 0, 0, w, h, m, true );
	}
	
// === start game ===
	public void clreaSquarePosition() 
 	{
 		squareX = SlideConstant.SQUARE_UNDEFINED ;
        squareY = SlideConstant.SQUARE_UNDEFINED ;
        squareFlag = false;
	}
	
	public void setSquarePosition( int y, int x ) 
 	{
 		squareX = x;
        squareY = y;
        squareFlag = true;
	}

	public void setImageCenter() 
	{
		setImageCenter( squareY, squareX );
	}
	
	public void setImageCenter( int y, int x ) 
	{
    	imageCenterX = mSquare.getCenterX( y, x );
    	imageCenterY = mSquare.getCenterY( y, x );       			
    }

// === touch ===    
    public void setBorder( int blankY, int blankX ) 
    {
    	int c = checkBlank( blankY, blankX );

		int sx0 = mSquare.getX0( squareY, squareX );
		int sx1 = mSquare.getX1( squareY, squareX );
		int sy0 = mSquare.getY0( squareY, squareX );
		int sy1 = mSquare.getY1( squareY, squareX );

		int bx0 = mSquare.getX0( blankY, blankX );
		int bx1 = mSquare.getX1( blankY, blankX );
		int by0 = mSquare.getY0( blankY, blankX );
		int by1 = mSquare.getY1( blankY, blankX );
		
		int m = mSquare.getMoveMargin();
								    
    	switch (c)
    	{
    		case SlideConstant.BLANK_L :
    			borderLeft  = sx0 + m;
    			borderRight = bx1 - m;
    			borderTop  = sy0 +  m;
				borderBottom = sy1 - m;
				break;
			
			case SlideConstant.BLANK_R :
    			borderLeft  = bx0 + m;
    			borderRight = sx1 - m;
    			borderTop  = sy0 +  m;
				borderBottom = sy1 - m;
				break;
					
			case SlideConstant.BLANK_U :
    			borderLeft  = sx0 + m;
    			borderRight = sx1 - m;
    			borderTop  = sy0 +  m;
				borderBottom = by1 - m;
				break;

			case SlideConstant.BLANK_D :
    			borderLeft  = sx0 + m;
    			borderRight = sx1 - m;
    			borderTop  = by0 +  m;
				borderBottom = sy1 - m;
				break;
					
			default:
				borderLeft  = 0;
    			borderRight = 0;
    			borderTop  = 0;
				borderBottom = 0;
				break;
    	}   	
    }

 // === check ===         	
    public int checkBlank( int blankY, int blankX ) 
    {
    	int d = SlideConstant.BLANK_N;
    	if ( squareY  == blankY ) {
    	    	if ( squareX  == blankX - 1 ) {
    	    		d = SlideConstant.BLANK_L;
    	    	}  else if ( squareX  == blankX + 1 ) {
    	    		d = SlideConstant.BLANK_R;
    	    	}
    	}  else if ( squareX  == blankX ) {
    	    	if ( squareY  == blankY - 1 ) {
    	    		d = SlideConstant.BLANK_U;
    	    	}  else if ( squareY  == blankY + 1 ) {
    	    		d = SlideConstant.BLANK_D;
    	    	}
    	 }
    	 return d;
    }

// === onDraw ===
	public void drawPiece( Canvas canvas, Paint paint ) 
	{
		if ( !squareFlag ) {
			return;
		}
		
        canvas.drawBitmap(
        	mBitmap, 
        	imageCenterX - imageHalfWidth, 
            imageCenterY - imageHalfHeight, 
            paint); 
	}

// === onTouchEvent ===
	public boolean matchPiece( int y, int x ) 
	{
 		if ( squareFlag &&( squareX == x )&&( squareY == y )) {
 			return true;
 		}
 		return false;
	}
	
	public void movePiece( float y, float x ) 
	{
     	if (( Math.abs( x - imageCenterX ) < DIFF ) &&
    		( Math.abs( y - imageCenterY ) < DIFF )) {
    		return;
    	}		

    	if ( x < borderLeft ) {
    		x = borderLeft;
    	} else if ( x > borderRight ) {
    		x = borderRight ;
    	}
    	if ( y < borderTop ) {
    		y = borderTop;
    	} else if ( y > borderBottom ) {
    		y = borderBottom ;
    	}

    	float defX = Math.abs( x - imageCenterX );
    	float defY = Math.abs( y - imageCenterY );
		
		if ( defX > defY) {
	    	imageCenterX = x;
		} else {
	    	imageCenterY = y;	
		}
	}

// --- other ---
    private void log_d( String s, int y, int x) 
    {
    	Log.d( TAG, s + " " + y + " " + x);  
	}
    
    private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}
}