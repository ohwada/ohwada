package jp.ohwada.android.nfccconcentration;

/**
 * Constant
 */
public class Constant {
// debug
	public final static String TAG = "NFC";
    public final static boolean DEBUG = true; 

// customize parameter
	public final static boolean USE_COPY_FILES = true;
	public final static boolean USE_ASSETS_FILE = false;
	public final static boolean USE_BUTTON_BULK = false;
	public final static boolean USE_BUTTON_NUM = false;
	public final static boolean USE_BUTTON_CREATE = false;
	public final static int MODE_ALREADY_DELETE = 1;
	public final static int MODE_ALREADY_UPDATE = 2;
	public final static int MODE_ALREADY = MODE_ALREADY_DELETE;
	public final static String URL_WEB  = "";

// REQUEST CODE
	public final static int REQUEST_CODE_SETTING = 1;	
	public final static int REQUEST_CODE_LIST = 2;	    
	public final static int REQUEST_CODE_CREATE = 3;
	public final static int REQUEST_CODE_UPDATE = 4;	
	public final static int REQUEST_CODE_VIDEO = 5;
			
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
	public final static String IMAGE_EXT = "png";
	public static final String IMAGE_NAME_START = "image_start.png";
	public static final String IMAGE_NAME_COMPLETE = "image_complete.png";
	public final static String VIDEO_NAME_COMPLETE = "video_complete.m4v";
}
