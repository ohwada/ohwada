package jp.ohwada.android.yag1.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.ohwada.android.yag1.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Binary Async Task
 */
public class BinaryAsyncTask extends AsyncTask<Void, Void, Boolean> {

	// debug
	private static final String TAG = Constant.TAG;
	private static final boolean D = Constant.DEBUG;
	private String TAG_SUB = "BnaryAsyncTask";

	private final static int EOF = -1;
	private final static int BUFFER_SIZE = 10240;
			
    // class object    	
	private HttpClient mClient = null;

	// local variable
	private File mFile = null;
	private String mUrl = "";
	private boolean mResult = false;
	
	/**
	 * === constructor ===
	 */			 
    public BinaryAsyncTask() {
        super();
        mClient = new DefaultHttpClient();
    }
	 
	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
		// dummy	
    }

	/**
	 * === doInBackground ===
	 * @return Boolean
	 */	
    @Override
    protected Boolean doInBackground( Void... params ) {
        execBackground();
		return true;
    }
	
	/**
	 * === onProgressUpdate ===
	 */	
    @Override
    protected void onProgressUpdate( Void... params ) {
		// dummy
    }

	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
		// dummy
    }

	/**
	 * setUrl
	 * @param String url
	 */  	
	public void setFileUrl( File file, String url ) {
		mFile = file;
		mUrl = url;
	}
	    		
	/**
	 * get Result
	 * @return boolean
	 */  	
	public boolean getResult() {
		return mResult;
	}

	/**
	 * execBackground
	 */  	
	protected void execBackground() {
		mResult = excuteGetBinaty( mFile, mUrl ); 
	}
	
	/**
	 * execute get metod
	 * @param String url
	 * @return boolean
	 */  		
    private boolean excuteGetBinaty( File file, String url ) {
    	if (( url == null )|| "".equals( url ) ) return false;
		HttpGet request = new HttpGet( url );
		boolean result = false;
		try {
			HttpResponse response = mClient.execute( request );
			switch ( response.getStatusLine().getStatusCode() ) {
       			case HttpStatus.SC_OK:
       				result = parseResponseBinary( file, response );
       				break;       			
            	case HttpStatus.SC_NOT_FOUND:
            		log_d( "not found ");     
       				break;  
            	default:
                	log_d( "unknown error");
       				break;  
			}		
		} catch ( ClientProtocolException e ) {
		    if (D) e.printStackTrace();
		} catch ( IOException e ) {
		    if (D) e.printStackTrace();
		}
    	return result;	
	}

	/**
	 * parse Response
	 * @param HttpResponse response
	 * @return boolean
	 */ 
	private boolean parseResponseBinary( File file, HttpResponse response ) {
		boolean ret = false;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
    	try {
			is = response.getEntity().getContent();
			fos = new FileOutputStream( file, false );
			bis = new BufferedInputStream( is, BUFFER_SIZE );
			bos = new BufferedOutputStream( fos, BUFFER_SIZE );
			byte buffer[] = new byte[ BUFFER_SIZE ];
			int size = 0;
			while( EOF != (size = bis.read( buffer )) ) {
				bos.write( buffer, 0, size );
			}
			bos.flush();
			ret = true;
		} catch ( ClientProtocolException e ) {
			if (D) e.printStackTrace();
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
		} finally {
			if (bis != null) { 
				try { 
					bis.close(); 
				} catch (IOException e) {
					if (D) e.printStackTrace();
					ret = false;
				} 
			}
			if (bos != null) { 
				try { 
					bos.close(); 
				} catch (IOException e) {
					ret = false;
					if (D) e.printStackTrace();
				} 
			}
		}
		return ret;
	}	

	/**
	 * --- shutdown  ---
	 * @param none
	 * @return void
	 */ 
    protected void shutdown() {
    	if ( mClient != null ) {
    		mClient.getConnectionManager().shutdown();
    	}
    	mClient = null;
    }
   		
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}			    		   
}