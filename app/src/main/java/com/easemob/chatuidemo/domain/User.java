/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.domain;

import com.alibaba.fastjson.JSON;
import com.easemob.chat.EMContact;

public class User extends EMContact {
	private int unreadMsgCount;
	private String header;
	private String avatar;
	private String age;
	private String emid;
	private String icon;
	private String nickname;
private String phone;
private String sex;
private long update_time;
private String xingzuo;
	
	public String getAge() {
	return age;
}

public void setAge(String age) {
	this.age = age;
}

public String getEmid() {
	return emid;
}

public void setEmid(String emid) {
	this.emid = emid;
}

public String getIcon() {
	return icon;
}

public void setIcon(String icon) {
	this.icon = icon;
}

public String getNickname() {
	return nickname;
}

public void setNickname(String nickname) {
	this.nickname = nickname;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

public String getSex() {
	return sex;
}

public void setSex(String sex) {
	this.sex = sex;
}

public long getUpdate_time() {
	return update_time;
}

public void setUpdate_time(long update_time) {
	this.update_time = update_time;
}

public String getXingzuo() {
	return xingzuo;
}

public void setXingzuo(String xingzuo) {
	this.xingzuo = xingzuo;
}

	public User(){}
	
	public User(String username){
	    this.username = username;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}
	
	

	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}
		return getUsername().equals(((User) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
	
	public static User parse(String result){
		try {
			return JSON.parseObject(result, User.class);
		} catch (Exception e) {
			return null;
		}
	}
}
