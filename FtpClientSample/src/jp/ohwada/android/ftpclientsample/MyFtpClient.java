package jp.ohwada.android.ftpclientsample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.util.Log;
	
/**
 * upload file by ftp
 * http://commons.apache.org/net/api-3.1/org/apache/commons/net/ftp/FTPClient.html
 */
public class MyFtpClient  {

	// constant
	private final static String TAG = "FtpClientSample";
	private final static String TAG_SUB = "FtpClientClass : ";
    private final static Boolean D = Constant.DEBUG;   

	private final static int FTP_PORT = 21;
    private final static int FILE_TYPE = FTP.BINARY_FILE_TYPE;
    private final static int TIMEOUT = 10*1000;

	// set by caller
    public String host = "";
    public String user = "";
    public String pass = "";
    public String dir = "";
    public int port = 0;
    
    // Please use passive mode,
    // because dont work active mode in Android
    private boolean isPassive = true;

	/**
	 * === constractor === 
	 * @param none
	 */
    public MyFtpClient() {
		// dummy
    }
    
	/**
	 * --- upload file ---
	 * @param String remote : The name to give the remote file
	 * @param InputStream local : The local InputStream from which to read the file
	 * @return boolean : True if successfully completed, false if not.
	 */		                  
    public boolean upload( String remote, InputStream local ) {
		log_d( "upload" );
		
        boolean isError = false;
        
		FTPClient ftp = getClient();
		if ( ftp == null ) {
			return false;
		}
 
 		// upload          		
		try {
			if ( ftp.storeFile( remote, local ) ) {
				log_d( "FTP upload succeeded : " + remote );	
			} else {
				isError = true;
				log_d( "FTP upload failed : " + remote );	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			isError = true;
		}

		disconnect( ftp );         	
        return !isError;
    }
   
	/**
	 * --- download file ---
	 * @param String remote : The name to give the remote file
	 * @param OutputStream local : The local OutputStream from which to write the file
	 * @return boolean : True if successfully completed, false if not.
	 */		                  
    public boolean download( String remote, OutputStream local ) {
		log_d( "download" );

        boolean isError = false;
        
		FTPClient ftp = getClient();
		if ( ftp == null ) {
			return false;
		}
 
		// download file
		try {
			if ( ftp.retrieveFile( remote, local ) ) {
				log_d( "FTP download succeeded : " + remote );	
			} else {
				isError = true;
				log_d( "FTP download failed : " + remote );	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			isError = true;
		}

		disconnect( ftp );         	
        return !isError;
    }
    
	/**
	 * get Client
	 * @param none
	 * @return FTPClient or null
	 */		                  
    private FTPClient getClient() {
		log_d( "getClient" );

        FTPClient ftp = null;
        String msg = "";
        String error = "";
        int code = 0;
        boolean isError = false;
                                    				                
        try {
        	log_d( "Start FTP user : " + user );
        	
            ftp = new FTPClient();
			int ftp_port = FTP_PORT;

			// if port specfiied
			if ( port > 0 ) {
				ftp_port = port;
			}

            ftp.setDefaultTimeout( TIMEOUT );            

			// connect
            ftp.connect( host, ftp_port );            
            msg = "FTP Connect :  " + host + " : " + port;  
            log_d( msg );
            code = ftp.getReplyCode();	                                                 
            if ( FTPReply.isPositiveCompletion(ftp.getReplyCode()) == false ) {
                error = msg+ " :  ReplyCode = " + code; 
                throw new Exception( error ); 
            }

            ftp.setSoTimeout( TIMEOUT );
            ftp.setDataTimeout( TIMEOUT );             

			// login
            if ( ftp.login( user, pass ) == false ) {
                error = "FTP Login :  " + user + " ; ReplyCode = " + ftp.getReplyCode();
                throw new Exception( error );  
            }                 

            ftp.setFileType( FILE_TYPE );

			// passive or active
            if ( isPassive ) {
                log_d("FTP passive mode" );	
                ftp.enterLocalPassiveMode();
            } else {
                log_d("FTP active mode" );
            	ftp.enterLocalActiveMode();
            }

            // change dir if dir specfiied
			if ( ! "".equals(dir) ) {
            	msg = "FTP cwd : " + dir;  
                log_d( msg );	
            	code = ftp.cwd( dir );
            	if ( !FTPReply.isPositiveCompletion(code) ) {
                	error = msg + " ; ReplyCode = " + code;  
                	throw new Exception( error );
            	}
			}

        } catch (SocketException e) {
        	isError = true;
            e.printStackTrace();

        } catch (IOException e) {
            isError = true;
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            isError = true;		
        } 

		if ( isError ) {
			return null;
		} else {       	
        	return ftp;
        }
    }

	/**
	 * disconnect
	 * @param FTPClient ftp
	 * @return boolean
	 */		                  
    private boolean disconnect( FTPClient ftp ) {
		log_d( "disconnect" );

        boolean isError = false;
        		
		if( ftp.isConnected() ) {
			try {
				ftp.disconnect();
				log_d( "FTP disconnect");
			} catch(IOException e) {
				e.printStackTrace();
			}	
        }
        
        return !isError;
	}
	        		
	/**
	 * write log
	 * @param String msg
	 * @return void
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}
}