package jp.ohwada.android.pinqa1.task;

import java.io.File;

import jp.ohwada.android.pinqa1.Constant;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * ArticleListTask
 */
public class ArticleListTask extends CommonTask {  

	// object  
	private Context mContext;
	private ArticleListParser mParser;
   	private ArticleListFile mFileClass;
   	private ArticleListAsync mAsync = null;
   	
	// variable
   	private File mFileTarget = null;
   	private ArticleList mList = null;
    	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public ArticleListTask( Context context, Handler handler ) {
    	super( handler, Constant.MSG_WHAT_TASK_ARTICLE_LIST );
        mContext = context;
		mParser = new ArticleListParser();
		mFileClass = new ArticleListFile(); 
		TAG_SUB = "ArticleListTask";
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
	 * @param int lat
	 * @param int lng	 
	 * @return boolean
	 */         
    public boolean execute( int lat, int lng ) {
    	mFileTarget = mFileClass.getFile( lat, lng );
		if ( mFileClass.isExpired( mFileTarget ) ) {
		 	// create async task each time
		 	mList = null;
		    mAsync = new ArticleListAsync( mContext );	
		    mAsync.setGeo( lat, lng );		
			mAsync.execute();
			startHandler();
			return false;
		} 
		// read file 
		mList = mFileClass.read( mFileTarget );
		return true;
	}

	/**
	 * getList
	 * @return ArticleList
	 */ 
	public ArticleList getList() {	
		return mList;
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
		File file = mFileClass.getFileWithTxt( "result" );	
		mFileClass.writeAfterDelete( file, result );	
		ArticleList list = mParser.parse( result );
		if (( list == null )||( list.size() == 0 )) {
        	log_d( "No parse data" );
        	return Constant.MSG_ARG1_TASK_ERR_PARSE;		
		}
		// save 
		mFileClass.write( mFileTarget, list );		
		// read same file to set addtional
		mList = mFileClass.read( mFileTarget );
        return Constant.MSG_ARG1_TASK_SUCCESS;		
	}

	/**
	 * readFile
     * @return boolean 
	 */ 	
	protected boolean readFile() {
		if ( mFileTarget.exists() ) { 
			mList = mFileClass.read( mFileTarget );
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