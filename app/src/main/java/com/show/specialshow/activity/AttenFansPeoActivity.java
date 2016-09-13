package com.show.specialshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.ShowerFollerAdapter;
import com.show.specialshow.model.ShowerFollowerCountMess;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

public class AttenFansPeoActivity extends BaseActivity {
	private GridView atten_fans_peo_gv;//
	
	private List<ShowerFollowerCountMess> attenFansMesses;// 关注的人//接受的数据
	private String title;//
	
	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		title=getIntent().getExtras().getString(ShowerDetailsActivity.TITLE);
		attenFansMesses=(List<ShowerFollowerCountMess>) getIntent().getExtras().getSerializable(ShowerDetailsActivity.ATTEN_FANS);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_atten_fans_peo);
		atten_fans_peo_gv=(GridView) findViewById(R.id.atten_fans_peo_gv);
	}

	@Override
	public void fillView() {
		head_title_tv.setText(title);
		atten_fans_peo_gv.setAdapter(new ShowerFollerAdapter(mContext, attenFansMesses, 2));
	}

	@Override
	public void setListener() {
		atten_fans_peo_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle=new Bundle();
				bundle.putString("user_id", attenFansMesses.get(position).getFav_id());
				UIHelper.startActivity(mContext, ShowerDetailsActivity.class,bundle);
			}
		});
	}

	@Override
	public void onClick(View v) {
		
	}



}
