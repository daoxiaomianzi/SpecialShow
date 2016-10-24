package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class UserMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3947661553991656102L;
	private String phone;
	private String nickname;
	private String create_time;
	private String icon;
	private String uid;
	private String sex;
	private String birthday;
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	private String address;
	private String sign_name;
	private boolean isLogin;
	private int user_biaoshi;//手艺人标识，1不为手艺人，2 为手艺人
	private boolean ichange;//true为修改过头像和昵称，false没有

	public boolean isSetTradingPass() {
		return setTradingPass;
	}

	public void setSetTradingPass(boolean setTradingPass) {
		this.setTradingPass = setTradingPass;
	}

	private boolean setTradingPass;//true为设置过交易密码，false为没有




	public boolean isIchange() {
		return ichange;
	}

	public void setIchange(boolean ichange) {
		this.ichange = ichange;
	}

	public int getUser_biaoshi() {
		return user_biaoshi;
	}
	public void setUser_biaoshi(int user_biaoshi) {
		this.user_biaoshi = user_biaoshi;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSign_name() {
		return sign_name;
	}
	public void setSign_name(String sign_name) {
		this.sign_name = sign_name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public static UserMessage parse(String result) {
		try {
			return JSON.parseObject(result, UserMessage.class);
		} catch (Exception e) {
			return null;
		}
	}
			
}
