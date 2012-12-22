package jp.ohwada.android.lodsample2;

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
public class EventAdapter extends ArrayAdapter<EventRecord> 
{
	// Layout Inflater
	private LayoutInflater mInflater = null;
			
    /**
     * === constractor ===
     * @param Context context
     * @param int textViewResourceId
     * @param List<EventRecord> objects     
	 * @return void	 
     */
	public EventAdapter( Context context, int textViewResourceId, List<EventRecord> objects ) {
		super( context, textViewResourceId, objects );
		mInflater = (LayoutInflater) super.getContext().getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE ) ;
	}

    /**
     * === get view ===
     * @param int position 
     * @param View convertView    
     * @param  ViewGroup parent      
	 * @return View	 
     */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View view = convertView;
        EventHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = mInflater.inflate( R.layout.event_row, null );
			
			// save 
			h = new EventHolder(); 
			h.tv_event = (TextView) view.findViewById( R.id.row_textview_event ); 
			h.tv_place = (TextView) view.findViewById( R.id.row_textview_place );
			h.tv_start = (TextView) view.findViewById( R.id.row_textview_start );
			h.tv_end = (TextView) view.findViewById( R.id.row_textview_end );
			h.tv_bar = (TextView) view.findViewById( R.id.row_textview_bar );
			view.setTag( h ); 

		} else {
			// load  
			h = (EventHolder) view.getTag();  
		}  
     
		// get item form Adapter
		EventRecord r = (EventRecord) getItem( position );

		// set value
		h.tv_event.setText( r.event_name ) ;
		h.tv_place.setText( r.place_name ) ;
		h.tv_start.setText( r.date_start );
		h.tv_end.setText( r.date_end );
		h.tv_bar.setText( r.date_bar );
				
		return view;
	}

	/**
	 * holder
	 */	
	static class EventHolder { 
		public TextView tv_event;
		public TextView tv_place;
		public TextView tv_start;
		public TextView tv_end;
		public TextView tv_bar;
    } 
}
