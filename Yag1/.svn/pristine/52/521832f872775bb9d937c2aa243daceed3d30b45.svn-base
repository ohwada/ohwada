package jp.ohwada.android.yag1.task;

import jp.ohwada.android.yag1.R;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

/**
 *Loading Dialog
 */
public class LoadingDialog extends Dialog {
			
	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public LoadingDialog( Context context ) {
		super( context, R.style.Theme_LoadingDialog );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public LoadingDialog( Context context, int theme ) {
		super( context, theme );
		create(); 
	}
		
	/**
	 * create
	 */ 	
//	@SuppressWarnings("deprecation")
	private void create() {
		setContentView( R.layout.dialog_loading );		
		// FILL_PARENT : This value is deprecated starting in API Level 8
		getWindow().setLayout( 
			ViewGroup.LayoutParams.FILL_PARENT, 		
			ViewGroup.LayoutParams.FILL_PARENT );	
	}
		
}
