package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShowerInfoMess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2393652098247817960L;
	private String icon;//
	private String user_name;//昵称
	private String user_sex;//性别
	private String user_age;//年龄
	private String user_constellation;//星座
	private String user_introduce;//简介
	private String user_where;//常居地
	private String user_icon;//头像
	private String tags;//兴趣标签
	private int attention;//是否关注（2为没有关注）

	public int getAttention() {
		return attention;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_age() {
		return user_age;
	}

	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}

	public String getUser_constellation() {
		return user_constellation;
	}

	public void setUser_constellation(String user_constellation) {
		this.user_constellation = user_constellation;
	}

	public String getUser_introduce() {
		return user_introduce;
	}

	public void setUser_introduce(String user_introduce) {
		this.user_introduce = user_introduce;
	}

	public String getUser_where() {
		return user_where;
	}

	public void setUser_where(String user_where) {
		this.user_where = user_where;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	public static ShowerInfoMess parse(String result){
		try {
			return JSON.parseObject(result, ShowerInfoMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
