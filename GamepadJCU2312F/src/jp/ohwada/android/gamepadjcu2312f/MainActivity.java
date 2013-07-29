package jp.ohwada.android.gamepadjcu2312f;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

  	private static final String LF = "\n";
  	
	private InputDeviceManager mInputDeviceManager;	
	private GamepadView mGamepadView;

	/**
	 * === onCreate ===
	 */                             	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		mGamepadView = new GamepadView( this ); 
		setContentView( mGamepadView );

        mInputDeviceManager = new InputDeviceManager( this );
		mInputDeviceManager.getInputDevices();
        mInputDeviceManager.setOnInputDeviceListener( new InputDeviceManager.OnInputDeviceListener() {
            public void onInputDeviceAdded( int deviceId ) {
				String msg = mInputDeviceManager.addInputDevice( deviceId );
				toast_show( "Added" + LF + msg );        
            }
            public void onInputDeviceRemoved( int deviceId ) {
				String msg = mInputDeviceManager.addInputDevice( deviceId );
				toast_show( "Changed" + LF + msg );       
            }
			public void onInputDeviceChanged( int deviceId ) {
				String name = mInputDeviceManager.removeInputDevice( deviceId );
				toast_show( "Removed " + name + " " + deviceId );        
            }
        });

	}

	/**
	 * === onResume ===
	 */ 
    @Override
    public void onResume() {
        super.onResume();
		mInputDeviceManager.register();
   }

	/**
	 * === onPause ===
	 */ 
    @Override
    public void onPause() {
        super.onPause();
		mInputDeviceManager.unregister();
    }

	/**
	 * toast_show
	 * @param String msg
	 */                	
	private void toast_show( String msg ) {
       	ToastMaster.showText( this, msg, Toast.LENGTH_LONG );
	} 
	
}
