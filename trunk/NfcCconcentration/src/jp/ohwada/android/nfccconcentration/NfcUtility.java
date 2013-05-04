package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;

/*
 * Nfc Utility
 */
public class NfcUtility {

	// dubug
    private final static boolean D = Constant.DEBUG; 

	private final static int REQUEST_CODE_NFC = 0 ;    
	private final static int FLAG_NONE = 0;
	
    private Activity mActivity;
    private Context mContext;
	
	// NFC
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    /**
     * === constractor ===
     */	
	public NfcUtility( Activity activity, Class<?> cls ) {
		mActivity = activity;
		mContext = activity;				
		mAdapter = NfcAdapter.getDefaultAdapter( mContext );
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        Intent intent = new Intent( mContext, cls ).addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
        mPendingIntent = PendingIntent.getActivity( mContext, REQUEST_CODE_NFC, intent, FLAG_NONE ) ;
        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter( NfcAdapter.ACTION_NDEF_DISCOVERED );
        try {
            ndef.addDataType( "*/*" );
        } catch ( MalformedMimeTypeException e ) {
			if (D) e.printStackTrace();
        }
        mFilters = new IntentFilter[] { ndef, };
        // Setup a tech list for Type-A Type-B Type-F tags
 		mTechLists = new String[][] { 
			new String[] { NfcA.class.getName() } , 
			new String[] { NfcB.class.getName() } , 
			new String[] { NfcF.class.getName() } };
    }

	/**
	 * enable ForegroundDispatch
	 */
    public void enable() {
        if ( mAdapter != null ) {
        	mAdapter.enableForegroundDispatch( mActivity, mPendingIntent, mFilters, mTechLists );
        }
    }

	/**
	 * disable ForegroundDispatch
	 */
    public void disable() {
        if ( mAdapter != null ) {
            mAdapter.disableForegroundDispatch( mActivity );
        }
    } 

	/**
	 * intent To TagID
	 * @param Intent intent
	 * @return String
	 */
    public String intentToTagID( Intent intent ) {
    	// return if invalid intent 
		if ( intent == null ) return null;
    	// return if invalid action 
		String action = intent.getAction();	
		if ( !action.equals( NfcAdapter.ACTION_TECH_DISCOVERED )) return null;
	    // return if invalid id 
    	byte[] byte_id = intent.getByteArrayExtra( NfcAdapter.EXTRA_ID );
		if ( byte_id == null ) return null;
		// get tag id	 
		String tag_id = bytesToText( byte_id );
		return tag_id;
    }

	/**
	 * bytes To Tex
	 * @param byte[] bytes
	 * @return String
	 */
    private String bytesToText( byte[] bytes ) {	 
    	StringBuilder buffer = new StringBuilder();	 
    	for ( byte b : bytes ) { 
    		String hex = String.format( "%02X", b );	 
			buffer.append( hex );	 
    	}	 
    	String text = buffer.toString().trim();	 
    	return text;	
    }
}
