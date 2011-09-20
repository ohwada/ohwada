package jp.ohwada.android.slidepuzzle;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for score list
 */
public class SlideAdapter extends ArrayAdapter<SlideScore> 
{
	// Layout Inflater
	private LayoutInflater inflater = null;
			
    /**
     * === constractor ===
     */
	public SlideAdapter( Context context, int textViewResourceId, List<SlideScore> objects )
	 {
		super( context, textViewResourceId, objects );
		inflater = (LayoutInflater) super.getContext().getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE ) ;
	}

    /**
     * === get view ===
     */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) 
	{
		View view = convertView;
        SlideHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = inflater.inflate( R.layout.score_row, null );
			
			// save 
			h = new SlideHolder(); 
			h.text_id = (TextView) view.findViewById( R.id.row_text_id ); 
			h.text_game = (TextView) view.findViewById( R.id.row_text_game );
			h.text_time = (TextView) view.findViewById( R.id.row_text_time );
			h.text_move = (TextView) view.findViewById( R.id.row_text_move );
			h.text_date   = (TextView) view.findViewById( R.id.row_text_date );
			view.setTag( h ); 

		} else {
			// load  
			h = (SlideHolder) view.getTag();  
		}  
     
		// get object form Adapter
		SlideScore item = (SlideScore) getItem( position );

		// set value
		h.text_game.setText( item.getStringId() ) ;
		h.text_game.setText( item.getStringGame() ) ;
		h.text_time.setText( item.getStringTime() );
		h.text_move.setText( item.getStringMove() ) ;
		h.text_date.setText( item.getStringDate() ) ;
		
		return view;
	}
	
	static class SlideHolder 
	{ 
		// view
		public TextView text_id;
		public TextView text_game;
		public TextView text_time;
		public TextView text_move;
		public TextView text_date;  
    } 
}
