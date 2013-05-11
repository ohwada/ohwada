package jp.ohwada.android.timeedittext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText mEditText2;	
	
	@Override
	protected void onCreate( Bundle savedInstanceState)  {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		EditText mEditText1 = (EditText) findViewById( R.id.EditText_1 );
		mEditText1.setVisibility( View.GONE );
		        
        mEditText2 = (EditText) findViewById( R.id.EditText_2 );
		mEditText2.setVisibility( View.GONE );

		EditText mEditText3 = (EditText) findViewById( R.id.EditText_3 );
		mEditText3.setVisibility( View.GONE );
		
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
					mEditText2.requestFocus(); 
				}
			}
		});

		TimeEditText mTimeEditText2 = (TimeEditText) findViewById( R.id.TimeEditText_2 );               
		mTimeEditText2.setCurrentTime( 1, 2, 3 );
	}

    private void toast_show( String msg ) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

}
