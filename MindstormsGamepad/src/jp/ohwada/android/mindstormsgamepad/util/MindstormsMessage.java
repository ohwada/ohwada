package jp.ohwada.android.mindstormsgamepad.util;

/**
 * Class for composing the proper messages for simple
 * communication over bluetooth
 *
 * http://mindstorms.lego.com/en-us/support/files/Advanced.aspx
 * LEGO MINDSTORMS NXT Communication protocol
 * LEGO MINDSTORMS NXT Direct commands
 */
public class MindstormsMessage {
	    
    // Command types constants. Indicates type of packet being sent or received.
    public static byte TYPE_DIRECT_COMMAND_REPLY = (byte)0x00;
    public static byte TYPE_SYSTEM_COMMAND_REPLY = (byte)0x01;
    public static byte TYPE_REPLY_COMMAND = (byte)0x02;
    public static byte TYPE_DIRECT_COMMAND_NOREPLY = (byte)0x80; // Avoids ~100ms latency
    public static byte TYPE_SYSTEM_COMMAND_NOREPLY = (byte)0x81; // Avoids ~100ms latency
    
    // System Commands:
    public static final byte CMD_OPEN_READ = (byte)0x80;
    public static final byte CMD_OPEN_WRITE = (byte)0x81;
    public static final byte CMD_READ = (byte)0x82;
    public static final byte CMD_WRITE = (byte)0x83;
    public static final byte CMD_CLOSE = (byte)0x84;
    public static final byte CMD_DELETE = (byte)0x85;        
    public static final byte CMD_FIND_FIRST = (byte)0x86;
    public static final byte CMD_FIND_NEXT = (byte)0x87;
    public static final byte CMD_GET_FIRMWARE_VERSION = (byte)0x88;
    public static final byte CMD_OPEN_WRITE_LINEAR = (byte)0x89;
    public static final byte CMD_OPEN_READ_LINEAR = (byte)0x8A;
    public static final byte CMD_OPEN_WRITE_DATA = (byte)0x8B;
    public static final byte CMD_OPEN_APPEND_DATA = (byte)0x8C;
    public static final byte CMD_BOOT = (byte)0x97;
    public static final byte CMD_ET_BRICK_NAME = (byte)0x98;
    public static final byte CMD_GET_DEVICE_INFO = (byte)0x9B;
    public static final byte CMD_DELETE_USER_FLASH = (byte)0xA0;
    public static final byte CMD_POLL_LENGTH = (byte)0xA1;
    public static final byte CMD_POLL = (byte)0xA2;
    public static final byte CMD_BLUETOOTH_FACTORY_REST = (byte)0xA4;

    // Direct Commands
    public static final byte CMD_START_PROGRAM = 0x00;
    public static final byte CMD_STOP_PROGRAM = 0x01;
    public static final byte CMD_PLAY_SOUND_FILE = 0x02;
    public static final byte CMD_PLAY_TONE = 0x03;
    public static final byte CMD_SET_OUTPUT_STATE = 0x04;
    public static final byte CMD_SET_INPUT_MODE = 0x05;
    public static final byte CMD_GET_OUTPUT_STATE = 0x06;
    public static final byte CMD_GET_INPUT_VALUES = 0x07;
    public static final byte CMD_RESET_SCALED_INPUT_VALUE = 0x08;
    public static final byte CMD_MESSAGE_WRITE = 0x09;
    public static final byte CMD_RESET_MOTOR_POSITION = 0x0A;   
    public static final byte CMD_GET_BATTERY_LEVEL = 0x0B;
    public static final byte CMD_STOP_SOUND_PLAYBACK = 0x0C;
    public static final byte CMD_KEEP_ALIVE = 0x0D;
    public static final byte CMD_LS_GET_STATUS = 0x0E;
    public static final byte CMD_LS_WRITE = 0x0F;
    public static final byte CMD_LS_READ = 0x10;
    public static final byte CMD_GET_CURRENT_PROGRAM_NAME = 0x11;
    public static final byte CMD_MESSAGE_READ = 0x13;

    // the folowing constants were taken from the leJOS project (http://www.lejos.org)     
    // NXJ additions
    public static byte CMD_NXJ_DISCONNECT = 0x20; 
    public static byte CMD_NXJ_DEFRAG = 0x21;
    
    // MINDdroidConnector additions
    public static final byte CMD_SAY_TEXT = 0x30;
    public static final byte CMD_VIBRATE_PHONE = 0x31;
    public static final byte CMD_ACTION_BUTTON = 0x32;
            
    public static final byte CMD_NXJ_FIND_FIRST = (byte)0xB6;
    public static final byte CMD_NXJ_FIND_NEXT = (byte)0xB7;
    public static final byte CMD_NXJ_PACKET_MODE = (byte)0xff;
   
    // Error codes : commnucation protocol  
    public static final byte STATUS_SUCCESS = (byte)0x00;  
    public static final byte STATUS_NO_MORE_HANDLE = (byte)0x81;  
    public static final byte STATUS_NO_SPACE = (byte)0x82;  
    public static final byte STATUS_NO_MORE_FILES = (byte)0x83;  
    public static final byte STATUS_END_OF_FILE_EXPECTED = (byte)0x84;  
    public static final byte STATUS_END_OF_FILE = (byte)0x85;  
    public static final byte STATUS_NOT_A_LINEAR_FILE = (byte)0x86;  
    public static final byte STATUS_FILE_NOT_FOUND = (byte)0x87; 
    public static final byte STATUS_HANDLE_ALL_READY_CLOSED = (byte)0x88; 
    public static final byte STATUS_NO_LINEAR_SPACE = (byte)0x89;  
    public static final byte STATUS_UNDEFINED_ERROR = (byte)0x8A; 
    public static final byte STATUS_FILE_IS_BUSY = (byte)0x8B; 
    public static final byte STATUS_NO_WRITE_BUFFERS = (byte)0x8C; 
    public static final byte STATUS_APPEND_NOT_POSSIBLE = (byte)0x8D; 
    public static final byte STATUS_FILE_IS_FULL = (byte)0x8E; 
    public static final byte STATUS_FILE_EXISTS = (byte)0x8F; 
    public static final byte STATUS_MODULE_NOT_FOUND = (byte)0x90; 
    public static final byte STATUS_OUT_OF_BOUNDARY = (byte)0x91; 
    public static final byte STATUS_ILLEGAL_FILE_NAME = (byte)0x92; 
    public static final byte STATUS_ILLEGAL_FILE_HANDLE = (byte)0x93; 
                                                                                  
    // Error codes : direct command
	public static final byte STATUS_PENDING_COMMNUCATION_TRANSACTION_IN_PROGRESS = (byte)0x20;
	public static final byte STATUS_SPECIFIED_MAILBOX_QUEUE_IS_EMPTY = (byte)0x40;
	public static final byte STATUS_REQUEST_FAILED = (byte)0xBD;		
	public static final byte STATUS_UNKNOWN_COMMAND_OPCODE = (byte)0xBE;	
	public static final byte STATUS_INSANE_PACKET = (byte)0xBF;	
	public static final byte STATUS_DATA_CONTAINS_OUT_OF_RANGE_VALUES = (byte)0xC0;
	public static final byte STATUS_COMMNUCATION_BUS_ERROR = (byte)0xDD;	
	public static final byte STATUS_NO_FREE_MEMORY_IN_COMMNUCATION_BUFFER = (byte)0xDE;	
	public static final byte STATUS_SPECIFIED_CHANNEL_COMMNUCATION_IS_NOT_VALID = (byte)0xDF;	
	public static final byte STATUS_SPECIFIED_CHANNEL_COMMNUCATION_NOT_CONFIGURED_OR_BUSY = (byte)0xE0;		
	public static final byte STATUS_NO_ACTIVE_PROGRAM = (byte)0xEC;	
	public static final byte STATUS_ILLEGAL_SIZE_SPECIFIED = (byte)0xED;
	public static final byte STATUS_ILLEGAL_MAILBOX_QUEUE_ID_SPECIFIED = (byte)0xEE;
	public static final byte STATUS_ATTEMPTED_TO_ACCESS_INVALID_FIELD_OF_A_STRUCTURE = (byte)0xEF;
	public static final byte STATUS_BAD_INPUT_OR_OUTPUT__SPECIFIED = (byte)0xF0;
	public static final byte STATUS_INSUFFICIENT_MEMORY_AVAILABLE = (byte)0xFB;
	public static final byte STATUS_BAD_ARGUMENTS = (byte)0xFF;

    // Error codes
    public static final byte STATUS_DIRECTORY_FULL = (byte) 0xFC;
    public static final byte STATUS_NOT_IMPLEMENTED = (byte) 0xFD;
	
    // Firmware codes
    public static byte[] FIRMWARE_VERSION_LEJOSMINDDROID = { 0x6c, 0x4d, 0x49, 0x64 };

    private static final byte CHAR_NULL = (byte) 0x00;
    
	// motor
    protected static final int MOTOR_A = 0;
    protected static final int MOTOR_B = 1;
    protected static final int MOTOR_C = 2;
    
    protected static final int MAX_SPEED = 100;

	// motor assign for standard type            
	private static int mMotorLeft = MOTOR_C;
	private static int mMotorRight = MOTOR_B;
//	private int mMotorAction = MOTOR_A;
	
	/*
	 * === Constractor ===
	 */  
	protected MindstormsMessage() {
		// dummy
	}

/*
 * ==============================
 * SetOutputState
 * ==============================
 */	

	/**
	 * Sends the motor control values to the communcation thread.
	 * @param left The power of the left motor from 0 to 100.
	 * @param rigth The power of the right motor from 0 to 100.
	 * @return byte[]
	 */   
	public static byte[] cmdMove( int left, int right ) {                        
		byte[] byte1 = cmdSetOutputState( 
			TYPE_DIRECT_COMMAND_NOREPLY, mMotorLeft, left );
		byte[] byte2 = cmdSetOutputState( 
			TYPE_DIRECT_COMMAND_NOREPLY, mMotorRight, right );
		byte[] byte3 = mergeBytes( byte1, byte2 );
		return byte3;
	}

	/**
	 * Sends stop to the motor control.
	 * reply from mindstorms
	 * @return byte[]
	 */  
	public static byte[] cmdStop() {                        
		byte[] byte1 = cmdSetOutputState( 
			TYPE_DIRECT_COMMAND_REPLY, mMotorLeft, 0 );
		byte[] byte2 = cmdSetOutputState( 
			TYPE_DIRECT_COMMAND_REPLY, mMotorRight, 0 );
		byte[] byte3 = mergeBytes( byte1, byte2 );
		return byte3;
	}

    /**
     * cmdMoveLimit
     * @param int motor
     * @param int end     
	 * @return byte[]
	 */	
    public static byte[] cmdMoveLimit( int motor, int speed, int end ) {
		return cmdSetOutputState( 
			TYPE_DIRECT_COMMAND_NOREPLY, motor, speed, end );
    }

	/**
 	 * SetOutputState
     * @param byte type 
     * @param int motor
     * @param int speed        
     * @param int end
	 * @return byte[]
	 */
    public static byte[] cmdSetOutputState( byte type, int motor, int speed, int end )  {
		if ( speed > MAX_SPEED ) speed = MAX_SPEED;
		if ( speed < - MAX_SPEED ) speed = - MAX_SPEED;
        byte[] message = cmdSetOutputState( type, motor, speed );
        // TachoLimit
        message[8] = (byte) end;
        message[9] = (byte) (end >> 8);
        message[10] = (byte) (end >> 16);
        message[11] = (byte) (end >> 24);
		return addHeaderToMessage( message );
    }

	/**
 	 * SetOutputState
     * @param byte type 
     * @param int motor
     * @param int speed        
	 * @return byte[]
	 */    
    public static byte[] cmdSetOutputState( byte type, int motor, int speed ) {
        byte[] message = new byte[12];
        message[0] = type;
        message[1] = CMD_SET_OUTPUT_STATE;
        // Output port
        message[2] = (byte) motor;
        if (speed == 0) {
            message[3] = 0;
            message[4] = 0;
            message[5] = 0;
            message[6] = 0;
            message[7] = 0;
        } else {
            // Power set option (Range: -100 - 100)
            message[3] = (byte) speed;
            // Mode byte (Bit-field): MOTORON + BREAK
            message[4] = 0x03;
            // Regulation mode: REGULATION_MODE_MOTOR_SPEED
            message[5] = 0x01;
            // Turn Ratio (SBYTE; -100 - 100)
            message[6] = 0x00;
            // RunState: MOTOR_RUN_STATE_RUNNING
            message[7] = 0x20;
        }
        // TachoLimit: run forever
        message[8] = 0;
        message[9] = 0;
        message[10] = 0;
        message[11] = 0;
		return addHeaderToMessage( message );
    }

/*
 * ==============================
 * Direct Commands
 * ==============================
 */
 
	/**
 	 * StartProgram
     * @param String  programName
	 * @return byte[]
	 */
    public static byte[] cmdStartProgram( String programName ) {
        byte[] message1 = new byte[ 22 ];
        message1[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message1[1] = CMD_START_PROGRAM;
      // copy programName and end with null delimiter   
    	byte[] message = copyToMessageWithNull( message1, 2,  programName );
		return addHeaderToMessage( message );
    }

	/**
 	 * StopProgram
	 * @return byte[]
	 */
    public static byte[] cmdStopProgram() {
        byte[] message = new byte[2];
        message[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message[1] = CMD_STOP_PROGRAM;
		return addHeaderToMessage( message );
    }

	/**
 	 * PlaySoundFile
     * @param boolean loop
     * @param String fileName
	 * @return byte[]
	 */
    public static byte[] cmdPlaySoundFile( boolean loop, String fileName ) {
        byte[] message1 = new byte[ 23 ];
        message1[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message1[1] = CMD_PLAY_SOUND_FILE;
        message1[2] = (byte) (loop ? 0x01 : 0x00) ; 
       // copy fileName and end with null delimiter     
    	byte[] message = copyToMessageWithNull( message1, 3, fileName );
		return addHeaderToMessage( message );
    }
    
	/**
 	 * PlayTone
     * @param int frequency
     * @param int duration    
	 * @return byte[]
	 */
    public static byte[] cmdPlayTone( int frequency, int duration ) {
        byte[] message = new byte[6];
        message[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message[1] = CMD_PLAY_TONE;
        // Frequency for the tone, Hz (UWORD); Range: 200-14000 Hz
        message[2] = (byte) frequency;
        message[3] = (byte) (frequency >> 8);
        // Duration of the tone, ms (UWORD)
        message[4] = (byte) duration;
        message[5] = (byte) (duration >> 8);
		return addHeaderToMessage( message );
    }

	/**
 	 * GetOutputState
     * @param int motor
	 * @return byte[]
	 */ 
    public static byte[] cmdGetOutputState( int motor ) {
        byte[] message = new byte[3];
        message[0] = TYPE_DIRECT_COMMAND_REPLY;
        message[1] = CMD_GET_OUTPUT_STATE;
        // Output port
        message[2] = (byte) motor;
		return addHeaderToMessage( message );
    }

	/**
 	 * cmdGetInputValues
     * @param int port
	 * @return byte[]
	 */ 
    public static byte[] cmdGetInputValues( int port ) {
        byte[] message = new byte[3];
        message[0] = TYPE_DIRECT_COMMAND_REPLY;
        message[1] = CMD_GET_INPUT_VALUES;
        // Output port
        message[2] = (byte) port;
		return addHeaderToMessage( message );
    }

	/**
 	 * ResetMotorPosition
     * @param int motor
	 * @return byte[]
	 */
    public static byte[] cmdResetMotorPosition( int motor ) {
        byte[] message = new byte[4];
        message[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message[1] = CMD_RESET_MOTOR_POSITION;
        // Output port
        message[2] = (byte) motor;
        // absolute position
        message[3] = 0;
		return addHeaderToMessage( message );
    }

	/**
 	 * GetBatteryLevel 
	 * @return byte[]
	 */     
    public static byte[] cmdGetBatteryLevel() {
        byte[] message = new byte[2];
		message[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message[1] = CMD_GET_BATTERY_LEVEL;
		return addHeaderToMessage( message );
    }

	/**
 	 * GetCurrentProgramName
	 * @return byte[]
	 */    
    public static byte[] cmdGetCurrentProgramName() {
        byte[] message = new byte[2];
        message[0] = TYPE_DIRECT_COMMAND_REPLY;
        message[1] = CMD_GET_CURRENT_PROGRAM_NAME;
		return addHeaderToMessage( message );
    }

/*
 * ==============================
 * System Commands
 * ==============================
 */

	/**
 	 * OpenWrite
     * @param String fileName
     * @param int fileLength    
	 * @return byte[]
	 */ 
    public static byte[] cmdOpenWrite( String fileName, int fileLength ) {
        byte[] message1 = new byte[26];
        message1[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message1[1] = CMD_OPEN_WRITE;        
        // copy fileName and end with null delimiter
		byte[] message = copyToMessageWithNull( message1, 2, fileName );
		
//        for (int pos=0; pos<fileName.length(); pos++) {
//            message[2+pos] = (byte) fileName.charAt(pos);
//		}
//        message[fileName.length()+2] = 0;

        // copy file size
        message[22] = (byte) fileLength;
        message[23] = (byte) (fileLength >>> 8);
        message[24] = (byte) (fileLength >>> 16);
        message[25] = (byte) (fileLength >>> 24);        
		return addHeaderToMessage( message );
    }

	/**
 	 * Write
     * @param int handle
     * @param byte[] data
	 * @param int dataLength      
	 * @return byte[]
	 */ 
    public static byte[] cmdWrite( int handle, byte[] data, int dataLength ) {
        byte[] message = new byte[ dataLength + 3 ];
        message[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message[1] = CMD_WRITE;
        // copy handle
        message[2] = (byte) handle;
        // copy data
        System.arraycopy( data, 0, message, 3, dataLength );
		return addHeaderToMessage( message );
    }

	/**
 	 * Close
     * @param int handle    
	 * @return byte[]
	 */     
    public static byte[] cmdClose( int handle ) {
        byte[] message = new byte[3];
        message[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message[1] = CMD_CLOSE;        
        // copy handle
        message[2] = (byte) handle;
		return addHeaderToMessage( message );
    }

	/**
 	 * Delete
     * @param String fileName 
	 * @return byte[]
	 */ 
    public static byte[] cmdDelete( String fileName ) {
        byte[] message1 = new byte[22];
        message1[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message1[1] = CMD_DELETE;        
        // copy fileName and end with null delimiter
		byte[] message = copyToMessageWithNull( message1, 2, fileName );
		return addHeaderToMessage( message );
    }
    
	/**
 	 * FindFirst
     * @param String searchString
	 * @return byte[]
	 */ 
    public static byte[] cmdFindFirst( String searchString ) {
        byte[] message1 = new byte[ 22 ];
        message1[0] = TYPE_SYSTEM_COMMAND_REPLY;
		message1[1] = CMD_FIND_FIRST;
		// copy searchString and end with null delimiter
		byte[] message = copyToMessageWithNull( message1, 2, searchString );
		return addHeaderToMessage( message );
    }

	/**
 	 * FindNext
     * @param int handle
	 * @return byte[]
	 */ 
    public static byte[] cmdFindNext( int handle ) {
        byte[] message = new byte[3];
        message[0] = TYPE_SYSTEM_COMMAND_REPLY;
		message[1] = CMD_FIND_NEXT;
		message[2] = (byte) handle;
		return addHeaderToMessage( message );
    }

	/**
 	 * GetFirmwareVersion
	 * @return byte[]
	 */ 
    public static byte[] cmdGetFirmwareVersion() {
        byte[] message = new byte[2];
        message[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message[1] = CMD_GET_FIRMWARE_VERSION;
		return addHeaderToMessage( message );
    }

	/**
 	 * GetDeviceInfo 
	 * @return byte[]
	 */     
    public static byte[] cmdGetDeviceInfo() {
        byte[] message = new byte[2];
		message[0] = TYPE_SYSTEM_COMMAND_REPLY;
        message[1] = CMD_GET_DEVICE_INFO;
		return addHeaderToMessage( message );
    } 
    
/*
 * ==============================
 * MINDdroidConnector
 * ==============================
 */

	/**
 	 * ActionButton
     * @param int action   
	 * @return byte[]
	 */    
    public static byte[] cmdActionButton( int action ) {
        byte[] message = new byte[3];
        message[0] = TYPE_DIRECT_COMMAND_NOREPLY;
        message[1] = CMD_ACTION_BUTTON;
        message[2] = (byte) action;
		return addHeaderToMessage( message );
    }   

/*
 * -----------------------------------------------------
 * private
 * -----------------------------------------------------
 */
    
	/**
 	 * copy String to Message and end with null delimiter
 	 * @param byte[] message;
 	 * @param int offset
 	 * @param String name
 	 * @return byte[]
	 */
    private static byte[] copyToMessageWithNull( byte[] message, int offset, String name ) {
    	int max = message.length - 1;
    	int length = name.length();
   		if ( length > max ) {
    		length = max;
    	}    
        // copy name
        for ( int i=0; i < length; i++ ) {
            message[ offset + i ] = (byte) name.charAt( i );
        }
        // last char is null    
        message[ offset + length ] = CHAR_NULL;
		return message;
    }
    
    /**
     * addHeaderToMessage
     * @param message, the message as a byte array
	 * @return byte[] 
     */
    private static byte[] addHeaderToMessage( byte[] message )  {
		byte[] header = getHeader( message );
		return mergeBytes( header, message );		
    }  

    /**
     * get header bytes
	 * @param message, the message as a byte array
	 * @return byte[] 
     */
	private static byte[] getHeader( byte[] message ) {
		int value = message.length;
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (value & 0xff);
		value = value >> 8;
		bytes[1] = (byte) (value & 0xff);
		return bytes;
	}

    /**
     * merge two byte array
	 * @param byte[] bytes1
	 * @param byte[] bytes2
	 * @return byte[] 
     */	
	private static byte[] mergeBytes( byte[] bytes1, byte[] bytes2 ) {
        byte[] bytes3 = new byte[ bytes1.length + bytes2.length ];
        System.arraycopy( bytes1, 0, bytes3, 0, bytes1.length ); 
        System.arraycopy( bytes2, 0, bytes3, bytes1.length, bytes2.length );
        return bytes3;
	}
   
}
