package jp.ohwada.android.mindstormsgamepad.util;

/**
 * PowerControl
 */
public class PowerJoystick extends PowerCommon {

	/** Debug */
	protected String TAG_SUB = "PowerJoystick";

	private final static float JOYSTICK_SENSITIVITY = 0.3f;	
								
	/*
	 * === Constractor ===
	 */					
	public PowerJoystick( int max, int min ) {
		super( max, min );
	}

 	/**
	 * calc
	 * @param float x
	 * @param float y
	 */
    public void calc( float x, float y ) {
    	clearReturnValues();
		if (( x*x + y*y ) < JOYSTICK_SENSITIVITY ) return;
		calcPower( x, y );		
	}
	
}
