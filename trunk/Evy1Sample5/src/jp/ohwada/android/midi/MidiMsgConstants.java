package jp.ohwada.android.midi;

/**
 * MIDI Message Constants
 * 
 * http://www.midi.org/techspecs/midimessages.php
 */
public class MidiMsgConstants {

	// staus byte
	// http://www.midi.org/techspecs/midimessages.php
	// Note Off event. 
	public final static int ST_NOTE_OFF = 0x0080;
	// Note On event. 
	public final static int ST_NOTE_ON = 0x0090;
	// Polyphonic Key Pressure (Aftertouch).
	public final static int ST_POLYPHONIC  = 0x00A0;
	// Control Change
	public final static int ST_CONTROL = 0x00B0;
	// Program Change
	public final static int ST_PROGRAM = 0x00C0;
	// Channel Pressure (After-touch)
	public final static int ST_CHANNEL = 0x00D0;
	// Pitch Wheel Change
	public final static int ST_PITCH = 0x00E0;
	// System Exclusive
	public final static int ST_SYSEX = 0x00F0;

	// channel
	public final static int CH_MIN = 0;
	public final static int CH_MAX = 15;

	// note
	public final static int NOTE_MIN = 0;
	public final static int NOTE_MAX = 127;		
}				
