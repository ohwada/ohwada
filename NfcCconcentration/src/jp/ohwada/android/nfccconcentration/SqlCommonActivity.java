package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * SQLite Common Activity
 */
public class SqlCommonActivity extends Activity {

	// dubug
	protected final static String TAG_SUB = "SqlCommon : ";
	protected final static String TAG = Constant.TAG;
	protected final static boolean D = Constant.DEBUG; 
    
	protected final static String BUNDLE_EXTRA_ID  = Constant.BUNDLE_EXTRA_ID;
	protected final static String BUNDLE_EXTRA_TAG  = Constant.BUNDLE_EXTRA_TAG;
	
	// class object
	protected CardHelper mHelper;
	
	// view conponent
	protected EditText mEditTextTag;
	protected EditText mEditTextNum;
	protected EditText mEditTextSet;
	protected Button mButtonCreate;
	protected Button mButtonUpdate;
	protected Button mButtonDelete;
	protected Button mButtonBack;
	protected ImageView mImageView;
			
// === onCreate ===
	/**=
	 * execCreate
	 */	
	protected void execCreate() {
		setContentView( R.layout.activity_record );
		// helper
		mHelper = new CardHelper( this );
		// view conponent
    	mEditTextTag = (EditText) findViewById( R.id.rec_edittext_tag );
    	mEditTextNum = (EditText) findViewById( R.id.rec_edittext_num );
		mEditTextSet = (EditText) findViewById( R.id.rec_edittext_set );
		mButtonCreate = (Button) findViewById( R.id.rec_button_create );
	   	mButtonUpdate = (Button) findViewById( R.id.rec_button_update );
	   	mButtonDelete = (Button) findViewById( R.id.rec_button_delete );
		mButtonBack = (Button) findViewById( R.id.rec_button_back );
		mImageView = (ImageView) findViewById( R.id.rec_imageview );
					   	
		// create button	   	
		mButtonCreate.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickCreateButton();
			}
		});

		// update button
		mButtonUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickUpdateButton();
			}
		});

		// delete button
		mButtonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickDeleteButton();
			}
		});
		
		// back button	 
		mButtonBack.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * click CreateButton
	 */	
    protected void clickCreateButton() {
    	// dummy;
    }

	/**
	 * click UpdateButton
	 */	
    protected void clickUpdateButton() {
    	// dummy;
    }
    
	/**
	 * click DeleteButton
	 */	
    protected void clickDeleteButton() {
    	// dummy;
    }
    
	/**=
	 * get EditTextTag
	 * @return String 
	 */					
    protected String getEditTextTag() {
    	return mEditTextTag.getText().toString();
    }

	/**=
	 * get EditTextNum
	 * @return int
	 */		
    protected int getEditTextNum() {
    	String str = mEditTextNum.getText().toString();
    	if ( "".equals(str) ) return 0;
		return Integer.parseInt( str );
	}

	/**=
	 * get EditTextSet
	 * @return int
	 */	
    protected int getEditTextSet() {
    	String str = mEditTextSet.getText().toString();
    	if ( "".equals(str) ) return 0;
		return Integer.parseInt( str );
	}

	/**
	 * clear EditText
	 * @param none
	 * @return void	 
	 */				
    protected void clearEditText() {	
		mEditTextTag.setText("");
		mEditTextNum.setText("");
		mEditTextSet.setText("");
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
		Toast.makeText (this, msg, Toast.LENGTH_SHORT ).show();
	}
}
