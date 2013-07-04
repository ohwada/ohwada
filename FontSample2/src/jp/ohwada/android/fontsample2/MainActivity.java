package jp.ohwada.android.fontsample2;

import java.io.File;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

	private static final String FONT_DIR = "/system/fonts";
	private static final String FONT_EXT = "ttf";
	private static final float TEXT_SIZE = 24; 

	/**
	 * === onCreate ===
	 */ 
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		ScrollView view = new ScrollView(this);
		setContentView( view );
		showView( view );
	}

	/**
	 * showView
	 * @param ScrollView svView
	 */ 
	private void showView( ScrollView svView ) {		
		LinearLayout mainView = new LinearLayout(this);
		mainView.setOrientation( LinearLayout.VERTICAL );
		mainView.setPadding( 10,10,10,10 );
		svView.addView( mainView );

		File[] files = FileUtility.getFiles( FONT_DIR, FONT_EXT );
		if (( files == null )||( files.length == 0 )) {
			tvFont = new TextView(this);
    		tvFont.setTextSize( TEXT_SIZE );			
			tvFont.setText( "No file" );	
			mainView.addView( tvFont );
			return;
		} 

		TextView tvLabel, tvFont;
		File file;

		for( int i = 0 ; i <  files.length ; i++ ){
			file = files[ i ];

			// label
			tvLabel = new TextView(this);
			tvLabel.setText( file.getName() );				
			mainView.addView( tvLabel );

			// font				
			tvFont = new TextView(this);
			Typeface typeface = Typeface.createFromFile( file );
    		if ( typeface != null ) {
    			tvFont.setText( R.string.sample_text );
    			tvFont.setTextSize( TEXT_SIZE );
    			tvFont.setTypeface( typeface );
			} else {
				tvFont.setText( "typeface is null" );
			}
			mainView.addView( tvFont );				

		}
	}
	
}
