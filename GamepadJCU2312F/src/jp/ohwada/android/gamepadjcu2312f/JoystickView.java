package jp.ohwada.android.gamepadjcu2312f;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * JoystickView
 */
public class JoystickView  {

    private Paint mPaintCircle;
    private Paint mPaintPoint;
 
    private float mCenterX = 100;
    private float mCenterY = 100;
    private float mCircleRadius = 50;
    private float mPointRadius = 5;

    private float mX = 0;
    private float mY = 0;

	private boolean isDubug = false;
	         
	/*
	 * === constractor ===
	 */         
    public JoystickView( ) {		
		mPaintCircle = new Paint();
		mPaintCircle.setStyle( Paint.Style.STROKE );
		mPaintCircle.setColor( Color.RED );	
				
		mPaintPoint = new Paint();
		mPaintPoint.setColor( Color.RED );		
    }

	/*
	 * setParam
	 * @param float x
	 * @param float y
	 * @param float circleRadius
	 * @param float pointRadius 
	 */
    public void setParam( float x, float y, float circleRadius, float pointRadius ) {
        mCenterX = x;
        mCenterY = y;
        mCircleRadius = circleRadius;
        mPointRadius = pointRadius;
     }

	/*
	 * draw
	 * @param Canvas canvas
	 */ 
    public void draw( Canvas canvas ) {

		if ( isDubug ) {	 
			canvas.drawCircle( mCenterX, mCenterY, mCircleRadius, mPaintCircle );
		}
     	
 		// changing value at each time
      	float point_x = mCenterX + mX * mCircleRadius;
     	float point_y = mCenterY + mY * mCircleRadius;    	
		canvas.drawCircle( point_x, point_y, mPointRadius, mPaintPoint );

    }

	/*
	 * setPos
	 * @param float x
	 * @param float y
	 */ 
    public void setPos( float x, float y ) {
    	float radius = (float) Math.sqrt( x * x + y * y );
    	if ( radius > 1 ) {
    		x = x / radius;
    		y = y / radius;    		
    	}
	    mX = x;
	    mY = y;
    }
            
}
