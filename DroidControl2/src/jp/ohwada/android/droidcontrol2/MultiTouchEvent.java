package jp.ohwada.android.droidcontrol2;

import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

/* http://developer.android.com/reference/android/view/MotionEvent.html */
/**
 * イベントのアクション名を取得する
 */
public class MultiTouchEvent
{
	/** デバック用 */
	static final boolean EVENT_DEBUG = true;
	static final String  EVENT_TAG = "MultiTouchEvent";

	/* pinch イベントの状態 */
	static final int EVENT_PINCH_MODE_NONE = 0;
	static final int EVENT_PINCH_MODE_DRAG = 1;
	static final int EVENT_PINCH_MODE_ZOOM = 2;

	/* pinch イベントの状態 */
	static final int EVENT_PINCH_STATUS_NONE = 0;
	static final int EVENT_PINCH_STATUS_IN   = 1;
	static final int EVENT_PINCH_STATUS_OUT  = 2;
	static final int EVENT_PINCH_STATUS_UP   = 3;

	/* pinch と判定する最小の距離 */
	static final float EVENT_PINCH_MODE_DIST = 10;

	/* pointer index */
	protected int eventPointerIndex = 0;

	/* pointer がダウンしたかのフラグ */
	protected boolean eventPointerDown = false;

	/* pointer がアップしたかのフラグ */
	protected boolean eventPointerUp = false;

	/* イベントのアクション名称 (独自に命名) */
	protected String eventName = "";

	/* pinch のパラメータ */
	protected int eventPinchX0 = 0;
	protected int eventPinchY0 = 0;
	protected int eventPinchX1 = 0;
	protected int eventPinchY1 = 0;
	protected int eventPinchDist = 0;

	/* ひとつ前のpinchの距離 */
	protected float eventPinchDistPrev = 0;

	/* pinch イベントの状態 */
	protected int eventPinchMode   = EVENT_PINCH_MODE_NONE;
	protected int eventPinchStatus = EVENT_PINCH_STATUS_NONE;

	/* デバック用メッセージ */
	protected String debugMessage = "";

	/**
	 * コンストラクタ
	 */
    public void MultiTouchEvent() 
	{
		/* dummy */
	}

	/**
	 * pointerIndex 変数の値を取得する
	 * @return 変数の値
	 */
    public int getEventPointerIndex() 
	{
		return eventPointerIndex;
	}

	/**
	 * pointerUp 変数の値を取得する
	 * @return 変数の値
	 */
    public boolean getEventPointerUp() 
	{
		return eventPointerUp;
	}

	/**
	 * pointerDown 変数の値を取得する
	 * @return 変数の値
	 */
    public boolean getEventPointerDown() 
	{
		return eventPointerDown;
	}

	/**
	 * pointer イベントが発生したかの判定
	 * @return 判定結果
	 */
    public boolean ckeckEventPointer() 
	{
		if ( eventPointerUp || eventPointerDown ) {
			return true;
		}
		return false;
	}

	/**
	 * アクション名 (ローカル変数) を取得する
	 * @return アクション名
	 */
    public String getEventName() 
	{
		return eventName;
	}

	/**
	 * pinchX0 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchX0() 
	{
		return eventPinchX0;
	}

	/**
	 * pinchY0 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchY0() 
	{
		return eventPinchY0;
	}

	/**
	 * pinchX1 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchX1() 
	{
		return eventPinchX1;
	}

	/**
	 * pinchY1 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchY1() 
	{
		return eventPinchY1;
	}

	/**
	 * pinchDist 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchDist() 
	{
		return eventPinchDist;
	}

	/**
	 * pinchDistPrev 変数の値を取得する
	 * @return 変数の値
	 */
    public float getPinchDistPrev() 
	{
		return eventPinchDistPrev;
	}

	/**
	 * pinchMode 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchMode() 
	{
		return eventPinchMode;
	}

	/**
	 * pinchStatus 変数の値を取得する
	 * @return 変数の値
	 */
    public int getPinchStatus() 
	{
		return eventPinchStatus;
	}

	/**
	 * pinch 状態を判定する (none)
	 * @return 判定結果
	 */
    public boolean checkPinchStatusNone() 
	{
		if ( eventPinchStatus == EVENT_PINCH_STATUS_NONE ) {
			return true;
		}
		return false;
	}

	/**
	 * pinch 状態を判定する (in)
	 * @return 判定結果
	 */
    public boolean checkPinchStatusIn() 
	{
		if ( eventPinchStatus == EVENT_PINCH_STATUS_IN ) {
			return true;
		}
		return false;
	}

	/**
	 * pinch 状態を判定する (out)
	 * @return 判定結果
	 */
    public boolean checkPinchStatusOut() 
	{
		if ( eventPinchStatus == EVENT_PINCH_STATUS_OUT ) {
			return true;
		}
		return false;
	}

	/**
	 * pinch 状態を判定する (up)
	 * @return 判定結果
	 */
    public boolean checkPinchStatusUp() 
	{
		if ( eventPinchStatus == EVENT_PINCH_STATUS_UP ) {
			return true;
		}
		return false;
	}

	/**
	 * デバック用メッセージを取得する
	 * @return メッセージ
	 */
	public String getDebugMessage() 
	{
		return debugMessage;
	}

    /**
	 * アクション名を取得する (独自に命名したもの)
	 * @param event MotionEvent
	 * @return アクション名
     */
    public String getEventName( MotionEvent event ) 
	{
		return getEventName( event.getAction() );
	}

    /**
	 * アクション名を取得する (独自に命名したもの)
	 * @param action アクション番号
	 * @return アクション名
     */
    public String getEventName( int action ) 
	{
		clearEventParam();

    	String name = "unknown:" + action;

    	switch (action) 
		{
    	case MotionEvent.ACTION_DOWN:
    		name = "down";
       		break;

    	case MotionEvent.ACTION_UP:
    		name = "up";
        	break;

    	case MotionEvent.ACTION_MOVE:
    		name = "move";
        	break;

    	case MotionEvent.ACTION_CANCEL:
    		name = "cancel";
        	break;

    	case MotionEvent.ACTION_OUTSIDE:
    		name = "outsize";
        	break;

    	case MotionEvent.ACTION_POINTER_DOWN:
    		name = "pointer_down";
			eventPointerIndex = 0;
			eventPointerDown  = true;
       		break;

    	case MotionEvent.ACTION_POINTER_UP:
    		name = "pointer_up";
			eventPointerIndex = 0;
			eventPointerUp    = true;
	       	break;

        default:
        	int point = action & MotionEvent.ACTION_MASK ;
			int index = (action & MotionEvent.ACTION_POINTER_ID_MASK)
		    			>> MotionEvent.ACTION_POINTER_ID_SHIFT;
        	String pref = "pointer_" + index + "_";

        	switch (point)
        	{
        	case MotionEvent.ACTION_POINTER_DOWN:
				/* pintter_xx_down */
        		name = pref + "down";
				eventPointerIndex = index;
				eventPointerDown  = true;
        		break;

        	case MotionEvent.ACTION_POINTER_UP:
				/* pintter_xx_up */ 
        		name = pref + "up";
				eventPointerIndex = index;
				eventPointerUp    = true;
        		break;
        	}
    	}

		eventName = name;
		return name;
	}

	/**
	 * pinch を判定する
	 * @param  event MotionEvent
	 * @return 判定結果
	 */
	public int checkPinch(MotionEvent event) 
	{
		eventPinchStatus = EVENT_PINCH_STATUS_NONE;
		debugMessage = "";

		switch (event.getAction() & MotionEvent.ACTION_MASK) 
		{
		case MotionEvent.ACTION_DOWN:
			eventPinchMode = EVENT_PINCH_MODE_DRAG;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			eventPinchDistPrev = calcPinchDist(event);
            eventPinchMode = EVENT_PINCH_MODE_ZOOM;
			break;

		case MotionEvent.ACTION_UP:
			eventPinchMode    = EVENT_PINCH_MODE_NONE;
			eventPinchStatus  = EVENT_PINCH_STATUS_UP;
			debugMessage = "pinch up";
			break;

		case MotionEvent.ACTION_POINTER_UP:
			eventPinchMode = EVENT_PINCH_MODE_NONE;
			break;

		case MotionEvent.ACTION_MOVE:
			execPinchMove(event);
         	break;
		}
		return eventPinchStatus;
   }

	/**
	 * pinch move の処理
	 * @param  event MotionEvent
	 */
	protected void execPinchMove(MotionEvent event) 
	{
		/* zoom でなければ */
		if (eventPinchMode != EVENT_PINCH_MODE_ZOOM) {
			return;
		}

		float dist = calcPinchDist(event);

		/* 一定量 移動してなければ */
		if ( Math.abs( dist - eventPinchDistPrev ) < EVENT_PINCH_MODE_DIST ) {
			return;
		}

		/* 大きくなっていれば */
		if ( dist > eventPinchDistPrev ) {
			eventPinchStatus  = EVENT_PINCH_STATUS_OUT;
			debugMessage = "pinch out";

		/* 小さくなっていれば */
		} else {
			eventPinchStatus  = EVENT_PINCH_STATUS_IN;
			debugMessage = "pinch in";
		}

		savePinchParam(event, dist );
		eventPinchDistPrev = dist;
	}

	/**
	 * ２つのタッチの距離を計算する 
	 * @param  event MotionEvent
	 * @return 距離
	 */
	protected float calcPinchDist(MotionEvent event) 
	{
		// タッチが２つ以上なければ、0 を返す
		if ( ! checkPointerCount(event, 2) ) {
			return 0;
		}

		return calcPinchDist(
			event.getX(0), event.getY(0), 
			event.getX(1), event.getY(1) );
	}

	/**
	 * ２つのタッチの距離を計算する 
	 * @param  x0, y0 タッチ0の座標
	 * @param  x1, y1 タッチ1の座標
	 * @return 距離
	 */
	protected float calcPinchDist(float x0, float y0, float x1, float y1 )
	{
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * pinch パラメータを保存する
	 * @param  event MotionEvent
	 * @param  dist   タッチ0と1の距離
	 */
	protected void savePinchParam(MotionEvent event, float dist )
	{
		savePinchParam(
			event.getX(0), event.getY(0), 
			event.getX(1), event.getY(1),
			dist );
	}

	/**
	 * pinch パラメータを保存する
	 * @param  x0, y0 タッチ0の座標
	 * @param  x1, y1 タッチ1の座標
	 * @param  dist   タッチ0と1の距離
	 */
	protected void savePinchParam(float x0, float y0, float x1, float y1, float dist )
	{
		eventPinchX0   = (int)x0;
		eventPinchY0   = (int)y0;
		eventPinchX1   = (int)x1;
		eventPinchY1   = (int)y1;
		eventPinchDist = (int)dist;
	}

	/**
	 * イベント変数の初期化
	 */
    protected void clearEventParam() 
	{
		eventPointerIndex = 0;
		eventPointerDown  = false;
		eventPointerUp    = false;
		eventName         = "";
	}

    /**
	 * ポインタがあるか判定する
	 * @param event MotionEvent
	 * @return 判定結果
     */
    protected boolean checkPointerCount( MotionEvent event, int n )
	{
		return checkPointerCount( 
			event.getPointerCount(), n );
	}

    /**
	 * ポインタがあるか判定する
	 * @param event MotionEvent
	 * @return 判定結果
     */
    protected boolean checkPointerCount( MotionEvent event )
	{
		return checkPointerCount( 
			event.getPointerCount(), 1 );
	}
    
    /**
	 * ポインタがあるか判定する
	 * @return 判定結果
     */
    protected boolean checkPointerCount( int count, int n )
	{
		/* ポインタの数が n 以上であれば */
		if ( count >= n ) {
			return true;
		}
		return false;
	}

    /**
	 * デバックログ
     */
    protected void logDebugEvent( String msg )
	{
		if ( EVENT_DEBUG ) {
			Log.d(EVENT_TAG, msg);
		}
	}
}