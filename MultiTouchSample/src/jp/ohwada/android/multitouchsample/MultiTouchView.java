package jp.ohwada.android.multitouchsample;

import jp.ohwada.android.multitouchsample.MultiTouchEvent;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/* http://developer.android.com/reference/android/view/MotionEvent.html */
/**
 * ビューがタッチされたを判定する
 */
public class MultiTouchView extends MultiTouchEvent
{
	/** デバック用 */
	static final protected boolean VIEW_DEBUG = true;
	static final String VIEW_TAG = "MultiTouchView";

	/** タッチ状態の定義 */
	static final protected int TOUCH_STATUS_UP   = 0;
	static final protected int TOUCH_STATUS_DOWN = 1;
	static final protected int TOUCH_STATUS_IN   = 2; 
	static final protected int TOUCH_STATUS_OUT  = 4;

	/** インデックスの初期値 */
	static final protected int POINTER_INDEX_NOT_SET = -1;

	/** POINTER_IDの初期値 */
	static final protected int POINTER_ID_NOT_SET = -1;

	/* ビューのスクリーン座標 */
	protected int viewLeft   = 0;
	protected int viewRight  = 0;
	protected int viewTop    = 0;
	protected int viewBottom = 0;

	/* ビューの名前 (デバック用) */
	protected String viewName = "";

	/*** 内部変数 ***/
	/* ビューがタッチされたの判定フラグ */
	protected boolean touchValid  = false;

	/* タッチされたスクリーン座標 */
	protected int touchX = 0;
	protected int touchY = 0;

	/* タッチされた強さ */
	protected double touchPressure = 0;

	/* タッチされた大きさ */
	protected double touchSize = 0;

	/* タッチされたインデックス */
	protected int pointerIndex = POINTER_INDEX_NOT_SET;

	/* タッチされたID */
	protected int pointerId = POINTER_ID_NOT_SET;

	/* ダウン状態になったときのID */
	protected int pointerIdDown = POINTER_ID_NOT_SET;

	/* タッチの状態 */
	protected int touchStatus = TOUCH_STATUS_UP;

	/**
	 * コンストラクタ
	 */
    public void MultiTouchView() 
	{
		super.MultiTouchEvent();
	}

	/**
	 * ビューを設定する
	 * @param v ビュー
	 * @param ox oy スクリーン座標とのオフセット
	 */
    public void setView( View v, int ox, int oy ) 
	{
    	viewLeft   = ox + v.getLeft();
		viewRight  = ox + v.getRight();
    	viewTop    = oy + v.getTop();
		viewBottom = oy + v.getBottom();
	}

	/**
	 * ビューの名前を設定
	 * @param s 画像の名前
	 */
    public void setViewName( String s ) 
	{
		viewName = s;
	}

	/**
	 * タッチされたの判定フラグを取得する
	 * @return 判定フラグ
	 */
    public boolean getTouchValid() 
	{
		return touchValid;
	}

	/**
	 * タッチされたX座標を取得する
	 * @return X座標
	 */
    public int getTouchX() 
	{
		return touchX;
	}

	/**
	 * タッチされたY座標を取得する
	 * @return Y座標
	 */
    public int getTouchY() 
	{
		return touchY;
	}

	/**
	 * タッチされた強さを取得する
	 * @return 強さ
	 */
    public double getTouchPressure() 
	{
		return touchPressure;
	}

	/**
	 * タッチされた大きさを取得する
	 * @return 大きさ
	 */
    public double getTouchSize() 
	{
		return touchSize;
	}

	/**
	 * マルチタッチのインデックスを取得する
	 * @return インデックス
	 */
    public int getPointerIndex() 
	{
		return pointerIndex;
	}

	/**
	 * マルチタッチのIDを取得する
	 * @return ID
	 */
    public int getPointerId() 
	{
		return pointerId;
	}

	/**
	 * ダウン状態のときIDを取得する
	 * @return ID
	 */
    public int getPointerIdDown() 
	{
		return pointerIdDown;
	}

	/**
	 * タッチの状態を取得する
	 * @return タッチの状態
	 */
    public int getTouchStatus() 
	{
		return touchStatus;
	}

    /**
	 * タッチイベントの処理
	 * @param  event MotionEvent
     */
    public void execTouchEventView( MotionEvent event ) 
	{
		clearTouchParam();

    	int action = event.getAction();
		getEventName( event );

   		switch (action) 
		{
    	case MotionEvent.ACTION_DOWN:
    		execTouchDown( event );
    		break;

    	case MotionEvent.ACTION_UP:
    		execTouchUp( event );
    		break;

    	case MotionEvent.ACTION_MOVE:
			execTouchMove( event );
    		break;

       	default:
			/* ポインタ・ダウンなら */
       		if ( eventPointerDown ) {
       			execPointerDown( event, eventPointerIndex );

			/* ポインタ・アップなら */
       		} else if ( eventPointerUp ) {
       			execPointerUp( event, eventPointerIndex );
       		}
        	break;
    	}
	}

    /**
	 * タッチイベントの処理 (ダウン)
	 * １つ目のタッチダウンのときに通知される
	 * @param  event MotionEvent
     */
    protected void execTouchDown( MotionEvent event )
	{
		/* タッチされていれば */
		if ( checkTouchScreen( event, 0 ) ) {
			saveTouchParam( event, 0 );
			setTouchStatusDown();
		}
	}

    /**
	 * タッチイベントの処理 (アップ)
	 * 最後の１個がアップされたときに通知される
	 * @param  event MotionEvent
     */
    protected void execTouchUp( MotionEvent event )
	{
		/* アップ状態でなければ */
		if ( touchStatus != TOUCH_STATUS_UP ) {
			saveTouchParam( event, 0 );
			setTouchStatusUp();
		}
	}

    /**
	 * タッチイベントの処理 (ポインタダウン)
	 * １つ目以降のタッチダウンのときに通知される
	 * @param event MotionEvent
	 * @param index ポインタインデックス
     */
    protected void execPointerDown( MotionEvent event, int index )
	{
	 	/* ポインタがなければ、何もしない (念のための検査)　*/
		if ( ! checkPointerCount( event ) ) {
			return;
		}

		/* タッチされていれば */
		if ( checkTouchScreen( event, index ) ) {
			saveTouchParam( event, index );
			setTouchStatusDown();
		}
	}

    /**
	 * タッチイベントの処理 (ポインタアップ)
	 * インデックスに紐付けされたタッチイベントがアップされたときに通知される
	 * @param event MotionEvent
	 * @param index ポインタインデックス
     */
    protected void execPointerUp( MotionEvent event, int index )
	{
	 	/* ポインタがなければ、何もしない (念のための検査)　*/
		if ( ! checkPointerCount( event ) ) {
			return;
		}

		/* IDが一致すれば */
		if ( event.getPointerId(index) == pointerIdDown ) {
			saveTouchParam( event, index );
			setTouchStatusUp();
		}
	}

    /**
	 * タッチイベントの処理 (ムーブ)
	 * 移動したときに通知される
	 * @param event MotionEvent
     */
	protected void execTouchMove( MotionEvent event )
	{
		int x = 0;
		int y = 0;
		boolean	flag = false;
		String msg = "";

		int count = event.getPointerCount();
		int size  = event.getHistorySize();

	 	/* ポインタがなければ、何もしない (念のための検査)　*/
		if ( ! checkPointerCount( count, 1 ) ) {
			return;
		}

		/* イベントが発生しても、来歴がないことがある */
		if ( size < 1 ) {
			return;
		}

		/* ログに */
		logDebugView( "move check " + viewName );

		/* 全てのポインターを調べる */
		for (int i=0; i<count; i++)
		{
			/* 全てのムーブ・ポイントを調べる */
			for (int j=0; j<size; j++)
			{
				/**
				 * getHistoricalX() getHistoricalY()
		 		 * イベントが発生する毎に
				 * 全てのポインタの座標を記録しているようだ
		 		 */
				x = (int)event.getHistoricalX(i,j);
				y = (int)event.getHistoricalY(i,j);
				msg = "move " + i + " " + j + " " + x + " " + y + " ";

				/* タッチされていれば */
				if ( checkTouchScreen(x,y) ) {
					flag = true;
					msg += "* " + viewName;
				}

				/* ログに */
				logDebugView( msg );
			}
		}

		/* 前のタッチ状態により、次のタッチ状態が決まる */
		switch ( touchStatus )
		{
		case TOUCH_STATUS_UP:
			/* アップ状態で、タッチされていれば */
			if ( flag ) {
				setTouchStatusIn();
			}
			break;

		case TOUCH_STATUS_DOWN:
			/* ダウン状態で、タッチされていなければ */
			if ( !flag ) {
				setTouchStatusOut();
			}
			break;

		case TOUCH_STATUS_IN:
			/* イン状態で、タッチされていなければ */
			if ( !flag ) {
				setTouchStatusOut();
			}
			break;

		case TOUCH_STATUS_OUT:
			/* アウト状態で、タッチされていれば */
			if ( flag ) {
				setTouchStatusIn();
			}
			break;
		}
	}



    /**
	 * タッチの状態をダウンに設定する
     */
    protected void setTouchStatusDown()
	{
		touchStatus = TOUCH_STATUS_DOWN;
		touchValid  = true;
		pointerIdDown = pointerId;
	}

    /**
	 * タッチの状態をアップに設定する
     */
    protected void setTouchStatusUp()
	{
		touchStatus = TOUCH_STATUS_UP;
		touchValid  = true;
		pointerIdDown = POINTER_ID_NOT_SET;
	}

    /**
	 * タッチの状態をインに設定する
     */
    protected void setTouchStatusIn()
	{
		touchStatus = TOUCH_STATUS_IN;
		touchValid  = true;
	}

    /**
	 * タッチの状態をアウトに設定する
     */
    protected void setTouchStatusOut()
	{
		touchStatus = TOUCH_STATUS_OUT;
		touchValid  = true;
	}

	/**
	 * イベント変数の初期化
	 */
    protected void clearTouchParam() 
	{
		saveTouchParam( 
			POINTER_INDEX_NOT_SET, 
			POINTER_ID_NOT_SET,
			0, 0, 
			0.0, 0.0, 
			false );

		touchValid  = false;
	}

    /**
	 * イベント変数を保存する
	 * @param event MotionEvent
	 * @param index ポインタインデックス
     */
    protected void saveTouchParam( MotionEvent event, int index )
	{
		saveTouchParam( 
			index,
			event.getPointerId(index),
			(int)event.getX(index), 
			(int)event.getY(index), 
			(double)event.getPressure(index),
			(double)event.getSize(index),
			true
		);
	}

    /**
	 * イベント変数を保存する
	 * @param index ポインタインデックス 
	 * @param id    ポインタID
	 * @param x,y   ポインタ座標
	 * @param p     ポインタ強さ
	 * @param s     ポインタ大きさ
	 * @param flag  ログを残すかのフラグ
     */
    protected void saveTouchParam( int index, int id, int x, int y, double p, double s, boolean flag )
	{
		pointerIndex  = index;
		pointerId     = id;
		touchX        = x;
		touchY        = y;
		touchPressure = p;
		touchSize     = s;

		/* ログに */
		if ( flag ) {
			String msg = eventName + " "
				+ x + " " + y + " "
				+ Double.toString(p) + " "
				+ Double.toString(s) + " "
				+ id + " " + viewName ;
			logDebugView( msg );
		}
	}

    /**
	 * ビューがタッチされたかを判定する
	 * @param event MotionEvent
	 * @param index ポインタインデックス
	 * @return タッチされたかの判定結果
     */
    protected boolean checkTouchScreen( MotionEvent event, int index )
	{
		return checkTouchScreen( 
			(int)event.getX(index), 
			(int)event.getY(index) );
	}

    /**
	 * ビューがタッチされたかを判定する
	 * @param  x, y タッチされたスクリーン座標
	 * @return タッチされたかの判定結果
     */
    protected boolean checkTouchScreen( int x, int y )
	{
		/* タッチされた座標はビューの内側か */
		if ((x > viewLeft)&&(x < viewRight)&&(y > viewTop)&&(y < viewBottom)) {
			return true;
		}
		return false;
	}

    /**
	 * デバックログ
     */
    protected void logDebugView( String msg )
	{
		if ( VIEW_DEBUG ) {
			Log.d(VIEW_TAG, msg);
		}
	}
}