package jp.ohwada.android.calllogcalls;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * adapter for ListView
 */
public class CallLogAdapter extends ArrayAdapter<CallLogRecord> 
{
	// Layout Inflater
	private LayoutInflater mInflater = null;
			
    /**
     * === constractor ===
     * @param Context context
     * @param int textViewResourceId
     * @param List<CallLogRecord> objects     
	 * @return void	 
     */
	public CallLogAdapter( Context context, int textViewResourceId, List<CallLogRecord> objects ) {
		super( context, textViewResourceId, objects );
		mInflater = (LayoutInflater) super.getContext().getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE ) ;
	}

    /**
     * === get view ===
     * @param int position 
     * @param View convertView    
     * @param ViewGroup parent      
	 * @return View	 
     */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View view = convertView;
        CallLogHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = mInflater.inflate( R.layout.call_row, null );
			
			// save 
			h = new CallLogHolder(); 
			h.tv_date = (TextView) view.findViewById( R.id.row_textview_date ); 
			h.tv_number = (TextView) view.findViewById( R.id.row_textview_number );
			h.tv_type = (TextView) view.findViewById( R.id.row_textview_type );
			view.setTag( h ); 

		} else {
			// load  
			h = (CallLogHolder) view.getTag();  
		}  
     
		// get item form Adapter
		CallLogRecord item = (CallLogRecord) getItem( position );

		// set value
		h.tv_date.setText( item.getDateString() ) ;
		h.tv_number.setText( item.number ) ;
		h.tv_type.setText( item.getTypeString() );
		
		return view;
	}

	/**
	 * holder
	 */	
	static class CallLogHolder {
		public TextView tv_date;
		public TextView tv_number;
		public TextView tv_type;
	} 
}
