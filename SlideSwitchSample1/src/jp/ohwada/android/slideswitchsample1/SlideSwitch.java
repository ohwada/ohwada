package jp.ohwada.android.slideswitchsample1;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/*
 * SlideSwitch
 */ 
public class SlideSwitch extends FrameLayout {

    /**
     * Interface definition for a callback 
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a switch has changed.
         *
         * @param view The switch view whose state has changed.
         * @param isChecked  The new checked state of view.
         */
        void onCheckedChanged( SlideSwitch view, boolean isChecked );
    }

	// orientation
    private static final int VERTICAL = 0;
   	private static final int HORIZONTAL = 1;    
    private int mOrientation = HORIZONTAL;
    		
	// view component  
  	private FrameLayout	 mFrameLayout;
  	private ImageView mImageView;
		    
	// border
  	private int mBorderRight = 0;
  	private int mBorderBottom = 0;
  	private int mGoalLeft = 0;
  	private int mGoalTop = 0;

  	// sensitivity
	private int mSensitivityLeft = 0;
	private int mSensitivityTop = 0;
	  	
	// flag for initialize
  	private boolean isFirst = true;

	// status
  	private boolean isChecked = false;	// right

	// Listener used to Checked Change events.
    private OnCheckedChangeListener mOnCheckedChangeListener;    

    /**
     * === constractor ====
     */    
    public SlideSwitch( Context context ) {
        this( context, null );
    }

    /**
     * === constractor ====
     */    
    public SlideSwitch( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    /**
     * === constractor ====
     */
    public SlideSwitch( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        initButton( context, attrs, defStyle );
    }    

	/*
	 * initButton
	 * @param Context context
	 * @param AttributeSet attrs
	 * @param int defStyle	 
	 */			    	    	 
    private void initButton( Context context, AttributeSet attrs, int defStyle ) {		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE );
        inflater.inflate( R.layout.slide_switch,
            this, // we are the parent
            true );
            
		mFrameLayout = (FrameLayout) findViewById( R.id.slide_switch_FrameLayout );
		mImageView = (ImageView) findViewById( R.id.slide_switch_ImageView );
		mImageView.setOnTouchListener( new ViewTouchListener() );

		initAttributes( context, attrs );
    }

	/*
	 * initAttributes
	 * @param Context context	
	 * @param AttributeSet attrs	 
	 */ 
	private void initAttributes( Context context, AttributeSet attrs ) {
    	if ( attrs == null ) return;
    	TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.SlideSwitch );
		int width = ta.getLayoutDimension( R.styleable.SlideSwitch_slideswitch_width, 0 );
		int height = ta.getLayoutDimension( R.styleable.SlideSwitch_slideswitch_height, 0 ); 
		int background = ta.getResourceId( R.styleable.SlideSwitch_slideswitch_background, 0 );    
		int image = ta.getResourceId( R.styleable.SlideSwitch_slideswitch_image, 0 );       
		String orientation = ta.getString( R.styleable.SlideSwitch_slideswitch_orientation );   
		if (( width > 0 )||( height > 0 )) {
			setFrameLayout( TypedValue.COMPLEX_UNIT_PX, width, height );
		}
		if ( background  > 0 ) {
    		setFrameBackgroundResource( background );
		} 
		if ( image  > 0 ) {
    		setImageResource( image );
		}
		if ( orientation != null ) {
			if ( "horizontal".equals( orientation )) {
				setOrientation( HORIZONTAL );
			} else if ( "vertical".equals( orientation )) {
				setOrientation( VERTICAL );
			}
		}
	}

	/**
	 * setFrameLayout
	 * @param float width
	 * @param float height
	 */  
    public void setFrameLayout( float width, float height ) {
    	setFrameLayout( TypedValue.COMPLEX_UNIT_DIP, width, height );
    }
    
	/**
	 * setFrameLayout
	 * @param int unit
	 * @param float width
	 * @param float height
	 */  
    public void setFrameLayout( int unit, float f_width, float f_height ) {
    	int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    	int height = ViewGroup.LayoutParams.WRAP_CONTENT;
    	if ( f_width > 0 ) {
			width = (int) getPixcelSize( unit, f_width );
    	}
    	if ( f_height > 0 ) {
    		height = (int) getPixcelSize( unit, f_height );
	    }	
    	FrameLayout.LayoutParams params = 
    		new FrameLayout.LayoutParams( width, height );
		mFrameLayout.setLayoutParams( params );		
	}

    /**
     * Get the pixcel size from a given unit and value.  
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    private float getPixcelSize( int unit, float size ) {        
        return TypedValue.applyDimension( 
			unit, size, getContext().getResources().getDisplayMetrics() );
    }

	/*
	 * setFrameBackgroundResource
	 * @param int id
	 */ 
	public void setFrameBackgroundResource( int id ) {
		mFrameLayout.setBackgroundResource( id );
	}

	/*
	 * setImageResource
	 * @param int id
	 */     		
	public void setImageResource( int id ) {
		mImageView.setImageResource( id );
	}

	/*
	 * setOrientation
	 * @param int orientation
	 */ 
	public void setOrientation( int orientation ) {
		mOrientation = orientation;
	}

    /**
     * Register a callback
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener( OnCheckedChangeListener listener ) {
        mOnCheckedChangeListener = listener;
    }
    		
	/*
	 * move view
	 * @param int x
	 * @param int y
	 */  
    private void moveView( int x, int y ) {
		// initialize    
    	if ( isFirst ) {
    		isFirst = false;
    		mBorderRight = getWidth();
    		mBorderBottom = getHeight();
			mGoalLeft = mBorderRight - mImageView.getWidth();
    		mGoalTop = mBorderBottom - mImageView.getHeight();
    		mSensitivityLeft = mImageView.getWidth() / 3;
    		mSensitivityTop = mImageView.getHeight() / 3;
    	}
		// new position
		if ( mOrientation == HORIZONTAL ) {
			y = 0;
		} else {
			x = 0;
		}
		int left = mImageView.getLeft() + x;
		int top = mImageView.getTop() + y;
		redrawView( left, top );
	}

	/*
	 * move view to original position
	 */ 
    private void moveViewToSide() {
        if ( isChecked ) {
			if ( mOrientation == HORIZONTAL ) {
            	// redraw in right side
				redrawView( mGoalLeft, 0 );
			} else {
				// redraw in bottom side
				redrawView( 0, mGoalTop );
			}	
		} else {
		    // redraw in start side
			redrawView( 0, 0 );
		}				
	}

	/*
	 * redraw view
	 * @param int left
	 * @param int top
	 */ 	
    private void redrawView( int left, int top ) {
    	int right = left + mImageView.getWidth();
		int bottom = top + mImageView.getHeight();
		// border check							
		if ( left < 0 ) return;
		if ( top < 0 ) return;		
		if ( right > mBorderRight ) return;
		if ( bottom > mBorderBottom ) return;
		// redraw		
		mImageView.layout( left, top, right, bottom );
		if ( isChecked && checkOriginalSide( left, top ) ) {
			// set off if arrive at origina side
	     	isChecked = false;		// off
			performCheckedChange();
		} else if ( !isChecked && checkGoalSide( left, top ) ) {
			// set on if arrive at goal side
	     	isChecked = true;		// on
			performCheckedChange();
		}
    }

	/*
	 * checkOriginalSide
	 * @param int left
	 * @param int top
	 */ 	
    private boolean checkOriginalSide( int left, int top ) {
		if (( mOrientation == VERTICAL ) && ( top < mSensitivityTop )) {
			return true;
		}
		if (( mOrientation == HORIZONTAL ) && ( left < mSensitivityLeft )) {
			return true;
		}
		return false;
	}	

	/*
	 * checkGoalSide
	 * @param int left
	 * @param int top
	 */ 
    private boolean checkGoalSide( int left, int top ) {
		if (( mOrientation == VERTICAL ) && ( Math.abs( top - mGoalTop  ) < mSensitivityTop )) {
			return true;
		}
		if (( mOrientation == HORIZONTAL ) && ( Math.abs( left - mGoalLeft ) < mSensitivityLeft )) {
			return true;
		}
		return false;
	}
			    
    /**
     * Call this view's performCheckedChange, if it is defined.
     */
    private void performCheckedChange() {
		if ( mOnCheckedChangeListener != null ) {
			mOnCheckedChangeListener.onCheckedChanged( this, isChecked );
		}
	}
	             					
	/*
	 * class ViewTouchListener
	 */  
	private class ViewTouchListener implements OnTouchListener {
		// prevous position
		private int mPrevX = 0;
		private int mPrevY = 0;

		/*
		 * constractor
		 */  
		public ViewTouchListener() {
			// dummy
		}

		/*
		 * === onTouch ===
		 */ 
		@Override
		public boolean onTouch( View view, MotionEvent event ) {
			// get position
			int x = (int) event.getRawX();
			int y = (int) event.getRawY();
			// action
			switch ( event.getAction() ) {
				case MotionEvent.ACTION_MOVE:
					execMove( x, y );
					break;
				case MotionEvent.ACTION_UP:
					moveViewToSide();
					break;					
			}
			// save position
			mPrevX = x;
			mPrevY = y;
			return true;
		}

		/*
		 * execute ACTION_MOVE
	 	 * @param int x
	 	 * @param int y
		 */  
		private void execMove( int x, int y ) {
			// move difference	
			int move_x = x - mPrevX;
			int move_y = y - mPrevY;
			moveView( move_x, move_y );
		}	
	}
	
}
