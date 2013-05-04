package jp.ohwada.android.nfccconcentration;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Create Read Update Delete
 */
public class CardListActivity extends ListActivity
	implements OnItemClickListener {  

	// customize
	private int mModeAlready = Constant.MODE_ALREADY; 
	private boolean isUseButtonCreate = Constant.USE_BUTTON_CREATE;
	
	// constant
	private final static String BUNDLE_EXTRA_ID  = Constant.BUNDLE_EXTRA_ID;
	private final static int REQUEST_CODE_CREATE = Constant.REQUEST_CODE_CREATE;
	private final static int REQUEST_CODE_UPDATE = Constant.REQUEST_CODE_UPDATE;
	private final static int LIMIT = 50;	

	// class object
	private CardHelper mHelper;
   	private CardAdapter mAdapter;
	private ImageUtility mImageUtility;
    private NfcUtility mNfcUtility;
    	
	// view conponent
	private ListView mListView;

	// variable
	private ArrayList<CardRecord> mList;

	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState
	 * @return void	 
	 */		
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_list );

		// helper
		mHelper = new CardHelper( this );
		mImageUtility = new ImageUtility( this );	
		mNfcUtility = new NfcUtility( this, getClass() );	
		
		// create button
		Button btnCreate = (Button) findViewById( R.id.button_create );
		btnCreate.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				startCreateActivity();
			}
		});

		// back button
		Button btnBack = (Button) findViewById( R.id.button_back );
		btnBack.setOnClickListener( new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				finish();
			}
		});
			        	        
		// apapter
		mList = new ArrayList<CardRecord>();
		mAdapter = new CardAdapter( this, R.layout.card_row, mList );

        // list view
		View headerView = getLayoutInflater().inflate( R.layout.card_header, null );
		mListView = getListView();
		mListView.addHeaderView( headerView );
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );

// not use, for customize
		if ( ! isUseButtonCreate ) {		
			btnCreate.setVisibility( View.GONE );
		}	
	}

	/**
	 * === onResume ===
	 * re-draw list when return from activity
	 * @param none
	 * @return void	 
	 */
	@Override
	public void onResume() {
	    super.onResume();
		showList();
        mNfcUtility.enable();	    
	}

	/**
	 * === onPause ===
	 */
    @Override
    public void onPause() {
        super.onPause();
		if ( isFinishing() ) {
		 	mNfcUtility.disable();
		}
    } 

	/**
	 * showList 
	 */
	private void showList() {
		// get records (Read)	
		mList.clear();
		List<CardRecord> list = mHelper.getRecordList( LIMIT );
		if ( list != null ) {
        	mList.addAll( list );
        }
        mAdapter.notifyDataSetChanged();				    
	}

	/**
	 * === onNewIntent ===
	 * @param Intent intent
	 */
    @Override
    public void onNewIntent( Intent intent ) {
		String tag = mNfcUtility.intentToTagID( intent );
		if ( tag != null ) {
    		// return to setting 
			finish();
		}
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
		CardRecord record = mList.get( n );
   		if ( record == null ) {
   			return;
   		}

		switch ( mModeAlready ) {
			case Constant.MODE_ALREADY_UPDATE:
				startUpdateActivity( record.id );
				break;
			case Constant.MODE_ALREADY_DELETE:
			default:
				showDialogAleady( record );
				break;
		}				
	}

	/**
	 * start create activity
	 * @param none
	 * @return void	 
	 */
	private void startCreateActivity() {
    	Intent intent = new Intent( this, CreateActivity.class );
		startActivityForResult( intent, REQUEST_CODE_CREATE );
	}

	/**
	 * start update activity with id
	 * @param int id
	 * @return void	 
	 */
	private void startUpdateActivity( int id ) {
    	Intent intent = new Intent( this, UpdateActivity.class );
    	Bundle bandle = new Bundle();
        bandle.putInt( BUNDLE_EXTRA_ID, id );
        intent.putExtras( bandle );
		startActivityForResult( intent, REQUEST_CODE_UPDATE );
	}

	/**
	 * === onActivityResult ===
	 * noting to do, pass to onResume  
	 * @param int request
	 * @param int result
	 * @param Intent data
	 * @return void	 
	 */	    	    
    @Override
    protected void onActivityResult( int request, int result, Intent data ) {
        super.onActivityResult( request, result, data );     	
	}

	/**
	 * showDialogAleady
	 * @param CardRecord record 
	 */	
	private void showDialogAleady( CardRecord record  ) {
		final int id = record.id;
		String msg = getString( R.string.item_id ) + ": " + id + "<br>";
		msg += getString( R.string.item_tag ) + ": " + record.tag + "<br>";
		msg +=  getString( R.string.item_num ) + ": " + record.num + "<br>";
		msg +=  getString( R.string.item_set ) + ": " + record.set + "<br>";
		msg += "<br>";				
		Spanned spanned = mImageUtility.getHtmlImage( msg, record.num );

		AlertDialog dialog = new AlertDialog.Builder( this )
			.setTitle( R.string.msg_info )
			.setMessage( spanned )
			.setCancelable( true )		               
			.setNegativeButton( R.string.button_close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
						// close
                    }
                })           
			.setPositiveButton( R.string.button_delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
						deleteRecord( id );
						showList();
                    }
                })
			.create();
		dialog.show();
    }

	/**
	 * delete record 
	 * @param int id
	 */
    private void deleteRecord( int id ) {   
    	// delete from DB
        int ret = mHelper.delete( id ); 
        // message        
        if ( ret > 0 ) {
			toast_short( R.string.delete_success );       		
	     } else {	    	   
	    	toast_short( R.string.delete_fail );
	     }   	   
	}
    
	/**
	 * toast short
	 * @param int id
	 */ 
    private void toast_short( int id ) {
		ToastMaster.makeText( this, id, Toast.LENGTH_SHORT ).show();
	}
}
