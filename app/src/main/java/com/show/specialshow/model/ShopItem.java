package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShopItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6549702294986520710L;

	private String shop_id;
	private String shop_names;

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_names() {
		return shop_names;
	}

	public void setShop_names(String shop_names) {
		this.shop_names = shop_names;
	}

	public static ShopItem parse(String result) {
		try {
			return JSON.parseObject(result, ShopItem.class);
		} catch (Exception e) {
			return null;
		}
	}

}
