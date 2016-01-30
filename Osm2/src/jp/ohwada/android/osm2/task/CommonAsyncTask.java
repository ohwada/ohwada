package jp.ohwada.android.osm2.task;

import jp.ohwada.android.osm2.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;

/**
 * Common Async Task
 */
public class CommonAsyncTask extends AsyncTask<Void, Void, Boolean> {
	
    // class object    	
    private Context mContext;
	protected LgdHttpClient mClient;
	private ProgressDialog mDialog;
			
	// local variable
	protected String mResult = "";

	/**
	 * === constructor ===
	 */			 
    public CommonAsyncTask( Context context ) {
        super();
        mContext = context;
        mClient = new LgdHttpClient();
    }

	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
    	showDialog();
    	mResult = null;	
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

    protected void execBackground() {
    	// dummy
    }
    
	/**
	 * === onProgressUpdate ===
	 */	
    @Override
    protected void onProgressUpdate( Void... params ) {
		// dummy
    }

	/**
	 * === onPostExecute ===
	 */	 
    @Override
    protected void onPostExecute( Boolean result ) {
        hideDialog();
    }
	    		
	/**
	 * get Result
	 * @return String 
	 */  	
	public String getResult() {
		return mResult;
	}

	/**
	 * shutdown
	 */ 
    public void shutdown() {
    	mClient.shutdown();
    }

// --- dialog ---
	/**
	 * show Dialog
	 */
	protected void showDialog() {
		mDialog = new ProgressDialog( mContext );
		mDialog.setCancelable( false ); 

		mDialog.setOnCancelListener( new DialogInterface.OnCancelListener() {  
			public void onCancel( DialogInterface dialog ) {
				cancelTask();
      		}  
		});  
 
		mDialog.setOnKeyListener( new DialogInterface.OnKeyListener() {
			public boolean onKey( DialogInterface dialog, int id, KeyEvent key) {
				cancelTask();
				return true; 
			}  
		});  

		String msg = mContext.getResources().getString( R.string.dialog_loading );	
		mDialog.setMessage( msg );
		mDialog.show();
	}
		
	/**
	 * hide Progress Dialog
	 */
	protected void hideDialog() {
		if ( mDialog != null ) {
			mDialog.dismiss();
		}
	}

	/**
	 * cancel Task
	 */
	protected void cancelTask() {
		hideDialog();
		cancel( true );
	}
//---
    		   
}