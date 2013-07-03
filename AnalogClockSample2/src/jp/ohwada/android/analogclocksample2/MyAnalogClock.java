package jp.ohwada.android.analogclocksample2;

import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * This widget display an analogic clock with three hands 
 * for hours, minutes and seconds
 * base android.widget.AnalogClock
 */
public class MyAnalogClock extends View {
    
    private Time mTime;

    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;

    private boolean isAttached = false;
    
    private float mSecond;
    private float mMinutes;
    private float mHour;
    private boolean isChanged;

    private Runnable mTicker;
    
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
	private void initClock(  Context context, AttributeSet attrs, int defStyle ) {
		Resources r = context.getResources();
        TypedArray a = context.obtainStyledAttributes(
			attrs, R.styleable.AnalogClock, defStyle, 0 );
		mDial = a.getDrawable( R.styleable.AnalogClock_dial );
		mHourHand = a.getDrawable( R.styleable.AnalogClock_hand_hour );
		mMinuteHand = a.getDrawable( R.styleable.AnalogClock_hand_minute );
		mSecondHand = a.getDrawable( R.styleable.AnalogClock_hand_second );
        if (mDial == null) {
        	mDial = r.getDrawable( R.drawable.clock_dial );           
        }                
        if (mHourHand == null) {
        	mHourHand = r.getDrawable( R.drawable.clock_hand_hour );
        }          
        if (mMinuteHand == null) {
        	mMinuteHand = r.getDrawable( R.drawable.clock_hand_minute );
        }
        if (mSecondHand == null) {
        	mSecondHand = r.getDrawable( R.drawable.clock_hand_second );
		}
		mTime = new Time();
		mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
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
		 * base android.widget.DigitalClock
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
        int heightMode = MeasureSpec.getMode( heightMeasureSpec );
        int heightSize =  MeasureSpec.getSize( heightMeasureSpec );

        float hScale = 1.0f;
        float vScale = 1.0f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension( 
        	resolveSize( (int) (mDialWidth * scale), widthMeasureSpec ),
			resolveSize( (int) (mDialHeight * scale), heightMeasureSpec) );
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = isChanged;
        if (changed) {
            isChanged = false;
        }

        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();        
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min(
            	(float) availableWidth / (float) w,
				(float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

		// dial
        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

		// hour
        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

		// minute
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();

		// second
        canvas.save();
        canvas.rotate(mSecond / 60.0f * 360.0f, x, y);
        final Drawable secondHand = mSecondHand;
        if (changed) {
            w = secondHand.getIntrinsicWidth() / 2  ;
            h = secondHand.getIntrinsicHeight();
            secondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        secondHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

	/*
	 * onTimeChanged
	 */ 
    private void onTimeChanged() {
        mTime.setToNow();
        int hour = mTime.hour;
        int minute = mTime.minute;
        int second = mTime.second;
        mSecond = second;
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;        
        isChanged = true;
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
        public void onReceive( Context context, Intent intent ) {
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
