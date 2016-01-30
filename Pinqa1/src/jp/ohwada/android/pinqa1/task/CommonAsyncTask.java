package jp.ohwada.android.pinqa1.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;

/**
 * Common AsyncTask with ProgressDialog
 */
public class CommonAsyncTask extends AsyncTask<Void, Void, Boolean> {

	// object
	protected Context mContext;
	private ProgressDialog mProgressDialog = null;
	
	/**
	 * === constructor ===
	 */			 
    public CommonAsyncTask( Context context ) {
        super();
        mContext = context;
    }
	 
	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
		execPreExecute();	
    }

	/**
	 * execBackground
	 */  	
	protected void execPreExecute() {
		// dummy
	}
	
	/**
	 * === doInBackground ===
	 * @return Boolean
	 */	
    @Override
    protected Boolean doInBackground( Void... params ) {
        execBackground();
		return true;
    }

	/**
	 * execBackground
	 */  	
	protected void execBackground() {
		// dummy
	}
		
	/**
	 * === onProgressUpdate ===
	 */	
    @Override
    protected void onProgressUpdate( Void... params ) {
		execProgressUpdate();
    }

	/**
	 * execProgressUpdate
	 */  	
	protected void execProgressUpdate() {
		// dummy
	}
	
	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
		execPostExecute();
    }

	/**
	 * execProgressUpdate
	 */  	
	protected void execPostExecute() {
		// dummy
	}

	/**
	 * show Progress Dialog
	 */
	protected void showProgress( int res_id ) {
		mProgressDialog = new ProgressDialog( mContext );
		mProgressDialog.setCancelable( false ); 

		mProgressDialog.setOnCancelListener( new DialogInterface.OnCancelListener() {  
			public void onCancel( DialogInterface dialog ) {
				cancelTask();
      		}  
		});  
 
		mProgressDialog.setOnKeyListener( new DialogInterface.OnKeyListener() {
			public boolean onKey( DialogInterface dialog, int id, KeyEvent key) {
				cancelTask();
				return true; 
			}  
		});  

		String msg = mContext.getResources().getString( res_id );	
		mProgressDialog.setMessage( msg );
		mProgressDialog.show();
	}
		
	/**
	 * hide Progress Dialog
	 */
	protected void hideProgress() {
		if ( mProgressDialog != null ) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * cancel Task
	 */
	protected void cancelTask() {
		hideProgress();
		cancel( true );
	}
			    		   
}