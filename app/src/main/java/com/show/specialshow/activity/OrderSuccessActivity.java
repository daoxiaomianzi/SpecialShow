package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

public class OrderSuccessActivity extends BaseActivity {

	@Override
	public void initData() {
		setContentView(R.layout.activity_order_success);
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void fillView() {
		head_title_tv.setText("预约");
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.look_my_reservation:
			UIHelper.startActivity(mContext, MyBookingActivity.class);
			break;

		default:
			break;
		}
	}



}
