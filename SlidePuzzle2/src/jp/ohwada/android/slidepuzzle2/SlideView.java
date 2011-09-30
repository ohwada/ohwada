package jp.ohwada.android.slidepuzzle2;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
  
public class SlideView extends View 
{ 
	// tag
	private final static String TAG  = "SlideView";
	
	// limit
    private final static int MAX_WIDTH = SlideConstant.MAX_WIDTH ;
    private final static int MAX_HEIGHT = SlideConstant.MAX_HEIGHT ;
    private final static int MIN_PIECE = SlideConstant.MIN_PIECE ;
    private final static int MAX_PIECE = SlideConstant.MAX_PIECE ;
    
    // piece
    private final static int PIECE_UNDEFINED = 0;
    
    // set object
    private Resources mResources = null;    
    private Handler mHandler = null;
    private HashMap<Integer, String> mHash = null ;
    
    // local object
	private Paint mPaint = null;
	private SlideSquare mSquare = null;
	private SlidePiece[] mPiece = new SlidePiece[ MAX_PIECE ];

	// setter
    private String packageName = null;
	private float displayWidth = 0f;
	private float displayHeight = 0f;
	private int boardWidth = 0;
	private int boardHeight = 0;

	// getter
	private int movePieceNum = 0; 
	private int swapPieceX = 0;
	private int swapPieceY = 0;
	private int swapBlankX = 0;
	private int swapBlankY = 0;
	
	// board		
	private boolean[][] wall = new boolean[ MAX_HEIGHT ][ MAX_WIDTH ];
	private int boardPiece = 0;
	
	// position	
	private int blankX = 0;
	private int blankY = 0;
	
	// touch
	private boolean isTouchPeice = false;	
	private boolean isTouchBlank = false;
	private int touchPiece = 0; 
	private float centerPosX = 0;
	private float centerPosY = 0;
    
	public SlideView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}
	
	public SlideView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
		
	public SlideView(Context context) 
	{
		super(context);
	}

// === init ===
    public void setHandler( Handler h ) 
    {
        mHandler = h;
    }
    
    public void setResources( Resources r ) 
    {
    	mResources = r;
	}
    
    public void setPackageName( String name ) 
    {
    	packageName = name;
	}

    public void setHash( HashMap<Integer, String> h )
    {
    	mHash = h;
    }

	public void setDisplaySize( float w, float h ) 
    {
		displayWidth = w;
		displayHeight = h;
    } 

// === getter ===
	public int getMovePiece() 
	{
		return movePieceNum;       			
	}
	
	public int getSwapPieceX() 
	{
		return swapPieceX;       			
	}

	public int getSwapPieceY() 
	{
		return swapPieceY;       			
	}

	public int getSwapBlankX() 
	{
		return swapBlankX;       			
	}		

	public int getSwapBlankY() 
	{
		return swapBlankY;       			
	}

// === init game ===	       		
	public void initGame()
	{	
		mPaint = new Paint();

		mSquare = new SlideSquare();
		mSquare.setDisplaySize( displayWidth , displayHeight );

        init_all_pieces();
        load_all_Images(); 
	}

    private void init_all_pieces() 
    {
        for (int n = 0; n < MAX_PIECE; n++ ) {
        	mPiece[n] = new SlidePiece();
        }
     }

	private void load_all_Images()
	{	
    	// 1 -> 9
		for ( int i = SlideConstant.MIN_PIECE; i < 10; i++ ) {
			String name = "fig" + mHash.get( i );
			load_bitmap( i, name );
		}
		
		// A -> Z
		for ( int i = 10; i < SlideConstant.MAX_PIECE ; i++ ) {
			String s = mHash.get( i );
			String name = s.toLowerCase();
			load_bitmap( i, name );
		}
	}

	private void load_bitmap( int i, String name )
	{
		int id = mResources.getIdentifier( name, "drawable", packageName );
		if ( id != 0 ) {
			Bitmap bitmap = BitmapFactory.decodeResource( mResources, id );
			set_birmap( i, bitmap );
		}
	}

    private void set_birmap( int n, Bitmap b ) 
    {
        mPiece[n].initSquare( mSquare ) ;
        mPiece[n].initBitmap( b );
	}
	
// === start game ===
	public void startGame( int w, int h, int[] initial )
	{
		boardWidth = w;
		boardHeight = h;
		boardPiece = w * h ;
		
	    mSquare.initBoard( w, h );
    	
    	crear_wall();
    	clear_all_piece(); 

		set_board( w, h, initial );

		// draw		
		invalidate();
	}
    
	private void crear_wall()
	{
        for ( int y = 0; y < MAX_HEIGHT ; y++ ) {
            for ( int x = 0; x < MAX_WIDTH ; x++ ) {
            	wall[y][x] = false;
            }
        }
    }

    private void clear_all_piece() 
	{
    	for ( int i = MIN_PIECE ; i < MAX_PIECE ; i ++ ) {
    		mPiece[i].clreaSquarePosition() ; 
    	}
    }
    
	private void set_board( int width, int height, int[] initial )
	{			
		int i = 0;

		for ( int y=0; y < height; y++ ) {
			for ( int x=0; x < width; x++ ) {				
				int n = initial[ i ];
				i ++;		
				switch (n)
				{
					case SlideConstant.B_BLANK :
					    set_blank( y, x );
						break;
						
					case SlideConstant.B_WALL :
					    set_wall( y, x );
						break;
						 
					default:
						set_piece( y, x, n );
						break;
				}
			}
		}
    }

	private void set_wall( int y, int x ) 
	{
		wall[y][x] = true;       			
    }

	private void set_blank( int y, int x ) 
	{
    	blankX = x ;
        blankY = y ;     			
    }

	private void set_piece( int y, int x , int n ) 
    {
		set_piece_position( y, x , n );
		set_image_center( n );
    }

	private void set_piece_position( int y, int x , int n ) 
    {
		mPiece[n].setSquarePosition( y, x );
    }
	
	private void set_image_center( int n ) 
    {
		mPiece[n].setImageCenter();
    }
	
// === onDraw ===
	@Override
	protected void onDraw(Canvas canvas) 
    {
    	canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

        drawBorad( canvas );
        drawWall( canvas ); 
        drawBlank( canvas );
        drawPieces( canvas );
	}
                
	private void drawBorad( Canvas canvas ) 
    {
		Paint pBorad = new Paint();
        pBorad.setColor(Color.WHITE);
        Rect rBoard = mSquare.createBoardRect();
        canvas.drawRect( rBoard , pBorad );
	}
	
	private void drawWall( Canvas canvas ) 
    {
		Paint pWall = new Paint();
        pWall.setColor(Color.GRAY);
        Rect rWall = null;
            
		for (int y = 0; y < boardHeight ; y++ ) {
            for (int x = 0; x < boardWidth ; x++ ) {
            	if ( wall[y][x] ) {           			
                 	rWall = createSquareRect( y, x );
                    canvas.drawRect( rWall, pWall );
            	}
            }
        }
	}

	private void drawBlank( Canvas canvas ) 
    {
		Paint pBlank = new Paint();
        pBlank.setColor(Color.WHITE);
        Rect rBlank = createSquareRect( blankY, blankX );
        canvas.drawRect( rBlank, pBlank );
	}

	private void drawPieces( Canvas canvas ) 
    {
        for ( int n = MIN_PIECE; n < boardPiece; n++ ) {
            mPiece[n].drawPiece( canvas, mPaint ) ;
        }
    }
        
// === onMeasure ===
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec );
	}
	
// === onTouchEvent ===       
    public boolean onTouchEvent( MotionEvent event  ) 
    {
    	int action = event.getAction();
		float x = event.getX() ;
        float y = event.getY() ;
        
        boolean ret = false;
             
        switch ( action )
        {
        	case MotionEvent.ACTION_DOWN :
        		touchDown( y , x );
                break;

        	case MotionEvent.ACTION_MOVE :
        		ret = touchMove( y, x ); 
                break;
                
        	case MotionEvent.ACTION_UP :
        		ret = touchUp( y, x );
                break;
            
            default:
            	Log.d(TAG, "execTouchEvent " + action );
            	break;
		}

		// draw
        invalidate();            
        return true;
	}
	
// --- touch down ---
	private void touchDown( float y, float x ) 
	{
        isTouchPeice = false;
    	isTouchBlank = false;
        touchPiece = PIECE_UNDEFINED;
        
        if ( ! mSquare.checkTouchAny( y, x ) ) {
        	return;
        }
        
        int tx = mSquare.getTouchX();
        int ty = mSquare.getTouchY();
        if (( tx == blankX )&&( ty == blankY )) {
         	return;
        }
        		
        int n = matchPiece( ty, tx );
    	if ( n == PIECE_UNDEFINED ) {
        	return;
        }

    	if ( ! findPieceBlank( n ) ) {
        	return;
        }

        isTouchPeice = true;
        touchPiece = n;
        mPiece[n].setBorder( blankY, blankX );
    }

	private int matchPiece( int y, int x )
	{
        for (int n = MIN_PIECE; n < boardPiece; n++ ) {
        	if ( mPiece[n].matchPiece( y, x ) )  {
        		return n;
        	}
        }
        return PIECE_UNDEFINED;
	}

	private boolean findPieceBlank( int n )
	{
		movePieceNum = mPiece[n].checkBlank( blankY, blankX );
		if ( movePieceNum != SlideConstant.BLANK_N ) {
			return true;
		}
		return false;
	}
	
// --- touch move ---	
	private boolean touchMove( float y, float x ) 
	{
		boolean flag = false; 
    	if ( isTouchPeice ) {
        	movePiece( y, x );
        	if ( mSquare.checkTouchCenter( centerPosY, centerPosX, blankY, blankX ) ) {
            	isTouchBlank = true;
        	}
        }
        return flag;
	}
        
	private boolean touchUp( float y, float x ) 
	{
		boolean flag = false; 
		
    	if ( isTouchPeice ) {
    		boolean p = checkTouchPiece( y, x );
    		boolean b = checkTouchBlank( y, x );
    		
        	if ( b || ( !p && isTouchBlank ) ) {
            	boolean ret = swapPiece( touchPiece );
            	if ( ret ) {
            		sendMassage();
					flag = true;
				}
			}	 
        	if ( ! flag )  {
        		set_image_center( touchPiece );
        	}
        }
    	
        isTouchPeice = false;
        isTouchBlank = false;
        return flag;
	}

	private boolean checkTouchPiece( float y, float x ) 
	{
    	int sx = getPieceX( touchPiece );
    	int sy = getPieceY( touchPiece );
    	if ( sx == SlideConstant.SQUARE_UNDEFINED ){
    		return false;
    	}
    	if ( sy == SlideConstant.SQUARE_UNDEFINED ){
    		return false;
    	}
    	return checkTouchSquare( y, x, sy, sx );
    }
	
	private boolean checkTouchBlank( float y, float x ) 
	{
    	return checkTouchSquare( y, x, blankY, blankX );
    }

	private boolean swapPiece( int n ) 
	{
    	swapPieceX = getPieceX( n );
    	swapPieceY = getPieceY( n );
    	swapBlankX = blankX;
    	swapBlankY = blankY;
    	
    	if ( swapPieceX == SlideConstant.SQUARE_UNDEFINED ){
    		return false;
    	}
    	if ( swapPieceY == SlideConstant.SQUARE_UNDEFINED ){
    		return false;
    	}
    	
    	set_piece( swapBlankY, swapBlankX, n ); 
    	set_blank( swapPieceY, swapPieceX );
    	return true;
	}
	
// --- square ---
	private Rect createSquareRect( int y, int x ) 
    {
    	return mSquare.createSquareRect( y, x );
	}

	private boolean checkTouchSquare( float y, float x, int py, int px ) 
	{
		return mSquare.checkTouch( y, x, py, px );
	}
	    	   	
// --- piece ---	
	private int getPieceX( int n ) 
	{
    	return mPiece[n].getSquareX();
	}

	private int getPieceY( int n ) 
	{
    	return mPiece[n].getSquareY();
	}
			
	private void movePiece( float y, float x ) 
	{
		movePiece( touchPiece, y, x );
	}

	private void movePiece( int n, float y, float x ) 
	{
    	mPiece[n].movePiece( y, x );
    	centerPosX = mPiece[n].getImageCenterX();
    	centerPosY = mPiece[n].getImageCenterY();
	}

// --- message ---	
	private void sendMassage() 
    {
		Message msg = mHandler.obtainMessage( SlideConstant.MESSAGE_SWAP );
        mHandler.sendMessage( msg );
    }
 
 // --- other ---	   	
    private void log_d( String s, int y, int x, int n ) 
    {
    	Log.d( TAG, build_msg( s, y, x, n ) );  
	}
    
    private void log_d( String s, int y, int x) 
    {
    	Log.d( TAG, build_msg( s, y, x ) );  
	}
    
    private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}

    private String build_msg( String s, int y, int x, int n ) 
    {
    	String str = s + " " + y + " " + x + " " + n ;
    	return str;
	}
    
    private String build_msg( String s, int y, int x ) 
    {
    	String str = s + " " + y + " " + x ;
    	return str;
	}
}