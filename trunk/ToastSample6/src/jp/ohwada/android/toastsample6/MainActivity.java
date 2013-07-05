package jp.ohwada.android.toastsample6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * MainActivity
 */ 
public class MainActivity extends Activity {

	/**
	 * === onCreate ===
	 */ 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn1 = (Button) findViewById( R.id.Button1 );
		btn1.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				toast_show( "Hello! This is a custom toast!" );
			}
		});
	}

	/**
	 * toast_show
	 * @param String msg	 
	 */ 
	private void toast_show( String msg ) {
	    MyToast.makeText( this, msg, Toast.LENGTH_LONG ).show();
	}

}
