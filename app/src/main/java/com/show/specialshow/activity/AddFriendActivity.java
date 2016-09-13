package com.show.specialshow.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;

public class AddFriendActivity extends BaseActivity {
	public static final String keyword="keyword";
	//相关控件
	private EditText add_friend_et;

	@Override
	public void initData() {
		setContentView(R.layout.activity_add_friend);
	}

	@Override
	public void initView() {
		add_friend_et=(EditText) findViewById(R.id.add_friend_et);
	}

	@Override
	public void fillView() {
		head_title_tv.setText(R.string.addfriend);
	}
	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		if(!BtnUtils.getInstance().isFastDoubleClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.add_friend_btn:
//			if(TextUtils.isEmpty(add_friend_et.getText().toString().trim())){
//				createAffirmDialog("请输入名称或手机号", DIALOG_SINGLE_STPE,true);
//			}else{
				Bundle bundle=new Bundle();
				bundle.putString(keyword, add_friend_et.getText().toString().trim());
				UIHelper.startActivity(mContext, SearchAddFriendActivity.class, bundle);
//			}
			break;
		case R.id.contest_confirm_tv:
			affirmDialog.dismiss();
			break;
		default:
			break;
		}
	}

}
