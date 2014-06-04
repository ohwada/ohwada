package jp.ohwada.android.satellitetracking1;

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

	private final static boolean D = true;
	private final static String TAG = "SatelliteView";

	private final static double MERIDIAN_JP = 135.0;
	private final static float MARGIN_RATIO = 0.05f;
    private final static float RADIUS = 5.0f;
    
    private Bitmap mBitmapAxis = null;
    private Paint mPaintCircle = null;
    private Paint mPaintCircleRed = null;
 
 	private float mCenterX = 0;  
	private float mCenterY = 0;  
	private double mWidthRatio = 0;  
	private double mHeightRatio = 0;  

	private List<SatelliteSpherical> mSatelliteSphericalList = null; 
    private List<Point> mPointList = new ArrayList<Point>();
	private int mCurrentNum = 0;
	private int mPointer = 0;
 
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
		double VIEW_RATIO = ( 1 - MARGIN_RATIO ) / 360.0 ; 
		mWidthRatio = VIEW_RATIO * width;
		mHeightRatio = 2 * VIEW_RATIO * height;		
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
		float x_jp = (float)( mWidthRatio * MERIDIAN_JP + mCenterX );
		
		mBitmapAxis = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
		Canvas canvas = new Canvas();
		canvas.setBitmap( mBitmapAxis );
		canvas.drawColor( Color.WHITE );
		canvas.drawLine( x0, mCenterY, x1, mCenterY, paint );
		canvas.drawLine( mCenterX, y0, mCenterX, y1, paint );
		canvas.drawLine( x_jp, y0, x_jp, y1, paint );
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
	public void setList( List<Satellite> list1, double div, double offset ) {
     	mPointList.clear();
		List<SatelliteSpherical> list2 = getSatelliteSphericalList( list1, div, offset ); 
		for ( int i = 0; i < list2.size(); i++ ) {  
    		mPointList.add( getPoint( list2.get( i ) ) );
    	}
    	mSatelliteSphericalList = list2;
        invalidate();            
    }
			
	/**
	 * getSatelliteSphericalList WithRrotation
	 */
	private List<SatelliteSpherical> getSatelliteSphericalList( List<Satellite> list1, double div, double offset ) {
		List<SatelliteSpherical> list2 = new ArrayList<SatelliteSpherical>();
		for ( int i = 0; i < list1.size(); i++ ) {
			Satellite sat1 = list1.get( i ); 	
			SatelliteSpherical sat2 = sat1.getSpherical();	
			sat2.setPhi( sat2.phi - ( i * div ) - offset );
			sat2.setLatLng();
			list2.add( sat2 );
		}
		return list2;
	}

    /** 
     * getPoint
     */	
	private Point getPoint( SatelliteSpherical sat ) {
		double xx = mWidthRatio * sat.longitude_deg + mCenterX;
		double yy = mHeightRatio * sat.latitude_deg + mCenterY;    	
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
     * getCurrentSatelliteSpherical
     */	
	public SatelliteSpherical getCurrentSatelliteSpherical() {
		if ( mCurrentNum >= mSatelliteSphericalList.size() ) return null;
		return mSatelliteSphericalList.get( mCurrentNum );
	}
		    
	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (D) Log.d( TAG, msg );
	}

}