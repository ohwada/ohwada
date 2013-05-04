package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * main Activity
 */
public class MainActivity extends Activity {
	// dubug
	private final static boolean D = Constant.DEBUG; 

    private static final int MSG_WHAT = Constant.MSG_WHAT_FINISH;
	private static final String IMAGE_NAME_START = Constant.IMAGE_NAME_START;
	private static final String IMAGE_NAME_COMPLETE = Constant.IMAGE_NAME_COMPLETE;
	private final static String VIDEO_NAME_COMPLETE = Constant.VIDEO_NAME_COMPLETE;
	private String mUrlWeb = Constant.URL_WEB;

	// about
	private final static String URL_USAGE = 
		"http://android.ohwada.jp/nfc_cconcentration";
	
    private static final int INTERVAL = 3000; // 3 sec;
    private static final int VIBRATE_TIME = 500;	// 0.5 sec
	private final static int STATUS_PREVIEW = 0;
	private final static int STATUS_START = 1;
	private final static int STATUS_STOP = 2;	
	private final static int STATUS_COMPLETE = 3;	
	private final static int CARD_SET_1 = 1;
	private final static int CARD_SET_2 = 2;
		
	// class object
	private CardHelper mHelper;		
	private Vibrator mVibrator;
	private PreferenceUtility mPreference;
	
	private ImageView mImageViewPhoto1;
	private ImageView mImageViewPhoto2; 
	private TextView mTextViewLeft;
	private TextView mTextViewCenter;
	private TextView mTextViewTime;

    private TimeView mTimeView;
	private ImageUtility mImageUtility;
	private FileUtility mFileUtility;
    private NfcUtility mNfcUtility;
    
	private int mCardNum = 0;		    
	private boolean[] mCardNumArray = null;
	private int mStatus = STATUS_PREVIEW ;
	private int mCardSet = CARD_SET_1;
	private int mNum1 = 0;
	private int mSet1 = 0;

	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */											             
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

		mVibrator = (Vibrator) getSystemService( VIBRATOR_SERVICE );
		mPreference = new PreferenceUtility( this );
		mHelper = new CardHelper( this );	
		mImageUtility = new ImageUtility( this );	
		mFileUtility = new FileUtility( this );	
		mNfcUtility = new NfcUtility( this, getClass() );	
		
        mImageViewPhoto1 = (ImageView) findViewById( R.id.imageview_photo_1 );
		mImageViewPhoto2 = (ImageView) findViewById( R.id.imageview_photo_2 );
        mTextViewLeft = (TextView) findViewById( R.id.textview_left );
		mTextViewCenter = (TextView) findViewById( R.id.textview_center );
		mTextViewTime = (TextView) findViewById( R.id.textview_time );
		mTimeView = new TimeView( this, mTextViewTime );

		mImageViewPhoto1.setOnClickListener(new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				clickPhoto1();
			}
		});
		
		mFileUtility.init();	
		showPreview();
	}

	/**
	 * startGame
	 */		
    private void startGame() {
		mStatus = STATUS_START ;
		mTimeView.start();
		mCardNum = mPreference.getNum();
		mCardNumArray = new boolean[ mCardNum + 1 ];
		for ( int i=0; i <= mCardNum; i++ ) {
			mCardNumArray[i] = false;
		}
	}
		
	/**
	 * stopGame
	 */	
    private void stopGame() {
		mStatus = STATUS_STOP ;
		mTimeView.stop();
	}

	/**
	 * isFinishGame
	 * @return boolean
	 */	
    private boolean isFinishGame() {
    	for ( int i=1; i <= mCardNum; i++ ) {
			if ( !mCardNumArray[i] ) return false;
		}
		return true;
	}

	/**
	 * showPreview
	 */				
    private void showPreview() {
    	mStatus = STATUS_PREVIEW ;
    	mTimeView.stop();
		mTimeView.showTime() ;
		mTextViewCenter.setText( "" );
		mTextViewCenter.setTextColor( Color.BLACK );
		mImageUtility.restart();
		mImageViewPhoto2.setImageDrawable( null );		
		if ( mHelper.getRecordCount() >= mPreference.getNum() ) {
			// when the number of cards is enough
			mTextViewLeft.setText( R.string.please_scan );
			mTextViewLeft.setTextColor( Color.BLACK );
		} else {
			// when not enough
			mTextViewLeft.setText( R.string.please_register );
			mTextViewLeft.setTextColor( Color.RED );
		}
		Bitmap bitmap = mImageUtility.getBitmap( IMAGE_NAME_START );
		if ( bitmap != null ) {
			// if image exists 
			mImageViewPhoto1.setImageBitmap( bitmap );
		} else {
			// default if not exists
			mImageViewPhoto1.setImageResource( R.drawable.image_start );
		}
	}

	/**
	 * showComplete
	 */				
    private void showComplete() {
    	mStatus = STATUS_COMPLETE ;
		mTimeView.showTime() ;
		mTextViewLeft.setText( R.string.geme_complete );
		mTextViewLeft.setTextColor( Color.BLUE );
		mTextViewCenter.setText( "" );
		mTextViewCenter.setTextColor( Color.BLACK );
		mImageViewPhoto2.setImageDrawable( null );
		Bitmap bitmap = mImageUtility.getBitmap( IMAGE_NAME_COMPLETE );
		if ( bitmap != null ) {
			// if image exists 
			mImageViewPhoto1.setImageBitmap( bitmap );
		} else {
			// default if not exists
			mImageViewPhoto1.setImageResource( R.drawable.image_complete );
		}
	}
		
	/**
	 * showFinish
	 */	
    private void showFinish() {
		stopGame();
		mTimeView.saveTime() ;
		mHandler.sendMessageDelayed( Message.obtain( mHandler, MSG_WHAT ), INTERVAL ); 
	}

	/**
	 * clickPhoto1
	 */	
    private void clickPhoto1() {
    	switch( mStatus ) {
			case STATUS_PREVIEW:
				if ( mUrlWeb.length() > 0 ) {
					startBrawser( mUrlWeb );
				}
				break;
			case STATUS_COMPLETE:
				showPreview();	
				break;
		}
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
    	// start Setting if no card
		if ( mHelper.getRecordCount() == 0 ) {
			startSettingActivity();
			return;
		}
		// noting to do if not start
		if (( mStatus == STATUS_STOP )||(
		  mStatus == STATUS_COMPLETE )) return;
		// get tag, noting to do if invalid tag
		String tag = mNfcUtility.intentToTagID( intent );
		if ( tag == null ) return;
		// read card, warning if invalid card
		CardRecord record = mHelper.getRecordByTag( tag );
		if ( record == null ) {
			toast_short( R.string.not_registered );
			return;
		}
		// start if preview
		if ( mStatus == STATUS_PREVIEW ) {
			startGame();
		}        
		switch ( mCardSet ) {
			case CARD_SET_2:
				showSecond( record );
				break;
			case CARD_SET_1:
			default:
				showFirst( record );
				break;
		}
	}

	/**
	 * showFirst
	 * @param CardRecord record
	 */ 
    private void showFirst( CardRecord record ) {
		String tag = record.tag;
		int num = record.num;
		int set = record.set;
		mNum1 = num;
		mSet1 = set;
		String msg = getString( R.string.first_card ) + " " + tag ;
		mTextViewLeft.setText( msg );
		mTextViewCenter.setText( "" );
		mImageViewPhoto1.setImageBitmap( 
			mImageUtility.getBitmapByNum( num ) );
		mImageViewPhoto2.setImageResource( R.drawable.white );
		mCardSet = CARD_SET_2; 
	}

	/**
	 * showSecond
	 * @param CardRecord record
	 */ 
    private void showSecond( CardRecord record ) {
		if ( record.num == mNum1 ) {
			showSecondSame( record );

		} else if ( record.set == mSet1 ) {
			showSecondMatch( record );

		} else {
			showSecondUnmatch( record );
		}
	}
   
	/**
	 * showSecondMatch
	 * @param CardRecord record
	 */ 
    private void showSecondMatch( CardRecord record ) {
    	showSecondCommon( record );

		mTextViewCenter.setText( R.string.pieces_match );
		mTextViewCenter.setTextColor( Color.BLUE );
		mCardNumArray[ record.num ] = true;
		mCardNumArray[ mNum1 ] = true;
		mVibrator.vibrate( VIBRATE_TIME );

		if ( isFinishGame() ) {
			showFinish();
		}
	}

	/**
	 * showSecondUnmatch
	 * @param CardRecord record
	 */ 
    private void showSecondUnmatch( CardRecord record ) {
    	showSecondCommon( record );
		mTextViewCenter.setText( R.string.pieces_unmatch );
		mTextViewCenter.setTextColor( Color.RED );
	}

	/**
	 * showSecondSame
	 * @param CardRecord record
	 */ 
    private void showSecondSame( CardRecord record ) {
		showSecondCommon( record );
		mTextViewCenter.setText( R.string.same_card );
		mTextViewCenter.setTextColor( Color.RED );
    }
	
	/**
	 * showSecond
	 * @param CardRecord record
	 */ 
    private void showSecondCommon( CardRecord record ) {
    	String msg = getString( R.string.second_card ) + " " + record.tag ;
		mTextViewLeft.setText( msg );
		mImageViewPhoto1.setImageBitmap( 
			mImageUtility.getBitmapByNum( mNum1 ) );
		mImageViewPhoto2.setImageBitmap( 
			mImageUtility.getBitmapByNum( record.num ) );						
		mCardSet = CARD_SET_1; 
	}
        
	/**
	 * === onCreateOptionsMenu ===
	 * @param Menu menu
	 * @return boolean
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
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
			case R.id.menu_about:
				showAboutDialog();
				return true;
			case R.id.menu_usage:
				startBrawser( URL_USAGE );
				return true;
			case R.id.menu_restart:
				showPreview();
				return true;
			case R.id.menu_setting:
				startSettingActivity();
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
		showPreview();
    }

// --- message handler ---   
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
        	updateFinish();
        }
    };
   
    /**
     * update Finish : show video
     */	
    private synchronized void updateFinish() {
    	// show video if video exists
    	if ( mImageUtility.existsFile( VIDEO_NAME_COMPLETE ) ) {
    		startVideoActivity();
    	} else {
    		showComplete();
    	}
    }

	/**
     * showAboutDialog
     */
	private void showAboutDialog() {
		AboutDialog dialog = new AboutDialog( this );
		dialog.show();
	}

	/**
	 * toast short
	 * @param int id
	 */ 
	private void toast_short( int id ) {
		ToastMaster.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}
    
// --- start Activity ---	
	/**
	 * start VideoActivity
	 */	
	private void startVideoActivity() {	
		Intent intent = new Intent( this, VideoActivity.class );
		startActivityForResult( intent, Constant.REQUEST_CODE_VIDEO );
	}

	/**
	 * start AddActivity
	 */	
	private void startSettingActivity() {	
		Intent intent = new Intent( this, SettingActivity.class );
		startActivityForResult( intent, Constant.REQUEST_CODE_SETTING );
	}

	/**
	 * startBrawser
	 * @param String url
	 */ 
    private void startBrawser( String url ) {
		if (( url == null )|| url.equals("") ) return;
    	try {
    		Uri uri = Uri.parse( url );
    		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
    		startActivity( intent );
    	} catch( Exception e ) {
			if (D) e.printStackTrace();
    	}
    }
	
}
