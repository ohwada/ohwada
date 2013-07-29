package jp.ohwada.android.gamepadjcu2312f;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Gamepad View
 */
public class GamepadView extends View {
    
    private MotionEventManager mMotionEventManager;
    private KeyEventManager mKeyEventManager;
    
    private JoystickView mJoystickView1;
    private JoystickView mJoystickView2;
    private JoystickView mJoystickView3;
        
    private Bitmap mBitmap;
    private Paint mPaintBitmap;
    private Paint mPaintButton;

    private int mDisplayWidth;
    private int mDisplayHeight;
		
    private int m1X, m1Y;
    private int m2X, m2Y;
    private int m3X, m3Y;
    private int m4X, m4Y;
    private int m9X, m9Y;
    private int m10X, m10Y;

    private int mUpX, mUpY;
	private int mDownX, mDownY;
	private int mLeftX, mLeftY;
	private int mRightX, mRightY;
                       
    private float mRadius;
		
    private Rect mRect5;
    private Rect mRect6;
    private Rect mRect7;
    private Rect mRect8;
    private Rect mRect11;
    private Rect mRect12;

    private boolean isPush1 = false;
    private boolean isPush2 = false;
    private boolean isPush3 = false;
    private boolean isPush4 = false;
    private boolean isPush5 = false;
    private boolean isPush6 = false;
    private boolean isPush7 = false;
	private boolean isPush8 = false;
	private boolean isPush9 = false;
	private boolean isPush10 = false;
	private boolean isPush11 = false;
	private boolean isPush12 = false;
	private boolean isPushUp = false;
	private boolean isPushDown = false;
	private boolean isPushLeft = false;
	private boolean isPushRight = false;
			
	private boolean isDubug = false;
                   
	/*
	 * === constractor ===
	 */         
    public GamepadView( Context context ) {
        this( context, null );
		initView( context, null, 0 );
    }

	/*
	 * === constractor ===
	 */ 
    public GamepadView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
		initView( context, attrs, 0 );
    }

	/*
	 * === constractor ===
	 */ 
    public GamepadView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        initView( context, attrs, defStyle );
	}

	/*
	 * initView
	 * @param Context context
	 * @param AttributeSet attrs
	 * @param int defStyle
	 */ 
    private void initView( Context context, AttributeSet attrs, int defStyle ) {
    	mMotionEventManager = new MotionEventManager();
    	mKeyEventManager = new KeyEventManager();
    	
        setFocusable(true);
        setFocusableInTouchMode(true);
        
    	getDisplaySize( context );

		float ratio = (float) mDisplayWidth / 480.0f;
     	
    	int joystick1x = (int)( 177.0 * ratio );
    	int joystick1y = (int)( 440.0 * ratio );
    	int joystick2x = (int)( 293.0 * ratio );
    	int joystick2y = joystick1y;
    	int joystick_c = (int)( 34.0 * ratio );
    	int joystick_p = (int)( 6.0 * ratio );

    	int joystick3x = (int)( 127.0 * ratio );
    	int joystick3y = (int)( 359.0 * ratio );
    	int joystick3c = (int)( 34.0 * ratio );
   	
		int button_x = (int)( 345.0 * ratio );
		int button_y = (int)( 359.0 * ratio );
		int button_c = (int)( 29.0 * ratio );
				
		mRadius = 12.0f * ratio;	
							
		m1X = button_x - button_c;
		m1Y = button_y;
		m2X = button_x;
		m2Y = button_y- button_c;
		m3X = button_x;
		m3Y = button_y+ button_c;				
		m4X = button_x + button_c;
		m4Y = button_y;

		m9X = joystick1x;
		m9Y = joystick1y;
		m10X = joystick2x;
		m10Y = joystick2y;
    			
		mUpX = joystick3x;
		mUpY = joystick3y - joystick3c;
		mDownX = joystick3x;
		mDownY = joystick3y + joystick3c;
		mLeftX = joystick3x - joystick3c;
		mLeftY = joystick3y;
		mRightX =  joystick3x + joystick3c;
		mRightY = joystick3y;
			
     	int btn5X1 = (int)( 120.0 * ratio ); 
     	int btn5Y1 = (int)( 110.0 * ratio );
     	int btn5X2 = btn5X1 + (int)( 50.0 * ratio );
     	int btn5Y2 = btn5Y1 + (int)( 30.0 * ratio );

     	int btn6X1 = (int)( 310.0 * ratio );
     	int btn6Y1 = btn5Y1;
     	int btn6X2 = btn6X1 + (int)( 50.0 * ratio ); 
     	int btn6Y2 = btn5Y2;

     	int btn7X1 = btn5X1; 
     	int btn7Y1 = (int)( 60.0 * ratio );
     	int btn7X2 = btn5X2;
     	int btn7Y2 = btn7Y1 + (int)( 40.0 * ratio );
     	
     	int btn8X1 = btn6X1; 
     	int btn8Y1 = btn7Y1;
     	int btn8X2 = btn6X2;
     	int btn8Y2 = btn7Y2;

     	int btn11X1 = (int)( 190.0 * ratio );
     	int btn11Y1 = (int)( 356.0 * ratio );
     	int btn11X2 = btn11X1 + (int)( 20.0 * ratio ); 
     	int btn11Y2 = btn11Y1 + (int)( 14.0 * ratio );

    	int btn12X1 = (int)( 260.0 * ratio ); 
     	int btn12Y1 = btn11Y1;
     	int btn12X2 = btn12X1 + (int)( 20.0 * ratio ); 
     	int btn12Y2 = btn11Y2;

    	initBitmap( context, mDisplayWidth, mDisplayHeight );
    	    	    	
		mJoystickView1 = new JoystickView();
		mJoystickView2 = new JoystickView();
		mJoystickView3 = new JoystickView();
		mJoystickView1.setParam( joystick1x, joystick1y, joystick_c, joystick_p );
		mJoystickView2.setParam( joystick2x, joystick2y, joystick_c, joystick_p );
		mJoystickView3.setParam( joystick3x, joystick3y, joystick3c, joystick_p );
		   	
     	mRect5 = new Rect( btn5X1, btn5Y1, btn5X2, btn5Y2 );    	
     	mRect6 = new Rect( btn6X1, btn6Y1, btn6X2, btn6Y2 );  
     	mRect7 = new Rect( btn7X1, btn7Y1, btn7X2, btn7Y2 );  
     	mRect8 = new Rect( btn8X1, btn8Y1, btn8X2, btn8Y2 );  
     	mRect11 = new Rect( btn11X1, btn11Y1, btn11X2, btn11Y2 );  
     	mRect12 = new Rect( btn12X1, btn12Y1, btn12X2, btn12Y2 );  
     	     	
		mPaintBitmap = new Paint();
		mPaintButton = new Paint();
		mPaintButton.setColor( Color.BLUE );

		if ( isDubug ) {		
			setAllButtons( true );	
		}
	}

	/*
	 * getDisplaySize
	 * @param Context context
	 */ 
    private void getDisplaySize( Context context ) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mDisplayWidth = size.x;
		mDisplayHeight = size.y;
	}

	/*
	 * initBitmap
	 * @param Context context
	 * @param int width
	 * @param int height	 
	 */ 		
    private void initBitmap( Context context, int width, int height ) {
		Bitmap bmp = BitmapFactory.decodeResource( 
			context.getResources(), R.drawable.gamepad ); 
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		float scale = Math.min( (float)width/w, (float)height/h ); 
		Matrix matrix = new Matrix();
		matrix.postScale( scale, scale );
		mBitmap = Bitmap.createBitmap( bmp, 0, 0, w, h, matrix, true );
	}

	/*
	 * === onGenericMotionEvent ===
	 */ 		
    @Override
    public boolean onGenericMotionEvent( MotionEvent event ) {
		boolean ret = mMotionEventManager.execGenericMotionEvent( event );
		if ( ret ) {
			setSparseAxis( mMotionEventManager.getSparseAxis() );
		} else {
			super.onGenericMotionEvent( event );
		}
		// always false
		// becuase NOT call execKeyEvent when the DPAD event handling joysitcjk 
		return false;
    }

	/*
	 * === onKeyDown ===
	 */ 
	@Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
		boolean ret = mKeyEventManager.execKeyDown( keyCode, event );
	    if ( ret ) {
			setSparseCode( mKeyEventManager.getSparseCode() );
			return true;
		}
		return super.onKeyDown( keyCode, event ); 
	}

	/*
	 * === onKeyUp ===
	 */ 
	@Override
    public boolean onKeyUp( int keyCode, KeyEvent event ) {
		boolean ret = mKeyEventManager.execKeyUp( keyCode, event );
	    if ( ret ) {
			setSparseCode( mKeyEventManager.getSparseCode() );
			return true;
		}
		return super.onKeyUp( keyCode, event );  
	}
	    
	/*
	 * === onDraw ===
	 */ 
    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

		canvas.drawBitmap( mBitmap, 0, 0, mPaintBitmap );

		if ( isPush1 ) {
			canvas.drawCircle( m1X, m1Y, mRadius, mPaintButton );
		}
		if ( isPush2 ) {
			canvas.drawCircle( m2X, m2Y, mRadius, mPaintButton );
		}
		if ( isPush3 ) {
			canvas.drawCircle( m3X, m3Y, mRadius, mPaintButton );
		}
		if ( isPush4 ) {
			canvas.drawCircle( m4X, m4Y, mRadius, mPaintButton );
		}
		if ( isPush5 ) {									 
    		canvas.drawRect( mRect5, mPaintButton );
    	}
    	if ( isPush6 ) {	
    		canvas.drawRect( mRect6, mPaintButton );
    	}
    	if ( isPush7 ) {	
    		canvas.drawRect( mRect7, mPaintButton );
    	}
    	if ( isPush8 ) {
    		canvas.drawRect( mRect8, mPaintButton );
	    }
		if ( isPush9 ) {
			canvas.drawCircle( m9X, m9Y, mRadius, mPaintButton );
		}
		if ( isPush10 ) {	
			canvas.drawCircle( m10X, m10Y, mRadius, mPaintButton );
		}
	    if ( isPush11 ) {	
    		canvas.drawRect( mRect11, mPaintButton );
    	}
	    if ( isPush12 ) {	
    		canvas.drawRect( mRect12, mPaintButton );
    	}
	    if ( isPushUp ) {	
			canvas.drawCircle( mUpX, mUpY, mRadius, mPaintButton );
    	}
	    if ( isPushDown ) {	
			canvas.drawCircle( mDownX, mDownY, mRadius, mPaintButton );
    	}
	    if ( isPushLeft ) {	
			canvas.drawCircle( mLeftX, mLeftY, mRadius, mPaintButton );
    	}
	    if ( isPushRight ) {	
			canvas.drawCircle( mRightX, mRightY, mRadius, mPaintButton );
    	}
    	    	    	     	    	
		mJoystickView1.draw( canvas );
		mJoystickView2.draw( canvas );
		mJoystickView3.draw( canvas );
    }

	/*
	 * setSparseAxis
	 * @param SparseArray<Float> sparse
	 */ 
    public void setSparseAxis( SparseArray<Float> sparse ) {
    	float x = sparse.get( MotionEvent.AXIS_X );
    	float y = sparse.get( MotionEvent.AXIS_Y );
    	float z = sparse.get( MotionEvent.AXIS_Z );
    	float rz = sparse.get( MotionEvent.AXIS_RZ );
    	float hat_x = sparse.get( MotionEvent.AXIS_HAT_X );
    	float hat_y = sparse.get( MotionEvent.AXIS_HAT_Y );
		mJoystickView1.setPos( x, y );
		mJoystickView2.setPos( rz, z );
		mJoystickView3.setPos( hat_x, hat_y );
		invalidate();
    }
  
	/*
	 * setSparseCode
	 * @param SparseBooleanArray sparse
	 */ 
    public void setSparseCode( SparseBooleanArray sparse ) {
		isPush1 = sparse.get( KeyEvent.KEYCODE_BUTTON_1 );
		isPush2 = sparse.get( KeyEvent.KEYCODE_BUTTON_2 );
		isPush3 = sparse.get( KeyEvent.KEYCODE_BUTTON_3 );
		isPush4 = sparse.get( KeyEvent.KEYCODE_BUTTON_4 );
		isPush5 = sparse.get( KeyEvent.KEYCODE_BUTTON_5 );
		isPush6 = sparse.get( KeyEvent.KEYCODE_BUTTON_6 );
		isPush7 = sparse.get( KeyEvent.KEYCODE_BUTTON_7 );
		isPush8 = sparse.get( KeyEvent.KEYCODE_BUTTON_8 );
		isPush9 = sparse.get( KeyEvent.KEYCODE_BUTTON_9 );
		isPush10 = sparse.get( KeyEvent.KEYCODE_BUTTON_10 );
		isPush11 = sparse.get( KeyEvent.KEYCODE_BUTTON_11 );
		isPush12 = sparse.get( KeyEvent.KEYCODE_BUTTON_12 );
		isPushUp = sparse.get( KeyEvent.KEYCODE_DPAD_UP );
		isPushDown = sparse.get( KeyEvent.KEYCODE_DPAD_DOWN );
		isPushLeft = sparse.get( KeyEvent.KEYCODE_DPAD_LEFT );
		isPushRight = sparse.get( KeyEvent.KEYCODE_DPAD_RIGHT );
		invalidate();																								
    }

	/*
	 * setAllButtons
	 * @param boolean b
	 */ 
    private void setAllButtons( boolean b ) {
		isPush1 = b;
		isPush2 = b;
		isPush3 = b;
		isPush4 = b;
		isPush5 = b;
		isPush6 = b;
		isPush7 = b;	
		isPush8 = b;
		isPush9 = b;
		isPush10 = b;
		isPush11 = b;
		isPush12 = b;
		isPushUp = b;
		isPushDown = b;
		isPushLeft = b;
		isPushRight = b;
    }
           
}
