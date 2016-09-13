package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopCommendcardStatusMess implements Serializable {

	/**
	 * 秀坊商户秀卡动态数据
	 */
	private static final long serialVersionUID = -7758934791819768649L;
	private String status_content;//动态正文
	private String status_user;//动态用户信息
	private String status_pics;//动态图片
	private String comment_icon;//点评头像
	private String comment_name;//点评用户名
	private String comment_time;//点评时间
	private String comment_total;//点评内容
	private String c_count;//蹭卡数
	private String x_count;//秀卡数
	private String d_count;//点评数
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
	public static List<ShopCommendcardStatusMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopCommendcardStatusMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
