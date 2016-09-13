package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class DynamicUser implements Serializable {

	private static final long serialVersionUID = 3830157885329052516L;

	/**
	 * 是否关注(1、已关注，2、未关注)
	 */
	private String attention;
	private int is_del;
	/**
	 * 用户头像
	 */
	private String user_icon;
	/**
	 * 用户ID
	 */
	private String user_id;
	public int getIs_del() {
		return is_del;
	}

	public void setIs_del(int is_del) {
		this.is_del = is_del;
	}

	/**
	 * 用户名
	 */
	private String user_name;
	/**
	 * 用户发布动态时间
	 */
	private String user_statusCreateTime;

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public static DynamicUser parse(String result) {
		return JSON.parseObject(result, DynamicUser.class);
	}
}
