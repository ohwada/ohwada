package jp.ohwada.android.m3pirobot;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Lcd Activity
 */
public class LcdActivity extends BtCommonActivity {

	/** TAG (Debug) */
	private static final String TAG = "LcdActivity";

	/** view component */
	private EditText mEditTextLcd;
    private Button mButtonEnter;
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
        setContentView(R.layout.lcd);

		/* Initialization of the title bar */
		createTitleBar();
		
		/* Initialization of textview msgt */		
		createTextViewMsg();
		
		/* Initialization of button connect */
		createButtonConnect();
		
		/* Initialization of Bluetooth Adapter( */
		createBluetoothAdapter();

		/* view component */
//		initImageViewLeds();
        mEditTextLcd = (EditText) findViewById(R.id.edit_lcd);
        
		/* Initialization of button */
        mButtonEnter = (Button) findViewById(R.id.button_enter);
        mButtonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				button_enter_click();
			}
        });
        
        mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				finish();
			}
        });

    }
// --- end ---

 	/**
	 * button_enter_click
	 */		
	private void button_enter_click() {
		CharSequence cs = mEditTextLcd.getText();
		SpannableStringBuilder sb = (SpannableStringBuilder) cs;
        String str = sb.toString();
        
        // command
        cmdSendClear();
		sleepMs(500);
		cmdSendLocate( 0,0 );
		sleepMs(500);
		cmdSendPrint( str );
	}
	
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
	 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        	case REQUEST_CONNECT_DEVICE:
				execActivityResultDevice(resultCode, data);
            	break;
        	case REQUEST_ENABLE_BT:
				execActivityResultEnable(resultCode, data);
            	break;
        }
    }
// --- end ---
}
