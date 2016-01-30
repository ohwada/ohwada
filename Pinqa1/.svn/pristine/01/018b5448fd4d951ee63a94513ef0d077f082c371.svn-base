package jp.ohwada.android.pinqa1.task;

import java.io.File;

import jp.ohwada.android.pinqa1.Constant;

import android.os.AsyncTask;
import android.os.Handler;

/**
 * ImgaeTask
 */
public class ImageTask extends CommonTask {  

    private static final long EXPIRE_IMAGE = Constant.EXPIRE_DAYS_IMAGE * TIME_MSEC_ONE_DAY; 
    private static final String PREFIX_TMP = "tmp_";
	
	// object        
   	private BinaryAsyncTask mAsync;
   	private CommonFile mFileClass;
 	
	// variable
    private File mFileImage = null;
    private File mFileTemp = null;
    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public ImageTask( Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_IMAGE );		
		mFileClass = new CommonFile();
        TAG_SUB = "ImageTask";
    }

	/**
	 * execute
	 * @param String name
	 * @param String url
	 */
    public boolean execute( ArticleRecord record ) {
		File file = mFileClass.getFileFromName( record.image_name );
		return execute( file, record.image_url );
    }
     
	/**
	 * execute
	 * @param String name
	 * @param String url
	 */
    public boolean execute( File file_image, String url ) {
    	mFileImage = file_image;
    	String name = PREFIX_TMP + file_image.getName();
    	mFileTemp = mFileClass.getFileFromName( name );
		if ( mFileClass.isExpiredFile( file_image, EXPIRE_IMAGE ) ) {
			// create async task each time
		    mAsync = new BinaryAsyncTask();								
			mAsync.setFileUrl( mFileTemp, url );
			mAsync.execute();
			startHandler();
			return false;
		}
		return true;
	}
	
	/**
	 * getPath
	 * @return String
	 */ 
	public String getFilePath() {	
		if ( mFileImage.exists() && ( mFileImage.length() > 0 )) { 
			return mFileImage.getPath();
		}
		return "";	
	}

	/**
	 * cancel
	 */	 
	public void cancel() {	
		if ( mAsync != null ) {
			mAsync.cancel( true );
			mAsync.shutdown();
		}
		stopHandler();	
	}

	/**
	 * execPost
	 */ 
	protected void execPost() {
		stopHandler();	
		int arg1 = 0;
		int arg2 = 0;
		boolean result = getAsyncResult();
		if ( result ) {
			arg1 = Constant.MSG_ARG1_TASK_SUCCESS;
			if ( mFileImage.exists() ) { 
				mFileImage.delete();
			}
			mFileTemp.renameTo( mFileImage );
		} else {
			log_d( "No result" );
        	arg1 = Constant.MSG_ARG1_TASK_ERR_RESULT;				
		}	
		sendMessage( MSG_WHAT, arg1, arg2 );		
	}
	
	/**
	 * getAsyncResult()
	 * @return boolean
	 */
	private boolean getAsyncResult() {
		boolean result = false;
		if ( mAsync != null ) {			
			result = mAsync.getResult();
			mAsync = null;
		}
		return result; 
	}
			
	/**
	 * getStatus
	 * @return AsyncTask.Status
	 */		
    protected AsyncTask.Status getStatus() {
    	if ( mAsync != null ) {
    		return mAsync.getStatus();
    	}
    	return AsyncTask.Status.RUNNING;	
    }

}