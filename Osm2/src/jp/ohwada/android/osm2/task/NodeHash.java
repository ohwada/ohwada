package jp.ohwada.android.osm2.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.ohwada.android.osm2.Constant;

/**
 * Node Hash
 */
public class NodeHash {

	// key
	private static final String KEY_STATUS = "status";
	protected static final int STATUS_DETAIL = 1;

	// code	
	private final static String TAB = Constant.TAB;
	private final static String LF = Constant.LF;
	
	// varibale
	public int status = 0;
	public int node_id = 0;
	public Map<String, String> hash = null; 	
	public NodeRecord record = null;
        
	/**
	 * === constractor ===
	 */
    public NodeHash() {
    	hash = new HashMap<String, String>(); 
    }

	/**
	 * === constractor ===
	 * @param Map<String, String> hash_in
	 */
    public NodeHash( Map<String, String> hash_in ) {
    	if ( hash_in == null ) return; 
    	hash = hash_in;
    	setAddtional();
    }

	/**
	 * getFileData
	 * @return String
	 */
    public String getFileData() {	
        String data = "";
		Set<Map.Entry<String, String>> set = hash.entrySet();	
		Iterator<Map.Entry<String, String>> it = set.iterator();
		while( it.hasNext() ) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			data += key + TAB + value + LF;
		} 
		return data;	
	}

	/**
	 * setFileData
	 * @param String data
	 */
    public void setFileData( String data ) {
    	String[] lines = data.split( LF );
		for ( int i=0; i<lines.length; i++ ) {
			String line = lines[ i ];
			if ( "".equals( line ) ) continue;
			String[] cols = line.split( TAB );
			String key = cols[0];
			String value = cols[1];
			hash.put( key, value );
		}
		setAddtional();
	}

	/**
	 * put
	 * @param String key
	 * @param String value
	 */
    public void put( String key, String value ) {
       	hash.put( key, value );
    }

	/**
	 * setAddtional
	 */
    public void setAddtional() {
    	setAddtionalStatus();
    	setAddtionalRecord();
    }
    
	/**
	 * setAddtionalStatus
	 */
    private void setAddtionalStatus( ) {
		if ( hash.containsKey( KEY_STATUS ) ) {
			String str = hash.get( KEY_STATUS );
			status = Integer.parseInt( str );
		}
	}

	/**
	 * setAddtionalRecord
	 */
    private void setAddtionalRecord() {
        record = new NodeRecord( hash );
        node_id = record.node_id;
	}

	/**
	 * isValidNode
	 * @return boolean
	 */
	public boolean isValidNode() {
		if ( record == null ) return false;
		return record.isValidNode();
	}
			
	/**
	 * size
	 * @return int
	 */
	public int size() {
		return hash.size();
	}

	/**
	 * hasDetail
	 * @return boolean
	 */
	public boolean hasDetail() {
		if ( status == STATUS_DETAIL ) return true;
		return false;
	}

	/**
	 * getContent
	 * @return String
	 */
	public String getContent() {
		List<Map.Entry<String, String>> entries = getSortedEntries();

		String text = "";
		for ( Map.Entry<String, String> entry : entries ) {
		    String key = (String) entry.getKey();
            String value = (String) entry.getValue() ;
            text += key + " " + value + Constant.LF ;
		}
		return text;
	}

	/**
	 * getSortedEntries
	 * @return SList<Map.Entry<String, String>>
	 */
	private List<Map.Entry<String, String>> getSortedEntries() {
		List<Map.Entry<String, String>> entries = new ArrayList<Entry<String, String>>( hash.entrySet() );
		Collections.sort( entries, new NodeComparator() );
		return entries;
	}
	
	/**
	 * === class NodeComparator ===
	 */	
	private class NodeComparator implements Comparator<Map.Entry<String, String>> {
		public NodeComparator() {
			// dummy
        }
        /**
	 	 * compare: sort in alphabet
	 	 */
	 	@Override
		public int compare( Map.Entry<String, String> entry1, Map.Entry<String, String> entry2 ){
		    String str1 = (String) entry1.getKey();
		    String str2 = (String) entry2.getKey();
		    return str1.compareTo( str2 );
    	}
    }
	
}