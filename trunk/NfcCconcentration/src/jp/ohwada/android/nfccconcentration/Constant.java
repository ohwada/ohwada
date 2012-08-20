package jp.ohwada.android.nfccconcentration;

/**
 * Constant
 */
public class Constant {
	public final static String TAG = "NFC";
    public final static boolean DEBUG = true; 

	public final static int REQUEST_CODE_SETTING = 1;	
	public final static int REQUEST_CODE_LIST = 2;	    
	public final static int REQUEST_CODE_CREATE = 3;
	public final static int REQUEST_CODE_UPDATE = 4;	
	public final static int REQUEST_CODE_VIDEO = 5;
			;	
			
	public final static String BUNDLE_EXTRA_ID  = "id";
	public final static String BUNDLE_EXTRA_TAG  = "tag";
	
	public static final int MSG_WHAT_FINISH = 1;
	public static final int MSG_WHAT_TIME = 2;

	public static final String PREF_KEY_DIR = "dir";	
	public static final String PREF_KEY_NUM = "num";	
	public static final String PREF_KEY_TIME = "time";

	public final static String DIR_MAIN = "NFC_Cconcentration";
	public final static String DIR_SUB_DEFAULT = "default";
	public final static int CARD_NUM_DEFAULT = 10;
	
	public final static String IMAGE_PREFIX = "image_";
	public final static String IMAGE_EXT = "jpg";
	public static final String IMAGE_NAME_START = "image_start.jpg";
	public static final String IMAGE_NAME_COMPLETE = "image_complete.jpg";
	public final static String VIDEO_NAME_COMPLETE = "video_complete.m4v";
}
