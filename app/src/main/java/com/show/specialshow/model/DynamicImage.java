package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class DynamicImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7910853090134095297L;

	/**
	 * 动态原图
	 */
	private String big_pic;
	/**
	 * 动态缩略图
	 */
	private String thumbnail_pic;

	
	public String getBig_pic() {
		return big_pic;
	}

	public void setBig_pic(String big_pic) {
		this.big_pic = big_pic;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public static DynamicImage parse(String result) {
		return JSON.parseObject(result, DynamicImage.class);
	}
}
