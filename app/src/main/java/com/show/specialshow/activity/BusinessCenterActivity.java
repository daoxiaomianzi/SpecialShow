package com.show.specialshow.activity;

import android.view.KeyEvent;
import android.view.View;

import com.show.specialshow.R;
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && content.canGoBack()) {
				content.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void goBack(View v) {
		switch (v.getId()) {
		case R.id.head_left_tv:
			if (content.canGoBack()) {
				content.goBack();
			} else {
				mContext.finish();
			}
			break;
		case R.id.head_close_tv:
			mContext.finish();
			break;

		default:
			break;
		}
	}

}
