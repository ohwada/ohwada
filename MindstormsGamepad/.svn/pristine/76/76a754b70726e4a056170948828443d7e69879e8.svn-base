package jp.ohwada.android.mindstormsgamepad.util;

import java.util.ArrayList;
import java.util.List;

/*
 * Debug Msg
 */
public class DebugMsg {
    
	/* List for message */
	private List<String> mList  = new ArrayList<String>();
	
	/*
	 * Constractor
	 * @param Context context
	 */			
	public DebugMsg() {
		// dummy
	}

	/**
	 * add
	* @param String msg
	 */				
	public void add( String msg ) {
		mList.add( msg );
	}

	/**
	 * build the messages
	 * @param max  : max of showing messages
	 * @param glue : glue of messages
	 * @return String
	 */
    public String build( int max, String glue ) {
    	return build( mList, max, glue );
	}
	
	/**
	 * build the messages
	 * @param list : message list
	 * @param max  : max of showing messages
	 * @param glue : glue of messages
	 * @return String
	 */
    public String build( List<String> list, int max, String glue ) {
		// set 'start' and 'end' , showing only 'max' messages
		int start = 0;
		int end = list.size();
		if ( end > max ) {
			start = end - max;
		}
		/* combine the messages */
		String msg = "";
		for ( int i = start; i < end; i++ ) {
			msg += list.get( i );
			msg += glue;
		}
		return msg;
	}	
} 