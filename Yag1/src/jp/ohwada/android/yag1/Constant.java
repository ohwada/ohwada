package jp.ohwada.android.yag1;

/**
 * Constant
 */
public class Constant {

    public final static boolean DEBUG = true;
	public final static String TAG = "YokohamaArtGuide1";
   	public final static String DIR_NAME = "YokohamaArtGuide1"; 

	// code
	public final static String LF = "\n";
	public final static String TAB = "\t";
		
	/* Intent request codes */
	public static final int REQUEST_MENU_EVENT = 1;	
	public static final int REQUEST_MENU_PLACE = 2;
	public static final int REQUEST_MENU_MAP = 3;
	public static final int REQUEST_EVENT = 4;	
	public static final int REQUEST_PLACE = 5;
	public static final int REQUEST_MAP = 6;
	public static final int REQUEST_WEB = 7;
	
	/* Intent result codes */
	public static final int RESULT_NONE = 0;	
	public static final int RESULT_MENU_EVENT = 1;	
	public static final int RESULT_MENU_PLACE = 2;
	public static final int RESULT_MENU_MAP = 3;
	public static final int RESULT_EVENT = 4;	
	public static final int RESULT_PLACE = 5;
	public static final int RESULT_MAP = 6;
	public static final int RESULT_WEB = 7;
	
	/* Intent bundle codes */		
	public final static String EXTRA_DATE = "date"; 
	public final static String EXTRA_EVENT = "event"; 
	public final static String EXTRA_EVENT_URL = "event_url"; 
	public final static String EXTRA_PLACE = "place"; 
	public final static String EXTRA_PLACE_URL = "place_url"; 
	public final static String EXTRA_MAP = "map"; 
	public final static String EXTRA_WEB = "web"; 
	public static final String EXTRA_CODE = "code";
	

	public static final int MSG_WHAT_NONE = 0;
	public static final int MSG_WHAT_TASK_EVENT_LIST = 1;
	public static final int MSG_WHAT_TASK_PLACE_LIST = 2;
	public static final int MSG_WHAT_TASK_EVENT = 3;
	public static final int MSG_WHAT_TASK_EVENT_LIST_PLACE = 4;
	public static final int MSG_WHAT_TASK_IMAGE = 5;
	public static final int MSG_WHAT_ERROR_RETRY = 6;
	public static final int MSG_WHAT_DIALOG_MAP = 7;
				
	public static final int MSG_ARG1_NONE = 0;
	public static final int MSG_ARG1_TASK_SUCCESS = 1;
	public static final int MSG_ARG1_TASK_CACHE = 2;
	public static final int MSG_ARG1_TASK_ERR_RESULT = 3;
	public static final int MSG_ARG1_TASK_ERR_PARSE = 4;
	public static final int MSG_ARG1_DIALOG_MAP_DEFAULT = 11;
	public static final int MSG_ARG1_DIALOG_MAP_GPS = 12;
	public static final int MSG_ARG1_DIALOG_MAP_SEARCH = 13;
	public static final int MSG_ARG1_DIALOG_MAP_MARKER = 14;
	public static final int MSG_ARG1_DIALOG_MAP_MAP = 15;
				
	public static final String BUNDLE_DIALOG_MAP_LOCATION = "location";

	// kannai station
	public final static int GEO_LAT = 35443233;
	public final static int GEO_LONG = 139637134;
	public final static int GEO_ZOOM = 16;

}
