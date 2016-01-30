package jp.ohwada.android.osm1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;

/**
 * UriGeo
 */
public class UriGeo {

	// public member	
	public double lat = 0;
	public double lng = 0;	
	public boolean flag = false;

	// private
	private static Pattern mPattern = Pattern.compile( "geo:(\\d+\\.\\d+),(\\d+\\.\\d+)" );

	/**
	 * === Constructor ===
	 */ 
	public UriGeo() {
		// dummy
	}
	
	/**
	 * === Constructor ===
	 * @param Intent intent
	 */ 
	public UriGeo( Intent intent ) {
		getPoint( intent );
	}

	/**
	 * getPoint
	 * @param Intent intent
	 */ 					
	public void getPoint( Intent intent ) {
		if ( intent == null ) return;
		Uri uri = (Uri) intent.getData();
		getPoint( uri );
	}

	/**
	 * getPoint
	 * @param Uri uri
	 */ 		
	public void getPoint( Uri uri ) {
		if ( uri == null ) return;
		getPoint( uri.toString() );
	}

	/**
	 * getPoint
	 * @param String str
	 */ 		
	public void getPoint( String str ) {
		if ( str == null ) return;
		Matcher m = mPattern.matcher( str );
		if ( m.find() ){
			flag = true;
			lat = Double.parseDouble( m.group(1) );
			lng = Double.parseDouble( m.group(2) );				
		}	
	}
		
}
