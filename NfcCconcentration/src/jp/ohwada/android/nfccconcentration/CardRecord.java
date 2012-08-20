package jp.ohwada.android.nfccconcentration;

/**
 * record 
 */
public class CardRecord {
	
	// column
	public int id;
	public String tag;
	public int num;
	public int set;

    /**
     * === constractor ===
     */	
	public CardRecord() {
		setRecord( 0, "", 0, 0 );
	}

	/**
     * === constractor ===
     * @param String _tag 
	 * @param iint _num
	 * @param int _set 
	 */	
	public CardRecord( String _tag, int _num, int _set ) {
		setRecord( 0, _tag, _num, _set  );
	}
	
    /**
     * === constractor ===
     * @param int _id 
     * @param String _tag 
	 * @param iint _num
	 * @param int _set 
	 */	
	public CardRecord( int _id, String _tag, int _num, int _set ) {
		setRecord( _id, _tag, _num, _set );
	}

    /**
     * === constractor ===
     * @param int _id 
     * @param String _tag 
	 * @param iint _num
	 * @param int _set 
	 */	
	public void setRecord( int _id, String _tag, int _num, int _set ) {
		id = _id;
		tag = _tag;
		num = _num;
		set = _set;
	}
	
    /**
     * get id for TextView
	 * @return String : id 
     */		
	public String getIdString() {
		return Integer.toString( id );
	}

    /**
     * get uum for TextView
	 * @return String : num
     */	
	public String getNumString() {
		return Integer.toString( num );
	}

    /**
     * get set for TextView
	 * @return String : set
     */	
	public String getSetString() {
		return Integer.toString( set );
	}
}
