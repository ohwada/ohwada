package jp.ohwada.android.yag1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

/**
 * ErrorView
 */
public class ErrorView  {  

	// Constructor
	private Context mContext;
	private Resources mResources;
	private TextView mTextViewError;
	private Handler msgHandler;
				    	
	/**
	 * === Constructor ===
	 * @param  Context context
	 * @param View view
	 */ 
    public ErrorView( Context context, View view ) {
    	mContext = context;
    	mResources = context.getResources();
		mTextViewError = (TextView) view.findViewById( R.id.textview_error );
	}

	/**
	 * setHandler
	 * @param Handler handler
	 */ 
	public void setHandler( Handler handler ) {
		msgHandler = handler;
	}

	/**
	 * showRetryNotGetPlace
	 */ 
	public void showRetryNotGetPlace() {
		String msg = getString( R.string.error_not_get_place );		
		setText( msg );
		showDialogRetry( msg );
	}

	/**
	 * showRetryNotGetEvent
	 */ 
	public void showRetryNotGetEvent() {
		String msg = getString( R.string.error_not_get_event );
		setText( msg );
		showDialogRetry( msg );
	}

	/**
	 * showNotGetPlace
	 */ 			
	public void showNotGetPlace() {
		String msg = getString( R.string.error_not_get_place );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * showNotGetEvent
	 */ 
	public void showNotGetEvent() {
		String msg = getString( R.string.error_not_get_event );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * showNoPlace
	 */ 
	public void showNoPlace() {
		String msg = getString( R.string.error_no_place_data );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * showNoEvent
	 */ 					 
	public void showNoEvent() {
		String msg = getString( R.string.error_no_event_data );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * showNotSpecifyEvent
	 */ 
	public void showNotSpecifyEvent() {
		String msg = getString( R.string.error_not_specify_event );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * showNotSpecifyPlace
	 */ 	
	public void showNotSpecifyPlace() {
		String msg = getString( R.string.error_not_specify_place );
		setText( msg );
		showDialogCaution( msg );
	}

	/**
	 * hideText
	 */ 
	public void hideText() {
		mTextViewError.setVisibility( View.GONE );
	}

	/**
	 * setText
	 * @param String msg
	 */ 		
	private void setText( String msg ) {
		mTextViewError.setVisibility( View.VISIBLE );
		mTextViewError.setText( msg );
	}

	/**
	 * showDialogCaution
	 * @param String msg
	 */
	private void showDialogRetry( String msg ) {
		showDialogCommon( msg, true );
	}

	/**
	 * showDialogCaution
	 * @param String msg
	 */
	private void showDialogCaution( String msg ) {
		showDialogCommon( msg, false );
	}
				
	/**
	 * showDialogCaution
	 * @param String msg
	 * @param boolean show_retry
	 */
	private void showDialogCommon( String msg, boolean show_retry ) {
		AlertDialog.Builder dialog = new AlertDialog.Builder( mContext );
		dialog.setTitle( R.string.dialog_error_caution );
    	dialog.setMessage( msg ); 
		dialog.setNegativeButton( R.string.dialog_button_close, new DialogInterface.OnClickListener() {
			@Override
      		public void onClick( DialogInterface dialog, int which ) {
      			// dummy
			}
    	});     

		if ( show_retry &&( msgHandler != null )) {  
    		dialog.setPositiveButton( R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
				@Override
      			public void onClick( DialogInterface dialog, int which ) {
      				// send message
    				Message msg = msgHandler.obtainMessage( Constant.MSG_WHAT_ERROR_RETRY );	        
    				msgHandler.sendMessage( msg );
      				// close dialog
					dialog.dismiss();
				}
    		}); 
    	}
    	           
    	dialog.show();
	}

	/**
	 * getString
	 * @param int id
	 * @return String
	 */
	private String getString( int id ) {
		return mResources.getString( id );
	}
}