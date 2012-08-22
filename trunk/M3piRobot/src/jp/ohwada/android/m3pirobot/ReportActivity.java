package jp.ohwada.android.m3pirobot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Report Activity
 */
public class ReportActivity extends BtCommonActivity {

	/** TAG (Debug) */
	private static final String TAG = "ReportActivity";

	/** view component */
	private TextView mTextViewVoltage;
	private TextView mTextViewBattery;
	private Button mButtonMeasure;
    private Button mButtonBack;

// --- onCreate ---
 	/**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setTag( TAG );		
		/* Customizing the title bar */
        createCustomTitle();
		/* set the layout on the screen */
        setContentView(R.layout.report);
		/* Initialization of the title bar */
		createTitleBar();		
		/* Initialization of textview msgt */		
		createTextViewMsg();		
		/* Initialization of button connect */
		createButtonConnect();		
		/* Initialization of Bluetooth Adapter( */
		createBluetoothAdapter();
		/* view component */
        mTextViewVoltage = (TextView) findViewById(R.id.text_voltage);
        mTextViewBattery = (TextView) findViewById(R.id.text_battery);                

		/* Measure button */
        mButtonMeasure = (Button) findViewById(R.id.button_measure);
        mButtonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				button_measure_click();
			}
        });

		/* Back button */       
        mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				finish();
			}
        });
    }

 	/**
	 * button_measure_click
	 */
	private void button_measure_click() {
	 	cmdSendBattery();
	}
// --- onCreate end ---
		
	/**
	 * === onStart ===
	 */
    @Override
    public void onStart() {
        if (D) Log.d( TAG,"onStart()" );
        super.onStart();
        startBluetoothService();
    }

	/**
	 * === onResume ===
	 */
    @Override
    public synchronized void onResume() {
        if (D) Log.d( TAG,"onResume()" );
        super.onResume();
        startBT();
    }

	/**
	 * === onPause ===
	 */
    @Override
    public synchronized void onPause() {
        super.onPause();
    }

	/**
	 * === onStop ===
	 */
    @Override
    public void onStop() {
        super.onStop();
    }

	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

// --- onActivityResult ---
	/**
	 * === onActivityResult ===
	 * @param int requestCode
	 * @param int resultCode
	 * @param Intent data
	 */
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch (requestCode) {
        	case REQUEST_CONNECT_DEVICE:
				execActivityResultDevice(resultCode, data);
            	break;
        	case REQUEST_ENABLE_BT:
				execActivityResultEnable(resultCode, data);
            	break;
        }
    }
// --- onActivityResult end ---

// --- Message Handler ---
	/**
	 * command receive : Battery
	 * @param String str
	 */
	protected void cmdRecvBattery( String str ) {	
		mTextViewBattery.setText( str );		
		// next command
		cmdSendVoltage( );
	}

	/**
	 * command receive : Voltage
	 * @param String str
	 */
	protected void cmdRecvVoltage( String str ) {	
		mTextViewVoltage.setText( str );
	}		
// --- Message Handler end ---

}
