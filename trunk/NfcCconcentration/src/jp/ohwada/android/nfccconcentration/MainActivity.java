package jp.ohwada.android.nfccconcentration;

import android.content.Intent;
import android.graphics.Color;
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

/*
 * main Activity
 */
public class MainActivity extends NfcCommonActivity {

    private static final int MSG_WHAT = Constant.MSG_WHAT_FINISH;
	private static final String IMAGE_NAME_START = Constant.IMAGE_NAME_START;
	private static final String IMAGE_NAME_COMPLETE = Constant.IMAGE_NAME_COMPLETE;
	private final static String VIDEO_NAME_COMPLETE = Constant.VIDEO_NAME_COMPLETE;

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
		prepareIntent();
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
		mTimeView.showTime() ;
		mTextViewLeft.setText( "Please scan card" );
		mTextViewLeft.setTextColor( Color.BLACK );
		mTextViewCenter.setText( "" );
		mTextViewCenter.setTextColor( Color.BLACK );
		mImageUtility.restart();
		mImageUtility.showImage( mImageViewPhoto1, IMAGE_NAME_START );
		mImageViewPhoto2.setImageDrawable( null );
	}

	/**
	 * showComplete
	 */				
    private void showComplete() {
    	mStatus = STATUS_COMPLETE ;
		mTimeView.showTime() ;
		mTextViewLeft.setText( "Complete" );
		mTextViewLeft.setTextColor( Color.BLUE );
		mTextViewCenter.setText( "" );
		mTextViewCenter.setTextColor( Color.BLACK );
		mImageUtility.showImage( mImageViewPhoto1, IMAGE_NAME_COMPLETE );
		mImageViewPhoto2.setImageDrawable( null );
	}

	/**
	 * showFinish
	 */	
    private void showFinish() {
		stopGame();
		mTimeView.saveTime() ;
		mHandler.sendMessageDelayed( Message.obtain(mHandler, MSG_WHAT), INTERVAL ); 
	}

	/**
	 * clickPhoto1
	 */	
    private void clickPhoto1() {
		if ( mStatus == STATUS_PREVIEW ) {
			showPreview();					
		}
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
		// noting to do if not start
		if (( mStatus == STATUS_STOP )||( mStatus == STATUS_COMPLETE )) {
			return;
		}

		String tag = intentToTagID( intent );
		CardRecord record = mHelper.getRecordByTag( tag );

		// warning if not card
		if ( record == null ) {
			toast_short( "Not Card " + tag );
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
		mTextViewLeft.setText( "First Card: " + tag );
		mTextViewCenter.setText( "" );
		mImageUtility.showImageByNum( mImageViewPhoto1, num );
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

		mTextViewCenter.setText( "Match" );
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
		mTextViewCenter.setText( "unmatch" );
		mTextViewCenter.setTextColor( Color.RED );
	}

	/**
	 * showSecondSame
	 * @param CardRecord record
	 */ 
    private void showSecondSame( CardRecord record ) {
		showSecondCommon( record );
		mTextViewCenter.setText( "same card" );
		mTextViewCenter.setTextColor( Color.RED );
    }
	
	/**
	 * showSecond
	 * @param CardRecord record
	 */ 
    private void showSecondCommon( CardRecord record ) {
		mTextViewLeft.setText( "Second Card: " + record.tag );
		mImageUtility.showImageByNum( mImageViewPhoto1, mNum1 );
		mImageUtility.showImageByNum( mImageViewPhoto2, record.num );
		mCardSet = CARD_SET_1; 
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
			case R.id.menu_restart:
				showPreview();
				return true;
			case R.id.menu_finish:
				finish();
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
}
