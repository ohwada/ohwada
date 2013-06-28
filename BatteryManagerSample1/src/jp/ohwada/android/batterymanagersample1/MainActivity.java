package jp.ohwada.android.batterymanagersample1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

// http://www.adakoda.com/android/000140.html

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private TextView mTextView1;
	private ImageView mImageView1;

	private BatteryReceiver mBatteryReceiver;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		mTextView1 = (TextView) findViewById( R.id.TextView1 ); 
		mImageView1 = (ImageView) findViewById( R.id.ImageView1 ); 

		mBatteryReceiver = new BatteryReceiver( this );
        mBatteryReceiver.setOnReceiveListener( new BatteryReceiver.OnReceiveListener() {
            public void onReceive( BatteryRecod recod ) {
            	execReceive( recod );          
            }
        });
	}

	/**
	 * === onResume ===
	 */	
   @Override
    protected void onResume() {
        super.onResume();
        mBatteryReceiver.register();
    }

	/**
	 * === onPause ===
	 */	
    @Override
    protected void onPause() {
        super.onPause();  
        mBatteryReceiver.unregister();     
    }

	/**
	 * execReceive
	 * @param BatteryRecod r
	 */	
    private void execReceive( BatteryRecod r ) {
		mTextView1.setText( r.toString() ); 
		if ( r.icon_small > 0 ) {
			mImageView1.setImageResource( r.icon_small );
		}
	}     
}
