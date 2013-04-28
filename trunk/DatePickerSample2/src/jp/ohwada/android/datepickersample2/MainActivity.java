package jp.ohwada.android.datepickersample2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyDatePicker picker1 = (MyDatePicker) findViewById( R.id.DatePicker1 );
		picker1.setOnDateChangedListener( new MyDatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged( MyDatePicker view, int year, int monthOfYear, int day ) {
				toast_show(  year + " " + (monthOfYear + 1) + " " + day );
			}
		});

		MyDatePicker picker2 = (MyDatePicker) findViewById( R.id.DatePicker2 );
		picker2.setOnDateChangedListener( new MyDatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged( MyDatePicker view, int year, int monthOfYear, int day ) {
				toast_show(  year + " " + (monthOfYear + 1) + " " + day );
			}
		});
	}

	private void toast_show( String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
