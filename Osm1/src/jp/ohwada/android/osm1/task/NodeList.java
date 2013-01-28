package jp.ohwada.android.osm1.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.osm1.Constant;

/**
 * NodeList
 */
public class NodeList {  
	
	// constant
	private final static String LF = Constant.LF;
	private final static String COLOR_DEFAULT = "white";
	
	// variable
	private List<NodeRecord> mList = null;
	private Map<String, String> mHashMarkerColor = null;
	
	/**
	 * === constarctor ===
	 * @param List<NodeRecord> list
	 */ 
    public NodeList( List<NodeRecord> list ) {
		setNodeList( list );
		initHashMarkerColor();
    }

	/**
	 * === constarctor ===
	 */ 
    public NodeList() {
		mList = new ArrayList<NodeRecord>();
		initHashMarkerColor();
    }

	/**
	 * setNodeList
	 * @param List<NodeRecord> list
	 */ 
    private void setNodeList( List<NodeRecord> list ) {
		mList = list;
    }
  	    
	/**
	 * getList
	 * @return List<NodeRecord>
	 */ 
	public List<NodeRecord> getList() {	
		return mList;
	}

	/**
	 * add
	 * @param NodeRecord record
	 */	
    public void add( NodeRecord record ) {
		mList.add( record );
    }

	/**
	 * add
	 * @param NodeRecord record
	 */	
    public void addWithMapColor( NodeRecord record ) {
    	record.map_color = getMarkerColor( record.direct_label );
		mList.add( record );
    }
    
	/**
	 * size
	 * @param int
	 */
	public int size() {
		return mList.size();
	}
	
	/**
	 * build WriteData
	 * @return String
	 */ 
	public String buildWriteData() {	
		String data = "";		
		for ( int i=0; i<mList.size(); i++ ) {
			NodeRecord record = mList.get( i );
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