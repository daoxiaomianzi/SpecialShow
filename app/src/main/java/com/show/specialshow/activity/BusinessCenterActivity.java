package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;

public class BusinessCenterActivity extends BaseBusCenWebActivity {
	@Override
	public void initData() {
		super.initData();
		path=URLs.BUSINESS_CENTER+TXApplication.getUserMess().getUid();
	}
	
	@Override
	public void fillView() {
		super.fillView();
		head_title_tv.setText("商户中心");
		head_left_tv.setVisibility(View.VISIBLE);
		loadDetail();
	}
	


}
