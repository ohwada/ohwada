package jp.ohwada.android.mindstormsgamepad;

import jp.ohwada.android.mindstormsgamepad.view.NineButtonsView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Main Activity
 */
public class MainActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "MainActivity";
		
	private NineButtonsView mNineButtonsView;
	
// --- onCreate ---
 	/**
	 * === onCreate ===
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
    	log_d( "onCreate" );   		
        super.onCreate( savedInstanceState );
		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_main, null );
		setContentView( view ); 

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_touch );

		/* MindstormsCommand */	
		initSeekbarPower( view ); 
		       
		/* view component */
		mNineButtonsView = new NineButtonsView( this, view );
		mNineButtonsView.setOnTouchListener( new NineButtonsView.OnButtonTouchListener() { 
	      	@Override 
        	public void onTouch( View view, MotionEvent event, int code ) {
        		execTouch( event, code ); 
        	} 
		});

		/* Initialization of button */
		Button btnGoOrientation = (Button) findViewById( R.id.button_go_orientation );
        btnGoOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( OrientationActivity.class, Constant.REQUEST_ORIENTATION );
			}
        });
        
		Button btnGoVoice = (Button) findViewById( R.id.button_go_voice );
        btnGoVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( VoiceActivity.class, Constant.REQUEST_VOICE );
			}
        });

        Button btnGoJoystick = (Button) findViewById( R.id.button_go_joystick );
        btnGoJoystick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( JoystickActivity.class, Constant.REQUEST_JOYSTICK );
			}
        });

        Button btnGoTwoJoystick = (Button) findViewById( R.id.button_go_two_joystick );
        btnGoTwoJoystick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( TwoJoystickActivity.class, Constant.REQUEST_TWO_JOYSTICK );
			}
        });

        Button btnGoGamepad = (Button) findViewById( R.id.button_go_gamepad );
        btnGoGamepad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( GamepadActivity.class, Constant.REQUEST_GAMEPAD  );
			}
        });

        Button btnGoSound = (Button) findViewById( R.id.button_go_sound );
        btnGoSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( SoundActivity.class, Constant.REQUEST_SOUND );
			}
        });

        Button btnGoReport = (Button) findViewById( R.id.button_go_report );
        btnGoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				execStartActivity( ReportActivity.class, Constant.REQUEST_REPORT );
			}
        });
 
        Button btnGoProgram = (Button) findViewById( R.id.button_go_program );
        btnGoProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        		execStartActivity( ProgramActivity.class, Constant.REQUEST_PROGRAM );
			}
        });
                                
    }
// --- onCreate end ---

	/**
	 * === onDestroy ===
	 */
	@Override
	public void onDestroy() {
		log_d( "onDestroy()" );
		super.onDestroy();
		sendStop();
		stopService();
	}

// --- touch & click --
 	/**
	 * execTouch
	 * @param MotionEvent event
	 * @param int code
	 */
	private void execTouch( MotionEvent event, int code ) {
		switch ( event.getAction() ) { 
			case MotionEvent.ACTION_DOWN: 
				switch ( code ) {
					case NineButtonsView.BUTTON_FORWARD_LEFT:
					case NineButtonsView.BUTTON_FORWARD:
					case NineButtonsView.BUTTON_FORWARD_RIGHT:
					case NineButtonsView.BUTTON_LEFT:
					case NineButtonsView.BUTTON_RIGHT:
					case NineButtonsView.BUTTON_BACK_LEFT:
					case NineButtonsView.BUTTON_BACK:
					case NineButtonsView.BUTTON_BACK_RIGHT:
						int direction = mNineButtonsView.getDirection( code );
						sendMove_seekbar( direction );
						break;
					case NineButtonsView.BUTTON_CENTER:
						sendStop(); 
						break;
				} 
				break; 
			case MotionEvent.ACTION_UP:
				sendStop();
				break; 
		} 
	}
	
	/**
	 * execStartActivity
	 * @param Class<?> cls
	 * @param int request	 
	 */		
	private void execStartActivity( Class<?> cls, int request ) {
		Intent intent = new Intent( this, cls );
        startActivityForResult( intent, request );
	}
}
