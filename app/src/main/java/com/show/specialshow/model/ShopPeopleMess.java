package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopPeopleMess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -359423842821540195L;
	private String choice_artisans_job;//职位
	private String choice_artisans_hot;//人气
	private String choice_artisans_name;//名字
	private String choice_artisans_icon;//手艺人头像图片
	private String choice_artisans_id;//手艺人id
	public String getChoice_artisans_id() {
		return choice_artisans_id;
	}
	public void setChoice_artisans_id(String choice_artisans_id) {
		this.choice_artisans_id = choice_artisans_id;
	}
	public String getChoice_artisans_job() {
		return choice_artisans_job;
	}
	public void setChoice_artisans_job(String choice_artisans_job) {
		this.choice_artisans_job = choice_artisans_job;
	}
	public String getChoice_artisans_hot() {
		return choice_artisans_hot;
	}
	public void setChoice_artisans_hot(String choice_artisans_hot) {
		this.choice_artisans_hot = choice_artisans_hot;
	}
	public String getChoice_artisans_name() {
		return choice_artisans_name;
	}
	public void setChoice_artisans_name(String choice_artisans_name) {
		this.choice_artisans_name = choice_artisans_name;
	}
	public String getChoice_artisans_icon() {
		return choice_artisans_icon;
	}
	public void setChoice_artisans_icon(String choice_artisans_icon) {
		this.choice_artisans_icon = choice_artisans_icon;
	}
	public static List<ShopPeopleMess> parse(String result){
		try {
			return JSON.parseArray(result, ShopPeopleMess.class);
		} catch (Exception e) {
			return null;
		}
	}
	
}
