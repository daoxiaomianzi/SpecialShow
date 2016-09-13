package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopComcardStaPicsMess implements Serializable {

	/**
	 * 秀坊商户动态图片
	 */
	private static final long serialVersionUID = 6827560645578805878L;
	private String big_pic;
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
	public static List<ShopComcardStaPicsMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopComcardStaPicsMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
