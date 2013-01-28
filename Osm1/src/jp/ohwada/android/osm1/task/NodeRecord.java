package jp.ohwada.android.osm1.task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.ohwada.android.osm1.Constant;

/**
 * Node Record
 */
public class NodeRecord {

	// debug
	private static final boolean D = Constant.DEBUG;
			
	// constance
	private final static String URL_NODE = "http://linkedgeodata.org/triplify/node";
	private final static String URL_ONTOLOGY = "http://linkedgeodata.org/ontology/";
	private final static String URL_RESOURCE = "http://ja.dbpedia.org/resource/";
	private final static String URL_PAGE = "http://ja.dbpedia.org/page/";
	private final static String URL_WIKIPEDIA = "http://ja.wikipedia.org/wiki/";

	public final static String TAB = Constant.TAB;
	
	// NOT work "|" 
	public final static String BAR = ";";
			
	// varibale
	public String node = "";
	public String label = "";
	public String label_ja = "";
	public String direct_type = "";
	public String direct_label_ja = "";
	public String geometry = "";
	public String lat = "";
	public String lng = "";
					
	// addtional
	public String name_node = "";
	public String direct_label = "";
	public String map_color = "";
	public int map_lat = 0;
	public int map_lng = 0;

	/**
	 * === constractor ===
	 */
    public NodeRecord() {
    	// dummy
    }

	/**
	 * === constractor ===
	 * @param String line
	 */
    public NodeRecord( String line ) {
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
		if ( cols.length < 8 ) return;
		node = cols[0];
		label = cols[1];
		label_ja = cols[2];
		direct_type = cols[3];					
		direct_label_ja = cols[4];	
		geometry = cols[5];
		lat = cols[6];
		lng = cols[7];
	}
	
	/**
	 * setAddtional
	 */
    public void setAddtional() {	
    	name_node = getNodeName( node );	
    	direct_label = getOntologyName( direct_type );	
		map_lat = strToE6( lat );
		map_lng = strToE6( lng );
	}
	
	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
    	String data = "";
		data += node + TAB;
		data += label + TAB;
		data += label_ja + TAB;
		data += direct_type + TAB;
		data += direct_label_ja + TAB;
		data += geometry + TAB;
		data += lat + TAB;
		data += lng + TAB ;	
		data += "*";	// end mark	
		return data;	
	}
		                
	/**
	 * getLabeleJa 
	 * @return String
	 */
    public String getLabeleJa() {
		String ja = label;
		if ( !"".equals( label_ja ) ) {
			ja = label_ja;
		}
		return ja;
	}

	/**
	 * getDirectLabeleJa 
	 * @return String
	 */
    public String getDirectLabeleJa() {
		String ja = direct_label;
		if ( !"".equals( direct_label_ja ) ) {
			ja = direct_label_ja;
		}
		return ja;
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
	 * getNodeName
	 * @param String url
	 * @return String 
	 */
    private String getNodeName( String url ) {
		return url.replaceFirst( URL_NODE, "" );
    }

	/**
	 * getOntologyName
	 * @param String url
	 * @return String 
	 */
    private String getOntologyName( String url ) {
		return url.replaceFirst( URL_ONTOLOGY, "" );
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