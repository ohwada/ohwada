// 2013-06-01
// setIsClearOnFocus()
//
// 2013-02-01
// DatePicker
// setOnDateChangedListener()
// setTextSize()
// setYearWidth()
// setMonthTwoDigit()
    
/*
 * Copyright (C) 2007 The Android Open Source Project
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

//package android.widget;
package jp.ohwada.android.datepickersample2;

import jp.ohwada.android.datepickersample2.R;
import jp.ohwada.android.datepickersample2.MyNumberPicker.OnChangedListener;

//import android.annotation.Widget;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;

// added
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

//import com.android.internal.R;
//import com.android.internal.widget.NumberPicker;
//import com.android.internal.widget.NumberPicker.OnChangedListener;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A view for selecting a month / year / day based on a calendar like layout.
 *
 * For a dialog using this view, see {@link android.app.DatePickerDialog}.
 */
//@Widget
//public class DatePicker extends FrameLayout {
public class MyDatePicker extends FrameLayout {

// 2013-02-01
	private final static boolean D = true;
	private final static String TAG = "MyDatePicker";

    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    
    /* UI Components */
//    private final NumberPicker mDayPicker;
//    private final NumberPicker mMonthPicker;
//    private final NumberPicker mYearPicker;
    private final MyNumberPicker mDayPicker;
    private final MyNumberPicker mMonthPicker;
    private final MyNumberPicker mYearPicker;
    
    /**
     * How we notify users the date has changed.
     */
    private OnDateChangedListener mOnDateChangedListener;
    
    private int mDay;
    private int mMonth;
    private int mYear;

    /**
     * The callback used to indicate the user changes the date.
     */
    public interface OnDateChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
//        void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth);
		void onDateChanged(MyDatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

//    public DatePicker(Context context) {
    public MyDatePicker(Context context) {
        this(context, null);
    }

//    public DatePicker(Context context, AttributeSet attrs) {    
    public MyDatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

//    public DatePicker(Context context, AttributeSet attrs, int defStyle) {
    public MyDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_picker, this, true);

//        mDayPicker = (NumberPicker) findViewById(R.id.day);
        mDayPicker = (MyNumberPicker) findViewById(R.id.datepicker_day);
        
//        mDayPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mDayPicker.setFormatter(MyNumberPicker.TWO_DIGIT_FORMATTER);
        
        mDayPicker.setSpeed(100);
        mDayPicker.setOnChangeListener(new OnChangedListener() {

//            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
            public void onChanged(MyNumberPicker picker, int oldVal, int newVal) {
            
                mDay = newVal;
                if (mOnDateChangedListener != null) {
//                    mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
                    mOnDateChangedListener.onDateChanged(MyDatePicker.this, mYear, mMonth, mDay);
                }
            }
        });
        
//        mMonthPicker = (NumberPicker) findViewById(R.id.month);
        mMonthPicker = (MyNumberPicker) findViewById(R.id.datepicker_month);
        
//        mMonthPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mMonthPicker.setFormatter(MyNumberPicker.TWO_DIGIT_FORMATTER);

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        mMonthPicker.setRange(1, 12, months);
        mMonthPicker.setSpeed(200);
        mMonthPicker.setOnChangeListener(new OnChangedListener() {

//            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
            public void onChanged(MyNumberPicker picker, int oldVal, int newVal) {
                            
                /* We display the month 1-12 but store it 0-11 so always
                 * subtract by one to ensure our internal state is always 0-11
                 */
                mMonth = newVal - 1;
                // Adjust max day of the month
                adjustMaxDay();
                if (mOnDateChangedListener != null) {
//                    mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
                    mOnDateChangedListener.onDateChanged(MyDatePicker.this, mYear, mMonth, mDay);
                }
                updateDaySpinner();
            }
        });
        
//        mYearPicker = (NumberPicker) findViewById(R.id.year);
        mYearPicker = (MyNumberPicker) findViewById(R.id.datepicker_year);
        
        mYearPicker.setSpeed(100);
        mYearPicker.setOnChangeListener(new OnChangedListener() {

//            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
            public void onChanged(MyNumberPicker picker, int oldVal, int newVal) {
            
                mYear = newVal;
                // Adjust max day for leap years if needed
                adjustMaxDay();
                if (mOnDateChangedListener != null) {
//                    mOnDateChangedListener.onDateChanged(DatePicker.this, mYear, mMonth, mDay);
                    mOnDateChangedListener.onDateChanged(MyDatePicker.this, mYear, mMonth, mDay);
                }
                updateDaySpinner();
            }
        });

// 2013-02-01        
        // attributes
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
//        int mStartYear = a.getInt(R.styleable.DatePicker_startYear, DEFAULT_START_YEAR);
//        int mEndYear = a.getInt(R.styleable.DatePicker_endYear, DEFAULT_END_YEAR);
//        mYearPicker.setRange(mStartYear, mEndYear);       
//        a.recycle();
		setAttributes( context, attrs, defStyle );
        
        // initialize to current date
        Calendar cal = Calendar.getInstance();
        init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        
        // re-order the number pickers to match the current date format
        reorderPickers(months);
        
        if (!isEnabled()) {
            setEnabled(false);
        }
    }

// 2013-02-01
    private void setAttributes( Context context, AttributeSet attrs, int defStyle ) {
		if ( attrs == null ) return;
		// same as DatePicker
		TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.MyDatePicker );
        int mStartYear = ta.getInt( R.styleable.MyDatePicker_datepicker_startYear, DEFAULT_START_YEAR );
        int mEndYear = ta.getInt( R.styleable.MyDatePicker_datepicker_endYear, DEFAULT_END_YEAR );
        mYearPicker.setRange( mStartYear, mEndYear );
        // added
		int yearWidth = ta.getLayoutDimension( R.styleable.MyDatePicker_datepicker_yearWidth, 0 );
		int monthWidth = ta.getLayoutDimension( R.styleable.MyDatePicker_datepicker_monthWidth, 0 );
		int dayWidth = ta.getLayoutDimension( R.styleable.MyDatePicker_datepicker_dayWidth, 0 );
		int textSize = ta.getDimensionPixelSize( R.styleable.MyDatePicker_datepicker_textSize, 0 );
		boolean isMonthTwoDigit = ta.getBoolean( R.styleable.MyDatePicker_datepicker_monthTwoDigit, false );
		boolean isClearOnFocus = ta.getBoolean( R.styleable.MyDatePicker_datepicker_isClearOnFocus, false );
		if ( textSize != 0 ) {
			setTextSize( TypedValue.COMPLEX_UNIT_PX, textSize );
		}
		if ( yearWidth != 0 ) {
			setYearWidth( TypedValue.COMPLEX_UNIT_PX, yearWidth );
		}
		if ( monthWidth != 0 ) {
			setMonthWidth( TypedValue.COMPLEX_UNIT_PX, monthWidth );
		}
		if ( dayWidth != 0 ) {
			setDayWidth( TypedValue.COMPLEX_UNIT_PX, dayWidth );
		}
		if ( isMonthTwoDigit ) {
			setMonthTwoDigit( isMonthTwoDigit );
		}
		if ( isClearOnFocus ) {
			setIsClearOnFocus( isClearOnFocus );
		}
		ta.recycle();
	}

// 2013-02-01
    public void setTextSize( int unit, float size ) {
    	log_d( "setTextSize " + unit + " " + size );
		mYearPicker.setTextSize( unit, size );
		mMonthPicker.setTextSize( unit, size );
		mDayPicker.setTextSize( unit, size );				
	}

// 2013-02-01
    public void setTextSize( float size ) {
    	setTextSize( TypedValue.COMPLEX_UNIT_SP, size );	
	}

// 2013-02-01
    public void setNumberWidth( float year, float month,  float day ) {
    	setYearWidth( TypedValue.COMPLEX_UNIT_DIP, year );
    	setMonthWidth( TypedValue.COMPLEX_UNIT_DIP, month );
		setDayWidth( TypedValue.COMPLEX_UNIT_DIP, day );
    }

// 2013-02-01
    public void setYearWidth( int unit, float size ) {
        log_d( "setYearWidth " + unit + " " + size );
		mYearPicker.setLayoutParams( 
			getLayoutParams( unit, size ) );	
	}

// 2013-02-01
    public void setMonthWidth( int unit, float size ) {
        log_d( "setMonthWidth " + unit + " " + size );
		mMonthPicker.setLayoutParams(
			getLayoutParams( unit, size ) );				
	}

// 2013-02-01
    public void setDayWidth( int unit, float size ) {
        log_d( "setDayWidth " + unit + " " + size );
		mDayPicker.setLayoutParams( 
			getLayoutParams( unit, size ) );	
	}

// 2013-02-01
    private LinearLayout.LayoutParams getLayoutParams( int unit, float size ) {
    	int width = (int) getPixcelSize( unit, size );
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
    		width, ViewGroup.LayoutParams.WRAP_CONTENT );
		return params;	
	}
	    
// 2013-02-01
    /**
     * Get the pixcel size from a given unit and value.  
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    private float getPixcelSize( int unit, float size ) {          
        return TypedValue.applyDimension( 
        		unit, size, getContext().getResources().getDisplayMetrics() );
    }

// 2013-06-01 K.OHWADA
	/**
	 * setIsClearOnFocus
	 * @param boolean is
	 */ 
	public void setIsClearOnFocus( boolean is ) {
		mYearPicker.setIsClearOnFocus( is );
		mMonthPicker.setIsClearOnFocus( is );
		mDayPicker.setIsClearOnFocus( is );
	}

    public void setMonthTwoDigit( boolean flag ) {
    	log_d( "setMonthTwoDigit " + flag );
    	DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
    	if ( flag ) {
    	    months = null;     
        }
       	mMonthPicker.setRange( 1, 12, months );
	}
	            
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDayPicker.setEnabled(enabled);
        mMonthPicker.setEnabled(enabled);
        mYearPicker.setEnabled(enabled);
    }

    private void reorderPickers(String[] months) {
        java.text.DateFormat format;
        String order;

        /*
         * If the user is in a locale where the medium date format is
         * still numeric (Japanese and Czech, for example), respect
         * the date format order setting.  Otherwise, use the order
         * that the locale says is appropriate for a spelled-out date.
         */

        if (months[0].startsWith("1")) {
            format = DateFormat.getDateFormat(getContext());
        } else {
            format = DateFormat.getMediumDateFormat(getContext());
        }

        if (format instanceof SimpleDateFormat) {
            order = ((SimpleDateFormat) format).toPattern();
        } else {
            // Shouldn't happen, but just in case.
            order = new String(DateFormat.getDateFormatOrder(getContext()));
        }

        /* Remove the 3 pickers from their parent and then add them back in the
         * required order.
         */
//        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
        LinearLayout parent = (LinearLayout) findViewById(R.id.datepicker_parent);

        parent.removeAllViews();

        boolean quoted = false;
        boolean didDay = false, didMonth = false, didYear = false;

        for (int i = 0; i < order.length(); i++) {
            char c = order.charAt(i);

            if (c == '\'') {
                quoted = !quoted;
            }

            if (!quoted) {
                if (c == DateFormat.DATE && !didDay) {
                    parent.addView(mDayPicker);
                    didDay = true;
                } else if ((c == DateFormat.MONTH || c == 'L') && !didMonth) {
                    parent.addView(mMonthPicker);
                    didMonth = true;
                } else if (c == DateFormat.YEAR && !didYear) {
                    parent.addView (mYearPicker);
                    didYear = true;
                }
            }
        }

        // Shouldn't happen, but just in case.
        if (!didMonth) {
            parent.addView(mMonthPicker);
        }
        if (!didDay) {
            parent.addView(mDayPicker);
        }
        if (!didYear) {
            parent.addView(mYearPicker);
        }
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateSpinners();
        reorderPickers(new DateFormatSymbols().getShortMonths());
    }

    private static class SavedState extends BaseSavedState {

        private final int mYear;
        private final int mMonth;
        private final int mDay;

        /**
         * Constructor called from {@link DatePicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int year, int month, int day) {
            super(superState);
            mYear = year;
            mMonth = month;
            mDay = day;
        }
        
        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mYear = in.readInt();
            mMonth = in.readInt();
            mDay = in.readInt();
        }

        public int getYear() {
            return mYear;
        }

        public int getMonth() {
            return mMonth;
        }

        public int getDay() {
            return mDay;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mYear);
            dest.writeInt(mMonth);
            dest.writeInt(mDay);
        }

// 2013-02-01
        @SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR =
                new Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    /**
     * Override so we are in complete control of save / restore for this widget.
     */
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        
        return new SavedState(superState, mYear, mMonth, mDay);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mYear = ss.getYear();
        mMonth = ss.getMonth();
        mDay = ss.getDay();
    }

    /**
     * Initialize the state.
     * @param year The initial year.
     * @param monthOfYear The initial month. starting from zero.
     * @param dayOfMonth The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth,
            OnDateChangedListener onDateChangedListener) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        mOnDateChangedListener = onDateChangedListener;
        updateSpinners();
    }

// 20130-02-01
    public void setDate( int year, int monthOfYear, int dayOfMonth ) {
    	log_d( year + " " + monthOfYear + " " + dayOfMonth );
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateSpinners();
    }
    
// 20130-02-01
    public void setOnDateChangedListener(
            OnDateChangedListener onDateChangedListener ) {
        mOnDateChangedListener = onDateChangedListener;
    }
    
    private void updateSpinners() {
        updateDaySpinner();
        mYearPicker.setCurrent(mYear);
        
        /* The month display uses 1-12 but our internal state stores it
         * 0-11 so add one when setting the display.
         */
        mMonthPicker.setCurrent(mMonth + 1);
    }

    private void updateDaySpinner() {
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth, mDay);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayPicker.setRange(1, max);
        mDayPicker.setCurrent(mDay);
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDayOfMonth() {
        return mDay;
    }

    private void adjustMaxDay(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (mDay > max) {
            mDay = max;
        }
    }
    
// 2013-02-01     
 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	} 
}
