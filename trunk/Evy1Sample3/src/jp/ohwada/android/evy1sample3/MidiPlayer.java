package jp.ohwada.android.evy1sample3;

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
	private final static boolean D = true;
	private final static String TAG = "MidiPlayer";

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
    private boolean isPlayStart = false;
    private boolean isPlayRunning = false;
	
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
	 * @param List<MidiList> list
	 * @param long time	 
	 */
	public MidiPlayer( List<MidiMessage> list, long time ) {
		mList = list;
		mTimebase = time;
	}

	/*
	 * 開始する
	 */    
	public void start() {
		log_d( "start" );
		startPlayTimer();
		isPlayLoop = true;
		mStartTime = SystemClock.elapsedRealtime();
		mPlayTime = mStartTime; 
	}

	/*
	 * 終了する
	 */ 
	public void stop() {
		stopPlayTimer();
	}
		
// --- Play Timer Handler ---
	/*
	 * startPlayTimer
	 */ 
	private void startPlayTimer() {
        isPlayStart = true;
        updatePlayRunning();
    }

	/*
	 * stopPlayTimer
	 */     
    private void stopPlayTimer() {
        isPlayStart = false;
        updatePlayRunning();
    }

	/*
	 * updatePlayRunning
	 */ 
    private void updatePlayRunning() {
        boolean running = isPlayStart;
        if ( running != isPlayRunning ) {
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
			isPlayRunning = running;
        }
    }

	/*
	 * Play Timer Handler
	 */ 
    private Handler playHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isPlayRunning ) {
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
			stopPlayTimer();
			notifyFinished();
			return;
		}
		// タイマーからの起動を停止する
	    isPlayLoop = false;
	    while( true ) {
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
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, msg );
	}

}
