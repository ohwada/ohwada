package jp.ohwada.android.satellitetracking1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.util.Log;

/**
 * CommonGLSurfaceView
 */
public class CommonGLSurfaceView extends GLSurfaceView 
	implements GLSurfaceView.Renderer{

	private final static boolean D = true;
	protected String TAG  = "CommonGLSurfaceView";

	protected final static double DEG_TO_RAD = Math.PI / 180;
	protected final static double RAD_TO_DEG = 180 / Math.PI;
	protected final static double TWO_PI = 2 * Math.PI;
		
	// fixied point
	private final static int ONE = 0x10000;	
	private final static int DOT_ONE = (int)(ONE * 0.1f);
 
  	private final static int NUM_POINT_VERTEX = 3;
   	private final static int NUM_POINT_COLOR = 4;
 	private final static int NUM_LINE_VERTEX = 2 * NUM_POINT_VERTEX;
 	private final static int NUM_LINE_COLOR = 2 * NUM_POINT_COLOR;
	
	private double mEyeX = 0;
	private double mEyeY = 0;
	private double mEyeZ = 0;
	private double mEyeCoRadius = 0;
	private double mEyeCoTheta = 0;
	private double mEyeCoPhi = 0;
	
    /** 
	 * === constructor ===
	 */		
	public CommonGLSurfaceView( Context context ){
		super( context );
	}

    /** 
	 * === constractor ===
	 */	 
	public CommonGLSurfaceView( Context context, AttributeSet attrs ){
		super( context, attrs );
	}
    
    /** 
	 * === onSurfaceCreated ===
	 */	
    public void onSurfaceCreated( GL10 gl, EGLConfig config ){
    	gl.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );  // white
    	gl.glEnable( GL10.GL_DEPTH_TEST );    	
    	gl.glShadeModel( GL10.GL_FLAT );
    	gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
    	gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
    }

    /** 
	 * === onSurfaceChanged ===
	 */	    
    public void onSurfaceChanged( GL10 gl, int width, int height ) {
    	float ratio = (float) width / height;
    	gl.glViewport( 0, 0, width, height );
    	gl.glMatrixMode( GL10.GL_PROJECTION );
    	gl.glLoadIdentity();
    	float F_BOTTOM = -1.0f;
    	float F_TOP = 1.0f;
    	float F_Z_NEAR = 1.0f;
    	float F_Z_FAR = 10.0f;
    	gl.glFrustumf( -ratio, ratio, F_BOTTOM, F_TOP, F_Z_NEAR, F_Z_FAR );
    }

    /** 
	 * === onDrawFrame ===
	 */	 
	public void onDrawFrame( GL10 gl ){
		// dummy
	}

    /** 
	 * glClear
	 */
    protected void glClear( GL10 gl ) {
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
	}

    /** 
	 * drawLine
	 */	
    protected void drawLine( GL10 gl, float size, IntBuffer vertex, IntBuffer color, int count ) {
		gl.glPointSize( size );	
		gl.glVertexPointer( 3, GL10.GL_FIXED, 0, vertex );
		gl.glColorPointer( 4, GL10.GL_FIXED, 0, color );
    	gl.glDrawArrays( GL10.GL_LINES, 0, 2*count );
    }

    /** 
	 * drawPoint
	 */	
    protected void drawPoint( GL10 gl, float size, IntBuffer vertex, IntBuffer color, int count ) {
    	gl.glPointSize( size );
		gl.glVertexPointer( 3, GL10.GL_FIXED, 0, vertex );
		gl.glColorPointer( 4, GL10.GL_FIXED, 0, color );	
    	gl.glDrawArrays( GL10.GL_POINTS, 0, count );
	}
	    		    	
    /** 
	 * initEye
	 */	
    protected void initEye( double x, double y, double z ) {
		mEyeCoRadius = Spherical.calcRadius( x, y, z );
		mEyeCoTheta = Spherical.calcTheta( x, y, z );
		mEyeCoPhi = Spherical.calcPhi( x ,y );
    	mEyeX = Spherical.calcX( mEyeCoRadius, mEyeCoTheta, mEyeCoPhi );
    	mEyeY = Spherical.calcY( mEyeCoRadius, mEyeCoTheta, mEyeCoPhi );   
    	mEyeZ = Spherical.calcZ( mEyeCoRadius, mEyeCoTheta ); 
	}

    /** 
	 * gluLookAt
	 */
    protected void gluLookAt( GL10 gl ) {
		// center of view
		float LOOK_CENTER_X = 0;
		float LOOK_CENTER_Y = 0;
		float LOOK_CENTER_Z = 0;
		// up vector
		float LOOK_UP_X = 0;
		float LOOK_UP_Y = 0;
		float LOOK_UP_Z = 1;
		GLU.gluLookAt( 
			gl, 
			(float)mEyeX, (float)mEyeY, (float)mEyeZ,
			LOOK_CENTER_X, LOOK_CENTER_Y, LOOK_CENTER_Z,	
			LOOK_UP_X, LOOK_UP_Y, LOOK_UP_Z );
	}

    /** 
	 * getEyeTheta
	 */	
    protected double getEyeTheta() {
    	return mEyeCoTheta;
    }

    /** 
	 * getEyePhi
	 */	
    protected double getEyePhi() {
    	return mEyeCoPhi;
    }

    /** 
	 * setEyeTheta
	 */	
    protected void setEyeTheta( double p ) {
    	mEyeCoTheta = p;
    }
    
    /** 
	 * setEyePhi
	 */	
    protected void setEyePhi( double p ) {
    	if( p < - TWO_PI ){
    		p += TWO_PI;
    	} else if( p > TWO_PI ){
    		p -= TWO_PI;
    	}
    	mEyeCoPhi = p;
    }

    /** 
	 * calcEye
	 */	    	
	protected void calcEye() {
		mEyeX = Spherical.calcX( mEyeCoRadius, mEyeCoTheta, mEyeCoPhi );
		mEyeY = Spherical.calcY( mEyeCoRadius, mEyeCoTheta, mEyeCoPhi ); 
		mEyeZ = Spherical.calcZ( mEyeCoRadius, mEyeCoTheta );   
	}

    /** 
	 * buildTargetColor
	 */	
	protected int[] buildTargetColor( float x, float y, float z ) {
	    int[] c = null; 
    	if ( x >= 0 ) {
    		if ( y >= 0 ) {
    			if ( z >= 0 ) {
    				// black
    				c = buldPointColor( 0, 0, 0 ); 
    			} else {
   					// Yellow
    				c = buldPointColor( 0.9f, 0.9f, 0 ); 
    			}
    		} else {
    		    if ( z >= 0 ) {
    				// Magenta
    				c = buldPointColor( 1, 0, 1 ); 
    			} else {
   					// red
    				c = buldPointColor( 0.8f, 0, 0 ); 
    			}
    		}
    	} else {
     		if ( y >= 0 ) {
    		    if ( z >= 0 ) {
    				// Cyan
    				c = buldPointColor( 0, 1, 1 ); 
    			} else {
   					// grenn
    				c = buldPointColor( 0, 1, 0 ); 
    			}
    		} else {
    		    if ( z >= 0 ) {
    				// blue
    				c = buldPointColor( 0, 0, 1 ); 
    			} else {
    				// grey
    				c = buldPointColor( 0.5f, 0.5f, 0.5f ); 
    			}
    		}
    	}
    	return c;	
    }
	
    /** 
     * buldPointVertex
     */	
    protected int[] buldPointVertex( float x, float y, float z ) {
   		int[] v = new int[ NUM_POINT_VERTEX ];
  		v[ 0 ] = (int)( x * DOT_ONE );
    	v[ 1 ] = (int)( y * DOT_ONE );
    	v[ 2 ] = (int)( z * DOT_ONE );
    	return v;
    }

    /** 
     * buldLineVertex
     */	    	
    protected int[] buldLineVertex( float x0, float y0, float z0, float x1, float y1, float z1 ) {
   		int[] v = new int[ NUM_LINE_VERTEX ];
   		int[] p0 = buldPointVertex( x0, y0, z0 );
   		int[] p1 = buldPointVertex( x1, y1, z1 );
   		for ( int i=0; i<NUM_POINT_VERTEX; i++ ) {
   			v[  i ] = p0[ i ];
   			v[ NUM_POINT_VERTEX + i ] = p1[ i ];
   		}
    		return v;
    }

    /** 
     * buldPointColorBuffer
     */	 
    protected IntBuffer buldPointColorBuffer( float r, float g, float b ) {
		return getIntBuffer( buldPointColor( 1.0f, 0, 0 ) ); 
	}
	
    /** 
     * buldPointColor
     */	 
    protected int[] buldPointColor( float r, float g, float b ) {
   		int[] v = new int[ NUM_POINT_COLOR ];
  		v[ 0 ] = (int)( r * ONE );
    		v[ 1 ] = (int)( g * ONE );
    		v[ 2 ] = (int)( b * ONE );
    		v[ 3 ] = ONE;
    		return v;
    }

    /** 
     * buldLineColor
     */	 
    protected int[] buldLineColor( float r, float g, float b ) {
   		int[] v = new int[ NUM_LINE_COLOR ];
   		int[] c = buldPointColor( r, g, b );
   		for ( int i=0; i<NUM_POINT_COLOR; i++ ) {
   			v[  i ] = c[ i ];
   			v[ NUM_POINT_COLOR + i ] = c[ i ];
   		} 
     	return v;  		
    }
 
    /** 
	 * getIntBuffer
	 */	
    protected static IntBuffer getIntBuffer( int[] table ) {
        ByteBuffer bb = ByteBuffer.allocateDirect( 4 * table.length );
        bb.order( ByteOrder.nativeOrder() );
        IntBuffer ib = bb.asIntBuffer();	
        ib.put( table );
        ib.position( 0 );
        return ib;
    }

	/**
	 * sleep
	 */	
	protected void sleep( long time ) {
		try{
    		Thread.sleep( time );
    	} catch ( InterruptedException e ){
    		if (D) e.printStackTrace();
    	}
    }
    	
	/**
	 * log_d
	 */	
	protected void log_d( String str ) {
		if (D) Log.d( TAG, str );
	}

}
