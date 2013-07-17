package jp.ohwada.android.mindstormsgamepad.view;

import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 *  SeekbarPower
 */
public class SeekbarPower { 

	/* power */		
	private static final int POWER_SENSITIVITY = 5;
	
	/* view component */
	private SeekBar mSeekBarPower;
        									       
    // local variable
    private int mMaxPower = 100;
	private int mPowerMain = 50; 
	    
	/*
	 * === Constractor ===
	 */						
	public SeekbarPower() {
		// dummy
	}

	/**
	 * setPower
	 * @param int max
	 * @param int default 
	 */	
	public void setParam( int max, int _default ) {
		mMaxPower = max;
		setPower( _default  );
	}
	
	/**
	 * getPower
	 * @return int	 
	 */	
	public int getPowerMain() {
		return mPowerMain;
	}
	
	/**
	 * initSeekbarPower
	 * @param View view
	 * @param int id
	 */	
	public void initSeekbarPower( View view, int id ) {
        mSeekBarPower = (SeekBar) view.findViewById( id );
        mSeekBarPower.setMax( mMaxPower );
        mSeekBarPower.setProgress( mPowerMain );
        mSeekBarPower.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch( SeekBar seek ) {
				// noting to do
            }
            @Override
            public void onProgressChanged( SeekBar seek, int progress, boolean touch ) {
            	execProgressChanged( seek, progress, touch );
            }
            @Override
            public void onStopTrackingTouch( SeekBar seek ) {
				// noting to do
            }
        });
	}

	/**
	 * initSeekbarPower
	 * @param SeekBar seek
	 * @param int progress
	 * @param boolean touch
	 */	
	private void execProgressChanged( SeekBar seek, int progress, boolean touch ) {
		if ( Math.abs( mPowerMain - progress ) > POWER_SENSITIVITY ) {
			setPower( progress );
		}
	}	
	
	/**
	 * setPower
	 * @param int power	 
	 */	
	private void setPower( int power ) {
		if ( power > 0 ) {
			mPowerMain = power;
		}
	}
}
