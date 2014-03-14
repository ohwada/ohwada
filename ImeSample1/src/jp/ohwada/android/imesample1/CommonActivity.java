package jp.ohwada.android.imesample1;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * CommonActivity
 */
public class CommonActivity extends Activity {

	private TextView mTextView1;
	private TextView mTextView2;
	private EditText mEditText1;
		
	/**
	 * === onCreate ===
	 */
	protected void execCreate() {
		setContentView( R.layout.activity_main );

		mTextView1 = (TextView) findViewById( R.id.TextView1 );		
		mTextView2 = (TextView) findViewById( R.id.TextView2 );
		mEditText1 = (EditText) findViewById( R.id.EditText1 );
					
		Button btn1 = (Button) findViewById( R.id.Button1 );
		btn1.setOnClickListener( new OnClickListener() {
            public void onClick( View v ) {  
				String text = mEditText1.getText().toString();
				mTextView2.setText( text );
             }
        }); 	
	}

	/**
	 * === onCreateOptionsMenu ===
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}

	/**
	 * === onOptionsItemSelected ===
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
    	switch ( item.getItemId() ) {
    		case R.id.action_default:
    			Intent intent1 = new Intent( this, MainActivity.class );
				startActivity( intent1 );
        		return true; 
    		case R.id.action_resize:
    			Intent intent2 = new Intent( this, ResizeActivity.class );
				startActivity( intent2 );
        		return true; 
    		case R.id.action_no_fullscreen:
    			Intent intent3 = new Intent( this, NoFullscreenActivity.class );
				startActivity( intent3 );
        		return true; 
    	}
    	return false;
	}

	/**
	 * setNoFullscreen
	 */
	protected void setNoFullscreen() {	
		mEditText1.setImeOptions( 
			EditorInfo.IME_FLAG_NO_FULLSCREEN |
			EditorInfo.IME_FLAG_NO_EXTRACT_UI );
	}
	
	/**
	 * setText
	 */
	protected void setText( int id ) {	
		mTextView1.setText( id );
	}
					
}
