package jp.ohwada.android.osm2;

import jp.ohwada.android.osm2.task.NodeHash;
import jp.ohwada.android.osm2.task.NodeRecord;
import jp.ohwada.android.osm2.task.NodeTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Node Activity
 */
public class NodeActivity extends Activity {  
		   
	// object
   	private NodeTask mNodeTask;
   				           	   					   	
	// view conponent
	private TextView mTextViewNode;
	private TextView mTextViewDirect;
	private TextView mTextViewExtra;
	private TextView mTextViewContent;
	private Button mButtonSource;
	private Button mButtonPhone;
	private Button mButtonBack;

	private NodeRecord mNodeRecord;
	private String mSource = "";
	private String mPhone = "";
					
	/**
	 * === onCreate ===
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater().inflate( R.layout.activity_node, null );
		setContentView( view ); 

		// view conponent
		mTextViewNode = (TextView) findViewById( R.id.node_textview_node );
		mTextViewDirect = (TextView) findViewById( R.id.node_textview_direct );
		mTextViewExtra = (TextView) findViewById( R.id.node_textview_extra );
		mTextViewContent = (TextView) findViewById( R.id.node_textview_content );
				
		mButtonSource = (Button) findViewById( R.id.node_button_source );
		mButtonSource.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startBrawser( mSource );
			}
		});

		mButtonPhone = (Button) findViewById( R.id.node_button_phone );
		mButtonPhone.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startDial( mPhone );
			}
		});
		
		mButtonBack = (Button) findViewById( R.id.node_button_back );
		mButtonBack.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				finish();
			}
		});
										
		// task
		mNodeTask = new NodeTask( this, msgHandler );
				
		// get record
		Intent intent = getIntent();
		String url = intent.getStringExtra( Constant.EXTRA_NODE_URL );
		if (( url == null )|| url.equals("") ) {
			mTextViewDirect.setText( R.string.error_not_specify_node );
			return;
		}

		// get file
		boolean ret = mNodeTask.execute( url );
		if ( ret ) {
			showNode( mNodeTask.getHash() );			
		}
		
	}
			
    /**
	 * showNode
	 * @param NodeRecord r
	 */     
	private void showNode( NodeHash hash ) {						
		// no data
		if (( hash == null )||( hash.size() == 0 )||( hash.record == null )) {
		    mTextViewDirect.setText( R.string.error_not_get_node );
			return;
		}

		mNodeRecord = hash.record;
		mSource = mNodeRecord.node;				
		mTextViewNode.setText( mNodeRecord.getLabeleJa() );
		mTextViewDirect.setText( mNodeRecord.getDirectLabeleJa() );
		mTextViewContent.setText( hash.getContent() );	
		
		// TextView Extra
		String extra = mNodeRecord.getExtra( Constant.SPACE );
		if (( extra != null )&&( extra.length() >0 )) { 
			mTextViewExtra.setText( extra );
		} else {
			mTextViewExtra.setVisibility( View.GONE );
		}

		// Button Phone
		String phone = mNodeRecord.getPhone();	
		if (( phone != null )&&( phone.length() >0 )) { 
			mPhone = phone;
		} else {
			mButtonPhone.setEnabled( false );
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
	}
				
	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mNodeTask.cancel();
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
            case Constant.MSG_WHAT_TASK_NODE:
            	showNode( mNodeTask.getHash() );
                break;   
        }
	}
	
}