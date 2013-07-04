package jp.ohwada.android.fontsample3;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * MainActivity
 */
public class MainActivity extends Activity {

	private static final float TEXT_SIZE = 24; 

	private static final String DEFAULT_LABEL = "default font";

// http://openfontlibrary.org/en/font/dancing
	private static final String FONT_FILE = "Dancing-Script.ttf";

	/**
	 * === onCreate ===
	 */		
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		showDefault();
		showFont();
	}

	/**
	 * sshowDefault
	 */ 
	private void showDefault() {	
		TextView tvLable = (TextView) findViewById( R.id.TextViewDefaultLabel );
		TextView tvText = (TextView) findViewById( R.id.TextViewDefaultText );
		tvLable.setText( DEFAULT_LABEL );
    	tvText.setText( R.string.sample_text );
	}
	
	/**
	 * showFont
	 */ 
	private void showFont() {	
		TextView tvLabel = (TextView) findViewById( R.id.TextViewFontLabel );
		TextView tvText = (TextView) findViewById( R.id.TextViewFontText );

    	tvLabel.setText( FONT_FILE );
 
		Typeface typeface = Typeface.createFromAsset( getAssets(), FONT_FILE );
    	if ( typeface != null  ) {
    		tvText.setText( R.string.sample_text );
    		tvText.setTextSize( TEXT_SIZE );
    		tvText.setTypeface( typeface );
		} else {
			tvText.setText( "typeface is null" );
		}
	}

}
