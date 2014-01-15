package jp.ohwada.android.midi;

/**
 * １つの MIDI メッセージを格納するクラス
 */
public class MidiMessage {

	// トラック番号
	public int track = 0;
	// ステータス・コード
	public int status = 0;
	// MIDIのバイト列
	public byte[] bytes = null;
	// 演奏時間
	public long playtime = 0;

	/**
	 * コンストラクタ
	 * @param int _track
	 * @param int _status
	 * @param byte[] _bytes
	 * @param int _time
	 */
	public MidiMessage( int _track, int _status, byte[] _bytes, long _time ) {
		track = _track;
		status = _status;
		bytes = _bytes;
		playtime = _time;
	}		
}				
