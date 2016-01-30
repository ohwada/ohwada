package jp.ohwada.android.pinqa1.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ArticleList JSON Parser
 */
public class ArticleListParser extends CommonParser {
		
    /**
	 * === constarctor ===
	 */
    public ArticleListParser() {
		TAG_SUB = "ArticleListParser";
    }

	/**
	 * parse 
	 * @param String str
	 * @return ArticleList
	 */   
	public ArticleList parse( String str ) {  
	 	ArticleList list = new ArticleList();
		Map<String, Boolean> map = new HashMap<String, Boolean>(); 
			 		
	 	JSONArray bindings = getBindings( str );
		if (( bindings == null )||( bindings.length() == 0 )) return null;

		// each binding
		for ( int i=0; i<bindings.length(); i++ ) {
		
			JSONObject obj = getObjectFromArray( bindings, i );		
			if ( obj == null ) continue;

			// check double url
			String article = getStringValue( obj, "article" );
			if ( map.containsKey( article ) ) continue;

			map.put( article, true );
			ArticleRecord r = new ArticleRecord();	
			r.article_url = article;
			r.article_label = getStringValue( obj, "article_label" );
			r.topic_url = getStringValue( obj, "topic" );
			r.topic_label = getStringValue( obj, "topic_label" );
			r.category_url = getStringValue( obj, "category" );
			r.category_label = getStringValue( obj, "category_label" );	
//			r.description = getStringValue( obj, "description" );
//			r.source = getStringValue( obj, "source" );
//			r.image_url = getStringValue( obj, "mage_url" );
			r.lat = getStringValue( obj, "lat" );	
			r.lng = getStringValue( obj, "long" );	
			list.add( r );
		}	
		return list;
	}

}