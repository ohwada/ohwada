package ydeb.hack.migatte.http.response;

import java.io.Serializable;
import java.util.ArrayList;

import ydeb.hack.migatte.http.dto.GnaviDto;

/**
 * <pre>
 * ぐるなび情報の直列化のための構造体を定義する
 * 該当件数、レストラン情報 など
 * </pre>
 */
public class Response implements Serializable{
	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;
	
	/** 該当件数 */
	public int total_hit_count;
	/** 表示件数 */
	public int hit_per_page;
	/** 表示ページ */
	public int page_offset;
	/** レストラン情報 */
	public ArrayList<GnaviDto> resultRest = new ArrayList<GnaviDto>();
}
