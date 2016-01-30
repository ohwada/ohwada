package jp.ohwada.android.mindstormsgamepad.util;

/**
 * PowerOrientation
 */
public class PowerOrientation extends PowerCommon {

	/** Debug */
	protected String TAG_SUB = "PowerOrientation";

	private final static float ANGLE_MIN = 10.0f;
	private final static float ANGLE_MAX = 60.0f;
								
	/*
	 * === Constractor ===
	 */					
	public PowerOrientation( int max, int min ) {
		super( max, min );
	}

 	/**
	 * calc
	 * @param float pitch
	 * @param float roll
	 */
    public void calc( float pitch, float roll ) {
        clearReturnValues();
    	float angle = calcAngle( pitch, roll );
    	if ( angle < ANGLE_MIN ) return;
    	int main = calcMainPower( angle ) ;
		setMainPower( main ) ;
    	calcPower( -pitch, -roll );
	}

 	/**
	 * calcMainPower
	 * @param float angle
	 * @return int
	 */
	private int calcMainPower( float angle ) {
		float power = MIN_POWER + ( MAX_POWER - MIN_POWER ) * angle / ANGLE_MAX;
		return (int)power;
	}

 	/**
	 * calcAngle
	 * @param float pitch
	 * @param float roll
	 * @return float
	 */
	private float calcAngle( float pitch, float roll ) {
		float angle_pitch = limitAngle( pitch );
		float angle_roll = limitAngle( roll );			
		float max = Math.max( angle_pitch,  angle_roll );
		return max;
	}

 	/**
	 * limitAngle
	 * @param float angle
	 * @return float
	 */
	private float limitAngle( float angle ) {	
		float abs = Math.abs( angle );
		if ( abs < ANGLE_MIN ) {
			abs = 0;
		}
		if ( abs > ANGLE_MAX ) {
			abs = ANGLE_MAX;
		}
		return abs;
	}

}
