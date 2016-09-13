package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShowerCommedMess implements Serializable {
	
	/**
	 * 秀客详情中秀卡，蹭卡，动态的数据
	 */
	private static final long serialVersionUID = 3750518175986224508L;
	private String status_content;//动态正文
	private String status_user;//动态用户信息
	private String status_pics;//动态图片
	private String c_count;//蹭卡数
	private String x_count;//秀卡数
	private String d_count;//点评数
	private String status_createTime;//动态时间
	private String tags;//标签
	public String getStatus_content() {
		return status_content;
	}
	public void setStatus_content(String status_content) {
		this.status_content = status_content;
	}
	public String getStatus_user() {
		return status_user;
	}
	public void setStatus_user(String status_user) {
		this.status_user = status_user;
	}
	public String getStatus_pics() {
		return status_pics;
	}
	public void setStatus_pics(String status_pics) {
		this.status_pics = status_pics;
	}
	public String getC_count() {
		return c_count;
	}
	public void setC_count(String c_count) {
		this.c_count = c_count;
	}
	public String getX_count() {
		return x_count;
	}
	public void setX_count(String x_count) {
		this.x_count = x_count;
	}
	public String getD_count() {
		return d_count;
	}
	public void setD_count(String d_count) {
		this.d_count = d_count;
	}
	public String getStatus_createTime() {
		return status_createTime;
	}
	public void setStatus_createTime(String status_createTime) {
		this.status_createTime = status_createTime;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public static List<ShowerCommedMess> parse(String result){
		try {
			return JSON.parseArray(result, ShowerCommedMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
