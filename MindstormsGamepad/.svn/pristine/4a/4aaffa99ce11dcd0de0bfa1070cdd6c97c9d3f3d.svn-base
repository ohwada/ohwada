package jp.ohwada.android.mindstormsgamepad;

import java.util.ArrayList;

import jp.ohwada.android.mindstormsgamepad.util.WordList;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Voice Activity
 */
public class VoiceActivity extends CommonActivity {

	/** Debug */
	protected String TAG_SUB = "VoiceActivity";

	// words.txt
    private static final int GROUP_FORWARD = 0; 
    private static final int GROUP_BACK = 1;
	private static final int GROUP_LEFT = 2; 
	private static final int GROUP_RIGHT = 3; 

	private static final int[] GROUP_TO_DERECTION = {
		Constant.DIRECTION_FORWARD,
		Constant.DIRECTION_BACK,
		Constant.DIRECTION_LEFT,
		Constant.DIRECTION_RIGHT
	}; 
	
	// 1 sec
	private static final long DURATION_TIME = 1000L;
   
	/* view component */
	private TextView mTextViewForward;
	private TextView mTextViewBack;
	private TextView mTextViewLeft;
	private TextView mTextViewRight;			
	private TextView mTextViewResult;
	private ImageView mImageViewMic;
    		
	// object
	private WordList mWordList;
		
// --- onCreate ---
	/**
	 * === onCreate ===
	 */													    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
		initTabSub( TAG_SUB );	
		log_d( "onCreate" );   
        super.onCreate( savedInstanceState );
		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_voice, null );
        setContentView( view );

        /* Initialization of Bluetooth */
		initManager( view );
		setTitleName( R.string.activity_voice );
		initSeekbarPower( view ); 
		initButtonBack();

		/* view component */
  		mTextViewForward = (TextView) findViewById( R.id.textview_forward );
		mTextViewBack = (TextView) findViewById( R.id.textview_back );
		mTextViewLeft = (TextView) findViewById( R.id.textview_left );
		mTextViewRight = (TextView) findViewById( R.id.textview_right );
		mTextViewResult = (TextView) findViewById( R.id.textview_result );

		/* Image mic */  
		mImageViewMic = (ImageView) findViewById( R.id.imageview_mic );
		mImageViewMic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
            	execClickMic( view );
           }
        });
       
		// WordList
		mWordList = new WordList( this );
		mWordList.load();
		// show wrod list
		showText();
    }

	/**
	 * show word list f
	 */		
	private void showText() {
		String[] words = mWordList.getWordArray();
		mTextViewForward.setText( words[ GROUP_FORWARD ] ); 
		mTextViewBack.setText( words[ GROUP_BACK ] ); 
		mTextViewLeft.setText( words[ GROUP_LEFT ] ); 
		mTextViewRight.setText( words[ GROUP_RIGHT ] ); 
	}
	
	/**
	 * exec mic onClick
	 * @param View view
	 */			
	private void execClickMic( View view ) {
		// clear text
		mTextViewForward.setTextColor( Color.BLACK ); 
		mTextViewBack.setTextColor( Color.BLACK ); 
		mTextViewLeft.setTextColor( Color.BLACK ); 
		mTextViewRight.setTextColor( Color.BLACK ); 
		mTextViewResult.setText( "" );  
		// call Recognizer Inten
		try {
			Intent intent = new Intent( 
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH ); 
 			intent.putExtra(
				RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(
				RecognizerIntent.EXTRA_PROMPT, 
				getStringFromResources( R.string.voice_prompt) );
			startActivityForResult( intent, Constant.REQUEST_RECOGNIZER );
		} catch ( ActivityNotFoundException e ) {
			// warnig if Activity Not Found
			toast_short( R.string.voice_activity_not_found );
		}
	}
// --- onCreate end ---

// --- onActivityResult ---            
	/**
	 * === onActivityResult ===
	 */
	@Override
	public void onActivityResult( int request, int result, Intent data ) {
		switch ( request ) {
			case Constant.REQUEST_RECOGNIZER:
				if ( result == RESULT_OK ) {
					execActivityResultRecognizer( data );
				}
				break; 
			default:
				execActivityResult( request, result, data );
				break;   
		}
	}

	/**
	 * exec Recognizer onActivityResult ( overwrite )
	 * @param Intent data
	 */	
	private void execActivityResultRecognizer( Intent data ) {
		int group = matchGroup( data );
		switch ( group ) { 
			case GROUP_FORWARD:
				mTextViewForward.setTextColor( Color.RED );  
				sendMoveDuration( group );
				break; 
			case GROUP_BACK:
				mTextViewBack.setTextColor( Color.RED );  
				sendMoveDuration( group );
				break; 
			case GROUP_LEFT: 
				mTextViewLeft.setTextColor( Color.RED );  
				sendMoveDuration( group );
				break; 
			case GROUP_RIGHT: 
				mTextViewRight.setTextColor( Color.RED );  
				sendMoveDuration( group );
				break; 
		} 
	}

	/**
	 * sendMoveDelay
	 * @param int group
	 */		
    private void sendMoveDuration( int group ) {
    	int direction = getDirection( group );
    	int main = mSeekbarPower.getPowerMain();
		mPowerSeekbar.calc( direction, main );
		mCommand.sendMoveDuration( 
			mPowerSeekbar.getLeft(),
			mPowerSeekbar.getRight(), 
			DURATION_TIME ) ;
	}

	/**
	 * getDirection
	 * @param int group
	 * @return int
	 */				
	private int getDirection( int group ) {
		if ( group < 0 ) return 0;
		if ( group > GROUP_TO_DERECTION.length ) return 0;
		return GROUP_TO_DERECTION[ group ];
	}
	
	/**
	 * match Group
	 * @param Intent data
	 * @return int
	 */		
	private int matchGroup( Intent data ) {
		ArrayList<String> results = data.getStringArrayListExtra(
			RecognizerIntent.EXTRA_RESULTS );
		// work variable
		String text = "";
		String word = "";
		int	 group = 0;  
		// process every word       
		for ( int i = 0; i< results.size(); i++ ) {
			word = results.get( i );
			group = mWordList.match( word );
			// if match
			if ( group != WordList.GROUP_UNMTACH ) {
				return group ;
			}
			text += i + ": " + word + Constant.LF;
		}
		// show result 
		mTextViewResult.setText( text );  
		return WordList.GROUP_UNMTACH;
	}
// --- onActivityResult end --- 

}
