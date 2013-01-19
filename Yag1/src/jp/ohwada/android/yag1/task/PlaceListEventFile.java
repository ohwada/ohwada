package jp.ohwada.android.yag1.task;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * PlaceListEventFile
 */
public class PlaceListEventFile {  
	   
	// object
   	private PlaceListFile mPlaceFile; 
   	private EventListFile mEventFile; 
	
	/**
	 * === Constructor ===
	 */ 
    public PlaceListEventFile() {
 		mPlaceFile = new PlaceListFile(); 
		mEventFile = new EventListFile(); 
	}

    /**
	 * getList
	 * the beginning of a list has an event info 
	 * @param Date date
	 * @return List<PlaceRecord>
	 */ 
	public List<PlaceRecord> getListForPlace( Date date ) {  
		Map<String, Boolean> hash = getEventHash( date );
		PlaceList list = mPlaceFile.read();
		return list.getListForPlace( hash );
	}

    /**
	 * getList
	 * the end of a list has an event info, since a marker is overwritten 
	 * @param Date date
	 * @return List<PlaceRecord>
	 */ 
	public List<PlaceRecord> getListForMap( Date date ) {  
		Map<String, Boolean> hash = getEventHash( date );
		PlaceList list = mPlaceFile.read();
		return list.getListForMap( hash );
	}

    /**
	 * getEventHash
	 * @param Date date
	 * @return Map<String, Boolean>
	 */ 
	private Map<String, Boolean> getEventHash( Date date ) {  
		File file = mEventFile.getFile( date );
		EventList list = mEventFile.read( file );
		return list.getHashPlaceUrl();
	}
			
}