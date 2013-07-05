package jp.ohwada.android.toastsample5;


import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
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
				toast_show( "hoge" );
			}
		});

		Button btn2 = (Button) findViewById( R.id.Button2 );
		btn2.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				toast_show_center( "hoge" );
			}
		});
	}

	/**
	 * toast_show
	 * @param String msg	 
	 */ 
	private void toast_show( String msg ) {
	    Toast toast = Toast.makeText( this, msg, Toast.LENGTH_LONG );
        toast.show();
	}

	/**
	 * toast_show
	 * @param String msg	 
	 */ 	
	private void toast_show_center( String msg ) {
	    Toast toast = Toast.makeText( this, msg, Toast.LENGTH_LONG );
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0 );
        toast.show();
	}
   
}
