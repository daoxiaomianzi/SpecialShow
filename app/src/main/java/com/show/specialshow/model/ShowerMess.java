package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShowerMess implements Serializable {
	/**
	 * 秀客详情数据
	 */
	private static final long serialVersionUID = -4165256223471636956L;
	private String info;//秀客基本信息
	private String card;//名片信息
	private String follower;//关注和粉丝
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public static ShowerMess parse(String result){
		try {
			return JSON.parseObject(result, ShowerMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
