package jp.ohwada.android.satellitetracking2;

import java.util.ArrayList;
import java.util.List;

import jp.ohwada.android.satellite.AziEle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * AziEleView
 */
public class AziEleView extends View {

	private final static String TAG  = "AziEleView";

	private final static double DEG_TO_RAD = Math.PI / 180.0;
	private final static float AXIS_TEXT_SIZE = 16;
	private final static float AXIS_TEXT_MARGIN = 4;
	private final static int NUM_AXIS_RADIUS = 3;
	private final static String LABEL_NORTH = "N";

	private final static double ELEVATION_RANGE = 90;
	private final static double AZIMUTH_OFFSET = 270;
 		
    private final static float RADIUS = 5.0f;
    
    private Bitmap mBitmapAxis = null;
    private Paint mPaintCircle = null;
    private Paint mPaintCircleRed = null;
  
	private float mDensity = 0;
 		
	private float mCenterX = 0;  
	private float mCenterY = 0;  

    private float mAxisRadius = 0;

    private List<Point> mPointList = new ArrayList<Point>();
	private int mCurrentNum = 0;
	private int mPointer = 0;
 
    /** 
	 * === constructor ===
	 */	
	public AziEleView( Context context, AttributeSet attrs, int defStyleAttr ) {
     	super( context, attrs, defStyleAttr );
     	initView( context );
	}

    /** 
	 * === constructor ===
	 */	
	public AziEleView( Context context, AttributeSet attrs ) {
     	super( context, attrs );
     	initView( context );
    }
     	
    /** 
	 * === constractor ===
	 */	        
	public AziEleView( Context context ) {
	    super( context );
     	initView( context );
	}

    /** 
	 * initView
	 */	 
	private void initView( Context context ) {
		getScaledDensity();
		mPaintCircle = new Paint();
		mPaintCircleRed = new Paint();
		mPaintCircleRed.setColor( Color.RED );
	}

    /** 
	 * getScaledDensity
	 */	
    private void getScaledDensity() { 
     	DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
 		mDensity = metrics.scaledDensity;
 	}
 
    /** 
	 * === onWindowFocusChanged ===
	 */	
	@Override  
	public void onWindowFocusChanged( boolean hasFocus ) {  
		super.onWindowFocusChanged( hasFocus );  
		int width = getWidth();  
		int height = getHeight();
		float text_h = ( AXIS_TEXT_SIZE + AXIS_TEXT_MARGIN ) * mDensity; 
		mCenterX = width / 2;  
		mCenterY = ( height + text_h ) / 2;
		float h = ( height - text_h ) / 2;
		mAxisRadius = (float) ( 0.95 * Math.min( mCenterX, h ) ); 	
		initAxis( width, height );
	}

    /** 
	 * initAxis
	 */	
	private void initAxis( int width, int height ) {
		Paint paint_line = new Paint();
		Paint paint_circle = new Paint();
		paint_circle.setStyle( Paint.Style.STROKE );
		Paint paint_text = new Paint( Paint.ANTI_ALIAS_FLAG );
		paint_text.setTextSize( AXIS_TEXT_SIZE * mDensity );
		
		float line_x0 = mCenterX - mAxisRadius;
		float line_x1 = mCenterX + mAxisRadius;
		float line_y0 = mCenterY - mAxisRadius;
		float line_y1 = mCenterY + mAxisRadius;
		float text_x = mCenterX - paint_text.measureText( LABEL_NORTH ) / 2;
		float text_y = line_y0 - ( AXIS_TEXT_MARGIN * mDensity );
		float div = mAxisRadius / NUM_AXIS_RADIUS;
		
		mBitmapAxis = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
		Canvas canvas = new Canvas();
		canvas.setBitmap( mBitmapAxis );
		canvas.drawColor( Color.WHITE );
		canvas.drawText( LABEL_NORTH, text_x, text_y, paint_text );
        canvas.drawLine( line_x0, mCenterY, line_x1, mCenterY, paint_line );
        canvas.drawLine( mCenterX, line_y0, mCenterX, line_y1, paint_line );
		for ( int i = 0; i < NUM_AXIS_RADIUS; i++ ) {
			float r = div * ( i + 1 );
		    canvas.drawCircle( mCenterX, mCenterY, r, paint_circle );
        }
	}
	        
    /** 
     * === onDraw ===
     */	        
	@Override
    protected void onDraw( Canvas canvas ) {
    	if ( mBitmapAxis != null ) {
    		canvas.drawBitmap( mBitmapAxis, 0, 0, null );
    	}

		for ( int i = 0; i < mPointList.size(); i++ ) {
			Point point = mPointList.get( i );
			float x = (float) point.x;
			float y = (float) point.y;
			if (( i == mCurrentNum )||( i == mPointer )) {
		    	canvas.drawCircle( x, y, 2 * RADIUS, mPaintCircleRed );
		    } else {
		    	canvas.drawCircle( x, y, RADIUS, mPaintCircle );
		    }
        }
		    
	}

    /** 
     * setList
     */	        
    public void setList( List<AziEle> list ) {
     	mPointList.clear();
		for( AziEle sat : list ) {
			Point point = getPoint( 
    			sat.getElevationDeg(), sat.getAzimuthDeg() );
    		mPointList.add( point );
		} 
        invalidate();            
    }


    /** 
     * getPoint
     */	 
    public Point getPoint( double elevation_deg, double azimuth_deg ) {
    	double r = ( ELEVATION_RANGE - elevation_deg ) / ELEVATION_RANGE;
 		double a = ( AZIMUTH_OFFSET + azimuth_deg ) * DEG_TO_RAD;
 		double x = r * Math.cos( a );
 		double y = r * Math.sin( a );
 		float xx = (float)x * mAxisRadius + mCenterX;
 		float yy = (float)y * mAxisRadius + mCenterY;
    	Point point = new Point( (int)xx, (int)yy );
		return point;
	}		

    /** 
     * setCurrenNum
     */	
	public void setCurrentNum( int num ) {
		mCurrentNum = num;    
	}

    /** 
     * updatePointer
     */	
	public void updatePointer( int num ) {
		mPointer = num;
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