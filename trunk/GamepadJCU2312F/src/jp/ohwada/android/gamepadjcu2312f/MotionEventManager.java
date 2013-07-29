package jp.ohwada.android.gamepadjcu2312f;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.MotionEvent;

/**
 * MotionEventManager
 */ 
public class MotionEventManager {
	
	private static final boolean D = true;
    private static final String TAG = "MotionEventManager"; 

	private static final String LF = "\n";

	private static final int STATUS_NONE = 0;
	private static final int STATUS_DETECTED = 1;

    private InputDevice mInputDevice; 
	private int[] mAxes;
	private List<Pos> mPosList;
	private SparseArray<Float> mSparseAxis = new SparseArray<Float>(); 

	private int mFirstMoveStatus = STATUS_NONE;	
	private boolean isDetectValue = false;
	private boolean isDetectZero = false;

	private int mFirstMoveAxis = 0;
	private float mFirstMoveAxisValue = 0;
        								
	/**
	 * constractor
	 */ 
	public MotionEventManager() {
		// dummy
	}

// --- execGenericMotionEvent ---
	/**
	 * execGenericMotionEvent
	 * @param MotionEvent e
	 * @return boolean
	 */			 							                
	public boolean execGenericMotionEvent( MotionEvent event ) {
		log_d( event.toString() );		

		if ( !isJoystickClass( event.getSource() ) ) return false;
		if ( event.getAction() != MotionEvent.ACTION_MOVE ) return false;
		boolean ret = setJoystickDevice( event );
		if ( !ret )  return false;	
		if (D) trcaeMotionEvent( event );
		procJoystickMotion( event );
		return true;
	}

	/**
	 * execGenericMotionEventHistory
	 * @param MotionEvent event
	 * @return boolean
	 */
	public boolean execGenericMotionEventHistory( MotionEvent event ) {
		log_d( event.toString() );

		if ( !isJoystickClass( event.getSource() ) ) return false;
		if ( event.getAction() != MotionEvent.ACTION_MOVE ) return false;	
		boolean ret = setJoystickDevice( event );
		if ( !ret )  return false;
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
	 * @return float
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
	 * getSparseAxis
	 * @return SparseArray<Float>
	 */ 
	public SparseArray<Float> getSparseAxis() {
		return mSparseAxis;
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
    	if ( mAxes == null ) return;
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
		if ( mAxes == null ) return;
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
	private boolean isJoystickClass( int source ) {
		return isSourceClass( source, InputDevice.SOURCE_CLASS_JOYSTICK );
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

// setJoystickDevice
	/**
	 * setJoystickDevice
	 * @param MotionEvent event
	 * @return int
	 */
	private boolean setJoystickDevice( MotionEvent event ) {
		InputDevice device = event.getDevice();
		if ( device == null ) return false; 
		if ( isDeviceMatch( device ) ) return true;
		setJoystickDevice( device );
		return true;       
	}

	/**
	 * setJoystickDevice
	 * @param InputDevice device
	 * @return String
	 */    
	private void setJoystickDevice( InputDevice device ) {
		mInputDevice = device;
		List<Integer> list = new ArrayList<Integer>();
		List<MotionRange> ranges = device.getMotionRanges();
		for ( MotionRange range : ranges ) {
			int source = range.getSource();
			int axis = range.getAxis();
			if ( isJoystickClass( source ) ) {
				list.add( axis );
			}
		}
		int size = list.size();
        mAxes = new int[ size ];		
		for ( int i=0; i< size; i++ ) {
		    mAxes[ i ] = list.get( i );
		}
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
		if (D) Log.d( TAG, msg );
	}

}
