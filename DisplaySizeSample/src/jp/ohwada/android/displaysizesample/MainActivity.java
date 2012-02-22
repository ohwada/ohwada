package jp.ohwada.android.displaysizesample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Main Activity
 */  
public class MainActivity extends CommonActivity {
                	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView( R.layout.main );        
		initCommon();
		enableNoButtons();
		hideBackButton(); 		
    }

	/**
	 * Button with click
	 */      
    private void enableNoButtons() {    
    	mButtonNoStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	button_no_status_click();   
			}
        });

        mButtonNoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	button_no_title_click();  
			}
        });
                
        mButtonNoStatusTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	button_no_status_title_click();   
			}
        });
    }

	/**
	 * start NoStatus Activity
	 */
    private void button_no_status_click() {    
    	Intent intent = new Intent(this, NoStatusActivity.class);
        startActivity(intent);    
    } 

	/**
	 * start NoTitle Activity
	 */        
    private void button_no_title_click() {    
    	Intent intent = new Intent(this, NoTitleActivity.class);
        startActivity(intent);    
    } 

	/**
	 * start NoStatusTitle Activity
	 */       
    private void button_no_status_title_click() {    
    	Intent intent = new Intent(this, NoStatusTitleActivity.class);
        startActivity(intent);    
    } 
    
}
