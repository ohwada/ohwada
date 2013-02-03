package jp.ohwada.android.pinqa1.task;

import java.io.File;

import jp.ohwada.android.pinqa1.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * ArticleTask
 */
public class ArticleTask extends CommonTask {  

	// object  
	private Context mContext;
	private ArticleParser mParser;
   	private ArticleFile mFileClass;
   	private ArticleAsync mAsync = null;

	// variable
   	private File mFileTarget = null;
	private ArticleRecord mRecord = null;
	private String mUrl = "";
    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public ArticleTask( Context context, Handler handler ) {
        super( handler, Constant.MSG_WHAT_TASK_ARTICLE );
        mContext = context;
        mParser = new ArticleParser();
		mFileClass = new ArticleFile(); 
		TAG_SUB = "ArticleTask";
    }

	/**
	 * exists Article file
	 * @return boolean
	 */
    public boolean existsFile() {
    	return mFileTarget.exists();
	}
	
	/**
	 * execute
	 * @param String url
	 * @return boolean
	 */         
    public boolean execute( String url ) {
    	mUrl = url;
    	ArticleRecord r = new ArticleRecord();
    	mFileTarget = mFileClass.getFile( r.getArticleId( url ) );
		if ( mFileClass.isExpired( mFileTarget ) ) {
		 	// create async task each time
		    mAsync = new ArticleAsync( mContext );	
		    mAsync.setUrl( url );		
			mAsync.execute();
			startHandler();
			return false;
		} 
		// read file 
		mRecord = mFileClass.read( mFileTarget );
		return true;
	}

	/**
	 * getList
	 * @return ArticleRecord
	 */ 
	public ArticleRecord getRecord() {	
		return mRecord;
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
		execPostFile();
	}
		
	/**
	 * saveFile
	 * @return int
	 */ 		
	protected int saveFile() {		
		// get 			
		String result = getAsyncResult();
		if (( result == null )|| result.equals("") ) {
			log_d( "No result" );
        	return Constant.MSG_ARG1_TASK_ERR_RESULT;			
		}				
		// parse 			
		ArticleRecord r = mParser.parse( result );
		if ( r == null ) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save
		r.article_url = mUrl; 
		mFileClass.write( mFileTarget, r );		
		// read same file to set addtional
		mRecord = mFileClass.read( mFileTarget );
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mRecord = mFileClass.read( mFileTarget );
			return true;
		}
		return false;	
	}

	/**
	 * getAsyncResult()
	 * @return  String 
	 */
	private String getAsyncResult() {
		String result = "";
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