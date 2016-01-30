package jp.ohwada.android.osm2.task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jp.ohwada.android.osm2.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Geocoder Async Task
 */
public class GeocoderAsync extends AsyncTask<Void, Void, Boolean> {

	// object
	private Context mContext;
	private View mView = null;
	private Geocoder mGeocoder = null;
	private ProgressDialog mProgressDialog = null;
	
	// param
	private int mMaxRresults = 1;
	private int mMaxRetry = 3;
	private String mLocation = "";
		
	// result		
	private List<Address> mResult = null;
    	
	/**
	 * === constructor ===
	 */			 
    public GeocoderAsync( Context context, View view ) {
        super();
        mContext = context;
        mView = view;
    }

	/*
	 * setMaxResults
	 * @param int results
	 */
	public void setMaxResults( int results ) {
		mMaxRresults = results;
	}

	/*
	 * setMaxRetry
	 * @param int retry
	 */
	public void setMaxRetry( int retry ) {
		mMaxRetry = retry ;
	}

	/*
	 * setLocation
	 * @param String location
	 */
	public void setLocation( String location ) {
		mLocation = location ;
	}
	    		
	/**
	 * get Result
	 * @return List<Address>
	 */  	
	public List<Address> getResult() {
		return mResult;
	}
	
	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute() {
    	showProgress();
    	hideInputMethod();
    	mResult = null;
	}

	/**
	 * === doInBackground ===
	 * @return Boolean
	 */	
    @Override
    protected Boolean doInBackground( Void... params ) {
		mResult = getAddressListRetry( mLocation );
		return true;
    }

	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
        hideProgress();
	}
					
	/**
	 * <pre>
	 * search latitude and longitude  from location name 
	 * repeats 3 times until get latitude and longitude
	 * </pre>
	 * @param String location
	 * @return List<Address>
	 */
	private List<Address> getAddressListRetry( String location ) {
		if ( "".equals( location ) ) return null;
		List<Address> list = null;
		// repeats 3 times until get latitude and longitude 
		for ( int i=0; i < mMaxRetry; i ++ ) {
			list = getAddressList(location);
			// break, if get latitude and longitude
			if (( list != null ) && !list.isEmpty() ) break;
		}
		return list;
	}
	
	/**
	 * search latitude and longitude  from location name 
	 * @param String location
	 * @return List<Address>
	 */
	private List<Address> getAddressList( String location ) {
		if ( "".equals( location ) ) return null;
		List<Address> list = null;
		mGeocoder = new Geocoder( mContext, Locale.getDefault() ) ;	
		try {
			list = mGeocoder.getFromLocationName( location, mMaxRresults );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * show Progress Dialog
	 */
	protected void showProgress( ) {
		mProgressDialog = new ProgressDialog( mContext );
		mProgressDialog.setCancelable( false ); 

		mProgressDialog.setOnCancelListener( new DialogInterface.OnCancelListener() {  
			public void onCancel( DialogInterface dialog ) {
				cancelTask();
      		}  
		});  
 
		mProgressDialog.setOnKeyListener( new DialogInterface.OnKeyListener() {
			public boolean onKey( DialogInterface dialog, int id, KeyEvent key) {
				cancelTask();
				return true; 
			}  
		});  

		String msg = mContext.getResources().getString( R.string.dialog_searching );	
		mProgressDialog.setMessage( msg );
		mProgressDialog.show();
	}
		
	/**
	 * hide Progress Dialog
	 */
	private void hideProgress() {
		if ( mProgressDialog != null ) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * cancel Task
	 */
	private void cancelTask() {
		hideProgress();
		cancel( true );
	}

	/**
	 * hide software keyboard
	 */
	private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager)
        	mContext.getSystemService( Context.INPUT_METHOD_SERVICE );
        // dont work InputMethodManager.HIDE_IMPLICIT_ONLY 
        imm.hideSoftInputFromWindow( mView.getWindowToken(), 0 ); 
	}
		    		   
}