package jp.ohwada.android.timeedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TimeEditText
 * A view for selecting the time of day.
 * when you enter 123456, it will interpreted as 12:34:56. 
 * when you enter 010203, it will interpreted as 01:02:03. 
 * 
 * base on android.widget.TimePicker
 */
public class TimeEditText extends FrameLayout {

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {
        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute The current minute.
         * @param second The current second.
         */
        void onTimeChanged( TimeEditText view, int hourOfDay, int minute, int second );
    }

    /**
     * The callback interface used to indicate the time has been entered hour, time, and second.
     */
    public interface OnFinishedListener {
        /**
         * @param view The view associated with this listener.
         * @param boolean hasFocusSecond.
         */
        void onFinished( TimeEditText view, boolean hasFocusSecond );
    }

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
     */
    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        public void onTimeChanged( TimeEditText view, int hourOfDay, int minute, int second ) {
        	// dummy
        }
    };    	

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
     */
    private static final OnFinishedListener NO_OP_FINISH_LISTENER = new OnFinishedListener() {
        public void onFinished( TimeEditText view, boolean hasFocusSecond ) {
        	// dummy
        }
    }; 

    // state
    private int mCurrentHour = 0; // 0-23
    private int mCurrentMinute = 0; // 0-59
    private int mCurrentSecond = 0; // 0-59
    
    // ui components
    private final NumberEditText mHourEditText;
    private final NumberEditText mMinuteEditText;
    private final NumberEditText mSecondEditText;

    private final TextView mHourMarkText;	
    private final TextView mMinuteMarkText;	
    private final TextView mSecondMarkText;	

    // callbacks
    private OnTimeChangedListener mOnTimeChangedListener;
    private OnFinishedListener mFinishedListener;  

	/*
	 * === constractor ===
	 */     
    public TimeEditText( Context context ) {
        this( context, null );
    }

	/*
	 * === constractor ===
	 */   
    public TimeEditText( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

	/*
	 * === constractor ===
	 */   
    public TimeEditText( Context context, AttributeSet attrs, int defStyle ) {    
        super( context, attrs, defStyle );

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_edit_text,
            this, // we are the parent
            true);

        mHourMarkText = (TextView) findViewById( R.id.TextView_hour_mark );   
        mMinuteMarkText = (TextView) findViewById( R.id.TextView_minute_mark );   
        mSecondMarkText = (TextView) findViewById( R.id.TextView_second_mark );   
		mSecondMarkText.setVisibility( View.GONE );
                
        // hour
        mHourEditText = (NumberEditText) findViewById( R.id.TimeEditText_hour );
        mHourEditText.setOnChangeListener( new NumberEditText.OnChangedListener() {
            public void onChanged(NumberEditText spinner, int oldVal, int newVal) {            
                mCurrentHour = newVal;                
                onTimeChanged();
            }
        });

        // digits of minute
        mMinuteEditText = (NumberEditText) findViewById( R.id.TimeEditText_minute );        
        mMinuteEditText.setRange(0, 59);
        mMinuteEditText.setFormatter(NumberEditText.TWO_DIGIT_FORMATTER);
        mMinuteEditText.setOnChangeListener(new NumberEditText.OnChangedListener() {
            public void onChanged(NumberEditText spinner, int oldVal, int newVal) {            
                mCurrentMinute = newVal;
                onTimeChanged();
            }
        });

        // digits of second
        mSecondEditText = (NumberEditText) findViewById( R.id.TimeEditText_second );        
        mSecondEditText.setRange(0, 59);
        mSecondEditText.setFormatter(NumberEditText.TWO_DIGIT_FORMATTER);
        mSecondEditText.setOnChangeListener(new NumberEditText.OnChangedListener() {
            public void onChanged(NumberEditText spinner, int oldVal, int newVal) {            
                mCurrentSecond = newVal;
                onTimeChanged();
            }
        });
        
        // now that the hour/minute picker objects have been initialized, set
        // the hour range properly based on the 12/24 hour display mode.
        configurePickerRanges();

        // initialize to current time
        setOnTimeChangedListener( NO_OP_CHANGE_LISTENER );
        setOnFinishedListener( NO_OP_FINISH_LISTENER );

		setAttributes( context, attrs, defStyle );
        
        if (!isEnabled()) {
            setEnabled(false);
        }
    }

	/**
	 * setAttributes
	 * @param Context context
	 * @param AttributeSet attrs
	 * @param int defStyle
	 */ 
    private void setAttributes( Context context, AttributeSet attrs, int defStyle ) {
		if ( attrs == null ) return;
		TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.TimeEditText );
		int numberWidth = ta.getLayoutDimension( R.styleable.TimeEditText_timeedittext_numberWidth, 0 );
		int textSize = ta.getDimensionPixelSize( R.styleable.TimeEditText_timeedittext_textSize, 0 );
		String hourMark = ta.getString( R.styleable.TimeEditText_timeedittext_hourMark );
		String minuteMark = ta.getString( R.styleable.TimeEditText_timeedittext_minuteMark );
		String secondMark = ta.getString( R.styleable.TimeEditText_timeedittext_secondMark );
		if ( textSize != 0 ) {
			setTextSize( TypedValue.COMPLEX_UNIT_PX, textSize );
		}
		if ( numberWidth != 0 ) {
			setNumberWidth( TypedValue.COMPLEX_UNIT_PX, numberWidth );
		}
		if (( hourMark != null )&&( hourMark.length() > 0 )) {
			setHourMark( hourMark );
		}
		if (( minuteMark != null )&&( minuteMark.length() > 0 )) {
			setMinuteMark( minuteMark );
		}
		if (( secondMark != null )&&( secondMark.length() > 0 )) {
			setSecondMark( secondMark );
		}
		ta.recycle();
	}

	/**
	 * setTextSize
	 * @param int unit
	 * @param float size
	 */  
    public void setTextSize( int unit, float size ) {
		mHourEditText.setTextSize( unit, size );
		mMinuteEditText.setTextSize( unit, size );
		mSecondEditText.setTextSize( unit, size );				
		mHourMarkText.setTextSize( unit, size );
		mMinuteMarkText.setTextSize( unit, size );
		mSecondMarkText.setTextSize( unit, size );	
	}

	/**
	 * setTextSize
	 * @param float size
	 */  
    public void setTextSize( float size ) {
    	setTextSize( TypedValue.COMPLEX_UNIT_SP, size );	
	}

	/**
	 * setNumberWidth
	 * @param int unit
	 * @param float size
	 */  
    public void setNumberWidth( int unit, float size ) {
    	int width = (int) getPixcelSize( unit, size );
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
    		width, ViewGroup.LayoutParams.WRAP_CONTENT );
		mHourEditText.setLayoutParams( params );
		mMinuteEditText.setLayoutParams( params );
		mSecondEditText.setLayoutParams( params );;			
	}

	/**
	 * setNumberWidth
	 * @param float size
	 */  
    public void setNumberWidth( float size ) {
    	setNumberWidth( TypedValue.COMPLEX_UNIT_DIP, size );
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
 
	/**
	 * setHourMark
	 * @param String mark
	 */   
    public void setHourMark( String mark ) {
    	mHourMarkText.setText( mark );	
	}

	/**
	 * setMinuteMark
	 * @param String mark
	 */   
    public void setMinuteMark( String mark ) {
    	mMinuteMarkText.setText( mark );	
	}

	/**
	 * setSecondMark
	 * @param String mark
	 */   
    public void setSecondMark( String mark ) {
    	mSecondMarkText.setText( mark );	
		mSecondMarkText.setVisibility( View.VISIBLE );
	}

	/**
	 * setEnabled
	 * @param boolean enabled
	 */    
    @Override
    public void setEnabled( boolean enabled ) {
        super.setEnabled(enabled);
        mHourEditText.setEnabled(enabled);
        mMinuteEditText.setEnabled(enabled);
        mSecondEditText.setEnabled(enabled);
    }

	/**
	 * isClearOnFocus
	 * @param boolean is
	 */ 
	public void isClearOnFocus( boolean is ) {
		mHourEditText.isClearOnFocus( is );
		mMinuteEditText.isClearOnFocus( is );
		mSecondEditText.isClearOnFocus( is );
	}

	/**
	 * setNextFocus
	 */ 	
	public void setNextFocus() {
		// move focus to Minute if Hour is inputted 2 digits
		mHourEditText.setFinishedLength( 2 );
		mHourEditText.setOnFinishedListener( new NumberEditText.OnFinishedListener() {
            public void onFinished( NumberEditText spinner, int value ) { 
            	requestFocusMinute();             
            }
        });  

		// move focus to Second if Minute is inputted 2 digits
		mMinuteEditText.setFinishedLength( 2 );
		mMinuteEditText.setOnFinishedListener( new NumberEditText.OnFinishedListener() {
            public void onFinished( NumberEditText spinner, int value ) {            
            	requestFocusSecond();               
            }
        });

		// move focus to Second if Minute is inputted 2 digits
		mSecondEditText.setFinishedLength( 2 );
		mSecondEditText.setOnFinishedListener( new NumberEditText.OnFinishedListener() {
            public void onFinished( NumberEditText spinner, int value ) {            
                notifyFinish();           
            }
        });
	}

    /**
	 * requestFocusMinute
     */
	private void requestFocusMinute() {
		if ( mHourEditText.hasFocus() ) {          
			mMinuteEditText.requestFocus();
		}   
    }

    /**
	 * requestFocusSecond
     */
	private void requestFocusSecond() {
		if ( mMinuteEditText.hasFocus() ) {   
			mSecondEditText.requestFocus(); 
		}
    }
	            		
    /**
	 * class SavedState
     * Used to save / restore state of TimeEditText
     */
    private static class SavedState extends BaseSavedState {

        private final int mHour;
        private final int mMinute;
        private final int mSecond;
        
        private SavedState( Parcelable superState, int hour, int minute, int second ) {
            super(superState);
            mHour = hour;
            mMinute = minute;
            mSecond = second;
        }
        
        private SavedState(Parcel in) {
            super(in);
            mHour = in.readInt();
            mMinute = in.readInt();
            mSecond = in.readInt();
        }

        public int getHour() {
            return mHour;
        }

        public int getMinute() {
            return mMinute;
        }

        public int getSecond() {
            return mSecond;
        }
        
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
            dest.writeInt(mSecond);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
	 * === onSaveInstanceState ===
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState( superState, mCurrentHour, mCurrentMinute, mCurrentSecond );
    }

    /**
	 * === onRestoreInstanceState ===
     */
    @Override
    protected void onRestoreInstanceState( Parcelable state ) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState( ss.getSuperState() );
        setCurrentHour( ss.getHour() );
        setCurrentMinute( ss.getMinute() );
        setCurrentSecond( ss.getSecond() );
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     * @param onTimeChangedListener the callback, should not be null.
     */
    public void setOnTimeChangedListener( OnTimeChangedListener onTimeChangedListener ) {
        mOnTimeChangedListener = onTimeChangedListener;
    }

    /**
     * Set the callback that indicates the time has been entered hour, time, and second by the user.
     * @param OnFinishedListener the callback, should not be null.
     */
    public void setOnFinishedListener( OnFinishedListener listener ) {
        mFinishedListener = listener;
    }

    /**
     * Set the current time.
     * @param int hour
     * @param int minute,
	 * @param int second
     */
    public void setCurrentTime( int hour, int minute, int second ) {
    	setCurrentHour( hour );
    	setCurrentMinute( minute );
		setCurrentSecond( second );    
    }
    
    /**
	 * Get the current hour.
     * @return The current hour (0-23).
     */
    public Integer getCurrentHour() {
        return mCurrentHour;
    }

    /**
     * Set the current hour.
     * @param Integer currentHour
     */
    public void setCurrentHour( Integer currentHour ) {
        this.mCurrentHour = currentHour;
        updateHourDisplay();
    }
    
    /**
	 * Get the current minute.
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
        return mCurrentMinute;
    }

    /**
     * Set the current minute (0-59).
     * @param Integer currentMinute
     */
    public void setCurrentMinute( Integer currentMinute ) {
        this.mCurrentMinute = currentMinute;
        updateMinuteDisplay();
    }

    /**
	 * Get the current second.
     * @return The current second.
     */
    public Integer getCurrentSecond() {
        return mCurrentSecond;
    }

    /**
     * Set the current second (0-59).
     * @param Integer currentSecond
     */
    public void setCurrentSecond( Integer currentSecond ) {
        this.mCurrentSecond = currentSecond;
        updateSecondDisplay();
    }

    /**
     * === getBaseline ===
     */    
    @Override
    public int getBaseline() {
        return mHourEditText.getBaseline(); 
    }

    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentHour;
        mHourEditText.setCurrent(currentHour);
        onTimeChanged();
    }

    /**
     * configurePickerRanges
     */
    private void configurePickerRanges() {
		mHourEditText.setRange(0, 23);
		mHourEditText.setFormatter(NumberEditText.TWO_DIGIT_FORMATTER);
    }

    /**
     * onTimeChanged
     */
    private void onTimeChanged() {
        mOnTimeChangedListener.onTimeChanged( this, getCurrentHour(), getCurrentMinute(), getCurrentSecond() );
    }

    /**
     * notifyFinish
     */
    private void notifyFinish() {
        if (mFinishedListener != null) {
            mFinishedListener.onFinished( this,  mSecondEditText.hasFocus() );
        }
    }

    /**
     * Set the state of the spinners appropriate to the current minute.
     */
    private void updateMinuteDisplay() {
        mMinuteEditText.setCurrent(mCurrentMinute);
        onTimeChanged();
    }
    
	/**
     * Set the state of the spinners appropriate to the current second.
     */
    private void updateSecondDisplay() {
        mSecondEditText.setCurrent(mCurrentSecond);
        onTimeChanged();
    }
}

