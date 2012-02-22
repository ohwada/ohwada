package jp.ohwada.android.displaysizesample;

import android.os.Bundle;

/**
 * NoTitle Activity
 */ 
public class NoTitleActivity extends CommonActivity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
//    hideTitleBar();	// AndroidManifest 
        setContentView( R.layout.main ); 
        createCommon();	
    }
}