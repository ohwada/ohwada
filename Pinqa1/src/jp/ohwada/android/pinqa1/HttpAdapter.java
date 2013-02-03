package jp.ohwada.android.pinqa1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * adapter for Http ListView
 */
public class HttpAdapter extends ArrayAdapter<String> 
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
	public HttpAdapter( Context context, int textViewResourceId, List<String> objects ) {
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
        HttpHolder h = null;  
            
		// once at first
		if ( view == null ) {
			// get view form xml
			view = mInflater.inflate( R.layout.http_row, null );
			
			// save 
			h = new HttpHolder(); 
			h.tv_http = (TextView) view.findViewById( R.id.http_row_textview_http ); 
			view.setTag( h ); 

		} else {
			// load  
			h = (HttpHolder) view.getTag();  
		}  
     
		// get item form Adapter
		String str = (String) getItem( position );

		// set value
		h.tv_http.setText( str ) ;
								
		return view;
	}

	/**
	 * holder
	 */	
	static class HttpHolder { 
		public TextView tv_http;
    } 
}
