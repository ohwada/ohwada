package jp.ohwada.android.inputmanagersample3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.KeyboardLayout;
import android.util.Log;
import android.view.InputDevice;

/**
 * MyInputManager
 */
public class MyInputManager {

	private static final String LF = "\n";

	private InputManager mInputManager;
	private Class<? extends InputManager> mClass;

	/**
	 * constractor
	 * @param Context context
	 */			
	public MyInputManager( Context context ) {
		mInputManager = (InputManager) context.getSystemService( Context.INPUT_SERVICE ) ;
		mClass = mInputManager.getClass();
	}

	/**
	 * getDeclaredMethods
	 * execute hide method using reflection
	 * @return String
	 */	
	public String getDeclaredMethods() {
		String str = "";
 	    Method[] methods = mClass.getDeclaredMethods();
    	for ( Method method : methods ) {
    		Log.d( "", method.getName() );
   			str+= method.getName() + LF;
		}
   		str+= LF;		
   		return str;
	}

    /**
     * Gets information about all supported keyboard layouts.
     * <p>
     * The input manager consults the built-in keyboard layouts as well
     * as all keyboard layouts advertised by applications using a
     * {@link #ACTION_QUERY_KEYBOARD_LAYOUTS} broadcast receiver.
     * </p>
	 * execute hide method using reflection
     *
     * @return A list of all supported keyboard layouts.
	 */    
	public KeyboardLayout[] getKeyboardLayouts() {
		KeyboardLayout kls[] = null;
		try {
			Method method = getMethod( "getKeyboardLayouts" );
			if ( method != null ) {
				kls = (KeyboardLayout[]) method.invoke( mInputManager );
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return kls;
	}

    /**
     * Gets the keyboard layout with the specified descriptor.
	 * execute hide method using reflection
     *
     * @param keyboardLayoutDescriptor The keyboard layout descriptor, as returned by
     * {@link KeyboardLayout#getDescriptor()}.
     * @return The keyboard layout, or null if it could not be loaded.
     *
     */
    public KeyboardLayout getKeyboardLayout( String keyboardLayoutDescriptor ) {
		KeyboardLayout kl = null;
		try {
//			Method method = getMethod( "getKeyboardLayout" );
			Method method = getMethod2( "getKeyboardLayout" );
			if ( method != null ) {
				kl = (KeyboardLayout) method.invoke( mInputManager, keyboardLayoutDescriptor );
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return kl;
    }

    /**
     * Gets the current keyboard layout descriptor for the specified input device.
	 * execute hide method using reflection
     *
     * @param inputDeviceDescriptor The input device descriptor.
     * @return The keyboard layout descriptor, or null if no keyboard layout has been set.
     */
    public String getCurrentKeyboardLayoutForInputDevice( String inputDeviceDescriptor ) {
		 String str = "";
		try {
// cannot get method with 	getMethod()	
//			Method method = getMethod( "getCurrentKeyboardLayoutForInputDevice" );
			Method method = getMethod2( "getCurrentKeyboardLayoutForInputDevice" );
			if ( method != null ) {
				str = (String) method.invoke( mInputManager, inputDeviceDescriptor ); 
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return str;
    }

    /**
     * Gets all keyboard layout descriptors that are enabled for the specified input device.
	 * execute hide method using reflection
     *
     * @param inputDeviceDescriptor The input device descriptor.
     * @return The keyboard layout descriptors.
     */
    public String[] getKeyboardLayoutsForInputDevice( String inputDeviceDescriptor ) {
		String kls[] = null;
		try {
//			Method method = getMethod( "getKeyboardLayoutsForInputDevice" );
			Method method = getMethod2( "getKeyboardLayoutsForInputDevice" );
			if ( method != null ) {
				kls = (String[]) method.invoke( mInputManager, inputDeviceDescriptor ); 
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return kls;
    }

    /**
     * getMethod
     * @param String name
     * @return Method
     */
	private Method getMethod( String name ) {
		Method method = null;
		try {
			method = mClass.getMethod( name );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method;	
	}

    /**
     * getMethod using getDeclaredMethods
     * @param String name
     * @return Method
     */
	private Method getMethod2( String name ) {
 	    Method[] methods = mClass.getDeclaredMethods();
    	for ( Method method : methods ) {
   			if ( method.getName().equals( name ) ) {
   				return method;
   			} 
		}	
   		return null;
	}

    /**
     * getExternalKeyboardList
     * @return List<InputDevice>
     */
	public List<InputDevice> getExternalKeyboardList() {
		List<InputDevice> list = new ArrayList<InputDevice>();
        int[] ids = mInputManager.getInputDeviceIds();
        for ( int i = 0; i < ids.length; i++ ) {
        	int id = ids[ i ];
        	InputDevice device = mInputManager.getInputDevice( id );
        	if ( device != null ) {
				MyInputDevice myDevice = new MyInputDevice( device );
        		if ( myDevice.isExternalKeyboard() ) {
        			list.add( device );
        		}
        	}
        }
        return list;
	}

    /**
     * getKeyboardLabel
     * @param String inputDeviceDescriptor
     * @return String
     */
	public String getKeyboardLabel( String inputDeviceDescriptor ) {
		String label = "";
		String keyboardLayoutDescriptor =  getCurrentKeyboardLayoutForInputDevice( inputDeviceDescriptor );
		KeyboardLayout kl = getKeyboardLayout( keyboardLayoutDescriptor );
		if ( kl != null ) {
			label = kl.getLabel();
		}
		return label;
	}

    /**
     * boardLayoutsToString
     * @param KeyboardLayout kls[]
     * @return String
     */				    
	public String boardLayoutsToString( KeyboardLayout kls[] ) {
		String str = "";	
		if ( kls == null ) return null;
    	for ( KeyboardLayout kl : kls ) {
    		str += kl.getLabel() + LF;
    	}
		str += LF;
    	return str;
	}
            
}
