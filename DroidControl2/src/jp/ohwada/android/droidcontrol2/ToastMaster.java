package jp.ohwada.android.droidcontrol2;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast が固まるのを回避するためのクラス
 * http://www.taosoftware.co.jp/blog/2009/04/android_toast.html
 * http://developer.android.com/reference/android/widget/Toast.html
 */
public class ToastMaster extends Toast
{
	private static Toast sToast = null;

	/**
	 * コンストラクタ
	 */
    public ToastMaster(Context context) 
	{
		super(context);
	}

	/**
	 * Toast を表示する
	 */
    @Override
    public void show() 
	{
    	ToastMaster.setToast(this);
    	super.show();
    }

	/**
	 * Toast オブジェクトを設定する
	 * @param toast Toast オブジェクト
	 */
    public static void setToast(Toast toast) 
	{
        if (sToast != null) sToast.cancel();
        sToast = toast;
    }

	/**
	 * Toast 表示を中断する
	 */
    public static void cancelToast() 
	{
        if (sToast != null) sToast.cancel();
        sToast = null;
    }

	/**
	 * Toast を表示する (長く)
	 * @param context Contextオブジェクト
	 * @param resId   リソースID
	 */
    public static void showLong( Context context, int resId ) 
	{
		makeText(context, resId, LENGTH_LONG).show();
	}

	/**
	 * Toast を表示する (長く)
	 * @param context Contextオブジェクト
	 * @param text    文字列
	 */
    public static void showLong( Context context, CharSequence text ) 
	{
		makeText(context, text, LENGTH_LONG).show();
	}

	/**
	 * Toast を表示する (短く)
	 * @param context Contextオブジェクト
	 * @param resId   リソースID
	 */
    public static void showShort( Context context, int resId ) 
	{
		makeText(context, resId, LENGTH_SHORT).show();
	}

	/**
	 * Toast を表示する (短く)
	 * @param context Contextオブジェクト
	 * @param text    文字列
	 */
    public static void showShort( Context context, CharSequence text ) 
	{
		makeText(context, text, LENGTH_SHORT).show();
	}
}
