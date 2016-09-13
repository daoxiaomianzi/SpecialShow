package com.show.specialshow.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShowVisitorList extends BaseList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3236200017432762003L;
	private List<ShopVisitorListMess> list;

	public List<ShopVisitorListMess> getList() {
		return list;
	}

	public void setList(List<ShopVisitorListMess> list) {
		this.list = list;
	}
	public static ShowVisitorList parse(String result) {
		try {
			return JSON.parseObject(result, ShowVisitorList.class);
			
		} catch (Exception e) {
			return null;
		}
	}
}
