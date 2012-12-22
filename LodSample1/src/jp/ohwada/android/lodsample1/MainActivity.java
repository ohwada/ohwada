package jp.ohwada.android.lodsample1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main Activity
 */
public class MainActivity extends Activity {

	private TextView mTextViewResult;

	/**
	 * onCreate
	 * @param Bundle savedInstanceState
	 */ 
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // view
        mTextViewResult = (TextView) findViewById( R.id.textview_result) ;
        
        // get button
        Button btnGet = (Button) findViewById( R.id.button_get );
        btnGet.setOnClickListener( new OnClickListener() {
            public void onClick( View v ) {  
                showEvents();
             }
        }); 
    }
    
    /**
	 * show events
	 */     
	private void showEvents() {
		String text = getEvents();
		mTextViewResult.setText( text );
	}
	
	/**
	 * get events
	 * @return String
	 */     
	private String getEvents() {
		String text = "";
		
		// today
		Calendar cal= Calendar.getInstance();
		Date date = cal.getTime();
		
		// get events
		EventHttpClient client = new EventHttpClient();
		String result = client.getEvents( date, date );
		if (( result == null )|| result.equals("") ) {
			text = "No result";
			return text;
		}						

		// parse events
		EventParser parser = new EventParser();
		ArrayList<EventRecord> list = parser.parse( result );
		if (( list == null )||( list.size() == 0 )) {	
			text = "No parse data";
			return text;
		}			

		// set events
		int size = list.size();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		String strDate = sdf.format( date );
		text = strDate + "\n";
		text += "events " + size + "\n\n";

		// each event
		for ( int i=0; i<size; i++) {
			EventRecord r = list.get( i );
			text += r.event_name + "\n";
			text += r.date_start + " " + r.date_bar + " " + r.date_end + "\n";
			text += r.place_name + "\n\n";
		}
		return text;
	}

}