package jp.ohwada.android.yag1.task;

import java.util.Date;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;

/**
 * Common AsyncTask with ProgressDialog
 */
public class CommonAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private final static String FORMAT_DATE = "T00:00:00+09:00\"^^xsd:dateTime";

	// object
	protected YafjpHttpClient mClient;
	private DateUtility mDateUtility;
	private LoadingDialog mDialog;

	// local variable
	protected String mResult = "";
			
	/**
	 * === constructor ===
	 */			 
    public CommonAsyncTask() {
        super();
        mClient = new YafjpHttpClient();
        mDateUtility = new DateUtility();
    }
	 
	/**
	 * === onPreExecute ===
	 */	
    @Override
    protected void onPreExecute(){
		execPreExecute();
    }

	/**
	 * execPreExecute()
	 */	
    protected void execPreExecute(){
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
	 * execPostExecute
	 */	
    protected void execPostExecute() {
		// dummy
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
    
	/**
	 * show Dialog
	 */
	protected void showDialog( Context context ) {
		mDialog = new LoadingDialog( context );
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

    /**
	 * getFilterDate
	 * @param Date first
	 * @param Date last
	 * @return String
	 */  
	protected String getFilterDate( Date firstDate, Date lastDate ) {	
		String s_first = mDateUtility.formatDate( firstDate );
		String s_last = mDateUtility.formatDate( lastDate );		
		String first = "\"" + s_first + FORMAT_DATE; 
		String last = "\"" + s_last + FORMAT_DATE; 
		String query = "FILTER ((?dtstart > " + first + " && ";
		query += "?dtstart < " + last + " ) || ";
		query += "(?dtend > " + first + " && ";
		query += "?dtend < " + last + " ) || ";
		query += "(?dtstart < " + first + " && ";
		query += "?dtend > " + last + " )) ";
		return query;
	}
			    		   
}