package jp.ohwada.android.osm1;

/**
 * Constant
 */
public class Constant {

    public final static boolean DEBUG = true;
	public final static String TAG = "osm1";
   	public final static String DIR_NAME = "OsmNodeViewer1"; 

	// code
	public final static String LF = "\n";
	public final static String TAB = "\t";	
	public final static String SPACE = " ";	
	
	// Intent request codes
	public static final int REQUEST_NODE = 1;
	public static final int REQUEST_MAP_SETTING = 2;
	
	// Intent bundle codes
	public static final String EXTRA_NODE_URL = "node";

	// massage handler codes	
	public static final int MSG_WHAT_NONE = 0;
	public static final int MSG_WHAT_TASK_NODE_LIST = 1;
	public static final int MSG_WHAT_TASK_NODE = 2;
	public static final int MSG_WHAT_ERROR_RETRY = 3;
	public static final int MSG_WHAT_DIALOG_MAP = 4;

	// timer handler codes
	public static final int MSG_WHAT_TIMER_NODE_LIST = 101;
	public static final int MSG_WHAT_TIMER_NODE = 102;
	public static final int MSG_WHAT_TIMER_GEOCODER = 103;

	// massage handler					
	public static final int MSG_ARG1_NONE = 0;
	public static final int MSG_ARG1_TASK_SUCCESS = 1;
	public static final int MSG_ARG1_TASK_CACHE = 2;
	public static final int MSG_ARG1_TASK_ERR_RESULT = 3;
	public static final int MSG_ARG1_TASK_ERR_PARSE = 4;
	public static final int MSG_WHAT_TASK_GEOCODER = 5;
	public static final int MSG_ARG1_DIALOG_MAP_DEFAULT = 11;
	public static final int MSG_ARG1_DIALOG_MAP_GPS = 12;
	public static final int MSG_ARG1_DIALOG_MAP_MARKER = 13;

	// Max number of the files to save 
	public static final int MAX_FILES = 1000;

	// expire time
	public static final long TIME_MSEC_ONE_DAY = 24 * 60 * 60 * 1000; // 1 day
	public static final int EXPIRE_DAYS_NODE_LIST_SUMMARY = 1 ; 
	public static final int EXPIRE_DAYS_NODE_LIST_DETAIL = 30 ; 
	public static final int EXPIRE_DAYS_NODE = 30 ; 

	// file name
	public static final String FILE_PREFIX_NODE_LIST_SUMMARY = "summries";
	public static final String FILE_PREFIX_NODE_LIST_DETAIL = "details";
	public static final String FILE_PREFIX_NODE = "node"; 

	// Preferences
	public static final String PREF_NAME_GEO_NAME = "geo_name";
	public static final String PREF_NAME_GEO_LAT = "geo_lat";
	public static final String PREF_NAME_GEO_LONG = "geo_long";	

	// kannai station
	public final static int GEO_LAT = 35443233;
	public final static int GEO_LONG = 139637134;
	public final static int GEO_ZOOM = 16;

	// api
	public final static String API_AUTHORITY = "lgd.ohwada.jp";
	public final static String API_PATH = "/api.php";

}
