package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShopShopMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9157900764392894039L;
	private String show_shop_introduce;//商户简介
	public String getShow_shop_introduce() {
		return show_shop_introduce;
	}
	public void setShow_shop_introduce(String show_shop_introduce) {
		this.show_shop_introduce = show_shop_introduce;
	}
	public static ShopShopMess parse(String result){
		try {
			return JSON.parseObject(result, ShopShopMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
