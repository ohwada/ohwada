package jp.ohwada.android.timepickersample2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyTimePicker picker1 = (MyTimePicker) findViewById( R.id.TimePicker1 );
		picker1.setOnTimeChangedListener( new MyTimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged( MyTimePicker view, int hourOfDay, int minute ) {
				toast_show( hourOfDay + " " + minute );
			}
		});

		MyTimePicker picker2 = (MyTimePicker) findViewById( R.id.TimePicker2 );
//		picker2.setVisibilitySecond( true );
//		picker2.setIs24HourView( true );
//		picker2.setNumberWidth( 100 );
//		picker2.setTextSize( 45 );
		picker2.setOnTimeSecondChangedListener( new MyTimePicker.OnTimeSecondChangedListener() {
			@Override
			public void onTimeChanged( MyTimePicker view, int hourOfDay, int minute, int second ) {
				toast_show( hourOfDay + " " + minute + " " + second );
			}
		});
				
	}

	private void toast_show( String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	        
}
