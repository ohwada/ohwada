package jp.ohwada.android.lodsample2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main Activity
 */
public class MainActivity extends ListActivity 
	implements OnItemClickListener {  

	// handler
    private static final int MSG_WHAT = 1;
    private static final int INTERVAL = 1000;  // 1 sec

	// object
	private EventParser mParser;
   	private EventAdapter mAdapter;
   	private EventAsyncTask mAsync;
				   	
	// view conponent
	private TextView mTextViewTitle;
	private TextView mTextViewCount;
	private ProgressBar mProgressBarWait;
	private ImageView mImageViewWait;
	private TextView mTextViewWait;
	private ListView mListView;

	// variable
	private ArrayList<EventRecord> mList;

	// handler  
    private boolean isStart = false;
    private boolean isRunning = false;
    	
	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

		// view conponent
		mTextViewTitle = (TextView) findViewById( R.id.textview_title );
		mTextViewCount = (TextView) findViewById( R.id.textview_count );
		mProgressBarWait = (ProgressBar) findViewById( R.id.progressbar_wait );
		mImageViewWait = (ImageView) findViewById( R.id.imageview_wait );
		mTextViewWait = (TextView) findViewById( R.id.textview_wait );		
	
		// set list view						
		mParser = new EventParser();
		mList = new ArrayList<EventRecord>();
		mAdapter = new EventAdapter( this, R.layout.event_row, mList );
		mListView = getListView();
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );

		// today
		Calendar cal= Calendar.getInstance();
		Date dateToday = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		String today = sdf.format( dateToday );
		mTextViewTitle.setText( "Yokoham Art Navi : " + today );

		// start async task						
		mAsync = new EventAsyncTask();
		mAsync.setToday( dateToday );
		mAsync.execute();
		startHandler();
    }
	
    /**
	 * showEvent
	 */     
	private void showEvent() {
		// hide ProgressBar
		mProgressBarWait.setVisibility( View.GONE );
		mImageViewWait.setVisibility( View.GONE );
		mTextViewWait.setVisibility( View.GONE );

		// get events			
		String result = mAsync.getResult();
		if (( result == null )|| result.equals("") ) {
			Toast.makeText(this, "No result", Toast.LENGTH_LONG).show();
        	return;		
		}
				
		// parse events				
		ArrayList<EventRecord> list = mParser.parse( result );
		if (( list == null )||( list.size() == 0 )) {
        	Toast.makeText(this, "No parse data", Toast.LENGTH_LONG).show();
        	return;		
		}

		// show count
		mTextViewCount.setText( "Events : " + list.size() );
				
		// set events 		
		mList.clear();
		mList.addAll( list );
        mAdapter.notifyDataSetChanged();	
	}
	
	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
		mAsync.cancel( true );
		stopHandler(); 
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
		if (( position < 0 )||( position >= mList.size() )) return;
		EventRecord record = mList.get( position );
   		if ( record == null ) return;
		showDialog( record );
	}

    /**
	 * showDialog
	 * @param EventRecord record
	 */ 
	private void showDialog( EventRecord record ) {
		EventDialog dialog = new EventDialog( this );
		dialog.setLayout( getDialogWidth(), LayoutParams.WRAP_CONTENT );	
        dialog.setTitle( "Yokoham Art Navi" );       
		dialog.setRecord( record );
		dialog.show();
	}

    /**
	 * getDialogWidth
	 * @return int
	 */ 
	private int getDialogWidth() {
		WindowManager wm = (WindowManager) getSystemService( WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		return (int) ( display.getWidth() * 0.9 );
	}
		    
// -- handler ---		
	/**
	 * start Handler
	 */    
	 private void startHandler() {
		isStart = true; 
		updateRunning();
	}

	/**
	 * stop Handler
	 */ 
	 private void stopHandler() {	    
		isStart = false;
		updateRunning();
	}

	/**
	 * updateRunning 
	 */		
    private void updateRunning() {
        boolean running = isStart;
        if ( running != isRunning ) {
			// restart running    
            if ( running ) {
                mHandler.sendMessageDelayed( Message.obtain( mHandler, MSG_WHAT ), INTERVAL );              
             // stop running             
             } else {
                mHandler.removeMessages( MSG_WHAT );
            }
            isRunning = running;
        }
    }

	/**
	 * message handler class
	 */	    
    private Handler mHandler = new Handler() {
        public void handleMessage( Message m ) {
            if ( isRunning ) {
				updateStatus();
                sendMessageDelayed( Message.obtain( this, MSG_WHAT ), INTERVAL );
            }
        }
    };
    	
	/**
	 * update Status
	 */	
    private synchronized void updateStatus() { 
		if ( mAsync.getStatus() == AsyncTask.Status.FINISHED ) {
			stopHandler();
			showEvent();
		}
	}


}