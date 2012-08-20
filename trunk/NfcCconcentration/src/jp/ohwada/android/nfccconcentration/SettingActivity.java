package jp.ohwada.android.nfccconcentration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Setting Activity : add card
 */
public class SettingActivity extends NfcCommonActivity {

	private final static int MODE_NONE = 0;	
	private final static int MODE_BULK = 1;	// default
	private final static int MODE_EACH = 2;
					
	// class object
	private CardHelper mHelper;		
	private ImageUtility mImageUtility;
	private PreferenceUtility mPreference;
    
	// view component
	private LinearLayout mLinearLayoutButtons;        
	private TextView mTextViewTitle;
	private ImageView mImageViewMain;
    private Button mButtonEach;
    private Button mButtonBulk;
    private Button mButtonList;
    private Button mButtonVideo;
    private Button mButtonDir;
    private Button mButtonNum;
       
    private int mMode = MODE_NONE; 
	private int mCardNum = 0;	
	
	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */											             
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );

		mHelper = new CardHelper( this );	
		mImageUtility = new ImageUtility( this );	
		mPreference = new PreferenceUtility( this );

        mTextViewTitle = (TextView) findViewById( R.id.textview_title );
        mImageViewMain = (ImageView) findViewById( R.id.imageview_main );
        mLinearLayoutButtons = (LinearLayout) findViewById( R.id.linearlayout_buttons );
                        
		// each button
		mButtonEach = (Button) findViewById( R.id.button_each );
		mButtonEach.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				clickEach();
			}
		});

		// bulk button
		mButtonBulk = (Button) findViewById( R.id.button_bulk );
		mButtonBulk.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				clickBulk();
			}
		});

		// list button
		mButtonList = (Button) findViewById( R.id.button_list );
		mButtonList.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			startListActivity();
			}
		});

		// video button
		mButtonVideo = (Button) findViewById( R.id.button_video );
		mButtonVideo.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			startVideoActivity();
			}
		});

		// dir button
		mButtonDir = (Button) findViewById( R.id.button_dir );
		mButtonDir.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			showDialogDir();
			}
		});

		// num button
		mButtonNum = (Button) findViewById( R.id.button_num );
		mButtonNum.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			showDialogNum();
			}
		});
				
		showMenu();		
		prepareIntent();
	}

	/**
	 * click Each
	 */		
	private void clickEach() {
		mMode = MODE_EACH;
		showPrepare();
	}

	/**
	 * click Bulk
	 */	
	private void clickBulk() {
		mMode = MODE_BULK;
		showPrepare();
	}

	/**
	 * prepare Scan card
	 */	
	private void showPrepare() {
		mTextViewTitle.setText( "Please scan card" );
		mTextViewTitle.setTextColor( Color.BLACK );
		mLinearLayoutButtons.setVisibility( View.GONE );
		mImageViewMain.setImageResource( R.drawable.white );
	}

	/**
	 * showMenu
	 */	
    private void showMenu() {
    	mMode = MODE_NONE; 
		mCardNum = mPreference.getNum();
		mTextViewTitle.setText( "Add Card : Please scan card" );
		mTextViewTitle.setTextColor( Color.BLACK );
		mLinearLayoutButtons.setVisibility( View.VISIBLE );
		mImageViewMain.setImageDrawable( null );
    }
 
	/**
	 * === onResume ===
	 */							
    @Override
    public void onResume() {
        super.onResume();
        enableForegroundDispatch();
    }

	/**
	 * === onNewIntent ===
	 * @param Intent intent
	 */
    @Override
    public void onNewIntent( Intent intent ) { 
		String tag = intentToTagID( intent );
        log_d( "Discovered tag with intent: " + intent );
		log_d( "tag: " + tag  );

		if ( mMode == MODE_NONE ) {
			clickBulk();
		}
	
		CardRecord record = mHelper.getRecordByTag( tag );
		if ( record == null ) {
			if ( mMode == MODE_BULK ) {
				addCardBulk( tag );
			} else {
				startCreateActivity( tag );
			}
		} else {
			if ( mMode == MODE_BULK ) {
				showAlready( record );
			} else {
				startUpdateActivity( record.id );
			}
        }
    }

	/**
	 * addCardBulk
	 * @param String tag
	 */	
	private void addCardBulk( String tag ) {
		int num = getNewCardNum();
		if ( num == 0 ) {
			toast_short( "Cards are full" );
			return;
		}
 		int set = ( num + 1 ) / 2;	
		CardRecord r = new CardRecord( tag, num, set );
        long ret = mHelper.insert( r ); 
        if ( ret > 0 ) {
	    	mTextViewTitle.setText( "Add Card: " + num + " " + tag );
	    	mTextViewTitle.setTextColor( Color.BLUE );
			mImageUtility.showImageByNum( mImageViewMain, num );      		
	     } else {
	    	 toast_short( "Add Card Failed " + tag );    	   
	    }
	}

	/**
	 * get NewCardNum
	 * @return int
	 */	
	private int getNewCardNum() {
		for ( int i=1; i <= mCardNum; i++ ) {
			CardRecord r = mHelper.getRecordByNum( i ); 
			// return num if not exist
			if ( r == null ) return i;
		}
		return 0;
	}
		
	/**
	 * showAlready
	 * @param CardRecord record
	 */	
    private void showAlready( CardRecord record ) {
		String tag_id = record.tag;
		int num = record.num;
		mTextViewTitle.setText( "Aleady Card: " + tag_id );
		mTextViewTitle.setTextColor( Color.RED );
		mImageUtility.showImageByNum( mImageViewMain, num ); 
	}

	/**
	 * === onPause ===
	 */
    @Override
    public void onPause() {
        super.onPause();
        disableForegroundDispatch();
    } 
    
	/**
	 * === onCreateOptionsMenu ===
	 * @param Menu menu
	 * @return boolean
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.add, menu );
        return true;
    }

	/**
	 * === onOptionsItemSelected ===
	 * @param MenuItem item
	 * @return boolean
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_restart:
				finish();
				return true;
			case R.id.menu_setting:
				showMenu();
				return true;
        }
		return false;
    }
    
	/**
	 * === onActivityResult ===
	 * @param int requestCode
	 * @param int int resultCode
	 * @param Intent data 
	 */
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
		showMenu();
    }

// AlertDialog
	/**
	 * showDialog : save dir
	 */
	private void showDialogDir() {
		String dir = mPreference.getDir();    
		final String[] items = mImageUtility.getSubDirs();
		String msg = "Sub Directory: " + dir;
		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( msg )
			.setPositiveButton( "Cancel", null )
			.setCancelable( true )
			.setItems( items, new DialogInterface.OnClickListener() {
				public void onClick( DialogInterface dialog, int whitch ) {
					mPreference.setDir( items[ whitch ] );
   				}
			})
			.create();
		dialog.show();
	}
		
	/**
	 * showDialog : save num
	 */
	private void showDialogNum() { 
		int num = mPreference.getNum();   
		final EditText et = new EditText( this );
		et.setText( Integer.toString( num ) );
		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( "Card Num" )
			.setView( et )
			.setCancelable( true )
			.setNegativeButton( "Cancel", null )
			.setPositiveButton( "Set", new AlertDialog.OnClickListener() {
				public void onClick( DialogInterface dialog, int id ) {
					String str = et.getText().toString();
					mPreference.setNum( str );	
               	}
           	})
			.create();
		dialog.show();
	}	
}
