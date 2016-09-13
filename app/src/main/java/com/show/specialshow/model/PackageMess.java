package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class PackageMess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2822986322461092275L;
	private String package_what;
	private String package_title;
	private String shop_id;
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getPackage_what() {
		return package_what;
	}
	public void setPackage_what(String package_what) {
		this.package_what = package_what;
	}
	public String getPackage_title() {
		return package_title;
	}
	public void setPackage_title(String package_title) {
		this.package_title = package_title;
	}
	public static PackageMess parse(String result){
		try {
			return JSON.parseObject(result, PackageMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
