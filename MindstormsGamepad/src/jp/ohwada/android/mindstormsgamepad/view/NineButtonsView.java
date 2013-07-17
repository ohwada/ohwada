package jp.ohwada.android.mindstormsgamepad.view;

import jp.ohwada.android.mindstormsgamepad.Constant;
import jp.ohwada.android.mindstormsgamepad.R;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * NineButtons View
 */
public class NineButtonsView {

	public static final int BUTTON_FORWARD_LEFT = 1;
	public static final int BUTTON_FORWARD = 2;
	public static final int BUTTON_FORWARD_RIGHT = 3;
	public static final int BUTTON_LEFT = 4;
	public static final int BUTTON_CENTER = 5;
	public static final int BUTTON_RIGHT = 6;
	public static final int BUTTON_BACK_LEFT = 7;
	public static final int BUTTON_BACK = 8;
	public static final int BUTTON_BACK_RIGHT = 9;
	public static final int BUTTON_SAVE = 21;
	public static final int BUTTON_CANCEL = 22;

	private static final int[] CODE_TO_DERECTION = {
		Constant.DIRECTION_NONE,
		Constant.DIRECTION_FORWARD_LEFT,
		Constant.DIRECTION_FORWARD,
		Constant.DIRECTION_FORWARD_RIGHT,
		Constant.DIRECTION_LEFT,
		Constant.DIRECTION_CENTER,
		Constant.DIRECTION_RIGHT,
		Constant.DIRECTION_BACK_LEFT,
		Constant.DIRECTION_BACK,
		Constant.DIRECTION_BACK_RIGHT,
	}; 

	private static final int[] DERECTION_TO_CODE = {
		BUTTON_CENTER,
		BUTTON_FORWARD_LEFT,
		BUTTON_FORWARD,
		BUTTON_FORWARD_RIGHT,
		BUTTON_LEFT,
		BUTTON_CENTER,
		BUTTON_RIGHT,
		BUTTON_BACK_LEFT,
		BUTTON_BACK,
		BUTTON_BACK_RIGHT,
	};
						
	// alpha
	private static final int ALPHA_OFF = 50;
	private static final int ALPHA_ON = 255;

	// callback
    private OnButtonTouchListener mOnTouchListener;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    	     	
	// view			
	private ImageView mImageViewForwardLeft ;   
	private ImageView mImageViewForward ;
	private ImageView mImageViewForwardRight ;
	private ImageView mImageViewLeft ;
	private ImageView mImageViewCenter ;
	private ImageView mImageViewRight ;
	private ImageView mImageViewBackLeft ;   
	private ImageView mImageViewBack ;
	private ImageView mImageViewBackRight ;

 	private TableRow mTableRowForward;
 	private TableRow mTableRowCenter; 	 		
	private TextView mTextViewForward ;	
	private TextView mTextViewLeft  ;

 	private LinearLayout mLinearLayoutButton; 	
	private Button mButtonSave;
	private Button mButtonCancel;
	
	/**
	 * interface OnTouchListener
	 */
    public interface OnButtonTouchListener {
        void onTouch( View view, MotionEvent event, int code );
    }
	
 	/**
	 * Constractor
	 * @param Context context
	 * @param View view
	 */	
    public NineButtonsView( Context context, View view ) {     
		mContext = context;
    	mDisplayMetrics = context.getResources().getDisplayMetrics();   
    	
		mImageViewForwardLeft = (ImageView) view.findViewById( R.id.ImageView_nine_forward_left );   
		mImageViewForward = (ImageView) view.findViewById( R.id.ImageView_nine_forward );
		mImageViewForwardRight = (ImageView) view.findViewById( R.id.ImageView_nine_forward_right );
		mImageViewLeft = (ImageView) view.findViewById( R.id.ImageView_nine_left );
		mImageViewCenter = (ImageView) view.findViewById( R.id.ImageView_nine_center );
		mImageViewRight = (ImageView) view.findViewById( R.id.ImageView_nine_right );
		mImageViewBackLeft = (ImageView) view.findViewById( R.id.ImageView_nine_back_left );   
		mImageViewBack = (ImageView) view.findViewById( R.id.ImageView_nine_back );
		mImageViewBackRight = (ImageView) view.findViewById( R.id.ImageView_nine_back_right );

 		mTableRowForward = (TableRow) view.findViewById( R.id.TableRow_nine_forward  );
 		mTableRowCenter = (TableRow) view.findViewById( R.id.TableRow_nine_center );
  		   				
		mTextViewForward = (TextView) view.findViewById( R.id.TextView_nine_forward );
		mTextViewLeft = (TextView) view.findViewById( R.id.TextView_nine_left );

		mLinearLayoutButton = (LinearLayout) view.findViewById( R.id.LinearLayout_nine_button );
		mButtonSave = (Button) view.findViewById( R.id.Button_nine_save );
		mButtonCancel = (Button) view.findViewById( R.id.Button_nine_cancel );

		hideSetting();
		setSizeByOrientation();
	}

	/**
	 * setOnTouchListener
	 * @param OnTouchListener listener
	 */
    public void setOnTouchListener( OnButtonTouchListener listener ) {
        mOnTouchListener = listener;

		mImageViewForwardLeft.setOnTouchListener( new OnTouchListener() { 
			@Override 
			public boolean onTouch( View view, MotionEvent event ) {
	       		mOnTouchListener.onTouch( view, event, BUTTON_FORWARD_LEFT ); 
				return true; 
	       	} 
		});

		mImageViewForward.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_FORWARD ); 
				return true; 
        	} 
        });

		mImageViewForwardRight.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_FORWARD_RIGHT ); 
				return true; 
        	} 
		});

		mImageViewLeft.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_LEFT ); 
				return true; 
        	} 
		});

        mImageViewCenter.setOnTouchListener( new OnTouchListener() {
            @Override
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_CENTER ); 
				return true;
			}
        });

		mImageViewRight.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_RIGHT ); 
				return true; 
        	} 
		});

		mImageViewBackLeft.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_BACK_LEFT ); 
				return true; 
			} 
		});

		mImageViewBack.setOnTouchListener( new OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_BACK ); 
				return true; 
        	} 
		});

		mImageViewBackRight.setOnTouchListener( new View.OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
				mOnTouchListener.onTouch( view, event, BUTTON_BACK_RIGHT ); 
				return true; 
        	} 
		}); 

        mButtonSave.setOnTouchListener( new View.OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		if ( event.getAction()  == KeyEvent.ACTION_UP ) {
					mOnTouchListener.onTouch( view, event, Constant.BUTTON_SAVE ); 
				}	
				return true; 
        	} 
		}); 

        mButtonCancel.setOnTouchListener( new View.OnTouchListener() { 
        	@Override 
        	public boolean onTouch( View view, MotionEvent event ) {
        		if ( event.getAction()  == KeyEvent.ACTION_UP ) {
					mOnTouchListener.onTouch( view, event, Constant.BUTTON_CANCEL ); 
				}	
				return true; 
        	} 
		});

 	}

	/**
	 * setSize By Orientation
	 */
    public void setSizeByOrientation() {
    	if( getOrientation() == Configuration.ORIENTATION_LANDSCAPE ) {
    		setSizeForLandscape();
    	} else {
    		setSizeForPortrait();
    	}
	}	

	/**
	 * getOrientation
	 */
    public int getOrientation() {
		Configuration config = mContext.getResources().getConfiguration();
		return config.orientation;
	}	
	
	/**
	 * setSize For Portrait
	 */
    public void setSizeForPortrait() {
    	Point point = getDisplaySize();
    	int size = adjustSize( point.x / 4 );
    	setSize( size );
    }

	/**
	 * setSize For Landscape
	 */
    public void setSizeForLandscape() {
    	Point point = getDisplaySize();
    	int size = adjustSize( point.y / 4.5f );
    	setSize( size );
    }

	/**
	 * setSize
	 * @param float size
	 */
    private void setSize( int size ) {
    	TableRow.LayoutParams params = new TableRow.LayoutParams( size, size );
		mImageViewForwardLeft.setLayoutParams( params );  
		mImageViewForward.setLayoutParams( params );
		mImageViewForwardRight.setLayoutParams( params );
		mImageViewLeft.setLayoutParams( params );
		mImageViewCenter.setLayoutParams( params );
		mImageViewRight.setLayoutParams( params );
		mImageViewBackLeft.setLayoutParams( params ); 
		mImageViewBack.setLayoutParams( params );
		mImageViewBackRight.setLayoutParams( params );		
	}

	/**
	 * getDisplaySize
	 * @return Point
	 */   
    private Point getDisplaySize() {
        WindowManager wm = (WindowManager) mContext.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize( point );
		return point;
	}

	/**
	 * adjustSize
	 * @param float size
	 * @return int
	 */ 
    private int adjustSize( float size ) {
    	float max = getPixcelSize( TypedValue.COMPLEX_UNIT_DIP, 100 );
    	float min = getPixcelSize( TypedValue.COMPLEX_UNIT_DIP,  50 );
    	if ( size > max ) {
    		size = max;
    	}
    	if ( size < min ) {
    		size = min;
    	}
    	return (int)size;
    }

    /**
     * Get the pixcel size from a given unit and value.  
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    private float getPixcelSize( int unit, float size ) {    
        return TypedValue.applyDimension( unit, size, mDisplayMetrics );
    }
 	       
	/**
	 * setCenterStop
	 */
	public void setCenterStop() {
		setOnEightButons();
        mImageViewCenter.setImageResource( R.drawable.robot_stop )	;
    }

	/**
	 * setCenterRun
	 */
	public void setCenterRun() {        
		setOffEightButons();
        mImageViewCenter.setImageResource( R.drawable.robot_center )	;	
	}

	/**
	 * hideImage
	 */ 
 	public void hideImage() {
 		setOffEightButons();
 	}
 
	/**
	 * showImage
	 * @param int direction 
	 */ 
 	public void showImageDirection( int direction ) {
 		showImageCode( getCode( direction ) );
 	}
 	 	
	/**
	 * showImage
	 * @param int code 
	 */ 
 	public void showImageCode( int code ) {
		switch ( code ) {
			case BUTTON_FORWARD_LEFT:
				setOffEightButons();
				mImageViewForwardLeft.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_FORWARD:
				setOffEightButons();
				mImageViewForward.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_FORWARD_RIGHT:
				setOffEightButons();
				mImageViewForwardRight.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_LEFT:
				setOffEightButons();
				mImageViewLeft.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_CENTER:
				setOffEightButons();
				break;
			case BUTTON_RIGHT:
				setOffEightButons();
				mImageViewRight.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_BACK_LEFT:
				setOffEightButons();
				mImageViewBackLeft.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_BACK:
				setOffEightButons();
				mImageViewBack.setImageAlpha( ALPHA_ON );
				break;
			case BUTTON_BACK_RIGHT:
				setOffEightButons();
				mImageViewBackRight.setImageAlpha( ALPHA_ON );
				break;			
		}
	}

	/**
	 * setOnEightButons
	 */				
	public void setOnEightButons() {
		setAlphaEightButons( ALPHA_ON );
	}

	/**
	 * setOffEightButons
	 */	
	private void setOffEightButons() {
		setAlphaEightButons( ALPHA_OFF );	
	}

	/**
	 * setAlphaEightButons
	 * @param int alpha
	 */	
	private void setAlphaEightButons( int alpha ) {
		mImageViewForwardLeft.setImageAlpha( alpha );					
		mImageViewForward.setImageAlpha( alpha );	
		mImageViewForwardRight.setImageAlpha( alpha );	
		mImageViewLeft.setImageAlpha( alpha );	
		mImageViewRight.setImageAlpha( alpha );	
		mImageViewBackLeft.setImageAlpha( alpha );	
		mImageViewBack.setImageAlpha( alpha );	
		mImageViewBackRight.setImageAlpha( alpha );	
	}

	/**
	 * getDirection
	 * @param int code
	 * @return int
	 */				
	public int getDirection( int code ) {
		if ( code < 0 ) return 0;
		if ( code > CODE_TO_DERECTION.length ) return 0;
		return CODE_TO_DERECTION[ code ];
	}

	/**
	 * getCode
	 * @param int direction
	 * @return int
	 */				
	public int getCode( int direction ) {
		if ( direction < 0 ) return 0;
		if ( direction > DERECTION_TO_CODE.length ) return 0;
		return DERECTION_TO_CODE[ direction ];
	}

 	/**
	 * showSetting
	 */
    public void showSetting() {
    	mImageViewLeft.setImageResource( R.drawable.robot_left );
    	mImageViewRight.setImageResource( R.drawable.robot_right );
    	mTableRowForward.setVisibility( View.VISIBLE );
    	mTableRowCenter.setVisibility( View.VISIBLE );    	
    	mLinearLayoutButton.setVisibility( View.VISIBLE );  
    }

 	/**
	 * hideSetting
	 */
    public void hideSetting() {
    	mImageViewLeft.setImageResource( R.drawable.robot_turn_left );
    	mImageViewRight.setImageResource( R.drawable.robot_turn_right );
    	mTableRowForward.setVisibility( View.GONE );
    	mTableRowCenter.setVisibility( View.GONE  );    	
    	mLinearLayoutButton.setVisibility( View.GONE  );  
    }

 	/**
	 * setSettingLabel
	 * @param String forward
	 * @param String left
	 */    
    public void setSettingLabel( String forward, String left ) {
		mTextViewForward.setText( forward );
		mTextViewLeft.setText( left );	
	}
	
}
