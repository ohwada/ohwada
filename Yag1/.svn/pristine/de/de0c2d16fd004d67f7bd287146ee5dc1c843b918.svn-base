package jp.ohwada.android.yag1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.ohwada.android.yag1.task.DateUtility;
import jp.ohwada.android.yag1.task.PlaceListEventFile;
import jp.ohwada.android.yag1.task.PlaceRecord;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Place List Activity
 */
public class PlaceListActivity extends ListActivity 
	implements OnItemClickListener {  
	   
	// object
   	private PlaceAdapter mAdapter;
    private MenuView mMenuView;
    private ErrorView mErrorView;
        	  	
	// view conponent
	private TextView mTextViewTitle;
	private TextView mTextViewCount;
	private ListView mListView;
	
	// variable
	private List<PlaceRecord> mListShow = null;		
	
	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater().inflate( R.layout.activity_place_list, null );
		setContentView( view ); 

		// view object
		mErrorView = new ErrorView( this, view );
		mMenuView = new MenuView( this, view );
		mMenuView.enableEvent();
		mMenuView.enableMap();
				
		// view conponent
		mTextViewTitle = (TextView) findViewById( R.id.place_textview_title );
		mTextViewCount = (TextView) findViewById( R.id.place_textview_count );
		
		// set list view			
		mListShow = new ArrayList<PlaceRecord>();
		mAdapter = new PlaceAdapter( this, R.layout.place_row, mListShow );
		mListView = getListView();
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );

		// show list
		showPlaceList( getPlaceList() );
	}

    /**
	 * getPlaceList
	 * @return List<PlaceRecord>
	 */ 
	private List<PlaceRecord> getPlaceList() {
		DateUtility utility = new DateUtility();  
		Date date = utility.getDateToday();
		PlaceListEventFile file = new PlaceListEventFile();
		return file.getListForPlace( date );
	}
	
    /**
	 * showPlace
	 * @return List<PlaceRecord> list
	 */     
	private void showPlaceList( List<PlaceRecord> list ) {
		// show title
		String title = getString( R.string.place_list );	
		mTextViewTitle.setText( title );
			
		// no data
		if ( list == null ) {
			mErrorView.showNoPlace();
			return;
		}

		// show count
		mErrorView.hideText();		
		String count = "( " + list.size() + " " + getString( R.string.places ) + " )";
		mTextViewCount.setText( count );
								
		// set list		
		mListShow.clear();
		mListShow.addAll( list );
        mAdapter.notifyDataSetChanged();	
	}

    /**
     * startPlace
     * @param String url
     */
	private void startPlace( String url ) {
		if (( url == null )|| url.equals("") ) return;
		Intent intent = new Intent( this, PlaceActivity.class );
		intent.putExtra( Constant.EXTRA_PLACE_URL, url );
		startActivityForResult( intent, Constant.REQUEST_PLACE );
	}

	/**
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
        super.onResume();
        mMenuView.execResume();
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
		PlaceRecord record = mListShow.get( position );
   		if ( record == null ) return;
   		startPlace( record.url );
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
				
}