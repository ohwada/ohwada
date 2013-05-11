package jp.ohwada.android.numberedittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * NumberEditText
 * base on com.android.internal.widget.NumberPicker
 */
public class NumberEditText extends EditText
	implements OnFocusChangeListener {

	/**
	 * interface OnChangedListener
	 */        
    public interface OnChangedListener {
        void onChanged( NumberEditText view, int oldVal, int newVal );
    }

	/**
	 * interface OnFinishedListener
	 */    
    public interface OnFinishedListener {
        void onFinished( NumberEditText view, int value );
    }

	/**
	 * interface Formatter
	 */  
    public interface Formatter {
        String toString( int value );
    }

    /*
     * Use a custom NumberPicker formatting callback to use two-digit
     * minutes strings like "01".  Keeping a static formatter etc. is the
     * most efficient way to do this; it avoids creating temporary objects
     * on every call to format().
     */
    public static final NumberEditText.Formatter TWO_DIGIT_FORMATTER = 
            new NumberEditText.Formatter() {
                final StringBuilder mBuilder = new StringBuilder();
                final java.util.Formatter mFmt = new java.util.Formatter(mBuilder);
                final Object[] mArgs = new Object[1];
                public String toString(int value) {
                    mArgs[0] = value;
                    mBuilder.delete(0, mBuilder.length());
                    mFmt.format("%02d", mArgs);
                    return mFmt.toString();
                }
        };

    private final InputFilter mNumberInputFilter;

    private String[] mDisplayedValues;
    private int mStart;
    private int mEnd;
    private int mCurrent;
    private int mPrevious;
    private OnChangedListener mChangedListener;
    private OnFinishedListener mFinishedListener;        
    private Formatter mFormatter;
 
    private int mFinishedLength = 0;  
    private boolean isClearOnFocus = false;

	/*
	 * === constractor ===
	 */      
    public NumberEditText( Context context ) {    
        this( context, null );
    }

	/*
	 * === constractor ===
	 */       
    public NumberEditText( Context context, AttributeSet attrs ) {    
        this( context, attrs, 0 );
    }

	/*
	 * === constractor ===
	 */    
    public NumberEditText( Context context, AttributeSet attrs, int defStyle ) {   
        super( context, attrs );

        InputFilter inputFilter = new NumberPickerInputFilter();
        mNumberInputFilter = new NumberRangeKeyListener();
        setOnFocusChangeListener( this );              
        setFilters( new InputFilter[] {inputFilter} );
        setRawInputType( InputType.TYPE_CLASS_NUMBER );
        setGravity( Gravity.CENTER );
		setTransformationMethod( SingleLineTransformationMethod.getInstance() );
    }

	/**
	 * setFinishedLength
	 * @param int length
	 */
	public void setFinishedLength( int length ) {
		mFinishedLength = length;
		addTextChangedListener( new NumberTextWatcher() );    
	}

	/**
	 * isClearOnFocus
	 * @param boolean is
	 */
	public void isClearOnFocus( boolean is ) {
		isClearOnFocus = is;
	}

	/**
	 * setOnChangeListener
	 * @param OnChangedListener listener
	 */
    public void setOnChangeListener( OnChangedListener listener ) {
        mChangedListener = listener;
    }

	/**
	 * setOnFinishedListener
	 * @param OnFinishedListener listener
	 */        
    public void setOnFinishedListener( OnFinishedListener listener ) {
        mFinishedListener = listener;
    }

	/**
	 * setFormatter
	 * @param Formatter formatter
	 */          
    public void setFormatter( Formatter formatter ) {
        mFormatter = formatter;
    }
    
    /**
     * Set the range of numbers allowed for the number picker. The current
     * value will be automatically set to the start.
     * 
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     */
    public void setRange( int start, int end ) {
        mStart = start;
        mEnd = end;
        mCurrent = start;
        updateView();
    }
    
    /**
     * Set the range of numbers allowed for the number picker. The current
     * value will be automatically set to the start. Also provide a mapping
     * for values used to display to the user.
     * 
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     * @param displayedValues the values displayed to the user.
     */
    public void setRange( int start, int end, String[] displayedValues ) {
        mDisplayedValues = displayedValues;
        mStart = start;
        mEnd = end;
        mCurrent = start;
        updateView();
    }

	/**
	 * setCurrent
	 * @param int current
	 */        
    public void setCurrent( int current ) {
        mCurrent = current;
        updateView();
    }

	/**
	 * formatNumber
	 * @param int value
	 * @return String
	 */     
    private String formatNumber( int value ) {
        return (mFormatter != null)
                ? mFormatter.toString(value)
                : String.valueOf(value);
    }

	/**
	 * notifyChange
	 */        
    private void notifyChange() {
        if (mChangedListener != null) {
            mChangedListener.onChanged(this, mPrevious, mCurrent);
        }
    }

	/**
	 * notifyFinish
	 */  
    private void notifyFinish() {
        if (mFinishedListener != null) {
            mFinishedListener.onFinished( this, mCurrent );
        }
    }

	/**
	 * updateView
	 */    
    private void updateView() {        
        /* If we don't have displayed values then use the
         * current number else find the correct value in the
         * displayed values for the current number.
         */
        if (mDisplayedValues == null) {
            setText( formatNumber(mCurrent) );
        } else {
            setText( mDisplayedValues[mCurrent - mStart] );
        }
        setSelection( getText().length() );        
    }

	/**
	 * validateCurrentView
	 * @param CharSequence str
	 */      
    private void validateCurrentView( CharSequence str ) {
        int val = getSelectedPos(str.toString());
        if ((val >= mStart) && (val <= mEnd)) {
            if (mCurrent != val) {
                mPrevious = mCurrent;
                mCurrent = val;
                notifyChange();
            }
        }
        updateView();
    }

	/**
	 * onFocusChange
	 * @param View v
	 * @param boolean hasFocus	 
	 */    
    public void onFocusChange( View v, boolean hasFocus ) {
		if ( hasFocus && isClearOnFocus ){
			String text = getText().toString();
			setText( "" );
			setHint( text );
		}
                
        /* When focus is lost check that the text field
         * has valid values.
         */
        if (!hasFocus) {
            validateInput(v);
        }
    }

	/**
	 * validateInput
	 * @param View v
	 */   
    private void validateInput( View v ) {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str)) {

            // Restore to the old value as we don't allow empty values
            updateView();
        } else {

            // Check the new value and ensure it's in range
            validateCurrentView(str);
        }
    }
  
    private static final char[] DIGIT_CHARACTERS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

	/**
	 * class NumberPickerInputFilter
	 */  
    private class NumberPickerInputFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {
            if (mDisplayedValues == null) {
                return mNumberInputFilter.filter(source, start, end, dest, dstart, dend);
            }
            CharSequence filtered = String.valueOf(source.subSequence(start, end));
            String result = String.valueOf(dest.subSequence(0, dstart))
                    + filtered
                    + dest.subSequence(dend, dest.length());
            String str = String.valueOf(result).toLowerCase();
            for (String val : mDisplayedValues) {
                val = val.toLowerCase();
                if (val.startsWith(str)) {
                    return filtered;
                }
            }
            return "";
        }
    }

	/**
	 * class NumberRangeKeyListener
	 */      
    private class NumberRangeKeyListener extends NumberKeyListener {

        // This doesn't allow for range limits when controlled by a
        // soft input method!
        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER;
        }
        
        @Override
        protected char[] getAcceptedChars() {
            return DIGIT_CHARACTERS;
        }
        
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {

            CharSequence filtered = super.filter(source, start, end, dest, dstart, dend);
            if (filtered == null) {
                filtered = source.subSequence(start, end);
            }

            String result = String.valueOf(dest.subSequence(0, dstart))
                    + filtered
                    + dest.subSequence(dend, dest.length());

            if ("".equals(result)) {
                return result;
            }
            int val = getSelectedPos(result);

            /* Ensure the user can't type in a value greater
             * than the max allowed. We have to allow less than min
             * as the user might want to delete some numbers
             * and then type a new number.
             */
            if (val > mEnd) {
                return "";
            } else {
                return filtered;
            }
        }
    }

	/**
	 * getSelectedPos
	 * @param String str
	 */  
    private int getSelectedPos( String str ) {
        if (mDisplayedValues == null) {
            return Integer.parseInt(str);
        } else {
            for (int i = 0; i < mDisplayedValues.length; i++) {
                
                /* Don't force the user to type in jan when ja will do */
                str = str.toLowerCase();
                if (mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return mStart + i;
                }
            }
            
            /* The user might have typed in a number into the month field i.e.
             * 10 instead of OCT so support that too.
             */
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                
                /* Ignore as if it's not a number we don't care */
            }
        }
        return mStart;
    }

    /**
     * getCurrent
     * @return the current value.
     */
    public int getCurrent() {
        return mCurrent;
    }

	/**
	 * class NumberTextWatcher
	 */     
	private class NumberTextWatcher implements TextWatcher {
		public void afterTextChanged( Editable arg ) {
			execAfterTextChanged( arg );
		}
		public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
			execBeforeTextChanged( s, start, count, after );
		}
		public void onTextChanged( CharSequence s, int start, int before, int count ) {
			execOnTextChanged( s, start, before, count );
		}
	}

	/**
	 * execAfterTextChanged
	 * @param Editable arg
	 */    
    private void execAfterTextChanged( Editable arg ) {
		if ( mFinishedLength <= 0 ) return;
    	if ( arg == null ) return;
    	String text = arg.toString();
    	if ( text == null ) return;
		if ( text.length() >= mFinishedLength )  {
			notifyFinish();
		}
    }    

	/**
	 * execBeforeTextChanged
	 * @param CharSequence s
	 * @param int start
	 * @param int count
	 * @param int after
	 */ 
	 private void execBeforeTextChanged( CharSequence s, int start, int count, int after ) {
		// dummy
	}

	/**
	 * execBeforeTextChanged
	 * @param CharSequence s
	 * @param int start
	 * @param int before
	 * @param int count
	 */ 
	 private void execOnTextChanged( CharSequence s, int start, int before, int count ) {
		// dummy
	 }
		    
}