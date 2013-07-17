package jp.ohwada.android.mindstormsgamepad.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * Kanji Yumi
 */	
public class KanjiYumi {

	// group number is unmatch. group number MUST begin from 0 
    public static final int GROUP_UNMTACH = -1;
    
	// words file
	private static final String FILE_NAME_WORDS = "words.txt";
    
	// file format
	private static final int MIN_COLUMN = 3;	
	private static final String CHAR_COMMA = ",";
    private static final String CHAR_LF = "\n";

	// view format
	private static final String GROUP_TITLE = "group: ";
	private static final String CHAR_SPACE = " ";
    private static final String CHAR_PARENTHESIS_LEFT = "(";
	private static final String CHAR_PARENTHESIS_RIGHT = ")";
				
	// FileUtility
	private FileUtility	 mFileUtility;

	// local variable : word list ( LIst in LIst )
	private List<PairWordArrayList> mWordList = new ArrayList<PairWordArrayList>();

	/**
	 * === Constructor ===
	 * @param Context context
	 */	
	public KanjiYumi( Context context ) {
		mFileUtility = new FileUtility( context );   		
	}

	/**
	 * load word list from Assets file
	 */		
	public void load() {
		// read file
 		String[] lines = mFileUtility.readAssetsFileLines( FILE_NAME_WORDS );
		// initial state MUST be null 
		PairWordArrayList list = null;
		// initial state MUST be unmatch
		int prev_group = GROUP_UNMTACH ;
		// work varibale
		PairWord pair = null;
		String[] columns = null;
		int group = 0;
		// process every line
 		for ( String line: lines ) {
 			columns = line.split( CHAR_COMMA );
 			// no action if NOT valid format
 			if ( columns.length < MIN_COLUMN ) continue;
 			// file format : group, kanji, yomi
 			group = Integer.parseInt( columns[0] );
			pair = new PairWord();
			pair.kanji = columns[1];
			pair.yomi = columns[2];
			// when the group number is changed
  			if ( prev_group != group ) {
  				prev_group = group;
  				// add in word list when NOT first time. initial status is null. 
				if ( list != null ) {
  					mWordList.add( list );
  				}
  				// set new object
				list = new PairWordArrayList();
  			}		
			list.add( pair );			 			 			 			
 		}
		// add in word list when last, because not process in a loop. 
  		mWordList.add( list );
	}

	/**
	 * get word list
	 * @return List<PairWordArrayList>
	 */		
	public List<PairWordArrayList> getWordList() {
		return mWordList;
	}
	
	/**
	 * get word list for view
	 * @return String
	 */		
	public String getWords() {
		// return variable
		String text = "";
		// work variable
		PairWordArrayList list = null;
		// process every group
		for ( int i=0; i < mWordList.size(); i++ ) {
			list = mWordList.get( i );
			text += GROUP_TITLE + i + CHAR_LF;	
			// process every word pair		
			for ( int j=0; j < list.size(); j++ ) {
				text += buildWord( 
					list.get( j ), 
					CHAR_PARENTHESIS_LEFT, 
					CHAR_PARENTHESIS_RIGHT + CHAR_SPACE );
			}
			text += CHAR_LF;
		}
		return text;
	}	

	/**
	 * get word list for view
	 * @return String[]
	 */		
	public String[] getWordArray() {
		int size = mWordList.size();
		// return variable
		String[] array = new String[ size ];
		// work variable
		PairWordArrayList list = null;
		String word = "";
		// process every group
		for ( int i=0; i < size; i++ ) {
			list = mWordList.get( i );
			word = ""; 
			// process every word pair		
			for ( int j=0; j < list.size(); j++ ) {
				word += buildWord( 
					list.get( j ), 
					CHAR_PARENTHESIS_LEFT, 
					CHAR_PARENTHESIS_RIGHT + CHAR_LF );
			}
			array[ i ] = word;
		}
		return array;
	}	

	/**
	 * get word list for view
	 * @param PairWord pair
	 * @param String left
	 * @param String right
	 * @return String
	 */		
	public String buildWord( PairWord pair, String left, String right ) {
		String str = pair.kanji + left + pair.yomi + right;
		return str;
	}
				
	/**
	 * match word list 
	 * @param String
	 * @return int group number
	 */	
	public int match( String word ) {
		// no action if NOT valid 
		if ( word == null ) return GROUP_UNMTACH;
		// work variable
		PairWordArrayList list = null;
		PairWord pair = null;
		// process every group
		for ( int i=0; i < mWordList.size(); i++ ) {
			list = mWordList.get( i );
			// process every word pair	
			for ( int j=0; j < list.size(); j++ ) {
				pair = list.get( j );
				// return group number in match
				if ( word.equals( pair.kanji ) ) return i;
			}
		}
		return GROUP_UNMTACH;
	}	

// --- inner class ---
	/**
	 * pair word list 
	 */		
	public class PairWordArrayList extends ArrayList<PairWord> {
		/** MUST in Serializable class */
		private static final long serialVersionUID = 1L;
	}	

	/**
	 * pair word 
	 */		
	public class PairWord {
		public String kanji;
		public String yomi;		
	}

}
