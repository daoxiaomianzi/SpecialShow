package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShowerCardMess implements Serializable {
	/**
	 * 秀客名片信息
	 */
	private static final long serialVersionUID = 4491092769677466525L;
	private String user_icon;//头像
	private String user_name;//昵称
	private String user_job;//职位
	private String user_hot;//人气
	private String user_fav;//点赞
	private String user_shop;//入住店铺
	private String shop_id;//商户id
	private int user_biaoshi;//标识此人是否是手艺人（2为手艺人）
	public int getUser_biaoshi() {
		return user_biaoshi;
	}
	public void setUser_biaoshi(int user_biaoshi) {
		this.user_biaoshi = user_biaoshi;
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
	public String getUser_job() {
		return user_job;
	}
	public void setUser_job(String user_job) {
		this.user_job = user_job;
	}
	public String getUser_hot() {
		return user_hot;
	}
	public void setUser_hot(String user_hot) {
		this.user_hot = user_hot;
	}
	public String getUser_fav() {
		return user_fav;
	}
	public void setUser_fav(String user_fav) {
		this.user_fav = user_fav;
	}
	public String getUser_shop() {
		return user_shop;
	}
	public void setUser_shop(String user_shop) {
		this.user_shop = user_shop;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public static ShowerCardMess parse(String result){
		try {
			return JSON.parseObject(result, ShowerCardMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
