package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ImageDetailFragment;

@SuppressLint("NewApi")
public class OneImageShowActivity extends BaseActivity {
	public static final String ONE_IMAGE_URL="one_image_url";
	private String imageUrl;

	@Override
	public void initData() {
		imageUrl=getIntent().getExtras().getString(ONE_IMAGE_URL, "");
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_one_image_show);
		ImageDetailFragment fragment= ImageDetailFragment.newInstance(imageUrl);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.rll_image_show, fragment)
		.hide(fragment).show(fragment).commit();
	}

	@Override
	public void fillView() {
		
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		
	}



}
