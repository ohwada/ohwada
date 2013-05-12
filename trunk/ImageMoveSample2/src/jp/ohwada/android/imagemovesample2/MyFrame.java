package jp.ohwada.android.imagemovesample2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/*
 * MyFrame
 */ 
public class MyFrame extends FrameLayout {
  	// frame
	private static final int LAYOUT_FRAME = R.layout.my_frame;
  	// image
	private static final int ID_IMAGE = R.id.Frame_ImageView ;	
  	// sensitivity
	private final static int SENSITIVITY = 5;			
	// view component  
  	private View mView;    
	// border
  	private int mBorderRight = 0;
  	private int mBorderBottom = 0;	  	
	// flag for initialize
  	private boolean isFirst = true;

    /**
     * === constractor ====
     */    
    public MyFrame( Context context ) {
        this( context, null );
    }

    /**
     * === constractor ====
     */    
    public MyFrame( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    /**
     * === constractor ====
     */
    public MyFrame( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        initFrame( context, attrs, defStyle );
    }    

	/*
	 * initFrame
	 * @param Context context
	 * @param AttributeSet attrs
	 * @param int defStyle	 
	 */			    	    	 
    private void initFrame( Context context, AttributeSet attrs, int defStyle ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( 
			Context.LAYOUT_INFLATER_SERVICE );
        inflater.inflate( LAYOUT_FRAME,
            this, // we are the parent
            true );
		mView = (View) findViewById( ID_IMAGE );
		mView.setOnTouchListener( new ViewTouchListener() );
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
    	}
		// new position
		int left = mView.getLeft() + x;
		int top = mView.getTop() + y;
		redrawView( left, top );
	}

	/*
	 * redraw view
	 * @param int left
	 * @param int top
	 */ 	
    private void redrawView( int left, int top ) {
    	int right = left + mView.getWidth();
		int bottom = top + mView.getHeight();
		// border check							
		if ( left < 0 ) return;
		if ( top < 0 ) return;		
		if ( right > mBorderRight ) return;
		if ( bottom > mBorderBottom ) return;
		// redraw						
		mView.layout( left, top, right, bottom );
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
			if (( Math.abs( mPrevX - x ) < SENSITIVITY )&& 
			    ( Math.abs( mPrevY - y ) < SENSITIVITY )) {
				return true;			
			}
			// action
			switch ( event.getAction() ) {
				case MotionEvent.ACTION_MOVE:
					execMove( x, y );
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
