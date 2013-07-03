package jp.ohwada.android.inputmanagersample1;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
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
		mTextView1.setText( getDeviceInfo() );
	}

	/**
	 * getDeviceInfo
	 * @return String
	 */ 
	private String getDeviceInfo() {
		String str = "";
		InputManager manager = (InputManager) getSystemService( Context.INPUT_SERVICE ) ; 
        int[] ids = manager.getInputDeviceIds();
        for ( int i = 0; i < ids.length; i++ ) {
        	int id = ids[ i ];
        	InputDevice device = manager.getInputDevice( id );
			str += device.toString() + LF;
        }
        return str;
	}
	
}
