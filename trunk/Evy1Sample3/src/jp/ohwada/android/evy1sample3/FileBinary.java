package jp.ohwada.android.evy1sample3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * FileBinary
 */
public class FileBinary {

	// debug
	private final static boolean D = true;
	private final static String TAG = "FileBinary";

	private Context mContext;
	
	/**
	 * constractor
	 * @param Context context
	 */						
	public FileBinary( Context context ) {
		mContext = context;
	}

	/**
	 * readAssetFile
	 * @param String name
     * @return byte[]
	 */	
	public byte[] readAssetBinaryFile( String name ) {  
		log_d( "readAssetFile" );
		AssetManager as = mContext.getResources().getAssets();
		InputStream is = null;
		try {
			is = as.open( name );
		} catch (IOException e) {
			if (D) e.printStackTrace();
		} 
		if ( is == null )	return null; 
		return readBinaryFile( is );
	}

    /** 
     * readBinaryFile
     * @param InputStream in
     * @return byte[]
     */  
    private byte[] readBinaryFile( InputStream in ) {  
    	int size = 0;
        byte[] data = new byte[1024];  
        ByteArrayOutputStream out = null;    
        try {  
            out = new ByteArrayOutputStream();  
            while ((size = in.read(data)) != -1 ) {  
                out.write(data, 0, size);  
            }
            return out.toByteArray();
        } catch (Exception e) {  
			if (D) e.printStackTrace();
        } finally {  
            try {  
                if (in != null) in.close();  
                if (out != null) out.close();  
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
