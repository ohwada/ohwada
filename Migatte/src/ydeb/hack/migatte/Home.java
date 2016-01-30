package ydeb.hack.migatte;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

/**
 * SelectHome と SimpleHome の基底クラス
 */
public abstract class Home {
	
	/** レイアウトID */
	int layout;
	
	/** キーワードのリスト */
	List<String> keywords;

	/** 画面の幅 */
	int width;

	/** 画面の高さ */
	int height;

	/** リソ－ス・オブジェクト */
	Resources res;

	/** 背景ID */
	int background = 0;

	/**
 	 * コンストラクタにて、
 	 * 画面の大きさを設定する
	 * @param w 画面の幅
	 * @param y 画面の高さ
	 * @param r リソ－ス・オブジェクト
 	 */
	public Home(int w, int y, Resources r) {
		this.width = w;
		this.height = y;
		this.res = r;
	}

	/**
 	 * コンストラクタにて、
 	 * 画面の大きさを設定する
	 * @param w 画面の幅
	 * @param y 画面の高さ
 	 */
	public Home(int w, int y) {
		this.width = w;
		this.height = y;
	}

	/**
 	 * レイアウトIDを取得する
	 * @return レイアウトID
 	 */
	public int getLayout() {
		return this.layout;
	}

	/**
 	 * レイアウトIDを設定する
	 * @param layout レイアウトID
 	 */
	public void setLayout(int layout) {
		this.layout = layout;
	}

	/**
 	 * キーワードを取得する
	 * @return キーワードのリスト
 	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
 	 * キーワードを追加する
	 * @param keywords カンマ区切りのキーワードト
 	 */
	public void addKeywords(String keywords) {
		if(this.keywords == null){
			this.keywords = new ArrayList<String>();
		}

		this.keywords.add(keywords);
	}

	/**
 	 * 画面の座標より、キーワードを取得する
	 * @param x 画面の横座標
	 * @param y 画面の縦座標
	 * @return キーワードのリスト
 	 */
	public abstract List<String> getKeyword(int x, int y);

	/**
 	 * リソースIDから文字列を取得する
	 * @param id リソースID
	 * @return 文字列
 	 */
	public String getString( int id ) {
		return this.res.getString( id );
	}

	/**
 	 * 背景IDを設定する
	 * @param id 背景ID
 	 */
	public void setBackgroundId(int id) {
		this.background = id;
	}

	/**
 	 * 背景IDを取得する
	 * @return 背景ID
 	 */
	public int getBackgroundId() {
		return this.background;
	}
}
