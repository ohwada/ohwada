package jp.ohwada.android.pinqa1.task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jp.ohwada.android.pinqa1.R;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Geocoder Async Task
 */
public class GeocoderAsync extends CommonAsyncTask {

	// object
	private View mView = null;
	private Geocoder mGeocoder = null;

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
        super( context );
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
	 * execBackground
	 */  	
	protected void execPreExecute() {
    	showProgress( R.string.searching );
    	hideInputMethod();
    	mResult = null;
	}

	/**
	 * execBackground
	 */
    protected void execBackground() {
		mResult = getAddressListRetry( mLocation );
    }

	/**
	 * execProgressUpdate
	 */  	
	protected void execPostExecute() {
        hideProgress();
	}
					
	/**
	 * hide software keyboard
	 */
	private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager)
        	mContext.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( mView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY ); 
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
		    		   
}