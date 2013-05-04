package jp.ohwada.android.nfccconcentration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * Preference Utility
 */
public class PreferenceUtility {
	private final static String PREF_KEY_DIR = Constant.PREF_KEY_DIR;    
	private final static String PREF_KEY_NUM = Constant.PREF_KEY_NUM;
	private final static String PREF_KEY_TIME = Constant.PREF_KEY_TIME;
	private final static String DIR_SUB_DEFAULT = Constant.DIR_SUB_DEFAULT;
	private final static int CARD_NUM_DEFAULT = Constant.CARD_NUM_DEFAULT;
	private final static String STR_EMPTY = "";
	
	private SharedPreferences mPreferences;

    /**
     * === constractor ===
     * @param Context c
     */	
    public PreferenceUtility( Context context ) {		
		mPreferences = PreferenceManager.getDefaultSharedPreferences( context );
	}

    /**
     * setDir
     * @param String value
     */	
   public void setDir( String value ) {
		mPreferences.edit().putString( PREF_KEY_DIR, value ).commit(); 
	}
	
	/**
	 * getDir
	 * @return String 
	 */	
    public String getDir() {
		return mPreferences.getString( PREF_KEY_DIR, DIR_SUB_DEFAULT );
	}

	/**
	 * isDefualtDir
	 * @return boolean
	 */	
	public boolean isDefualtDir() {
		return DIR_SUB_DEFAULT.equals( getDir() );
	}

    /**
     * setNum
     * @param int value
     */	
    public void setNum( int value ) {
		mPreferences.edit().putInt( PREF_KEY_NUM, value ).commit(); 	
	}
				
	/**
	 * getNum
	 * @return int
	 */											             
    public int getNum() {		
		return mPreferences.getInt( PREF_KEY_NUM,  CARD_NUM_DEFAULT );
	}

    /**
     * setTime
     * @param String value
     */	
   public void setTime( String value ) {
		mPreferences.edit().putString( PREF_KEY_TIME, value ).commit(); 
	}

	/**
	 * getTime
	 * @return String 
	 */	
   public String getTime() {
   		return mPreferences.getString( PREF_KEY_TIME,  STR_EMPTY );
	}

}
