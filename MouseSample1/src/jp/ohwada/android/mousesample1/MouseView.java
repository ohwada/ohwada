package jp.ohwada.android.mousesample1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

/**
 * MouseView
 */
public class MouseView extends View {

	private static final boolean D = true;
    private static final String TAG = "MouseView"; 
    
    private static final int[] COLORS = {
    	Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.GRAY, Color.CYAN, };    
    private static final int TEXT_SIZE = 16;
    private static final  int POINT_SIZE = 10;
	
    private Paint mPaintText;
    private Paint mPaintLine;
    private Paint mPaintPoint;

	private Bitmap mBitmap = null;
	private Canvas mCanvas = null;	

    private int mX = 0;
    private int mY = 0;
    
    private int mTextX = 0;
    private int mTextXY = 0;
    private int mTextYY = 0;
    private int mTextVscrollY = 0;
    private int mTextPrimaryY = 0;
    private int mTextSecondaryY = 0;
    private int mTextTertiaryY = 0;        
    private float mPointRadius = 0;

	private int mColorIndex = 0;

 	private boolean isDrawLine = false;
 	private boolean isDrawPoint = false;
 	private boolean isFirst = true;
			
 	private boolean isPrimary = false;
 	private boolean isSecondary = false;
 	private boolean isTertiary = false;

	private float mPrevX = 0f;
	private float mPrevY = 0f;
     		
    private String mStringX = "";
    private String mStringY = "";
    private String mStringVscroll = "";
    private String mStringPrimary = "";
    private String mStringSecondary = "";
    private String mStringTertiary = "";
        
	/*
	 * === constractor ===
	 */         
    public MouseView( Context context  ) {	
        this( context, null );	
		initView( context, null, 0 );
    }
    
	/*
	 * === constractor ===
	 */ 
    public MouseView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
		initView( context, attrs, 0 );
    }

	/*
	 * === constractor ===
	 */ 
    public MouseView( Context context, AttributeSet attrs, int defStyle ) {
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

    	float size = getPixcelSize( TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE );
    	int line = (int)( 1.5 * size );
		mTextX = (int)( 0.5 * size );
		mTextXY = line;
		mTextYY = mTextXY + line;
		mTextVscrollY = mTextYY + line;
		mTextPrimaryY = mTextVscrollY + line;
		mTextSecondaryY = mTextPrimaryY + line;
		mTextTertiaryY = mTextSecondaryY + line;

    	mPointRadius = getPixcelSize( TypedValue.COMPLEX_UNIT_DIP, POINT_SIZE );
								
		mPaintText = new Paint();
		mPaintText.setAntiAlias( true ); 
		mPaintText.setTextSize( size );
		mPaintText.setColor( Color.BLACK );	

		mPaintLine = new Paint();
		mPaintPoint = new Paint();
		setColor();
										
		setTextString( 0, 0, 0 ,false, false, false );	
    }

    /**
     * Get the pixcel size from a given unit and value.  
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    private float getPixcelSize( int unit, float size ) {        
        return TypedValue.applyDimension( 
			unit, size, getContext().getResources().getDisplayMetrics() );
    }

	/*
	 * === onSizeChanged ===
	 */ 
    @Override
	protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
		super.onSizeChanged( w, h, oldw, oldh );
		mBitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
		mCanvas = new Canvas( mBitmap );
	}

	/*
	 * === onTouchEvent ===
	 */ 
	@Override
	public boolean onTouchEvent( MotionEvent event ) {
		log_d( "onTouchEvent" );
		log_d( event.toString() );
		return setValues( event );
	}

	/*
	 * === onHoverEvent ===
	 */
	@Override
	public boolean onHoverEvent( MotionEvent event ) {
		log_d( "onHoverEvent" );
		log_d( event.toString() );
		return setValues( event );
	}

	/*
	 * === onGenericMotionEvent ===
	 */
	@Override
	public boolean onGenericMotionEvent( MotionEvent event ) {
		log_d( "onGenericMotion" );
		log_d( event.toString() );
		return setValues( event );
	}
        
	/*
	 * === onDraw ===
	 */ 
    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

    	if ( mBitmap != null ) {
			canvas.drawBitmap( mBitmap, 0, 0, null );
		}
		
    	canvas.drawText( mStringX, mTextX, mTextXY, mPaintText );
   		canvas.drawText( mStringY, mTextX, mTextYY, mPaintText );
    	canvas.drawText( mStringVscroll, mTextX, mTextVscrollY, mPaintText );
    	canvas.drawText( mStringPrimary, mTextX, mTextPrimaryY, mPaintText );
    	canvas.drawText( mStringSecondary, mTextX, mTextSecondaryY, mPaintText );
    	canvas.drawText( mStringTertiary, mTextX, mTextTertiaryY, mPaintText );

		if ( isDrawPoint ) {
			isDrawPoint = false;
			canvas.drawCircle( mX, mY, mPointRadius, mPaintPoint );
		}		
    }

	/**
	 * setValues
	 * @param MotionEvent event
	 */	
	private boolean setValues( MotionEvent event ) {
		if ( !isMouse( event ) ) return false;
		boolean ret = false;
		float x = event.getX();
		float y = event.getY();
		float vscroll = 0;
		switch ( event.getAction() ) {
			// onTouchEvent
			case MotionEvent.ACTION_MOVE:
			// onHoverEvent
			case MotionEvent.ACTION_HOVER_ENTER:
			case MotionEvent.ACTION_HOVER_MOVE:
			case MotionEvent.ACTION_HOVER_EXIT:
				ret = true;
				break;
			// onTouchEvent
			case MotionEvent.ACTION_DOWN:
				switch ( event.getButtonState() ) {
					case MotionEvent.BUTTON_PRIMARY:
						ret = true;
						isPrimary = true;
						break;						
					case MotionEvent.BUTTON_SECONDARY:
						ret = true;
						isSecondary = true;
						break;	
					case MotionEvent.BUTTON_TERTIARY:
						ret = true;
						isTertiary = true;
						break;	
				}
				break;
			// onTouchEvent
			case MotionEvent.ACTION_UP:
				ret = true;
				isPrimary = false;
				isSecondary = false;						
				isTertiary = false;
				break;
			// onGenericMotionEvent			
			case MotionEvent.ACTION_SCROLL:
				ret = true;
				vscroll = event.getAxisValue( MotionEvent.AXIS_VSCROLL );
				break;
		}
		if ( ret ) {
			setValues( x, y, vscroll, isPrimary, isSecondary, isTertiary );
		}
		return ret;	
	}

	/*
	 * setValues
	 * @param float x
	 * @param float y
	 * @param float vscroll
	 * @param boolean primary
	 * @param boolean secondary
	 * @param boolean tertiary
	 */ 
    private void setValues( float x, float y, float vscroll, boolean primary, boolean secondary, boolean tertiary ) {
    
        setTextString( x, y, vscroll, primary, secondary, tertiary );

        mX = (int)x;
        mY = (int)y;
			        
		if ( primary ) {
			isDrawLine = true;
			if ( isFirst ) {
				isFirst = false;
				startLine( x, y );
			}
		} else {
			isDrawLine = false;
			isFirst = true;
		}

		if ( secondary ) {
			clearLine();
		}

		if ( isDrawLine ) {
			drawLine( x, y );
		}

		boolean flag = false;
		if ( !isDrawPoint ) {
			if ( vscroll > 0.9 ) {
				flag = true;
				mColorIndex ++;
				if ( mColorIndex >=  COLORS.length ) {
					mColorIndex = 0;
				}
			} else if ( vscroll < -0.9 ) {
				flag = true;
				mColorIndex --;
				if ( mColorIndex < 0 ) {
					mColorIndex = COLORS.length - 1;
				}
			}
			if ( flag ) {
				isDrawPoint = true;
				setColor();
			}
		}
		
		invalidate();
    }

    /** 
	 * clearLine
	 */	
	 private void clearLine() {
		mCanvas.drawColor( Color.WHITE );
 	}

    /** 
	 * startLine
	 * @param int x
	 * @param int y	 
	 */	
	 private void startLine( float x, float y ) {
	 	clearLine();
		mPrevX = x;
 		mPrevY = y;
 	}

    /** 
	 * drawLine
	 * @param int x
	 * @param int y	 
	 */	
	 private void drawLine( float x, float y ) {
		mCanvas.drawLine( mPrevX, mPrevY, x, y, mPaintLine );
		mPrevX = x;
		mPrevY = y;
	}
	 				 										
    /** 
	 * setTextString
	 * @param int x
	 * @param int y	 
	 */	
    private void setTextString( float x, float y, float vscroll, boolean primary, boolean secondary, boolean tertiary  ) { 
    	mStringX = "X " + x;
    	mStringY = "Y " + y ;
    	mStringVscroll = "VScroll " + vscroll;
    	mStringPrimary = "Button Primary " + primary;
    	mStringSecondary = "Button Secondary " + secondary;
    	mStringTertiary = "Button Tertiary " + tertiary;
    }

    /** 
	 * setColor
	 */    
    private void setColor() {
		int color = COLORS[ mColorIndex ]; 
		mPaintLine.setColor( color );
		mPaintPoint.setColor( color );
	}

	/**
	 * isMouse
	 * @param MotionEvent event
	 * @return boolean
	 */	
	private boolean isMouse( MotionEvent event ) {
		return isSource( event.getSource(), InputDevice.SOURCE_MOUSE );
  }

	/**
	 * isSource
	 * @param int source
	 * @param int kind
	 * @return boolean
	 */	
	private boolean isSource( int source, int kind ) {
        return ( source & kind & ~InputDevice.SOURCE_CLASS_MASK ) != 0;
    }
  	
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}
}
