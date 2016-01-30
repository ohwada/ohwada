package ydeb.hack.migatte.http.dto;

import java.io.Serializable;

/**
 * <pre>
 * ぐるなび情報の DTO（Data Transfer Object）
 * 店舗ID、店舗名称、店舗画像 など
 * </pre>
 */
// public class GnaviDto {
public class GnaviDto implements Serializable {

	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;

	/** 検索 キーワード */
	public StringBuilder search_keyword = new StringBuilder();
	/** 検索 緯度 */
	public StringBuilder search_latitude = new StringBuilder();
	/** 検索 経度 */
	public StringBuilder search_longitude = new StringBuilder();

	/** 応答： 店舗ID */
	public StringBuilder id = new StringBuilder();
	/** 情報更新日時 */
	public StringBuilder update_date = new StringBuilder();
	/** 店舗名称 */
	public StringBuilder name = new StringBuilder();
	/** 店舗名称カナ */
	public StringBuilder name_kana = new StringBuilder();
	/** 緯度 */
	public double latitude;
	/** 経度 */
	public double longitude;
	/** フリーワードカテゴリ */
	public StringBuilder category = new StringBuilder();
	/** PCサイトのURL */
	public StringBuilder url = new StringBuilder();
	/** 携帯サイトのURL */
	public StringBuilder url_mobile= new StringBuilder();
	/** 店舗画像 */
	public ImageUrl imageUrl = new ImageUrl();
	/** 住所 */
	public StringBuilder address = new StringBuilder();
	/** 電話番号 */
	public StringBuilder tel = new StringBuilder();
	/** FAX */
	public StringBuilder fax = new StringBuilder();
	/** 営業時間 */
	public StringBuilder opentime = new StringBuilder();
	/** 休業日 */
	public StringBuilder holiday = new StringBuilder();
	/** アクセス */
	public Access access = new Access();
	/** PR */
	public Pr pr = new Pr();
	/** 平均予算 */
	public StringBuilder budget = new StringBuilder();
	/** 設備 */
	public StringBuilder equipment = new StringBuilder();
}