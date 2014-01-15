package jp.ohwada.android.midi;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * MIDIメッセージを演奏する
 */
public class MidiPlayer {

	// debug
	private final static String TAG_SUB = "MidiPlayer";

    // Play Timer
    private static final int MSG_WHAT_PLAY = 100;
    private static final int PLAY_DELAY = 100; // 0.1 sec
	
	// MIDIのリスト
	private List<MidiMessage> mList = null;

    // リストの処理中の位置を示すポインタ
	private int mPointer = 0;
	
	// ベース時間
	private long mTimebase = 0;		

	// 演奏を開始したシステム時間
	private long mStartTime = 0;

	// MIDIメッセージを実行するシステム時間	
	private long mPlayTime = 0;
		
    // 演奏を行うかどうかのフラグ
    private boolean isPlayLoop = false;  
    
    // Play Timer
    private boolean isStart = false;
    private boolean isRunning = false;
	
	// callback 
    private OnChangedListener mListener;  

	/*
	 * callback interface
	 */    
    public interface OnChangedListener {
    	// MIDI メッセージを通知する
        void onChanged( MidiMessage mes );
    	// 終了を通知する
        void onFinished();
    }

	/*
	 * callback
	 */ 
    public void setOnChangedListener( OnChangedListener listener ) {
        mListener = listener;
    }
       		
	/*
	 * コンストラクタ
	 */
	public MidiPlayer() {
		// dummy
	}

	/*
	 * 演奏する
	 * @param List<MidiMessage> list
	 * @param long time	 
	 */    
	public void play( List<MidiMessage> list, long time ) {
		log_d( "play" );
		stop( true );
		mList = list;
		mTimebase = time;
		mPointer = 0;
		isPlayLoop = true;
		mStartTime = SystemClock.elapsedRealtime();
		mPlayTime = mStartTime; 
		startTimer();
	}

	/*
	 * 終了する
	 * @param boolean flag
	 */ 
	public void stop( boolean flag ) {
		stopTimer();
        if ( flag ) {
			// 完全に停止するまで待つ ( 最大0.1秒 )
			while( true ) {
        		if ( !isRunning ) break;
        	}
        }	
	}
		
// --- Play Timer Handler ---
	/*
	 * startTimer
	 */ 
	private void startTimer() {
        isStart = true;
        updateRunning();
    }

	/*
	 * stopTimer
	 */     
    private void stopTimer() {
        isStart = false;
        updateRunning();
    }

	/*
	 * updateRunning
	 */ 
    private void updateRunning() {
        boolean running = isStart;
        if ( running != isRunning ) {
			if ( running ) {
				if ( isPlayLoop ) {
					procMessageLoop();
				}
				// start
				playHandler.sendMessageDelayed(
					Message.obtain( playHandler, MSG_WHAT_PLAY ), 
					PLAY_DELAY );               
			} else {
				// stop
				playHandler.removeMessages( MSG_WHAT_PLAY );
			}
			isRunning = running;
        }
    }

	/*
	 * Play Timer Handler
	 */ 
    private Handler playHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isRunning ) {
            	if ( isPlayLoop ) {
            		procMessageLoop();
            	}
                sendMessageDelayed(
                	Message.obtain( this, MSG_WHAT_PLAY ), 
                	PLAY_DELAY );
            }
        }
    };

	/*
	 * タイマーから起動して、MIDIのリストを処理する
	 */ 
    private synchronized void procMessageLoop() {
		// 全てのMIDIメッセージを処理したときは、終了する
    	if ( isEnd() ) {
			log_d( "Stop" );	
			stopTimer();
			notifyFinished();
			return;
		}
		// タイマーからの起動を停止する
	    isPlayLoop = false;
	    while( true ) {
	    	// Play が中断したときは、終了する
	        if ( !isStart ) break;
	    	// 全てのMIDIメッセージを処理したときは、終了する
	        if ( isEnd() ) break;
	        // 演奏時間にならないときは、いったん終了する
			if ( isWaiting() ) break;
    		MidiMessage mes = mList.get( mPointer ); 
			mPointer ++;
			if ( mes != null ) {
	        	// 演奏時間を設定する
				long playtime = mes.playtime / ( 1000 * mTimebase );
				mPlayTime = mStartTime + playtime;
				// 機器に送信する
				nofityMessage( mes );
			}
		}					
		// タイマーからの起動を有効にする
		isPlayLoop = true;	
    }
	
	/*
	 * 全てのMIDIメッセージを処理したかの判定
	 * @return boolean
	 */ 
	private boolean isEnd() {
		if ( mPointer >= mList.size() ) {
			return true;
		}
		return false;
    }	
    	
	/*
	 * 演奏時間になったかの判定
	 * @return boolean
	 */ 
	private boolean isWaiting() {
		if ( SystemClock.elapsedRealtime() < mPlayTime ) {
			return true;
		}
		return false;
	}
	
	/*
	 * MIDI メッセージを通知する
	 * @param MidiMessage m
	 */ 
    private void nofityMessage( MidiMessage m ) {
 		if ( mListener != null ) {
 			mListener.onChanged( m );
		}
    }

	/*
	 * 終了を通知する
	 */ 
    private void notifyFinished() {
 		if ( mListener != null ) {
 			mListener.onFinished();
		}
    }
 		   	
	/**
	 * logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (MidiConstant.D) Log.d( MidiConstant.TAG, TAG_SUB + " " + msg );	
	}

}
