package jp.ohwada.android.pinqa1.task;

import java.util.List;

import jp.ohwada.android.pinqa1.Constant;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

/**
 * Geocoder Task
 */
public class GeocoderTask extends CommonTask {  

	//  object    
	private Context mContext = null;
	private View mView = null;
	private GeocoderAsync mAsync;

	// result
	private List<Address> mList = null;
       	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public GeocoderTask( Context context, View view, Handler handler ) {
    	super(  handler, Constant.MSG_WHAT_TASK_GEOCODER );
        mContext = context;
        mView = view;
     	TAG_SUB = "GeocoderTask";
    }
	
	/**
	 * execute
	 * @param String location
	 * @return boolean
	 */         
    public void execute( String location ) {
		mAsync = new GeocoderAsync( mContext, mView );	
		mAsync.setLocation( location );	
		mAsync.execute();
		startHandler();
	}

	/**
	 * execPost
	 */ 
	protected void execPost() {
		stopHandler();	
		mList = mAsync.getResult();
		sendMessage();		
	}

	/**
	 * getList
	 * @return List<Address>
	 */ 
	public List<Address> getList() {	
		return mList;
	}

	/**
	 * cancel
	 */
	public void cancel() {
		if ( mAsync != null ) {
			mAsync.cancel( true );
		}
		stopHandler();	
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