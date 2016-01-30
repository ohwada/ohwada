package jp.ohwada.android.pinqa1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ohwada.android.pinqa1.task.ArticleRecord;
import jp.ohwada.android.pinqa1.task.ArticleTask;
import jp.ohwada.android.pinqa1.task.ImageTask;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Article Activity
 */
public class ArticleActivity extends ListActivity
	implements OnItemClickListener {  

	private static final float DISPLAY_RATIO = 0.9f;
	private static final String REGEX_HTTP = 
		"https?:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+";
		   
	// object
   	private ArticleTask mArticleTask;
   	private ImageTask mImageTask;
   	private BitmapUtility mBitmapUtility ;
   	private HttpAdapter mAdapter;
   				           	   					   	
	// view conponent
	private TextView mTextViewArticle;
	private TextView mTextViewHeaderTopic;
	private TextView mTextViewHeaderDescription;
	private ImageView mImageViewHeaderArticle;
	private Button mButtonSource;
	private Button mButtonBack;
	private ListView mListView;
		
	// variable	
	private ArticleRecord mArticleRecord = null;		

	// variable
	private List<String> mListShow = null;		
	
	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater().inflate( R.layout.activity_article, null );
		setContentView( view ); 
								
		// view conponent
		mTextViewArticle = (TextView) findViewById( R.id.article_textview_article );

		mButtonSource = (Button) findViewById( R.id.article_button_source );
		mButtonSource.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mArticleRecord.source );
			}
		});

		mButtonBack = (Button) findViewById( R.id.article_button_back );
		mButtonBack.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				finish();
			}
		});
        
		// set list view			
		mListShow = new ArrayList<String>();
		mAdapter = new HttpAdapter( this, R.layout.http_row, mListShow );
		mListView = getListView();

// NOTE: Call this before calling setAdapter. 
// This is so ListView can wrap the supplied cursor with one 
// that will also account for header and footer views.
		mListView.addHeaderView( getHeaderView(), null, false ); 
		
		mListView.setAdapter( mAdapter );
		mListView.setOnItemClickListener( this );
								
		// object
		mArticleTask = new ArticleTask( this, msgHandler );
		mImageTask = new ImageTask( msgHandler );
		mBitmapUtility = new BitmapUtility( this );
				
		// get record
		Intent intent = getIntent();
		String url = intent.getStringExtra( Constant.EXTRA_ARTICLE_URL );
		if (( url == null )|| url.equals("") ) {
			mTextViewHeaderDescription.setText( R.string.error_not_specify_article );
			return;
		}
		
		// get file
		boolean ret = mArticleTask.execute( url );
		if ( ret ) {
			mArticleRecord = mArticleTask.getRecord();
			showArticle( mArticleRecord );			
		}
		
	}

    /**
	 * getHeaderView
	 * @return LinearLayout
	 */     
	private LinearLayout getHeaderView() {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout view = (LinearLayout) inflater.inflate( R.layout.http_header, null );
		mTextViewHeaderTopic = (TextView) view.findViewById( R.id.http_header_textview_topic );
		mTextViewHeaderDescription = (TextView) view.findViewById( R.id.http_header_textview_description );
		mImageViewHeaderArticle = (ImageView) view.findViewById( R.id.http_header_imageview_article );
		return view;
	}
			
    /**
	 * showArticle
	 * @param ArticleRecord r
	 */     
	private void showArticle( ArticleRecord r ) {
						
		// no data
		if ( r == null ) {
		    mTextViewHeaderDescription.setText( R.string.error_not_get_article );
			return;
		}

		// hide image
		mImageViewHeaderArticle.setVisibility( View.GONE );
											
		// show article
		mTextViewArticle.setText( r.article_label );
		mTextViewHeaderTopic.setText( r.topic_label );

		// show list
		showListView( r.description );
		
		// disable button if not have source 						
		if ( "".equals( r.source ) ) {
			mButtonSource.setEnabled( false );
		}
				
		// get image date if specify
		if ( !"".equals( r.image_url ) ) {
			boolean ret = mImageTask.execute( r );
			if ( ret ) {
				showImage( mImageTask.getFilePath() );			
			}
		}
																			
    }

    /**
	 * showListView
	 * @param String description
	 */  
	 private void showListView( String description ) {
		List<String> list = getListMatchedHttp( description );
		String description_new = getDescriptionWithoutHttp( list, description );
		mTextViewHeaderDescription.setText( description_new );
				
		// set list	
		if (( list != null )&&( list.size() > 0 )) {	
			mListShow.clear();
			mListShow.addAll( list );
        	mAdapter.notifyDataSetChanged();	
		}
	}

	/**
	 * getListMatchedHttp
	 * @param String description 
	 * @return List<String>
	 */ 	
	private List<String> getListMatchedHttp( String description ) {
	 	if (( description == null )|| "".equals( description ) ) return null;
	    List<String> list = new ArrayList<String>();
	    Map<String, Boolean> map = new HashMap<String, Boolean>(); 
		Pattern p = Pattern.compile( REGEX_HTTP );
		Matcher m = p.matcher( description );
		while( m.find() ) {
			String url = m.group();
			if ( !map.containsKey( url ) ) {
				list.add( url ) ;
			}
		}
		return list;
	}

	/**
	 * getDescriptionWithoutHttp
	 * @param List<String> list
	 * @param String description 
	 * @return String
	 */ 
	 private String getDescriptionWithoutHttp( List<String> list, String description ) {
		if (( list != null )&&( list.size() > 0 )) {		
			for ( int i=0; i<list.size(); i++ ) {
				description = description.replaceAll( list.get( i ), "" );
			}
		}
		return description;
	}
				
    /**
	 * showImage
	 * @param String path
	 */     
	private void showImage( String path ) {
		Bitmap bitmap = mBitmapUtility.getBitmap( path, DISPLAY_RATIO );
		if ( bitmap != null ) {
			mImageViewHeaderArticle.setVisibility( View.VISIBLE );
			mImageViewHeaderArticle.setImageBitmap( bitmap );
		}	
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
	 * === onResume ===
	 */
    @Override
    protected void onResume() {
        super.onResume();
	}
				
	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mArticleTask.cancel();
        mImageTask.cancel();
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
		// if click header
		if ( id == -1 ) return;
		int n = position - 1;
		if (( n < 0 )||( n >= mListShow.size() )) return;
		String str= mListShow.get( n );
   		if (( str == null )|| "".equals( str ) ) return;
		startBrawser( str );
	}

	/**
	 * === onActivityResult ===
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
	@Override
    public void onActivityResult( int request, int result, Intent data ) {
    	super.onActivityResult( request, result, data );
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
            case Constant.MSG_WHAT_TASK_ARTICLE:
            	mArticleRecord = mArticleTask.getRecord();
            	showArticle( mArticleRecord );
                break;  
            case Constant.MSG_WHAT_TASK_IMAGE:
            	showImage( mImageTask.getFilePath() );
                break;  
        }
	}

}