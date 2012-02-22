package jp.ohwada.android.displaysizesample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Common for Main Activity, etc
 */ 
public class CommonActivity extends Activity {

	// line feed
	protected final static String LF = "\n";
   
    // view component  	
	protected LinearLayout mLinearLayoutMain;
	protected TextView mTextViewDisplay;
	protected TextView mTextViewDraw;
	protected Button  mButtonNoStatus; 
    protected Button  mButtonNoTitle; 
    protected Button  mButtonNoStatusTitle; 
    protected Button  mButtonBack; 
    
    // display        
    protected int display_width = 0;
    protected int display_height = 0;
	protected float density = 0;

	/**
	 * hide Title Bar
	 */
    public void hideTitleBar() { 
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * hide Status Bar
	 */	
    public void hideStatusBar() {  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
    }

	/**
	 * create without status bar, title bar 
	 */    
    public void createCommon() {  
        initCommon();       
		hideNextButtons();
		enableBackButton();  
	}

	/**
	 * Back Button with click
	 */   
    private void enableBackButton() {    
    	mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();   
			}
        });
    }

	/**
	 * hide Back Button
	 */    		    
    protected void hideBackButton() {    
		if (mButtonBack.getVisibility() == View.VISIBLE) {  
			mButtonBack.setVisibility(View.INVISIBLE); 
		}
    }

	/**
	 * hide NoStatus Button, etc
	 */   
    protected void hideNextButtons() { 
		if (mButtonNoStatus.getVisibility() == View.VISIBLE) {  
			mButtonNoStatus.setVisibility(View.INVISIBLE); 
		}
		if (mButtonNoTitle.getVisibility() == View.VISIBLE) {  
			mButtonNoTitle.setVisibility(View.INVISIBLE); 
		}
		if (mButtonNoStatusTitle.getVisibility() == View.VISIBLE) {  
			mButtonNoStatusTitle.setVisibility(View.INVISIBLE); 
		}
    }

	/**
	 * init view component & show display info
	 */ 
    protected void initCommon() {
    	initViews();
		initTextViewDisplay();
		initLinearLayoutMain();
    }

	/**
	 * init view component
	 */                
    protected void initViews() {
      	mLinearLayoutMain = (LinearLayout) findViewById(R.id.linearlayout_main); 
        mTextViewDisplay = (TextView) findViewById(R.id.textview_display);
        mTextViewDraw = (TextView) findViewById(R.id.textview_draw);   
	    mButtonNoStatus = (Button) findViewById(R.id.button_no_status);	
		mButtonNoTitle = (Button) findViewById(R.id.button_no_title);
	    mButtonNoStatusTitle = (Button) findViewById(R.id.button_no_status_title);
	    mButtonBack = (Button) findViewById(R.id.button_back);		
    }

	/**
	 * main view with run
	 */ 
    protected void initLinearLayoutMain() {    
		mLinearLayoutMain.post( new Runnable() {
  			public void run(){
  				view_main_run();
			}
		});
    }
        
	/**
	 * show display info
	 */ 
    protected void initTextViewDisplay() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
 		Display display = wm.getDefaultDisplay();
  		display_width = display.getWidth();
  		display_height = display.getHeight();
	
 		DisplayMetrics metrics = new DisplayMetrics();  
 		getWindowManager().getDefaultDisplay().getMetrics(metrics);

 		density = metrics.density;
        
        String msg = "";
        msg += "Display" + LF;  
  		msg += "display: " + display_width + "x" +  display_height + LF;
  		msg += LF;    
  		msg += "DisplayMetrics" + LF;    
  		msg += "pixels: " + metrics.widthPixels + "x" + metrics.heightPixels + LF;  
  		msg += "dpi: " + metrics.xdpi + "x" + metrics.ydpi + LF;
  		msg += "densityDpi: " + metrics.densityDpi + LF;  
  		msg += "density: " + metrics.density + LF;  
  		msg += "scaledDensity: " + metrics.scaledDensity + LF;  

         mTextViewDisplay.setText( msg );       
    }

	/**
	 * show bar info
	 */     
    protected void view_main_run() { 

// https://groups.google.com/group/android-developers/browse_thread/thread/e365dada8ae08eb4?fwc=1&hl=ja
     	int status_height = 0;	
    	int title_height = 0; 
    	int id_title_height = 0; 

    	Window mWindow = getWindow();
        View mDecorView = mWindow.getDecorView();
    	View mViewContent = (View) mWindow.findViewById(Window.ID_ANDROID_CONTENT);
    	    	
// http://d.hatena.ne.jp/Kazzz/20100828/p1
    	TextView mTextViewTitleBar = (TextView)this.findViewById(android.R.id.title); 
    	ViewGroup mViewGroupContent = (ViewGroup) findViewById(android.R.id.content);
    	   
        Rect rect= new Rect();
	    mDecorView.getWindowVisibleDisplayFrame( rect ); 
	    int rect_left = rect.left;
	    int rect_top = rect.top;
	    int rect_width = rect.right - rect_left;
	    int rect_height = rect.bottom - rect_top;
        int content_top = mViewContent.getTop();
        int main_left = mLinearLayoutMain.getLeft();	
    	int main_top = mLinearLayoutMain.getTop();	
        int wp = mLinearLayoutMain.getWidth();
        int hp = mLinearLayoutMain.getHeight();
        int wd = (int)( wp / density );
        int hd = (int)( hp / density );   			
		int bar_height = display_height - hp ;

		if ( mTextViewTitleBar != null ) {
			id_title_height = mTextViewTitleBar.getHeight();
		}
		
    	if ( bar_height > 0 ) {
    		if ( id_title_height > 0 ) {
    	    	if ( content_top > rect_top ) {
    	        	status_height = rect_top;	
    				title_height = content_top - rect_top;
    			} else {
    		    	title_height = content_top;
    			}   
    	    } else {
    	        status_height = rect_top;
    		}  
    	}
    	
        String msg = "";
        msg += "After Draw" + LF;  
        msg += "Status Bar Height: " + status_height + LF;  
        msg += "Title Bar Height: " + title_height + LF; 
        msg += "Main: " + wp + "x" + hp + " px " + LF;  
        msg += "Main: " + wd + "x" + hd + " dp" + LF; 
        msg += LF; 
        msg += "Additional Information" + LF;  
        msg += "Bar Height: " + bar_height + LF;
        msg += "id.title Height: " + id_title_height + LF; 
                
// same as ANDROID CONTENT and  id.content      
        msg += "ANDROID CONTENT: " + mViewContent.getWidth() + "x" + mViewContent.getHeight() + " " +  mViewContent.getLeft() + " " + content_top + LF;
         msg += "id.content: " + mViewGroupContent.getWidth() + "x" + mViewGroupContent.getHeight() + " " + mViewGroupContent.getLeft() + " " + mViewGroupContent.getTop() +  LF;
         
        msg += "Rect: " + rect_width + "x" + rect_height + " " +  rect_left + " " + + rect_top + LF;
        msg += "Main: " + wp + "x" + hp + " " + main_left + " " + main_top + LF;  
                
        mTextViewDraw.setText( msg );   
    }
}