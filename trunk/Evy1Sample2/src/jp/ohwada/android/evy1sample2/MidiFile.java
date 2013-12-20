package jp.ohwada.android.evy1sample2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * MidiFile
 */
public class MidiFile {

	// debug
	private final static boolean D = true;
	private final static String TAG = "MidiFile";
	
	private Context mContext;

	/**
	 * constractor
	 * @param Context context
	 */						
	public MidiFile( Context context ) {
		mContext = context;
	}

	/**
	 * readAssetFile
	 * @param String name
     * @return byte[]
	 */	
	public byte[] readAssetFile( String name ) {  
		log_d( "readAssetFile" );
		AssetManager as = mContext.getResources().getAssets();
		InputStream is = null;
		try {
			is = as.open( name );
		} catch (IOException e) {
			if (D) e.printStackTrace();
		} 
		if ( is == null )	return null; 
		return readFile( is );
	}
		
    /** 
     * readFile
     * @param InputStream in
     * @return byte[]
     */  
    private byte[] readFile( InputStream in ) {  
		log_d( "readFile" );
		ByteArrayBuffer out = new ByteArrayBuffer( 12000 );
		BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
		String line = "";
        try { 
        	// １行づつ読み込む 
			while (( line = br.readLine() ) != null ) {
        		// カンマで分割して、バイト単位にする
				for ( String s: line.split(",") ) {
					out.append( Integer.parseInt( s, 16 ) );
				}
			}	
			return out.toByteArray();
        } catch (Exception e) {  
			if (D) e.printStackTrace();
        } finally {  
            try {  
				br.close(); 
            } catch (Exception e) {
    			if (D) e.printStackTrace();
            }  
        }
		return null;
    } 

	/**
	 * write log
	 * @param String msg
	 * @return void
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	} 
					
}

