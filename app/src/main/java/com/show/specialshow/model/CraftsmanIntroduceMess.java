package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class CraftsmanIntroduceMess implements Serializable {
	/**
	 * 手艺人详情页工作数据相关
	 */
	private static final long serialVersionUID = -2750225378597461734L;
	private String cratsman_introduce_shopId;//入住的商户
	private String cratsman_introduce_fav;//点赞
	private String cratsman_introduce_hot;//人气
	private String cratsman_introduce_shop;//入住店铺
	private String cratsman_introduce_job;//职位
	private String cratsman_introduce_bigIcon;//头像
	private String cratsman_introduce_name;//手艺人姓名
	private String cratsman_introduce_id;//手艺人id
	private int hit;//登录用户是否对其点过赞（0,1？没点过：点过）
	public String getCratsman_introduce_id() {
		return cratsman_introduce_id;
	}
	public void setCratsman_introduce_id(String cratsman_introduce_id) {
		this.cratsman_introduce_id = cratsman_introduce_id;
	}
	public String getCratsman_introduce_shopId() {
		return cratsman_introduce_shopId;
	}
	public void setCratsman_introduce_shopId(String cratsman_introduce_shopId) {
		this.cratsman_introduce_shopId = cratsman_introduce_shopId;
	}
	public String getCratsman_introduce_fav() {
		return cratsman_introduce_fav;
	}
	public void setCratsman_introduce_fav(String cratsman_introduce_fav) {
		this.cratsman_introduce_fav = cratsman_introduce_fav;
	}
	public String getCratsman_introduce_hot() {
		return cratsman_introduce_hot;
	}
	public void setCratsman_introduce_hot(String cratsman_introduce_hot) {
		this.cratsman_introduce_hot = cratsman_introduce_hot;
	}
	public String getCratsman_introduce_shop() {
		return cratsman_introduce_shop;
	}
	public void setCratsman_introduce_shop(String cratsman_introduce_shop) {
		this.cratsman_introduce_shop = cratsman_introduce_shop;
	}
	public String getCratsman_introduce_job() {
		return cratsman_introduce_job;
	}
	public void setCratsman_introduce_job(String cratsman_introduce_job) {
		this.cratsman_introduce_job = cratsman_introduce_job;
	}
	public String getCratsman_introduce_bigIcon() {
		return cratsman_introduce_bigIcon;
	}
	public void setCratsman_introduce_bigIcon(String cratsman_introduce_bigIcon) {
		this.cratsman_introduce_bigIcon = cratsman_introduce_bigIcon;
	}
	public String getCratsman_introduce_name() {
		return cratsman_introduce_name;
	}
	public void setCratsman_introduce_name(String cratsman_introduce_name) {
		this.cratsman_introduce_name = cratsman_introduce_name;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public static CraftsmanIntroduceMess parse(String result){
		try {
			return JSON.parseObject(result, CraftsmanIntroduceMess.class);
		} catch (Exception e) {
			return null;
		}
	}

}
