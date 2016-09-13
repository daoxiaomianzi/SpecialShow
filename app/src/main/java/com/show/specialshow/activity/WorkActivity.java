package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.MyWorkAdapter;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.utils.UIHelper;

import java.util.ArrayList;

public class WorkActivity extends BaseActivity {
	private GridView my_work_gv;
	private ArrayList<ShopComcardStaPicsMess> myWorkPhoto;
	private MyWorkAdapter myWorkAdapter;
	private String nickname;
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public void initData() {
		nickname=getIntent().getExtras().getString("nick_name", "");
		myWorkPhoto=(ArrayList<ShopComcardStaPicsMess>) getIntent().getExtras().getSerializable("work_pics");
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_work);
		my_work_gv=(GridView) findViewById(R.id.my_work_gv);
	}

	@Override
	public void fillView() {
		head_title_tv.setText(nickname+"的作品");
//		head_right_tv.setVisibility(View.VISIBLE);
//		head_right_tv.setText("添加");
		myWorkAdapter=new MyWorkAdapter(mContext, myWorkPhoto,1);
		my_work_gv.setAdapter(myWorkAdapter);
	}

	@Override
	public void setListener() {
		my_work_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(
						ImagePagerActivity.EXTRA_IMAGE_URLS,
						myWorkPhoto
						);
				bundle.putInt(
						ImagePagerActivity.EXTRA_IMAGE_INDEX,
						position);
				UIHelper.startActivity(mContext,
						ImagePagerActivity.class, bundle);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv:
			UIHelper.startActivity(mContext, AddWorkActivity.class);
			break;

		default:
			break;
		}
	}

}
