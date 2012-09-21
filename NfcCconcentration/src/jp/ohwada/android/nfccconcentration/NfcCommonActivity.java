package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;

/*
 * Nfc Common Activity
 */
public class NfcCommonActivity extends Activity {

	// dubug
	protected String TAG_SUB = "NfcCommon : ";
	protected final static String TAG = Constant.TAG;
    protected final static boolean D = Constant.DEBUG; 

	protected final static int REQUEST_CODE_LIST = Constant.REQUEST_CODE_LIST ;
	protected final static int REQUEST_CODE_CREATE = Constant.REQUEST_CODE_CREATE ;
	protected final static int REQUEST_CODE_UPDATE = Constant.REQUEST_CODE_UPDATE ;
	protected final static int REQUEST_CODE_SETTING = Constant.REQUEST_CODE_SETTING ;
	protected final static int REQUEST_CODE_VIDEO = Constant.REQUEST_CODE_VIDEO ;
	protected final static String BUNDLE_EXTRA_ID  = Constant.BUNDLE_EXTRA_ID;
	protected final static String BUNDLE_EXTRA_TAG  = Constant.BUNDLE_EXTRA_TAG;

	protected final static int REQUEST_CODE_NFC = 0 ;    
	protected final static int FLAG_NONE = 0;
	
	// NFC
    protected NfcAdapter mAdapter;
    protected PendingIntent mPendingIntent;
    protected IntentFilter[] mFilters;
    protected String[][] mTechLists;
	
// === onCreate ===
	/**
	 * prepare onNewIntent
	 */	
	protected void prepareIntent() {					
		mAdapter = NfcAdapter.getDefaultAdapter( this );
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        Intent intent = new Intent( this, getClass() ).addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
        mPendingIntent = PendingIntent.getActivity( this, REQUEST_CODE_NFC, intent, FLAG_NONE ) ;
        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter( NfcAdapter.ACTION_NDEF_DISCOVERED );
        try {
            ndef.addDataType( "*/*" );
        } catch ( MalformedMimeTypeException e ) {
			e.printStackTrace();
        }
        mFilters = new IntentFilter[] { ndef, };
        // Setup a tech list for Type-A Type-B Type-F tags
 		mTechLists = new String[][] { 
			new String[] { NfcA.class.getName() } , 
			new String[] { NfcB.class.getName() } , 
			new String[] { NfcF.class.getName() } };
    }

// === onResume ===
    protected void enableForegroundDispatch() {
        if ( mAdapter != null ) {
        	mAdapter.enableForegroundDispatch( this, mPendingIntent, mFilters, mTechLists );
        }
    }

// === onNewIntent ===
	/**
	 * intent To TagID
	 * @param Intent intent
	 * @return String
	 */
    protected String intentToTagID( Intent intent ) {
    	// return if invalid intent 
		if ( intent == null ) {
			log_d( "intentToTagID: intent is null" );
			return null;
		}			

    	// return if invalid action 
		String action = intent.getAction();	
		if ( !action.equals( NfcAdapter.ACTION_TECH_DISCOVERED ) ) {
			log_d( "intentToTagID: invalid action: " + action );
			return null;
		}
	
    	// return if invalid id 
    	byte[] byte_id = intent.getByteArrayExtra( NfcAdapter.EXTRA_ID );
		if ( byte_id == null ) {
			log_d( "intentToTagID: tag is null" );
			return null;
		}

		// get tag id	 
		String tag_id = bytesToText( byte_id );
        log_d( "Discovered tag with intent: " + intent );
		log_d( "id: " + tag_id  );
		return tag_id;
    }

	/**
	 * bytes To Tex
	 * @param byte[] bytes
	 * @return String
	 */
    protected String bytesToText( byte[] bytes ) {	 
    	StringBuilder buffer = new StringBuilder();	 
    	for ( byte b : bytes ) { 
    		String hex = String.format( "%02X", b );	 
			buffer.append( hex );	 
    	}	 
    	String text = buffer.toString().trim();	 
    	return text;	
    }
		
// === onPause ===
    protected void disableForegroundDispatch() {
        if ( this.isFinishing() && ( mAdapter != null ) ) {
            mAdapter.disableForegroundDispatch( this );
        }
    } 

// --- start Activity ---	
	/**
	 * start ListActivity
	 */	
	protected void startListActivity() {	
		Intent intent = new Intent( this, CardListActivity.class );
		startActivityForResult( intent, REQUEST_CODE_LIST );
	}

	/**
	 * start VideoActivity
	 */	
	protected void startVideoActivity() {	
		Intent intent = new Intent( this, VideoActivity.class );
		startActivityForResult( intent, REQUEST_CODE_VIDEO );
	}

	/**
	 * start AddActivity
	 */	
	protected void startSettingActivity() {	
		Intent intent = new Intent( this, SettingActivity.class );
		startActivityForResult( intent, REQUEST_CODE_SETTING );
	}
	
	/**
	 * start CreateActivity
	 * @param String tag_id
	 */			    
	protected void startCreateActivity( String tag_id ) {
		Intent new_intent = new Intent( this, CreateActivity.class );
		Bundle bandle = new Bundle();
		bandle.putString( BUNDLE_EXTRA_TAG, tag_id );
		new_intent.putExtras( bandle );
		startActivityForResult( new_intent, REQUEST_CODE_CREATE );
	}

	/**
	 * start update activity with id
	 * @param int id
	 * @return void	 
	 */
	protected void startUpdateActivity( int id ) {
    	Intent intent = new Intent( this, UpdateActivity.class );
    	Bundle bandle = new Bundle();
        bandle.putInt( BUNDLE_EXTRA_ID, id );
        intent.putExtras( bandle );
		startActivityForResult( intent, REQUEST_CODE_UPDATE );
	}

// --- debug ---		           
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + msg );
	} 

	/**
	 * toast short
	 * @param String msg
	 */ 
	protected void toast_short( String msg ) {
		ToastMaster.showShort( this, msg );
	}
}
