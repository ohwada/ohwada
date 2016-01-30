package jp.ohwada.android.pinqa1.task;

import java.io.File;
import java.util.List;

import jp.ohwada.android.pinqa1.Constant;

/**
 * ArticleList file 
 */
public class ArticleListFile extends CommonFile { 

	// constant
	private static final String FILE_PREFIX = Constant.FILE_PREFIX_ARTICLE + "_";
	private static final long EXPIRE_ARTICLE_LIST = Constant.EXPIRE_DAYS_ARTICLE_LIST * TIME_MSEC_ONE_DAY;  // 2 day
    	
	/**
	 * === constractor ===
	 */
    public ArticleListFile() {
        super();
    	TAG_SUB = "ArticleListFile";
    }
		
	/**
	 * getFile
	 * @param Date date
	 * @return File
	 */
    public File getFile( int lat, int lng ) {
    	String name = FILE_PREFIX + e6ToE3( lat ) + "_" + e6ToE3( lng );
		return getFileWithTxt( name );
	}
		
	/**
	 * isExpired Article
	 * @param File file
	 * @return boolean
	 */
    public boolean isExpired( File file ) {
		return isExpiredFile( file, EXPIRE_ARTICLE_LIST );
	}

	/**
	 * write ArticleList
	 * @param File file,
	 * @param List<EvenRecord> list
	 */
	public void write( File file, List<ArticleRecord> list ) {
		write( file, new ArticleList( list ) );
	}
			
	/**
	 * write ArticleList
	 * @param File file
	 * @param ArticleList list
	 */
	public void write( File file, ArticleList list ) {
		String data = list.buildWriteData();
    	writeAfterDelete( file, data );
	}

	/**
	 * read ArticleList
	 * @param File file
	 * @return ArticleList
	 */
    public ArticleList read( File file ) {
		ArticleList list = new ArticleList();
		String data = readData( file );
		// if no data
		if (( data == null )|| data.equals( "" ) ) { 
		    log_d( "read no data" );
			return list;
		}	
		String[] lines = data.split( LF );
		// build read data list
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( !"".equals( line ) ) { 
				list.addWithMapColor( new ArticleRecord( line ) );
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