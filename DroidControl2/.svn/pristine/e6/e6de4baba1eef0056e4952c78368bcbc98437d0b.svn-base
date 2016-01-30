package jp.ohwada.android.droidcontrol2;

import jp.ohwada.android.droidcontrol2.MultiTouchImageView;

import android.view.MotionEvent;

/**
 * 画像がタッチされたときの処理 (DroidControl2用)
 */
public class DroidImageView extends MultiTouchImageView
{
	/**
	 * コンストラクタ
	 */
	public DroidImageView() 
	{
		super.MultiTouchImageView();
	}

    /**
	 * タッチイベントの処理
	 * @param  event MotionEvent
	 * @return イベントに変化があったかの判定結果
     */
	public boolean execTouchEvent( MotionEvent event ) 
	{
		execTouchEventView( event );

		/* イベントに変化があれば */
		if ( touchValid ) {
			debugMessage = eventName + " " + viewName;

			switch ( touchStatus )
			{
			case TOUCH_STATUS_DOWN:
				if ( imageToggle ) {
					imageToggle = false;
				} else {
					imageToggle = true;
				}
				return true;
			}
		}

		return false;
	}

}