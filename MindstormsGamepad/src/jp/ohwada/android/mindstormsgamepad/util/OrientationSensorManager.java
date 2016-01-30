package jp.ohwada.android.mindstormsgamepad.util;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 *  Orientation Sensor Manager
 */
public class OrientationSensorManager {

	/**
	 * interface OnButtonClickListener
	 */
    public interface OnSensorListener {
        void onSensorChanged( SensorEvent event, int pitch, int roll  );
    }

	/** Debug */
    private static final boolean D = Constant.DEBUG;
    private static final String TAG = Constant.TAG; 
	private static final String TAG_SUB = "OrientationSensorManager ";
	
	private static final boolean DEBUG_ORIENTATION_SENSOR = Constant.DEBUG_ORIENTATION_SENSOR;

	private final static int SENSITIVITY = 5;	
	
	// radian to degree
	private final static double RAD2DEG = 180 / Math.PI;

	// class object													
	private SensorManager mSensorManager;
    private OnSensorListener mOnSensorListener;
    	
	// local variable
	private float[] mRotationMatrix = new float[9];
	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private float[] mAttitude = new float[3];						

	private int mPrevPitch = 0; 
	private int mPrevRoll = 0;
	
 	/**
	 * Constractor
	 * @param Context context
	 */	
	public OrientationSensorManager( Context context ) {
		if ( DEBUG_ORIENTATION_SENSOR ) return;
		mSensorManager = (SensorManager) context.getSystemService( Context.SENSOR_SERVICE );
	}

 	/**
	 * setOnSensorListener
	 * @param OnSensorListener listener
	 */	
    public void setOnSensorListener( OnSensorListener listener ) {
        mOnSensorListener = listener;
	}
	  
	/**
	 * registerListener
	 */
    public void registerListener() {
    	if ( DEBUG_ORIENTATION_SENSOR ) return;
		mSensorManager.registerListener(
			mSensorEventListener,
			mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ),
			SensorManager.SENSOR_DELAY_NORMAL );
		mSensorManager.registerListener(
			mSensorEventListener,
			mSensorManager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD ),
			SensorManager.SENSOR_DELAY_NORMAL );
	}

	/**
	 * unregisterListener
	 */
    public void unregisterListener() {
    	if ( DEBUG_ORIENTATION_SENSOR ) return;	
		mSensorManager.unregisterListener( mSensorEventListener );
	}

	/**
	 * SensorEventListener
	 */
	private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
		public void onAccuracyChanged( Sensor sensor, int accuracy ) {
			// dummy
		}
		@Override
		public void onSensorChanged( SensorEvent event ) {
			execSensorChanged( event );
		}
	};

	/**
	 * execSensorChanged
	 * @param SensorEvent event
	 */
	private void execSensorChanged( SensorEvent event ) {
		if ( DEBUG_ORIENTATION_SENSOR ) return;
		switch( event.sensor.getType() ) {
			case Sensor.TYPE_MAGNETIC_FIELD:
				mGeomagnetic = event.values.clone();
				if ( mGeomagnetic == null ) return;
				break;
			case Sensor.TYPE_ACCELEROMETER:
				mGravity = event.values.clone();
				if ( mGravity == null ) return;
				break;
		}
		
		SensorManager.getRotationMatrix( mRotationMatrix, null, mGravity, mGeomagnetic );				
		SensorManager.getOrientation( mRotationMatrix, mAttitude );
		int pitch = (int) ( mAttitude[1] * RAD2DEG );
		int roll = (int) ( mAttitude[2] * RAD2DEG );
		if (( Math.abs( mPrevPitch - pitch ) < SENSITIVITY ) &&
		    ( Math.abs( mPrevRoll - roll ) < SENSITIVITY )) return;
		mPrevPitch = pitch;
		mPrevRoll = roll;
		log_d( "pitch " + pitch + " roll " + roll );
		if ( mOnSensorListener != null ) {
			mOnSensorListener.onSensorChanged( event, pitch, roll );
		}	
	}

	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}						   
}
