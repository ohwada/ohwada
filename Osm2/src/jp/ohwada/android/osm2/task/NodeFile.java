package jp.ohwada.android.osm2.task;

import java.io.File;

import jp.ohwada.android.osm2.Constant;

/**
 * Node file 
 */
public class NodeFile extends CommonFile { 

	// constant
	private static final long EXPIRE_NODE = Constant.EXPIRE_DAYS_NODE * TIME_MSEC_ONE_DAY;
    	
	/**
	 * === constractor ===
	 */
    public NodeFile() {
    	TAG_SUB = "NodeFile";
    }
		
	/**
	 * getFile
	 * @param int node
	 * @return File
	 */
    public File getFile( int node ) {
    	String name = Constant.FILE_PREFIX_NODE + "_" + node;
		return getFileWithTxt( name );
	}
		
	/**
	 * isExpired Node
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, EXPIRE_NODE );
	}

	/**
	 * write Node
	 * @param File file,
	 * @param NodeHash hash
	 */
	public void write( File file, NodeHash hash ) {
		String data = hash.getFileData();
    	writeAfterDelete( file, data );
	}

	/**
	 * read Node
	 * @param File file,
	 * @return NodeHash
	 */
	public NodeHash read( File file ) {
		String data = readData( file );	
		// if no data
		if (( data == null )|| data.equals( "" ) ) { 
		    log_d( "read no data" );
			return null;
		}
		NodeHash hash = new NodeHash();
		hash.setFileData( data );
		if ( !hash.isValidNode() ) {
			log_d( "read hash is invalid" );
			return null;
		}
		return hash;
	}
				
}