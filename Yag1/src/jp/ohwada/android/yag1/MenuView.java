package jp.ohwada.android.yag1;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * MenuView
 */
public class MenuView {  

	// object 
 	private Activity mActivity;
 	  	
	// view conponent
	private Button mButtonMenuEvent;
	private Button mButtonMenuPlace;
	private Button mButtonMenuMap;
	
	// variable
	private boolean isEnableEvent = false;		
	private boolean isEnablePlace = false;	
	private boolean isEnableMap = false;	
	private int mResultCode = 0;
	
	/**
	 * === Constructor ===
	 * @param Activity activity
	 * @param View view
	 */ 
    public MenuView( Activity activity, View view  ) {
    	mActivity = activity;
		mButtonMenuEvent = (Button) view.findViewById( R.id.button_menu_event );
		mButtonMenuPlace = (Button) view.findViewById( R.id.button_menu_place );
		mButtonMenuMap = (Button) view.findViewById( R.id.button_menu_map );
	}

	/**
	 * enableEvent
	 */ 
    public void enableEvent() {
    	isEnableEvent = true;	
    	mButtonMenuEvent.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				finishEvent();
			}
		});
	}

	/**
	 * enablePlace
	 */ 
    public void enablePlace() {
    	isEnablePlace = true;	
    	mButtonMenuPlace.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				finishPlace();
			}
		});
	}

	/**
	 * enableMap
	 */ 	
	public void enableMap() {
    	isEnableMap = true;	
    	mButtonMenuMap.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				finishMap();
			}
		});
	}
					
    /**
     * finishEvent
     */
	private void finishEvent() {
	    if( !isEnableEvent ) return;
		setResultCode( Constant.RESULT_MENU_EVENT );
		mActivity.finish();
	}

    /**
     * finishPlace
     */
	private void finishPlace() {
		if( !isEnablePlace ) return;
		setResultCode( Constant.RESULT_MENU_PLACE );
		mActivity.finish();
	}
		
    /**
     * finishMap
     */
	private void finishMap() {
		if( !isEnableMap ) return;
		forcedFinishMap();
	}

    /**
     * finishMap
     */
	public void forcedFinishMap() {
		setResultCode( Constant.RESULT_MENU_MAP );
		mActivity.finish();
	}
	
    /**
     * setResultCode
     * @param int code 
     */
	private void setResultCode( int code ) {
		Intent data = new Intent();
		data.putExtra( Constant.EXTRA_CODE, code );
		mActivity.setResult( Activity.RESULT_OK, data );
	}

	/**
	 * execResume
	 */
    public void execResume() {
        switch ( mResultCode ) {
        	case Constant.RESULT_MENU_EVENT:
				finishEvent();
        		break;
        	case Constant.RESULT_MENU_PLACE:
        		finishPlace();
        		break;	
			case Constant.RESULT_MENU_MAP:
				finishMap();
        		break;		
		}
	}

	/**
	 * execActivityResult
	 * @param int request
	 * @param int result
	 * @param Intent data
	 */
    public void execActivityResult( int request, int result, Intent data ) {
        mResultCode = 0;
		if ( result == Activity.RESULT_OK ) {
			mResultCode = data.getIntExtra( Constant.EXTRA_CODE, 0 );
		}
    }

}