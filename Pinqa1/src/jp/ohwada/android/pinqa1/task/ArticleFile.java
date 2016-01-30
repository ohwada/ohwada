package jp.ohwada.android.pinqa1.task;

import java.io.File;

import jp.ohwada.android.pinqa1.Constant;

/**
 * Article file 
 */
public class ArticleFile extends CommonFile { 

	// constant
	private static final String FILE_PREFIX = Constant.FILE_PREFIX_NODE + "_";;
	private static final long EXPIRE_ARTICLE = Constant.EXPIRE_DAYS_ARTICLE * TIME_MSEC_ONE_DAY;  // 2     	
	/**
	 * === constractor ===
	 */
    public ArticleFile() {
    	super();
    	TAG_SUB = "ArticleFile";
    }
		
	/**
	 * getFile
	 * @param Date date
	 * @return File
	 */
    public File getFile( int id ) {    
		return getFileWithTxt( FILE_PREFIX + id );
	}
		
	/**
	 * isExpired Article
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, EXPIRE_ARTICLE );
	}

	/**
	 * write Article
	 * @param File file,
	 * @param List<EvenRecord> list
	 */
	public void write( File file, ArticleRecord r ) {
		writeAfterDelete( file, r.getFileData() );
	}

	/**
	 * read Article
	 * @param File file
	 * @return Article
	 */
    public ArticleRecord read( File file ) {
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) { 
		    log_d( "read no data" );
			return null;
		}	
		ArticleRecord r = new ArticleRecord( data );
		return r;
	}
 				
}