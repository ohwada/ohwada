package jp.ohwada.android.yag1.task;

import java.util.List;

import jp.ohwada.android.yag1.Constant;

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.google.android.maps.GeoPoint;

/**
 * Geocoder Task
 */
public class GeocoderTask extends CommonTask {  

	//  object    
   	private Context mContext; 
	private View mView;
	private GeocoderAsync mAsync = null;

	// result
	private List<Address> mResultList = null;
       	    	 			
	/**
	 * === constarctor ===
	 * @param Handler handler
	 */ 
    public GeocoderTask( Context context, View view, Handler handler ) {
    	super( handler, Constant.MSG_WHAT_TASK_GEOCODER );
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
		mResultList = mAsync.getResult();
		sendMessage();		
	}

	/**
	 * get Result
	 * @return List<Address>
	 */ 
	public List<Address> getResultList() {	
		return mResultList;
	}

	/**
	 * get Result
	 * @return GeoPoint
	 */  	
	public GeoPoint getResultPoint() {
		if (( mResultList == null )|| mResultList.isEmpty() ) return null;
		Address addr = mResultList.get( 0 );
		int lat = doubleToE6( addr.getLatitude() ); 
		int lng = doubleToE6( addr.getLongitude() );
		GeoPoint point = new GeoPoint( lat, lng );
		return point;
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

	/**
	* convert real number to integer
	* @param Double : location( floating point format )
	* @return int : location( E6 format )
	*/
	private int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}
}