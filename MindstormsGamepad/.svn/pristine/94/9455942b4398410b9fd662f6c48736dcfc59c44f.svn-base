package jp.ohwada.android.mindstormsgamepad.util;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.util.Log;

/**
 * PowerCommon
 */
public class PowerCommon {

	/** Debug */
	private static final boolean D = Constant.BT_DEBUG_LOG_ACTIVITY;
    private static final String TAG = Constant.TAG; 
	protected String TAG_SUB = "PowerCommon";

	// radian to degree
	private final static double RAD2DEG = 180 / Math.PI;

	protected int MAX_POWER = 100;	
	protected int MIN_POWER = 50;
	protected float POWER_RATIO = 0.6f;	
		
	protected int mMainPower = 100;	
			
	protected int mLeft = 0;
	protected int mRight = 0;
	protected int mDirection = 0;
				
	/*
	 * === Constractor ===
	 */					
	public PowerCommon( int max, int min ) {
		MAX_POWER = max;
		MIN_POWER = min;
	}

	/**
	 * setMaxPower
	 * @param int max 
	 */	
	public void setMaxPower( int max ) {
		MAX_POWER = max;
	}

	/**
	 * setMinPower
	 * @param int min
	 */	
	public void setMinPower( int min ) {
		MIN_POWER = min;
	}

 	/**
	 * setRatio
	 * @param float ratio
	 */
	public void setPowerRatio( float ratio ) {
		POWER_RATIO = ratio;
	}


 	/**
	 * setMaxPower
	 * @param int max
	 */
	public void setMainPower( int max ) {
		mMainPower = max;
	}

 	/**
	 * gettLeft(
	 * @return int
	 */	
    public int getLeft() {
		return mLeft;
	}

 	/**
	 * getRight
	 * @return int
	 */	
    public int getRight() {
		return mRight;
	}

 	/**
	 * getDirection
	 * @return int
	 */	
	public int getDirection() {
		return mDirection;
	}

 	/**
	 * clearReturnValues
	 */	
	protected void clearReturnValues() {
		mLeft = 0;
		mRight = 0;
		mDirection = Constant.DIRECTION_NONE;
	}

 	/**
	 * calcPower
	 * @param float x
	 * @param float y
	 */
	protected void calcPower( float x, float y ) {
		calcPower( x, y, mMainPower );
	}

 	/**
	 * calcPower
	 * @param float x
	 * @param float y
	 * @param int main_power
	 */
	protected void calcPower( float x, float y, int main_power ) {
		int deg = calcDeg( x, y );
		mDirection = degToDirection( deg );
		float sub = calcSubPower( main_power, deg );
		calcMoterPower( main_power, sub ) ;
	}

 	/**
	 * calcDirection
	 * @param float x
	 * @param float y
	 */
	protected void calcDirection( float x, float y ) {
		log_d( "calcDirection " + x + " " + y ); 
		int deg = calcDeg( x, y );
		mDirection = degToDirection( deg );
		log_d( "deg " + deg + " direction " + mDirection ); 
	}
			
 	/**
	 * degToDirection
	 * @param int deg 
	 * @return int
	 */
	protected int degToDirection( int deg ) {
		int direction = Constant.DIRECTION_NONE;
		if ( deg < 10 )  {
			// forward 0 -> 10
			direction = Constant.DIRECTION_FORWARD;
		} else if ( deg < 70 ) {
			direction = Constant.DIRECTION_FORWARD_RIGHT;
		} else if ( deg < 110 ) {
			// left 70 -> 110
			direction = Constant.DIRECTION_RIGHT;
		} else if ( deg < 170 ) {
			direction = Constant.DIRECTION_BACK_RIGHT;
		} else if ( deg < 190 ) {
			// forward 170 -> 190
			direction = Constant.DIRECTION_BACK;
		} else if ( deg < 250 ) {
			direction = Constant.DIRECTION_BACK_LEFT;
		} else if ( deg < 290 ) {
			// left 250 -> 290
			direction = Constant.DIRECTION_LEFT;
		} else if ( deg < 350 ) {
			direction = Constant.DIRECTION_FORWARD_LEFT;
		} else {
			// forward 350 -> 360
			direction = Constant.DIRECTION_FORWARD;
		}		
		return direction;	
	}

 	/**
	 * calcSubPower
	 * @param float main_power
	 * @return float
	 */
	protected float calcSubPower( float main_power ) {
		float sub = main_power * POWER_RATIO;
		return sub;
	}

 	/**
	 * calcSubPower
	 * @param float main_power
	 * @param int deg
	 * @return float
	 */
	protected float calcSubPower( float main_power, int deg ) {
		float ratio = 0;
		if ( deg < 90 )  {
			ratio = calcPowerRatio( 90 - deg ) ;
		} else if ( deg < 180 ) {
			ratio = calcPowerRatio( deg - 90 ) ;
		} else if ( deg < 270 )  {
			ratio = calcPowerRatio( 270 - deg ) ;
		} else {
			ratio = calcPowerRatio( deg - 270 ) ;
		}	
		float sub = main_power * ratio;
		return sub;
	}

 	/**
	 * calcPowerRatio
	 * @param int deg
	 * @return float
	 */	
	protected float calcPowerRatio( int deg ) {
		float ratio = (float) deg / 90.0f;
		return ratio;
	}

 	/**
	 * calcMoterPower
	 * @param float main_power
	 * @param float sub_power
	 */
	protected void calcMoterPower( float main_power, float sub_power ) {
		calcMoterPower( mDirection, main_power, sub_power );
	}
				
 	/**
	 * calcMoterPower
	 * @param int direction
	 * @param float main_power
	 * @param float sub_power
	 */
	protected void calcMoterPower( int direction, float main_power, float sub_power ) {
		log_d( "calcMoterPower " + direction + " " + main_power + " " + sub_power );
		float left = 0;
		float right = 0;		
		switch ( direction ) {
			case Constant.DIRECTION_FORWARD:
				left = main_power;
				right = main_power;
				break;
			case Constant.DIRECTION_FORWARD_RIGHT:
				left = main_power;
				right = sub_power;
				break;
			case Constant.DIRECTION_RIGHT:
				left = main_power;
				right = - main_power;
				break;
			case Constant.DIRECTION_BACK_RIGHT:
				left = - main_power;
				right = - sub_power;
				break;
			case Constant.DIRECTION_BACK:
				left = - main_power;
				right = - main_power;
				break;
			case Constant.DIRECTION_BACK_LEFT:
				left = - sub_power;
				right = - main_power;	
				break;
			case Constant.DIRECTION_LEFT:
				left = - main_power;
				right = main_power;
				break;
			case Constant.DIRECTION_FORWARD_LEFT:
				left = sub_power;
				right = main_power;
				break;
		}
		mLeft = limitPower( left );
		mRight = limitPower( right );
		log_d( "left " + mLeft + " right " + mRight );
	}

 	/**
	 * limitPower
	 * @param float power
	 * @return int 
	 */		
	protected int limitPower( float power ) {
		if ( power < -MAX_POWER ) {
			power = -MAX_POWER;
		} else if ( power > MAX_POWER ) {
			power = MAX_POWER;
		}
		return (int)power;				
	}

 	/**
	 * calcDeg
	 * @param float x
	 * @param float y
	 * @return int
	 */
	protected int calcDeg( float x, float y ) {
		double rad = Math.atan2( (double)y, (double)x ); 
		// rotate counter-clockwise 
		int deg = (int) ( rad * RAD2DEG ) + 90;
		// adjust to 0 - 360
		if ( deg < 0 ) {
			deg += 360;
		}
		if ( deg > 360 ) {
			deg -= 360;
		}
		return deg;
	}

	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}

}
