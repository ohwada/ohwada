package jp.ohwada.android.evy1;

import jp.ohwada.android.midi.MidiMsgConstants;

/*
 * Message for Evy1
 *
 * YMW820 アプリケーションノート
 * http://yamaha-webmusic.github.io/nsx1-apps/specs/ANMW820A-001-10-j.pdf
 */ 
public class Evy1Message {
		
	/*
	 * constructor
	 */ 		
	public Evy1Message() {
		// dummy
	}

	/*
	 * All Sound Off
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getAllSoundOff( int channel ) {
		byte b0 = buildCmdStatus( MidiMsgConstants.ST_CONTROL, channel );
		byte[] bytes = new byte[]{ b0, (byte)0x78, (byte)0x00 };
		return bytes;		
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
