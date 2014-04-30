package jp.ohwada.android.shootinggame1;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * CameraView
 */ 
public class CameraView extends SurfaceView 
	implements SurfaceHolder.Callback {

	// debug
	private final static boolean D = Constant.D;
	
	// OptimalPreviewSize
	private final static double ASPECT_TOLERANCE = 0.01;
	
	// object	
	private Camera mCamera = null;
						
	/**
	 * === Constrator ===
	 */			
	public CameraView( Context context ){
		super(context);
		init();
	}

	/**
	 * === Constrator ===
	 */		
	public CameraView( Context context, AttributeSet attrs){
		super( context, attrs );
		init();
	}

	/**
	 * === Constrator ===
	 */		
	public CameraView(Context context, AttributeSet attrs, int defStyle){
		super( context, attrs, defStyle);
		init();
	}
	
	/**
	 * Initialization
	 */	
	private void init(){
		SurfaceHolder holder = getHolder();
		holder.addCallback( this );
	}

	/**
	 * === surfaceCreated ===
	 */		
	@Override
	public void surfaceCreated( SurfaceHolder holder ){
		mCamera = Camera.open();
		try{
			mCamera.setPreviewDisplay( holder );
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * === surfaceDestroyed ===
	 */	
	@Override
	public void surfaceDestroyed( SurfaceHolder holder ){
		if( mCamera == null ) return;
		mCamera.setPreviewCallback( null );
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	/**
	 * === surfaceChanged ===
	 */
	@Override
	public void surfaceChanged( SurfaceHolder holder, int format, int w, int h ){
		if( mCamera == null ) return;
		// set the parameter of a camera
		Camera.Parameters parameters = mCamera.getParameters();
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size previewSize = getOptimalPreviewSize( sizes, w, h );
		parameters.setPreviewSize( previewSize.width, previewSize.height );
		List<Size> sizesp = parameters.getSupportedPictureSizes();
		Size pictureSize = getOptimalPreviewSize( sizesp, w, h );
		parameters.setPictureSize( pictureSize.width, pictureSize.height );
		mCamera.setParameters( parameters );
		// start Preview
		mCamera.startPreview();
	}
			
	/**
	 * calculate the nearest size of the aspect ratio of screen size. 
	 * @param List<Size> sizes
	 * @param int w
	 * @param int h
	 * @return Size	 
	 */	
	private Size getOptimalPreviewSize( List<Size> sizes, int w, int h ) {
		double targetRatio = (double) w / h;
		if (sizes == null) return null;
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

}
