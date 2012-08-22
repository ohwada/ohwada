package jp.ohwada.android.m3pirobot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Main Activity
 */
public class MainActivity extends BtCommonActivity {

	/** TAG (Debug) */
	private static final String TAG = "MainActivity";

	/** view component */
	private ImageView mImageViewPin;
    private Button mButtonGoLcd;
    private Button mButtonGoReport;
	
	private ImageView mImageViewForward ;
	private ImageView mImageViewBackward  ;
	private ImageView mImageViewLeft  ;
	private ImageView mImageViewRight ;
	private ImageView mImageViewStop;
	
	/* status of views */
    private boolean pinToggle  = false;

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
        setContentView(R.layout.main);
       
		/* Initialization of the title bar */
		createTitleBar();
		
		/* Initialization of textview msgt */		
		createTextViewMsg();
		
		/* Initialization of button connect */
		createButtonConnect();
		
		/* Initialization of Bluetooth Adapter( */
		createBluetoothAdapter();
		
		/* view component */
		initImageViewLeds();
		mImageViewPin = (ImageView) findViewById(R.id.image_pin);
		        
		mImageViewForward= (ImageView) findViewById(R.id.image_forward);
		mImageViewForward.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		image_forward_touch( event ); 
				return true; 
        	} 
        });
                 
		mImageViewBackward = (ImageView) findViewById(R.id.image_backward);
		mImageViewBackward.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		image_backward_touch( event ); 
				return true; 
        	} 
        });
		
		mImageViewLeft = (ImageView) findViewById(R.id.image_left);
		mImageViewLeft.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		image_left_touch( event ); 
				return true; 
        	} 
        });
		
		mImageViewRight = (ImageView) findViewById(R.id.image_right);
		mImageViewRight.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		image_right_touch( event ); 
				return true; 
        	} 
        });
		
		mImageViewStop = (ImageView) findViewById(R.id.image_stop);
		mImageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	image_stop_click();
			}
        });

		/* Initialization of button */
        mButtonGoLcd = (Button) findViewById(R.id.button_go_lcd);
        mButtonGoLcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				button_go_lcd_click();
			}
        });

        mButtonGoReport = (Button) findViewById(R.id.button_go_report);
        mButtonGoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				button_go_report_click();
			}
        });
         
    }

 	/**
	 * image_forward_touch
	 * @param MotionEvent event
	 */				
	private void image_forward_touch( MotionEvent event ) {
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN: 
				cmdSendForward(); 
				break; 
			case MotionEvent.ACTION_UP:
				cmdSendStop();
				break; 
		} 
	}

 	/**
	 * image_backward_touch
	 * @param MotionEvent event
	 */	
	private void image_backward_touch( MotionEvent event ) {
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN: 
				cmdSendBackward(); 
				break; 
			case MotionEvent.ACTION_UP:
				cmdSendStop();
				break; 
		} 
	}

 	/**
	 * image_left_touch
	 * @param MotionEvent event
	 */	
	private void image_left_touch( MotionEvent event ) {
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN: 
				cmdSendLeft(); 
				break; 
			case MotionEvent.ACTION_UP:
				cmdSendStop();
				break; 
		} 
	}

 	/**
	 * image_right_touch
	 * @param MotionEvent event
	 */	
	private void image_right_touch( MotionEvent event ) {
		switch (event.getAction()) { 
			case MotionEvent.ACTION_DOWN: 
				cmdSendRight(); 
				break; 
			case MotionEvent.ACTION_UP:
				cmdSendStop();
				break; 
		} 
	}

 	/**
	 * image_stop_click
	 */	
	private void image_stop_click() {
		cmdSendStop();
	}

 	/**
	 * button_go_lcd_click
	 */		
	private void button_go_lcd_click() {
		Intent serverIntent = new Intent(this, LcdActivity.class);
        startActivityForResult(serverIntent, REQUEST_LCD);
	}

 	/**
	 * button_go_report_click
	 */		
	private void button_go_report_click() {
		Intent serverIntent = new Intent(this, ReportActivity.class);
        startActivityForResult(serverIntent, REQUEST_REPORT);
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
		disconnectBT();
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
         	case REQUEST_LCD:
				// onResume
            	break;            	
         	case REQUEST_REPORT:
				// onResume
            	break;
    	}
    }
// --- end ---

// --- Message Handler ---
	/**
	 * command receive : Pin
	 */
	protected void cmdRecvPin( String str ) {	

		// Pin off
		if ( str.equals("0") ) {
			pinToggle = pinToggle ? false : true;
		// status ON
			if ( pinToggle ) {
			mImageViewPin.setImageResource( R.drawable.pin_green ); 

			// status OFF
			} else {
				mImageViewPin.setImageResource( R.drawable.pin_red );
			}

		// Pin on
		} else if ( str.equals("1") ) {
			mImageViewPin.setImageResource( R.drawable.pin_yellow );
		}
	}
// --- end ---

}
