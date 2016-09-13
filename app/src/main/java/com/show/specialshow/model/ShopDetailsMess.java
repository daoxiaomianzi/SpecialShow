package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
/**
 * 商户详情数据
 * @author admin
 *
 */
public class ShopDetailsMess implements Serializable {

	private static final long serialVersionUID = 3570737684698972731L;
	private String shop_info;//店铺地址，电话等信息
	private String show_shop;//店铺简介等
	private String shop_info_img;//
	private String shop_service;//服务列表
	private int store_count;//分店数
	private String shop_people;//手艺人
	private String package_shop;//商户信息
	
	public int getStore_count() {
		return store_count;
	}
	public void setStore_count(int store_count) {
		this.store_count = store_count;
	}
	public String getPackage_shop() {
		return package_shop;
	}
	public void setPackage_shop(String package_shop) {
		this.package_shop = package_shop;
	}
	public String getShop_info() {
		return shop_info;
	}
	public void setShop_info(String shop_info) {
		this.shop_info = shop_info;
	}
	public String getShow_shop() {
		return show_shop;
	}
	public void setShow_shop(String show_shop) {
		this.show_shop = show_shop;
	}
	public String getShop_info_img() {
		return shop_info_img;
	}
	public void setShop_info_img(String shop_info_img) {
		this.shop_info_img = shop_info_img;
	}
	public String getShop_service() {
		return shop_service;
	}
	public void setShop_service(String shop_service) {
		this.shop_service = shop_service;
	}
	public String getShop_people() {
		return shop_people;
	}
	public void setShop_people(String shop_people) {
		this.shop_people = shop_people;
	}
	public static ShopDetailsMess parse(String result){
		try {
			return JSON.parseObject(result, ShopDetailsMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
