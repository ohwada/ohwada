package jp.ohwada.android.yag1;

import jp.ohwada.android.yag1.task.EventRecord;
import jp.ohwada.android.yag1.task.EventTask;
import jp.ohwada.android.yag1.task.ImageTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Event Activity
 */
public class EventActivity extends Activity {
   
	// object
   	private EventTask mEventTask;
   	private ImageTask mImageTask;
    private MenuView mMenuView;
    private LoadingView mLoadingView;
    private ErrorView mErrorView;
	           	   					   	
	// view conponent
	private TextView mTextViewEvent;
	private TextView mTextViewPlace;
	private TextView mTextViewStart;
	private TextView mTextViewEnd;
	private TextView mTextViewBar;
	private TextView mTextViewCategory;
	private TextView mTextViewAbstract;
	private TextView mTextViewSchedule;
	private TextView mTextViewFee;
	private TextView mTextViewCare;
	private TextView mTextViewApinfo;
	private TextView mTextViewApstart;
	private TextView mTextViewApend;
	private TextView mTextViewStatus;
	private TextView mTextViewParticipant;
	private TextView mTextViewContact;
	private TextView mTextViewPhone;
	private ImageView mImageViewEvent;
	private Button mButtonPlace;
	private Button mButtonMap;
	private Button mButtonApinfo;
	private Button mButtonHomepage;
	private Button mButtonPhone;
			
	// variable	
	private EventRecord mEventRecord = null;		

	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater().inflate( R.layout.activity_event, null );
		setContentView( view ); 

		// view obect
		mLoadingView = new LoadingView( view );
		mErrorView = new ErrorView( this, view );
		mMenuView = new MenuView( this, view );
		mMenuView.enableEvent();
		mMenuView.enablePlace();
		mMenuView.enableMap();
		mMenuView.hideBottons() ;
								
		// view conponent
		mTextViewEvent = (TextView) findViewById( R.id.event_textview_event );
		mTextViewPlace = (TextView) findViewById( R.id.event_textview_place );
		mTextViewStart = (TextView) findViewById( R.id.event_textview_start );
		mTextViewEnd = (TextView) findViewById( R.id.event_textview_end );
		mTextViewBar = (TextView) findViewById( R.id.event_textview_bar );
		mTextViewCategory = (TextView) findViewById( R.id.event_textview_category );
		mTextViewAbstract = (TextView) findViewById( R.id.event_textview_abstract );
		mTextViewSchedule = (TextView) findViewById( R.id.event_textview_schedule );
		mTextViewFee = (TextView) findViewById( R.id.event_textview_fee );		
		mTextViewCare = (TextView) findViewById( R.id.event_textview_care );
		mTextViewApinfo = (TextView) findViewById( R.id.event_textview_apinfo );
		mTextViewApstart = (TextView) findViewById( R.id.event_textview_apstart );
		mTextViewApend = (TextView) findViewById( R.id.event_textview_apend );
		mTextViewStatus = (TextView) findViewById( R.id.event_textview_status );
		mTextViewParticipant = (TextView) findViewById( R.id.event_textview_participant );
		mTextViewContact = (TextView) findViewById( R.id.event_textview_contact );
		mTextViewPhone = (TextView) findViewById( R.id.event_textview_phone );
		mImageViewEvent = (ImageView) findViewById( R.id.event_imageview_event );

		mButtonApinfo = (Button) findViewById( R.id.event_button_apinfo );
		mButtonApinfo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mEventRecord.apinfo );
			}
		});
		
		mButtonPhone = (Button) findViewById( R.id.event_button_phone );
		mButtonPhone.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startDial( mEventRecord.phone );
			}
		});
		
		mButtonHomepage = (Button) findViewById( R.id.event_button_homepage );
		mButtonHomepage.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mEventRecord.homepage );
			}
		});
		
		mButtonPlace = (Button) findViewById( R.id.event_button_place );
		mButtonPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startPlace( mEventRecord.place_url );
			}
		});

		mButtonMap = (Button) findViewById( R.id.event_button_map );
		mButtonMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startMap( mEventRecord.place_url );
			}
		});

		// hide button
		mButtonPlace.setVisibility( View.INVISIBLE );
		mButtonMap.setVisibility( View.INVISIBLE );
		mButtonApinfo.setVisibility( View.INVISIBLE );			
		mButtonHomepage.setVisibility( View.INVISIBLE );
		mButtonPhone.setVisibility( View.INVISIBLE );
						
		// object
		mEventTask = new EventTask( msgHandler );
		mImageTask = new ImageTask( msgHandler );
		
		// get record
		Intent intent = getIntent();
		String url = intent.getStringExtra( Constant.EXTRA_EVENT_URL );
		if (( url == null )|| url.equals("") ) {
			mErrorView.showNotSpecifyEvent();
			return;
		}
		
		// get file
		boolean ret = mEventTask.execute( url );
		if ( ret ) {
			mEventRecord = mEventTask.getEvent();
			showEvent( mEventRecord );			
		}

	}
	
    /**
	 * showEvent
	 * @param EventRecord r
	 */     
	private void showEvent( EventRecord r ) {
		// hide ProgressBar
		mLoadingView.hideProgressBar();
		mLoadingView.hideImage();
		// show buton
		mMenuView.showBottons();
						
		// no data
		if ( r == null ) {
			mErrorView.showNotGetEvent();
			return;
		}

		// show buton
		mButtonPlace.setVisibility( View.VISIBLE );
		mButtonMap.setVisibility( View.VISIBLE );
		mButtonApinfo.setVisibility( View.VISIBLE );
		mButtonHomepage.setVisibility( View.VISIBLE );
		mButtonPhone.setVisibility( View.VISIBLE );

		// hide image
		mImageViewEvent.setVisibility( View.GONE );
											
		// show event
		mErrorView.hideText();
		mTextViewEvent.setText( r.event_label );
		mTextViewPlace.setText( r.place_label );
		mTextViewStart.setText( r.date_start );
		mTextViewEnd.setText( r.date_end );
		mTextViewBar.setText( r.date_bar );	
		setText( mTextViewCategory, 0, r.category );			
		setText( mTextViewAbstract, 0, r.event_abstract );			
		setText( mTextViewSchedule, R.string.event_schedule, r.schedule );	
		setText( mTextViewFee, R.string.event_fee, r.fee );	
		setText( mTextViewCare, R.string.event_care, r.care );	
		setText( mTextViewApstart, R.string.event_apstart, r.date_apstart );	
		setText( mTextViewApend, R.string.event_apend, r.date_apend );	
		setText( mTextViewStatus, R.string.event_status, r.status );	
		setText( mTextViewParticipant, R.string.event_participant, r.participant );	
		setText( mTextViewContact, R.string.event_contact, r.contact );	
		setText( mTextViewPhone, R.string.event_phone, r.phone  );	
		
		// if url format
		if ( matchUrl( r.apinfo ) ) {
			// hide apinfo
			mTextViewApinfo.setVisibility( View.GONE );
		} else {
			// disable button 
			mButtonApinfo.setEnabled( false );
			// show apinfo
			setText( mTextViewApinfo, R.string.event_apinfo, r.apinfo );			
		}
		
		// disable button if not have homepage 						
		if ( "".equals( r.homepage ) ) {
			mButtonHomepage.setEnabled( false );
		}

		// disable button if not have phone
		if ( "".equals( r.phone) ) {
			mButtonPhone.setEnabled( false );
		} 
		
		// get image date if specify
		if ( !"".equals( r.image_url ) ) {
			boolean ret = mImageTask.executeEventRecord( r );
			if ( ret ) {
				showImage();			
			}
		}
																			
    }

    /**
	 * showImage
	 */     
	private void showImage() {
		String path = mImageTask.getFilePath();
		if (( path == null )|| "".equals( path ) ) return;
		Bitmap bitmap = BitmapFactory.decodeFile( path );
		if ( bitmap != null ) {
			// adjust image size
			bitmap.setDensity( DisplayMetrics.DENSITY_MEDIUM );
			// show image
			mImageViewEvent.setVisibility( View.VISIBLE );
			mImageViewEvent.setImageBitmap( bitmap );
		}	
	}

    /**
	 * setText
	 * @param TextView view
	 * @param int id
	 * @param String str 	 
	 */   
	private void setText( TextView view, int id, String str ) {
		if ( "".equals( str ) ) {
			view.setVisibility( View.GONE );
		} else {
			view.setVisibility( View.VISIBLE );
			view.setText( getStringText( id, str ) );
		}
	}

    /**
	 * getStringText
	 * @param int id
	 * @param String str 	 
	 * @return String	
	 */  
	private String getStringText( int id, String str ) {
		String msg = str;
		if ( id > 0 ) {
			msg = getString( id ) + ": "+ str;
		}
		return msg;
	}

    /**
	 * matchUrl
	 * @param String url
	 * @return boolean	 
	 */  
	private boolean matchUrl( String url ) {
		if (( url == null )|| "".equals( url ) ) return false;
		if ( url.startsWith( "http://" ) ) return true;
		if ( url.startsWith( "https://" ) ) return true;	
		return false;
	}
							
    /**
     * startPlace
     * @param String url
     */
	private void startPlace( String url ) {
		if (( url == null )|| "".equals( url ) ) return;
		Intent intent = new Intent( this, PlaceActivity.class );
		intent.putExtra( Constant.EXTRA_PLACE_URL, url );
		startActivityForResult( intent, Constant.REQUEST_PLACE );
	}
	
    /**
     * startMap
     * @param String url
     */
	private void startMap( String url ) {
		if (( url == null )|| "".equals( url ) ) return;
		Intent intent = new Intent( this, MapPlaceActivity.class );
		intent.putExtra( Constant.EXTRA_PLACE_URL, url );
		startActivityForResult( intent, Constant.REQUEST_MAP );
	}

    /**
     * startBrawser
     * @param String url
     */
	private void startBrawser( String url ) {
		if ( !matchUrl( url ) ) return;
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
		mEventTask.cancel();
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
    	switch (msg.what) {
            case Constant.MSG_WHAT_TASK_EVENT:
            	mEventRecord = mEventTask.getEvent();
            	showEvent( mEventRecord );
                break;  
            case Constant.MSG_WHAT_TASK_IMAGE:
            	showImage();
                break;  
        }
	}

}