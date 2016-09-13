package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class CraftsmanMess implements Serializable {
	/**
	 * 手艺人详情页数据
	 */
	private static final long serialVersionUID = -3863418718288467377L;
	private String cratsman_info;
	private String cratsman_introduce;
	private String cratsman_pics;//作品图片
	private String service_info;//服务数据
	public String getService_info() {
		return service_info;
	}
	public void setService_info(String service_info) {
		this.service_info = service_info;
	}
	public String getCratsman_info() {
		return cratsman_info;
	}
	public void setCratsman_info(String cratsman_info) {
		this.cratsman_info = cratsman_info;
	}
	public String getCratsman_introduce() {
		return cratsman_introduce;
	}
	public void setCratsman_introduce(String cratsman_introduce) {
		this.cratsman_introduce = cratsman_introduce;
	}
	public String getCratsman_pics() {
		return cratsman_pics;
	}
	public void setCratsman_pics(String cratsman_pics) {
		this.cratsman_pics = cratsman_pics;
	}
	public static CraftsmanMess parse(String result){
		try {
			return JSON.parseObject(result, CraftsmanMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
