package jp.ohwada.android.pinqa1.task;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Article JSON Parser
 */
public class ArticleParser extends CommonParser {

	// predicate scheme
	private String P_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	private String P_SOURCE = "http://purl.org/dc/elements/1.1/source";
	private String P_DESCRIPTION = "http://purl.org/dc/elements/1.1/description";
	private String P_LAT = "http://www.w3.org/2003/01/geo/wgs84_pos#lat";
	private String P_LONG = "http://www.w3.org/2003/01/geo/wgs84_pos#long";
	private String P_TOPIC = "http://lod.pinqa.com/resource/vocab/articles_topic";
	private String P_IMAGE_URL = "http://lod.pinqa.com/resource/vocab/articles_original_image_url";	
		
    /**
	 * === constarctor ===
	 */
    public ArticleParser() {
		TAG_SUB = "ArticleParser";
    }

	/**
	 * parse 
	 * @param String str
	 * @return ArticleRecord
	 */     
	public ArticleRecord parse( String str ) {
		ArticleRecord r = new ArticleRecord();										
		JSONArray bindings = getBindings( str );
		if (( bindings == null )||( bindings.length() == 0 )) return null;
		
		// each binding
		for ( int i=0; i<bindings.length(); i++ ) {
 
			// parse binding
			JSONObject obj = getObjectFromArray( bindings, i );		
			if ( obj == null ) continue;

			String p = getStringValue( obj, "p" );
			String o = getStringValue( obj, "o" );

			if ( P_LABEL.equals( p ) ) {
				r.article_label = o;
			} else if ( P_SOURCE.equals( p ) ) {
				r.source = o;
			} else if ( P_DESCRIPTION.equals( p ) ) {
				r.description = o;				
			} else if ( P_LAT.equals( p ) ) {
				r.lat = o;
			} else if ( P_LONG.equals( p ) ) {
				r.lng = o;		
			} else if ( P_TOPIC.equals( p ) ) {
				r.topic_label = o;					
			} else if ( P_IMAGE_URL.equals( p ) ) {
				r.image_url = o;
			} 
		}
		return r;
	}

}