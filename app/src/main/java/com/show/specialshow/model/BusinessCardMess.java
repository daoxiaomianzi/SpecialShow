package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class BusinessCardMess implements Serializable {
	/**
	 * 手艺人个人信息资料
	 */
	private static final long serialVersionUID = 5643096806449186779L;
	private String pic;//头像
	private String shop;//所属秀坊
	private String staffname;//手艺人名称
	private String rank;//头衔
	private String worktime;//从业时间
	private String goodat;//擅长
	private String introduction;//简介
	private String staffid;//手艺人id
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getStaffname() {
		return staffname;
	}
	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getWorktime() {
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getGoodat() {
		return goodat;
	}
	public void setGoodat(String goodat) {
		this.goodat = goodat;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getStaffid() {
		return staffid;
	}
	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}
	public static BusinessCardMess parse(String result){
		try {
			return JSON.parseObject(result, BusinessCardMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
