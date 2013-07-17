package jp.ohwada.android.mindstormsgamepad.view;

import jp.ohwada.android.mindstormsgamepad.R;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * JoystickTwoView
 */
public class JoystickTwoView {

	public static final int BUTTON_SAVE = 21;
	public static final int BUTTON_CANCEL = 22;

	// alpha
	private static final int ALPHA_OFF = 50;
	private static final int ALPHA_ON = 255;

	// callback
    private OnButtonClickListener mOnClickListener;
    	
	/** view component */
	private ImageView mImageViewLeftForward ;	
	private ImageView mImageViewRightForward ;
	private ImageView mImageViewLeftBack  ;
	private ImageView mImageViewRightBack ;

 	private TableRow mTableRowForward;
 	private TableRow mTableRowBack; 	
 		
	private TextView mTextViewLeftForward ;	
	private TextView mTextViewRightForward ;
	private TextView mTextViewLeftBack  ;
	private TextView mTextViewRightBack ;

 	private LinearLayout mLinearLayoutButton; 	
	private Button mButtonSave;
	private Button mButtonCancel;

	/**
	 * interface OnButtonClickListener
	 */
    public interface OnButtonClickListener {
        void onClick( View view, int code );
    }
		
 	/**
	 * Constractor
	 * @param View view
	 */	
    public JoystickTwoView( View view ) {
		mImageViewLeftForward = (ImageView) view.findViewById( R.id.ImageView_two_left_forward );
		mImageViewRightForward = (ImageView) view.findViewById( R.id.ImageView_two_right_forward );
		mImageViewLeftBack = (ImageView) view.findViewById( R.id.ImageView_two_left_back);
		mImageViewRightBack = (ImageView) view.findViewById( R.id.ImageView_two_right_back );

 		mTableRowForward = (TableRow) view.findViewById( R.id.TableRow_two_forward  );
 		mTableRowBack = (TableRow) view.findViewById( R.id.TableRow_two_back );
  		   				
		mTextViewLeftForward = (TextView) view.findViewById( R.id.TextView_two_left_forward );
		mTextViewRightForward = (TextView) view.findViewById( R.id.TextView_two_right_forward );
		mTextViewLeftBack = (TextView) view.findViewById( R.id.TextView_two_left_back);
		mTextViewRightBack = (TextView) view.findViewById( R.id.TextView_two_right_back );

		mLinearLayoutButton = (LinearLayout) view.findViewById( R.id.LinearLayout_two_button );
		mButtonSave = (Button) view.findViewById( R.id.Button_two_save );
		mButtonCancel = (Button) view.findViewById( R.id.Button_two_cancel );
						
		mImageViewRightForward.setImageAlpha( ALPHA_OFF );	
		mImageViewRightBack.setImageAlpha( ALPHA_OFF );	
		mImageViewLeftForward.setImageAlpha( ALPHA_OFF );	
		mImageViewLeftBack.setImageAlpha( ALPHA_OFF );	

		hideSetting();	
	}
		
	/**
	 * setOnClickListener
	 * @param OnButtonsClickListener listener
	 */
    public void setOnClickListener( OnButtonClickListener listener ) {
        mOnClickListener = listener;

        mButtonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
	       		mOnClickListener.onClick( v, BUTTON_SAVE ); 
			}
        });

        mButtonCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
	       		mOnClickListener.onClick( v, BUTTON_CANCEL ); 
			}
        });

	}
	
 	/**
	 * showImage
	 * @param boolean isLeftForward
	 * @param boolean isLeftBack
	 * @param boolean isRightForward
	 * @param boolean isRightBack
	 */
	public void showImage( boolean isLeftForward, boolean isLeftBack, boolean isRightForward, boolean isRightBack ) {
		if ( isLeftForward ) {
			isLeftBack = false;
		}	
		if ( isRightForward ) {
			isRightBack = false;
		}
		showImageIndividual( isLeftForward, isLeftBack, isRightForward, isRightBack );
	}

 	/**
	 * showImageIndividua
	 * @param boolean isLeftForward
	 * @param boolean isLeftBack
	 * @param boolean isRightForward
	 * @param boolean isRightBack
	 */		 
	public void showImageIndividual( boolean isLeftForward, boolean isLeftBack, boolean isRightForward, boolean isRightBack ) {	
		if ( isLeftForward ) {
			mImageViewLeftForward.setImageAlpha( ALPHA_ON );	
		} else {		
			mImageViewLeftForward.setImageAlpha( ALPHA_OFF );	
		}
		if ( isLeftBack ) { 	
			mImageViewLeftBack.setImageAlpha( ALPHA_ON );	
		} else {		
			mImageViewLeftBack.setImageAlpha( ALPHA_OFF );	
		}
		if ( isRightForward ) {		
			mImageViewRightForward.setImageAlpha( ALPHA_ON );		
		} else {
			mImageViewRightForward.setImageAlpha( ALPHA_OFF );	
		}
		if ( isRightBack ) {		
			mImageViewRightBack.setImageAlpha( ALPHA_ON );	
		} else {	
			mImageViewRightBack.setImageAlpha( ALPHA_OFF );	
		}
	}

 	/**
	 * showSetting
	 */
    public void showSetting() {
    	mTableRowForward.setVisibility( View.VISIBLE );
    	mTableRowBack.setVisibility( View.VISIBLE );    	
    	mLinearLayoutButton.setVisibility( View.VISIBLE );  
    }

 	/**
	 * hideSetting
	 */
    public void hideSetting() {
    	mTableRowForward.setVisibility( View.GONE );
    	mTableRowBack.setVisibility( View.GONE  );    	
    	mLinearLayoutButton.setVisibility( View.GONE  );  
    }

 	/**
	 * setSettingLabel
	 * @param String left_forward
	 * @param String left_back
	 * @param String right_forward
	 * @param String right_back
	 */    
    public void setSettingLabel( String left_forward, String left_back, String right_forward, String right_back ) {
		mTextViewLeftForward.setText( left_forward );
		mTextViewLeftBack.setText( left_back );
		mTextViewRightForward.setText( right_forward );
		mTextViewRightBack.setText( right_back );		
	}

}
