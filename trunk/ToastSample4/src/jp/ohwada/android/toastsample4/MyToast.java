package jp.ohwada.android.toastsample4;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * MyToast
 */
public class MyToast {

    private static final String TAG = "MyToast";
    private static final boolean localLOGV = true;

    private static final int MESSAGE_TIMEOUT = 100;
    
    private final Context mContext;
    private final TN mTN;
 
    private Toast mToast;   
    private View mView;
    private long mDuration;  

    /**
     * Construct an empty Toast object. 
     * @param context  The context to use.  
     */
    public MyToast( Context context ) {
		mContext = context;
		mTN = new TN();
	}
	
	/**
     * Show the view for the specified duration.
     * @param Toast toast 
     * @param long delay
     */
    public void show() {
        if ( mToast == null ) {
            throw new RuntimeException("Toast must have been set");
        }  	
        if ( mToast.getView() == null ) {
            throw new RuntimeException("setView must have been called");
        }
        mTN.handleShow( mToast );
        Message m = Message.obtain( msgHandler, MESSAGE_TIMEOUT );
        msgHandler.sendMessageDelayed( m, mDuration );
    }

    /**
     * Close the view 
     */
    public void cancel() {
        mTN.handleHide();
    }

 	/**
     * Show a standard toast that just contains a text view.
     *
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message. (msec) 
     *
     */
    public static MyToast makeText( Context context, CharSequence text, long duration ) {
    	MyToast result = new MyToast( context );
    	result.mToast = Toast.makeText( context, text, Toast.LENGTH_SHORT );    
    	result.mDuration = duration;
    	return result;
    }

    /**
     * Show a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.   
     * @param resId    The resource id of the string resource to use.  
     * @param duration How long to display the message.  (msec)  
     *
     */
     public static MyToast makeText( Context context, int resId, long duration ) {
    	MyToast result = new MyToast( context );
    	result.mToast = Toast.makeText( context, resId, Toast.LENGTH_SHORT );    
    	result.mDuration = duration;
    	return result;
     }
        
	/**
	 * Message Handler
	 */
	private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
            switch (msg.what) {
                case MESSAGE_TIMEOUT:
                    cancel();
                    break;
            }
        }
    };

	/**
	 * class TN
	 */       
    private class TN  {

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        private WindowManager mWM;

		/**
		* Construct 
		*/        
        private TN() {
            // This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
        }

		/**
		* handleShow
     	* @param Toast toast 
		*/ 
        public void handleShow( Toast toast ) {
			View nextView = toast.getView();
        	if (localLOGV) Log.v(TAG, 
            	"HANDLE SHOW: " + this + " mView=" + mView + " mNextView=" + nextView);
            if (mView != nextView) {
                // remove the old view if necessary
                handleHide();
                mView = nextView;
                mWM = (WindowManager) mContext.getSystemService( Context.WINDOW_SERVICE );               
                final int gravity = toast.getGravity();;
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = toast.getXOffset();
                mParams.y = toast.getYOffset();
                mParams.verticalMargin = toast.getVerticalMargin() ;
                mParams.horizontalMargin = toast.getHorizontalMargin();
                if (mView.getParent() != null) {
                    if (localLOGV) Log.v(TAG, "REMOVE! " + mView + " in " + this);
                    mWM.removeView(mView);
                }
                if (localLOGV) Log.v(TAG, "ADD! " + mView + " in " + this);
                mWM.addView(mView, mParams);
            }
        }

		/**
		* handleHide
		*/ 
        public void handleHide() {
            if (localLOGV) Log.v(TAG, "HANDLE HIDE: " + this + " mView=" + mView);
            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
                    if (localLOGV) Log.v(TAG, "REMOVE! " + mView + " in " + this);
                    mWM.removeView(mView);
                }
                mView = null;
            }
        }
	}
}
