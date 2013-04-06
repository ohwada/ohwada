package jp.ohwada.android.numberpickersample2;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyNumberPicker numberPicker1 = (MyNumberPicker) findViewById(R.id.numberpicker1);
        numberPicker1.setRange(0,99);
        
		MyNumberPicker numberPicker2 = (MyNumberPicker) findViewById(R.id.numberpicker2);
        numberPicker2.setRange(0,99);
        
	}

}
