package jp.ohwada.android.osm1;

/**
 * Constant
 */
public class Constant {

    public final static boolean DEBUG = true;
	public final static String TAG = "osm1";
   	public final static String DIR_NAME = "OpenStrretMapViewer1"; 

	// code
	public final static String LF = "\n";
	public final static String TAB = "\t";	

	public static final int MSG_WHAT_NONE = 0;
	public static final int MSG_WHAT_TASK_NODE_LIST = 1;
	public static final int MSG_WHAT_ERROR_RETRY = 2;
	public static final int MSG_WHAT_DIALOG_MAP = 3;
				
	public static final int MSG_ARG1_NONE = 0;
	public static final int MSG_ARG1_TASK_SUCCESS = 1;
	public static final int MSG_ARG1_TASK_CACHE = 2;
	public static final int MSG_ARG1_TASK_ERR_RESULT = 3;
	public static final int MSG_ARG1_TASK_ERR_PARSE = 4;
	public static final int MSG_ARG1_DIALOG_MAP_DEFAULT = 11;
	public static final int MSG_ARG1_DIALOG_MAP_GPS = 12;
	public static final int MSG_ARG1_DIALOG_MAP_SEARCH = 13;
	public static final int MSG_ARG1_DIALOG_MAP_MARKER = 14;
				
	public static final String BUNDLE_DIALOG_MAP_LOCATION = "location";

	// kannai station
	public final static int GEO_LAT = 35443233;
	public final static int GEO_LONG = 139637134;
	public final static int GEO_ZOOM = 16;

}
