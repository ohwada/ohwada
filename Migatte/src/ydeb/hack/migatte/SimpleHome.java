package ydeb.hack.migatte;

import java.util.List;

/**
 * 特別な日におけるキーワードを選択する
 */
public class SimpleHome extends Home {

	/**
 	 * コンストラクタにて、
 	 * 画面の大きさを設定する
	 * 引数なしのときは 0,0 を設定する
 	 */
	public SimpleHome() {
		super(0, 0);
	}
	
	/**
 	 * 画面の座標に関係なく、キーワードを取得する。
	 * @param x 画面の横座標 (使用しない)
	 * @param y 画面の縦座標 (使用しない)
	 * @return キーワードのリスト (呼出し側から与えられたもの)
 	 */
	@Override
	public List<String> getKeyword(int x, int y) {
		// TODO Auto-generated method stub
		return keywords;
	}

}
