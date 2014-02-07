package jp.ohwada.android.ntpclientsample2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import android.content.AsyncTaskLoader;
import android.content.Context;

/*
 * NtpLoader
 */ 
public class NtpLoader extends AsyncTaskLoader<TimeInfo> {

	private static final boolean D = true;
	
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
    public TimeInfo loadInBackground() {
    	TimeInfo info = null;
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout( TIMEOUT );
        try {
            client.open();
			InetAddress hostAddr = InetAddress.getByName( HOST );		
			info = client.getTime( hostAddr );
        } catch ( SocketException e ) {
            if (D) e.printStackTrace();
        } catch ( IOException e ) {
			if (D) e.printStackTrace();
        }
        client.close();
        return info;	
    }
    	
}