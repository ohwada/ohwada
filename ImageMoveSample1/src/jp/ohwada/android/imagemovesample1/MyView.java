package jp.ohwada.android.imagemovesample1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/** 
 * MyView
 */	  
public class MyView extends View {
  	// image	
	private static final int DRAWABLE = R.drawable.droid ;
  	// sensitivity  	
	private static final int SENSITITVITY = 5;

	private Context mContext = null;
    // view component  
    private Bitmap mBitmap = null;
    private Paint mPaint = null;

	private int displayWidth = 0;
	private int displayHeight = 0;
	private int mBitmapHalfWidth = 0;
	private int mBitmapHalfHeight = 0;
	private int rectLeft = 0;
	private int rectRight = 0;
	private int rectTop = 0;
	private int rectBottom = 0;
	private int borderLeft = 0;
	private int borderRight = 0;
	private int borderTop = 0;
	private int borderBottom = 0;		
	private int imageX = 0;
    private int imageY = 0;
    private boolean isTouch = false; 

    /** 
	 * === constractor ===
	 */	        
	public MyView( Context context ) {
     	super( context );
     	initView( context );
	}

    /** 
	 * initView
	 * @param Context contex
	 */	
	private void initView( Context context ) {
	    mContext = context;
		mPaint = new Paint();
		getDisplaySize();
		setRectSize( displayWidth, displayHeight );
		Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), DRAWABLE );
		setBitmap( bitmap );
        invalidate(); 
	}

    /** 
	 * getDisplaySize
	 */		
	private void getDisplaySize() {
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
	}

    /** 
	 * setRectSize
	 * @param int w
	 * @param int h	 
	 */	
	private void setRectSize( int w, int h ) {	
        imageX = w / 2;
        imageY = h / 2;
		rectLeft  =  w * 1 / 4  ;
		rectRight =  w * 3 / 4  ;
		rectTop  =  h * 1 / 4  ;
		rectBottom =  h * 3 / 4 ;
	} 

    /** 
	 * setBitmap
	 * @param Bitmap b 
	 */		        
	private void setBitmap( Bitmap b ) {
        mBitmap = b;
        mBitmapHalfWidth = mBitmap.getWidth() / 2 ; 
        mBitmapHalfHeight = mBitmap.getHeight() / 2 ;
		borderLeft  = rectLeft  + mBitmapHalfWidth;
		borderRight = rectRight - mBitmapHalfWidth;
		borderTop  = rectTop +  mBitmapHalfHeight;
		borderBottom = rectBottom - mBitmapHalfHeight;
	}        

    /** 
	 * === onDraw ===
	 */	        
	@Override
    protected void onDraw( Canvas canvas ) {
		// rect
    	Paint paintDisplay = new Paint();
        paintDisplay.setColor( Color.GRAY );
        Rect rectDisplay = new Rect( 0, 0, displayWidth, displayHeight );
        canvas.drawRect( rectDisplay, paintDisplay );
		// rect
    	Paint paintBorder = new Paint();
        paintBorder.setColor( Color.WHITE );
        Rect rectBorder = new Rect( rectLeft, rectTop, rectRight, rectBottom );
        canvas.drawRect( rectBorder, paintBorder );
		// bitmap
        canvas.drawBitmap(
        	mBitmap, 
         	imageX - mBitmapHalfWidth, 
            imageY - mBitmapHalfHeight, 
            mPaint);
	}

    /** 
	 * === onTouchEvent ===
	 */	
	@Override 
    public boolean onTouchEvent( MotionEvent event ) {
        int x = (int) event.getX();
        int y = (int) event.getY();
    	switch ( event.getAction() ) {
        	case MotionEvent.ACTION_DOWN :
        		execDown( x, y );
                break;        	       	
        	case MotionEvent.ACTION_MOVE :
        	    execMove( x, y );
                break;                
        	case MotionEvent.ACTION_UP :
        		execUp( x, y );
                break;
		}
        invalidate();            
        return true;
    }

    /** 
	 * execDown
	 * @param int x
	 * @param int y
	 */	
    private void execDown( int x, int y ) {
        isTouch = false; 
        if ( Math.abs( x - imageX ) > mBitmapHalfWidth ) return;
    	if ( Math.abs( y - imageY ) > mBitmapHalfHeight ) return;
    	isTouch = true; 
    }

    /** 
	 * execMove
	 * @param int x
	 * @param int y
	 */	        
    private void execMove( int x, int y ) { 
        if ( ! isTouch ) return;         
    	if ( Math.abs( x - imageX ) < SENSITITVITY ) return;
        if ( Math.abs( y - imageY ) < SENSITITVITY ) return;
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
        imageX = x;
        imageY = y;
	}

    /** 
	 * execUp
	 * @param int x
	 * @param int y
	 */		
	private void execUp( int x, int y ) {
    	execMove( x, y );
    	isTouch = false; 
    } 
}