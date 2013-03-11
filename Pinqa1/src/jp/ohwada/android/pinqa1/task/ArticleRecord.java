package jp.ohwada.android.pinqa1.task;

import jp.ohwada.android.pinqa1.Constant;

/**
 * Article Record
 */
public class ArticleRecord {

	// debug
	private static final boolean D = Constant.DEBUG;
			
	// constant
	public final static String TAB = Constant.TAB;
	private static final String HTTP_ARTICLES = "http://lod.pinqa.com/resource/articles/";
	private static final String HTTP_TOPICS = "http://lod.pinqa.com/resource/topics/";
	private static final String HTTP_CATEGORIES = "http://lod.pinqa.com/resource/categories/";
	private static final String FILE_PREFIX_IMAGE = Constant.FILE_PREFIX_IMAGE + "_";
	private static final String STAR = "*";
     			
	// variable
	public String article_url = "";
	public String article_label = "";
	public String topic_url = "";
	public String topic_label = "";
	public String category_url = "";
	public String category_label = "";
	public String source = "";
	public String description = "";
	public String image_url = "";
	public String lat = "";
	public String lng = "";
					
	// addtional
	public int article_id = 0;
	public int topic_id = 0;
	public int category_id = 0;
	public String image_name = "";
	public String map_color = "";
	public int map_lat = 0;
	public int map_lng = 0;

	/**
	 * === constractor ===
	 */
    public ArticleRecord() {
    	// dummy
    }

	/**
	 * === constractor ===
	 * @param String line
	 */
    public ArticleRecord( String line ) {
    	setFileData( line );
    	setAddtional();
    }
            
	/**
	 * setFileData
	 * @param String line
	 */
    public void setFileData( String line ) {
    	if ( line == null ) return;
		String[] cols = line.split( TAB );
		if ( cols.length < 11 ) return;
		article_url = cols[0];
		article_label = cols[1];
		topic_url = cols[2];
		topic_label = cols[3];
		category_url = cols[4];
		category_label = cols[5];
		source = cols[6];
		description = cols[7];
		image_url  = cols[8];
		lat = cols[9];
		lng = cols[10];
	}

	/**
	 * setAddtional
	 */
    public void setAddtional() {		
		article_id = getArticleId( article_url );
		topic_id = getTopicId( topic_url );
		category_id = getCategoryId( category_url );
		image_name = parseImageName( article_id, image_url );		
		map_lat = strToE6( lat );
		map_lng = strToE6( lng );	
	}
	
	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
    	String data = "";
		data += article_url + TAB;
		data += article_label + TAB;
		data += topic_url + TAB;
		data += topic_label + TAB;
		data += category_url + TAB;
		data += category_label + TAB;
		data += source + TAB;
		data += description + TAB;
		data += image_url + TAB;
		data += lat + TAB;
		data += lng + TAB ;	
		data += STAR;		// end mark	
		return data;	
	}

	/**
	 * get ArticleId
	 * @param String url
	 * @return int
	 */    
    public int getArticleId( String url ) {
		String name = url.replaceFirst( HTTP_ARTICLES, "" );
    	return parseInt( name );
    }

	/**
	 * get TopicId
	 * @param String url
	 * @return int
	 */   
    public int getTopicId( String url ) {
    	String name = url.replaceFirst( HTTP_TOPICS, "" );
    	return parseInt( name );
    }

	/**
	 * get CategoryId
	 * @param String url
	 * @return int
	 */  
    public int getCategoryId( String url ) {
    	String name = url.replaceFirst( HTTP_CATEGORIES, "" );
    	return parseInt( name );
    }
        
	/**
	 * parse ImageName
	 * @param String name
	 * @param String image_url
	 * @return String 
	 */
	private String parseImageName( int id, String image_url ) {
		if ( "".equals( image_url ) ) return "";
		String ext = parseExt( image_url );
		String str = FILE_PREFIX_IMAGE + id + "." + ext ;
		return str;
	}
	    			                
	/**
	 * convert string into E6 format 
	 * @param String : String of real number format
	 * @return int : Integer of E6 format
	 */
    private int strToE6( String str ) {
    	int ret = 0;
    	if (( str == null )||( "".equals( str ))) return ret;
        try {
        	Double d = Double.parseDouble( str ) * 1E6;
        	ret = d.intValue();
        } catch (Exception e) {
            if (D) e.printStackTrace();
        }
        return ret;
	}

	/**
	 * parseInt
	 * @param String str 
	 * @return int
	 */ 
    private int parseInt( String str ) {
        if (( str == null )|| "".equals( str ) ) return 0;
        int num = 0;
		try {
           num = Integer.parseInt( str );
		} catch (Exception e) {
            if (D) e.printStackTrace();
        }
        return num;
	}
	    
	/**
	 * parseExt
	 * @param String name 
	 * @return String
	 */ 	
	private String parseExt( String name ) {
		int point = name.lastIndexOf(".");
		if ( point != -1 ) {
			String str = name.substring( point + 1 );
			return str.toLowerCase();
		}
		return "";
	}
      
}