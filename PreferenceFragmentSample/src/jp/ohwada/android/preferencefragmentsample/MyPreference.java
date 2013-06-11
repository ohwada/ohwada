package jp.ohwada.android.preferencefragmentsample;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;

/**
 * MyPreference
 */
public class MyPreference {
	private Context mContext = null;
	private Ringtone mRingtone = null;

    /**
	 * constractor
	 */	
    public MyPreference( Context context ) {
    	mContext = context;
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