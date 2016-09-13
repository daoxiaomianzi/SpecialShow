package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopReviewMess implements Serializable {

	/**
	 * 点评数据
	 */
	private static final long serialVersionUID = 8350038028608909740L;
	private String comment_icon;//点评头像
	private String comment_name;//点评用户名
	private String comment_uid;//点评用户id

	public String getComment_uid() {
		return comment_uid;
	}

	public void setComment_uid(String comment_uid) {
		this.comment_uid = comment_uid;
	}

	private String comment_time;//点评时间
	private String comment_total;//点评内容
	private String status_pics;//
	private String c_count;//
	private String x_count;//
	private String d_count;//
	public String getComment_icon() {
		return comment_icon;
	}
	public void setComment_icon(String comment_icon) {
		this.comment_icon = comment_icon;
	}
	public String getComment_name() {
		return comment_name;
	}
	public void setComment_name(String comment_name) {
		this.comment_name = comment_name;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}
	public String getComment_total() {
		return comment_total;
	}
	public void setComment_total(String comment_total) {
		this.comment_total = comment_total;
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
	public static List<ShopReviewMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopReviewMess.class);
		} catch (Exception e) {
			return null;
		}
	}

}
