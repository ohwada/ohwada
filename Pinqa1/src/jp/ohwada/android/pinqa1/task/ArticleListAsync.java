package jp.ohwada.android.pinqa1.task;

import jp.ohwada.android.pinqa1.R;
import android.content.Context;

/**
 * ArticleList Async Task
 */
public class ArticleListAsync extends CommonAsyncTask {

	// constant
	private final static int AREA = 20000;	// 2km

	// object
	private PinqaHttpClient mClient;

	// local variable
	private int mLat = 0;
	private int mLong = 0;
	private String mResult = "";

	/**
	 * === constructor ===
	 */			 
    public ArticleListAsync( Context context ) {
        super( context );
    }

	/**
	 * setGeo
	 * @return int lat 
	 * @return int lng 
	 */ 
	public void setGeo( int lat, int lng ) {
		mLat = lat;
		mLong = lng;
	}

	/**
	 * get Result
	 * @return String 
	 */  	
	public String getResult() {
		return mResult;
	}

	/**
	 * shutdown
	 */ 
    public void shutdown() {
		if ( mClient != null ) {
    		mClient.shutdown();
    	}
    }
	
	/**
	 * execBackground
	 */  	
	protected void execPreExecute() {
    	showProgress( R.string.loading );
    	mResult = null;
	}

	/**
	 * execBackground
	 */
    protected void execBackground() {
        mClient = new PinqaHttpClient();
		mResult = mClient.execute( getQuery() );
    }

	/**
	 * execProgressUpdate
	 */  	
	protected void execPostExecute() {
        hideProgress();
	}

	/**
	 * getQuery
	 * @return String
	 */  	
	 private String getQuery() {
		String query = "";
		query += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
		query += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		query += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ";
//		query += "PREFIX dc: <http://purl.org/dc/elements/1.1/> ";
		query += "PREFIX pinqa: <http://lod.pinqa.com/resource/vocab/> ";
		query += "SELECT * ";
		query += "WHERE { ";
		query += "?topic rdf:type pinqa:Topic; ";
		query += "  rdfs:label ?topic_label; ";
		query += "  pinqa:topics_category ?category; ";
		query += "  pinqa:topics_article ?article. ";
		query += "?category rdfs:label ?category_label. ";
		query += "?article rdfs:label ?article_label; ";
		query += "  geo:lat ?lat; ";
		query += "  geo:long ?long. ";
//		query += "OPTIONAL { ?article dc:source ?source . } ";
//		query += "OPTIONAL { ?article dc:description ?description . } ";
//		query += "OPTIONAL { ?article pinqa:articles_original_image_url ?image_url . } ";
		query += getFilterLocation();
		query += "} ";
		query += "ORDER BY ASC(?article) ";
		return query;	
	}

	/**
	 * getFilterLocation
	 * @return String
	 */ 
	private String getFilterLocation() {
		String query = "FILTER ( ";
		query += "?lat > " + e6ToString( mLat - AREA ) + " && ";
		query += "?lat < " + e6ToString( mLat + AREA ) + " && ";
		query += "?long > " + e6ToString( mLong - AREA ) + " && ";
		query += "?long < " + e6ToString( mLong + AREA ) + " ) ";
		return query;
	}

	/**
	 * e6ToString
	 * @param int e6
	 * @return String
	 */ 
	private String e6ToString( int e6 ) {
		double d = (double) e6 / 1E6;
		String str = Double.toString( d );
		return str;
	}	    		   
}