package jp.ohwada.android.evy1sample2;

/**
 * MidiMessage
 */
public class MidiMessage {
	public int status = 0;
	public byte[] bytes = null;
	public long time = 0;

	/**
	 * constractor
	 * @param int _status
	 * @param byte[] _bytes
	 * @param long _time
	 */		
	public MidiMessage( int _status, byte[] _bytes, long _time ) {
		status = _status;
		bytes = _bytes;
		time = _time;
	}		
}				
