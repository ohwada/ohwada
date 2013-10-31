package jp.ohwada.android.timeedittext;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */
public class MainActivity extends Activity {
	
	private TextView mTextView1;

	/**
	 * onCreate
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState)  {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		// There are no method that EditView is not focused.
		// And then Textview is prepared as dummy, and is focused.
		// As a result, EditView is not focused for appearance.
		mTextView1 = (TextView) findViewById( R.id.TextView_1 );
        mTextView1.setFocusable( true );
        mTextView1.setFocusableInTouchMode( true );
		mTextView1.requestFocus(); 
							
        TimeEditText mTimeEditText1 = (TimeEditText) findViewById( R.id.TimeEditText_1 );               
		mTimeEditText1.isClearOnFocus( true );
		mTimeEditText1.setNextFocus();
		mTimeEditText1.setCurrentTime( 12, 34, 56 );
		mTimeEditText1.setOnTimeChangedListener( new TimeEditText.OnTimeChangedListener() {
       		public void onTimeChanged( TimeEditText view, int hourOfDay, int minute, int second ) {
       		String msg = "onTimeChanged " + hourOfDay + " " + minute + " " + second;
        		toast_show( msg );     		
        	}
    	});
		mTimeEditText1.setOnFinishedListener( new TimeEditText.OnFinishedListener() {
	 		@Override
			public void onFinished( TimeEditText view, boolean hasFocusSecond ) {
				if ( hasFocusSecond ) {
					mTextView1.requestFocus(); 
					hideKeyboard();
				}
			}
		});

		TimeEditText mTimeEditText2 = (TimeEditText) findViewById( R.id.TimeEditText_2 );               
		mTimeEditText2.setCurrentTime( 1, 2, 3 );
	}

	/**
	 * hide Keyboard
	 */
	private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
        	getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( mTextView1.getWindowToken(), 0 ); 
	}

	/**
	 * toast_show
	 */	
    private void toast_show( String msg ) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

}
