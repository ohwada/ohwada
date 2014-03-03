package jp.ohwada.android.rapirosample1;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.UartConfig;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

    // debug settings
    private static final boolean D = true;
    private static final String TAG = "MainActivity";
    
    // UART
    private static final int UART_BAUDRATE = 57600;
    private static final boolean UART_DTR = false;
    private static final boolean UART_RTS = false;
    
	private  static final int TIME_WRITE = 10;
	private  static final int TIME_READ = 50;
	private  static final int READ_BUFFER_SIZE = 4096;

	private  static final String SPACE = " ";

	// Rapiro Seavo
    private static final int[] SERVO_MIN = new int[]{ 0, 0, 0, 40, 60, 0, 50, 60, 70, 70, 50, 50 };
    private static final int[] SERVO_MAX = new int[]{ 180, 180, 180, 130, 110, 180, 140, 110, 130, 110, 110, 110 };
    private static final int[] SERVO_INIT = new int[]{ 90, 90, 0, 130, 90, 180, 50, 90, 90, 90, 90, 90 };

	private DecimalFormat mFotmatTwo = new DecimalFormat("00");
	private DecimalFormat mFotmatThree = new DecimalFormat("000");		
	            
    private Physicaloid mSerial;

	// UI
    private LinearLayout mLinearLayoutMove;
    private LinearLayout mLinearLayoutPose;

	// recieve
    private boolean isStop = true;
    private boolean isRunning = false;

    private static final String ACTION_USB_PERMISSION =
		"jp.ohwada.android.rapirosample1.USB_PERMISSION";

	/**
	 * === onCreate ===
	 */
    @Override
    public void onCreate( Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState) ;        
        setContentView( R.layout.actvity_main );

		mLinearLayoutMove = (LinearLayout) findViewById( R.id.LinearLayout_move );
		mLinearLayoutPose = (LinearLayout) findViewById( R.id.LinearLayout_pose );
        mLinearLayoutPose.setVisibility( View.GONE );
            					
		initButton( R.id.Button_move );
		initButton( R.id.Button_pose );			
		initButton( R.id.Button_green );
		initButton( R.id.Button_yellow );
		initButton( R.id.Button_blue );
		initButton( R.id.Button_red );
		initButton( R.id.Button_push );
					
		initImageView( R.id.ImageView_stop );
		initImageView( R.id.ImageView_forward );
		initImageView( R.id.ImageView_back );
		initImageView( R.id.ImageView_left );
		initImageView( R.id.ImageView_right );

		initSeekBarRGB( R.id.SeekBar_R );
		initSeekBarRGB( R.id.SeekBar_G );
		initSeekBarRGB( R.id.SeekBar_B );

		initSeekBarS( 0, R.id.SeekBar_S00 );
		initSeekBarS( 1, R.id.SeekBar_S01 );
		initSeekBarS( 2, R.id.SeekBar_S02 );
		initSeekBarS( 3, R.id.SeekBar_S03 );
		initSeekBarS( 4, R.id.SeekBar_S04 );
		initSeekBarS( 5, R.id.SeekBar_S05 );
		initSeekBarS( 6, R.id.SeekBar_S06 );
		initSeekBarS( 7, R.id.SeekBar_S07 );
		initSeekBarS( 8, R.id.SeekBar_S08 );
		initSeekBarS( 9, R.id.SeekBar_S09 );
		initSeekBarS( 10, R.id.SeekBar_S10 );
		initSeekBarS( 11, R.id.SeekBar_S11 );
																				 
        // get service
        mSerial = new Physicaloid(this);
		log_d( "New instance : " + mSerial);
        
        // listen for new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction( UsbManager.ACTION_USB_DEVICE_ATTACHED );
        filter.addAction( UsbManager.ACTION_USB_DEVICE_DETACHED );
        registerReceiver( mUsbReceiver, filter );

        log_d( "FTDriver beginning");
        openUsbSerial();
    }

	/**
	 * === onDestroy ===
	 */	
    @Override
    protected void onNewIntent( Intent intent ) {
        log_d( "onNewIntent");        
        openUsbSerial();
    }

	/**
	 * === onDestroy ===
	 */	
    @Override
    public void onDestroy() {
        mSerial.close();
		isStop = true;
        unregisterReceiver( mUsbReceiver );
        super.onDestroy();
    }

	/**
	 * initButton
	 */
    private void initButton( int id ) {
		Button btn = (Button) findViewById( id );
		btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execButton( view );
			}
		});
	}

	/**
	 * execButton
	 */
    private void execButton( View view ) {
    	int id = view.getId();
    	if ( id == R.id.Button_move ) {
        	mLinearLayoutMove.setVisibility( View.VISIBLE );
        	mLinearLayoutPose.setVisibility( View.GONE );   		
    	} else if ( id == R.id.Button_pose ) {
        	mLinearLayoutMove.setVisibility( View.GONE );
        	mLinearLayoutPose.setVisibility( View.VISIBLE );
    	} else if ( id == R.id.Button_green ) {
    		writeSerial( "#M5" );
    	} else if ( id == R.id.Button_yellow ) {
    		writeSerial( "#M6" );
    	} else if ( id == R.id.Button_blue ) {
    		writeSerial( "#M7" );
    	} else if ( id == R.id.Button_red ) {
    		writeSerial( "#M8" );
    	} else if ( id == R.id.Button_push ) {
    		writeSerial( "#M9" );
    	}	
	}

	/**
	 * initImageView
	 */
    private void initImageView( int id ) {
		ImageView btn= (ImageView) findViewById( id );
		btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
            	execImageView( view );
			}
		});
	}

	/**
	 * execImageView
	 */
    private void execImageView( View view ) {
    	int id = view.getId();
    	if ( id == R.id.ImageView_stop ) {
    		writeSerial( "#M0" );
    	} else if ( id == R.id.ImageView_forward ) {
    		writeSerial( "#M1" );
    	} else if ( id == R.id.ImageView_back ) {
    		writeSerial( "#M2" );
    	} else if ( id == R.id.ImageView_left ) {
    		writeSerial( "#M3" );
    	} else if ( id == R.id.ImageView_right ) {
    		writeSerial( "#M4" );
    	}
    }
	
	/**
	 * initSeekBar RGB
	 */	            			
    private void initSeekBarRGB( int id ) {
    	initSeekBar( id, 255, 128 );
    }

	/**
	 * initSeekBar Servo
	 */	 
    private void initSeekBarS( int index, int id ) {
    	initSeekBar( id, 180, SERVO_INIT[ index ] );
    }

	/**
	 * initSeekBar
	 */	    
    private void initSeekBar( int id, int max, int progress ) {
		SeekBar sb = (SeekBar) findViewById( id );
        sb.setMax( max );
        sb.setProgress( progress );
        sb.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
            @Override
            public void onProgressChanged( SeekBar seekBar, int progress, boolean fromTouch ) {
				execSeekBar( seekBar, progress );
            }
            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {
				// dummy
            }
        });
	}

	/**
	 * execSeekBar
	 */	
    private void execSeekBar( SeekBar view, int progress ) {
    	int id = view.getId();
    	if ( id == R.id.SeekBar_S00 ) {   	
    		execSeekBarS( 0, progress );
    	} else if ( id == R.id.SeekBar_S01 ) {
    		execSeekBarS( 1, progress );
    	} else if ( id == R.id.SeekBar_S02 ) {
    		execSeekBarS( 2, progress );
    	} else if ( id == R.id.SeekBar_S03 ) {
    		execSeekBarS( 3, progress );
    	} else if ( id == R.id.SeekBar_S04 ) {
    		execSeekBarS( 4, progress );
    	} else if ( id == R.id.SeekBar_S05 ) {
    		execSeekBarS( 5, progress );
    	} else if ( id == R.id.SeekBar_S06 ) {
    		execSeekBarS( 6, progress );
    	} else if ( id == R.id.SeekBar_S07 ) {
    		execSeekBarS( 7, progress );
    	} else if ( id == R.id.SeekBar_S08 ) {
    		execSeekBarS( 8, progress );
     	} else if ( id == R.id.SeekBar_S09 ) {
    		execSeekBarS( 9, progress );
    	} else if ( id == R.id.SeekBar_S10 ) {
    		execSeekBarS( 10, progress );
    	} else if ( id == R.id.SeekBar_S11 ) {
    		execSeekBarS( 11, progress );
    	} else if ( id == R.id.SeekBar_R ) {
    		execSeekBarRGB( "R", progress );
    	} else if ( id == R.id.SeekBar_G ) {
    		execSeekBarRGB( "G", progress );
    	} else if ( id == R.id.SeekBar_B ) {
    		execSeekBarRGB( "B", progress );
    	} 
	}

	/**
	 * execSeekBar servo
	 */
    private void execSeekBarS( int servo, int progress ) {
    	int min = SERVO_MIN[ servo ];
    	int max = SERVO_MAX[ servo ];
    	progress = ( progress < min ) ? progress = min : progress;
    	progress = ( progress > max ) ? progress = max : progress;    	
    	String str = "#PS" + getStringTwoDigit( servo ) + "A" + getStringThreeDigit( progress ) + "T001";
		writeSerial( str );
	}

	/**
	 * execSeekBar RGB
	 */	
    private void execSeekBarRGB( String rgb, int progress ) {
    	String str = "#P" + rgb + getStringThreeDigit( progress ) + "T001";
		writeSerial( str );
	}

	/**
	 * getString TwoDigit
	 */	 
    private String getStringTwoDigit( int servo ) {
		return mFotmatTwo.format( servo );
	}

	/**
	 * getString ThreeDigit
	 */		    	
    private String getStringThreeDigit( int progress ) {
		return mFotmatThree .format( progress );
	}

	/**
	 * writeSerial
	 */			
    private void writeSerial( String str ) {
		log_d( str );
    	mSerial.write( str.getBytes(), str.length() );
    	sleep( TIME_WRITE );
    }

	/**
	 * openUsbSerial
	 */    
    private void openUsbSerial() {
        if ( mSerial == null ) {
            toast_show( "cannot open" );
            return;
        }

        if ( !mSerial.isOpened( )) {
            log_d( "onNewIntent begin");
            if ( !mSerial.open() ) {
                toast_show( "cannot open" );
                return;
            } else {
				mSerial.setConfig( new UartConfig( 
					UART_BAUDRATE, UartConfig.DATA_BITS8, UartConfig.STOP_BITS1, UartConfig.PARITY_NONE, UART_DTR, UART_RTS) );
                toast_show( "connected" );
            }
        }
        
        if ( !isRunning ) {
			startRecv();
		}

    }

	/**
	 * detachedUi
	 */ 
    private void detachedUi() {
        toast_show( "disconnect" );
    }

	/**
	 * sleep
	 * @param long time
	 */		
	private void sleep( long time ) {
		try {
			Thread.sleep( time );
		} catch ( InterruptedException e ) {
			if (D) e.printStackTrace();
		}
	}

	/**
	 * startRecv
	 */
    private void startRecv() {
        toast_show( "connected" );
		log_d( "start startRecv" );
		isStop = false;
		isRunning = true;
		new Thread( mRunnable ).start();
    }

	/**
	 * --- Runnable ---
	 */
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			int len = 0;
			byte[] rbuf = new byte[ READ_BUFFER_SIZE ];
			while ( true ) {// this is the main loop for transferring
				len = mSerial.read( rbuf );
				if ( len > 0 ) {
					String msg = bytesToHexString( rbuf, len );
					log_d( "Read : " + msg );
				}
				sleep( TIME_READ );
				if ( isStop ) {
					isRunning = false;
					return;
				}
			}
		}
	};

	/**
	 * Bytes To HexString
	 */ 
	private String bytesToHexString( byte[] bytes, int length ) {
		String str = "";
		for ( int i=0; i < length ; i++ ) {
			str += String.format( "%02X", bytes[ i ] ) + SPACE;
		}
		return str;
	}

	/**
	 * --- BroadcastReceiver ---
	 */ 
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				log_d( "Device attached");
                if (!mSerial.isOpened()) {
					log_d( "Device attached begin");
                    openUsbSerial();
                }
                if ( !isRunning ) {
					log_d( "Device attached startRecv");
                    startRecv();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				log_d( "Device detached");
				isStop = true;
                detachedUi();
                mSerial.close();
            } else if ( ACTION_USB_PERMISSION.equals( action ) ) {
				log_d( "Request permission");
                synchronized ( this ) {
                    if ( !mSerial.isOpened() ) {
						log_d( "Request permission begin");
                        openUsbSerial();
                    }
                }
                if (!isRunning) {
					log_d( "Request permission startRecv" );
                    startRecv();
                }
            }
        }
    };

	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
		ToastMaster.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}

 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	}	
}
