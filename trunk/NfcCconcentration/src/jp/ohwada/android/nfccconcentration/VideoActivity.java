package jp.ohwada.android.nfccconcentration;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

/*
 * Video Activity
 */
public class VideoActivity extends Activity {

	// dubug
	private String TAG_SUB = "Video : ";
	private final static String TAG = Constant.TAG;
    private final static boolean D = Constant.DEBUG; 
    
	private final static String VIDEO_NAME_COMPLETE = Constant.VIDEO_NAME_COMPLETE;
	
	/**
	 * === onCreate ===
	 * @param Bundle savedInstanceState 
	 */	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_video );
        log_d( "onCreate" );
        
		// VideoView 
        VideoView video = (VideoView) findViewById( R.id.videoview );
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
        	@Override
        	public void onCompletion( MediaPlayer mp ) {
				finish();
        	}
        });

		ImageUtility utility = new ImageUtility( this );	        
		String path = utility.getPath( VIDEO_NAME_COMPLETE );

		// setVideoURI		
		try {
			video.setVideoPath( path );			
		} catch (Exception e) {
			e.printStackTrace();
		}

		video.start();
    } 
    
	/**
	 * write log
	 * @param String msg
	 */ 
	private void log_d( String msg ) {
		if (D) Log.d( TAG, TAG_SUB + msg );
	}
}
