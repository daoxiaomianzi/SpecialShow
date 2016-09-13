package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShopComcardStaUserMess implements Serializable {

	/**
	 * 秀坊商户动态的用户信息
	 */
	private static final long serialVersionUID = -5715935776289130037L;
	private String user_icon;
	private String user_name;
	private String user_statusCreateTime;
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_icon() {
		return user_icon;
	}
	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_statusCreateTime() {
		return user_statusCreateTime;
	}
	public void setUser_statusCreateTime(String user_statusCreateTime) {
		this.user_statusCreateTime = user_statusCreateTime;
	}
	public static ShopComcardStaUserMess parse(String result){
		try {
			return JSON.parseObject(result, ShopComcardStaUserMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
