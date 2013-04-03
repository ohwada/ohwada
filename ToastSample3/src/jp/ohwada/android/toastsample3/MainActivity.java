package jp.ohwada.android.toastsample3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int mNum = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        Button btnToast = (Button) findViewById( R.id.button_toast );
		btnToast.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				execToast();
			}	
		});			

        Button btnMyToast = (Button) findViewById( R.id.button_my_toast );
		btnMyToast.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v) {
				execMyToast();
			}	
		});	

	}

	private void execToast() {
		Toast.makeText( this, "abc " + mNum, Toast.LENGTH_LONG ).show();
		mNum ++;
	}

	private void execMyToast() {
		MyToast.makeText( this, "abc " + mNum, Toast.LENGTH_LONG, 32f ).show();
		mNum ++;
	}
}
