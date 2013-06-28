package jp.ohwada.android.batterymanagersample1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * BatteryReceiver
 */
public class BatteryReceiver extends BroadcastReceiver {

	/**
	 * interface OnReceiveListener
	 */        
    public interface OnReceiveListener {
        void onReceive( BatteryRecod recod );
    }

    private Context mContext;
    private OnReceiveListener mReceiveListener;

	/**
	 * constractor
	 */ 
	public BatteryReceiver( Context context ) {
		super();
		mContext = context; 
	}

	/**
	 * === onReceive ===
	 */  	
	@Override
	public void onReceive( Context context, Intent intent ) {
        String action = intent.getAction();
        if ( action.equals( Intent.ACTION_BATTERY_CHANGED ) ) {
        	notifyReceive( intent );
		}
	}

	/**
	 * register
	 */	
    public void register() {
        IntentFilter filter = new IntentFilter();        
        filter.addAction( Intent.ACTION_BATTERY_CHANGED );
        mContext.registerReceiver( this, filter );
    }

	/**
	 * unregister
	 */	
    public void unregister() {
        mContext.unregisterReceiver( this );
    }
                
	/**
	 * setOnReceiveListener
	 * @param OnReceiveListener listener
	 */
    public void setOnReceiveListener( OnReceiveListener listener ) {
        mReceiveListener = listener;
    }

	/**
	 * notifyReceive
	 * @param Intent intent
	 */        
    private void notifyReceive( Intent intent ) {
        if ( mReceiveListener != null ) {
            mReceiveListener.onReceive( new BatteryRecod( intent ) );
        }
    } 
    	
}
