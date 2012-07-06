package jp.ohwada.android.calllogcalls;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * show Call Log list
 */
public class MainActivity extends ListActivity
	implements OnItemClickListener {  

	// constant
	private final static int LIMIT = 50;	
	private final static String LF = "\n";	
	
	// class object
	private CallLogCalls mCalls;
   	private CallLogAdapter mAdapter;

	// view conponent
	private ListView mListView;

	// variable
	private ArrayList<CallLogRecord> mList;

	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 * @return void	 
	 */		   		                        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// helper
		mCalls = new CallLogCalls();

		// apapter
		mList = new ArrayList<CallLogRecord>();
		mAdapter = new CallLogAdapter( this, R.layout.call_row, mList );

        // list view
		View headerView = getLayoutInflater().inflate( R.layout.call_header, null );
		mListView = getListView();
		mListView.addHeaderView( headerView );
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );
		
		// get records (Read)	
		mList.clear();
		List<CallLogRecord> list = mCalls.getRecordList( this, LIMIT );
		if ( list != null ) {
        	mList.addAll( list );
        }
        mAdapter.notifyDataSetChanged();	
    }
    
    	/** === onItemClick ===
	 * @param AdapterView parent
	 * @param View view
	 * @param int position
	 * @param long id
	 * @return void	 
	 */
	@Override
	public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
		// header
		if ( position == 0 ) {
			// noting to do

		// footer
		} else if (id == -1 ) {
			// noting to do

		// item
		} else {
			clickItem( position );
		}
	}

	/**
	 * when click Item, start update activity
	 * @param int position
	 * @return void	 
	 */	
	private void clickItem( int position ) {
	
		// check position
		int n = position - 1;
		if (( n < 0 )||( n >= mList.size() )) {
		   	return;
		}

		// get record (Read)	
		CallLogRecord record = mList.get( n );
   		if ( record == null ) {
   			return;
   		}

		showDialog( record );			
	}
	
	/**
	 * show dialog
	 * @param CallLogRecord record
	 * @return void	 
	 */	
	private void showDialog( CallLogRecord record ) {
		String title = "Call Log";
		String msg = "";
		msg += "Date : " + record.getDateString() + " (" + record.date + ")" + LF;
		msg += "Number : " + record.number + LF;
		msg += "Type : " + record.getTypeString() + " (" + record.type+ ")" + LF;
		msg += "Duration : " + record.duration + LF;
		msg += "New : " + record.getNewString() + " (" + record.call_new+ ")" + LF;
		msg += "Cached Name : " + record.cachedName + LF;
		msg += "Cached Number Label : " + record.cachedNumberLabel + LF;
		msg += "Cached Number Type : " + record.getCachedNumberTypeString(this) + " (" + record.cachedNumberType+ ")" + LF;

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( title );
        builder.setMessage( msg );

        builder.setNegativeButton( "Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						// close
                    }
                });
                
        builder.setCancelable( true );
        builder.create().show();
    }
}
