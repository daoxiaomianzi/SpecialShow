package com.show.specialshow.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopList extends BaseList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -591990552419261156L;

	private List<ShopItem> list;

	public List<ShopItem> getList() {
		return list;
	}

	public void setList(List<ShopItem> list) {
		this.list = list;
	}

	public static ShopList parse(String result) {
		return JSON.parseObject(result, ShopList.class);
	}

}
