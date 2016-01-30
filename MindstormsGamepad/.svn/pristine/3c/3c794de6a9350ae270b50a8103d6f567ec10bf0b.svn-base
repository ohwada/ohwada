package jp.ohwada.android.mindstormsgamepad;

import java.util.List;

import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsMessage;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Sound Activity
 */
public class SoundActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "SoundActivity";

	private static final int CMD_PLAY_TONE = MindstormsCommand.CMD_PLAY_TONE;
    
	public static final int TONE_C1  = 262;	// do
	public static final int TONE_CS1 = 277; 
	public static final int TONE_D1 = 294;	// re 
	public static final int TONE_DS1 = 311; 
	public static final int TONE_E1 = 330;		// mi 
	public static final int TONE_F1 = 349;		// fa 
	public static final int TONE_FS1 = 370; 
	public static final int TONE_G1 = 392;	// so
	public static final int TONE_GS1  = 415; 
	public static final int TONE_A1 = 440;	// ra 
	public static final int TONE_AS1 = 466; 
	public static final int TONE_B1 = 494;		// si
	public static final int TONE_C2  = 523;	// do
	public static final int TONE_CS2 = 554; 
	public static final int TONE_D2 = 587;	// re 
	public static final int TONE_DS2 = 622; 
	public static final int TONE_E2 = 659;		// mi 
	public static final int TONE_F2 = 698;		// fa 
	public static final int TONE_FS2 = 740; 
	public static final int TONE_G2 = 784;	// so
	public static final int TONE_GS2 = 831; 
	public static final int TONE_A2 = 880;	// ra 
	public static final int TONE_AS2 = 932; 
	public static final int TONE_B2 = 988;		// si
    
// --- onCreate ---
 	/**
	 * === onCreate ===
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
		log_d( "onCreate" );   
        super.onCreate( savedInstanceState );       
        /* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_sound, null );
		setContentView( view ); 		

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_sound );
		initButtonBack();

		/* Tone1 button */
        Button btnTone1 = (Button) findViewById( R.id.Button_sound_tone_1 );
        btnTone1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				sendPlayTone1();
			}
        });

		/* Tone2 button */
        Button btnTone2 = (Button) findViewById( R.id.Button_sound_tone_2 );
       btnTone2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				sendPlayTone2();
			}
        });

		/* Tone3 button */
        Button btnTone3 = (Button) findViewById( R.id.Button_sound_tone_3 );
       btnTone3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				sendPlayTone3();
			}
        });
 
 		/* sound button */
		Button btnSound = (Button) findViewById( R.id.Button_sound_file );
		btnSound.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// first comannd
				sendFindFirst();
			}
        });                      
    }

// --- onCreate end --

// --- comand send ---      
    /**
	 * cmd Send FindFiles
	 */	
	private void sendFindFirst() {
		mCommand.sendFindFirstSound();  
	}

    /**
     * PlaySoundFile
     */	
	private void sendPlaySoundFile( String name ) {
		byte[] cmd = MindstormsMessage.cmdPlaySoundFile( false, name );
		sendCmdMassage( cmd ); 	
	}
 
    /**
     * PlayTone
     */	
    private void sendPlayTone1() {
		// G-F-E-D-C								
    	sendViaHandler(    0, CMD_PLAY_TONE, TONE_G1, 100 );
    	sendViaHandler( 200, CMD_PLAY_TONE, TONE_F1, 100 );
    	sendViaHandler( 400, CMD_PLAY_TONE, TONE_E1, 100 );
    	sendViaHandler( 600, CMD_PLAY_TONE, TONE_D1, 100 );
    	sendViaHandler( 800, CMD_PLAY_TONE, TONE_C1, 300 );
	}

    /**
     * PlayTone
     */		
    private void sendPlayTone2() {
		// Wolfgang Amadeus Mozart 
		// "Zauberfloete - Der Vogelfaenger bin ich ja"
    	sendViaHandler(     0, CMD_PLAY_TONE, TONE_G1, 100 );
    	sendViaHandler(  200, CMD_PLAY_TONE, TONE_A1, 100 );
    	sendViaHandler(  400, CMD_PLAY_TONE, TONE_B1, 100 );
    	sendViaHandler(  600, CMD_PLAY_TONE, TONE_C2, 100 );
    	sendViaHandler(  800, CMD_PLAY_TONE, TONE_D2, 300 );
    	sendViaHandler( 1200, CMD_PLAY_TONE, TONE_C2, 300 );
    	sendViaHandler( 1600, CMD_PLAY_TONE, TONE_B1, 300 );
	}	

    /**
     * PlayTone
     */		
    private void sendPlayTone3() {
		// ka e ru no u ta ga 
    	sendViaHandler(     0, CMD_PLAY_TONE, TONE_C1, 100 );
    	sendViaHandler(  200, CMD_PLAY_TONE, TONE_D1, 100 );
    	sendViaHandler(  400, CMD_PLAY_TONE, TONE_E1, 100 );
    	sendViaHandler(  600, CMD_PLAY_TONE, TONE_F1, 100 );
    	sendViaHandler(  800, CMD_PLAY_TONE, TONE_E1, 100 );
    	sendViaHandler( 1000, CMD_PLAY_TONE, TONE_D1, 100 );
    	sendViaHandler( 1200, CMD_PLAY_TONE, TONE_C1, 100 );
		// ku ko e tu ku ru yo
    	sendViaHandler( 1600, CMD_PLAY_TONE, TONE_E1, 100 );
    	sendViaHandler( 1800, CMD_PLAY_TONE, TONE_F1, 100 );
    	sendViaHandler( 2000, CMD_PLAY_TONE, TONE_G1, 100 );
    	sendViaHandler( 2200, CMD_PLAY_TONE, TONE_A1, 100 );
    	sendViaHandler( 2400, CMD_PLAY_TONE, TONE_G1, 100 );
    	sendViaHandler( 2600, CMD_PLAY_TONE, TONE_F1, 100 );
    	sendViaHandler( 2800, CMD_PLAY_TONE, TONE_E1, 100 );    	    	
	}

	/**
     * Sends the message via handler to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param value1 first parameter
     * @param value2 second parameter
     */
    private void sendViaHandler( int delay, int message, int value1, int value2 ) {
        mCommand.sendViaHandler( delay, message, value1, value2 );
    }

// --- comand send end ---

// --- comand recv ---
	/**
	 * execRecvFindEnd ( overwrite )
     * @param List<String> list
	 */
	protected void execRecvFindEnd( List<String> list ) {
		showDialog( list );
	}

    /**
     * execRecvViaHandler ( overwrite )
     * @param int message
     * @param int value1
	 * @param int value2
     */	
	protected void execRecvViaHandler( int message, int value1, int value2 ) {
        if ( message == CMD_PLAY_TONE ) {
			mCommand.sendPlayTone( value1, value2 );
		}
	}

// --- dialog ---
    /**
     * Shows the dialog
     * @param startStop when true shows another title (for leJOSMINDdroid)
     */     
    private void showDialog( List<String> list ) { 
        String title = mResources.getString( R.string.sound_dialog_title );    
     	showDialog( list, title );      
	}

	/**
	 * startCmdFile ( overwrite )
     * play a sound file on the NXT robot.
     * @param String name : sound file name. 
     */	
    protected void startCmdFile( String name ) {
		sendPlaySoundFile( name );
    }
		
// --- dialog end ---
	
// --- cmdHandler ---


}
