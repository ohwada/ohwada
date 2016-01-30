package jp.ohwada.android.mindstormsgamepad;

import java.util.List;

import jp.ohwada.android.mindstormsgamepad.util.MindstormsCommand;
import jp.ohwada.android.mindstormsgamepad.util.MindstormsMessage;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Program Activity
 */
public class ProgramActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "ProgramActivity";

	private static final int CMD_START_PROGRAM = MindstormsCommand.CMD_START_PROGRAM;
			
//	private static boolean START_STOP_ON = true;
	private static boolean START_STOP_OFF = false;

	// local variable
    private String mProgramToStart = "";
        
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
		View view = getLayoutInflater().inflate( R.layout.activity_program, null );
		setContentView( view ); 		

		/* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_program );
		initButtonBack();
                
		/* Program button */
        Button btnProgram = (Button) findViewById( R.id.button_program );
        btnProgram.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// first comannd
				sendFindFirst();
			}
        });

		// first comannd
		sendFindFirst();                
    }
// --- onCreate end --

// --- comand send ---    
    /**
	 * cmd Send FindFiles
	 */	
	private void sendFindFirst() {
		mCommand.sendFindFirstProgram();  
	}

    /**
     * GetProgramName
     */	
	private void sendGetProgramName() {
		byte[] cmd = MindstormsMessage.cmdGetCurrentProgramName();
		sendCmdMassage( cmd ); 
	} 

    /**
     * StartProgram
     */	
	private void sendStartProgram( String name ) {
		byte[] cmd = MindstormsMessage.cmdStartProgram( name );
		sendCmdMassage( cmd ); 	
	}

    /**
     * StopProgram
     */	
	private void sendStopProgram( ) {
		byte[] cmd = MindstormsMessage.cmdStopProgram();
		sendCmdMassage( cmd ); 	
	}
   
    /**
     * setProgramToStart
     */	
    private void setProgramToStart( String name ) {
    	mProgramToStart = name;
	}

	 /**
     * Depending on the status (whether the program runs already) 
     * we stop it, wait and restart it again.
     * @param status The current status, 0x00 means that the program is already running.
     */   
    private void startRXEprogram( byte status ) {
    	// the program is already running
        if ( status == 0x00 ) {
        	sendStopProgram();
        	sendViaHandler( 1000, CMD_START_PROGRAM, mProgramToStart );
        }  else {
        	sendStartProgram( mProgramToStart );
        }
    }

    /**
     * Sends the message via handler to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param String a String parameter
     */       
    void sendViaHandler( int delay, int message, String name ) {
    	mCommand.sendViaHandler( delay, message, name );
	}
// --- comand send end ---
	
// --- comand recv ---
	/**
	 * execRecvCmdMessage ( overwrite )
	 * @param byte[] bytes  
	 */
	 protected void execRecvCmdMessage( byte[] bytes ) {	
		int reply = mMindstormsCommand.getReply( bytes );
	 	switch ( reply ) {
			case MindstormsCommand.REPLY_GET_CURRENT_PROGRAM_NAME:
				recvProgram( bytes );	
				break;				
		}
	}

	/**
	 * execRecvFindEnd ( overwrite )
	 */
	protected void execRecvFindEnd( List<String> list ) {
		showDialog( list, START_STOP_OFF );
	}

     /**
     * execRecvViaHandler ( overwrite )
     * @param int message
     * @param String name
     */	
	protected void execRecvViaHandler( int message, String name ) {
        if ( message ==  CMD_START_PROGRAM ) {
            sendStartProgram( name );
		} 
	}

	/**
	 * receive Program
	 * @param byte[] bytes
	 */
	private void recvProgram( byte[] bytes ) {
		startRXEprogram( bytes[2] );
	}

// --- comand recv end ---

// --- dialog ---
    /**
     * Shows the dialog
     * @param startStop when true shows another title (for leJOSMINDdroid)
     */     
    private void showDialog( List<String> list, boolean startStop ) { 
        int id = startStop ? R.string.file_dialog_title_1 : R.string.file_dialog_title_2;
        String title = mResources.getString( id );    
     	showDialog( list, title );
	}

	/**
	 * startCmdFile ( overwrite )
     * Starts a program on the NXT robot.
     * @param name The program name to start. 
     * Has to end with .rxe on the LEGO firmware and with .nxj on the leJOS NXJ firmware.
     */	
	protected void startCmdFile( String name ) {
        // for .rxe mFileNames: 
        // get program name, 
        // eventually stop this and start the new one delayed is handled in startRXEprogram()
        if (name.endsWith(".rxe")) {
            setProgramToStart( name );        
			sendGetProgramName();
            return;
        }
              
        // for .nxj mFileNames: 
        // stop bluetooth communication after starting the program
        if (name.endsWith(".nxj")) {
			sendStartProgram( name );
            stopService();
            return;
        }        

        // for all other mFileNames: 
        // just start the program
		sendStartProgram( name );
    }
		
// --- dialog end ---

// --- recvMessage ---

 
}
