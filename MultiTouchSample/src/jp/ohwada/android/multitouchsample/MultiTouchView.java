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
	/** タッチ状態の定義 */
	final protected int MULTI_TOUCH_STATUS_UP   = 0;
	final protected int MULTI_TOUCH_STATUS_DOWN = 1;
	final protected int MULTI_TOUCH_STATUS_IN   = 2; 
	final protected int MULTI_TOUCH_STATUS_OUT  = 4;

	/** インデックスの初期値 */
	final protected int MULTI_TOUCH_INDEX_NOT_SET = -1;

	/** IDの初期値 */
	final protected int MULTI_TOUCH_ID_NOT_SET = -1;

	/** TAG (デバック用) */
	private static final String TAG = "MultiTouchView";

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
	protected int touchIndex = MULTI_TOUCH_INDEX_NOT_SET;

	/* タッチされたID */
	protected int touchId = MULTI_TOUCH_ID_NOT_SET;

	/* ダウン状態になったときのID */
	protected int touchIdDown = MULTI_TOUCH_ID_NOT_SET;

	/* タッチの状態 */
	protected int touchStatus = MULTI_TOUCH_STATUS_UP;

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
    public int getTouchIndex() 
	{
		return touchIndex;
	}

	/**
	 * マルチタッチのIDを取得する
	 * @return ID
	 */
    public int getTouchId() 
	{
		return touchId;
	}

	/**
	 * ダウン状態のときIDを取得する
	 * @return ID
	 */
    public int getTouchIdDown() 
	{
		return touchIdDown;
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
       		if ( pointerDown ) {
       			execPointerDown( event, pointerIndex );
       		} else if ( pointerUp ) {
       			execPointerUp( event, pointerIndex );
       		}
        	break;
    	}
	}

    /**
	 * タッチイベントの処理 (ダウン)
	 * １つ目のタッチダウンのときに通知される
	 * @param  event MotionEvent
     */
    private void execTouchDown( MotionEvent event )
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
    private void execTouchUp( MotionEvent event )
	{
		/* アップ状態でなければ */
		if ( touchStatus != MULTI_TOUCH_STATUS_UP ) {
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
    private void execPointerDown( MotionEvent event, int index )
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
    private void execPointerUp( MotionEvent event, int index )
	{
	 	/* ポインタがなければ、何もしない (念のための検査)　*/
		if ( ! checkPointerCount( event ) ) {
			return;
		}

		/* IDが一致すれば */
		int id = (int)event.getPointerId(index); 
		if ( id == touchIdDown ) {
			saveTouchParam( event, index );
			setTouchStatusUp();
		}
	}

    /**
	 * タッチイベントの処理 (ムーブ)
	 * 移動したときに通知される
	 * @param event MotionEvent
     */
	private void execTouchMove( MotionEvent event )
	{
		int x = 0;
		int y = 0;
		boolean	flag = false;
		String msg = "";

		int count = event.getPointerCount();
		int size  = event.getHistorySize();

	 	/* ポインタがなければ、何もしない (念のための検査)　*/
		if ( ! checkPointerCount( count ) ) {
			return;
		}

		/* イベントが発生しても、来歴がないことがある */
		if ( size < 1 ) {
			return;
		}

		/* ログに */
		msg = "move check " + viewName;
		Log.d(TAG, msg);

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
				Log.d(TAG, msg);
			}
		}

		/* 前のタッチ状態により、次のタッチ状態が決まる */
		switch ( touchStatus )
		{
		case MULTI_TOUCH_STATUS_UP:
			/* アップ状態で、タッチされていれば */
			if ( flag ) {
				setTouchStatusIn();
			}
			break;

		case MULTI_TOUCH_STATUS_DOWN:
			/* ダウン状態で、タッチされていなければ */
			if ( !flag ) {
				setTouchStatusOut();
			}
			break;

		case MULTI_TOUCH_STATUS_IN:
			/* イン状態で、タッチされていなければ */
			if ( !flag ) {
				setTouchStatusOut();
			}
			break;

		case MULTI_TOUCH_STATUS_OUT:
			/* アウト状態で、タッチされていれば */
			if ( flag ) {
				setTouchStatusIn();
			}
			break;
		}
	}

    /**
	 * ポインタがあるか判定する
	 * @param event MotionEvent
	 * @return 判定結果
     */
    private boolean checkPointerCount( MotionEvent event )
	{
		return checkPointerCount( 
			event.getPointerCount() );
	}

    /**
	 * ポインタがあるか判定する
	 * @return 判定結果
     */
    private boolean checkPointerCount( int count )
	{
		if ( count > 0 ) {
			return true;
		}
		return false;
	}

    /**
	 * タッチの状態をダウンに設定する
     */
    private void setTouchStatusDown()
	{
		touchStatus = MULTI_TOUCH_STATUS_DOWN;
		touchValid  = true;
		touchIdDown = touchId;
	}

    /**
	 * タッチの状態をアップに設定する
     */
    private void setTouchStatusUp()
	{
		touchStatus = MULTI_TOUCH_STATUS_UP;
		touchValid  = true;
		touchIdDown = MULTI_TOUCH_ID_NOT_SET;
	}

    /**
	 * タッチの状態をインに設定する
     */
    private void setTouchStatusIn()
	{
		touchStatus = MULTI_TOUCH_STATUS_IN;
		touchValid  = true;
	}

    /**
	 * タッチの状態をアウトに設定する
     */
    private void setTouchStatusOut()
	{
		touchStatus = MULTI_TOUCH_STATUS_OUT;
		touchValid  = true;
	}

	/**
	 * イベント変数の初期化
	 */
    private void clearTouchParam() 
	{
		saveTouchParam( 
			MULTI_TOUCH_INDEX_NOT_SET, 
			MULTI_TOUCH_ID_NOT_SET,
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
    private void saveTouchParam( MotionEvent event, int index )
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
    private void saveTouchParam( int index, int id, int x, int y, double p, double s, boolean flag )
	{
		touchIndex    = index;
		touchId       = id;
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
			Log.d(TAG, msg);
		}
	}

    /**
	 * ビューがタッチされたかを判定する
	 * @param event MotionEvent
	 * @param index ポインタインデックス
	 * @return タッチされたかの判定結果
     */
    private boolean checkTouchScreen( MotionEvent event, int index )
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
    private boolean checkTouchScreen( int x, int y )
	{
		/* タッチされた座標はビューの内側か */
		if ((x > viewLeft)&&(x < viewRight)&&(y > viewTop)&&(y < viewBottom)) {
			return true;
		}
		return false;
	}

}