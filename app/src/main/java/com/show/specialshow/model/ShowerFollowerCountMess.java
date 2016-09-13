package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShowerFollowerCountMess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2946840687121709276L;
	private String id;
	private String nickname;
	private String fav_icon;
	private String fav_id;
	private String fav_name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFav_icon() {
		return fav_icon;
	}
	public void setFav_icon(String fav_icon) {
		this.fav_icon = fav_icon;
	}
	public String getFav_id() {
		return fav_id;
	}
	public void setFav_id(String fav_id) {
		this.fav_id = fav_id;
	}
	public String getFav_name() {
		return fav_name;
	}
	public void setFav_name(String fav_name) {
		this.fav_name = fav_name;
	}
	public static List<ShowerFollowerCountMess> parse(String result){
		try {
			return JSON.parseArray(result,ShowerFollowerCountMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
