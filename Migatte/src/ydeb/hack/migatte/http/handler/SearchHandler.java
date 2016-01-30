package ydeb.hack.migatte.http.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ydeb.hack.migatte.http.dto.GnaviDto;
import ydeb.hack.migatte.http.response.Response;

/**
 * ぐるなび情報を解析するSAXイベントハンドラ
 */
public class SearchHandler extends DefaultHandler{

	/** 現在処理しているタグの名前 */
	private String nowTag;

	/** 解析結果 */
	private final Response response = new Response();

	/** ぐるなび情報 */
	private GnaviDto rest;

	/**
	 * 変換されたデータを取得する
	 * @return 変換されたデータ
	 */
	public Response getResponse() {
		return this.response;
	}

	/**
	 * 要素の開始通知の処理
	 * @param uri 名前空間 URI
	 * @param localName ローカル名
	 * @param qName 修飾名
	 * @param attributes 属性
	 */
	@Override
    public void startElement (String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
		nowTag = localName;
		if (rest == null) {
			rest = new GnaviDto();
		}
    }

	/**
	 * 要素の終了通知の処理
	 * @param uri 名前空間 URI
	 * @param localName ローカル名
	 * @param qName 修飾名
	 */
	@Override
    public void endElement (String uri, String localName, String qName) throws SAXException {
		if ("rest".equals(localName)) {
			response.resultRest.add(rest);
			rest = null;
		}
		nowTag = null;
    }

	/**
	 * 要素内の文字データの通知の処理
	 * @param ch 文字配列
	 * @param start 文字配列内の開始位置
	 * @param length 文字配列から使用される文字数
	 */
	@Override
    public void characters (char ch[], int start, int length) throws SAXException {
		if (nowTag == null) return;
		
		if ("total_hit_count".equals(nowTag)) {
			this.response.total_hit_count = Integer.parseInt(this.createCharacters(ch, start, length));
		} else if ("hit_per_page".equals(nowTag)) {
			this.response.hit_per_page = Integer.parseInt(this.createCharacters(ch, start, length));
		} else if ("page_offset".equals(nowTag)) {
			this.response.page_offset = Integer.parseInt(this.createCharacters(ch, start, length));
		} else if ("id".equals(nowTag)) {
			this.rest.id.append(this.createCharacters(ch, start, length));
		} else if ("update_date".equals(nowTag)) {
			this.rest.update_date.append(this.createCharacters(ch, start, length));
		} else if ("name".equals(nowTag)) {
			this.rest.name.append(this.createCharacters(ch, start, length));
		} else if ("name_kana".equals(nowTag)) {
			this.rest.name_kana.append(this.createCharacters(ch, start, length));
		} else if ("latitude".equals(nowTag)) {
			this.rest.latitude = Double.parseDouble(this.createCharacters(ch, start, length));
		} else if ("longitude".equals(nowTag)) {
			this.rest.longitude = Double.parseDouble(this.createCharacters(ch, start, length));
		} else if ("category".equals(nowTag)) {
			this.rest.category.append(this.createCharacters(ch, start, length));
		} else if ("url".equals(nowTag)) {
			this.rest.url.append(this.createCharacters(ch, start, length));
		} else if ("url_mobile".equals(nowTag)) {
			this.rest.url_mobile.append(this.createCharacters(ch, start, length));
		} else if ("shop_image1".equals(nowTag)) {
			this.rest.imageUrl.shop_image1.append(this.createCharacters(ch, start, length));
		} else if ("shop_image2".equals(nowTag)) {
			this.rest.imageUrl.shop_image2.append(this.createCharacters(ch, start, length));
		} else if ("qrcode".equals(nowTag)) {
			this.rest.imageUrl.qrcode.append(this.createCharacters(ch, start, length));
		} else if ("address".equals(nowTag)) {
			this.rest.address.append(this.createCharacters(ch, start, length));
		} else if ("tel".equals(nowTag)) {
			this.rest.tel.append(this.createCharacters(ch, start, length));
		} else if ("fax".equals(nowTag)) {
			this.rest.fax.append(this.createCharacters(ch, start, length));
		} else if ("opentime".equals(nowTag)) {
			this.rest.opentime.append(this.createCharacters(ch, start, length));
		} else if ("holiday".equals(nowTag)) {
			this.rest.holiday.append(this.createCharacters(ch, start, length));
		} else if ("line".equals(nowTag)) {
			this.rest.access.line.append(this.createCharacters(ch, start, length));
		} else if ("station".equals(nowTag)) {
			this.rest.access.station.append(this.createCharacters(ch, start, length));
		} else if ("station_exit".equals(nowTag)) {
			this.rest.access.station_exit.append(this.createCharacters(ch, start, length));
		} else if ("walk".equals(nowTag)) {
			this.rest.access.walk.append(this.createCharacters(ch, start, length));
		} else if ("note".equals(nowTag)) {
			this.rest.access.note.append(this.createCharacters(ch, start, length));
		} else if ("pr_short".equals(nowTag)) {
			this.rest.pr.pr_short.append(this.createCharacters(ch, start, length));
		} else if ("pr_long".equals(nowTag)) {
			this.rest.pr.pr_long.append(this.createCharacters(ch, start, length));
		} else if ("areacode".equals(nowTag)) {

		} else if ("areaname".equals(nowTag)) {

		} else if ("prefcode".equals(nowTag)) {

		} else if ("prefname".equals(nowTag)) {

		} else if ("budget".equals(nowTag)) {
			this.rest.budget.append(this.createCharacters(ch, start, length));
		} else if ("equipment".equals(nowTag)) {
			this.rest.equipment.append(this.createCharacters(ch, start, length));
		} else if ("mobile_site".equals(nowTag)) {

		} else if ("mobile_coupon".equals(nowTag)) {

		} else if ("pc_coupon".equals(nowTag)) {

		}
	}

	/**
	 * 文字配列の切り出し
	 * @param ch 文字配列
	 * @param start 文字配列内の開始位置
	 * @param length 文字配列から使用される文字数
	 * @return 切り出された文字列
	 */
	protected String createCharacters(char ch[], int start, int length) {
		StringBuilder sb = new StringBuilder();
		for(int i=start; i<(start + length); i++) {
			sb.append(ch[i]);
		}
		return sb.toString();
	}
}
