package com.show.specialshow.wheelcity;

import android.view.View;

public class WheelMain {
	private View view;
	private WheelView wv_peoplenum;
	private boolean hasSelectTime;
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public WheelView getWv_peoplenum() {
		return wv_peoplenum;
	}
	public void setWv_peoplenum(WheelView wv_peoplenum) {
		this.wv_peoplenum = wv_peoplenum;
	}
	public WheelMain(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMain(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}
	/**
	 * 弹出选择器
	 */
}
