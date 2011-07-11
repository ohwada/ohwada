package jp.ohwada.android.multitouchsample;

import android.view.MotionEvent;

/* http://developer.android.com/reference/android/view/MotionEvent.html */
/**
 * イベントのアクション名を取得する
 */
public class MultiTouchEvent
{
	/* pointer index */
	protected int pointerIndex = 0;

	/* pointer がダウンしたかのフラグ */
	protected boolean pointerDown = false;

	/* pointer がアップしたかのフラグ */
	protected boolean pointerUp = false;

	/* イベントのアクション名称 (独自に命名) */
	protected String eventName = "";

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
    public int getPointerIndex() 
	{
		return pointerIndex;
	}

	/**
	 * pointerUp 変数の値を取得する
	 * @return 変数の値
	 */
    public boolean getPointerUp() 
	{
		return pointerUp;
	}

	/**
	 * pointerDown 変数の値を取得する
	 * @return 変数の値
	 */
    public boolean getPointerDown() 
	{
		return pointerDown;
	}

    /**
	 * イベントのアクションが pointer か
	 * @return true / false
     */
    public boolean ckeckEventPointer() 
	{
		if 	( pointerUp || pointerDown ) {
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
	 * アクション名を取得する
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
			pointerIndex = 0;
			pointerDown  = true;
       		break;

    	case MotionEvent.ACTION_POINTER_UP:
    		name = "pointer_up";
			pointerIndex = 0;
			pointerUp   = true;
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
				pointerIndex = index;
				pointerDown  = true;
        		break;

        	case MotionEvent.ACTION_POINTER_UP:
				/* pintter_xx_up */ 
        		name = pref + "up";
				pointerIndex = index;
				pointerUp    = true;
        		break;
        	}
    	}

		eventName = name;
		return name;
	}

	/**
	 * イベント変数の初期化
	 */
    private void clearEventParam() 
	{
		pointerIndex = 0;
		pointerDown  = false;
		pointerUp    = false;
		eventName    = "";
	}

}