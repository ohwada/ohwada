package jp.ohwada.android.yag1.task;

/**
 * PlaceList Async Task
 */
public class PlaceListAsync extends CommonAsyncTask {

	/**
	 * === constructor ===
	 */			 
    public PlaceListAsync() {
        super();
        TAG_SUB = "PlaceListAsync";
    }

	/**
	 * execBackground
	 */  	
	protected void execBackground() {
		mResult = getHttp();
	}
	
	/**
	 * getPlaceList
	 */  	
	private String getHttp() {
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		query += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
		query += "PREFIX schema: <http://schema.org/> ";
		query += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ";
		query += "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";
		query += "PREFIX foaf: <http://xmlns.com/foaf/0.1/> ";
		query += "PREFIX place: <http://fp.yafjp.org/terms/place#> ";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "?place rdf:type place:Place; ";
		query += "rdfs:label ?label; ";
		query += "rdfs:label ?hrkt; ";
		query += "geo:lat ?lat; ";
		query += "geo:long ?long . ";
		query += "OPTIONAL { ?place schema:address ?address . } ";
		query += "OPTIONAL { ?place schema:telephone ?telephone . } ";
		query += "OPTIONAL { ?place owl:sameAs ?same_as . } ";
		query += "OPTIONAL { ?place place:isPartOf ?is_part_of . }  ";
		query += "OPTIONAL { ?place place:contact ?contact . "; 
		query += "?contact foaf:homepage ?homepage . } ";
		query += "FILTER (lang(?label) =\"ja\" ) ";
		query += "FILTER (lang(?hrkt) =\"ja-hrkt\" ) ";
		query += "} ";
		query += "ORDER BY ASC(?hrkt) ";
		return getResult( query );	
	}	    		   
}