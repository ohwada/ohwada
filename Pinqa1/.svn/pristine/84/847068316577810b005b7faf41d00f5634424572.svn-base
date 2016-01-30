package jp.ohwada.android.pinqa1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Common Dialog
 */
public class CommonDialog extends Dialog {
	
	// constant
	private final static float WIDTH_RATIO_FULL = 0.95f;
	protected final static float WIDTH_RATIO_HALF = 0.5f;
	private final static int MSG_ARG2 = 0;
	
	// object
	protected Activity mActivity;
	protected Handler msgHandler;
	
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public CommonDialog( Context context ) {
		super( context );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public CommonDialog( Context context, int theme ) {
		super( context, theme ); 
	}

	/**
	 * setHandler
	 * @param Handler handler
	 */ 
	public void setHandler( Handler handler ) {
		msgHandler = handler ;
	}

	/**
	 * createButtonClose
	 */ 
	protected void createButtonClose() {
		Button btnClose = (Button) findViewById( R.id.dialog_button_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});
	}

	/**
	 * setLayout
	 */ 
	protected void setLayoutFull() {
		setLayout( getWidthFull() );
	}

	/**
	 * setLayout
	 */ 
	protected void setLayoutHalf() {
		setLayout( getWidthHalf() );
	}
	
	/**
	 * setLayout
	 * @param  int width
	 */ 
	protected void setLayout( int width ) {
		getWindow().setLayout( width, ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	/**
	 * getWidth
	 * @return int
	 */ 
	protected int getWidthFull() {
		int width = (int)( getWindowWidth() * WIDTH_RATIO_FULL );
		return width;
	}	 

	/**
	 * getWidth
	 * @return int
	 */ 
	protected int getWidthHalf() {
		int width = (int)( getWindowWidth() * WIDTH_RATIO_HALF );
		return width;
	}

	/**
	 * getWindowWidth
	 * @return int
	 */ 
//	@SuppressWarnings("deprecation")
	private int getWindowWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		// Display#getWidth() This method was deprecated in API level 13
		return display.getWidth();
	}
		
	/**
	 * setGravity
	 */ 
	protected void setGravityTop() {
		// show on the top of screen. 
		getWindow().getAttributes().gravity = Gravity.TOP;
	}

	/**
	 * sendMessage
	 * @param int arg1
	 */	
    protected void sendMessage( int what, int arg1 ) {
    	Message msg = msgHandler.obtainMessage( what, arg1, MSG_ARG2 );	        
    	msgHandler.sendMessage( msg );
    }
		
}
