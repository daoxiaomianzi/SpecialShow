package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopServiceMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7023403061342548282L;
	private String service_list_discount;//优惠多少
	private String service_list_price_old;//旧价格
	private String service_list_price_now;//当前价格
	private String service_list_title;//服务名称
	private String service_list_icon;//服务图片
	private String service_list_des;//服务介绍
	private String service_list_id;//服务id
	
	public String getService_list_id() {
		return service_list_id;
	}
	public void setService_list_id(String service_list_id) {
		this.service_list_id = service_list_id;
	}
	public String getService_list_des() {
		return service_list_des;
	}
	public void setService_list_des(String service_list_des) {
		this.service_list_des = service_list_des;
	}
	public String getService_list_discount() {
		return service_list_discount;
	}
	public void setService_list_discount(String service_list_discount) {
		this.service_list_discount = service_list_discount;
	}
	public String getService_list_price_old() {
		return service_list_price_old;
	}
	public void setService_list_price_old(String service_list_price_old) {
		this.service_list_price_old = service_list_price_old;
	}
	public String getService_list_price_now() {
		return service_list_price_now;
	}
	public void setService_list_price_now(String service_list_price_now) {
		this.service_list_price_now = service_list_price_now;
	}
	public String getService_list_title() {
		return service_list_title;
	}
	public void setService_list_title(String service_list_title) {
		this.service_list_title = service_list_title;
	}
	public String getService_list_icon() {
		return service_list_icon;
	}
	public void setService_list_icon(String service_list_icon) {
		this.service_list_icon = service_list_icon;
	}
	public static List<ShopServiceMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopServiceMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
