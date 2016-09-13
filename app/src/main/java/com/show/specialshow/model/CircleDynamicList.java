package com.show.specialshow.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class CircleDynamicList extends BaseList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1265606923510463L;

	private List<CircleDynamicItem> list;

	public List<CircleDynamicItem> getList() {
		return list;
	}

	public void setList(List<CircleDynamicItem> list) {
		this.list = list;
	}

	public static CircleDynamicList parse(String result) {
		return JSON.parseObject(result, CircleDynamicList.class);
	}

}
