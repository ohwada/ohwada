package jp.ohwada.android.shootinggame1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * MySensor
 */ 
public class MySensor
	implements SensorEventListener {   

	// convert radian to degree
	private static final float RATIO_RAD_DEG = 180 / (float)Math.PI;
	
	// object	
	private SensorManager mSensorManager;

	// sensor
	private static final int MATRIX_SIZE = 16;
	private float[] mInR = new float[ MATRIX_SIZE ];
	private float[] mOutR = new float[ MATRIX_SIZE ];
		
	// value of sensor
	private static final int SENSOR_SIZE = 3;
	private float[] mCurrentOrientations = new float[ SENSOR_SIZE ];
	private float[] mAverageOrientations = new float[ SENSOR_SIZE ];
	private float[] mMagnetics = new float[ SENSOR_SIZE ];
	private float[] mAccelerometers = new float[ SENSOR_SIZE ];

	/**
	 * === Constrator ===
	 * @param Context context
	 */	
	public MySensor( Context context ){
       mSensorManager = (SensorManager) context.getSystemService( Context.SENSOR_SERVICE ); 
	}

	/**
	 * registerListener
	 */
	public void registerListener() {
    	final Sensor accele = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
    	final Sensor magnet = mSensorManager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD );
    	mSensorManager.registerListener( this, accele, SensorManager.SENSOR_DELAY_GAME );
    	mSensorManager.registerListener( this, magnet, SensorManager.SENSOR_DELAY_GAME );
    }

	/**
	 * unregisterListener
	 */
	public void unregisterListener() {
        mSensorManager.unregisterListener( this );
	}

	/**
	 * getCurrentOrientations
	 * @return float[]
	 */	        
	public float[] getCurrentOrientations() {
		return mCurrentOrientations;
	}
	
	/**
	 * getAverageOrientations
	 * @return float[]
	 */	        
	public float[] getAverageOrientations() {
		return mAverageOrientations;
	}
	    
	/**
	 * === onAccuracyChanged ===
	 */	
	@Override
	public void onAccuracyChanged( Sensor sensor, int accuracy ) {
		// noting to do
	}

	/**
	 * === onAccuracyChanged ===
	 */	
	@Override
	public void onSensorChanged( SensorEvent event ) {
		synchronized( this ) {
			switch( event.sensor.getType() ) { 
				case Sensor.TYPE_ACCELEROMETER: 
					mAccelerometers = event.values.clone();
					break;
				case Sensor.TYPE_MAGNETIC_FIELD: 
					mMagnetics = event.values.clone();
					break;
			}
			if( mAccelerometers != null && mMagnetics != null ){
				SensorManager.getRotationMatrix( mInR, null, mAccelerometers, mMagnetics );
				// when a camera is horizontal 
				SensorManager.remapCoordinateSystem( mInR, SensorManager.AXIS_X, SensorManager.AXIS_Z, mOutR );
				float[] orientations = new float[ SENSOR_SIZE ];
				SensorManager.getOrientation( mOutR, orientations );
				for ( int i = 0; i < SENSOR_SIZE; i++ ) {
					mCurrentOrientations[ i ] = radToDeg( orientations[ i ] );
					mAverageOrientations[ i ] = 0.9f * mAverageOrientations[ i ] + 0.1f * mCurrentOrientations[ i ];
				}
			}
		}
	}

	/**
	 * convert radian to degree
	 * @param float rad
	 * @return float
	 */	
	private static float radToDeg( float rad ){
		return RATIO_RAD_DEG * rad;
	}
		    		 			
}
