package jp.ohwada.android.satelliteorbit1;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * SatelliteView
 */
public class SatelliteView extends View {

	private final static String TAG  = "SatelliteView";

	private final static double TWO_PI = 2 * Math.PI;
	private final static double HALF_PI = Math.PI / 2;
	private final static float MARGIN_RATIO = 0.05f;
    private final static float RADIUS = 5.0f;

    private Bitmap mBitmapAxis = null;
    private Paint mPaintCircle = null;
    private Paint mPaintCircleRed = null;
 
 	private float mCenterX = 0;  
	private float mCenterY = 0;  
	private double mWidthRatio = 0;  
	private double mHeightRatio = 0;  
	   
    private List<Point> mList = new ArrayList<Point>();
	private int mCurrentNum = 0;
 
    /** 
	 * === constructor ===
	 */	
	public SatelliteView( Context context, AttributeSet attrs, int defStyleAttr ) {
     	super( context, attrs, defStyleAttr );
     	initView( context );
	}

    /** 
	 * === constructor ===
	 */	
	public SatelliteView( Context context, AttributeSet attrs ) {
     	super( context, attrs );
     	initView( context );
    }
     	
    /** 
	 * === constructor ===
	 */	        
	public SatelliteView( Context context ) {
	    super( context );
     	initView( context );
	}

    /** 
	 * initView
	 */	 
	private void initView( Context context ) {
		mPaintCircle = new Paint();
		mPaintCircleRed = new Paint();
		mPaintCircleRed.setColor( Color.RED );
	}

    /** 
	 * === onWindowFocusChanged ===
	 */	
	@Override  
	public void onWindowFocusChanged( boolean hasFocus ) {  
		super.onWindowFocusChanged( hasFocus );  
		int width = getWidth();  
		int height = getHeight();  
		mCenterX = width / 2;  
		mCenterY = height / 2; 
		double VIEW_RATIO = ( 1 - MARGIN_RATIO ) / Math.PI ; 
		mWidthRatio = VIEW_RATIO * width;
		mHeightRatio = VIEW_RATIO * height;		
		initAxis( width, height );
	} 

    /** 
	 * initAxis
	 */	
	private void initAxis( int width, int height ) {
		Paint paint = new Paint();
    	float x0 = MARGIN_RATIO * width;
    	float x1 = width - x0;    	
    	float y0 = MARGIN_RATIO * height;
    	float y1 = height - y0;   
		
		mBitmapAxis = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
		Canvas canvas = new Canvas();
		canvas.setBitmap( mBitmapAxis );
		canvas.drawColor( Color.WHITE );
		canvas.drawLine( x0, mCenterY, x1, mCenterY, paint );
		canvas.drawLine( mCenterX, y0, mCenterX, y1, paint );
	}

    /** 
     * === onDraw ===
     */	        
	@Override
    protected void onDraw( Canvas canvas ) {
    	if ( mBitmapAxis != null ) {
    		canvas.drawBitmap( mBitmapAxis, 0, 0, null );
    	}	

		for ( int i = 0; i < mList.size(); i++ ) {
			Point point = mList.get( i );
			float x = (float) point.x;
			float y = (float) point.y;
			if ( i == mCurrentNum ) {
		    	canvas.drawCircle( x, y, 2 * RADIUS, mPaintCircleRed );
		    } else {
		    	canvas.drawCircle( x, y, RADIUS, mPaintCircle );
		    }	
        }           

	}

    /** 
     * setList
     */	        
    public void setList( List<Satellite> list1, double div ) {
     	mList.clear();
     	List<SatelliteSpherical> list2 = getSatelliteList( list1, div ); 
		for ( int i = 0; i < list2.size(); i++ ) {  
			Point point = getPoint( list2.get( i ) );
    		mList.add( point );
    	}
        invalidate();            
    }
			
	/**
	 * getSatelliteList WithRrotation
	 */
	private List<SatelliteSpherical> getSatelliteList( List<Satellite> list1, double div ) {
		List<SatelliteSpherical> list2 = new ArrayList<SatelliteSpherical>();
		int num = list1.size();
		for ( int i = 0; i < num; i++ ) {
			Satellite sat1 = list1.get( i );
			SatelliteSpherical sat2 = sat1.getSpherical();	
			sat2.phi = adjustPhi( sat2.phi - i * div );
			list2.add( sat2 );
		}
		return list2;
	}

    /** 
     * getPoint
     */	
	private Point getPoint( SatelliteSpherical sat ) {
		double lat = adjustLatitude( sat.theta );
		double lng = adjustLongitude( sat.phi );
		double xx = mWidthRatio * lng + mCenterX;
		double yy = mHeightRatio * lat + mCenterY;    	
    	Point point = new Point( (int)xx, (int)yy );
    	return point;
    }

    /** 
     * adjustPhi
     */	  
	private double adjustPhi( double rad ) {
		double p = rad;
		if ( p < 0 ) {
			p = TWO_PI + p;		
		}
		if ( p > TWO_PI ) {
			p = p - TWO_PI;		
		}
		return p;
	}
			
    /** 
     * adjustLatitude
     */	  
	private double adjustLatitude( double rad ) {
		double t =  rad - HALF_PI ;
		return t;
	}

    /** 
     * adjustLongitude
     */	
	private double adjustLongitude( double rad ) {
		double p = rad ;
		if ( rad > Math.PI ) {
			p = rad - TWO_PI;
		} 
		return p;
	}

    /** 
     * updateNum
     */	
	public void updateNum( int num ) {
		mCurrentNum = num;
        invalidate();       
	}
	    
	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String str ) {
		Log.d( TAG, str );
	}
}