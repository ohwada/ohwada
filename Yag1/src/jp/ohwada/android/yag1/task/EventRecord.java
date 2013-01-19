package jp.ohwada.android.yag1.task;

import jp.ohwada.android.yag1.Constant;

/**
 * Event Record
 */
public class EventRecord {
	
	// tab
	private static final String HTTP_EVENT = "http://yan.yafjp.org/event/";
	private final static String TAB = Constant.TAB;
	
	// varibale
	public String event_label = "";
	public String event_url = "";
	public String place_label = "";
	public String place_url = "";
	public String created = "";
	public String modified = "";
	public String dtstart = "";
	public String dtend = "";
	public String event_abstract = "";
	public String category = "";
	// 10
	public String schedule = "";
	public String fee = "";
	public String care = "";
	public String apinfo = "";
	public String apstart = "";
	public String apend = "";				
	public String homepage = "";
	public String image_url = "";
	public String participant = "";	
	public String status = "";	
	// 20	
	public String contact = "";
	public String phone  = "";

	// addtional
	public String name = "";
	public String date_start = "";
	public String date_end = "";
	public String date_bar = "";
	public String date_apstart = "";
	public String date_apend = "";
	public String image_name = "";
			
	/**
	 * === constractor ===
	 */
    public EventRecord() {
    	// dummy
    }

	/**
	 * === constractor ===
	 * @param String line
	 */
    public EventRecord( String line ) {
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
		if ( cols.length < 22 ) return;
		event_url = cols[0];
		event_label = cols[1];
		place_label = cols[2];
		place_url = cols[3];
		created = cols[4];
		modified = cols[5];
		dtstart = cols[6];
		dtend = cols[7];
		event_abstract = cols[8];
		category = cols[9];
		// 10
		schedule = cols[10];
		fee = cols[11];
		care = cols[12];
		apinfo = cols[13];
		apstart = cols[14];
		apend = cols[15];										
		homepage = cols[16];
		image_url = cols[17];
		participant = cols[18];	
		status = cols[19];
		// 20
		contact = cols[20];
		phone = cols[21];	
	}
	
	/**
	 * setAddtional
	 */
    public void setAddtional() {	
		name = getEventName( event_url );
		image_name = parseImageName( name, image_url );		
		date_start = parseDate( dtstart );
		if ( !dtstart.equals( dtend ) ) {
			date_end = parseDate( dtend );
			date_bar = "-";
		}
		date_apstart = parseDate( apstart ) ;
		date_apend = parseDate( apend );
		image_name = parseImageName( name, image_url );				
	}
	
	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
    	String data = "";
		data += event_url + TAB;
		data += event_label + TAB;
		data += place_label + TAB ;	
		data += place_url + TAB ;	
		data += created + TAB;
		data += modified + TAB;
		data += dtstart + TAB;
		data += dtend + TAB;
		data += event_abstract + TAB;	
		data += category + TAB;
		// 10
		data += schedule + TAB;
		data += fee + TAB;
		data += care + TAB;
		data += apinfo + TAB;
		data += apstart + TAB;
		data += apend + TAB;												
		data += homepage + TAB;
		data += image_url + TAB;
		data += participant + TAB;
		data += status  + TAB;
		// 20
		data += contact + TAB;
		data += phone + TAB;
		data += "*";	// end mark				
		return data;	
	}

    /**
	 * parse Date
	 * 2011-07-01T00:00:00+09:00 => 2011-07-01
	 * @param String date	 
	 * @return String
	 */ 	
	private String parseDate( String date ) {
		String str = "";
		if ( !"".equals( date ) ) {
			String[] strAry = date.split( "T" );
			str = strAry[0];
		}
		return str;
	}
		
	/**
	 * getEventName
	 * @param String url
	 * @return String 
	 */
    public String getEventName( String url ) {
		String name = url.replaceFirst( HTTP_EVENT, "" );
    	return name;
    }

	/**
	 * parseImageName
	 * @param String name
	 * @param String image_url
	 * @return String 
	 */
	private String parseImageName( String name, String image_url ) {
		String ext = parseExt( image_url );
		String str = "image_" + name + "." + ext ;
		return str;
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