package jp.ohwada.android.osm2.task;

import java.io.File;

import jp.ohwada.android.osm2.Constant;

/**
 * NodeList file 
 */
public class NodeListFile extends CommonFile { 

	// constant
	private static final long EXPIRE_SUMMARY = Constant.EXPIRE_DAYS_NODE_LIST_SUMMARY  * TIME_MSEC_ONE_DAY; 
	private static final long EXPIRE_DETAIL = Constant.EXPIRE_DAYS_NODE_LIST_DETAIL  * TIME_MSEC_ONE_DAY; 
	    	
	/**
	 * === constractor ===
	 */
    public NodeListFile() {
    	TAG_SUB = "NodeListFile";
    }

	/**
	 * getFile
	 * @param int lat
	 * @param int lng
	 * @return File
	 */
    public File getFileSummary( int lat, int lng ) {
    	String name = Constant.FILE_PREFIX_NODE_LIST_SUMMARY + "_" + getFileName( lat, lng );
		return getFileWithTxt( name );
	}

	/**
	 * getFile
	 * @param int lat
	 * @param int lng
	 * @return File
	 */
    public File getFileDetail( int lat, int lng ) {
    	String name = Constant.FILE_PREFIX_NODE_LIST_DETAIL + "_" + getFileName( lat, lng );
		return getFileWithTxt( name );
	}

	/**
	 * getFileName
	 * @param int lat
	 * @param int lng
	 * @return String
	 */
    private String getFileName( int lat, int lng ) {
    	String str = e6ToE3( lat ) + "_" + e6ToE3( lng );
		return str;
	}
				
	/**
	 * isExpired Node
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpiredSummary( File file ) {
		return isExpiredFile( file, EXPIRE_SUMMARY );
	}

	/**
	 * isExpired Node
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpiredDetail( File file ) {
		return isExpiredFile( file, EXPIRE_DETAIL );
	}
			
	/**
	 * write NodeList
	 * @param File file
	 * @param NodeList list
	 */
	public void write( File file, NodeList list ) {
		String data = list.buildWriteData();
    	writeAfterDelete( file, data );
	}

	/**
	 * read NodeList
	 * @param File file
	 * @return NodeList
	 */
    public NodeList read( File file ) {
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) { 
		    log_d( "read no data" );
			return null;
		}	
		NodeList list = new NodeList();
		String[] lines = data.split( LF );
		// build read data list
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( !"".equals( line ) ) { 
				NodeRecord r = new NodeRecord( line );
				if ( r.isValidNode() ) {
					list.addWithMapColor( r );
				}
			}	
		}
		return list;
	}

	/**
	 * e6ToE3
	 * @param int e6
	 * @return int
	 */
    private int e6ToE3( int e6 ) {
   		int e3 = e6 / 1000;
   		return e3;
	}
  				
}