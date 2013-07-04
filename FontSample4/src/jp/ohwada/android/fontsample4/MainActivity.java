package jp.ohwada.android.fontsample4;

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

// http://www001.upp.so-net.ne.jp/mikachan/
	private String FONT_INTERNAL = "mikachan";

// http://www.forest.impress.co.jp/library/software/aoyagifont/
	private String FONT_EXTERNAL = "kouzanmouhitu";

	/**
	 * === onCreate ===
	 */		
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		showDefault();
		showInternal();
		showExternal();
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
	 * showInternal
	 */ 
	private void showInternal() {	
		TextView tvLabel = (TextView) findViewById( R.id.TextViewInternalLabel );
		TextView tvText = (TextView) findViewById( R.id.TextViewInternalText );

    	tvLabel.setText( FONT_INTERNAL + " in Internal"  );
 
 		MyFont font = new MyFont( this, FONT_INTERNAL, MyFont.MODE_INTERNAL ); 
		font.initFile();
		showFont( tvText, font.getTypeface() );
	}

	/**
	 * showExernal
	 */ 
	private void showExternal() {	
		TextView tvLabel = (TextView) findViewById( R.id.TextViewExternalLabel );
		TextView tvText = (TextView) findViewById( R.id.TextViewExternalText );

    	tvLabel.setText( FONT_EXTERNAL + " in External" );
 
 		MyFont font = new MyFont( this, FONT_EXTERNAL, MyFont.MODE_EXTERNAL ); 
		font.initFile();
		showFont( tvText, font.getTypeface() );
	}

	/**
	 * showFont
	 * @param TextView tv
	 * @param Typeface typeface
	 */ 
	private void showFont( TextView tv, Typeface typeface ) {	
    	if ( typeface != null  ) {
    		tv.setText( R.string.sample_text );
    		tv.setTextSize( TEXT_SIZE );
    		tv.setTypeface( typeface );
		} else {
			tv.setText( "typeface is null" );
		}
	}

}
