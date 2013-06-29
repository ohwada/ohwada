package jp.ohwada.android.inputmanagersample2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

	private static final String TAG = "InputManagerSample";
	private static final String LF = "\n";
		
	private InputDeviceManager mInputDeviceManager;	
	private TextView mTextView1;
	
	/**
	 * === onCreate ===
	 */ 				
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		log_d( "onCreate" );
		setContentView( R.layout.activity_main );
		mTextView1 = (TextView) findViewById( R.id.TextView1 );

        mInputDeviceManager = new InputDeviceManager( this );
        mInputDeviceManager.setOnInputDeviceListener( new InputDeviceManager.OnInputDeviceListener() {
            public void onInputDeviceAdded( InputDeviceRecord record ) {
        		execCommon( "added", record );         
            }
            public void onInputDeviceRemoved( InputDeviceRecord record ) {
        		execCommon( "removed", record );        
            }
			public void onInputDeviceChanged( InputDeviceRecord record ) {
        		execCommon( "changed", record );         
            }
        });
        
	}

	/**
	 * === onResume ===
	 */ 
   @Override
    protected void onResume() {
        super.onResume();
        log_d( "onResume" );
		mInputDeviceManager.register();
   }

	/**
	 * === onPause ===
	 */ 
    @Override
    protected void onPause() {
        super.onPause();        
        log_d( "onPause" );
		mInputDeviceManager.unregister();
    }

	/**
	 * === onDestroy ===
	 */ 
    @Override
    protected void onDestroy() {
        super.onDestroy();   
        log_d( "onDestroy" );
    }

	/**
	 * execCommon
	 * @param String event
	 * @param InputDeviceRecord record	 
	 */ 
    private void execCommon( String event, InputDeviceRecord record ) {
    	String text = "event: " + event + LF;
		text += record.toString();
		mTextView1.setText( text );		
		toast_show( text );
		log_d( text );
	}

	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
       	ToastMaster.showText( this, msg, Toast.LENGTH_LONG );
	} 
	
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	   Log.d( TAG, msg );
	}
	
}
