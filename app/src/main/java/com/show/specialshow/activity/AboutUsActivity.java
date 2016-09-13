package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseWebActivity;
import com.show.specialshow.R;
import com.show.specialshow.URLs;

public class AboutUsActivity extends BaseWebActivity {

	@Override
	public void initData() {
		super.initData();
		path=URLs.ABOUS_US;
	}
	
	@Override
	public void fillView() {
		super.fillView();
		head_title_tv.setText(R.string.abouts_us);
		head_left_tv.setVisibility(View.VISIBLE);
		loadDetail();
	}

}
