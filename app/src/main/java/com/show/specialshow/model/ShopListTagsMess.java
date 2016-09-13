package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopListTagsMess implements Serializable {

	/**
	 * 标签
	 */
	private static final long serialVersionUID = 8691440900667589167L;
	private String tag_name;
	public String getTag_name() {
		return tag_name;
	}
	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}
	public static List<ShopListTagsMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopListTagsMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
