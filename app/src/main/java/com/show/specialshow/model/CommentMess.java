package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
/*8
 * 评论数据
 */
public class CommentMess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4997890758576405534L;
	private String Status_comment;//评论相关
	private String Status_fav;//点赞相关
	private String userInfo;//动态数据
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public String getStatus_comment() {
		return Status_comment;
	}
	public void setStatus_comment(String status_comment) {
		Status_comment = status_comment;
	}
	public String getStatus_fav() {
		return Status_fav;
	}
	public void setStatus_fav(String status_fav) {
		Status_fav = status_fav;
	}
	public static CommentMess parse(String result){
		try {
			return JSON.parseObject(result, CommentMess.class);
		} catch (Exception e) {
			return null;
		}
	}
}
