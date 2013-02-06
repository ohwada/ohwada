package jp.ohwada.android.yag1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
	private final static float WIDTH_RATIO = 0.95f;
	private final static int MSG_ARG2 = 0;
	
	// object
	protected Context mContext;
	protected Handler msgHandler;
	protected Activity mActivity = null;
		
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public CommonDialog( Context context ) {
		super( context );
		mContext = context;
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public CommonDialog( Context context, int theme ) {
		super( context, theme ); 
		mContext = context;
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
	protected void setLayout() {
		int width = (int)( getWidth() * WIDTH_RATIO );
		getWindow().setLayout( width, ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	/**
	 * setLayout
	 */ 
	@SuppressWarnings("deprecation")
	private int getWidth() {
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
	 * setGravity
	 */ 
	protected void setGravityBottom() {
		// show on the lower of screen. 
		getWindow().getAttributes().gravity = Gravity.BOTTOM;
	}
	    	
	/**
	 * sendMessage
	 * @param int arg1
	 */	
    protected void sendMessage( int arg1 ) {
    	Message msg = msgHandler.obtainMessage( Constant.MSG_WHAT_DIALOG_MAP, arg1, MSG_ARG2 );	        
    	msgHandler.sendMessage( msg );
    }
    
    /**
	 * sendMessage
	 * @param int arg1
	 * @param Bundle bundle
	 */
	protected void sendMessage( int arg1, Bundle bundle ) { 	
		Message msg = msgHandler.obtainMessage( Constant.MSG_WHAT_DIALOG_MAP, arg1, MSG_ARG2 );
        msg.setData( bundle );	        
    	msgHandler.sendMessage( msg );
	}
		
}
