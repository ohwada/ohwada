package jp.ohwada.android.shootinggame1;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * TargetUnit
 */ 
public class TargetUnit {

	// debug
	private static final String TAG_SUB = "TargetUnit";

	// ロックオンするエリアの大きさの比率
 	public static final float RATIO_RENGE_LOCKON = 0.15f;

	// ナビの大きさの比率
 	private static final float RATIO_NAVI_HEIGHT = 0.18f;
 	private static final float RATIO_NAVI_BASE = 0.13f;
 	private static final float RATIO_NAVI_MARGIN = 0.02f;

	// ナビの方向
 	private static final int NAVI_NONE = 0;
 	private static final int NAVI_LEFT = 1;
 	private static final int NAVI_RIGHT = 2;
  	private static final int NAVI_TOP = 3;
  	private static final int NAVI_BOTTOM = 4;
  	 		 			
	// 標的の色 半透明のピンク
	private static final int COLOR_TARGET = 0x80ff00ff;

	// 標的の位置を決める細かさ
	private static final int POSITON_STEP = 10;
		 	 	
	// 南を向いているか
	// このときは +180 から -180 に変化する
 	private boolean isSouth = false; 

	// ロックオンしているか
	private boolean isLockon = false;
	
	// UI
	private Paint mPaintTarget;
//	private Paint mPaintNavi;

	// 現在の方位センサの値
	private float mOrientX = 0 ;
	private float mOrientY = 0 ;
	private float mOrientZ = 0 ; 

	// 基準の方位角
	private float mOrientLeft = 0;
	private float mOrientRight = 0;
	private float mOrientTop = 0;
	private float mOrientBottom = 0;

	// 方位センサの角度と画面サイズの比
	private float mRatioX =  0;
	private float mRatioY = 0;
    
	// 標的の方位
	private float mTargetOrientX = 0;
 	private float mTargetOrientY = 0;
 	
 	// 標的の座標
	private float mTargetX = 0;
	private float mTargetY = 0;
	private float mTargetRadius = 0;
 	
	// ナビの図形の座標
	private Path mPathLeft = null;
	private Path mPathRight = null;
	private Path mPathTop = null;
	private Path mPathBottom = null;

 	// ナビの位置
 	private int mNaviPos = NAVI_NONE; 
 		
	// 標的をランダムに移動するパラメータ
	private int mMoveRandomNum = 0; 

	// 標的を移動する大きさ
 	private float mMoveSmall = 0;
 	private float mMoveLarge = 0;
 		
	// ロックオンを判定するエリアの大きさ 
 	private float mLockonRenge = 0;
				   		    		
	// 画面サイズ
	private int mWidth = 0;
	private int mHeight = 0;

	// 画面中心の座標
	private float mCenterX = 0;
	private float mCenterY = 0;	
        
	/**
	 * === Constrator ===
	 */	
	public TargetUnit(){
		mPaintTarget = new Paint();
		mPaintTarget.setColor( COLOR_TARGET );
	}

	/**
	 * 基準の方位を設定する
	 * @param float[] orientations
	 */	
	public void startTarget( float[] orientations ) {
		log_d( "tartTarget" );
		float left = orientations[0] ; 
		float right = orientations[1] ;
		float top = orientations[2] ;
		float bottom = orientations[3] ;
		if ( left  > right ) {
			// 左側が大きいときは +180 から -180 に変化しているので、
			// 右側に 360 を加算する
			isSouth = true;
			right += 360;
		} 
		mOrientLeft = left;
		mOrientRight = right;
		mOrientTop = top;
		mOrientBottom = bottom;
		mTargetOrientX = ( left + right ) / 2;
		mTargetOrientY = ( top + bottom ) / 2;  
		mRatioX = mWidth / ( right - left );
		mRatioY = mHeight / ( bottom - top );
	}

	/**
	 * ランダムに標的の位置を決める
	 */	
    public void setTargetRandomPositon() {				
		mTargetOrientX = calcTargetRandom( mOrientLeft, mOrientRight );
		mTargetOrientY = calcTargetRandom( mOrientTop, mOrientBottom );
 	}

	/**
	 * calcTargetRandom
	 * @param float x1
	 * @param float x2
	 * @return float	 
	 */
    private float calcTargetRandom( float x1, float x2 ) {
    	float ret = 0;
    	float delta = Math.abs( x1 - x2 ) / POSITON_STEP;
    	Random r = new Random();
    	int step = r.nextInt( POSITON_STEP );
    	if ( x2 > x1 ) {
			ret = x1 + delta * step ;
		} else {
			ret = x2 + delta * step ;		
		}
		return ret;	
	}
		
// --- Timer ---
	/**
	 * 現在の座標を設定する (設定画面)
	 * @param float x
	 * @param float y
	 */
    public void updateSetting( float x, float y ) {		
    	float dx = mRatioX * ( mTargetOrientX - x );
    	float dy = mRatioY * ( mTargetOrientY - y );
    	mTargetX = mCenterX + dx;
    	mTargetY = mCenterY + dy;
  		getNaviPosition(); 
	}

	/**
	 * 現在の方位を設定する (ゲーム画面)
	 * @param float[] orientations
	 */
    public boolean updateGame( float[] orientations ) {	
        float x = getOrientX( orientations[0] );
		float y = getOrientY( orientations[1] );
   		float[] pos = getTargetPosition( x, y );
    	float dx = pos[0];
    	float dy = pos[1];
    	mTargetX = mCenterX + dx;
    	mTargetY = mCenterY + dy;
  		getNaviPosition();
		isLockon = checkLockon( dx, dy ); 
		return isLockon;
	}

	/**
	 * Xの方位を設定する
	 * @param float x 
	 */
    public float getOrientX( float x ) {	
		// 南を向いているときは、+180 から -180 に変化しているので、
		// マイナスのときは 360 加算する
    	if ( isSouth && ( x < 0 )) {
    		x += 360;
		} 
		mOrientX = 0.9f * mOrientX + 0.1f * x;  
		return mOrientX;
	}

	/**
	 * Yの方位を設定する
	 * @param float x 
	 */
    public float getOrientY( float y ) {	
		mOrientY = 0.9f * mOrientY + 0.1f * y;  
		return mOrientY;
	}

	/**
	 * Zの方位を設定する
	 * @param float x 
	 */
    public float getOrientZ( float z ) {	
		mOrientZ = 0.9f * mOrientZ + 0.1f * z;  
		return mOrientZ;
	}

	/**
	 * 標的が画面からははみ出したかを判定する
	 */
	private void getNaviPosition() {
		mNaviPos = NAVI_NONE;   	    	
    	if ( mTargetX < 5 ) {
    		// 右寄り
    		mNaviPos = NAVI_LEFT; 
    	} else if ( mTargetX > (mWidth - 5) ) {
    		// 左寄り
    		mNaviPos = NAVI_RIGHT; 
    	} else if ( mTargetY < 5 ) {
     		// 上寄り
    		mNaviPos = NAVI_TOP; 
    	} else if ( mTargetY > (mHeight - 5) ) {
     		// 下寄り
    		mNaviPos = NAVI_BOTTOM; 
    	} 	
	}

	/**
	 * getTargetPosition
	 * @param float x
	 * @param float y
	 * @return float[]
	 */
	private float[] getTargetPosition( float x, float y ) {
    	float dd = mMoveSmall;
    	// ロックオン中ならば、大きく移動する
    	if ( isLockon ) {
    	    dd = mMoveLarge;
    	}
    	float dx = mRatioX * ( mTargetOrientX - x );
    	float dy = mRatioY * ( mTargetOrientY - y );
    	// ランダムに標的を移動する
    	switch( mMoveRandomNum ) {
			case 1:
				dx += dd;
				break;
			case 2:
				dx -= dd;
				break;
			case 3:
				dy += dd;
				break;
			case 4:
				dy -= dd;
				break;
		}
		// 一度移動したら、１秒間はそのままに
		mMoveRandomNum = 0;	
		float[] ret = new float[]{ dx, dy };
		return ret;	
	}

	/**
	 * checkLockon
	 * @param float dx
	 * @param float dy
	 */
	private boolean checkLockon( float dx,  float dy ) {
		boolean flag = false;
    	if ( (dx * dx + dy * dy) < ( 0.5 * mLockonRenge * mLockonRenge) ) {
    		// ロックオン領域ならば
    		flag = true;
		}
		return flag;
	}

	/**
	 * ランダムに標的を移動するための値を設定する
	 */	
    public void moveTargetRandom() {
    	Random r = new Random();
		mMoveRandomNum = r.nextInt( 5 );
	}

// --- onDraw ---
	/**
	 * execDraw
	 * @param Canvas canvas
	 */	
	public void execDraw( Canvas canvas ) {
		switch ( mNaviPos ) {
			// 標的が画面からははみ出したときは、ナビを表示する
			case NAVI_LEFT:
				canvas.drawPath( mPathLeft, mPaintTarget );
				break;
			case NAVI_RIGHT:
				canvas.drawPath( mPathRight, mPaintTarget );
				break;
			case NAVI_TOP:
				canvas.drawPath( mPathTop, mPaintTarget );			
				break;
			case NAVI_BOTTOM:
				canvas.drawPath( mPathBottom, mPaintTarget );		
				break;
			default:
				if ( isLockon ) {
					// ロックオン中
					canvas.drawCircle( mCenterX, mCenterY, mTargetRadius, mPaintTarget );
				} else {
					// 標的	
					canvas.drawCircle( mTargetX, mTargetY, mTargetRadius, mPaintTarget );
				}
				break;
		}	
	}
	
// --- onSizeChanged ---
	/**
	 * execSizeChanged
	 * @param int width
	 * @param int height
	 */
	public void execSizeChanged( int width, int height ) {
		mWidth = width;
		mHeight = height;
		mCenterX = width / 2;
		mCenterY = height / 2;
		mTargetRadius = 0.05f * height;
		mLockonRenge = RATIO_RENGE_LOCKON * height;	
		mMoveSmall = 0.5f * mLockonRenge;
		mMoveLarge = 1.2f * mLockonRenge;
		createPathLeft( width, height );
		createPathRight( width, height );
		createPathTop( width, height );
		createPathBottom( width, height );
	}

	/**
	 * createPathLeft
	 * @param int width
	 * @param int height
	 */
	private void createPathLeft( int width, int height ) { 
		float h = RATIO_NAVI_HEIGHT * height;
		float base = RATIO_NAVI_BASE * height;
		float margin = RATIO_NAVI_MARGIN * height;
		float x0 = margin;
		float y0 = height / 2;
		float x1 = x0 + h;
		float y1 = y0 - base;
		float x2 = x1;
		float y2 = y0 + base;
		mPathLeft = createPath( x0, y0, x1, y1, x2, y2 );
	}

	/**
	 * createPathRight
	 * @param int width
	 * @param int height
	 */
	private void createPathRight( int width, int height ) { 
		float h = RATIO_NAVI_HEIGHT * height;
		float base = RATIO_NAVI_BASE * height;
		float margin = RATIO_NAVI_MARGIN * height;
		float x0 = width - margin;
		float y0 = height / 2;
		float x1 = x0 - h;
		float y1 = y0 - base;
		float x2 = x1;
		float y2 = y0 + base;
		mPathRight = createPath( x0, y0, x1, y1, x2, y2 );
	}

	/**
	 * createPathTop
	 * @param int width
	 * @param int height
	 */	
	private void createPathTop( int width, int height ) { 
		float h = RATIO_NAVI_HEIGHT * height;
		float base = RATIO_NAVI_BASE * height;
		float margin = RATIO_NAVI_MARGIN * height;
		float x0 = width / 2;
		float y0 = margin;
		float x1 = x0 - base;
		float y1 = y0 + h;
		float x2 = x0 + base;
		float y2 = y1;
		mPathTop = createPath( x0, y0, x1, y1, x2, y2 );
	}

	/**
	 * createPathBottom
	 * @param int width
	 * @param int height
	 */	
	private void createPathBottom( int width, int height ) { 
		float h = RATIO_NAVI_HEIGHT * height;
		float base = RATIO_NAVI_BASE * height;
		float margin = RATIO_NAVI_MARGIN * height;
		float x0 = width / 2;
		float y0 = height - margin;
		float x1 = x0 - base;
		float y1 = y0 - h;
		float x2 = x0 + base;
		float y2 = y1;
		mPathBottom = createPath( x0, y0, x1, y1, x2, y2 );
	}

	/**
	 * createPath
	 * @param float x0
	 * @param float y0
	 * @param float x1
	 * @param float y1
	 * @param float x2
	 * @param float y2	 
	 */	
	private Path createPath( float x0, float y0, float x1, float y1, float x2, float y2 ) { 
		Path path = new Path();
		path.moveTo( x0, y0 );
		path.lineTo( x1, y1 );
		path.lineTo( x2, y2 );
		path.lineTo( x0, y0 );
		return path;
	}
	
 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}	
    		 			
}
