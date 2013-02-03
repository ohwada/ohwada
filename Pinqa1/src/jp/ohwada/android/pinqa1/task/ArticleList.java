package jp.ohwada.android.pinqa1.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ohwada.android.pinqa1.Constant;

/**
 * ArticleList
 */
public class ArticleList {  
	
	// constant
	private final static String LF = Constant.LF;
	private final static String COLOR_DEFAULT = "gray";
   		
	// variable
	private List<ArticleRecord> mList = null;
	private Map<Integer, String> mHashMarkerColor = null;
		
	/**
	 * === constarctor ===
	 * @param List<ArticleRecord> list
	 */ 
    public ArticleList( List<ArticleRecord> list ) {
		setArticleList( list );
		initHashMarkerColor();
    }

	/**
	 * === constarctor ===
	 */ 
    public ArticleList() {
		mList = new ArrayList<ArticleRecord>();
		initHashMarkerColor();
    }

	/**
	 * setArticleList
	 * @param List<ArticleRecord> list
	 */ 
    private void setArticleList( List<ArticleRecord> list ) {
		mList = list;
    }
  	    
	/**
	 * getList
	 * @return List<ArticleRecord>
	 */ 
	public List<ArticleRecord> getList() {	
		return mList;
	}

	/**
	 * add
	 * @param ArticleRecord record
	 */	
    public void add( ArticleRecord record ) {
		mList.add( record );
    }

	/**
	 * add
	 * @param NodeRecord record
	 */	
    public void addWithMapColor( ArticleRecord record ) {
    	record.map_color = getMarkerColor( record.category_id );
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
			ArticleRecord record = mList.get( i );
			data += record.getFileData() + LF;	
		}
		return data;		
	}

	/**
	 * getMarkerColor
	 * @param String name
	 * @return String
	 */  
	private String getMarkerColor( int id ) {
        String color = COLOR_DEFAULT;
        if ( mHashMarkerColor.containsKey( id ) ) {
        	color = mHashMarkerColor.get( id );
        }
        return color;
    }
    
	/**
	 * initHashMarkerColor
	 */          
	private void initHashMarkerColor() {
		Map<Integer, String> hash = new HashMap<Integer, String>();
		hash.put( 1, "bule" );	// 1 旅・観光
		hash.put( 2, "yellow" );	// 2 グルメ・ランチ
		hash.put( 3, "teal"  );	// 3 プチ旅行・散歩
		hash.put( 4, "red"  );	// 4 デート
		hash.put( 5, "aqua" );	// 5 イベント
		hash.put( 6, "maroon" );	// 6 スポーツ	
		hash.put( 7, "green" );	// 7ドライブ・ツーリング
		hash.put( 8, "lime" );	// 8 ショッピング
		hash.put( 9, "fuchsia" );	// 9 カルチャー
		hash.put( 10, "purple" );	// 10 アウトドア
		hash.put( 11, "navy" );	// 11 ストーリー
		hash.put( 12, "gray" );	// 12 その他
		mHashMarkerColor = hash;
	}

}