package jp.ohwada.android.mousesample1;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

/**
 * InputDeviceManager
 */ 
public class InputDeviceManager {
	
	private static final boolean D = true;
    private static final String TAG = "GamePad"; 
    private static final String TAG_SUB = "InputDeviceManager";

	private static final String LF = "\n";
	
	/**
	 * interface OnInputDeviceListener
	 */        
    public interface OnInputDeviceListener {
        void onInputDeviceAdded( int deviceId );
        void onInputDeviceRemoved( int deviceId );
		void onInputDeviceChanged( int deviceId );
    }
		
	private InputManager mInputManager;
    private OnInputDeviceListener mOnInputDeviceListener;
    private InputDevice mInputDevice; 
    
	private Values mValues = null;
									        								
	/**
	 * constractor
	 * @param Context context
	 */ 
	public InputDeviceManager( Context context ) {
		mInputManager = (InputManager) context.getSystemService( Context.INPUT_SERVICE ) ; 
	}

	/**
	 * register
	 */ 
    public void register() {
		mInputManager.registerInputDeviceListener( mInputDeviceListener, new Handler() );
   }

	/**
	 * unregister
	 */ 
    public void unregister() {
		mInputManager.unregisterInputDeviceListener( mInputDeviceListener );
   }

	/**
	 * getInputDevices
	 */ 
	public String getInputDevices() {
    	String str = "";
		int[] ids = mInputManager.getInputDeviceIds();
        for ( int i = 0; i < ids.length; i++ ) {
            str += addInputDevice( ids[i] );
        }
        log_d( str );
        return str;
    }
	
// --- dispatchMotionEvent ---
	/**
	 * dispatchMotionEvent
	 * @param MotionEvent event
	 * @return int
	 */			 							                
	public boolean dispatchMotionEvent( MotionEvent event ) {
		log_d( event.toString() );
		if ( !isMouse( event.getSource() ) ) return false;
		setValues( event );
		return true;
	}

	/**
	 * setValues
	 * @param MotionEvent event
	 */	
	private void setValues( MotionEvent event ) {
		float x = event.getX();
		float y = event.getY();
		float vscroll = 0;
		boolean primary = false;
		boolean secondary = false;
		boolean tertiary = false;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_SCROLL:
				vscroll = event.getAxisValue( MotionEvent.AXIS_VSCROLL );
				break;
			case MotionEvent.ACTION_HOVER_EXIT:
				switch ( event.getButtonState() ) {
					case MotionEvent.BUTTON_PRIMARY:
						primary = true;
						break;						
					case MotionEvent.BUTTON_SECONDARY:
						secondary = true;
						break;	
					case MotionEvent.BUTTON_TERTIARY:
						tertiary = true;
						break;	
				}
				break;
		}
		mValues	= new Values( x, y, vscroll, primary, secondary, tertiary );	
	}

	/**
	 * getValues
	 * @return Values
	 */	
	public Values getValues() {
		return mValues;
	}

	/**
	 * isMouse
	 * @param int source
	 * @return boolean
	 */	
	public boolean isMouse( int source ) {
		return isSource( source, InputDevice.SOURCE_MOUSE );
  }

	/**
	 * isSource
	 * @param int source
	 * @param int kind
	 * @return boolean
	 */	
	private boolean isSource( int source, int kind ) {
        return ( source & kind & ~InputDevice.SOURCE_CLASS_MASK ) != 0;
    }
	           
	/**
	 * setOnInputDeviceListener
	 * @param OnInputDeviceListener listener
	 */
    public void setOnInputDeviceListener( OnInputDeviceListener listener ) {
        mOnInputDeviceListener = listener;
    }

// ==== InputDeviceListener ====
	/**
	 * InputDeviceListener
	 */    
	private InputDeviceListener mInputDeviceListener = new InputDeviceListener() {
        @Override
        public void onInputDeviceAdded( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceAdded( deviceId );
        	}
        }
        @Override
        public void onInputDeviceRemoved( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceRemoved( deviceId );
        	}
        }
        @Override
        public void onInputDeviceChanged( int deviceId ) {
       		if ( mOnInputDeviceListener != null ) {
        		mOnInputDeviceListener.onInputDeviceChanged( deviceId );
        	}
        }
    };  

 // --- addInputDevice ---
 	/**
 	 * addInputDevice
 	 * @param int deviceId
 	 * @return String device name
 	 */
 	public String addInputDevice( int deviceId ) {    
     	String msg = "";
 		InputDevice device = mInputManager.getInputDevice( deviceId );
 		if ( device == null ) return "";
 		msg += device.toString() + LF;
 		if ( isMouse( device.getSources() ) ) {
             if ( !isDeviceMatch( device ) ) {
         		mInputDevice = device;
             }    
 		}
 		log_d( msg );
 		return msg;
     }

 	/**
 	 * isDeviceMatch
 	 * @param InputDevice device
 	 * @return  boolean
 	 */
 	private boolean isDeviceMatch( InputDevice device ) {
 		if (( mInputDevice != null )&&( mInputDevice.getId() != device.getId() )) {
 			return true;
 		}
 		return false;
     }

 	/**
 	 * getDeviceName
 	 * @return String
 	 */
 	private String getDeviceName() {
 		String str = "";
 		if ( mInputDevice != null ) {
 			str = mInputDevice.getName();
 		}
 		return str;
     }


 // --- removeInputDevice ---
 	/**
 	 * removeInputDevice
 	 * @param int deviceId
 	 * @return String device name
 	 */		 	
     public String removeInputDevice( int deviceId ) {
     	String str = "";
     	InputDevice device = mInputManager.getInputDevice( deviceId );
 		if ( device == null ) return "";
		 if ( isDeviceMatch( device ) ) {
			str = getDeviceName();
			mInputDevice = null;
		}    
 		return str;
     }

// === class Values ===
	public class Values {
		public float x = 0;
		public float y = 0;
		public float vscroll = 0;
		public boolean primary = false;
		public boolean secondary = false;
		public boolean tertiary = false;
	
		public Values( float _x, float _y, float _vscroll, boolean _primary, boolean _secondary, boolean _tertiary ) {
			x = _x;
			y = _y;
			vscroll = _vscroll;
			primary = _primary;
			secondary = _secondary;
			tertiary = _tertiary;
		}	
	}

	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + " " + msg );
	}

}
