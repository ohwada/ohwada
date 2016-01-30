package jp.ohwada.android.osm2.task;

import java.util.Map;

import jp.ohwada.android.osm2.Constant;

/**
 * Node Record
 */
public class NodeRecord {
	
	// debug
	private static final boolean D = Constant.DEBUG;

	// key
	public final static String KEY_STATUS = "status" ;
	public final static String KEY_RDF_LABEL = "rdf:label" ;
	public final static String KEY_RDF_LABEL_JA = "rdf:label:ja" ;
	public final static String KEY_GEO_GEOMETRY = "geo:geometry" ;
	public final static String KEY_GEO_LAT = "geo:lat" ;
	public final static String KEY_GEO_LONG = "geo:long" ;
	public final static String KEY_LGDO_NODE = "lgdo:Node" ;
	public final static String KEY_LGDO_DIRECT_TYPE = "lgdo:directType" ;
	public final static String KEY_LGDO_DIRECT_TYPE_LABEL_JA = "lgdo:directType:label_ja" ;
	public final static String KEY_LGDO_PHONE = "lgdp:phone";
	public final static String KEY_LGDO_CONTACT_PHONE = "lgdp:contact%3Aphone";
	public final static String KEY_LGDP_KSJ2_OPC = "lgdp:KSJ2%3AOPC";
	public final static String KEY_LGDP_KSJ2_LIN = "lgdp:KSJ2%3ALIN";

	// direct type
	private final static String DIRECT_LABEL_RAILWAY_STATION = "RailwayStation";
				
	// url
	private final static String URL_NODE = "http://linkedgeodata.org/triplify/node";
	private final static String URL_ONTOLOGY = "http://linkedgeodata.org/ontology/";

	// code	
	private final static String TAB = Constant.TAB;
	private final static String LF = Constant.LF;
				
	// varibale
	public int status = 0;
	public String node = "";
	public String label = "";
	public String label_ja = "";
	public String direct_type = "";
	public String direct_label_ja = "";
	public String geometry = "";
	public String lat = "";
	public String lng = "";
	public String ksj2_opc = "";
	public String ksj2_lin = "";
		
	// addtional
	public int node_id = 0;
	public String direct_label = "";
	public String map_color = "";
	public String contact_phone = "";
	public String phone = "";
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
	 * === constractor ===
	 * @param Map<String, String> map
	 */
    public NodeRecord( Map<String, String> map ) {
    	status = strToInt( getStringFromHash( map, KEY_STATUS ) );
		node = getStringFromHash( map, KEY_LGDO_NODE );
    	label = getStringFromHash( map, KEY_RDF_LABEL );
		label_ja = getStringFromHash( map, KEY_RDF_LABEL_JA );
		direct_type = getStringFromHash( map, KEY_LGDO_DIRECT_TYPE );
		direct_label_ja = getStringFromHash( map, KEY_LGDO_DIRECT_TYPE_LABEL_JA );
		geometry = getStringFromHash( map, KEY_GEO_GEOMETRY );	
		lat = getStringFromHash( map, KEY_GEO_LAT );	
		lng = getStringFromHash( map, KEY_GEO_LONG );
		ksj2_opc = getStringFromHash( map, KEY_LGDP_KSJ2_OPC ); 
		ksj2_lin = getStringFromHash( map, KEY_LGDP_KSJ2_LIN ); 
		contact_phone = getStringFromHash( map, KEY_LGDO_CONTACT_PHONE ); 
		phone = getStringFromHash( map, KEY_LGDO_PHONE ); 
    	setAddtional();
    }

	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
    	String data = "";
		data += status + TAB;
		data += node + TAB;
		data += label + TAB;
		data += label_ja + TAB;
		data += direct_type + TAB;
		data += direct_label_ja + TAB;
		data += geometry + TAB;
		data += lat + TAB;
		data += lng + TAB ;	
		data += ksj2_opc + TAB ;	
		data += ksj2_lin + TAB ;	
		data += "*";	// end mark	
		return data;	
	}

	/**
	 * setFileData
	 * @param String line
	 */
    public void setFileData( String line ) {
    	if ( line == null ) return;
		String[] cols = line.split( TAB );
		if ( cols.length < 11 ) return;
		status = strToInt( cols[0] );
		node = cols[1];
		label = cols[2];
		label_ja = cols[3];
		direct_type = cols[4];					
		direct_label_ja = cols[5];	
		geometry = cols[6];
		lat = cols[7];
		lng = cols[8];
		ksj2_opc = cols[9];
		ksj2_lin = cols[10];
	}
		
	/**
	 * getStringFromHash
	 * @param Map<String, String> map
	 * @return String
	 */
    private String getStringFromHash( Map<String, String> map, String key ) {
		String str = map.containsKey( key ) ? map.get( key ) : "";
		return str;
	}
	
	/**
	 * setAddtional
	 */
    public void setAddtional() {	
    	node_id = getNodeId( node );	
    	direct_label = getOntologyName( direct_type );	
		map_lat = strToE6( lat );
		map_lng = strToE6( lng );
	}
	


	/**
	 * isValidNode
	 * @return boolean
	 */
	public boolean isValidNode() {
		if (( node == null )|| "".equals( node ) ) return false;
		if ( !node.startsWith( URL_NODE ) ) return false; 
		return true;
	}
			                
	/**
	 * getLabeleJa 
	 * @return String
	 */
    public String getLabeleJa() {
		String ja = label;
		if (( label_ja != null )&& !"".equals( label_ja ) ) {
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
	 * getNodeId
	 * @param String url
	 * @return int 
	 */
    public int getNodeId( String url ) {
    	if (( url == null )||( "".equals(url) )) return 0;
		String name = url.replaceFirst( URL_NODE, "" );
		return Integer.parseInt( name );
    }

	/**
	 * getOntologyName
	 * @param String url
	 * @return String 
	 */
    private String getOntologyName( String url ) {
    	if (( url == null )||( "".equals(url) )) return "";
		return url.replaceFirst( URL_ONTOLOGY, "" );
    }

	/**
	 * geSnippet
	 * @param NodeRecord r
	 * @return String 
	 */
	public String getMarkerSnippet( ) {
		String str = "";
		String direct = getDirectLabeleJa();
		String extra = getExtra( LF );
		if ( checkString( extra ) )  {
			str = direct + LF + extra;
		} else {
			str = direct ;
		}
		return str;
	}

	/**
	 * getExtra
	 * @param String glue
	 * @return String 
	 */ 
	public String getExtra( String glue ) {
		if ( DIRECT_LABEL_RAILWAY_STATION.equals( direct_label ) ) {
			return getExtraRailStation( glue );
		}
		return "";
	}
		
	/**
	 * getExtraRailStation
	 * @param String glue
	 * @return String 
	 */ 
	private String getExtraRailStation( String glue ) {
		String str = "";
		if ( checkString( ksj2_opc ) && checkString( ksj2_lin ) ) {
			str = ksj2_opc + glue + ksj2_lin;	
		} else if ( checkString( ksj2_opc ) ) {
			str = ksj2_opc;	
		} else if ( checkString( ksj2_lin ) ) {
			str = ksj2_lin;			
		}
		return str;
	}

	/**
	 * getPhone
	 * @return String
	 */
    public String getPhone() {
		String str = "";
		if ( checkString( contact_phone ) ) {
			str = contact_phone;	
		} else if ( checkString( phone ) ) {
			str = phone;	
		}
		return str;
	}
	
	/**
	 * checkString
	 * @param String str
	 * @return boolean 
	 */ 
	private boolean checkString( String str ) {
		if (( str != null )&&( str.length() >0 )) return true;
		return false;
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
	 * convert string into E6 format 
	 * @param String : String of real number format
	 * @return int : Integer of E6 format
	 */
    private int strToInt( String str ) {
    	int ret = 0;
    	if (( str == null )||( "".equals( str ))) return ret;
        try {
        	ret = Integer.parseInt( str );
        } catch (Exception e) {
            if (D) e.printStackTrace();
        }
        return ret;
	}

}