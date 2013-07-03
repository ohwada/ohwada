package jp.ohwada.android.inputmanagersample3;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.InputDevice;
import android.widget.TextView;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private static final String LF = "\n";
	
	private TextView mTextView1;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		mTextView1 = (TextView) findViewById( R.id.TextView1 ); 
		mTextView1.setText( getResult() );
	}

	/**
	 * getResult
	 * @return String
	 */	
	private String getResult() {
		String str = "";
		MyInputManager manager = new MyInputManager( this ); 
		List<InputDevice> list = manager.getExternalKeyboardList();
		for ( int i=0; i<list.size(); i++ ) {
			InputDevice device = list.get( i );
			str += device.getName() + LF;
			str += manager.getKeyboardLabel( device.getDescriptor() ) + LF;
			str += LF;			
		}
		return str;
	}
		            
}
