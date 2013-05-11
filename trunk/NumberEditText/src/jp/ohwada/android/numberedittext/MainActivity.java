package jp.ohwada.android.numberedittext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
       
public class MainActivity extends Activity {

	private EditText mEditText2;	
       
	@Override
	protected void onCreate( Bundle savedInstanceState)  {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

        mEditText2 = (EditText) findViewById( R.id.EditText_2 );
                
        NumberEditText mNumberEditText1 = (NumberEditText) findViewById( R.id.NumberEditText_1 );
        mNumberEditText1.setRange( 0, 99 );
        mNumberEditText1.setText( "12" );
        mNumberEditText1.setFormatter( NumberEditText.TWO_DIGIT_FORMATTER );
        mNumberEditText1.setFinishedLength( 2 );
        mNumberEditText1.isClearOnFocus( true );
        mNumberEditText1.setOnChangeListener( new NumberEditText.OnChangedListener() {
            public void onChanged( NumberEditText spinner, int oldVal, int newVal ) {            
                toast_show( "onChanged " + newVal );
            }
        });
        mNumberEditText1.setOnFinishedListener( new NumberEditText.OnFinishedListener() {
	 		@Override
			public void onFinished( NumberEditText view, int value ) {
				mEditText2.requestFocus();   
			}
		});
	}

    private void toast_show( String msg ) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }
    
}
