package jp.ohwada.android.mindstormsgamepad.util;

/**
 * PowerSeekbar
 */
public class PowerSeekbar extends PowerCommon {

	/** Debug */
	protected String TAG_SUB = "PowerSeekbar";
								
	/*
	 * === Constractor ===
	 */					
	public PowerSeekbar( int max, int min ) {
		super( max, min );
	}

 	/**
	 * calc
	 * @param int direction
	 * @param int main_power
	 * @return int
	 */
    public void calc( int direction, int main_power ) {
    	calc( direction, (float) main_power );
    }
    
 	/**
	 * calc
	 * @param int direction
	 * @param float main_power
	 * @return int
	 */
    public void calc( int direction, float main_power ) {
    	float sub_power = calcSubPower( main_power );	
    	calcMoterPower( direction, main_power, sub_power );
	}
	
}
