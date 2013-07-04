package jp.ohwada.android.fontsample1;
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

	private static final float TEXT_SIZE = 24; 

	private static final String[] FAMILY_NAMES = 
		{"System", "DEFAULT", "DEFAULT_BOLD", "MONOSPACE", "SANS_SERIF", "SERIF"};
	private static final Typeface[] FAMILIES = 
		{null, Typeface.DEFAULT, Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF, Typeface.SERIF};

	private static String[] STYLE_NAMES = 
		{"", "NORMAL", "BOLD", "ITALIC", "BOLD_ITALIC"};
	private static Integer[] STYLES = 
		{-1, Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC};
			
	/**
	 * === onCreate ===
	 */ 
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		ScrollView view = new ScrollView( this );
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

		String familyName, styleName;
		Typeface family;	
		int style;
		TextView tvLable, tvFont;
													
		for( int j = 0 ; j < STYLES.length ; j++ ){
			styleName = STYLE_NAMES[ j ];
			style = STYLES[ j ];
		
			for( int i = 0 ; i < FAMILIES.length ; i++ ){
				familyName = FAMILY_NAMES[ i ];
				family = FAMILIES[ i ];

				// label						
				tvLable = new TextView(this);
				if (style == -1) {
					tvLable.setText( familyName );
				}else{
					tvLable.setText( familyName + " " + styleName );
				}
				mainView.addView( tvLable );

				// font								
				tvFont = new TextView(this);
				tvFont.setText( R.string.sample_text );
				tvFont.setTextSize( TEXT_SIZE );
				if(family == null){
					// dummy
				}else if (style == -1){
					tvFont.setTypeface( family );
				}else{
					tvFont.setTypeface( Typeface.create( family, style ) );
				}
				mainView.addView( tvFont );
			}
		}
	}
}
