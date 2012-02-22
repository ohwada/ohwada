package jp.ohwada.android.displaysizesample;

import android.os.Bundle;
import android.view.WindowManager;

/**
 * NoStatus Activity
 */ 
public class NoStatusActivity extends CommonActivity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState); 
        setContentView( R.layout.main ); 
        createCommon();
        hideStatusBar();
    }
}