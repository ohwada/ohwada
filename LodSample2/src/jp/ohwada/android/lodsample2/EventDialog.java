package jp.ohwada.android.lodsample2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Event Dialog
 */
public class EventDialog extends Dialog {

	private Activity mActivity;
	
	private TextView mTextViewEvent;
	private TextView mTextViewPlace;
	private TextView mTextViewStart;
	private TextView mTextViewEnd;
	private TextView mTextViewBar;
	private TextView mTextViewAddress;
	private TextView mTextViewAbstract;
	private Button mButtonEvent;
	private Button mButtonPlace;
	private Button mButtonMap;
	private Button mButtonClose;

	private EventRecord mRecord;

    /**
     * === constractor ===
     * @param Context context
     */
	public EventDialog( Activity activity ) {
		super( activity );
		mActivity = activity;
		create();
	}

    /**
     * === constractor ===
     * @param Context context
	 * @param int theme
     */
	public EventDialog( Activity activity, int theme ) {
		super( activity, theme );
		mActivity = activity;
		create();
	}

    /**
     * create dialog
     */
	private void create() {
		setContentView( R.layout.event_dialog );
		mTextViewEvent = (TextView) findViewById( R.id.dialog_textview_event );
		mTextViewPlace = (TextView) findViewById( R.id.dialog_textview_place );
		mTextViewStart = (TextView) findViewById( R.id.dialog_textview_start );
		mTextViewEnd = (TextView) findViewById( R.id.dialog_textview_end );
		mTextViewBar = (TextView) findViewById( R.id.dialog_textview_bar );
		mTextViewAddress = (TextView) findViewById( R.id.dialog_textview_address );
		mTextViewAbstract = (TextView) findViewById( R.id.dialog_textview_abstract );

		mButtonEvent = (Button) findViewById( R.id.dialog_button_event );
		mButtonEvent.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startEvent();
			}
		});

		mButtonPlace = (Button) findViewById( R.id.dialog_button_place );
		mButtonPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startPlace();
			}
		});

		mButtonMap = (Button) findViewById( R.id.dialog_button_map );
		mButtonMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				startMap();
			}
		});
						
		mButtonClose = (Button) findViewById( R.id.dialog_button_close );
		mButtonClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				dismiss();
			}
		});
	}
		
    /**
     * startEvent
     */
	private void startEvent() {
		Uri uri = Uri.parse( mRecord.event_url );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		mActivity.startActivity( intent );
	}

    /**
     * startPlace
     */
	private void startPlace() {
		Uri uri = Uri.parse( mRecord.place_url );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		mActivity.startActivity( intent );
	}

    /**
     * startPlace
     */
	private void startMap() {
		String str = "geo:" + mRecord.place_lat + "," + mRecord.place_long + "?z=17";
		Uri uri = Uri.parse( str );
		Intent intent = new Intent( Intent.ACTION_VIEW, uri );
		mActivity.startActivity( intent );
	}

    /**
     * setLayout
	 * @param int width
	 * @param int height
     */
	public void setLayout( int width, int height ) {
		getWindow().setLayout( width, height );
	}

    /**
     * setRecord
	 * @param EventRecord record
     */			
	public void setRecord( EventRecord r ) {
		mRecord = r;
		mTextViewEvent.setText( r.event_name );
		mTextViewPlace.setText( r.place_name );
		mTextViewStart.setText( r.date_start );
		mTextViewEnd.setText( r.date_end );
		mTextViewBar.setText( r.date_bar );	
		mTextViewAddress.setText( r.address );		
		mTextViewAbstract.setText( r.event_abstract );					
	}
}
