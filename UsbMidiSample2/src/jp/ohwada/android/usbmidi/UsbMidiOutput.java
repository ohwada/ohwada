package jp.ohwada.android.usbmidi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import jp.ohwada.android.midi.MidiMsgConstants;
import jp.ohwada.android.usb.UsbOutput;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

/**
 * MIDI Output Device
 * refer http://www.usb.org/developers/devclass_docs/midi10.pdf
 */
public class UsbMidiOutput extends UsbOutput {
	
	// Code Index Number
	// Miscellaneous function codes
	private final static int CIN_MISC = 0x00;
	// Cable events
	private final static int CIN_CABLE = 0x01;
	// Two-byte System Common messages like MTC, SongSelect
	private final static int CIN_SYS_TWO = 0x02;
	// Three-byte System Common messages like SPP
	private final static int CIN_SYS_THREE = 0x03;
	// SysEx starts or continues
	private final static int CIN_SYSEX_START = 0x04;
	// Single-byte System Common Message
    // or SysEx ends with following single byte
	private final static int CIN_SYS_SINGLE = 0x05;
	private final static int CIN_SYSEX_END_SINGLE = 0x05;
	// SysEx ends with following two bytes
	private final static int CIN_SYSEX_END_TWO = 0x06;
	// SysEx ends with following three bytes
	private final static int CIN_SYSEX_END_THREE = 0x07;
	// Note-off
	private final static int CIN_NOTE_OFF = 0x08;
	// Note-on
	private final static int CIN_NOTE_ON = 0x09;
	// Poly-KeyPress
	private final static int CIN_POLY = 0x0A;
	// Control Change
	private final static int CIN_CONTROL = 0x0B;
	// Program Change
	private final static int CIN_PROGRAM = 0x0C;
	// Channel Pressure
	private final static int CIN_CHANNEL = 0x0D;
	// PitchBend Change
	private final static int CIN_PITCH = 0x0E;
	// Single Byte
	private final static int CIN_SINGLE = 0x0F;

	/**
	 * constructor
	 * @param UsbInterface _interface
	 * @param UsbEndpoint endpoint
	 * @param UsbDeviceConnection connection
	 */
	public UsbMidiOutput( UsbInterface _interface, UsbEndpoint endpoint, UsbDeviceConnection connection ) {
		super( _interface, endpoint, connection );
		TAG_SUB = "UsbMidiOutput";
		openConnection();
	}

	/**
	 * close this device.
	 */
	public void close() {
		closeConnection();
	}
				
	/**
	 * Miscellaneous Function Codes
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendMiscellaneousFunctionCodes( int cable, byte[] bytes ) {
		sendByte3( CIN_MISC, cable, bytes );
	}

	/**
	 * Cable Events
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendCableEvents( int cable, byte[] bytes ) {
		sendByte3( CIN_CABLE, cable, bytes );
	}

	/**
	 * System Common messages
	 * or SysEx ends with following single byte.
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendSystemCommonMessage( int cable, byte[] bytes ) {
		if (bytes == null) {
			return;
		}
		switch (bytes.length) {
		case 1:
			sendByte1( CIN_SYS_SINGLE, cable, bytes );
			break;
		case 2:
			sendByte2( CIN_SYS_TWO, cable, bytes );
			break;
		case 3:
			sendByte3( CIN_SYS_THREE, cable, bytes );
			break;
		default:
			// do nothing.
			break;
		}
	}

	/**
	 * System Exclusive
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendSystemExclusive( int cable, byte[] bytes ) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		for (int index = 0; index < bytes.length; index += 3) {
			if ((index + 3 < bytes.length)) {
				// sysex starts or continues...
				stream.write( buildCmdCable( cable, CIN_SYSEX_START ));
				stream.write( bytes[index + 0] );
				stream.write( bytes[index + 1] );
				stream.write( bytes[index + 2] );
			} else {
				switch (bytes.length % 3) {
				case 1:
					// sysex end with 1 byte
					stream.write( buildCmdCable( cable, CIN_SYSEX_END_SINGLE ));
					stream.write( bytes[index + 0] );
					stream.write(0);
					stream.write(0);
					break;
				case 2:
					// sysex end with 2 bytes
					stream.write( buildCmdCable( cable, CIN_SYSEX_END_TWO ));
					stream.write( bytes[index + 0] );
					stream.write( bytes[index + 1] );
					stream.write(0);
					break;
				case 0:
					// sysex end with 3 bytes
					stream.write( buildCmdCable( cable, CIN_SYSEX_END_THREE ));
					stream.write( bytes[index + 0] );
					stream.write( bytes[index + 1] );
					stream.write( bytes[index + 2] );
					break;
				}
			}
		}
		ByteBuffer buffer = ByteBuffer.wrap( stream.toByteArray() );
		int size = stream.size();
		sendByteBuffer( buffer, size );
		log_d( "SysEx " + size + " bytes" );
	}

	/**
	 * Note Off
	 * @param int cable
	 * @param int channel
	 * @param int note 
	 * @param int velocity 
	 */
	public void sendNoteOff( int cable, int channel, int note, int velocity ) {
		log_d( "sendNoteOff " + cable + " " + channel + " " + note + " " + velocity );
		byte[] bytes = new byte[3];
		bytes[0] = buildCmdStatus( MidiMsgConstants.ST_NOTE_OFF, channel );
		bytes[1] = (byte)( note & 0x00ff );
		bytes[2] = (byte)( velocity & 0x00ff );
		sendNoteOff( cable, bytes );		
	}

	/**
	 * Note Off
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendNoteOff( int cable, byte[] bytes ) {
		sendByte3( CIN_NOTE_OFF, cable, bytes );
	}

	/**
	 * Note On
	 * @param int cable
	 * @param int channel
	 * @param int note 
	 * @param int velocity 
	 */
	public void sendNoteOn( int cable, int channel, int note, int velocity ) {
		log_d( "sendNoteOn " + cable + " " + channel + " " + note + " " + velocity );
		byte[] bytes = new byte[3];
		bytes[0] = buildCmdStatus( MidiMsgConstants.ST_NOTE_ON, channel );
		bytes[1] = (byte)( note & 0x00ff );
		bytes[2] = (byte)( velocity & 0x00ff );
		sendNoteOn( cable, bytes );
	}

	/**
	 * Note On
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendNoteOn( int cable, byte[] bytes ) {
		sendByte3( CIN_NOTE_ON, cable, bytes );
	}
	
	/**
	 * Polyphonic Key Pressure
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendPolyphonicKeyPressure( int cable, byte[] bytes ) {
		sendByte3( CIN_POLY, cable, bytes );
	}

	/**
	 * Control Change
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendControlChange( int cable, byte[] bytes ) {
		sendByte3( CIN_CONTROL, cable, bytes );
	}

	/**
	 * Program Change
	 * @param int cable 
	 * @param int channel
	 * @param int number
	 */
	public void sendProgramChange( int cable, int channel, int number ) {
		byte[] bytes = new byte[2];
		bytes[0] = buildCmdStatus( MidiMsgConstants.ST_PROGRAM, channel );
		bytes[1] = (byte)( number & 0x00ff );
		sendProgramChange( cable, bytes );
	}
	
	/**
	 * Program Change
	 * @param int cable 
	 * @param byte[] bytes
	 */
	public void sendProgramChange( int cable, byte[] bytes ) {
		sendByte2( CIN_PROGRAM, cable, bytes );
	}

	/**
	 * Channel Pressure
	 * @param int cable 
	 * @param byte[] bytes
	 */
	public void sendChannelPressure( int cable, byte[] bytes ) {
		sendByte2( CIN_CHANNEL, cable, bytes );
	}

	/**
	 * Pitch Wheel Change
	 * @param int cable 
	 * @param byte[] bytes
	 */
	public void sendPitchWheelChange( int cable, byte[] bytes ) {
		sendByte3( CIN_PITCH, cable, bytes );
	}

	/**
	 * Single Byte
	 * @param int cable
	 * @param byte[] bytes
	 */
	public void sendSingleByte( int cable, byte[] bytes ) {
		sendByte1( CIN_SINGLE, cable, bytes );
	}

	/**
	 * sendByte1
	 * @param int cin
	 * @param int cable
	 * @param byte[] bytes
	 */
	private void sendByte1( int cin, int cable, byte[] bytes ) {		
		ByteBuffer buffer = ByteBuffer.allocate( 4 );
		buffer.put( buildCmdCable( cable, cin ) );
		buffer.put( bytes[0] );
		buffer.put( (byte)0 );
		buffer.put( (byte)0 );
		sendByteBuffer( buffer, 4 );
	}

	/**
	 * sendByte2
	 * @param int cin
	 * @param int cable
	 * @param byte[] bytes
	 */
	private void sendByte2( int cin, int cable, byte[] bytes ) {		
		ByteBuffer buffer = ByteBuffer.allocate( 4 );
		buffer.put( buildCmdCable( cable, cin ) );
		buffer.put( bytes[0] );
		buffer.put( bytes[1] );
		buffer.put( (byte)0 );
		sendByteBuffer( buffer, 4 );
	}

	/**
	 * sendByte3
	 * @param int cin
	 * @param int cable
	 * @param byte[] bytes
	 */
	private void sendByte3( int cin, int cable, byte[] bytes ) {		
		ByteBuffer buffer = ByteBuffer.allocate( 4 );
		buffer.put( buildCmdCable( cable, cin ) );
		buffer.put( bytes[0] );
		buffer.put( bytes[1] );
		buffer.put( bytes[2] );
		sendByteBuffer( buffer, 4 );
	}

	/**
	 * buildCmdCable
	 * @param int cable
	 * @param int cin
	 * @return byte[]
	 */
	private byte buildCmdCable( int cable, int cin ) {
		byte b = (byte)((( cable & 0x0f ) << 4 ) | ( cin & 0x0f ) );
		return b;
	}

	/**
	 * buildCmdStatus
	 * @param int status
	 * @param int channel
	 * @return byte[]
	 */
	private byte buildCmdStatus( int status, int channel ) {
		byte b = (byte)(( status & 0x00f0 ) | ( channel & 0x0f )) ;
		return b;
	}

}
