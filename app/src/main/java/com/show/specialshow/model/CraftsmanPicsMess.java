package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class CraftsmanPicsMess implements Serializable {
	/**
	 * 作品图片
	 */
	private static final long serialVersionUID = -8896079691544184102L;
	private String thumbnail_pic;//作品图片

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}
	public static List<CraftsmanPicsMess> parse(String result){
		try {
			return JSON.parseArray(result, CraftsmanPicsMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
