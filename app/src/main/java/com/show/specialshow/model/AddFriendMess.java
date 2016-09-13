package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class AddFriendMess implements Serializable {

	/**
	 * 添加好友的数据model
	 */
	private static final long serialVersionUID = 5160899512879013794L;
	private int emid;
	private String icon;
	private String nickname;
	private String phone;
	public int getEmid() {
		return emid;
	}
	public void setEmid(int emid) {
		this.emid = emid;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public static List<AddFriendMess> parse(String result){
		try {
			return JSON.parseArray(result, AddFriendMess.class);
		} catch (Exception e) {
			return null;
		}
	}

}
