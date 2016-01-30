package jp.ohwada.android.mindstormsgamepad.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.util.Log;

/**
 * This class is for talking to a LEGO NXT robot via bluetooth.
 * The communciation to the robot is done via LCP (LEGO communication protocol).
 * Objects of this class can either be run as standalone thread or controlled
 * by the owners, i.e. calling the send/recive methods by themselves.
 */
public class MindstormsCommand extends MindstormsMessage {

	/** Debug */
	private static final boolean D = Constant.DEBUG;
    private static final String TAG = Constant.TAG; 
    protected String TAG_SUB = "MindstormsCommand";
    
	public static final int REPLY_SET_OUTPUT_STATE = CMD_SET_OUTPUT_STATE & 0xFF;
    public static final int REPLY_GET_OUTPUT_STATE = CMD_GET_OUTPUT_STATE & 0xFF;
    public static final int REPLY_GET_INPUT_VALUES = CMD_GET_INPUT_VALUES & 0xFF;
    public static final int REPLY_GET_FIRMWARE_VERSION = CMD_GET_FIRMWARE_VERSION & 0xFF;  
	public static final int REPLY_GET_CURRENT_PROGRAM_NAME = CMD_GET_CURRENT_PROGRAM_NAME & 0xFF;
	public static final int REPLY_GET_DEVICE_INFO = CMD_GET_DEVICE_INFO & 0xFF;
    public static final int REPLY_GET_BATTERY_LEVEL = CMD_GET_BATTERY_LEVEL & 0xFF;
	public static final int REPLY_SAY_TEXT = CMD_SAY_TEXT & 0xFF;
	public static final int REPLY_VIBRATE_PHONE = CMD_VIBRATE_PHONE & 0xFF;

    public static final int REPLY_NONE = 1000;
    public static final int REPLY_FIND_FILES = 1007;
    public static final int REPLY_FIND_FILES_ERROR = 1034;

    // this is the only OUI registered by LEGO
    // see http://standards.ieee.org/regauth/oui/index.shtml
    public static final String OUI_LEGO = "00:16:53";
    
	public static final int MAX_POWER = MAX_SPEED;
	public static final int SEEK_POWER_DEFAULT = 70;
	public static final float SEEK_POWER_SUB_RATIO = 0.7f;
    public static final int ORIENTATION_MIN_POWER = 50;
    public static final int ORIENTATION_MIN_SUB_POWER = 50;

    public static final boolean FIND_FILES_FIRST = true;
    public static final boolean FIND_FILES_NEXT = false;
    public static final int FIND_FILES_FIRST_HANDLER = 0;
        
    private static final int HEADER_LENGTH = 2;

	/*
	 * === Constractor ===
	 */                 
    public MindstormsCommand() {
    	// dummy
    }

    /**
     * Receives a message on the opened InputStream
	 * @param message, the message as a byte array
     * @return ArrayList<byte[]>
     */                
	public ArrayList<byte[]> getReceiveMessageList( byte[] message )  {
    	ArrayList<byte[]> list = new ArrayList<byte[]>();
    	int size = message.length;
    	int start = 0;
    	// loop if next message
    	while ( size > 0 ) {
			int length = toUWORD( message, start );
			int total = HEADER_LENGTH +  length;
			// if message buffer has valid data
			if ( size >= total ) {
				int body_start = start + HEADER_LENGTH;
				byte type = message[ body_start ];
				// if valid type
				if (( type == TYPE_REPLY_COMMAND ) ||
			    	( type == TYPE_DIRECT_COMMAND_NOREPLY )) {
        			byte[] body = new byte[ length ];
        			System.arraycopy( message, body_start, body, 0,  length );
        			list.add( body );
        		}
	        }
        	start += total;
        	size -= total;
        }
        return list;
    }    

    /**
     * getReply
     * @param byte[] message
     * @return int
     */	
    public int getReply( byte[] message ) {
		int reply = REPLY_NONE;
		int length = message.length;
        switch ( message[1] ) {
            case CMD_SET_OUTPUT_STATE:
                reply = REPLY_SET_OUTPUT_STATE;   
                break;
            case CMD_GET_OUTPUT_STATE:
                if ( length >= 25 ) {
                    reply = REPLY_GET_OUTPUT_STATE;
                }     
                break;
            case CMD_GET_FIRMWARE_VERSION:
                if ( length >= 7 ) {
                    reply = REPLY_GET_FIRMWARE_VERSION;
                }    
                break;
            case CMD_FIND_FIRST:
            case CMD_FIND_NEXT:
				if ( length >= 3 ) {
					reply = REPLY_FIND_FILES_ERROR;
				}
                if ( length >= 28 ) {
                    // Success
                    if ( isStatusSuccess( message ) ) {
                        reply = REPLY_FIND_FILES;
					}   
                }
                break;                
            case CMD_GET_CURRENT_PROGRAM_NAME:
                if ( length >= 23 ) {
                    reply = REPLY_GET_CURRENT_PROGRAM_NAME;
                }                
                break;                
            case CMD_SAY_TEXT:                
                if ( length == 22 ) {
                    reply = REPLY_SAY_TEXT;
                }                
            case CMD_VIBRATE_PHONE:
                if ( length == 3 ) {
                    reply = REPLY_VIBRATE_PHONE;
                }
			case CMD_GET_DEVICE_INFO:
                if ( length == 33 ) {
                    reply = REPLY_GET_DEVICE_INFO;
                }    
                break; 
			case CMD_GET_BATTERY_LEVEL:
                if ( length >= 4 ) {
                    reply = REPLY_GET_BATTERY_LEVEL;
                }    
                break;                               
			case CMD_GET_INPUT_VALUES:			
                if ( length >= 16 ) {
                    reply = REPLY_GET_INPUT_VALUES;
                }    
                break;
        }
        return reply;
    }
 
    /**
     * GetOutputState
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvGetOutputState( byte[] bytes ) {
		RecvMessage msg = new RecvMessage();
		// 2 Status
		msg.status = toStatus( bytes );
		// 3 Ouput port
		msg.GetOutputState_output_port = toUBYTE( bytes, 3 );
		// 4 Power set point
		msg.GetOutputState_power_point = toSBYTE( bytes, 4 );
		// 5 Mode
		msg.GetOutputState_mode = toUBYTE( bytes, 5 );
		// 6 Regulation mode (UBYTE)
		msg.GetOutputState_regulation_mode = toUBYTE( bytes, 6 );
		// 7 Turn ration (SBYTE)
		msg.GetOutputState_turn_ratio = toSBYTE( bytes, 7 );
		// 8 Run status (UBYTE)
		msg.GetOutputState_run_status = toUBYTE( bytes, 8 );
		// 9-12 Tacho Limit (ULONG)
		msg.GetOutputState_tacho_limit = toULONG( bytes, 9 );
		// 13-15 Tacho Count (SLONG)
		msg.GetOutputState_tacho_count = toSLONG( bytes, 13 );
		// 17-20 Block Tacho Count (SLONG)
		msg.GetOutputState_block_tacho_count = toSLONG( bytes, 17 );
		// 21-24 Rotation Count (SLONG)
		msg.GetOutputState_rotation_count = toSLONG( bytes, 21 );
        return msg;
    }
    
    /**
     * FirmwareVersion
	 * 02 88 00 7C 01 1C 01
	 * 
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvFirmwareVersion( byte[] bytes ) {
		int status = toStatus( bytes );
		String protocol = "";
		String firmware = "";
		boolean isLejosMindDroid = false;
		if ( status == MindstormsMessage.STATUS_SUCCESS ) {
			protocol = toUBYTE( bytes, 4 ) + "." + toUBYTE( bytes, 3 );
			firmware = toUBYTE( bytes, 6 ) + "." + toUBYTE( bytes, 5 );
			isLejosMindDroid = true;
			for ( int pos=0; pos<4; pos++ ) {
				if ( bytes[ pos + 3 ] != MindstormsMessage.FIRMWARE_VERSION_LEJOSMINDDROID[pos]) {
					isLejosMindDroid = false;
					break;
				}
			} 
	 	}
	 	RecvMessage msg = new RecvMessage();
	 	msg.status = status;
	 	msg.FirmwareVersion_protocol = protocol;
		msg.FirmwareVersion_firmware = firmware;
		msg.isLejosMindDroid = isLejosMindDroid;
        return msg;
    }

    /**
     * FindFiles
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvFindFiles( byte[] bytes ) {
		RecvMessage msg = new RecvMessage();
		// 2 status
		msg.status = toStatus( bytes );
		// 3 handle number
		msg.FindFiles_handle_number = toUBYTE( bytes, 3 );
		// 4-23 the name of the file format : null terminator
		msg.FindFiles_name = toStringNullTerminator( bytes, 4, 23 );
		// 24-27 file size
		msg.FindFiles_file_size = toULONG( bytes, 24 );
        return msg;
    }
		
    /**
     * ProgramName
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvProgramName( byte[] bytes ) {
		RecvMessage msg = new RecvMessage();
		msg.ProgramName_name = bytes[2];
		return msg;
	}

    /**
     * SayText
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvSayText( byte[] bytes ) {
		// evaluate control byte 
		byte controlByte = bytes[2];
		// BIT7: Language
		Locale language = null;
		if ((controlByte & 0x80) == 0x00) {
 			language = Locale.US;
		} else {
			language = Locale.getDefault();
		}	
		// BIT6: Pitch
		float pitch = 0f;
		if ((controlByte & 0x40) == 0x00) {
			pitch = 1.0f;
		} else {
			pitch = 0.75f;
		}	
		// BIT0-3: Speech Rate
		float rate = 0f;   
		switch (controlByte & 0x0f) {
			case 0x01: 
				rate = 1.5f;
				break;                                 
			case 0x02: 
				rate = 0.75f;
				break;
			default: 
				rate = 1.0f;
				break;
		}
		String ttsText = new String( bytes, 3, 19 );
		ttsText = ttsText.replaceAll("\0","");
		RecvMessage msg = new RecvMessage();		
		msg.SayText_language = language;
		msg.SayText_pitch = pitch;	
		msg.SayText_rate = rate;	
		msg.SayText_text = ttsText;	
		return msg;
	}

    /**
     * VibratePhone
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvVibratePhone( byte[] bytes ) {
		RecvMessage msg = new RecvMessage();	
		msg.VibratePhone_vibrate = bytes[2] * 10;
		return msg;
	}
                                                
    /**
     * BatteryLevel
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvBatteryLevel( byte[] bytes ) {
		int voltage = bytes[3];
		if ( bytes.length > 4 ) {
			voltage += bytes[4] << 8;
		}
		RecvMessage msg = new RecvMessage();
		msg.BatteryLevel_status = bytes[2];
		msg.BatteryLevel_voltage = voltage;
        return msg;
    }
                                         
    /**
     * DeviceInfo
	 * @param byte[] bytes
     * @return RecvMessage
     */	
    public RecvMessage getRecvDeviceInfo( byte[] bytes ) {
		RecvMessage msg = new RecvMessage();
		// 2 status
		msg.status = toStatus( bytes );
		// 3-17 NTX name :  null terminator
		msg.DeviceInfo_name = toStringNullTerminator( bytes, 3, 17 );
		//18-24  Bluetooth address
		byte[] address = toBytes( bytes, 18, 24 );
		msg.DeviceInfo_address = bytesToHexString( address, ":" );
		// 25-28 Bluetooth signal strength
		msg.DeviceInfo_signal = toULONG( bytes, 25 );
		// 29-32 Flash free use
		msg.DeviceInfo_flash = toULONG( bytes, 29 );
        return msg;
    }

    /**
     * Status
	 * @param byte[] bytes
     * @return int
     */	
	private int toStatus( byte[] bytes ) {
		return toUBYTE( bytes, 2 );
	}

    /**
     * Status Success
	 * @param byte[] bytes
     * @return boolean
     */	
	public boolean isStatusSuccess( byte[] bytes ) {
		return isStatusSuccess( toStatus( bytes ) );
	}

    /**
     * Status Success
	 * @param RecvMessage recv
     * @return boolean
     */	
	public boolean isStatusSuccess( RecvMessage recv ) {
		return isStatusSuccess( recv.status );
	}

    /**
     * Status Success
	 * @param int status
     * @return boolean
     */	
	public boolean isStatusSuccess( int status ) {
		boolean is = ( status == MindstormsMessage.STATUS_SUCCESS ) ? true : false;
		return is;
	}
		
    /**
     * Boolean
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */		
//	private boolean toBoolean( byte[] bytes, int offset ) {
//		return bytes[offset] != 0;
//	}

    /**
     * SBYTE
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
	private int toSBYTE( byte[] bytes, int offset ) {
		return bytes[offset];
	}

    /**
     * UBYTE
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
	private int toUBYTE(byte[] bytes, int offset) {
		return bytes[offset] & 0xFF;
	}

    /**
     * SWORD
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
//	private int toSWORD(byte[] bytes, int offset) {
//		return (bytes[offset] & 0xFF) |
//			(bytes[offset + 1] << 8);
//	}

    /**
     * UWORD
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
	private int toUWORD(byte[] bytes, int offset) {
		return (bytes[offset] & 0xFF) |
			((bytes[offset + 1] & 0xFF) << 8);
	}

    /**
     * SLONG
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
	private int toSLONG(byte[] bytes, int offset) {
		return (bytes[offset] & 0xFF) |
			((bytes[offset + 1] & 0xFF) << 8) |
			((bytes[offset + 2] & 0xFF) << 16) |
			(bytes[offset + 3] << 24);
	}

    /**
     * ULONG
	 * @param byte[] bytes
	 * @param int offset
     * @return int
     */
	private long toULONG(byte[] bytes, int offset) {
		return (bytes[offset] & 0xFFL) |
			((bytes[offset + 1] & 0xFFL) << 8) |
			((bytes[offset + 2] & 0xFFL) << 16) |
			((bytes[offset + 3] & 0xFFL) << 24);
	}

    /**
     * String NullTerminator
	 * @param byte[] bytes
	 * @param int offset
	 * @param int end
     * @return String
     */
	private String toStringNullTerminator( byte[] bytes, int offset, int end ) {
		int count = end - offset;
		String str = toString( bytes, offset, count );
		str = str.replaceAll( "\0", "" );
		return str;
	}
			
    /**
     * String
	 * @param byte[] bytes
	 * @param int offset
	 * @param int count
     * @return String
     */	
	private String toString( byte[] bytes, int offset, int count ) {
		try {
			return new String( bytes, offset, count, "ISO-8859-1" );
		} catch ( UnsupportedEncodingException e ) {
			return new String( bytes, offset, count );
		}
	}
	
    /**
     * Bytes
	 * @param byte[] bytes
	 * @param int offset
	 * @param int end
     * @return byte[]
     */	
	private byte[] toBytes( byte[] bytes, int offset, int end ) {
		int length = end - offset;
        byte[] ret = new byte[ length ];
        System.arraycopy( bytes, offset, ret, 0, length ); 
        return ret;
	}
	
	/**
	 * bytes To HexString
	 * @param byte[] bytes
	 * @param String glue
     * @return String
	 */ 
	private String bytesToHexString( byte[] bytes, String glue ) {
		String str = "";
		for ( int i=0; i < bytes.length ; i++ ) {
			str += byteToHexString( bytes[ i ] );
			str += glue;
		}
		return str;
	}

	/**
	 * byte To HexString
	 * @param byte b
	 * @return String
	 */ 
	public String byteToHexString( byte b ) {
		return String.format( "%02X", b );
	}
	 	        
    /**
     * === class RecvMessage ===
     */	
	public class RecvMessage {
		public int status = 0;
		public int GetOutputState_output_port = 0;
		public int GetOutputState_power_point = 0;
		public int GetOutputState_mode = 0;
		public int GetOutputState_regulation_mode = 0;
		public int GetOutputState_turn_ratio = 0;
		public int GetOutputState_run_status = 0;
		public long GetOutputState_tacho_limit = 0;
		public long GetOutputState_tacho_count = 0;
		public long GetOutputState_block_tacho_count = 0;
		public long GetOutputState_rotation_count = 0;
		public String FirmwareVersion_protocol = "";
		public String FirmwareVersion_firmware = "";
		public boolean isLejosMindDroid = false;		
		public String FindFiles_name = "";
		public int FindFiles_handle_number = 0;
		public long FindFiles_file_size = 0;
		public int ProgramName_name = 0;
		public Locale SayText_language = null;	
		public float SayText_pitch = 0f;	
		public float SayText_rate = 0f;	
		public String SayText_text = "";
		public int VibratePhone_vibrate = 0;
		public int BatteryLevel_status = 0;											
		public int BatteryLevel_voltage = 0;	
		public String DeviceInfo_name = "";
		public String DeviceInfo_address = "";
		public long DeviceInfo_signal = 0;
		public long DeviceInfo_flash = 0;
	}	

	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}   
}
