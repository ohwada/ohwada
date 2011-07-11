package jp.ohwada.android.multitouchsample;

import jp.ohwada.android.multitouchsample.MultiTouchImageView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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

	/** スクリーンの原点からのオフセット */
    private int	offsetX1 = 0;
    private int	offsetY1 = 0;
    private int	offsetX2 = 0;
    private int	offsetY2 = 0;

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
    	MultiTouchEvent mEvent = new MultiTouchEvent();
   		actionName    = mEvent.getEventName( action );
		boolean flag  = mEvent.ckeckEventPointer();

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
		if (( flag == false ) && (actionName != "move")) {
			String msg = actionName + " " + 
				(int)event.getX() + " " +
				(int)event.getY() ;
			addMsg( msg );
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
    	MultiTouchImageView view = new MultiTouchImageView( image, offsetX, offsetY );
		view.setViewName(  name );
		view.setResources( resources );
		view.setImageStrong( R.drawable.yellow );
		view.setImageWeak(   R.drawable.lemon );
		view.setImageOn(     R.drawable.red );
		view.setImageOff(    R.drawable.green );
		view.setImageIn(     R.drawable.gray );
		view.setImageOut(    R.drawable.white );
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
