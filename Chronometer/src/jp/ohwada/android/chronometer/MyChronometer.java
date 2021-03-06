/*
 * MyChronometer
 * base on android.widget.Chronometer
 * 2011-08-22 K.OHWADA
 */
 
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// K.OHWADA
//package android.widget;
package jp.ohwada.android.chronometer;

import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
//import android.text.format.DateUtils;
import android.util.AttributeSet;
//import android.view.RemotableViewMethod;
//import android.util.Log;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

//import java.util.Formatter;
//import java.util.IllegalFormatException;
//import java.util.Locale;	

/**
 * Class that implements a simple timer.
 * <p>
 * You can give it a start time in the {@link SystemClock#elapsedRealtime} timebase,
 * and it counts up from that, or if you don't give it a base time, it will use the
 * time at which you call {@link #start}.  By default it will display the current
 * timer value in the form "MM:SS" or "H:MM:SS", or you can use {@link #setFormat}
 * to format the timer value into an arbitrary string.
 *
 * @attr ref android.R.styleable#Chronometer_format
 */
@RemoteView
public class MyChronometer extends TextView {

// K.OHWADA
//	private static final String TAG = "Chronometer";
    private static final String TAG = "MyChronometer";
    
// K.OHWADA
    private static final int DELAY_TIME = 100;
    
    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnChronometerTickListener {

        /**
         * Notification that the chronometer has changed.
         */
        void onChronometerTick(MyChronometer chronometer);

    }

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
//    private boolean mLogged;
//    private String mFormat;
//    private Formatter mFormatter;
//    private Locale mFormatterLocale;
//    private Object[] mFormatterArgs = new Object[1];
//    private StringBuilder mFormatBuilder;
    private OnChronometerTickListener mOnChronometerTickListener;
//    private StringBuilder mRecycle = new StringBuilder(8);
    
    private static final int TICK_WHAT = 2;
    
    /**
     * Initialize this Chronometer object.
     * Sets the base to the current time.
     */
    public MyChronometer(Context context) {
        this(context, null, 0);
    }

    /**
     * Initialize with standard view layout information.
     * Sets the base to the current time.
     */
    public MyChronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Initialize with standard view layout information and style.
     * Sets the base to the current time.
     */
    public MyChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

// K.OHWADA
// platform/frameworks/base/core/res/res/values/attrs.xml
// <declare-styleable name="Chronometer">
// <!-- Format string: if specified, the Chronometer will display this
//      string, with the first "%s" replaced by the current timer value
//      in "MM:SS" or "H:MM:SS" form.
//      If no format string is specified, the Chronometer will simply display
//      "MM:SS" or "H:MM:SS". -->
// <attr name="format" format="string" localization="suggested" />
// </declare-styleable>
//       
//       TypedArray a = context.obtainStyledAttributes(
//                attrs,
//                com.android.internal.R.styleable.Chronometer, defStyle, 0);
//       setFormat(a.getString(com.android.internal.R.styleable.Chronometer_format));//
//       a.recycle();

        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }
    
    /**
     * Set the time that the count-up timer is in reference to.
     *
     * @param base Use the {@link SystemClock#elapsedRealtime} time base.
     */    
    @android.view.RemotableViewMethod
    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText( SystemClock.elapsedRealtime() );
    }

    /**
     * Return the base time as set through {@link #setBase}.
     */
    public long getBase() {
        return mBase;
    }

    /**
     * Sets the format string used for display.  The Chronometer will display
     * this string, with the first "%s" replaced by the current timer value in
     * "MM:SS" or "H:MM:SS" form.
     *
     * If the format string is null, or if you never call setFormat(), the
     * Chronometer will simply display the timer value in "MM:SS" or "H:MM:SS"
     * form.
     *
     * @param format the format string.
     */
     
// K.OHWADA  
//    @android.view.RemotableViewMethod
//    public void setFormat(String format) {
//        mFormat = format;
//        if (format != null && mFormatBuilder == null) {
//            mFormatBuilder = new StringBuilder(format.length() * 2);
//        }
//    }

    /**
     * Returns the current format string as set through {@link #setFormat}.
     */
     
// K.OHWADA  
//    public String getFormat() {
//        return mFormat;
//    }

    /**
     * Sets the listener to be called when the chronometer changes.
     * 
     * @param listener The listener.
     */
    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    /**
     * @return The listener (may be null) that is listening for chronometer change
     *         events.
     */
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    /**
     * Start counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     * 
     * Chronometer works by regularly scheduling messages to the handler, even when the 
     * Widget is not visible.  To make sure resource leaks do not occur, the user should 
     * make sure that each start() call has a reciprocal call to {@link #stop}. 
     */
    public void start() {
        mStarted = true;
        updateRunning();
    }

    /**
     * Stop counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     * 
     * This stops the messages to the handler, effectively releasing resources that would
     * be held as the chronometer is running, via {@link #start}. 
     */
    public void stop() {
        mStarted = false;
        updateRunning();
    }

    /**
     * The same as calling {@link #start} or {@link #stop}.
     * @hide pending API council approval
     */  
    @android.view.RemotableViewMethod
    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

// K.OHWADA  
//    private synchronized void updateText(long now) {
//       long seconds = now - mBase;
//        seconds /= 1000;
//        String text = DateUtils.formatElapsedTime(mRecycle, seconds);
//        if (mFormat != null) {
//            Locale loc = Locale.getDefault();
//            if (mFormatter == null || !loc.equals(mFormatterLocale)) {
//                mFormatterLocale = loc;
//                mFormatter = new Formatter(mFormatBuilder, loc);
//           }
//            mFormatBuilder.setLength(0);
//            mFormatterArgs[0] = text;
//            try {
//                mFormatter.format(mFormat, mFormatterArgs);
//                text = mFormatBuilder.toString();
//            } catch (IllegalFormatException ex) {
//                if (!mLogged) {
//                    Log.w(TAG, "Illegal format string: " + mFormat);
//                    mLogged = true;
//                }
//            }
//        }
//        setText(text);
//    }

// K.OHWADA  
    private synchronized void updateText(long now) {
        setText( formatText(now) );
    }
    
// K.OHWADA    
    private String formatText(long now) {
    	int msec = (int)(now - mBase);      
        int min = msec / (60*1000); 
        int msec_r = msec - min * (60*1000);
        int sec = msec_r / 1000;
        msec_r = msec_r - sec * 1000;
        int msec_v = msec_r / DELAY_TIME ;          
        String text = "";
      
        // add min when over 60 sec 
        if (min > 0) {
    	    text = min + ":";
        }
 
        // add 0 when sec is less then 10 
        if (sec < 10) {
    	    text += "0";
        }
      
	    // 12;34.5
	    text += sec + "." + msec_v;
        return text;
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();

// K.OHWADA
//                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), DELAY_TIME );
                
             } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();

// K.OHWADA
//                sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), DELAY_TIME);

            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
}
