package jp.ohwada.android.evy1sample7;

import jp.ohwada.android.midi.MidiSoundConstants;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Percussion Dialog
 */
public class PercussionDialog extends Dialog {

	// debug
	private static final boolean D = true;
	private static final String TAG = "Evy1";
	protected String TAG_SUB = "PercussionDialog";

	private static final int PERC_KEY_MIN = MidiSoundConstants.PERC_KEY_MIN;
	private static final int PERC_KEY_MAX = MidiSoundConstants.PERC_KEY_MAX;

	// tag
	private static final int TAG_MIN = 0;
	private static final int TAG_MAX = 7;

	// 1000 msec
	private static final int DELAY_TIME = 1000;
	
	private Handler mHandler = new Handler();
	private boolean isFlag = false;
        			
	// callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onClicked( int parent, int key );
    }

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public PercussionDialog( Context context ) {
		super( context );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public PercussionDialog( Context context, int theme ) {
		super( context, theme );
		create(); 
	}

	/**
	 * create
	 */ 
	private void create() {
		setContentView( R.layout.dialog_percussion );
		setTitle( R.string.percussion_select );
 		isFlag = false;
	}

	/**
	 * initView
	 * @param int[] keys
	 */
    public void initView( int[] keys ) {
      	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
  			getContext(), R.layout.dialog_percussion_list_item );
  		adapter.setDropDownViewResource( 
  			android.R.layout.simple_spinner_dropdown_item );	
  		int len = MidiSoundConstants.PERCUSSION_NAMES.length;
        for ( int i = 0; i < len; i++ ) {
        	adapter.add( MidiSoundConstants.PERCUSSION_NAMES[ i ] );
        }
        
		initSpinner( R.id.Spinner_perc_0, adapter, keys[0] );
		initSpinner( R.id.Spinner_perc_1, adapter, keys[1] );
		initSpinner( R.id.Spinner_perc_2, adapter, keys[2] );
		initSpinner( R.id.Spinner_perc_3, adapter, keys[3] );
		initSpinner( R.id.Spinner_perc_4, adapter, keys[4] );
		initSpinner( R.id.Spinner_perc_5, adapter, keys[5] );
		initSpinner( R.id.Spinner_perc_6, adapter, keys[6] );
		initSpinner( R.id.Spinner_perc_7, adapter, keys[7] );

		Button btnClose = (Button) findViewById( R.id.Button_dialog_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});
		
		mHandler.postDelayed( mRunnable, DELAY_TIME ) ; 
    }

	/**
	 * initSpinner
	 * @param int id
	 * @param ArrayAdapter<String> adapter
	 * @param int key	 
	 */		
    private void initSpinner( int id, ArrayAdapter<String> adapter, int key ) {
    	if ( key < PERC_KEY_MIN )  {
    		key = PERC_KEY_MIN;
    	} else if ( key > PERC_KEY_MAX ) {
    	    key = PERC_KEY_MAX;
    	}
    	int sel = key - PERC_KEY_MIN;
		Spinner spinner = (Spinner) findViewById( id );
		spinner.setAdapter( adapter );
		spinner.setSelection( sel );
        spinner.setOnItemSelectedListener( 
        	new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( 
            	AdapterView<?> parent, View view, int position, long id ) {
            	execItemSelected( parent, position );
            }
            @Override
            public void onNothingSelected( AdapterView<?> arg0 ) {
            	// dummy
            }
        });
	}
	        
	/**
	 * execItemSelected
	 * @param AdapterView<?> parent
	 * @param int position
	 */ 
	private void execItemSelected( AdapterView<?> parent, int position ) {
		if ( !isFlag ) return;
		Spinner spinner = (Spinner) parent;
		int tag = Integer.parseInt( (String) spinner.getTag() );
		if ( tag < TAG_MIN ) {
			tag = TAG_MIN;
		} else if ( tag > TAG_MAX ) {
			tag = TAG_MAX;
		}
		int key = position + MidiSoundConstants.PERC_KEY_MIN;
        notifyClicked( tag, key); 		
	}

// --- OnChangedListener ---
     /**
     * setOnChangedListener
     * @param OnChangedListener listener
     */
    public void setOnChangedListener( OnChangedListener listener ) {
        mListener = listener;
    }

	/**
	 * notifyClicked
	 * @param int parent
	 * @param int key
	 */
	private void notifyClicked( int parent, int key ) {
		if ( mListener != null ) {
			mListener.onClicked( parent, key );
		}
	}

	/**
	 * --- Runnable ---
	 */
	private final Runnable mRunnable = new Runnable() {
    	@Override
    	public void run() {
        	isFlag = true;
    	}
	};

	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );	
	}
}
