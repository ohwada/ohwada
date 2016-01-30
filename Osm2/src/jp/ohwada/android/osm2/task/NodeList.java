package jp.ohwada.android.osm2.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.osm2.Constant;

/**
 * NodeList
 */
public class NodeList {  
	
	// constant
	private final static String LF = Constant.LF;
	private final static String COLOR_DEFAULT = "white";
	
	// public variable
	public int status = 0;
	
	// variable
	private List<NodeRecord> mListRecord = null;
	private List<NodeHash> mListHash = null;
	private Map<String, String> mHashMarkerColor = null;

	/**
	 * === constarctor ===
	 */ 
    public NodeList() {
    	mListHash = new ArrayList<NodeHash>();
		mListRecord = new ArrayList<NodeRecord>();
		initHashMarkerColor();
    }
  	    
	/**
	 * getList
	 * @return List<NodeRecord>
	 */ 
	public List<NodeRecord> getListRecord() {	
		return mListRecord;
	}

	/**
	 * getListHash
	 * @return List<NodeHash>
	 */ 
	public List<NodeHash> getListHash() {	
		return mListHash;
	}

	/**
	 * add
	 * @param NodeHash hash
	 */	
    public void addHash( NodeHash hash ) {
		mListHash.add( hash );
		mListRecord.add( hash.record );
    }
    
	/**
	 * add
	 * @param NodeRecord record
	 */	
    public void addWithMapColor( NodeRecord record ) {
    	record.map_color = getMarkerColor( record.direct_label );
		mListRecord.add( record );
    }

	/**
	 * size
	 * @param int
	 */
	public int sizeHash() {
		return mListHash.size();
	}

	/**
	 * hasDetail
	 * @return boolean
	 */
	public boolean hasDetail() {
		if ( status == NodeHash.STATUS_DETAIL ) return true;
		return false;
	}
			
	/**
	 * build WriteData
	 * @return String
	 */ 
	public String buildWriteData() {	
		String data = "";		
		for ( int i=0; i<mListRecord.size(); i++ ) {
			NodeRecord record = mListRecord.get( i );
			data += record.getFileData() + LF;	
		}
		return data;		
	}
	
	/**
	 * getMarkerColor
	 * @param String name
	 * @return String
	 */  
	private String getMarkerColor( String name ) {
        String color = COLOR_DEFAULT;
        if ( mHashMarkerColor.containsKey( name ) ) {
        	color = mHashMarkerColor.get( name );
        }
        return color;
    }
    
	/**
	 * initHashMarkerColor
	 */          
	private void initHashMarkerColor() {
		Map<String, String> hash = new HashMap<String, String>();
		hash.put( "RailwayStation", "red" );
		hash.put( "University", "blue" );
		hash.put( "Kindergarten", "blue"  );
		hash.put( "School", "blue"  );
		hash.put( "Restaurant", "green" );
		hash.put( "Cafe", "green" );
		hash.put( "PostOffice", "yellow" );
		mHashMarkerColor = hash;
	}

}