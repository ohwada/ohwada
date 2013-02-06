package jp.ohwada.android.yag1;

import java.util.ArrayList;
import java.util.List;

import jp.ohwada.android.yag1.task.EventListPlaceTask;
import jp.ohwada.android.yag1.task.EventRecord;
import jp.ohwada.android.yag1.task.PlaceList;
import jp.ohwada.android.yag1.task.PlaceListFile;
import jp.ohwada.android.yag1.task.PlaceRecord;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Place Activity
 */
public class PlaceActivity extends ListActivity 
	implements OnItemClickListener {  
   
	// object
   	private EventAdapter mAdapter;
   	private PlaceListFile mPlaceFile;
	private EventListPlaceTask mEventTask;
    private MenuView mMenuView;
    private ErrorView mErrorView;
	           					   	
	// view conponent
	private TextView mTextViewLabel;
	private TextView mTextViewHeaderSub;
	private TextView mTextViewHeaderAddress;
	private TextView mTextViewHeaderTelephone;
	private TextView mTextViewHeaderCount;
	private Button mButtonHomepage;
	private Button mButtonWiki;
	private Button mButtonMap;
	private Button mButtonPhone;
	private ListView mListView;
	
	// variable
	private List<EventRecord> mListShow = null;
	private PlaceRecord mPlaceRecordParent = null;		
	private PlaceRecord mPlaceRecordOrig = null;	

	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
		View view = getLayoutInflater().inflate( R.layout.activity_place, null );
		setContentView( view ); 

		// view object
		mErrorView = new ErrorView( this, view );
		mMenuView = new MenuView( this, view );
		mMenuView.enableEvent();
		mMenuView.enablePlace();
		mMenuView.enableMap();
								
		// view conponent
		mTextViewLabel = (TextView) findViewById( R.id.place_textview_label );
//		mTextViewSub = (TextView) findViewById( R.id.place_textview_sub );
//		mTextViewAddress = (TextView) findViewById( R.id.place_textview_address );
//		mTextViewTelephone = (TextView) findViewById( R.id.place_textview_telephone );
//		mTextViewCount = (TextView) findViewById( R.id.place_textview_count );
				
		mButtonHomepage = (Button) findViewById( R.id.place_button_homepage );
		mButtonHomepage.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mPlaceRecordParent.homepage );
			}
		});

		mButtonWiki = (Button) findViewById( R.id.place_button_wiki );
		mButtonWiki.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mPlaceRecordParent.wiki );
			}
		});

		mButtonMap = (Button) findViewById( R.id.place_button_map );
		mButtonMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startMap( mPlaceRecordParent.url );
			}
		});

		mButtonPhone = (Button) findViewById( R.id.place_button_phone );
		mButtonPhone.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startDial( mPlaceRecordParent.telephone );
			}
		});
		
		// object
		mPlaceFile = new PlaceListFile();
		mEventTask = new EventListPlaceTask( this, msgHandler );
		
		// set list view			
		mListShow = new ArrayList<EventRecord>();
		mAdapter = new EventAdapter( this, R.layout.event_row, mListShow );
		mListView = getListView();
		
// NOTE: Call this before calling setAdapter. 
// This is so ListView can wrap the supplied cursor with one 
// that will also account for header and footer views.
		mListView.addHeaderView( getHeaderView(), null, false ); 
		
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );
		
		// get record
		Intent intent = getIntent();
		String url = intent.getStringExtra( Constant.EXTRA_PLACE_URL );
		if ( "".equals( url ) ) {
			mErrorView.showNotSpecifyPlace();
			return;
		}

		// get file	
		PlaceList list = mPlaceFile.read();
		mPlaceRecordOrig = list.getPlace( url );
		mPlaceRecordParent = list.getPlace( mPlaceRecordOrig.getParentUrl() );

		boolean ret = mEventTask.execute( mPlaceRecordParent ) ;
		if ( ret ) {
			List<EventRecord> list_event = mEventTask.getList() ;
			showPlace( mPlaceRecordParent, list_event );
		}						
	}

    /**
	 * getHeaderView
	 * @return LinearLayout
	 */     
	private LinearLayout getHeaderView() {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate( R.layout.place_header, null );
        mTextViewHeaderSub = (TextView) view.findViewById( R.id.place_header_textview_sub );
		mTextViewHeaderAddress = (TextView) view.findViewById( R.id.place_header_textview_address );
		mTextViewHeaderTelephone = (TextView) view.findViewById( R.id.place_header_textview_telephone );
		mTextViewHeaderCount = (TextView) view.findViewById( R.id.place_header_textview_count );
		return view;
	}
							
    /**
	 * showPlace
	 */     
	private void showPlace( PlaceRecord record, List<EventRecord> list ) {		
						
		// no data
		if ( record == null ) {
			mErrorView.showNoPlace();
			// hide button
			mButtonHomepage.setVisibility( View.INVISIBLE );
			mButtonWiki.setVisibility( View.INVISIBLE );
			mButtonMap.setVisibility( View.INVISIBLE );
			mButtonPhone.setVisibility( View.INVISIBLE );
			return;
		}

		mErrorView.hideText();	
		mTextViewLabel.setText( record.label );	
		
		// --- header ---
		mTextViewHeaderAddress.setText( record.address );
		mTextViewHeaderCount.setText( getCount( list ) );	
						
		// NOT show if same as orignal
		if ( record.url.equals( mPlaceRecordOrig.url ) ) {
			mTextViewHeaderSub.setVisibility( View.GONE );
		} else {
			// show original title if different
			mTextViewHeaderSub.setText( mPlaceRecordOrig.label );
		}

		// hide if empty
		if ( "".equals( record.telephone ) ) {
			mTextViewHeaderTelephone.setVisibility( View.GONE );
		} else {
			mTextViewHeaderTelephone.setText( record.telephone ) ;
		}

		// --- list ---			
		mListShow.clear();			
		if (( list != null )&&( list.size() > 0 )) {
			mListShow.addAll( list );
		}			
        mAdapter.notifyDataSetChanged();	
        
        // --- button ---
        // disable button if not have homepage 		
		if ( "".equals( record.homepage ) ) {
			mButtonHomepage.setEnabled( false );
		}
		
		// disable button if not have wiki 	
		if ( "".equals( record.wiki ) ) {
			mButtonWiki.setEnabled( false );
		}
		
    }

    /**
     * getCount
     * @param List<EventRecord> list
     * @return String
     */
	private String getCount( List<EventRecord> list ) {
		String count = "";			
		if (( list != null )&&( list.size() > 0 )) {
			count = getString( R.string.related_event ) ;
			count += " ( " + list.size() + " " ;
			count += getString( R.string.events ) + " )";

		} else {
			// no evnent list
			count = getString( R.string.error_no_event_data );
		}
		return count;
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
     * startMap
     * @param String url
     */
	private void startMap( String url ) {
		if (( url == null )|| url.equals("") ) return;
		Intent intent = new Intent( this, MapPlaceActivity.class );
		intent.putExtra( Constant.EXTRA_PLACE_URL, url );
		startActivityForResult( intent, Constant.REQUEST_MAP );
	}

    /**
     * startEvent
     * @param String url
     */
	private void startBrawser( String url ) {
		if (( url == null )|| url.equals("") ) return;
		Uri uri = Uri.parse( url );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		startActivity( intent );
	}

    /**
	 * startDial
	 * @param String url 
	 */ 
	private void startDial( String phone ) {
		if (( phone == null )|| "".equals( phone ) ) return;
		Uri uri = Uri.parse( "tel:" + phone );		
		Intent intent = new Intent( Intent.ACTION_DIAL, uri );
		startActivity( intent );
	}
	
	/**
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
    	super.onResume();
        mMenuView.execResume();
	}
									
	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
		mEventTask.cancel( );
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
    	mMenuView.execActivityResult( request, result, data );
    }

// --- Message Handler ---
	/**
	 * Message Handler
	 * The Handler that gets information back from the BluetoothChatService
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
    	switch ( msg.what ) {      
            case Constant.MSG_WHAT_TASK_EVENT_LIST_PLACE:
            	List<EventRecord> list = mEventTask.getList();
				showPlace( mPlaceRecordParent, list );
                break;   
        }
	}

}