package jp.ohwada.android.mousesample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {
  
  	private static final String LF = "\n";
  	  
	private InputDeviceManager mInputDeviceManager;	
	private MouseView mMouseView;

	/**
	 * === onCreate ===
	 */                             	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		mMouseView = new MouseView( this ); 
		setContentView( mMouseView );

        mInputDeviceManager = new InputDeviceManager( this );
		mInputDeviceManager.getInputDevices();
        mInputDeviceManager.setOnInputDeviceListener( new InputDeviceManager.OnInputDeviceListener() {
            public void onInputDeviceAdded( int deviceId ) {
				String msg = mInputDeviceManager.addInputDevice( deviceId );
				toast_show(  "Added" + LF + msg );        
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

        mMouseView.setOnGenericMotionListener( new View.OnGenericMotionListener() {
            public boolean onGenericMotion( View v, MotionEvent event ) {
            	execGenericMotion( event );
            	return true;
            }
		});
		            
	}

	/**
	 * === onResume ===
	 */ 
    @Override
    protected void onResume() {
        super.onResume();
		mInputDeviceManager.register();
   }

	/**
	 * === onPause ===
	 */ 
    @Override
    protected void onPause() {
        super.onPause();
		mInputDeviceManager.unregister();
    }

	/**
	 * execGenericMotion
	 * @param MotionEvent event
	 */
    private void execGenericMotion( MotionEvent event ) {
		boolean ret = mInputDeviceManager.dispatchMotionEvent( event );
		if ( ret ) {
			mMouseView.setValues( mInputDeviceManager.getValues() );		
        }
    }

	/**
	 * toast_show
	 * @param String msg
	 */                	
	private void toast_show( String msg ) {
       	ToastMaster.showText( this, msg, Toast.LENGTH_LONG );
	} 
	
}
