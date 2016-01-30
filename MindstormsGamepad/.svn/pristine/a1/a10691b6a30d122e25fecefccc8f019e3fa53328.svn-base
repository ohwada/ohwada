package jp.ohwada.android.mindstormsgamepad.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * InputDeviceManager
 */ 
public class InputDeviceManager {
	
	private static final boolean D = true;
    private static final String TAG = "GamePad"; 
    private static final String TAG_SUB = "InputDeviceManager";

	private static final String LF = "\n";

	private static final int STATUS_NONE = 0;
	private static final int STATUS_DETECTED = 1;
								
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
	private int[] mAxes;
	private List<Pos> mPosList;
	private SparseArray<Float> mSparseAxis = new SparseArray<Float>(); 
	private SparseBooleanArray mSparseCode = new SparseBooleanArray();
	private boolean isFirstDown = false;

	private int mFirstMoveStatus = STATUS_NONE;	
	private boolean isDetectValue = false;
	private boolean isDetectZero = false;

	private int mFirstDownCode = 0;
	private int mFirstMoveAxis = 0;
	private float mFirstMoveAxisValue = 0;
        								
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

// --- dispatchKeyEvent ---
	/**
	 * dispatchKeyEvent
	 * @param KeyEvent event
	 * @return boolean
	 */ 
	public boolean dispatchKeyEvent( KeyEvent event ) {
		log_d( event.toString() );

		int source = event.getSource();
		if ( isJoystick( source ) || isGamepad( source ) ) {
			processKeyEvent( event );
			return true;
		}
		return false;
	}

	/**
	 * getSparseCode
	 * @return SparseBooleanArray 	 
	 */	
	public SparseBooleanArray getSparseCode() {
		return mSparseCode;
	}

	/**
	 * isFirstDown
	 * @return boolean
	 */
	public boolean isFirstDown() {
		return isFirstDown;
	}
	
	/**
	 * getFirstDownCode
	 * @return int 
	 */
	public int getFirstDownCode() {
		return mFirstDownCode;
	}
			
	/**
	 * processKeyEvent
	 * @param KeyEvent e
	 */ 		
	private void processKeyEvent( KeyEvent event ) {
		int code = event.getKeyCode();
		isFirstDown = false;
		switch ( event.getAction()  ) {
			case KeyEvent.ACTION_DOWN :
				mSparseCode.put( code, true );
				if ( event.getRepeatCount() == 0 ) {
					isFirstDown = true;
					mFirstDownCode = code;
				}
				break;
			case KeyEvent.ACTION_UP :
				mSparseCode.put( code, false );
				break;
		}		
	}

// --- dispatchMotionEvent ---
	/**
	 * dispatchMotionEvent
	 * @param MotionEvent e
	 * @return int
	 */			 							                
	public boolean dispatchMotionEvent( MotionEvent event ) {
		log_d( event.toString() );
		if ( !isJoystickClass( event.getSource() ) ) return false;
		if ( event.getAction() != MotionEvent.ACTION_MOVE ) return false;	
		if (D) trcaeMotionEvent( event );
		procJoystickMotion( event );
		return true;
	}

	/**
	 * dispatchMotionEventHistory
	 * @param MotionEvent e
	 * @return int
	 */
	public boolean dispatchMotionEventHistory( MotionEvent event ) {
		log_d( event.toString() );
		if ( !isJoystickClass( event.getSource() ) ) return false;
		if ( event.getAction() != MotionEvent.ACTION_MOVE ) return false;	
		if (D) trcaeMotionEvent( event );
		procJoystickMotionHistory( event );
		return true;
	}

	/**
	 * clearFirstMove
	 */
	public void clearFirstMove() {
		mFirstMoveStatus = STATUS_NONE;
		isDetectValue = false;
		isDetectZero = false;
		mFirstMoveAxis = -1;
	}

	/**
	 * isFirstMove
	 * mFirstMoveStatus will be changeed when this method is called,
	 * @return boolean
	 */
	public boolean isFirstMove() {
	    if ( mFirstMoveStatus == STATUS_NONE ) {
	   		if ( isDetectValue ) {
	    		mFirstMoveStatus = STATUS_DETECTED ;
				return true;
			}	
	    } else if ( mFirstMoveStatus == STATUS_DETECTED ) {
	    	if ( isDetectZero ) {
				clearFirstMove();
				mFirstMoveStatus = STATUS_NONE;
	    	}
		}
	    return false;
	}

	/**
	 * getPosList
	 * @return List<Pos>
	 */	
	public List<Pos> getPosList() {
		return mPosList ;
	}
		
	/**
	 * getFirstMoveAxis
	 * @return int 
	 */
	public int getFirstMoveAxis() {
		return mFirstMoveAxis;
	}

	/**
	 * getFirstMoveAxisValue
	 * @return float
	 */
	public float getFirstMoveAxisValue() {
		return mFirstMoveAxisValue;
	}

 	/**
	 * getAxisLable
	 * @param int axis 
	 * @param boolean sign
	 * @return String
	 */ 
	public String getAxisLable( int axis, boolean sign ) {
		String str = "";
		if ( sign ) {
			str = "+ ";
		} else {
			str = "- ";
		}
		str += MotionEvent.axisToString( axis );
		return str;
	}

 	/**
	 * getAxisValue
	 * @param int axis
	 * @param float value 
	 */ 
	public float getAxisValue( int axis, boolean sign ) {
		if (( mSparseAxis == null )||( mSparseAxis.size() == 0 )) return 0;
		float value = mSparseAxis.get( axis );
		if ( sign ) {
			value = - value;
		}
		return value;
	}

	/**
	 * procJoystickMotionHistory
	 * @param MotionEven event
	 */	
	private void procJoystickMotionHistory( MotionEvent event ) {
		mPosList = new ArrayList<Pos>();		
		// Process all historical movement samples in the batch.
		int size = event.getHistorySize();
		for ( int i = 0; i < size; i++ ) {
			procJoystickMotion( event, i );
		}
		// Process the current movement sample in the batch.
		procJoystickMotion( event, -1 );
	}

	/**
	 * procJoystickMotion
	 * @param MotionEven event
	 */			
    private void procJoystickMotion( MotionEvent event ) {
    	for ( int i=0; i < mAxes.length; i++ ) {
    		int axis = mAxes[ i ];
        	float value = getCenteredAxis( event, axis );
        	float abs = Math.abs( value );
        	log_d( i + " " + axis + " " + value  );
        	mSparseAxis.put( axis, value );
        	if ( mFirstMoveStatus == STATUS_NONE ) {
        		if ( abs > 0.9 ) {
        			isDetectValue = true;
        			mFirstMoveAxis = axis;
        			mFirstMoveAxisValue = value;
        		}
        	} else if ( mFirstMoveStatus == STATUS_DETECTED ) {
        		if (( axis == mFirstMoveAxis )&&( abs < 0.1 )) {
        			isDetectZero = true;
        		}         			
        	}
    	}
    }

	/**
	 * procJoystickMotion
	 * @param MotionEven event
	 * @param int historyPos
	 */			
    private void procJoystickMotion( MotionEvent event, int historyPos ) {
        float x = getCenteredAxis( event, MotionEvent.AXIS_X, historyPos );
        if (x == 0) {
            x = getCenteredAxis( event, MotionEvent.AXIS_HAT_X, historyPos );
        }

        float y = getCenteredAxis( event, MotionEvent.AXIS_Y, historyPos );
        if (y == 0) {
            y = getCenteredAxis( event, MotionEvent.AXIS_HAT_Y, historyPos );
        }

        float z = getCenteredAxis( event, MotionEvent.AXIS_Z, historyPos );
        float rz = getCenteredAxis( event, MotionEvent.AXIS_RZ, historyPos );
	
		long step = historyPos < 0 ? event.getEventTime() :event.getHistoricalEventTime( historyPos );
		mPosList.add( new Pos( x, y, z, rz, step ) );	
    }

	/**
	 * getCenteredAxis
	 * @param MotionEvent event
	 * @param int axis
	 * @return float
	 */	
    private float getCenteredAxis( MotionEvent event, int axis ) {
    	return getCenteredAxis( event, axis, -1 );
    }

	/**
	 * getCenteredAxis
	 * @param MotionEvent event
	 * @param int axis
	 * @param int historyPos
	 * @return float
	 */	
    private float getCenteredAxis( MotionEvent event, int axis, int historyPos ) {
    	if ( mInputDevice == null ) return 0;
        InputDevice.MotionRange range = mInputDevice.getMotionRange( axis, event.getSource() );
        if ( range != null ) {
            float flat = range.getFlat();
            float value = historyPos < 0 ? 
            	event.getAxisValue( axis ) : 
            	event.getHistoricalAxisValue( axis, historyPos );
            // Ignore axis values that are within the 'flat' region of the joystick axis center.
            // A joystick at rest does not always report an absolute position of (0,0).
            if ( Math.abs( value ) > flat)  {
                return value;
            }
        }
        return 0;
    }

	/**
	 * trcaeMotionEvent
	 * @param MotionEvent e
	 */			 							                
	private void trcaeMotionEvent( MotionEvent event ) {
		String msg = "";
		int size = event.getHistorySize();
		for ( int i = 0; i < mAxes.length; i++ ) {
			int axis = mAxes[ i ];
			float value = event.getAxisValue( axis );
			msg += MotionEvent.axisToString( axis ) + " " + value + LF;
			if ( size > 0 ) {
				msg += "HistoricalAxisValue" + LF;
				for ( int j = 0; j < size; j++ ) {
					msg += event.getHistoricalAxisValue( axis, j ) + LF;
				}
			}
		}
		log_d( msg );
	}

	/**
	 * isJoystickClass
	 * @param int source
	 * @return boolean
	 */	
	public boolean isJoystickClass( int source ) {
		return isSourceClass( source, InputDevice.SOURCE_CLASS_JOYSTICK );
    }

	/**
	 * isGamepadKeyCode
	 * @param int source
	 * @return boolean
	 */	
	public boolean isGamepadKeyCode( int source ) {
		boolean ret = isJoystick( source ) || isGamepad( source );
		return ret;
	}
		
	/**
	 * isJoystick
	 * @param int source
	 * @return boolean
	 */	
	private boolean isJoystick( int source ) {
		return isSource( source, InputDevice.SOURCE_JOYSTICK );
    }

	/**
	 * isGamepad
	 * @param int source
	 * @return boolean
	 */	
	private boolean isGamepad( int source ) {
		return isSource( source, InputDevice.SOURCE_GAMEPAD );
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
	 * isSourceClass
	 * @param int source
	 * @param int kind
	 * @return boolean
	 */	
	private boolean isSourceClass( int source, int kind ) {
        return ( source & kind & InputDevice.SOURCE_CLASS_MASK ) != 0;
    }

	/**
	 * getSourceString
	 * @param int source
	 * @return String
	 */	
    private String getSourceString( int source ) {
		String str = "source: 0x" + String.format( "%08X", source ) + " " ;
		return str;
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
		if ( isJoystickClass( device.getSources() ) ) {
            if ( !isDeviceMatch( device ) ) {
                msg += setJoystickDevice( device );
            }    
		}
		log_d( msg );
		return device.getName();
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

	/**
	 * setJoystickDevice
	 * @param InputDevice device
	 * @return String
	 */    
	private String setJoystickDevice( InputDevice device ) {
		String str = "";
		mInputDevice = device;
		List<Integer> list = new ArrayList<Integer>();

		List<MotionRange> ranges = device.getMotionRanges();
		for ( MotionRange range : ranges ) {
			int source = range.getSource();
			int axis = range.getAxis();
			str += getSourceString( source ) + LF;
			str += "axis: " + MotionEvent.axisToString( axis ) + LF;
			if ( isJoystickClass( source ) ) {
				list.add( axis );
			}
		}

		int size = list.size();
        mAxes = new int[ size ];		
		for ( int i=0; i< size; i++ ) {
		    mAxes[ i ] = list.get( i );
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
		if ( isJoystickClass( device.getSources() ) ) {
            if ( isDeviceMatch( device ) ) {
            	str = getDeviceName();
                mInputDevice = null;
            }    
		}
		return str;
    }

// === class Pos ===
	public class Pos {
		public float x = 0;
		public float y = 0;	
		public float z = 0;	
		public float rz = 0;	
		public long step = 0;	
	
		public Pos( float _x, float _y, float _z, float _rz, long _s ) {
			x = _x;
			y = _y;
			z = _z;
			rz = _rz;
			step = _s;
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
