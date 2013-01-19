package jp.ohwada.android.yag1.task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.yag1.Constant;

/**
 * Place Record
 */
public class PlaceRecord {

	// debug
	private static final boolean D = Constant.DEBUG;
		
	// constance
	private final static String HTTP_PLACE = "http://yan.yafjp.org/place-info/";
	private final static String URL_RESOURCE = "http://ja.dbpedia.org/resource/";
	private final static String URL_PAGE = "http://ja.dbpedia.org/page/";
	private final static String URL_WIKIPEDIA = "http://ja.wikipedia.org/wiki/";
	private final static String TAB = Constant.TAB;

	// NOT work "|" 
	public final static String BAR = ";";
			
	// varibale
	public String url = "";
	public String label = "";
	public String reading = "";
	public String created = "";
	public String modified = "";
	public String place_abstract = "";
	public String address = "";
	public String telephone  = "";
	public String homepage = "";
	public String dbpedia = "";			
	public String lat = "";
	public String lng  = "";
	public String is_part_of = "";
	public String has_part = "";
	public String parent_url = "";
	public String child_urls = "";
			
	// addtional
	public String name = "";
	public String wiki = "";
	public int map_lat = 0;
	public int map_lng = 0;
	public boolean event_flag = false;
	
	/**
	 * === constractor ===
	 */
    public PlaceRecord() {
    	// dummy
    }

	/**
	 * === constractor ===
	 * @param String line
	 */
    public PlaceRecord( String line ) {
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
		if ( cols.length < 16 ) return;
		url = cols[0];
		label = cols[1];
		reading = cols[2];
		created = cols[3];
		modified = cols[4];
		place_abstract = cols[5];
		address = cols[6];
		telephone  = cols[7];
		homepage = cols[8];	
		dbpedia = cols[9];							
		lat = cols[10];
		lng = cols[11];
		is_part_of = cols[12];
		has_part = cols[13];
		parent_url = cols[14];
		child_urls = cols[15];
	}
	
	/**
	 * setAddtional
	 */
    public void setAddtional() {		
		name = getPlaceName( url );
		wiki = dbpediaToWiki( dbpedia );
		map_lat = strToE6( lat );
		map_lng = strToE6( lng );
	}
	
	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
    	String data = "";
		data += url + TAB;
		data += label + TAB;
		data += reading + TAB;
		data += created + TAB;
		data += modified + TAB;
		data += place_abstract + TAB;								
		data += address + TAB;
		data += telephone + TAB;		
		data += homepage + TAB;			
		data += dbpedia + TAB;	
		data += lat + TAB;
		data += lng + TAB ;	
		data += is_part_of + TAB ;	
		data += has_part + TAB ;	
		data += parent_url + TAB ;	
		data += child_urls + TAB ;	
		data += "*";	// end mark	
		return data;	
	}

	/**
	 * getParentUrl
	 * @return String
	 */
    public String getParentUrl() {
    	if ( !"".equals( parent_url ) ) return parent_url;
        return url;
    }

	/**
	 * getListHasPart
	 * @return List<String>
	 */
    public List<String> getListHasPart() {
        return stringToList( has_part );
    }

	/**
	 * getListChildUrls
	 * @return List<String>
	 */        
    public List<String> getListChildUrls() {
    	return stringToList( child_urls );
    }
 
 	/**
	 * setChildUrls
	 * @param List<String> list
	 */   
    public void setChildUrls( List<String> list ) {
   		child_urls = listToString( list );
    }

 	/**
	 * stringToList
	 * @param String str
	 * @return List<String>
	 */  
    private List<String> stringToList( String str ) {
    	if ( "".equals( str ) ) return null;
		String[] array = str.split( BAR );
    	List<String> list = new ArrayList<String>();
    	if (( array == null )||( array.length == 0 )) return list;
    	for ( int i=0; i<array.length; i++ ) {
    		String a = array[ i ];
    		if ( !"".equals( a ) ) { 
				list.add( a );
			}
		}	
		return list;
    }

 	/**
	 * listToString
	 * @param List<String> list
	 * @return String
	 */         
    private String listToString( List<String> list ) {
    	String ret = "";
    	if (( list == null )||( list.size() == 0 )) return ret;
    	Map<String, Boolean> map = new HashMap<String, Boolean>(); 
		for ( int i=0; i<list.size(); i++ ) {
			String	 url = list.get( i );
			// check double
			if ( !map.containsKey( url ) ) {
				map.put( url, true );
				// bar mark is unnecessary at first
				if ( i != 0 ) ret += BAR;
				ret += url;
			}
		}
		return ret;
    }
            
	/**
	 * convert string into E6 format 
	 * @param String : String of real number format
	 * @return int : Integer of E6 format
	 */
    protected int strToE6( String str ) {
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
	 * getPlaceName
	 * @param String url
	 * @return String 
	 */
    protected String getPlaceName( String url ) {
		return url.replaceFirst( HTTP_PLACE, "" );
    }
    
    /**
	 * dbpedia To Wiki
	 * @param String str
	 * @return String
	 */     
	private String dbpediaToWiki( String str ) {
		if (( str == null )|| "".equals( str ) )  return "";
		String wiki = ""; 
		if ( str.startsWith( URL_RESOURCE ) ) {
			String name = str.replaceFirst( URL_RESOURCE, "" );
			try {
				wiki = URL_WIKIPEDIA + URLEncoder.encode( name, "UTF-8" );
			} catch (UnsupportedEncodingException e) {
				if (D) e.printStackTrace();
			}
		}
		if ( str.startsWith( URL_PAGE ) ) {
			String name = str.replaceFirst( URL_PAGE, "" );
			wiki = URL_WIKIPEDIA + name ;
		}
		return wiki;
	}

}