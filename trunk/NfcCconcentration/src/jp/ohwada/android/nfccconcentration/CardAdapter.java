package jp.ohwada.android.nfccconcentration;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * adapter for ListView
 */
public class CardAdapter extends ArrayAdapter<CardRecord> 
{
	// Layout Inflater
	private LayoutInflater mInflater;
	private ImageUtility mUtility;
			
    /**
     * === constractor ===
     * @param Context context
     * @param int textViewResourceId
     * @param List<CardRecord> objects     
     */
	public CardAdapter( Context context, int textViewResourceId, List<CardRecord> objects ) {
		super( context, textViewResourceId, objects );
		mInflater = (LayoutInflater) context.getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE ) ;
		mUtility = new ImageUtility( context );
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
        CardHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = mInflater.inflate( R.layout.card_row, null );
			
			// save 
			h = new CardHolder(); 
			h.tv_id = (TextView) view.findViewById( R.id.row_textview_id ); 
			h.tv_tag = (TextView) view.findViewById( R.id.row_textview_tag );
			h.tv_num = (TextView) view.findViewById( R.id.row_textview_num );
			h.tv_set = (TextView) view.findViewById( R.id.row_textview_set );
			h.iv = (ImageView) view.findViewById( R.id.row_imageview );
			view.setTag( h ); 

		} else {
			// load  
			h = (CardHolder) view.getTag();  
		}  
     
		// get item form Adapter
		CardRecord item = (CardRecord) getItem( position );
		int num = item.num;
		// set value
		h.tv_id.setText( item.getIdString() ) ;
		h.tv_tag.setText( item.tag ) ;
		h.tv_num.setText( Integer.toString( num ) );
		h.tv_set.setText( item.getSetString() );
		if ( num > 0 ) {
			mUtility.showImageByNum( h.iv, num );	
		}		
		return view;
	}

	/**
	 * holder class
	 */	
	static class CardHolder { 
		public TextView tv_id;
		public TextView tv_tag;
		public TextView tv_num;
		public TextView tv_set;
		public ImageView iv;
    } 
}
