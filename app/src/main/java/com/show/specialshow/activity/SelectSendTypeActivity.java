package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

public class SelectSendTypeActivity extends BaseActivity {
	public final static int SHOW_CARD_CODE = 1;
	public final static int BORROW_CARD_CODE = 2;
	public final static int SEND_STATE_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("InlinedApi")
	@Override
	public void initView() {
		setContentView(R.layout.activity_select_send_type);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("选择动态类型");
	}

	@Override
	public void setListener() {

	}

	@Override
	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.send_show_card_rll:
			bundle.putInt("send_type", SHOW_CARD_CODE);
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		case R.id.send_loiter_card_rll:
			bundle.putInt("send_type", BORROW_CARD_CODE);
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		case R.id.send_dynamic_rll:
			bundle.putInt("send_type", SEND_STATE_CODE);
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		}
	}
}
