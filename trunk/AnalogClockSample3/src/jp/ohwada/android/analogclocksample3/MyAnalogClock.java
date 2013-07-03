package jp.ohwada.android.analogclocksample3;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

/**
 * MyAnalogClock
 */
public class MyAnalogClock extends View {

    private final static int DEFAULT_WIDTH = 200;
    private final static int VIEW_MARGIN = 10;
    private final static float DEG_DIV = 360.0f / 60.0f ;

    private final static int RECT_BIG_LONG = 30;
    private final static int RECT_BIG_SHORT = 10;
    private final static int RECT_SMALL_LONG = 20;
    private final static int RECT_SMALL_SHORT = 8;
    private final static int RECT_LONG_DEFF = 5;
    private final static int TEXT_SIZE = 80;
    
    private final static int COLOR_BIG = Color.BLUE; 
    private final static int COLOR_SMALL = Color.GRAY; 
    private final static int COLOR_SECOND = Color.RED; 
    private final static int COLOR_TEXT = Color.BLACK; 
        		
    private final static int RECT_BIG_SHORT_HALF = RECT_BIG_SHORT / 2 ;    
    private final static int RECT_SMALL_SHORT_HALF = RECT_SMALL_SHORT / 2 ;

    private final static String FORMAT_12H = "h:mm:ss aa";
    private final static String FORMAT_24H = "k:mm:ss";
        	
    private Time mTime = new Time();
    
    private Runnable mTicker;
    private boolean isAttached;

    private Calendar mCalendar;    

    private int mSecond;
    private String mClockText;
    private boolean isChanged;

    private Rect mRectBig;
    private Rect mRectSmall;
    
    private Paint mPaintSmall;
    private Paint mPaintBig;
    private Paint mPaintSecond;
    private Paint mPaintText;

	private float mFontHegiht;
	
    private String mFormat;
            
	/*
	 * === constractor ===
	 */         
    public MyAnalogClock( Context context ) {
        this( context, null );
    }

	/*
	 * === constractor ===
	 */ 
    public MyAnalogClock( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

	/*
	 * === constractor ===
	 */ 
    public MyAnalogClock( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        initClock( context, attrs, defStyle );
	}

	/*
	 * initClock
	 * @param Context context
	 * @param AttributeSet attrs
	 * @param int defStyle
	 */ 
	private void initClock( Context context, AttributeSet attrs, int defStyle ) {
		if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

		mPaintSmall = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintSmall.setColor( COLOR_SMALL ); 
		mPaintBig = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintBig.setColor( COLOR_BIG );   
		mPaintSecond = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintSecond.setColor( COLOR_SECOND ); 
        mPaintText = new Paint( Paint.ANTI_ALIAS_FLAG );

        mPaintText.setTextSize( TEXT_SIZE );
        mPaintText.setColor( COLOR_TEXT );
		FontMetrics fm = mPaintText.getFontMetrics();
		mFontHegiht = fm.ascent + fm.descent;
		
		setFormat(); 
    }

	/*
	 * === onAttachedToWindow ===
	 */ 
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if ( !isAttached ) {
            isAttached = true;
            IntentFilter filter = new IntentFilter();
            // The current time has changed. Sent every minute.
            filter.addAction( Intent.ACTION_TIME_TICK );
            // The current time has changed
            filter.addAction( Intent.ACTION_TIME_CHANGED );
            // The timezone has changed
            filter.addAction( Intent.ACTION_TIMEZONE_CHANGED );
            getContext().registerReceiver( mIntentReceiver, filter, null, mHandler );
        }
 
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
                public void run() {
                    if ( !isAttached ) return;
        			onTimeChanged();
					invalidate();
					runNextTicker();
                }
            };
        mTicker.run();

        // The time zone may have changed while the receiver wasn't registered, so update the Time
		mTime = new Time();

        // Make sure we update to the current time
        onTimeChanged();
    }

	/*
	 * runNextTicker
	 */
	private void runNextTicker() {
		long next = SystemClock.uptimeMillis() + 1000;
		mHandler.postAtTime( mTicker, next );
	}

	/*
	 * === onDetachedFromWindow ===
	 */ 
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
		if ( isAttached ) {
            getContext().unregisterReceiver( mIntentReceiver );
            isAttached = false;
        }
    }

	/*
	 * === onMeasure ===
	 */ 
    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int widthMode = MeasureSpec.getMode( widthMeasureSpec );
        int widthSize =  MeasureSpec.getSize( widthMeasureSpec );

        int size = DEFAULT_WIDTH;
        if ( widthMode != MeasureSpec.UNSPECIFIED && widthSize <  DEFAULT_WIDTH ) {
            size = widthSize;
        }
		int width = resolveSize( size, widthMeasureSpec );
        setMeasuredDimension( width, width );

    }

	/*
	 * === onSizeChanged ===
	 */ 
    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
		super.onSizeChanged( w, h, oldw, oldh );
		isChanged = true;
    }

	/*
	 * === onDraw ===
	 */ 
    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw(canvas);

		boolean changed = isChanged;
		if (changed) {
			isChanged = false;
		}

        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();        
        int center_x = availableWidth / 2;
        int center_y = availableHeight / 2;

		// dial
        if (( mRectBig == null )||( mRectSmall == null )) {
        	int big_left = center_x - RECT_BIG_SHORT_HALF;
        	int big_right = center_x + RECT_BIG_SHORT_HALF;
        	int big_top = VIEW_MARGIN;	
        	int big_bottom = big_top + RECT_BIG_LONG;	 
        	int small_left =  center_x - RECT_SMALL_SHORT_HALF;
        	int small_right =  center_x + RECT_SMALL_SHORT_HALF;
        	int small_top = big_top + RECT_LONG_DEFF;	
        	int small_bottom = small_top + RECT_SMALL_LONG ;	

        	mRectBig = new Rect( big_left, big_top, big_right, big_bottom );
        	mRectSmall = new Rect( small_left, small_top, small_right, small_bottom );
        }

		// second
        int second = mSecond;
		for ( int i=0; i<60; i++ ) {
			float degrees = (float) i * DEG_DIV;
			Rect rect = mRectSmall;
    		Paint paint = mPaintSmall ;
			if (( i % 5 ) == 0 ) {
				rect = mRectBig;
			 	paint = mPaintBig ;	
			}
			if ( i == second ) {
			 	paint = mPaintSecond ;	
			}
			canvas.save();
			canvas.rotate( degrees, center_x, center_y );				
			canvas.drawRect( rect, paint );
			canvas.restore();
		}		

		// digital clock
		String clock_text = mClockText;
		float text_width = mPaintText.measureText( clock_text );
		float text_x = center_x - text_width / 2;
		float text_y = center_y - mFontHegiht / 2;
		canvas.drawText( clock_text, text_x, text_y, mPaintText ) ;

    }

	/*
	 * onTimeChanged
	 */ 
    private void onTimeChanged() {
        mTime.setToNow();
		mSecond = mTime.second;             
		mCalendar.setTimeInMillis( System.currentTimeMillis() );
		mClockText = (String) DateFormat.format( mFormat, mCalendar );
		isChanged = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat( getContext() );
    }

	/**
	 * setFormat
	 */
    private void setFormat() {
        if (get24HourMode()) {
            mFormat = FORMAT_24H;
        } else {
            mFormat = FORMAT_12H;
        }
    }

	/**
	 * Handler
	 */    
	private final Handler mHandler = new Handler();

	/**
	 * BroadcastReceiver
	 */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mTime = new Time( TimeZone.getTimeZone(tz).getID() );
            }
			onTimeChanged();
			invalidate();
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
        		mHandler.removeCallbacks( mTicker );
            	runNextTicker();
            }
        }
    };        
}
