package jp.ohwada.android.multitouchsample;

import jp.ohwada.android.multitouchsample.MultiTouchView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 画像がタッチされたときの処理
 * 具体的な処理は、何をやらせたいかで異なる
 */
public class MultiTouchImageView extends MultiTouchView
{
	/** デバック用 */
	static final protected boolean IMAGE_DEBUG = true;
	static final String IMAGE_TAG = "MultiTouchImageView";

	/** タッチの大きさの閾値 (実機に合わせて調整) */
	final protected double IMAGE_SIZE_BIG = 0.15;

	/* 画像のビュー */
	protected ImageView imageView;

	/* リソース */
	protected Resources resources;

	/* ビットマップ */
	protected Bitmap bitmapStrong;
	protected Bitmap bitmapWeak;
	protected Bitmap bitmapOn;
	protected Bitmap bitmapOff;
	protected Bitmap bitmapIn;
	protected Bitmap bitmapOut;
	protected Bitmap bitmapPinchOn;
	protected Bitmap bitmapPinchOff;

	/** 青・赤どちらを表示しているかの状態 */
    protected boolean imageToggle = false;

	/**
	 * コンストラクタ
	 * @param v ビュー
	 * @param ox oy スクリーン座標とのオフセット
	 */
	public void MultiTouchImageView() 
	{
		super.MultiTouchView();
	}

	/**
	 * ImageView を設定する
	 * @param v ImageView
	 * @param ox oy スクリーン座標とのオフセット
	 */
    public void setImageView( ImageView v, int ox, int oy ) 
	{
		imageView = v ;
		setView( v, ox, oy );
	}

	/**
	 * リソースを設定
	 * @param r リソース
	 */
	public void setResources( Resources r ) 
	{
		resources = r;
	}

	/**
	 * 画像の設定 (強い)
	 * @param id リソースID
	 */
	public void setImageStrong( int id ) 
	{
		bitmapStrong = idToBitmap( id );
	}

	/**
	 * 画像の設定 (弱い)
	 * @param id リソースID
	 */
	public void setImageWeak( int id ) 
	{
		bitmapWeak = idToBitmap( id );
	}

	/**
	 * 画像の設定 (オン)
	 * @param id リソースID
	 */
	public void setImageOn( int id ) 
	{
		bitmapOn = idToBitmap( id );
	}

	/**
	 * 画像の設定 (オフ)
	 * @param id リソースID
	 */
	public void setImageOff( int id ) 
	{
		bitmapOff = idToBitmap( id );
	}


	/**
	 * 画像の設定 (イン)
	 * @param id リソースID
	 */
	public void setImageIn( int id ) 
	{
		bitmapIn = idToBitmap( id );
	}

	/**
	 * 画像の設定 (アウト)
	 * @param id リソースID
	 */
	public void setImageOut( int id ) 
	{
		bitmapOut = idToBitmap( id );
	}

	/**
	 * 画像の設定 (pinch on)
	 * @param id リソースID
	 */
	public void setImagePinchOn( int id ) 
	{
		bitmapPinchOn = idToBitmap( id );
	}

	/**
	 * 画像の設定 (pinch off)
	 * @param id リソースID
	 */
	public void setImagePinchOff( int id ) 
	{
		bitmapPinchOff = idToBitmap( id );
	}

	/**
	 * 青・赤どちらを表示しているかの状態を取得する
	 * @return 状態
	 */
    public boolean getImageToggle() 
	{
		return imageToggle;
	}

    /**
	 * タッチイベントの処理
	 * @param  event MotionEvent
	 * @return イベントに変化があったかの判定結果
     */
	public boolean execTouchEventImageView( MotionEvent event ) 
	{
		execTouchEventView( event );

		/* イベントに変化があれば */
		if ( touchValid ) {
			debugMessage = eventName + " " + viewName;

			switch ( touchStatus )
			{
			case TOUCH_STATUS_UP:
				execImageUp();
				break;

			case TOUCH_STATUS_DOWN:
				execImageDown();
				break;

			case TOUCH_STATUS_IN:
				execImageIn();
				break;

			case TOUCH_STATUS_OUT:
				execImageOut();
				break;
			}

			return true;
		}

		return false;
	}

	/**
	 * 画像にタッチしていいるかを判定する
	 * @return 判定結果
     */
    public boolean checkImageStatusInTouch() 
	{
			switch ( touchStatus )
			{
			case TOUCH_STATUS_DOWN:
			case TOUCH_STATUS_IN:
				return true;
			}
			return false;
	}

    /**
	 * 画像の処理 (ダウン)
     */
    protected void execImageDown() 
	{
		/**
		 * pressure の値はさほど違いがなっかたので 
		 * size の値で強さを判定する 
		 */

		/* 強いとき */
		if ( touchSize > IMAGE_SIZE_BIG ) {
			setImageBitmapStrong();

		/* 弱いとき */
		} else {
			setImageBitmapWeak();
		}
	}

    /**
	 * 画像の処理 (アップ)
	 * 画像を交互に表示する
     */
    protected void execImageUp() 
	{
		/* オンのときは、オフ画像に */
		if ( imageToggle ) {
			setImageBitmapOff();

		/* オフのときは、オン画像に */
		} else {
			setImageBitmapOn();
		}
	}

    /**
	 * 画像の処理 (イン)
     */
    protected void execImageIn() 
	{
		setImageBitmapIn();
	}

    /**
	 * 画像の処理 (アウト)
     */
    protected void execImageOut() 
	{
		setImageBitmapOut();
	}

    /**
	 * 画像を変更する (強い)
     */
    public void setImageBitmapStrong() 
	{
		setImageBitmap( bitmapStrong );
	}

    /**
	 * 画像を変更する (弱い)
     */
    public void setImageBitmapWeak() 
	{
		setImageBitmap( bitmapWeak );
	}

    /**
	 * 画像を変更する (オン)
     */
    public void setImageBitmapOn() 
	{
		imageToggle = true;
		setImageBitmap( bitmapOn );
	}

    /**
	 * 画像を変更する (オフ)
     */
    public void setImageBitmapOff() 
	{
		imageToggle = false;
		setImageBitmap( bitmapOff );
	}

    /**
	 * 画像を変更する (イン)
     */
    public void setImageBitmapIn() 
	{
		setImageBitmap( bitmapIn );
	}

    /**
	 * 画像を変更する (アウト)
     */
    public void setImageBitmapOut() 
	{
		setImageBitmap( bitmapOut );
	}

    /**
	 * 画像を変更する (pinch on)
     */
    public void setImageBitmapPinchOn() 
	{
		setImageBitmap( bitmapPinchOn );
	}

    /**
	 * 画像を変更する (pinch on)
     */
    public void setImageBitmapPinchOff() 
	{
		setImageBitmap( bitmapPinchOff );
	}

    /**
	 * 画像を設定する
	 * @param  id  リソースID
     */
    protected void setImageBitmap( Bitmap m ) 
	{
		imageView.setImageBitmap( m );
	}

    /**
	 * リソースID を ビットマップ に変換する
	 * @param  id  リソースID
	 * @return ビットマップ
     */
    protected Bitmap idToBitmap( int id ) 
	{
		return BitmapFactory.decodeResource( resources, id );
	}

    /**
	 * デバックログ
     */
    protected void logDebugImage( String msg )
	{
		if ( IMAGE_DEBUG ) {
			Log.d(IMAGE_TAG, msg);
		}
	}
}