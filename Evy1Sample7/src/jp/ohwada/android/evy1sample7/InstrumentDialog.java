package jp.ohwada.android.evy1sample7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.midi.MidiSoundConstants.InstGroup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

/**
 * Instrument Dialog
 */
public class InstrumentDialog extends Dialog {

	private final static String KEY_GROUP = "GROUP";
	private final static String KEY_CHILD = "CHILD";

	// callback
    private OnChangedListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnChangedListener {
        public void onClicked( int num );
    }

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public InstrumentDialog( Context context ) {
		super( context );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public InstrumentDialog( Context context, int theme ) {
		super( context, theme );
		create(); 
	}

	/**
	 * create
	 */ 
	private void create() {
		setContentView( R.layout.dialog_instrument );
		setTitle( R.string.instrument_select );

		Button btnClose = (Button) findViewById( R.id.Button_dialog_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});

	}

	/**
	 * initExpandableListView
	 * @param int num
	 */ 	
	public void initExpandableListView( int num ) {	
 		List<Map<String, Object>> parentsList = new ArrayList<Map<String, Object>>();
 		List<List<Map<String, Object>>> childrenList = new ArrayList<List<Map<String, Object>>>();
        int	 group = 0;
		// parents List
 		List<InstGroup> list = MidiSoundConstants.INST_GROUP_LIST;
        for( int i = 0; i < list.size(); i++ ) {
        	MidiSoundConstants.InstGroup inst = (InstGroup) list.get( i );
        	Map<String, Object> parentData = new HashMap<String, Object>();
        	parentData.put( KEY_GROUP, inst.name );
        	parentsList.add( parentData );
        	List<Map<String, Object>> childList = new ArrayList<Map<String,Object>>();
        	// child List
        	for( int j = inst.start; j <= inst.end; j++ ) {
        		String name = MidiSoundConstants.INSTRUMENT_NAMES[ j ];
				Map<String, Object> childData = new HashMap<String, Object>();
				childData.put( KEY_CHILD, name );
	        	childList.add( childData );
        	}
        	childrenList.add( childList );
        	if (( num >= inst.start )&&( num >= inst.end )) {
        		group = i;
        	}
        }
        
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
        		getContext(),
        		parentsList,
        		android.R.layout.simple_expandable_list_item_1,
        		new String[]{ KEY_GROUP },
        		new int[]{android.R.id.text1},
        		childrenList,
        		android.R.layout.simple_expandable_list_item_1,
        		new String[]{ KEY_CHILD },
        		new int[]{android.R.id.text1} );

     	ExpandableListView elv = (ExpandableListView) findViewById( R.id.ExpandableListView_dialog_inst );
        elv.setAdapter( adapter );
        elv.expandGroup( group );
        elv.setOnChildClickListener(
        		new ExpandableListView.OnChildClickListener() {
			public boolean onChildClick( 
				ExpandableListView parent, View v, int group, int child, long id ) {
				execChildClick( group, child );
				return true;
			}
		} );
	}

	/**
	 * execChildClick
	 * @param int group
	 * @param int child 
	 */ 
	private void execChildClick( int group, int child ) {
 		List<InstGroup> list = MidiSoundConstants.INST_GROUP_LIST;
        MidiSoundConstants.InstGroup inst = (InstGroup) list.get( group );
        int num = inst.start + child;
        notifyClicked( num ); 		
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
	 * @param int num
	 */
	private void notifyClicked( int num ) {
		if ( mListener != null ) {
			mListener.onClicked( num );
		}
	}
		
}
