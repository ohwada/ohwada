package jp.ohwada.android.imagemovesample1;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
        MyView view = new MyView( this );
        setContentView( view );
	}

}
