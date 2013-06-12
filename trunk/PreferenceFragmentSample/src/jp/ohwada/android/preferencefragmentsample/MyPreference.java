package jp.ohwada.android.preferencefragmentsample;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceManager;

/**
 * MyPreference
 */
public class MyPreference {
	
	private final static String LF = "\n";
	private final static String COMMA = ", ";

	private Context mContext;
	private SharedPreferences mPreferences; 
	private Ringtone mRingtone = null;

    /**
	 * constractor
	 */	
    public MyPreference( Context context ) {
    	mContext = context;
    	mPreferences = PreferenceManager.getDefaultSharedPreferences( context );
	}

    /**
	 * getListEntry
	 * @param ListPreference list
	 * @param Object object
	 * @return String
	 */	                
	public String getListEntry( ListPreference list, Object object ) {
		return getListEntry( list, (String) object );
    }

    /**
	 * getListEntry
	 * @param ListPreference list
	 * @param String value
	 * @return String
	 */	     
	public String getListEntry( ListPreference list, String value ) {
        int index =  list.findIndexOfValue( value );
        String str = "undefined";
        if ( index >= 0 ) {
        	CharSequence[] cs = list.getEntries();
        	str = (String) cs[ index ];
        }
        return str;
    }

    /**
	 * getListEntry
	 * @param Object object
	 * @return String
	 */	
	@SuppressWarnings("unchecked")
	public String getListEntry( MultiSelectListPreference list, Object object ) {
		return getListEntry( list, (HashSet<String>) object );
	}

    /**
	 * getListEntry
	 * @param HashSet<String> set
	 * @return String
	 */	
	public String getListEntry( MultiSelectListPreference list, HashSet<String> set ) {
        String ret = "";
        String str = "";
        int index = 0;	    
		CharSequence[] cs = list.getEntries();
		for ( Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
			index =  list.findIndexOfValue( iterator.next() );
			str = "undefined";
        	if ( index >= 0 ) {	
        		str = (String) cs[ index ];
        	}
			ret += str + LF;			
		}
		return ret;
	}

    /**
	 * getMultiSelectListValue
	 * @param String key
	 * @return String
	 */	
	public String getMultiSelectListValue( String key ) {
		Set<String> set = getStringSet( key, getEmptySet() );
		String str = toString( set,  COMMA ) ;
		return str;
	}
	
    /**
	 * getEmptySet
	 * @return Set<String>
	 */	
	public Set<String> getEmptySet() {
		Set<String> set = new HashSet<String>();  
		return set;
	}
	
    /**
	 * getStringSet
	 * @param String key
	 * @param Set<String> set_default
	 * @return Set<String>
	 */	
	public Set<String> getStringSet( String key, Set<String> set_default ) {
        Set<String> set = mPreferences.getStringSet( key, set_default );
		return set;
	}

    /**
	 * getStringSet
	 * @param String key
	 * @return Set<String>
	 */	
	public  Set<String> getStringSet( String key ) {
		return getStringSet( key, getEmptySet() );
	}

    /**
	 * toString
	 * @param Set<String> set
	 * @param String glue
	 * @return String
	 */	
	public String toString( Set<String> set, String glue ) {
        String str = "";
		for ( Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
			str += iterator.next() + glue;			
		}
		return str;
	}
				
    /**
	 * getRingtoneTitle
	 * @param Object object
	 * @return String
	 */	        
	public String getRingtoneTitle( Object object ) {
		return getRingtoneTitle( (String) object );
	}

    /**
	 * getRingtoneTitle
	 * @param String value
	 * @return String
	 */	  	
	public String getRingtoneTitle( String value ) {
		Ringtone ringtone = getRingtone( value );
		 if ( ringtone == null ) {
     		return "Silent";    		
 		}
        return ringtone.getTitle( mContext );
	}

    /**
	 * getRingtone
	 * @param String value
	 * @return String
	 */	
	public Ringtone getRingtone( String value ) {
		 if ( "".equals(value) ) return null;    		
        Uri uri = Uri.parse( value );  
        return RingtoneManager.getRingtone( mContext, uri );   
	}

    /**
	 * playRingtone
	 * @param Object object
	 * @return boolean
	 */	
	public boolean playRingtone( Object object ) {
		return playRingtone( (String) object );
	}

    /**
	 * playRingtone
	 * @param String value
	 * @return boolean
	 */		
	public boolean playRingtone( String value ) {
		mRingtone = getRingtone( value );
		 if ( mRingtone == null ) return false;    		
        mRingtone.play();
        return true;
	}

    /**
	 * stopRingtone
	 */		
	public void stopRingtone() {
		if ( mRingtone == null ) return;
		if ( mRingtone.isPlaying() ) {
			mRingtone.stop();
		}
	}
 
}