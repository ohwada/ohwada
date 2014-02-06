package jp.ohwada.android.ntpclientsample1;

/**
 * NtpResult
 */
public class NtpResult {
	public boolean ret = false;	
	public long time = 0;
	public long reference = 0;
    public long roundtrip = 0;
    
	/**
	 * Constractor
	 */
	public NtpResult() {
		ret = false;
	}

	/**
	 * Constractor
	 * @param long _time
	 * @param long _reference
	 * @param long _roundtrip
	 */
	public NtpResult( long _time, long _reference, long _roundtrip ) {
		ret = true;
		time = _time;
		reference = _reference;
		roundtrip = _roundtrip;
	}
}
