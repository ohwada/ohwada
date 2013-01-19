package jp.ohwada.android.yag1.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.yag1.Constant;

/**
 * PlaceList
 */
public class PlaceList {  

	// constant
	private final static String LF = Constant.LF;
	private final static int MAX_DEPTH = 4;
	
	// variable
	private List<PlaceRecord> mList = null;
    private Map<String, PlaceRecord> mMapPlace = null;

	/**
	 * === constarctor ===
	 * @param List<PlaceRecord> list
	 */ 
    public PlaceList( List<PlaceRecord> list ) {
		setPlaceList( list );
    }

	/**
	 * === constarctor ===
	 */ 
    public PlaceList() {
		mList = new ArrayList<PlaceRecord>();
    }

	/**
	 * setPlaceList
	 * @param List<PlaceRecord> list
	 */ 
    private void setPlaceList( List<PlaceRecord> list ) {
		mList = list;
		initHash();
    }
  
	/**
	 * init Hash
	 * key: url, value: PlaceRecord
	 */ 
    public void initHash() {
    	Map<String, PlaceRecord> hash = new HashMap<String, PlaceRecord>(); 
		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			hash.put( r.url, r );	
		}
		mMapPlace = hash;
	}
	    
	/**
	 * getList
	 * @return List<PlaceRecord>
	 */ 
	public List<PlaceRecord> getList() {	
		return mList;
	}

	/**
	 * add
	 * @param PlaceRecord record
	 */	
    public void add( PlaceRecord record ) {
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
			PlaceRecord record = mList.get( i );
			data += record.getFileData() + LF;	
		}
		return data;		
	}

	/**
	 * getPlace
	 * @param String url
	 * @return PlaceRecord
	 */	
    public PlaceRecord getPlace( String url ) {
    	return mMapPlace.get( url );
	}

	/**
	 * addExtension
	 */ 
	public void addExtension() {
	    List<PlaceRecord> list1 = getListWithHasPart();
	    setPlaceList( list1 );
	    List<PlaceRecord> list2 = getListWithParentChild();
	    setPlaceList( list2 );
	}
	        
	/**
	 * getListWithHasPart
	 * @return List<PlaceRecord>
	 */ 
	private List<PlaceRecord> getListWithHasPart() {
	    List<PlaceRecord> list_new = new ArrayList<PlaceRecord>();	
		Map<String, String> hash = getHashWithHasPart();	
		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			String urls = hash.get( r.url );
			// set value if exist
			if ( urls != null ) {
				r.has_part = urls;
			}
			list_new.add( r );
		}
		return list_new;		
	}

	/**
	 * getHashWithHasPart
	 * @return Map<String, String>
	 */ 
	private Map<String, String> getHashWithHasPart() {
		Map<String, String> hash = new HashMap<String, String>(); 
		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord record = mList.get( i );
			String url = record.url; 
			String is_part_of = record.is_part_of;
			if ( "".equals( is_part_of ) ) continue;
			// get previous value
			String urls = hash.get( is_part_of );
			if ( urls == null ) {
				// at first
				urls = url ; 
			} else {
				// at second
				urls += PlaceRecord.BAR + url ; 
			}
			// set new value
			hash.put( is_part_of, urls );
		}
		return hash;
	}
    
	/**
	 * getListWithParentChild
	 * @return List<PlaceRecord>
	 */ 
    private List<PlaceRecord> getListWithParentChild() {
    	List<PlaceRecord> list_new = new ArrayList<PlaceRecord>();
		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			String url = r.url;
			// parent
			PlaceRecord r_parent = getParentToRoot( url );
			if( r_parent != null ) {
				r.parent_url = r_parent.url;
			} else {
				r.parent_url = url;
			}
			// child
			List<String> list_url = getListChildUrls( r );
			r.setChildUrls( list_url );
			list_new.add( r );
		}
		return list_new;
	}
				
	/**
	 * getParentToRoot
	 * @param String url
	 * @return PlaceRecord
	 */ 
    public PlaceRecord getParentToRoot( String url ) {
    	return getParentRecursive( url, 0 );
	}

	/**
	 * getParentToRoot Recursive
	 * @param String url
	 * @param int depth
	 * @return PlaceRecord
	 */
    private PlaceRecord getParentRecursive( String url, int depth ) {
    	depth ++;
    	PlaceRecord record = getPlace( url );
    	if( record == null ) return null;
    	if ( "".equals( record.is_part_of ) ) return record;
    	if ( depth >= MAX_DEPTH ) return record;
    	return getParentRecursive( record.is_part_of, depth );
    }	

	/**
	 * getChildUrls
	 * @param PlaceRecord reocrd 
	 * @return List<String>
	 */	
    public List<String> getListChildUrls( PlaceRecord reocrd ) {
    	List<String> list = new ArrayList<String>();
    	return getChildRecursive( list, reocrd, 0 );
	}

	/**
	 * getChildUrls Recursive
	 * @param PlaceRecord reocrd 
	 * @param List<String> list
	 * @param int depth
	 * @return List<String>
	 */	
    private List<String> getChildRecursive( List<String> list, PlaceRecord record, int depth ) {
		if ( record == null ) return list;
		depth ++;
		String url = record.url;
		list.add( url );
		List<String> list_part = record.getListHasPart();
		if (( list_part == null )||( list_part.size() == 0 )) return list;
		if ( depth >= MAX_DEPTH ) return list;
		for ( int i=0; i<list_part.size(); i++ ) {
			String url_child = list_part.get( i );
			list.add( url_child );
			PlaceRecord record_child = getPlace( url_child );
			if ( record_child != null ) {
				list = getChildRecursive( list, record_child, depth );
			}
		}
		return list; 
	}

	/**
	 * getListForPlace
	 * @param Map<String, Boolean> hash
	 * @return List<PlaceRecord>
	 */	
	public List<PlaceRecord> getListForPlace( Map<String, Boolean> hash ) {  
		List<PlaceRecord> list_new = new ArrayList<PlaceRecord>();
		// in the first half of a list, places have events
		list_new = getListWithEvent( list_new, hash );
		// in the second half of a list, places have no event	
		list_new = getListWithoutEvent( list_new, hash );	
		return list_new;
	}

	/**
	 * getListForMap
	 * @param Map<String, Boolean> hash
	 * @return List<PlaceRecord>
	 */		
	public List<PlaceRecord> getListForMap( Map<String, Boolean> hash ) {  
		// remove children and makes it only parents. 
		// since NOT show the marker on the same place
		List<PlaceRecord> list1 = getListWithoutChild();
		setPlaceList( list1 );
		List<PlaceRecord> list_new = new ArrayList<PlaceRecord>();
		// in the first half of a list, places have no event	
		list_new = getListWithoutEvent( list_new, hash );
		// in the second half of a list, places have events
		// since markers are overwritten	
		list_new = getListWithEvent( list_new, hash );	
		return list_new;
	}
	
	/**
	 * getListWithoutChild
	 * remove children and makes it only parents. 
	 * @return List<PlaceRecord> 
	 */	
	private List<PlaceRecord> getListWithoutChild() {
	    List<PlaceRecord> list_new = new ArrayList<PlaceRecord>();
		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			// if belongs to no other place 
			if ( "".equals( r.is_part_of ) ) {
				list_new.add( r );
			} 
		}
		return list_new;	
	}
				
	/**
	 * get list included in event list
	 * @param List<PlaceRecord> list
	 * @param Map<String, Boolean> hash
	 * @return List<PlaceRecord> 
	 */	
	private List<PlaceRecord> getListWithEvent( 
		List<PlaceRecord> list, 
		Map<String, Boolean> hash ) {

		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			// if include
			if ( hash.containsKey( r.url ) || 
			     hash.containsKey( r.parent_url ) ) {
				if ( !r.event_flag ) {
					r.event_flag = true;
					list.add( r );
				}
			}
		}
		return list;
	}
	
	/**
	 * get list NOT included in event list
	 * @param List<PlaceRecord> list
	 * @param Map<String, Boolean> hash
	 * @return List<PlaceRecord> 
	 */	
	private List<PlaceRecord> getListWithoutEvent( 
		List<PlaceRecord> list, 
		Map<String, Boolean> hash ) {

		for ( int i=0; i<mList.size(); i++ ) {
			PlaceRecord r = mList.get( i );
			// if NOT include
			if ( !hash.containsKey( r.url ) &&
			    !hash.containsKey( r.parent_url ) ) {
				list.add( r );
			}
		}
		return list;
	}
	
}