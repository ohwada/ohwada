package jp.ohwada.android.gpsstatussample1;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * SatelliteView
 */
public class SatelliteView extends View {

	private final static String TAG  = "SatelliteView";

	private final static double DEG_TO_RAD = Math.PI / 180.0;
	private final static float AXIS_TEXT_SIZE = 16;
	private final static float AXIS_TEXT_MARGIN = 4;
	private final static float SAT_TEXT_SIZE = 16;
	private final static int NUM_AXIS_RADIUS = 3;
	private final static String LABEL_NORTH = "N";

    private Bitmap mBitmapAxis = null;
	private Paint mPaintCircle = null;
    private Paint mPaintText= null;
  
	private float mDensity = 0;
 		
	private float mCenterX = 0;  
	private float mCenterY = 0;  	
    private float mTextHalfHeight = 0;
    private float mAxisRadius = 0;
	private float mSatRadius = 0;

    private List<SatellitePoint> mList = new ArrayList<SatellitePoint>();
 
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
		getScaledDensity();
		mPaintCircle = new Paint();
		mPaintCircle.setStyle( Paint.Style.STROKE );
		mPaintCircle.setColor( Color.RED );
		mPaintText = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintText.setTextSize( SAT_TEXT_SIZE * mDensity );
		FontMetrics metrics = mPaintText.getFontMetrics();
		mTextHalfHeight = (metrics.ascent + metrics.descent) / 2;
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
		mCenterX = width / 2;  
		mCenterY = ( height + ( AXIS_TEXT_SIZE + AXIS_TEXT_MARGIN ) * mDensity ) / 2;  
		mAxisRadius = (float) ( 0.95 * Math.min( mCenterX, mCenterY ) ); 
		mSatRadius = 12 * mDensity;
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

		for ( int i = 0; i < mList.size(); i++ ) {
			SatellitePoint point = mList.get( i );
			float x = point.x * mAxisRadius + mCenterX;
			float y = point.y * mAxisRadius + mCenterY;
			float text_x = x - mPaintText.measureText( point.name ) / 2;
			float text_y = y - mTextHalfHeight;
		    canvas.drawCircle( x, y, mSatRadius, mPaintCircle );
		    canvas.drawText( point.name, text_x, text_y, mPaintText );
        }           

	}

    /** 
     * setList
     */	        
    public void setList( Iterable<GpsSatellite> list ) {
     	mList.clear();
		for( GpsSatellite sat : list ) {
    		mList.add( new SatellitePoint( sat ) );
		} 
        invalidate();            
    }

    /** 
     * class SatellitePoint
     */	
 	private class SatellitePoint { 
		public String name = "";
		public float x = 0;		
		public float y = 0;		
 		public SatellitePoint( GpsSatellite sat ) {
			name = Integer.toString( sat.getPrn() );
			float r = ( 90 - sat.getElevation() ) / 90;
 			double azimuth = ( 270 + sat.getAzimuth() ) * DEG_TO_RAD;
			x = r * (float) Math.cos( azimuth );
			y = r * (float) Math.sin( azimuth );
		}	
	}	
	   
	/**
	 * log_d
	 */	
	@SuppressWarnings("unused")
	private void log_d( String str ) {
		Log.d( TAG, str );
	}
}