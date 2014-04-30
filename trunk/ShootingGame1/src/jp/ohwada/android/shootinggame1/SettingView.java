package jp.ohwada.android.shootinggame1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * SettingView
 */ 
public class SettingView extends View {

	// debug
	private static final String TAG_SUB = "SettingView";
	
	// ガイドの番号
 	private static final int GUIDE_LEFT = 0; 
 	private static final int GUIDE_RIGHT = 1; 
 	private static final int GUIDE_TOP = 2;
 	private static final int GUIDE_BOTTOM = 3;
 	 	   	
	// 色 
	private static final int COLOR_CIRCLE = 0x80ff0000;
	private static final int COLOR_TEXT = 0xffffffff;
	
	private TargetUnit mTargetUnit;

	// ガイドを表示するか
	private boolean isGuide = false;

	// 標的を表示するか
    private boolean isTarget = false;

	// UI
	private Paint mPaintOrient;
	private Paint mPaintTouch;
	private Paint mPaintGame;
	private Paint mPaintCircle;

	// ガイドの座標
	private float mCircleX = 0;
	private float mCircleY = 0;
	private float mCircleRadius = 0;
 	    			
    // ガイドの番号
    private int mGuideNum = 0;

	// ガイド毎の傾きセンサの値
   	private float[] mOrientations = new float[ 4 ];

	// 現在の方位センサの値
	private float mOrientX = 0 ;
	private float mOrientY = 0 ;
	private float mOrientZ = 0 ; 

	// テキストの座標
	private float mTextY = 0;
	private float mTouchX = 0;
	private float mTouchY = 0;
	private float mGameX = 0;
	private float mGameY = 0;
			
	// テキスト
	private String mTextTouch = "";
	private String mTextGame = "";
		
	// 画面サイズ
	private int mWidth = 0;
	private int mHeight = 0;

	// 画面中心の座標
	private float mCenterX = 0;
	private float mCenterY = 0;	
        
	/**
	 * === Constrator ===
	 */	
	public SettingView( Context context ){
		super( context );
		init( context );
	}

	/**
	 * === Constrator ===
	 */	
	public SettingView( Context context, AttributeSet attrs ){
		super( context, attrs );
		init( context );
	}

	/**
	 * === Constrator ===
	 */	
	public SettingView( Context context, AttributeSet attrs, int defStyle ){
		super( context, attrs, defStyle );
		init( context );
	}

	/**
	 * Initialization
	 * @param Context context
	 */			
	private void init( Context context ){
		// Initialization of Paint
		mPaintOrient = new Paint();
		mPaintOrient.setAntiAlias( true );
		mPaintOrient.setColor( COLOR_TEXT ); 	
		mPaintTouch = new Paint();
		mPaintTouch.setAntiAlias( true );
		mPaintTouch.setColor( COLOR_TEXT ); 	
		mPaintGame = new Paint();
		mPaintGame.setAntiAlias( true );
		mPaintGame.setColor( COLOR_TEXT ); 	
		mPaintCircle = new Paint();
		mPaintCircle.setColor( COLOR_CIRCLE );		
		mTargetUnit = new TargetUnit();
		
		// Initialization of text
		Resources r = context.getResources();
		mTextTouch = r.getString( R.string.touch );
		mTextGame = r.getString( R.string.game );
	}
			
	/**
	 * 設定を開始する
	 */	
    public void start() {
   		log_d( "start" );
    	isGuide = true;
    	isTarget = false;
    	setVisibility( View.VISIBLE );
    	// この時点では画面サイズが決まっていないので
    	// ガイドを表示しない
		mGuideNum = -1;
    }

	/**
	 * stop
	 */	
    public void stop() {
    	isGuide = false;
    	isTarget = false;
    	setVisibility( View.GONE );
    }

	/**
	 * 方位角を取得する
	 * @return float[]
	 */	
    public float[] getOrientations() {
		return mOrientations;
    }
      
// --- onTouch ---
	/**
	 * 画面をタッチしたときの処理
	 * @param float[] orientations
	 * @return boolean
	 */	
	public boolean execTouchDown( float[] orientations ) {
		// 方位を設定する
		switch ( mGuideNum ) {
			case GUIDE_LEFT:
				// ガイドの表示と方位は左右反対になる
				mOrientations[ GUIDE_RIGHT ] = orientations[0];
				break;
			case GUIDE_RIGHT:
				mOrientations[ GUIDE_LEFT ] = orientations[0];
				break;
			case GUIDE_TOP:
				// ガイドの表示と方位は上下反対になる
				mOrientations[ GUIDE_BOTTOM ] = orientations[1];
				break;
			case GUIDE_BOTTOM:
				mOrientations[ GUIDE_TOP ] = orientations[1];
				break;
		}
		mGuideNum ++;
		if ( mGuideNum > GUIDE_BOTTOM + 1 ) {
			return true;
		} else if ( mGuideNum > GUIDE_BOTTOM ) {
			// ４回目なら設定を終了する
			startTarget( mOrientations );
		} else {
			// 次のガイドを表示する
			showGuide( mGuideNum );
		}
		return false;
	}

	/**
	 * ガイドを表示する
	 * @param int num
	 */     
    private void showGuide( int num ) {
        float left = mCircleRadius + 10;
    	float right = mWidth - mCircleRadius - 10;
    	float top = mCircleRadius + 10;
    	float bottom = mHeight - mCircleRadius - 10;
    	// 座標を設定する
    	switch( num ) {
    		case GUIDE_LEFT:
    			// 左
    			mCircleX = left;
    			mCircleY = mCenterY;
    			break;
    		case GUIDE_RIGHT:
    			// 右
    			mCircleX = right;
    			mCircleY = mCenterY;
    			break;
    		case GUIDE_TOP:
    			// 上
    			mCircleX = mCenterX;
    			mCircleY = top;
    			break;
    		case GUIDE_BOTTOM:
    			// 下
    			mCircleX = mCenterX;
    			mCircleY = bottom;
    			break;
    	}
		invalidate();	    			
    }

	/**
	 * 基準の方位を設定する
	 * @param float[] orientations
	 */	
	public void startTarget( float[] orientations ) {
		isGuide = false;
		isTarget = true;
		mTargetUnit.startTarget( orientations );
	}

// --- Timer ---
	/**
	 * 現在の方位を設定する
	 * @param float[] orientations
	 */
    public void updateOrientation( float[] orientations ) {		

		mOrientX = mTargetUnit.getOrientX( orientations[0] );
		mOrientY = mTargetUnit.getOrientY( orientations[1] );
		mOrientZ = mTargetUnit.getOrientZ( orientations[2] );

		if ( isTarget ) { 		
			mTargetUnit.updateSetting( mOrientX, mOrientY );
  		}
  		
		invalidate();	
	}
    	
	/**
	 * === onDraw ===
	 */	
	@Override
	public void onDraw( Canvas canvas ) {
		// 左上に方位を表示する
		canvas.drawText( "x " + mOrientX, 10, mTextY, mPaintOrient );
		canvas.drawText( "y " + mOrientY, 10, (2 * mTextY), mPaintOrient );
		canvas.drawText( "z " + mOrientZ, 10, (3 * mTextY), mPaintOrient );	

		if ( mGuideNum == -1 ) {
			canvas.drawText( mTextTouch, mTouchX, mTouchY, mPaintTouch );
		}
							
		if ( isGuide && ( mGuideNum >= 0 )) {
			// ガイド
			canvas.drawCircle( mCircleX, mCircleY, mCircleRadius, mPaintCircle );
		}
		
		if ( isTarget ) {
			canvas.drawText( mTextGame, mGameX, mGameY, mPaintGame );
			mTargetUnit.execDraw( canvas );
		}
	
	}
		
	/**
	 * === onSizeChanged ===
	 */	
	@Override
	protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
		mWidth = w;
		mHeight = h;
		mCenterX = w / 2;
		mCenterY = h / 2;
		mCircleRadius = 0.1f * h;
		float size1 = 0.08f * h;
		mTextY = 1.1f * size1;
		mPaintOrient.setTextSize( size1 );
		
		// 中央に表示するように座標を計算する
		float size2 = 0.3f * h;
		mPaintTouch.setTextSize( size2 );
		mTouchX = mCenterX - mPaintTouch.measureText( mTextTouch ) / 2;
		mTouchY = mCenterY + size2 / 2 ;	

		float size3 = 0.13f * h;
		mPaintGame.setTextSize( size3 );
		mGameX = mCenterX - mPaintGame.measureText( mTextGame ) / 2;
		mGameY = h - ( 0.25f * h ) ;	
								
		mTargetUnit.execSizeChanged( w, h );
	}

 	/**
	 * write into logcat
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}		    		 			
}
