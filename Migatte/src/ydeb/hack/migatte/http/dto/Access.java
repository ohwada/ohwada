package ydeb.hack.migatte.http.dto;

import java.io.Serializable;

/**
 * <pre>
 * 駅の DTO（Data Transfer Object）
 * 駅名、路線名、駅出口 など
 * </pre>
 */
public class Access implements Serializable {
	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;

	/** 路線名 */
	public StringBuffer line = new StringBuffer();
	/** 駅名 */
	public StringBuffer station = new StringBuffer();
	/** 駅出口 */
	public StringBuffer station_exit = new StringBuffer();
	/** 徒歩分 */
	public StringBuffer walk = new StringBuffer();
	/** 備考 */
	public StringBuffer note = new StringBuffer();
}
