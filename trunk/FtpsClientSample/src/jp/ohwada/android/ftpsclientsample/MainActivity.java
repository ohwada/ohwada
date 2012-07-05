package jp.ohwada.android.ftpsclientsample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * fpt client sample
 */
public class MainActivity extends Activity {

	private final static String DIR_NAME = "FtpClientSample";
    private final static String FILE_NAME = "sample.jpg";
	private final static String EXT = "jpg";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");	
    	
	/**
	 * onCreate
	 * @param Bundle savedInstanceState
	 * @return void
	 */          
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        // upload button
        Button btnUpload = (Button) findViewById(R.id.button_upload);
        btnUpload.setOnClickListener( new OnClickListener() {
        
        	/**
	 		 * when onClick, upload file
	 		 * @param View v
	 		 * @return void
	 		 */
            public void onClick( View v ) {  
                upload();
             }
        });
        
        // download button
        Button btnDownload = (Button) findViewById(R.id.button_download);
        btnDownload.setOnClickListener( new OnClickListener() {
        
        	/**
	 		 * when onClick, download file
	 		 * @param View v
	 		 * @return void
	 		 */
            public void onClick( View v ) {  
                download();
             }
        });         
    }
	
	/**
	 * upload file
	 * @param none
	 * @return void
	 */     
	private void upload() {
		InputStream local = getAssetsInputStream( FILE_NAME );
		if ( local == null ) {
			Toast.makeText(this, "cannot read file", Toast.LENGTH_SHORT).show();
			return;
		}		

		MyFtpClient client = new MyFtpClient();
		client.host = Constant.FTP_HOST ;
		client.user = Constant.FTP_USER ;
		client.pass = Constant.FTP_PASS ;
		client.dir = Constant.FTP_REMOTE_DIR ;
		client.setFtps();
		boolean ret = client.upload( getFilename(), local );

		String msg = "Upload succeeded";
		if ( !ret ) {
			msg = "Upload failed";
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();							}

	/**
	 * download file
	 * @param none
	 * @return void
	 */     
	private void download() {
		OutputStream local = getExternalStorageOutputStream( getFilename() );
		if ( local == null ) {
			Toast.makeText(this, "cannot create write file", Toast.LENGTH_SHORT).show();
			return;
		}
		  		
		MyFtpClient client = new MyFtpClient();
		client.host = Constant.FTP_HOST ;
		client.user = Constant.FTP_USER ;
		client.pass = Constant.FTP_PASS ;
		client.dir = Constant.FTP_REMOTE_DIR ;
		client.setFtps();
		boolean ret = client.download( FILE_NAME, local );

		String msg = "Download succeeded";
		if ( !ret ) {
			msg = "Download failed";
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();					
	}

	/**
	 * make directory if not exists 
	 * @param none
	 * @return void
	 */ 
	private void initExternalStorage() {
		File file = new File( getDir() );
		if ( ! file.exists() ) { 
			file.mkdir();
		}
	}

	/**
	 * get dirctory
	 * @param none
	 * @return String
	 */ 
	private String getDir() {
		String path = Environment.getExternalStorageDirectory().getPath();
		String dir = path + "/" + DIR_NAME ;
		return dir;
	}
				
	/**
	 * get InputStream of asset file
	 * @param String filename
	 * @return InputStream
	 */ 
	private InputStream getAssetsInputStream( String filename ) {
		InputStream is = null;
		try {
			is = getAssets().open( filename );		
		} catch ( IOException e ) {
			e.printStackTrace();
		} 
		return is;
	}
	
	/**
	 * get OutputStream of ExternalStorage file
	 * @param String filename
	 * @return OutputStream
	 */ 
	private OutputStream getExternalStorageOutputStream( String filename ) {	
		initExternalStorage();
		String file = getDir() + "/" + filename;
		OutputStream os = null;
		try {
			os = new FileOutputStream( file );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return os;
	}
	
	/**
	 * get filename
	 * @param none
	 * @return String
	 */ 
	private String getFilename() {	
		String name = mDateFormat.format( new Date( System.currentTimeMillis() ) );
		name  += "." + EXT; 
        return name;   
	}	
}