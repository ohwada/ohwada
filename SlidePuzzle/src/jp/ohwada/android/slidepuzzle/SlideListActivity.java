package jp.ohwada.android.slidepuzzle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SlideListActivity extends ListActivity
	implements OnClickListener, OnItemClickListener  
{
	// tag
	private final static String TAG = "SlideListActivity ";
	
	private final static int LIMIT = 50;
	private final static int NONE_LIMIT = 0;
	
	// class object
   	private SlideAdapter adapter = null;
	private SlideHelper helper = null;
	private SlideInput input = null;
	
	// View
	private ListView list_view;
	private Button button_start;
	private Button button_mail;
	private EditText edit_start;
	
	// score iist
	private int game_start = 0;
	ArrayList<SlideScore> score_list = null;

	// alert
	private SlideScore a_score = null;
	private String a_message = "";
	
   	@Override
    public void onCreate( Bundle savedInstanceState ) 
	{
        super.onCreate( savedInstanceState );
        setContentView( R.layout.score_list );

		input = new SlideInput();
		
        // apapter
        score_list = new ArrayList<SlideScore>();
		adapter = new SlideAdapter( this, R.layout.score_row, score_list );

        // list view
        View header_view = getLayoutInflater().inflate(R.layout.score_header, null);
        list_view = getListView();
        list_view.addHeaderView( header_view );
		list_view.setAdapter( adapter );
        list_view.setOnItemClickListener( this );
        
        // View
        edit_start = (EditText) findViewById(R.id.header_edit_start);
		button_start = (Button) findViewById(R.id.header_button_start);
		button_start.setOnClickListener(this);
		button_mail = (Button) findViewById(R.id.header_button_mail);
		button_mail.setOnClickListener(this);
				
        // database
        helper = new SlideHelper( this );
    	
    	// show
    	show_score( SlideInput.MIN_GAME );
    }
	
	private void show_score( int start )
	{
		game_start = start;
		log_d( "start " + start );
	
        // get score list
		score_list.clear();
        score_list.addAll( helper.getAll( start, LIMIT ) );
        
        // notify adapter
        adapter.notifyDataSetChanged();				    
	}

    // === onClick ===
    @Override
	public void onClick(View v)
	 {
	   	switch( v.getId() )
	   	{
	   	 	case R.id.header_button_start:
				click_start();
   				break;
   				
   			case R.id.header_button_mail:
				click_mail();
   				break;
   		}
	}

    // === onItemClick ===
	@Override
	public void onItemClick( AdapterView<?> parent, View view, int position, long id ) 
	{
		// header
		if ( position == 0 ) {
			// noting to do

		// footer
		} else if (id == -1 ) {
			// noting to do

		//
		} else {
			click_item( position );
		}
	}

	// click		
	private void click_start()
	{
		boolean check = input.checkEditGame( edit_start );
		if ( ! check ) {
			toast( input.getErrorMsg() );
			return;
		}
					
		show_score( input.getGame() );	
	}
   
   	private void click_mail()
	{ 
		List<SlideScore> score_list = 
			helper.getAll( SlideInput.MIN_GAME, NONE_LIMIT );
		String text = get_all_score_cvs( score_list );
		send_mail( text );
	}

   	private void click_item(  int position )
	{
		int n = position - 1;
		if (( n < 0 )||( n >= score_list.size() )) {
		   	return;
		}
	
		SlideScore score = score_list.get( n );
   		if ( score == null ) {
   			return;
   		}

   		alert_dialog( score );
	}

   	private void click_alert_close( )
	{
		// do noting
		// close AlertDialog	
	}

   	private void click_alert_remove( SlideScore score  )
	{
		helper.delete( score );
		show_score( game_start );
		toast( "score removed" );
	}
   	
   	private void click_alert_send( String text  )
	{
		send_mail( text );
	}

	// score
	private String get_all_score_cvs( List<SlideScore> score_list )
	{
		if (( score_list == null )||( score_list.size() == 0 )) {
			return null;
		}
		
		String text = "Game,Time,Move,Result,Date\n";
		
		Iterator ite = score_list.iterator();				
        while( ite.hasNext() )
        { 
        	SlideScore s = (SlideScore) ite.next();
			if ( s != null ) {
				text += s.getCsv();
			}
        }
        
        return text;
	}

	private SlideScore get_score_by_id( int id )
	{
		Iterator ite = score_list.iterator();				
        while( ite.hasNext() )
        { 
        	SlideScore s = (SlideScore) ite.next();
			if (( s != null )&&( s.getIntId() == id )) {
				return s;
			}
        }
        
        return null;
    }
        
	// alert
	private void alert_dialog( SlideScore score )
	{
		a_score = score;
		
		String title = "Score";
		a_message = "Game " + score.getStringGame() + "\n" ;
		a_message += score.getStringDate() + "\n" ;
		a_message += score.getStringMove() + " moves \n" ;
		a_message += score.getStringTime() + " sec \n" ;
		a_message += score.getStringResult() + "\n" ;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( title );
        alertDialogBuilder.setMessage( a_message );

        alertDialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_close();
                    }
                });

        alertDialogBuilder.setNeutralButton("Remove",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_remove( a_score );
                    }
                });

        alertDialogBuilder.setNegativeButton("Send",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						click_alert_send( a_message );
                    }
                });

        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

	// mail
   	private void send_mail( String text )
	{ 
 		Intent intent = new Intent( Intent.ACTION_SEND );
 		intent.setType( "text/plain" ); 
		intent.putExtra(Intent.EXTRA_SUBJECT, "Slide Puzzle" );
		intent.putExtra(Intent.EXTRA_TEXT, text);
		
		startActivity(intent);
	}
	
	// others
    private void toast( String str ) 
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	} 
	
	private void log_d( String s ) 
    {
    	Log.d( TAG, s );  
	}   
}
