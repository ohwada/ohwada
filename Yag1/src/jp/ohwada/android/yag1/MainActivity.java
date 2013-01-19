package jp.ohwada.android.yag1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.task.CommonFile;
import jp.ohwada.android.yag1.task.DateUtility;
import jp.ohwada.android.yag1.task.EventListTask;
import jp.ohwada.android.yag1.task.EventRecord;
import jp.ohwada.android.yag1.task.PlaceListTask;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Main Activity
 */
public class MainActivity extends ListActivity 
	implements OnItemClickListener {  
  
	// object
   	private EventListTask mEventTask;
   	private PlaceListTask mPlaceTask;
	private EventAdapter mAdapter;
   	private CommonFile mFile;
   	private DateUtility mDateUtility;
    private LoadingView mLoadingView;
    private ErrorView mErrorView;
        		   	   	   	   					   	
	// view conponent
	private Button mButtonMenuPlace;
	private Button mButtonMenuMap;
	private Button mButtonDate;
	
	private TextView mTextViewTitle;
	private TextView mTextViewDate;
	private TextView mTextViewCount;
	private ListView mListView;
		
	// variable
	private List<EventRecord> mListShow = null;
	private List<EventRecord> mListEvent = null;		
	private int mResultCode = 0;
	private boolean isEventFinish = false;
	private boolean isPlaceFinish = false;  
	private Date mDateStart = null;
			
	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater().inflate( R.layout.activity_main, null );
		setContentView( view ); 

		// view object
		mLoadingView = new LoadingView( view );
		mErrorView = new ErrorView( this, view );
		mErrorView.setHandler( msgHandler );
				
		// view conponent
		mTextViewTitle = (TextView) findViewById( R.id.textview_title );
		mTextViewDate = (TextView) findViewById( R.id.textview_date );
		mTextViewCount = (TextView) findViewById( R.id.textview_count );
		
		mButtonMenuPlace = (Button) findViewById( R.id.button_menu_place );
		mButtonMenuPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startMenuPlace();
			}
		});
		
		mButtonMenuMap = (Button) findViewById( R.id.button_menu_map );
		mButtonMenuMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startMenuMap();
			}
		});

		mButtonDate = (Button) findViewById( R.id.button_date );
		mButtonDate.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				selectDate();
			}
		});
					
		// object
		mEventTask = new EventListTask( msgHandler );
		mPlaceTask = new PlaceListTask( msgHandler );
		mDateUtility = new DateUtility();
		mFile = new CommonFile();
		mFile.init();
 
		// set list view			
		mListShow = new ArrayList<EventRecord>();
		mAdapter = new EventAdapter( this, R.layout.event_row, mListShow );
		mListView = getListView();
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );

		// get data
		isEventFinish = true;
		isPlaceFinish = true;
		execDateTask( mDateUtility.getDateToday() );
	}

    /**
	 * execDateTask
	 * @param Date date
	 */  						
	private void execDateTask( Date date ) {
		mDateStart = date;
		// show waiting
		mLoadingView.show();	
		// hide view
		mButtonMenuPlace.setVisibility( View.INVISIBLE );
		mButtonMenuMap.setVisibility( View.INVISIBLE );
		mButtonDate.setVisibility( View.INVISIBLE );
		mTextViewTitle.setText( "" );
		mTextViewDate.setText( "" );
		mTextViewCount.setText( "" );
		mListShow.clear();
		mErrorView.hideText();	
			
		// get events & places
		isEventFinish = mEventTask.execute( date );
		isPlaceFinish = mPlaceTask.execute();

		if ( isEventFinish ) {
			mListEvent = mEventTask.getList();
			if ( isPlaceFinish ) {
				showEventList();
			}
		}
    }
	
    /**
	 * showEvent
	 */     
	private void showEventList() {	
		// hide ProgressBar
		mLoadingView.hideProgressBar();
		mLoadingView.hideImage();		
		// show buton
		mButtonMenuPlace.setVisibility( View.VISIBLE );
		mButtonMenuMap.setVisibility( View.VISIBLE );		
		mButtonDate.setVisibility( View.VISIBLE );
				
		// title
		String title = getString( R.string.event_list ) ;
		mTextViewTitle.setText( title );
		String date = mDateUtility.formatDate( mDateStart );
		mTextViewDate.setText( date );
		
		// no data
		if ( !mPlaceTask.existsFile() ) {
			mErrorView.showRetryNotGetPlace();
			return;
		}
				
		// no data
		if ( mListEvent	== null ) {
			mErrorView.showRetryNotGetEvent();
			return;
		}
			
		// show count
		mErrorView.hideText();	
		String count = "( " + mListEvent.size() + " " + getString( R.string.events ) + " )";
		mTextViewCount.setText( count );
		
		// set events 		
		mListShow.clear();
		mListShow.addAll( mListEvent );
        mAdapter.notifyDataSetChanged();	
	}

    /**
     * startMenuPlace
     */
	private void startMenuPlace() {
		Intent intent = new Intent( this, PlaceListActivity.class );
		startActivityForResult( intent, Constant.REQUEST_MENU_PLACE );
	}
	
    /**
     * startMenuMap
     */
	private void startMenuMap() {
		Intent intent = new Intent( this, MapListActivity.class );
		startActivityForResult( intent, Constant.REQUEST_MENU_MAP );
	}

    /**
     * startEvent
     * @param String url
     */
	private void startEvent( String url ) {
		if (( url == null )|| url.equals("") ) return;
		Intent intent = new Intent( this, EventActivity.class );
		intent.putExtra( Constant.EXTRA_EVENT_URL, url );
		startActivityForResult( intent, Constant.REQUEST_EVENT );
	}

    /**
     * selectDate
     */
	private void selectDate() {
        Calendar calendar = Calendar.getInstance();
       	int cal_year = calendar.get( Calendar.YEAR );
        int cal_month = calendar.get( Calendar.MONTH );
        int cal_day = calendar.get( Calendar.DAY_OF_MONTH );
		// DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet( DatePicker view, int year, int month, int day ) {
					Date date = mDateUtility.createDate( year, month, day );
					execDateTask( date );
                }
            },
            cal_year,  cal_month,  cal_day );
        datePickerDialog.show();
	}			
											
	/**
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
        super.onResume();
        switch ( mResultCode ) {
        	case Constant.RESULT_MENU_PLACE:
        		mResultCode = 0;
        		startMenuPlace();
        		break;
			case Constant.RESULT_MENU_MAP:
			    mResultCode = 0;
        		startMenuMap();
        		break;	
        	case Constant.RESULT_MENU_EVENT:        		
			default:
				break;          		
		}
	}

	/**
	 * === onPause ===
	 */
    @Override
    public void onPause() {
        super.onPause();
        mEventTask.cancel();
        mPlaceTask.cancel();
	}
						
	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventTask.cancel();
        mPlaceTask.cancel();
	}

	/**
	 * === onCreateOptionsMenu ===
	 */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
		/* Initial of menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.activity_main, menu );
        return super.onCreateOptionsMenu( menu );
    }
 
 	/**
	 * === onOptionsItemSelected ===
	 */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case R.id.menu_about:
				AboutDialog dialog = new AboutDialog( this );
				dialog.show();
				return true;
			case R.id.menu_clear:
				mFile.clearCache();
				return true;
        }
        return super.onOptionsItemSelected( item );
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
		if (( position < 0 )||( position >= mListShow.size() )) return;
		EventRecord record = mListShow.get( position );
   		if ( record == null ) return;
		startEvent( record.event_url );
	}

	/**
	 * === onActivityResult ===
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
	@Override
    public void onActivityResult( int request, int result, Intent data ) {
        mResultCode = 0;
		if ( result == RESULT_OK ) {
			mResultCode = data.getIntExtra( Constant.EXTRA_CODE, 0 );
		}
    }

// --- Message Handler ---
	/**
	 * Message Handler
	 */
    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
			execHandler( msg );
        }
    };

	/**
	 * Message Handler ( handle message )
	 * @param Message msg
	 */
	private void execHandler( Message msg ) {
		boolean is_retry =false;
    	switch ( msg.what ) {
            case Constant.MSG_WHAT_TASK_EVENT_LIST:
            	mListEvent = mEventTask.getList();
            	isEventFinish = true;
                break;
            case Constant.MSG_WHAT_TASK_PLACE_LIST:
            	isPlaceFinish = true;
                break;                
            case Constant.MSG_WHAT_ERROR_RETRY:
				is_retry = true;
                break;  
        }
        if ( is_retry ) {
			execDateTask( mDateStart ); 
		} else if ( isEventFinish && isPlaceFinish ) {
			showEventList();
		}
	}

}