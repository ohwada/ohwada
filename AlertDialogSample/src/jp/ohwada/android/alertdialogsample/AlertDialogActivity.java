//================================================
// 2011-09-25 K.OHWADA
//================================================

package jp.ohwada.android.alertdialogsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {
	
	private final static String TAG = "AlertDialogActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                 showDialog();
            }
        });
    }
    
    private void showDialog()
    {        
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Are you sure you want to exit?")
           .setCancelable(false)
           .setPositiveButton("Yes", new AlertDialog.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    finish();
               }
           })
           .setNegativeButton("No", new AlertDialog.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
           });
    	
    	AlertDialog dialog = builder.create();

    	MyDialog my_dialog = new MyDialog( dialog );
    	my_dialog.setOnShowListener(new MyDialog.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            	Toast.makeText(getApplicationContext(), "onShow", Toast.LENGTH_LONG).show();
        		Log.d(TAG, "onShow");
           }
        });
        
    	my_dialog.show();
    }
}    