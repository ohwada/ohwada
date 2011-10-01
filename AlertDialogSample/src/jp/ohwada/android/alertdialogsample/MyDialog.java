//================================================
// 2011-09-25 K.OHWADA
// base android.content.DialogInterface android.app.Dialog
//================================================

package jp.ohwada.android.alertdialogsample;

import java.lang.ref.WeakReference;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyDialog {

	private final static String TAG = "MyDialog";
    private static final int SHOW = 0x45;

	private Dialog mDialog;
    private Handler mListenersHandler; 
    private Message mShowMessage;
    
    MyDialog( Dialog dialog ) {
        mDialog = dialog;
        mListenersHandler = new ListenersHandler( dialog );
    }

    public void setOnShowListener( OnShowListener listener ) {        
       if (listener != null) {
            mShowMessage = mListenersHandler.obtainMessage( SHOW, listener );
        } else {
            mShowMessage = null;
        }
    }

    public void show() {
    	mDialog.show();
    	sendShowMessage();
    }

    interface OnShowListener {
        public void onShow(DialogInterface dialog);
    }

    private void sendShowMessage() {
        if (mShowMessage != null) {
            Message.obtain(mShowMessage).sendToTarget();
        }
    }
    
    private class ListenersHandler extends Handler {
        private WeakReference<DialogInterface> mDialog1;

        public ListenersHandler(Dialog dialog) {
            mDialog1 = new WeakReference<DialogInterface>(dialog);
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                	Log.d(TAG, "Show");
                    ((OnShowListener) msg.obj).onShow( mDialog1.get() );
                    break;
            }
        }
    }
}
