package ydeb.hack.migatte.http.dto;

import java.io.Serializable;

/**
 * <pre>
 * PR文の DTO（Data Transfer Object）
 * PR短文(50文字)、PR長文(200文字)
 * </pre>
 */
public class Pr implements Serializable{
	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;
	
	/** PR文　最大５０文字 */
	public StringBuffer pr_short = new StringBuffer();
	/** PR文　最大２００文字 */
	public StringBuffer pr_long = new StringBuffer();

}
