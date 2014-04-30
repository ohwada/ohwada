package jp.ohwada.android.shootinggame1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

	// debug
	private final static String TAG_SUB = "MainActivity";
  	
	// timer
	private static final int TIMER_WHAT = 100;
    // 0.1 sec
	private static final int TIMER_INTERVAL = 100;
	// 10 回
	private static final int TIMER_1SEC_INTERVAL = 1000 / TIMER_INTERVAL; 	
 	
	// View
	private SettingView mSettingView;

	// class object
	private MySensor mSensor;
	private GameControl mGameControl;
	
	// 設定中を示すフラグ
    private boolean isSetting = false;

	// ゲーム中を示すフラグ    
    private boolean isGame = false;
    
    // 画面にタッチしたか
    // 設定画面の一番最初のときは、終了する
    // それ以外は、再度 設定画面に戻る
    private boolean isTouch = false;
    
	// timer
	private boolean isStart = false;
	private boolean isRunning = false;

    // １秒毎のタイマー処理のためのカウンタ
	private int mTimerCounter = 0;
	
	/**
	 * === onCreate ===
	 */ 	
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

		// BT200 フルスクリーン
		Window win = getWindow();
		WindowManager.LayoutParams params = win.getAttributes();
		params.flags |= 0x80000000;
		win.setAttributes( params );
		
        setContentView( R.layout.activity_main );
        
        // Initialization of View 
        mSettingView = (SettingView) findViewById( R.id.SettingView );
		mSettingView.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				if ( isSetting ) {
					execTouch( event );
					return true;
				}
				return false;
			}
		});
	
        GameView gameView = (GameView) findViewById( R.id.GameView );
		mGameControl = new GameControl( this, gameView ); 
		
 		mSensor = new MySensor( this );   
    }

	/**
	 * === onResume ===
	 */     
    @Override
    protected void onResume(){
    	super.onResume();
    	mSensor.registerListener();
    	mGameControl.loadSound();
    	startSetting();
    	startTimer();
    }
								    
	/**
	 * === onPause ===
	 */ 
    @Override
    protected void onPause() {
    	super.onPause();   	
		mSensor.unregisterListener();
		mGameControl.releaseSound();
        stopTimer();
    }

	/**
	 * === dispatchKeyEvent ===
	 */
	@Override
	public boolean dispatchKeyEvent( KeyEvent event ) {
    	if ( event.getAction() == KeyEvent.ACTION_DOWN ) {
        	switch ( event.getKeyCode() ) {
        		case KeyEvent.KEYCODE_BACK:
        			// バックキーの処理
        			if ( isTouch ) {
        				// 再度 設定画面に戻る
						startSetting();
					} else {
    					// 設定画面の一番最初のときは、終了する
						finish();
					}
            		return true;
        	}
    	}
    	return super.dispatchKeyEvent(event);
	}

	/**
	 * 設定を開始する
	 * Resume から呼び出される
	 */ 
	private void startSetting() {
		log_d( "startSetting" );
		isGame = false;
		isSetting = true;
		isTouch = false;
		mSettingView.start();
		mGameControl.stop();
	}

	/**
	 * 設定が終了したときの処理
	 */
	private void stopSetting() {
		isSetting = false;
		mSettingView.stop();
		// ゲームを開始する
		isGame = true;
		mGameControl.start( mSettingView.getOrientations() );
		startTimer();
	}

// --- onTouch ---  
	/**
	 * 画面をタッチしたときの処理
	 * @param MotionEvent event
	 */ 
	private void execTouch( MotionEvent event ) {
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				// 方位角を設定する
				isTouch = true;
				boolean ret = mSettingView.execTouchDown( 
					mSensor.getAverageOrientations() );
				if ( ret ) {
					stopSetting();
				}
				break;
		}
	}

// --- Timer ---    
	/**
	 * startTimer
	 */		
	private void startTimer() {
		isStart = true;
		updateRunning();		
	}

	/**
	 * stopTimer
	 */		
	private void stopTimer() {
		isStart = false;
		updateRunning();		
	}

	/**
	 * 0.1秒毎の処理
	 */	
	private void updateGame() {
		// 現在の方位を設定する
		if ( isSetting ) {
			mSettingView.updateOrientation( 
				mSensor.getCurrentOrientations() );
		}
		if ( isGame ) {	
			mGameControl.updateOrientation( 
				mSensor.getCurrentOrientations() );
		}		
		// カウントアップする
		mTimerCounter ++;
		if ( mTimerCounter > TIMER_1SEC_INTERVAL ) {
			mTimerCounter = 0;
			if ( isGame ) {	
				// １秒周期の処理を行う
				mGameControl.updateGame();
			}	
		}
	}
	
	/**
	 * updateRunning
	 */		
	private void updateRunning() {
        boolean running = isStart;
        if (running != isRunning) {
            if (running) {
				updateGame();
                timerHandler.sendMessageDelayed(
                	Message.obtain( timerHandler, TIMER_WHAT ), TIMER_INTERVAL );               
             } else {
                timerHandler.removeMessages( TIMER_WHAT );
            }
            isRunning = running;
        }
    }

	/**
	 * timer handler
	 */		    
    private Handler timerHandler = new Handler() {
        public void handleMessage( Message m ) {
            if (isRunning) {
				updateGame();
                sendMessageDelayed(
                	Message.obtain( this, TIMER_WHAT ), TIMER_INTERVAL );
            }
        }
    };

 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}
		  
}