package jp.ohwada.android.pinqa1;

/**
 * Constant
 */
public class Constant {

	// debug
    public final static boolean DEBUG = true;
	public final static String TAG = "pinqa1";
   	public final static String DIR_NAME = "PinqaReader1"; 

	// code
	public final static String LF = "\n";
	public final static String TAB = "\t";	

	// activity request
	public static final int REQUEST_ARTICLE = 1;
	public static final String EXTRA_ARTICLE_URL = "article";
	
	// message what
	public static final int MSG_WHAT_NONE = 0;
	public static final int MSG_WHAT_TASK_ARTICLE_LIST = 1;
	public static final int MSG_WHAT_TASK_ARTICLE = 2;
	public static final int MSG_WHAT_TASK_IMAGE = 3;	
	public static final int MSG_WHAT_TASK_GEOCODER = 4;
	public static final int MSG_WHAT_DIALOG_OPTION = 5;

	// message arg1	
	public static final int MSG_ARG1_NONE = 0;
	public static final int MSG_ARG1_TASK_SUCCESS = 1;
	public static final int MSG_ARG1_TASK_CACHE = 2;
	public static final int MSG_ARG1_TASK_ERR_RESULT = 3;
	public static final int MSG_ARG1_TASK_ERR_PARSE = 4;
	public static final int MSG_ARG1_DIALOG_OPTION_DEFAULT = 11;
	public static final int MSG_ARG1_DIALOG_OPTION_GPS = 12;
	public static final int MSG_ARG1_DIALOG_OPTION_SEARCH = 13;
	public static final int MSG_ARG1_DIALOG_OPTION_MARKER = 14;

	// return value						
	public static final String BUNDLE_DIALOG_OPTION_LOCATION = "location";

	// file prefix
	public static final String FILE_PREFIX_NODE = "node";
	public static final String FILE_PREFIX_ARTICLE = "article";
	public static final String FILE_PREFIX_IMAGE = "image";
	
	// Max number of the files to save 
	public static final int MAX_FILES = 1000;
	
	// time
	public static final long TIME_MSEC_ONE_DAY = 24 * 60 * 60 * 1000; // 1 day	
	// one week
	public static final int EXPIRE_DAYS_ARTICLE_LIST = 7;
	// one month
	public static final int EXPIRE_DAYS_ARTICLE = 30;
	public static final int EXPIRE_DAYS_IMAGE = 30;
		
	// kannai station
	public final static int GEO_LAT = 35443233;
	public final static int GEO_LONG = 139637134;
	public final static int GEO_ZOOM = 15;

}
