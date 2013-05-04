package jp.ohwada.android.nfccconcentration;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.VideoView;

/*
 * Video Activity
 */
public class VideoActivity extends Activity 
	implements SurfaceHolder.Callback {

	// dubug
    private final static boolean D = Constant.DEBUG; 
    
    // customize 
	private boolean isUseAssetsFile = Constant.USE_ASSETS_FILE; 
	
	private final static String VIDEO_NAME_COMPLETE = Constant.VIDEO_NAME_COMPLETE;

	private ImageUtility mImageUtility;
	private FileUtility mFileUtility;
	private MediaPlayer mMediaPlayer = null;
		
	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */	
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_video );

        getWindow().setFormat( PixelFormat.TRANSPARENT );        
		mImageUtility = new ImageUtility( this );
		mFileUtility = new FileUtility( this );     

        VideoView video = (VideoView) findViewById( R.id.videoview );
		SurfaceHolder holder = video.getHolder();
		holder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
		holder.addCallback( this );
    } 

	@Override
	public void surfaceChanged( SurfaceHolder holder, int format, int width,int height ) {
		// dummy
	}

	@Override
	public void surfaceCreated( SurfaceHolder holder ) {
        mMediaPlayer = new MediaPlayer();
    	mMediaPlayer.setDisplay( holder );
        mMediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
        	@Override
        	public void onCompletion( MediaPlayer mp ) {
				finish();
        	}
        });

		if ( isUseAssetsFile && mImageUtility.isDefualtDir() ) {
			setAssetDataSource();
		} else {
			setSdDataSource();
		}
		
		mMediaPlayer.prepareAsync();
		mMediaPlayer.setOnPreparedListener( new OnPreparedListener() {
			@Override
			public void onPrepared( MediaPlayer mp ) {
				mp.start();
			}
		});

	}

	/**
	 * setAssetDataSource
	 */ 
	private void setAssetDataSource() {
		AssetFileDescriptor afd = mFileUtility.getAssetFileDescriptor( VIDEO_NAME_COMPLETE );
		try {
			mMediaPlayer.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength() );
		} catch (IllegalArgumentException e1) {
			if (D) e1.printStackTrace();
		} catch (IllegalStateException e1) {
			if (D) e1.printStackTrace();
		} catch (IOException e1) {
			if (D) e1.printStackTrace();
		}	
	}

	/**
	 * setSdDataSource
	 */ 
	private void setSdDataSource() {
		String path = mImageUtility.getPath( VIDEO_NAME_COMPLETE );
		FileDescriptor fd = mFileUtility.getFileDescriptor( path );
		try {
			mMediaPlayer.setDataSource( fd );
		} catch (IllegalArgumentException e1) {
			if (D) e1.printStackTrace();
		} catch (IllegalStateException e1) {
			if (D) e1.printStackTrace();
		} catch (IOException e1) {
			if (D) e1.printStackTrace();
		}	
	}
	
	@Override
	public void surfaceDestroyed( SurfaceHolder holder ) {
		 if( mMediaPlayer != null ) {
		      mMediaPlayer.release();
		      mMediaPlayer = null;
		 }
	}
 
}
