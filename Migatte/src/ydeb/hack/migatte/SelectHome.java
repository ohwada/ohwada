package ydeb.hack.migatte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.res.Resources;
import android.util.Log;

/**
 * 普通の日におけるキーワードを選択する
 */
public class SelectHome extends Home {

	/**
 	 * コンストラクタにて、
 	 * 画面の大きさを設定する
	 * @param w 画面の幅
	 * @param y 画面の高さ
	 * @param r リソ－ス・オブジェクト
 	 */
	public SelectHome(int w, int y, Resources r) {
		super(w, y, r);
	}
	
	/**
 	 * selectKeyword() により画面の座標より、キーワードを取得する。
	 * @param x 画面の横座標
	 * @param y 画面の縦座標
	 * @return キーワードのリスト
 	 */
	@Override
	public List<String> getKeyword(int x, int y) {
		return selectKeyword(x,y);
	}

	/**
 	 * 画面の座標より、キーワードを取得する
	 * @param x 画面の横座標
	 * @param y 画面の縦座標
	 * @return キーワードのリスト
 	 */
	private List<String> selectKeyword(int x, int y) {
		int area = getArea(x, y);
		int id  = 0;

		Log.v("selectKeyword", "Area="+area);

		// TODO 
		List<String> words = new ArrayList<String>();

		switch(area){
		case 0: /* 左上 */
			id = R.string.keywords_left_up ;
			break;

		case 1: /* 左下 */
			id = R.string.keywords_left_down ;
			break;

		case 2: /* 右上 */
			id = R.string.keywords_right_up ;
			break;

		case 3: /* 右下 */
		default:
			id = R.string.keywords_right_down ;
			break;
		}

		String[] keywords = getString( id ).split(",");
		boolean a = words.addAll( Arrays.asList( keywords ) );
		return words;
	}

	/**
 	 * 画面の座標より、４分割したエリアを取得する
	 * @param x 画面の横座標
	 * @param y 画面の縦座標
	 * @return エリアのコード(0..3)
 	 */
	private int getArea(int x, int y) {
		Log.v("getArea", "width,height="+width+","+height);
		Log.v("getArea", "x,y="+x+","+y);
		if (x <= width / 2) {
			if (y <= height / 2) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (y <= height / 2) {
				return 2;
			} else {
				return 3;
			}
		}
	}

}
