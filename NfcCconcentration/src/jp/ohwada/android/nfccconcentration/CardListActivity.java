package jp.ohwada.android.nfccconcentration;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * Create Read Update Delete
 */
public class CardListActivity extends ListActivity
	implements OnItemClickListener {  

	// constant
	private final static String BUNDLE_EXTRA_ID  = Constant.BUNDLE_EXTRA_ID;
	private final static int REQUEST_CODE_CREATE = Constant.REQUEST_CODE_CREATE;
	private final static int REQUEST_CODE_UPDATE = Constant.REQUEST_CODE_UPDATE;
	private final static int LIMIT = 50;	

	// class object
	private CardHelper mHelper;
   	private CardAdapter mAdapter;

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

		// create button
		Button btnCreate = (Button) findViewById( R.id.button_create );
		btnCreate.setOnClickListener(new OnClickListener() {
	 		@Override
			public void onClick( View v ) {
				startCreateActivity();
			}
		});

		// back button
		Button btnBack = (Button) findViewById( R.id.button_back );
		btnBack.setOnClickListener(new OnClickListener() {
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

		// get records (Read)	
		mList.clear();
		List<CardRecord> list = mHelper.getRecordList( LIMIT );
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
		CardRecord record = mList.get( n );
   		if ( record == null ) {
   			return;
   		}

		startUpdateActivity( record.id );				
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
		
}
