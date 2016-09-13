package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShopInfoImgMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6982709527858946059L;
	private String thumbnail_pic;//秀坊商户图片
	public String getThumbnail_pic() {
		return thumbnail_pic;
	}
	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}
	public static ShopInfoImgMess parse(String result){
		try {
			return JSON.parseObject(result, ShopInfoImgMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
