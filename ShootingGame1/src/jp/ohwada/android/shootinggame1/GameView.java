package jp.ohwada.android.shootinggame1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * GameView
 */ 
public class GameView extends View {

	// debug
	private final static String TAG_SUB = "GameView";

	// 文字の色 白
	private static final int COLOR_TEXT = 0xffffffff;
	// ロックオンしたときの標的の色 不透明な赤
	private static final int COLOR_LOCKON = 0xffff0000;
	// 自分のライフの色 半透明の青
	private static final int COLOR_MY_LIFE = 0x800000ff;
	// 敵のライフの色 半透明の緑
	private static final int COLOR_ENEMY_LIFE = 0x8000ff00;
	// 弾丸の色 半透明の黄色
	private static final int COLOR_BULLET = 0x80ffff00;
	// 照準の色 半透明の白
	private static final int COLOR_SIGHT = 0x80ffffff;
	// ロックオンしたときの照準の色 半透明の赤
	private static final int COLOR_SIGHT_LOCKON = 0x80ff0000;
	// 照準の背景色 透明
	private static final int COLOR_BG_SIGHT = 0x000000000;

	// 照準の座標
	private static final float SIGHT_LEFT = 0;
	private static final float SIGHT_TOP = 0;
	private static final Paint SIGHT_PAINT = null;

	private TargetUnit mTargetUnit;
				
	// graphics
    private Bitmap mBitmapSight;    
    private Bitmap mBitmapLockon;   
	private Paint mPaintText;
	private Paint mPaintLockon;
	private Paint mPaintMyLife;
	private Paint mPaintEnemyLife;
	private Paint mPaintBullet;
	private Paint paintCircle;
	private Paint paintLine;
			
 	// 表示するか
 	private boolean isDraw = false;
 	
 	 // 標的を表示するか
 	private boolean isTarget = false;
 		
	// ロックオンしているか
	private boolean isLockon = false;

	// 勝ったか
	private boolean isWin = false;
	// 負けたか
	private boolean isLose = false;

	// 文字の座標
	private float mWinX = 0;
	private float mLoseX = 0;
	private float mTextY = 0;
 
	// 自分のライフ	
	private int mMyLife = 0;

	// 敵のライフ	
	private int mEnemyLife = 0;

	// 自分の弾の数
	private int mBullet = 0;	

	// テキスト
	private String mTextWin = "";
	private String mTextLose = "";	
			 
	// 画面サイズ
	private int mWidth = 0;
	private int mHeight = 0;

	// 画面中心の座標
	private float mCenterX = 0;
	private float mCenterY = 0;	
	   
	/**
	 * === Constrator ===
	 */	
	public GameView( Context context ){
		super( context );
		init( context );
	}

	/**
	 * === Constrator ===
	 */	
	public GameView( Context context, AttributeSet attrs ){
		super( context, attrs );
		init( context );
	}

	/**
	 * === Constrator ===
	 */	
	public GameView( Context context, AttributeSet attrs, int defStyle ){
		super( context, attrs, defStyle );
		init( context );
	}

	/**
	 * Initialization
	 * @param Context context
	 */			
	private void init( Context context ){
		// Initialization of Paint
		mPaintText = new Paint();
		mPaintText.setAntiAlias( true );
		mPaintText.setColor( COLOR_TEXT );			
		mPaintLockon = new Paint();
		mPaintLockon.setColor( COLOR_LOCKON );	
		mPaintMyLife = new Paint();
		mPaintMyLife.setColor( COLOR_MY_LIFE );
		mPaintEnemyLife = new Paint();
		mPaintEnemyLife.setColor( COLOR_ENEMY_LIFE );
		mPaintBullet = new Paint();
		mPaintBullet.setColor( COLOR_BULLET );
		paintCircle = new Paint();
		paintCircle.setColor( COLOR_SIGHT );
		paintCircle.setStyle( Paint.Style.STROKE );
		paintCircle.setStrokeWidth( 10f );
		paintLine = new Paint();
		paintLine.setColor( COLOR_SIGHT );
		paintLine.setStrokeWidth( 10f );
				
		// Initialization of text
		Resources r = context.getResources();
		mTextWin = r.getString( R.string.win );
		mTextLose = r.getString( R.string.lose );		

		mTargetUnit = new TargetUnit();
	}

	/**
	 * 基準の方位角を設定する
	 * @param float[] orientations
	 */	
	public void start( float[] orientations ) {
		log_d( "start" );
		isDraw = true;		
		mTargetUnit.startTarget( orientations );
	}

	/**
	 * ゲーム画面を終了する
	 */	
	public void stop() {
		isDraw = false;	
	}
		      
	/**
	 * 自分のライフを設定する
	 * @param int life
	 */	
    public void setMyLife( int life ) {
    	mMyLife = life;		
    }

	/**
	 * 敵のライフを設定する
	 * @param int life
	 */	
    public void setEnemyLife( int life ) { 	
    	mEnemyLife = life;	
    }

	/**
	 * 自分の弾の数を設定する
	 * @param int val
	 */	
    public void setBullet( int val ) {
    	mBullet = val;		
    }

	/**
	 * ロックオンしているか
	 */	
    public boolean isLockon() {
    	return ( isTarget & isLockon );
    }

	/**
	 * ランダムに標的の位置を決める
	 */	
    public void setTargetRandomPositon() {			
    	mTargetUnit.setTargetRandomPositon();
 	}
		
	/**
	 * 標的を表示にする
	 */	
    public void showTarget() {
    	isTarget = true;
 	}

	/**
	 * 標的を非表示にする
	 */	
    public void hideTarget() {
    	isTarget = false;
 	}

	/**
	 * 「勝ち」を表示にする
	 */	
    public void showWin() {
    	isWin = true;
 	}

	/**
	 * 「勝ち」を非表示にする
	 */	
    public void hideWin() {
    	isWin = false;
 	}
    
   	/**
   	 * 「負け」を表示にする
   	 */	
    public void showLose() {
       	isLose = true;
    }
    
	/**
	 * 「負け」を非表示にする
	 */	
    public void hideLose() {
    	isLose = false;
 	}

// --- Timer ---
	/**
	 * 現在の方位角を設定する
	 * @param float[] orientations
	 */
    public void updateOrientation( float[] orientations ) {
		isLockon = mTargetUnit.updateGame( orientations );
		invalidate();	
	}

	/**
	 * ランダムに標的を移動するための値を設定する
	 */	
    public void moveTargetRandom() {
    	mTargetUnit.moveTargetRandom();
	}
 
	/**
	 * === onDraw ===
	 */	
	@Override
	public void onDraw( Canvas canvas ){
		if ( !isDraw ) return;
		if ( isTarget ) {		
			mTargetUnit.execDraw( canvas );
		}

		if ( isWin ) {
			// 勝ったとき
			canvas.drawText( mTextWin, mWinX, mTextY, mPaintText );
		} else if ( isLose ) {
			// 負けたとき
			canvas.drawText( mTextLose, mLoseX, mTextY, mPaintText );
		} else if ( isTarget & isLockon ) {
			// ロックオン中の照準
			canvas.drawBitmap( mBitmapLockon, SIGHT_LEFT, SIGHT_TOP, SIGHT_PAINT );
		} else {
			// 照準	
			canvas.drawBitmap( mBitmapSight, SIGHT_LEFT, SIGHT_TOP, SIGHT_PAINT );
		}
			
		drawEnemyLife( canvas );
		drawMyLife( canvas );
		drawBullet( canvas );
	}

	/**
	 * drawEnemyLife
	 * @param Canvas canvas
	 */	
	private void drawEnemyLife( Canvas canvas ) {	
		// 余白を含む１つ当たりの大きさ  
		float frame = mHeight / 12;
		// 四角形の１辺の長さ 
		float length = 0.8f * frame; 
		// 左側に表示する
		float left = 10 + 0.1f * frame;
		float right = left + length;
		drawLife( canvas, frame, length, left, right, mEnemyLife, mPaintEnemyLife );
	}

	/**
	 * drawMyLife
	 * @param Canvas canvas
	 */	
	private void drawMyLife( Canvas canvas ) {		
		// 余白を含む１つ当たりの大きさ
		float frame = mHeight / 12;
		// 四角形の１辺の長さ 
		float length = 0.8f * frame; 
		// 右側に表示する
		float right = mWidth - 0.1f * frame - 10;
		float left = right - length;
		drawLife( canvas, frame, length, left, right, mMyLife, mPaintMyLife );	
	}

	/**
	 * drawLife
	 * @param Canvas canvas
	 * @param float frame
	 * @param float length
	 * @param float left
	 * @param float right	 	 	 
	 * @param int life	
	 * @param Paint paint		 
	 */		
	private void drawLife( Canvas canvas, float frame, float length, float left, float right, int life, Paint paint ) {
		float top = 0; 
		float bottom = 0 ; 
		// 下から上に
		for ( int i = 0; i < life; i++ ) {
			bottom = ( mHeight - 0.5f * length ) - i * frame ;
			top = bottom - length;
			canvas.drawRect( left, top, right, bottom, paint );
		}
	}

	/**
	 * drawBullet
	 * @param Canvas canvas
	 */
	private void drawBullet( Canvas canvas ) {
		// 余白を含む１つ当たりの大きさ	
		float frame = mHeight / 20;
		// 矩形の短辺の長さ
		float len_short = 0.8f * frame; 
		// 矩形の長辺の長さ
		float len_long = 1.5f * len_short; 
		float top = 0.5f * frame;
		float bottom = top + len_long;
		float right = 0;
		float left = 0;
		// 右から左に
		for ( int i = 0; i < mBullet; i++ ) {
			right = ( mWidth - 0.5f * len_short ) - i * frame;
			left = right - len_short;
			canvas.drawRect( left, top, right, bottom, mPaintBullet );
		}
				
	}
		
	/**
	 * === onSizeChanged ===
	 */	
	@Override
	protected void onSizeChanged( int w, int h, int oldw, int oldh ){
		mWidth = w;
		mHeight = h;
		mCenterX = w / 2;
		mCenterY = h / 2;
		
		// 中央に表示するように座標を計算する
		float size = 0.4f * h;
		mPaintText.setTextSize( size );
		mWinX = mCenterX - mPaintText.measureText( mTextWin ) / 2;
		mLoseX = mCenterX - mPaintText.measureText( mTextLose ) / 2;
		mTextY = mCenterY + size / 2 ;	
		
		mBitmapSight = buildBitmapSight( w, h, COLOR_SIGHT );
		mBitmapLockon = buildBitmapSight( w, h, COLOR_SIGHT_LOCKON );

		mTargetUnit.execSizeChanged( w, h );
	}

	/**
	 * buildBitmapSight
	 * @param int width
	 * @param int height
	 */		
	private Bitmap buildBitmapSight( int width, int height, int color ) {
		Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas();
		canvas.setBitmap( bitmap );			
		canvas.drawColor( COLOR_BG_SIGHT );

		Paint paintCircle = new Paint();
		paintCircle.setColor( color );
		paintCircle.setStyle( Paint.Style.STROKE );
		paintCircle.setStrokeWidth( 10f );
		
		Paint paintLine = new Paint();
		paintLine.setColor( color );
		paintLine.setStrokeWidth( 10f );
				
		// 丸の大きさ
		float r1 = TargetUnit.RATIO_RENGE_LOCKON * height;
		float r2 = 2 * r1;
		float r3 = 2.2f * r1;
		
		// 内側の丸			
		canvas.drawCircle( mCenterX, mCenterY, r1, paintCircle );
		// 外側の丸
		canvas.drawCircle( mCenterX, mCenterY, r2, paintCircle );
		// 一番外側の丸
		canvas.drawCircle( mCenterX, mCenterY, r3, paintCircle );

		// 横の線
		float x1 = mCenterX - r3;
		float x2 = mCenterX + r3;
		canvas.drawLine( x1, mCenterY, x2, mCenterY, paintLine );

		// 縦の線
		float y1 = mCenterY - r3;
		float y2 = mCenterY + r3;
		canvas.drawLine( mCenterX, y1, mCenterX, y2, paintLine );
		
		return bitmap;
	}
	
 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}
			    		 			
}
