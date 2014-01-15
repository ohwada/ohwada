package jp.ohwada.android.midi;

import java.util.List;

/**
 * MIDI ファイルの解析結果を格納するクラス
 */
public class MidiLoaderResult {
	public long timebase = 0;	
	public List<MidiMessage> list = null;
	public int code = -1;

	/**
	 * コンストラクタ
	 * @param int _code
	 */
	public MidiLoaderResult( int _code ) {
		code = _code;
	}

	/**
	 * コンストラクタ
	 * @param int _code
	 * @param long _timebase
	 * @param List<MidiMessage> _list 
	 */
	public MidiLoaderResult( int _code, long _timebase, List<MidiMessage> _list ) {
		code = _code;
		timebase = _timebase;
		list = _list;
	}
}
