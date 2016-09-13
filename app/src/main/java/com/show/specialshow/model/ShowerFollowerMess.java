package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShowerFollowerMess implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -517705291109967241L;
private String attention_count;//关注
private String fan_count;//粉丝
public String getAttention_count() {
	return attention_count;
}
public void setAttention_count(String attention_count) {
	this.attention_count = attention_count;
}
public String getFan_count() {
	return fan_count;
}
public void setFan_count(String fan_count) {
	this.fan_count = fan_count;
}
public static ShowerFollowerMess parse(String result){
	try {
		return JSON.parseObject(result,ShowerFollowerMess.class);
	} catch (Exception e) {
		return null;
	}
}
}
