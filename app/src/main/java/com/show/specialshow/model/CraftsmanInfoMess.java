package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class CraftsmanInfoMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5627991231335544358L;
	private String cratsman_info_icon;//头像
	private String cratsman_info_good;//擅长
	private String cratsman_info_about;//简介
	private String cratsman_info_space;//个人空间
	private String cratsman_info_work_time;//从业时间
	public String getCratsman_info_icon() {
		return cratsman_info_icon;
	}
	public void setCratsman_info_icon(String cratsman_info_icon) {
		this.cratsman_info_icon = cratsman_info_icon;
	}
	public String getCratsman_info_good() {
		return cratsman_info_good;
	}
	public void setCratsman_info_good(String cratsman_info_good) {
		this.cratsman_info_good = cratsman_info_good;
	}
	public String getCratsman_info_about() {
		return cratsman_info_about;
	}
	public void setCratsman_info_about(String cratsman_info_about) {
		this.cratsman_info_about = cratsman_info_about;
	}
	public String getCratsman_info_space() {
		return cratsman_info_space;
	}
	public void setCratsman_info_space(String cratsman_info_space) {
		this.cratsman_info_space = cratsman_info_space;
	}
	public String getCratsman_info_work_time() {
		return cratsman_info_work_time;
	}
	public void setCratsman_info_work_time(String cratsman_info_work_time) {
		this.cratsman_info_work_time = cratsman_info_work_time;
	}
	public static CraftsmanInfoMess parse(String result){
		try {
			return JSON.parseObject(result, CraftsmanInfoMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
