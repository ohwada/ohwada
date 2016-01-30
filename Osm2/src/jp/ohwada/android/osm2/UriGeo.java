package jp.ohwada.android.osm2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;

/**
 * UriGeo
 */
public class UriGeo {

	private static final boolean D = Constant.DEBUG;
	
	// public member	
	public double d_lat = 0;
	public double d_long = 0;
	public int e6_lat = 0;
	public int e6_long = 0;		
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
			d_lat = strToDouble( m.group(1) );
			d_long = strToDouble( m.group(2) );
			e6_lat = doubleToE6( d_lat );
			e6_long = doubleToE6( d_long );				
		}	
	}

	/**
	 * convert string into double 
	 * @param String : String of real number format
	 * @return double
	 */
    private double strToDouble( String str ) {
    	double ret = 0;
    	if (( str == null )||( "".equals( str ))) return ret;
        try {
        	ret = Double.parseDouble( str );
        } catch (Exception e) {
            if (D) e.printStackTrace();
        }
        return ret;
	}
	
	/**
	 * convert real number to integer
	 * @param Double : location( floating point format )
	 * @return int : location( E6 format )
	 */
    private int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}		
}
