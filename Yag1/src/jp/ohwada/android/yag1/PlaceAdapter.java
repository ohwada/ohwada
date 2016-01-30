package jp.ohwada.android.yag1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jp.ohwada.android.yag1.task.PlaceRecord;

/**
 * adapter for Place ListView
 */
public class PlaceAdapter extends ArrayAdapter<PlaceRecord> 
{
	// Layout Inflater
	private LayoutInflater mInflater = null;
			
    /**
     * === constractor ===
     * @param Context context
     * @param int textViewResourceId
     * @param List<PlaceRecord> objects     
	 * @return void	 
     */
	public PlaceAdapter( Context context, int textViewResourceId, List<PlaceRecord> objects ) {
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
        PlaceHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = mInflater.inflate( R.layout.place_row, null );
			
			// save 
			h = new PlaceHolder(); 
			h.tv_label = (TextView) view.findViewById( R.id.place_row_textview_label ); 
			h.tv_address = (TextView) view.findViewById( R.id.place_row_textview_address );
			h.tv_telephone = (TextView) view.findViewById( R.id.place_row_textview_telephone );
			h.tv_event = (TextView) view.findViewById( R.id.place_row_textview_event );
			view.setTag( h ); 

		} else {
			// load  
			h = (PlaceHolder) view.getTag();  
		}  
     
		// get item form Adapter
		PlaceRecord r = (PlaceRecord) getItem( position );

		// set value
		h.tv_label.setText( r.label ) ;
		h.tv_address.setText( r.address ) ;

		// hide if empty
		if ( "".equals( r.telephone ) ) {
			h.tv_telephone.setVisibility( View.GONE );
		} else {
			h.tv_telephone.setText( r.telephone ) ;
		}

		// show mark if event flag		
		if ( r.event_flag ) {
			h.tv_event.setText( "* " ) ;
		} else {
			h.tv_event.setText( "" ) ;
		}
								
		return view;
	}

	/**
	 * holder
	 */	
	static class PlaceHolder { 
		public TextView tv_label;
		public TextView tv_address;
		public TextView tv_telephone;
		public TextView tv_event;
    } 
}
