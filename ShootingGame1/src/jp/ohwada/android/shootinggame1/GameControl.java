package jp.ohwada.android.shootinggame1;

import java.util.Random;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * GameControl
 */ 
public class GameControl {

	// debug
	private final static String TAG_SUB = "GameControl";

	// デバック用 敵の攻撃を受けるか
	private boolean isEnemyAttack = true;

	// デバック用 標的がランダムに移動するか
	private boolean isMoveRandom = true;
        
    // ライフの初期値
	private static final int LIFE_DEFAULT = 10;

    // 弾の初期値
	private static final int BULLET_DEFAULT = 15;
    // 弾を補充をするときの手持ちの弾の数
	private static final int BULLET_LIMIT = 2;

	// 次のゲームが始まるまでの時間間隔 10 sec
	private static final int GAME_INTERVAL = 10;

	// 次の標的を表示するまでの時間間隔 3 sec
	private static final int TARGET_INTERVAL = 3;

	// 弾を補充するまでの時間間隔 3 sec
	private static final int BULLET_INTERVAL = 3;

	// 次の攻撃を受けるの時間間隔 2 sec
	private static final int ATTACK_INTERVAL = 2;
	
	// 攻撃を受ける頻度 4秒に1回
	private static final int ATTACK_RANDOM = 4;

	// 撃たれたときに振動する時間  0.5 sec	
	private static final int VIBRATE_DURATION = 500;

 	// color
	private static final int COLOR_NORMAL = 0x6000ff33;
	private static final int COLOR_RED = 0x60ff0000;
	private static final int COLOR_BLUE = 0x600000ff;
	private static final int COLOR_YELLOW = 0x60ffff00;
	private static final int COLOR_TRANSPARENT = 0;
	
	// sound
	private static final int SOUND_MAX_STREAMS = 4;
	private static final int SOUND_SRC_QUALITY = 0;
	private static final int SOUND_LOAD_PRIORITY = 0;
	private static final float SOUND_LEFT_VOLUME = 1.0f;
	private static final float SOUND_RIGHT_VOLUME = 1.0f;
	private static final int SOUND_PLAY_PRIORITY = 0;
	private static final int SOUND_LOOP = 0;
	private static final float SOUND_RATE = 1.0f;

	// class object
	private Context mContext;
	private SoundPool mSoundPool;
	private Vibrator mVibrator;

	// View
	private GameView mGameView;
	
	// 自分のライフ			        	
	private int mMyLife = 0;
	// 敵のライフ
	private int mEnemyLife = 0;

	// 自分の弾の数		        	
	private int mBullet = 0;
		
	// 効果音の番号
	private int mSoundHit = 0;
	private int mSoundMiss = 0;
	private int mSoundEmpty = 0;
	private int mSoundAttack = 0;

	// ゲーム中を示すフラグ
    private boolean isGame = false;
    // 次のゲームまでの時間を計るカウンタ
	private int mGameCounter = 0;

	// 次の標的を表示するのを待っていることを示すフラグ
    private boolean isTargetWait = false;
    // 次の標的の表示までの時間を計るカウンタ
    private int mTargetCounter = 0;

    // 弾を補充するまでの時間を計るカウンタ   
    private int mBulletCounter = 0;

    // 次の攻撃を受けるまでの時間を計るカウンタ 
    private int mAttackCounter = 0;
			
	/**
	 * === Constrator ===
	 */	
	public GameControl( Context context, GameView view  ){
		mContext = context;
		mVibrator = (Vibrator) context.getSystemService( Context.VIBRATOR_SERVICE );

		mGameView = view;
		mGameView.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				if ( isGame ) {
					execTouch( event );
					return true;
				}
				return false;
			}
		});	

	}

// --- onResume ---
	/**
	 * loadSound
	 */	
    public void loadSound() {
    	mSoundPool = new SoundPool( 
    		SOUND_MAX_STREAMS, 
    		AudioManager.STREAM_MUSIC, 
    		SOUND_SRC_QUALITY );
    	mSoundHit = loadSoundPool( R.raw.hit );
    	mSoundMiss = loadSoundPool( R.raw.miss );
    	mSoundEmpty = loadSoundPool( R.raw.empty );
    	mSoundAttack = loadSoundPool( R.raw.attack );
    }

	/**
	 * loadSoundPool
	 * @param int id
	 * @return int
	 */
    private int loadSoundPool( int id ) {	
    	return mSoundPool.load( 
    		mContext, id, SOUND_LOAD_PRIORITY );
    }
    										    
// --- onPause ---
	/**
	 * releaseSound
	 */	
    public void releaseSound() {
        mSoundPool.release();
    }

	/**
	 * 方位角を設定して、ゲームを開始する
	 * @param float[] orientations 
	 */	
	public void start( float[] orientations ) {
		log_d( "start" );
		// 方位角を設定する
		mGameView.start( orientations );
		// ゲームを開始する
		startGame();
	}

	/**
	 * ゲーム画面を終了する
	 */	
	public void stop() {
		mGameView.stop();
		stopGame();
		mGameView.setBackgroundColor( COLOR_TRANSPARENT );
	}

// --- onTouch ---
	/**
	 * 画面をタッチしたときの処理
	 * @param MotionEvent event
	 */ 
	private void execTouch( MotionEvent event ) {
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				execTouchDown();
				break;
		}
	}

	/**
	 * 画面をタッチしたときの処理
	 */ 		
	private void execTouchDown() {
		if ( mBullet == 0 ) { 
			// 弾がないときは、効果音を鳴らす
			playSound( mSoundEmpty );
			return;
		}
		// 弾を減少する
		mBullet --;
		mGameView.setBullet( mBullet );	
		if ( !mGameView.isLockon() ) {
			// ロックオンしてなければ、効果音を鳴らす
			playSound( mSoundMiss );
			mGameView.setBackgroundColor( COLOR_YELLOW );
			return;
		}
		// ロックオンしていれば、敵のライフを減少する
		playSound( mSoundHit );
		mGameView.setBackgroundColor( COLOR_BLUE );
		mEnemyLife --;
		mGameView.setEnemyLife( mEnemyLife );	
		// 標的を非表示にする
		mGameView.hideTarget(); 
		if ( mEnemyLife == 0 ) {
			// 敵のライフがゼロになれば、ゲームオーバー
			stopGame();
			mGameView.showWin();
			mGameView.setBackgroundColor( COLOR_BLUE );
		} else {
			// 次の標的の準備をする
			setTarget();
		}
	}

	/**
	 * 効果音を鳴らす
	 * @param int id
	 */ 
	private void playSound( int id ) {
		mSoundPool.play( 
			id, 
			SOUND_LEFT_VOLUME, 
			SOUND_RIGHT_VOLUME, 
			SOUND_PLAY_PRIORITY, 
			SOUND_LOOP, 
			SOUND_RATE );
	}
				
	/**
	 * ゲームを開始する
	 */ 
    public void startGame()  {
    	isGame = true;
		mGameCounter = 0;
		// ライフを設定する
		mMyLife = LIFE_DEFAULT;
		mEnemyLife = LIFE_DEFAULT;
		mBullet = BULLET_DEFAULT;
		mGameView.setMyLife( mMyLife );
		mGameView.setEnemyLife( mEnemyLife );	
		mGameView.setBullet( mBullet );
		mGameView.hideWin();
		mGameView.hideLose();
		mGameView.setBackgroundColor( COLOR_NORMAL );	
		// 次の標的の準備をする
		setTarget();
	}

	/**
	 * ゲームを終了する
	 */ 
	private void stopGame() {
		isGame = false;
		mGameCounter = 0;
		mGameView.hideTarget(); 
	}

	/**
	 * 次の標的の準備をする
	 */
    private void setTarget() {
    	// ランダムに標的の位置を決める
		mGameView.setTargetRandomPositon(); 
		isTargetWait = true;
		mTargetCounter = 0;
	}

// --- Timer ---
	/**
	 * 現在の方位角を設定する
	 * @param float[] orientations
	 */
	public void updateOrientation( float[] orientations ) {
		mGameView.updateOrientation( orientations );
	}

	/**
	 * １秒周期の処理を行う
	 */	
	public void updateGame() {
		// １秒周期の処理を行う
		procWaitGame();
		procGame();
		procEnemyAttack();
	}

	/**
	 * ゲームが開始していないときの処理
	 */	
	private void procWaitGame() {
		if ( isGame ) return;
		// ゲームが開始してなければ、カウントアップする
		mGameCounter ++;
		if ( mGameCounter > GAME_INTERVAL ) {
			// ３秒経てば、ゲームを開始する
			startGame();
		}
	}

	/**
	 * ゲーム中の処理
	 */	
	private void procGame() {
		if ( !isGame ) return;
		if ( isMoveRandom ) {		
			// ランダムに標的を移動する
			mGameView.moveTargetRandom();
		}
		mGameView.setBackgroundColor( COLOR_NORMAL );
		if ( isTargetWait ) {
			// 次の標的の表示を待っているときは、カウントアップする
			mTargetCounter ++;
			if ( mTargetCounter > TARGET_INTERVAL ) {
			// ３秒以上経てば、標的を表示する
				isTargetWait = false;
				mGameView.showTarget(); 
			}
		}
		if ( mBullet <= BULLET_LIMIT ) {
			// 弾が１個以下のときは、カウントアップする
			mBulletCounter ++;
			if ( mBulletCounter > BULLET_INTERVAL ) {
				mBulletCounter = 0;
				// ２秒以上経てば、弾を補充する
				mBullet ++;
				mGameView.setBullet( mBullet );	
			}	
		}
	}

	/**
	 * 撃たれたときの処理
	 */
	private void procEnemyAttack() {
		if ( !isEnemyAttack ) return;
		if ( !isGame ) return;	
		if ( isTargetWait ) return;
		if ( mAttackCounter > 0 ) {
			// 一度攻撃を受けたら、２秒間は攻撃されない
			mAttackCounter --;
			return;
		}
		// 乱数で攻撃を受けたかを決める
		Random r = new Random();
		int num = r.nextInt( ATTACK_RANDOM );
		switch( num ) {
			case 0:
				// 攻撃を受けたら、自分のライフを減少する
				mMyLife --;
				playSound( mSoundAttack );
				mVibrator.vibrate( VIBRATE_DURATION );
				mGameView.setBackgroundColor( COLOR_RED );
				mAttackCounter = ATTACK_INTERVAL - 1;
				break;
		}		
		mGameView.setMyLife( mMyLife );
		// 自分のライフがゼロになったら、ゲームオーバー
		if ( mMyLife == 0 ) {
			stopGame();
			mGameView.showLose();
			mGameView.setBackgroundColor( COLOR_RED );
		}	
	}

 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}
		  
}