package jp.ohwada.android.nfccconcentration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * SQLite demo : Create record
 */
public class CreateActivity extends SqlCommonActivity {
	
	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */	    	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		execCreate();

		// hide update & delete button
		mImageView.setVisibility( View.GONE );
	   	mButtonUpdate.setVisibility( View.GONE );
	   	mButtonDelete.setVisibility( View.GONE );

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

        String tag = bundle.getString( BUNDLE_EXTRA_TAG );           
		// check id
		if ( "".equals( tag ) ) return;

		// set EditText
		mEditTextTag.setText( tag );
	}
	
	/**=
	 * clickCreateButton
	 */	
    protected void clickCreateButton() {
		createRecord();
		clearEditText();
    }

	/**
	 * create record 
	 */
    private void createRecord() {   
		// get value
		String tag = getEditTextTag();
		if ( "".equals(tag) ) {
		     toast_short( "Please entry tag" );
		     return;
		}

		// save to DB
		CardRecord r = new CardRecord( 
			tag, 
			getEditTextNum(), 
			getEditTextSet() ) ;
        long ret = mHelper.insert( r );        
        // message
        String msg = "Insert Failed";
        if ( ret > 0 ) {
	    	msg = "Insert Successful";       		
	     }	    	   
	     toast_short( msg );
	}	
}
