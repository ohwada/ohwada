package jp.ohwada.android.ntpclientsample1;

import android.content.AsyncTaskLoader;
import android.content.Context;

/*
 * NtpLoader
 */ 
public class NtpLoader extends AsyncTaskLoader<NtpResult> {

	private static final String HOST = "pool.ntp.org";
	private static final int TIMEOUT = 10000;	// 10 sec

	/*
	 * Constractor
	 * @param Context context
	 */ 
    public NtpLoader( Context context ) {
        super( context );
	}

	/*
	 * === loadInBackground ===
	 */ 	 
    @Override
    public NtpResult loadInBackground() {
    	SntpClient client = new SntpClient();
    	boolean ret = client.requestTime( HOST, TIMEOUT );
    	if ( !ret ) {
    	    NtpResult res1 = new NtpResult();
			return res1;	
    	}
    	NtpResult res2 = new NtpResult( 
			client.getNtpTime(), 
			client.getNtpTimeReference(),
			client.getRoundTripTime() );
		return res2;	
    }
    	
}