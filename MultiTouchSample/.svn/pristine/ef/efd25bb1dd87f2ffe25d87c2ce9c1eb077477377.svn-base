package jp.ohwada.android.multitouchsample;

import jp.ohwada.android.multitouchsample.MultiTouchImageView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * メインの画面
 */
public class main extends Activity
{
	/** デバック・メッセージを表示する数 */
	final private int MAX_MSG = 10;

	/** 画面の大きさ */
	private int displayWidthDiv = 0;

	/* リソース */
	private Resources resources;

	/** 画面のビュー */
    private View viewContent;
    private LinearLayout layout1;
    private LinearLayout layout2;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
    private TextView text1;

	/* 画像を処理するクラス */
	private MultiTouchImageView view1;
	private MultiTouchImageView view2;
	private MultiTouchImageView view3;
	private MultiTouchImageView view4;

	/* タッチイベントを処理するクラス */
   	private MultiTouchEvent mEvent;

	/** スクリーンの原点からのオフセット */
    private int	offsetX1 = 0;
    private int	offsetY1 = 0;
    private int	offsetX2 = 0;
    private int	offsetY2 = 0;

	/** pinch の状態 */
	private int	pinchStatus = -1;

	/* アクション名称 (独自に命名) */
	private String actionName = "";

	/** デバック・メッセージのリスト */
	private List<String> msgs = new ArrayList<String>();

	/**
	 * 初期設定
	 * @param savedInstanceState Bundle
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		/* 画面の大きさを取得する */
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		displayWidthDiv = (int) ( display.getWidth() / 7 );

		/* リソースを取得する */
		resources = getResources();

		/* UIの素材のビューを取得する */
    	viewContent = getWindow().findViewById(Window.ID_ANDROID_CONTENT);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
		image1  = (ImageView) findViewById(R.id.image1);
		image2  = (ImageView) findViewById(R.id.image2);
		image3  = (ImageView) findViewById(R.id.image3);
		image4  = (ImageView) findViewById(R.id.image4);
		text1   = (TextView)  findViewById(R.id.text1);

		/* タッチイベントを処理するクラス */
    	mEvent = new MultiTouchEvent();
    }

    /**
	 * タッチイベントの処理
	 * @param  event MotionEvent
	 * @return 常に true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) 
	{
    	int action = event.getAction();

		/* タッチイベントを処理するクラス */
   		actionName   = mEvent.getEventName( action );
		boolean flag = mEvent.ckeckEventPointer();
		               mEvent.checkPinch( event );

   		switch (action) 
		{
    	case MotionEvent.ACTION_DOWN:
    	case MotionEvent.ACTION_UP:
    	case MotionEvent.ACTION_MOVE:
			execTouchEvent( event );
    		break;

       	default:
			/* アクションが pointer であれば */
			if ( flag ) {
				execTouchEvent( event );

			/* そうでなければ */
			} else {
       			addMsg( actionName );
       		}
        	break;
    	}

   		printMsg();
		return true;
	}

    /**
	 * 画像がタッチされたときの処理
	 * @param  event MotionEvent
     */
    private void execTouchEvent( MotionEvent event )
	{
		calcOffset();

		boolean flag = false;

		/* オブジェクトがなければ、生成する */
		if ( view1 == null ) {
			view1 = createView( image1, offsetX1, offsetY1, "image1" );
		}

		/* オブジェクトがなければ、生成する */
		if ( view2 == null ) {
			view2 = createView( image2, offsetX1, offsetY1, "image2" );
		}

		/* オブジェクトがなければ、生成する */
		if ( view3 == null ) {
			view3 = createView( image3, offsetX2, offsetY2, "image3" );
		}

		/* オブジェクトがなければ、生成する */
		if ( view4 == null ) {
			view4 = createView( image4, offsetX2, offsetY2, "image4" );
		}

		boolean flag1 = view1.execTouchEventImageView( event );

		/* 画像がタッチされれば */
		if ( flag1 ) {
			addMsg( view1.getDebugMessage() );
			flag = true;
		}

		boolean flag2 = view2.execTouchEventImageView( event );

		/* 画像がタッチされれば */
		if ( flag2 ) {
			addMsg( view2.getDebugMessage() );
			flag = true;
		}

		boolean flag3 = view3.execTouchEventImageView( event );

		/* 画像がタッチされれば */
		if ( flag3 ) {
			addMsg( view3.getDebugMessage() );
			flag = true;
		}

		boolean flag4 = view4.execTouchEventImageView( event );

		/* 画像がタッチされれば */
		if ( flag4 ) {
			addMsg( view4.getDebugMessage() );
			flag = true;
		}

		/* どの画像もタッチされなければ */
		if ( flag == false ) {

			/* pinch であれば */
			if ( checkPinch() ) {
				execPinch();

			/* move でなければ */
			} else if (actionName != "move") {
				String msg = actionName + " " + 
					(int)event.getX() + " " +
					(int)event.getY() ;
				addMsg( msg );
			}
		}
	}

	/**
	 * 画像を処理するクラスを生成する
	 * @param  image ImageView
	 * @param  offsetX offsetY スクリーンの原点からのオフセット
	 * @param  name 画像の名前
	 */
    private MultiTouchImageView createView( ImageView image, int offsetX, int offsetY, String name ) 
	{
    	MultiTouchImageView view = new MultiTouchImageView();
    	view.setImageView( image, offsetX, offsetY );
		view.setViewName(  name );
		view.setResources( resources );
		view.setImageStrong( R.drawable.yellow );
		view.setImageWeak(   R.drawable.lemon );
		view.setImageOn(     R.drawable.red );
		view.setImageOff(    R.drawable.green );
		view.setImageIn(     R.drawable.gray );
		view.setImageOut(    R.drawable.white );
		view.setImagePinchOn(  R.drawable.orenge );
		view.setImagePinchOff( R.drawable.blue );
		return view;
	}

    /**
	 * スクリーンの原点からのオフセットを計算する
     */
    private void calcOffset()
	{
		int viewLeft  = viewContent.getLeft();
		int viewTop   = viewContent.getTop();

    	offsetX1 = viewLeft + layout1.getLeft();
    	offsetY1 = viewTop  + layout1.getTop();
		offsetX2 = viewLeft + layout2.getLeft();
    	offsetY2 = viewTop  + layout2.getTop();
	}

	/**
	 * pinch 状態か判定する
	 * @return 判定結果
	 */
    private boolean checkPinch() 
	{
		/* 画像にタッチしていれば */
		if ( view1.checkImageStatusInTouch() ) {
			return false;
		}
		if ( view2.checkImageStatusInTouch() ) {
			return false;
		}
		if ( view3.checkImageStatusInTouch() ) {
			return false;
		}
		if ( view4.checkImageStatusInTouch() ) {
			return false;
		}

		/* pinch 状態でなければ */
		if ( mEvent.checkPinchStatusNone() ) {
			return false;
		}
		return true;
	}

	/**
	 * pinch を実行する
	 */
    private void execPinch() 
	{
		int dist   = mEvent.getPinchDist();
		String msg = mEvent.getDebugMessage();

		/* up イベントであれば */
		if (  mEvent.checkPinchStatusUp() ) {

			/* この状態(0)でなければ */
			if ( pinchStatus != 0 ) {
				pinchStatus = 0;
				view1.setImageBitmapPinchOff();
				view2.setImageBitmapPinchOff();
				view3.setImageBitmapPinchOff();
				view4.setImageBitmapPinchOff();
				addMsg( msg + " 0" );
			}

		/* pinch の距離が 5/7 以上 */
		} else if ( dist > 5*displayWidthDiv ) {

			/* この状態(4)でなければ */
			if ( pinchStatus != 4 ) {
				pinchStatus = 4;
				view1.setImageBitmapPinchOn();
				view2.setImageBitmapPinchOn();
				view3.setImageBitmapPinchOn();
				view4.setImageBitmapPinchOn();
				addMsg( msg + " 4" );
			}

		/* pinch の距離が 4/7 以上 */
		} else if ( dist > 4*displayWidthDiv ) {

			/* この状態(3)でなければ */
			if ( pinchStatus != 3 ) {
				pinchStatus = 3;
				view1.setImageBitmapPinchOn();
				view2.setImageBitmapPinchOn();
				view3.setImageBitmapPinchOn();
				view4.setImageBitmapPinchOff();
				addMsg( msg + " 3" );
			}

		/* pinch の距離が 3/7 以上 */
		} else if ( dist > 3*displayWidthDiv ) {

			/* この状態(2)でなければ */
			if ( pinchStatus != 2 ) {
				pinchStatus = 2;
				view1.setImageBitmapPinchOn();
				view2.setImageBitmapPinchOn();
				view3.setImageBitmapPinchOff();
				view4.setImageBitmapPinchOff();
				addMsg( msg + " 2" );
			}

		/* pinch の距離が 2/7 以上 */
		} else if ( dist > 2*displayWidthDiv ) {

			/* この状態(1)でなければ */
			if ( pinchStatus != 1 ) {
				pinchStatus = 1;
				view1.setImageBitmapPinchOn();
				view2.setImageBitmapPinchOff();
				view3.setImageBitmapPinchOff();
				view4.setImageBitmapPinchOff();
				addMsg( msg + " 1" );
			}
		}
	}

	/**
	 * メッセージをリストに追加する
	 * @param msg 文字列
	 */
    private void addMsg( String msg ) 
	{
		msgs.add( msg );
	}

	/**
	 * メッセージを出力する
	 */
    private void printMsg() 
	{
		/* 10個だけ表示するように start end を設定する */
		int start = 0;
		int end = msgs.size();
		if ( end > MAX_MSG ) {
			start = end - MAX_MSG;
		}

		/* メッセージの連結 */
		String msg = "";
		for ( int i = start; i < end; i++ ) {
			msg += msgs.get( i );
			msg += "\n";
		}

		/* 画面表示 */
   		text1.setText(msg);
	}
}
