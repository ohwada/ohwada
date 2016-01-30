package jp.ohwada.android.yag1.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.yag1.Constant;

/**
 * EventList 
 */
public class EventList { 

	// constant
	private final static String LF = Constant.LF;
	
	// variable
	private List<EventRecord> mList = null;
		
	/**
	 * === constractor ===
	 * @param List<EventRecord> list
	 */
    public EventList( List<EventRecord> list ) {
		mList = list;
    }

	/**
	 * === constractor ===
	 */
    public EventList() {
    	mList = new ArrayList<EventRecord>();
    }

	/**
	 * getList
	 * @return List<EventRecord>
	 */
    public List<EventRecord> getList() {
		return mList;
    }

	/**
	 * add
	 * @param EventRecord record
	 */
    public void add( EventRecord record ) {
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
	 * buildWriteData
	 * @return String
	 */ 
	public String buildWriteData() {	
		String data = "";		
		for ( int i=0; i<mList.size(); i++ ) {
			EventRecord record = mList.get( i );
			data += record.getFileData() + LF;	
		}
		return data;		
	}

	/**
	 * getListPlaceUrlList
	 * @param List<String> list_url
	 * @return List<EventRecord>
	 */         
    public List<EventRecord> getListPlaceUrlList( List<String> list_url ) { 
		List<EventRecord> list_new = new ArrayList<EventRecord>();
		for ( int i=0; i<list_url.size(); i++ ) {
			String url = list_url.get( i );
			List<EventRecord> list_add = getListPlaceUrl( url );
			list_new = merge( list_new, list_add );
		}
		return list_new;
	}
		            	
	/**
	 * getListPlaceUrl
	 * @param String url
	 * @return List<EventRecord>
	 */
	private List<EventRecord> getListPlaceUrl( String url ) {
		List<EventRecord> list_new = new ArrayList<EventRecord>();
		for ( int i=0; i<mList.size(); i++ ) {
			EventRecord r = mList.get( i );
			if ( url.equals( r.place_url ) ) {
				list_new.add( r );	
			}
		}
		return list_new;
	}
	
	/**
	 * getHashPlaceUr
	 * @return Map<String, Boolean>
	 */
    public Map<String, Boolean> getHashPlaceUrl() {	
		Map<String, Boolean> map = new HashMap<String, Boolean>(); 
		for ( int i=0; i<mList.size(); i++ ) {
			EventRecord r = mList.get( i );
			String key = r.place_url;	
			// check double
			if ( !map.containsKey( key ) ) {
				map.put( key, true );
			}	
		}
		return map;
	}
	
	/**
	 * merge EventList
	 * @param List<EventRecord> list1
	 * @param List<EventRecord> list2
	 * @return List<EventRecord>
	 */
    public List<EventRecord> merge( 
    	List<EventRecord> list1, 
    	List<EventRecord> list2 ) {

		Map<String, Boolean> map = new HashMap<String, Boolean>();     	
		List<EventRecord> list_new = new ArrayList<EventRecord>();
		list_new = mergeWithHash( list1, list_new, map );
		list_new = mergeWithHash( list2, list_new, map );
		return list_new;
	}

	/**
	 * merge EventList
	 * @param List<EventRecord> list1
	 * @param List<EventRecord> list2
	 * @param Map<String, Boolean> map
	 * @return List<EventRecord>
	 */
    private List<EventRecord> mergeWithHash( 
    	List<EventRecord> list_orig, 
    	List<EventRecord> list_new,
    	Map<String, Boolean> map ) {
    	
		for ( int i=0; i<list_orig.size(); i++ ) {
			EventRecord r = list_orig.get( i );
			String key = r.event_url;
			// check double	
			if ( !map.containsKey( key ) ) {
				map.put( key, true );
				list_new.add( r );
			}	
		}
		return list_new;
	}
					
}