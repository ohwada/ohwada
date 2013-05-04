package jp.ohwada.android.nfccconcentration;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Setting Activity 
 */
public class SettingActivity extends Activity {
	// customize 
	private int mModeAlready = Constant.MODE_ALREADY;
	private boolean isUseButtonBulk = Constant.USE_BUTTON_BULK;
	private boolean isUseButtonNum = Constant.USE_BUTTON_NUM;
							
	private final static int MODE_NONE = 0;	
	private final static int MODE_BULK = 1;	// default
	private final static int MODE_EACH = 2;
					
	// class object
	private CardHelper mHelper;		
	private ImageUtility mImageUtility;
	private PreferenceUtility mPreference;
    private NfcUtility mNfcUtility;
    		    
	// view component
	private LinearLayout mLinearLayoutButtons;        
	private TextView mTextViewCard;
	private ImageView mImageViewMain;
	private Spinner mSpinnerNum;
       
    private int mMode = MODE_NONE; 
	private int mCardNum = 0;	

	private boolean isShowSelection = false;

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
		mNfcUtility = new NfcUtility( this, getClass() );	

        mTextViewCard = (TextView) findViewById( R.id.textview_card );
        mImageViewMain = (ImageView) findViewById( R.id.imageview_main );
        mLinearLayoutButtons = (LinearLayout) findViewById( R.id.linearlayout_buttons );
                                
		// each button
		Button btnEach = (Button) findViewById( R.id.button_each );
		btnEach.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				clickEach();
			}
		});

		// bulk button
		Button btnBulk = (Button) findViewById( R.id.button_bulk );
		btnBulk.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				clickBulk();
			}
		});

		// list button
		Button btnList = (Button) findViewById( R.id.button_list );
		btnList.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			startListActivity();
			}
		});

		// video button
		Button btnVideo = (Button) findViewById( R.id.button_video );
		btnVideo.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			startVideoActivity();
			}
		});

		// dir button
		Button btnDir = (Button) findViewById( R.id.button_dir );
		btnDir.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			showDialogDir();
			}
		});

		// num button
		Button btnNum = (Button) findViewById( R.id.button_num );
		btnNum.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
	 			showDialogNum();
			}
		});

        Button btnClear = (Button) findViewById( R.id.button_clear );        
        btnClear.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				clickClear();
			}
		});

        Button btnStart = (Button) findViewById( R.id.button_start );        
        btnStart.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				finish();
			}
		});

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource( 
			android.R.layout.simple_spinner_dropdown_item );       
        adapter.add( getString( R.string.num_two ));
        adapter.add( getString( R.string.num_four ));
        adapter.add( getString( R.string.num_six ));
        adapter.add( getString( R.string.num_eight ));
        adapter.add( getString( R.string.num_ten ));
        
        if ( isUseButtonNum ) {
			adapter.add( getString( R.string.num_exceed ));
		}
        
        mSpinnerNum = (Spinner) findViewById( R.id.spinner_num );        
        mSpinnerNum.setAdapter( adapter );       
        mSpinnerNum.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected( AdapterView<?> arg0, View view, int position, long id ) {
				selectItem( position );
			}
			@Override
			public void onNothingSelected( AdapterView<?> arg0 ) {
				// dummy
			}
		});

// not use, for customize
		if ( ! isUseButtonBulk ) {
			btnBulk.setVisibility( View.GONE );
			btnEach.setVisibility( View.GONE );
		}
		if ( ! isUseButtonNum ) {
			btnNum.setVisibility( View.GONE );
		}
		
		showMenu();
		showSelection();	
	}

	/**
	 * set each mode when click Each
	 */		
	private void clickEach() {
		mMode = MODE_EACH;
		showPrepare();
	}

	/**
	 * set buld mode when click Bulk
	 */	
	private void clickBulk() {
		mMode = MODE_BULK;
		showPrepare();
	}

	/**
	 * delete all cards when click Clear
	 */	
	private void clickClear() {
		mHelper.deleteAll();
		showMenu();
	}

	/**
	 * set number of cards when select Item
	 * ＠param int position 
	 */				
	private void selectItem( int position ) {
		if ( isShowSelection ) {
			isShowSelection = false;
			return;
		}

		int num = 0;
		switch( position ) {
			case 0:
				num = 2;
				break;
			case 1:
				num = 4;
				break;
			case 2:
				num = 6;
				break;
			case 3:
				num = 8;
				break;
			case 4:
				num = 10;
				break;
		}
		if ( num > 0 ) {
			setCardNum( num );
		} else {
			showSelection();
			toast_short( R.string.msg_manual );
		}
		showMenu();
	}

	/**
	 * setCardNum
	 */				
	private void setCardNum( int num ) {
		if ( mPreference.getNum() == num ) return;
		mPreference.setNum( num );
	}
	
	/**
	 * showSelection
	 */				
	private void showSelection() {
		isShowSelection = true;
        int num = mPreference.getNum();        
        switch( num ) {
        	case 2:
        		mSpinnerNum.setSelection( 0 );
        		break;       		
        	case 4:
        		mSpinnerNum.setSelection( 1 );
        		break;       		
        	case 6:
        		mSpinnerNum.setSelection( 2 );
        		break;        		
        	case 8:
        		mSpinnerNum.setSelection( 3 );
        		break;        		
        	case 10:
        		mSpinnerNum.setSelection( 4 );
        		break;
        	default:
        		mSpinnerNum.setSelection( 5 );
        		break;
        }
	}
	
	/**
	 * prepare Scan card
	 */	
	private void showPrepare() {		
		mTextViewCard.setText( geMsgScanCard() );
		mTextViewCard.setTextColor( Color.BLACK );
		mLinearLayoutButtons.setVisibility( View.GONE );
		mImageViewMain.setImageResource( R.drawable.white );
	}

	/**
	 * showMenu
	 */	
    private void showMenu() {
    	mMode = MODE_NONE; 
		mTextViewCard.setText( geMsgScanCard() );
		mTextViewCard.setTextColor( Color.BLACK );
		mLinearLayoutButtons.setVisibility( View.VISIBLE );
		mImageViewMain.setImageDrawable( null );
    }
			
	/**
	 * geMsgScanCard
	 * @return String
	 */	
	private String geMsgScanCard() {
		String msg = "";
		mCardNum = mPreference.getNum();
		List<CardRecord> lists = mHelper.getRecordList( mCardNum );
		if( lists != null ) {
			int size = lists.size();
			if( size < mCardNum ) {
				msg = getMsgCardIn( size, mCardNum );
			} else {
				msg = getString( R.string.msg_all_already );
			}
		} else {
			msg = getMsgCardIn( 0, mCardNum );
		}
		return msg;
	}

	/**
	 * getMsgCardIn
	 * @param int size
	 * @param int num
	 * @return String
	 */	
	private String getMsgCardIn( int size, int num ) {
		String msg = getString( R.string.msg_please );
		String format = getString( R.string.msg_card_in );
   		msg += String.format( format, size, num );
   		return msg;
	}
						 
	/**
	 * === onResume ===
	 */							
    @Override
    public void onResume() {
        super.onResume();
		mNfcUtility.enable();
    }

	/**
	 * === onPause ===
	 */
    @Override
    public void onPause() {
        super.onPause();
		if ( isFinishing() ) {
		 	mNfcUtility.disable();
		}
    } 
    
	/**
	 * === onNewIntent ===
	 * @param Intent intent
	 */
    @Override
    public void onNewIntent( Intent intent ) { 
		String tag = mNfcUtility.intentToTagID( intent );
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
				showMenu();
				showDialogAlready( record );
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
			showDialogFull( tag );
			return;
		}
 		int set = ( num + 1 ) / 2;	
		CardRecord r = new CardRecord( tag, num, set );
        long ret = mHelper.insert( r ); 
        if ( ret > 0 ) {
        	String msg = getString( R.string.msg_registered ) + " " + num + " " + tag ;
	    	mTextViewCard.setText( msg );
	    	mTextViewCard.setTextColor( Color.BLUE );
			mImageViewMain.setImageBitmap( 
				mImageUtility.getBitmapByNum( num ) );
			if ( mHelper.getRecordCount() >= mPreference.getNum() ) {
				showDialogFinish();	
			}  		
	     } else {
	     	String msg = getString( R.string.msg_add_fail ) + " " + tag;
			toast_short( msg );    	   
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
	 * delete record 
	 * @param int id
	 */
    private void deleteRecord( int id ) {   
    	// delete from DB
        int ret = mHelper.delete( id ); 
        // message        
        if ( ret > 0 ) {
			toast_short( R.string.delete_success );       		
	     } else {	    	   
	    	 toast_short( R.string.delete_fail );
	     }   	   
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
		String msg = getString( R.string.dialog_dir ) + " " + dir;

		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( msg )
			.setPositiveButton( R.string.button_cancel, null )
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
		final EditText et = new EditText( this );
		et.setText( 
			Integer.toString( mPreference.getNum() ) );
        et.setTransformationMethod( 
        	SingleLineTransformationMethod.getInstance() );
        et.setRawInputType( InputType.TYPE_CLASS_NUMBER );
        
		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( R.string.dialog_num )
			.setView( et )
			.setCancelable( true )
			.setNegativeButton( R.string.button_cancel, null )
			.setPositiveButton( R.string.button_set, 
				new AlertDialog.OnClickListener() {
					public void onClick( DialogInterface dialog, int id ) {
						int num = Integer.parseInt( et.getText().toString() );
						setCardNum( num );
						showSelection();
						showMenu();
               		}
           		})
			.create();
		dialog.show();
	}
	
	/**
	 * showDialogAlready
	 * @param CardRecord record 
	 */	
	private void showDialogAlready( CardRecord record  ) {
		final int id = record.id;
		String msg = "tag = " + record.tag + "<br><br>";
		Spanned spanned = mImageUtility.getHtmlImage( msg, record.num );

		int res_id;
		switch ( mModeAlready ) {
			case Constant.MODE_ALREADY_UPDATE:
				res_id = R.string.button_edit;
				break;
			case Constant.MODE_ALREADY_DELETE:
			default:
				res_id = R.string.button_delete;
				break;
		}

		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( R.string.msg_already )
			.setMessage( spanned )
			.setCancelable( true )		               
			.setNegativeButton( R.string.button_close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
						// close
                    }
                })            
			.setPositiveButton( res_id,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                    	clickAlready( id );
                    }
                })
			.create();
		dialog.show();
    }

	/**
	 * clickAlready
	 * @param int id
	 */	
	private void clickAlready( int id ) {
		switch ( mModeAlready ) {
			case Constant.MODE_ALREADY_UPDATE:
				startUpdateActivity( id );
				break;
			case Constant.MODE_ALREADY_DELETE:
			default:
				deleteRecord( id );
				showMenu();
				break;
		}
	}
	
	/**
	 * showDialogFull
	 * @param String tag
	 */	
	private void showDialogFull( String tag ) {
		String msg = "tag = " + tag;

		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( R.string.msg_full )
			.setMessage( msg )
			.setCancelable( true )		               
			.setNegativeButton( R.string.button_close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						// close
               		}
			})
			.create();
		dialog.show();
	} 
 
 	/**
	 * showDialogFinish
	 */	
	private void showDialogFinish() {
		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( R.string.msg_finish )
			.setCancelable( true )		               
			.setPositiveButton(  R.string.button_start,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
						finish();
                    }
                })
			.setNegativeButton( R.string.button_close,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						showMenu();
               		}
			})
			.create();
		dialog.show();
	}
	
// --- start Activity ---	
	/**
	 * start ListActivity
	 */	
	private void startListActivity() {	
		Intent intent = new Intent( this, CardListActivity.class );
		startActivityForResult( intent, Constant.REQUEST_CODE_LIST );
	}
		
	/**
	 * start VideoActivity
	 */	
	private void startVideoActivity() {	
		Intent intent = new Intent( this, VideoActivity.class );
		startActivityForResult( intent, Constant.REQUEST_CODE_VIDEO );
	}

	/**
	 * start CreateActivity
	 * @param String tag_id
	 */			    
	private void startCreateActivity( String tag_id ) {
		Intent new_intent = new Intent( this, CreateActivity.class );
		Bundle bandle = new Bundle();
		bandle.putString( Constant.BUNDLE_EXTRA_TAG, tag_id );
		new_intent.putExtras( bandle );
		startActivityForResult( new_intent, Constant.REQUEST_CODE_CREATE );
	}

	/**
	 * start update activity with id
	 * @param int id
	 * @return void	 
	 */
	private void startUpdateActivity( int id ) {
	    Intent intent = new Intent( this, UpdateActivity.class );
	    Bundle bandle = new Bundle();
	    bandle.putInt( Constant.BUNDLE_EXTRA_ID, id );
	    intent.putExtras( bandle );
		startActivityForResult( intent, Constant.REQUEST_CODE_UPDATE );
	}
		
	/**
	 * toast short
	 * @param int id
	 */ 
	private void toast_short( int id ) {
		ToastMaster.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}
		
	/**
	 * toast short
	 * @param String msg
	 */ 
	private void toast_short( String msg ) {
		ToastMaster.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}

}
