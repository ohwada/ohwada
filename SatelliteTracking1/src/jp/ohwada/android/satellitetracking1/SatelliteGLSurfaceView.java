package jp.ohwada.android.satellitetracking1;

import java.nio.IntBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.AttributeSet;

/**
 * SatelliteGLSurfaceView
 */
public class SatelliteGLSurfaceView extends CommonGLSurfaceView {

 	private final static double DIV_PHI = 10 * DEG_TO_RAD;
 
  	private final static int NUM_POINT_VERTEX = 3;
   	private final static int NUM_POINT_COLOR = 4;
 	private final static int NUM_LINE_VERTEX = 2 * NUM_POINT_VERTEX;
 	private final static int NUM_LINE_COLOR = 2 * NUM_POINT_COLOR;

 	private final static float POINT_SIZE_LINE = 8f;
 	private final static float POINT_SIZE_POINT = 8f;

	private final static int LINE_NUM = 6;	
	private int mPointNum = 0;

	private IntBuffer mVertexBuff;
	private IntBuffer mColorBuff;
	private IntBuffer mCurrentColorBuff;
	private IntBuffer mLineVertexBuff;
	private IntBuffer mLineColorBuff;

	private int mCurrentNum = 0;
	private int mPointer = 0;
	private int[] mVertexPoint = null;
	
    /** 
	 * === constructor ===
	 */		
	public SatelliteGLSurfaceView( Context context ){
		super( context );
		initView( context );
	}

    /** 
	 * === constractor ===
	 */	 
	public SatelliteGLSurfaceView( Context context, AttributeSet attrs ){
		super( context, attrs );
		initView( context );	
	}

    /** 
	 * initView
	 */	 
	private void initView( Context context ){
		TAG  = "SatelliteGLSurfaceView";
		setRenderer( this );		
		initEye();
		initLineVertex();
		initLineColor();
    	mCurrentColorBuff = buldPointColorBuffer( 1, 0, 0 ); 	// red   
	}	

    /** 
	 * initEye
	 */	
	public void initEye() {
		float EYE_LEN = 2;
		initEye( -EYE_LEN, EYE_LEN, EYE_LEN );
	}

    /** 
	 * initLineVertex
	 */	 
    private void initLineVertex() {
		int LINE_LEN = 30;	
		int[] vertex = new int[ LINE_NUM * NUM_LINE_VERTEX ];
		int[][] v = new int[ LINE_NUM ][ NUM_LINE_VERTEX ];
		v[ 0 ] = buldLineVertex( 0, 0, 0, LINE_LEN, 0, 0 );
		v[ 1 ] = buldLineVertex( 0, 0, 0, -LINE_LEN, 0, 0 );
		v[ 2 ] = buldLineVertex( 0, 0, 0, 0, LINE_LEN, 0 );
		v[ 3 ] = buldLineVertex( 0, 0, 0, 0, -LINE_LEN, 0 );
		v[ 4 ] = buldLineVertex( 0, 0, 0, 0, 0, LINE_LEN );
		v[ 5 ] = buldLineVertex( 0, 0, 0, 0, 0, -LINE_LEN );						
     	for ( int i = 0; i < LINE_NUM; i++ ) {
    		for ( int j = 0; j < NUM_LINE_VERTEX; j++ ) {
    			vertex[ NUM_LINE_VERTEX * i  + j ] = v[ i ][ j ];
    		}
    	}
    	mLineVertexBuff = getIntBuffer( vertex );
    }

    /** 
	 * initLineColor
	 */	
    private void initLineColor() {
    	int[] color = new int[ LINE_NUM * NUM_LINE_COLOR ];
		int[][] c = new int[ LINE_NUM ][ NUM_LINE_COLOR ];
    	// x blue
		c[ 0 ] = buldLineColor( 0, 0, 1 );
    	// y green
		c[ 2 ] = buldLineColor( 0, 1, 0 );
    	// z magenta   
		c[ 4 ] = buldLineColor( 1, 0, 1 );
		// black
		c[ 1 ] = c[ 3 ] = c[ 5 ] = buldLineColor( 0, 0, 0 );						
     	for ( int i = 0; i < LINE_NUM; i++ ) {
    		for ( int j = 0; j < NUM_LINE_COLOR; j++ ) {
    			color[ NUM_LINE_COLOR * i  + j ] = c[ i ][ j ];
    		}
    	}
    	mLineColorBuff = getIntBuffer( color );
    }
    
    /** 
	 * === onDrawFrame ===
	 */	 
    public void onDrawFrame( GL10 gl ) {
    	super.onDrawFrame( gl );
    	glClear( gl );
    	gluLookAt( gl );

		// line	
		drawLine( gl, POINT_SIZE_LINE, mLineVertexBuff, mLineColorBuff, LINE_NUM );	

		if ( mPointNum > 0 ) {
			// orbit
			drawPoint( gl, POINT_SIZE_POINT, mVertexBuff, mColorBuff, mPointNum );

			// current 
    		IntBuffer buff_c = getVertexBuff( mCurrentNum );
    		if ( buff_c != null ) {
    			drawPoint( gl, 3*POINT_SIZE_POINT, buff_c, mCurrentColorBuff, 1 );
    		}

			// pointer
    		IntBuffer buff_p = getVertexBuff( mPointer );
    		if ( buff_p != null ) {
    			drawPoint( gl, 3*POINT_SIZE_POINT, buff_p, mCurrentColorBuff, 1 );
    		}
		}
		
		sleep( 20 );
    }

    /** 
	 * getVertexBuff
	 */
   private IntBuffer getVertexBuff( int num ) {
		int n = 3 * num;
    	if ( mVertexPoint == null ) return null;
		if ( mVertexPoint.length <= ( n + 2 )) return null;
		int[] vertex = new int[]{
			mVertexPoint[ n ], 
			mVertexPoint[ n + 1 ], 
			mVertexPoint[ n + 2 ] };
    	return getIntBuffer( vertex );
    }

    /** 
	 * setList
	 */		
    public void setList( List<Satellite> list ) {

		double RATIO = 3;
		mPointNum = list.size();
    	int[] v = new int[ NUM_POINT_VERTEX ]; 
    	int[] c = new int[ NUM_POINT_COLOR ]; 
		int[] vertex = new int[ NUM_POINT_VERTEX * mPointNum ];
    	int[] color = new int[ NUM_POINT_COLOR * mPointNum ];

		for ( int i = 0; i < list.size(); i++ ) {  
    		int iv = NUM_POINT_VERTEX * i;			
    		int ic = NUM_POINT_COLOR * i;
    		
			Satellite sat = list.get( i ); 	
    		float x = (float)( RATIO * sat.x );
    		float y = (float)( RATIO * sat.y );    	
	   		float z = (float)( RATIO * sat.z ); 
    		
			v = buldPointVertex( x, y, z );
    		for( int j=0 ; j<NUM_POINT_VERTEX ; j++ ) {
    			vertex[ iv +  j ] = v[ j ];	
    		}

  			c = buildTargetColor( x, y, z );
    		 for( int j=0 ; j<NUM_POINT_COLOR ; j++ ){
    			color[ ic + j ] = c[ j ];
    		}   			
    	}

		mVertexPoint = vertex;
    	mVertexBuff = getIntBuffer( vertex );
    	mColorBuff = getIntBuffer( color );  

    	sleep( 100 );
    }

    /** 
	 * moveLeft
	 */
    public void moveLeft() {
    	setEyePhi( getEyePhi() - DIV_PHI );
    	calcEye();
    	sleep( 100 );
    }

    /** 
	 * moveRight
	 */
    public void moveRight() {
    	setEyePhi( getEyePhi() + DIV_PHI );
    	calcEye();
    	sleep( 100 );
    }

    /** 
     * setCurrentNum
     */	
	public void setCurrentNum( int num ) {
		mCurrentNum = num;    
	}
        		
    /** 
     * updatePointer
     */	
	public void updatePointer( int num ) {
		mPointer = num;    
	}

}
