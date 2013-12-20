package jp.ohwada.android.evy1sample3;

/**
 * １つの解析したメッセージを格納するクラス
 */
public class ParseMessage {

	// ステータス・バイトの定義
	public final static int STATUS_TEMPO = 0x00FF51;		
	public final static int STATUS_OHERS = 0x00FF00;
	public final static int STATUS_DELTA_TIME = 0xFFFFFFFF;

	// ステータス・コード	
	public int status = 0;
	// MIDIのバイト列
	public byte[] bytes = null;
	// デルタタイム
	public long deltatime = 0;

	/**
	 * コンストラクタ
 　 * 普通のメッセージは bytes を格納する
	 * @param int _status
	 * @param byte[] _bytes
	 * @param int _time
	 */
	public ParseMessage( int _status, byte[] _bytes ) {
		status = _status;
		bytes = _bytes;
		deltatime = 0;
	}
	
	/**
	 * コンストラクタ
	 * デルタタイムは deltatime を格納する
	 * @param int _status
	 * @param int _time
	 */
	public ParseMessage( int _status, long _time ) {
		status = _status;
		bytes = null;
		deltatime = _time;
	}		
}				
