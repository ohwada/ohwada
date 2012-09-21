package jp.ohwada.android.nfccconcentration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

/**
 * SQLite demo : Update & Delete record
 */
public class UpdateActivity extends SqlCommonActivity {

	private ImageUtility mImageUtility;
	
	// variable
	private int mId = 0; 

	/**
	 * === onCreate === 
	 */	    	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		execCreate();
		
		// hide create button		
		mButtonCreate.setVisibility( View.GONE );

		mImageUtility = new ImageUtility( this );
		showRocord();
	}
    
	/**
	 * show rocord 
	 */
	private void showRocord() {	
		// get id from bundle
		Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        
        // check bundle
        if ( bundle == null ) return;

		mId = bundle.getInt( BUNDLE_EXTRA_ID );
		// check id
		if ( mId == 0 ) return;

		// get record (Read)
		CardRecord record = mHelper.getRecordById( mId ) ; 
		int num = record.num;
		// set EditText
		mEditTextTag.setText( record.tag );
		mEditTextNum.setText( Integer.toString( num ) );
		mEditTextSet.setText( record.getSetString() );
		mImageUtility.showImageByNum( mImageView, num );
	}

	/**
	 * click UpdateButton
	 */	
    protected void clickUpdateButton() {
    	updateRecord();
    }

	/**
	 * click DeleteButton
	 */	
    protected void clickDeleteButton() {
		deleteRecord();
		disableButton();
		clearEditText();
    }

	/**
	 * update record 
	 */
    private void updateRecord() {    	
		// save to DB
		CardRecord r = new CardRecord( 
			mId,
			getEditTextTag(), 
			getEditTextNum(), 
			getEditTextSet() ) ;
        int ret = mHelper.update( r );         
        // message
        String msg = "Update Failed";
        if ( ret > 0 ) {
	    	msg = "Update Successful";       		
	     }	    	   
	     toast_short( msg );
	}

	/**
	 * delete record 
	 */
    private void deleteRecord() {   
    	// delete from DB
        int ret = mHelper.delete( mId ); 
        // message        
        if ( ret == 0 ) {
			toast_short( "Delete Failed" ); 
			return; 		
	    }
		toast_short( "Delete Successful" ); 
		finish();      	   
	}

	/**
	 * disable button	 
	 */
    private void disableButton() {
    	// disable click 	
		mButtonUpdate.setClickable( false );
		mButtonDelete.setClickable( false );
		// set gray color
		mButtonUpdate.setTextColor( Color.GRAY );
		mButtonDelete.setTextColor( Color.GRAY );
	}					
}
