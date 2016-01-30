package ydeb.hack.migatte.http.dto;

import java.io.Serializable;

/**
 * <pre>
 * 店舗画像の DTO（Data Transfer Object）
 * 店舗画像のURL、QRコード画像のURL など
 * </pre>
 */
public class ImageUrl implements Serializable{
	/** シリアル・バージョン */
	private static final long serialVersionUID = 1L;
	
	/** 店舗画像１のURL */
	public StringBuffer shop_image1 = new StringBuffer();
	/** 店舗画像２のURL */
	public StringBuffer shop_image2 = new StringBuffer();
	/** QRコード画像のURL */
	public StringBuffer qrcode = new StringBuffer();
}
