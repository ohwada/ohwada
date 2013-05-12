package jp.ohwada.android.slideswitchsample1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity  {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
    	super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

    	SlideSwitch sw2 = (SlideSwitch) findViewById( R.id.SlideSwitch_2 );
		sw2.setOnCheckedChangeListener( new SlideSwitch.OnCheckedChangeListener() {
            public void onCheckedChanged( SlideSwitch view, boolean isChecked ) {
                 toast_show( "onCheckedChanged " + isChecked );
            }
        });
 
    	SlideSwitch sw1 = (SlideSwitch) findViewById( R.id.SlideSwitch_1 );
		sw1.setOnCheckedChangeListener( new SlideSwitch.OnCheckedChangeListener() {
            public void onCheckedChanged( SlideSwitch view, boolean isChecked ) {
                 toast_show( "onCheckedChanged " + isChecked );
            }
        });

    }

	private void toast_show( String msg ) {
		Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}
}