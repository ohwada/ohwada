package jp.ohwada.android.displaysizesample;

import android.os.Bundle;

/**
 * NoStatusTitle Activity
 */ 
public class NoStatusTitleActivity extends CommonActivity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);  
        setContentView( R.layout.main ); 
        createCommon();	
    }
}