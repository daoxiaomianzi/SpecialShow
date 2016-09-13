package com.show.specialshow.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ImageDetailFragment;
import com.show.specialshow.model.DynamicImage;
import com.show.specialshow.view.ViewPagerFixed;

import java.util.List;

public class DynamicImagePagerActivity extends BaseActivity {
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String EXTRA_IMAGE_INDEX = "image_index";

	private ViewPagerFixed mPager;
	private TextView dynamic_img_indicator;
	private List<DynamicImage> mList;
	private int pagerPosition;
	private ImagePagerAdapter mAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
//		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//			getWindow().clearFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		Bundle bundle = getIntent().getExtras();
		mList = (List<DynamicImage>) bundle.getSerializable(EXTRA_IMAGE_URLS);
		pagerPosition = getIntent().getExtras().getInt(EXTRA_IMAGE_INDEX);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_dynamic_image_pager);
		mPager = (ViewPagerFixed) findViewById(R.id.dynamic_page);
		dynamic_img_indicator = (TextView) findViewById(R.id.dynamic_img_indicator);
	}

	@Override
	public void fillView() {
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(pagerPosition);
		CharSequence text = getString(R.string.viewpager_indicator,
				pagerPosition+1, mPager.getAdapter().getCount());
		dynamic_img_indicator.setText(text);
	}

	@Override
	public void setListener() {
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				dynamic_img_indicator.setText(text);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			String url = mList.get(position).getBig_pic();
//			final DynamicImageDetailFragment f = new DynamicImageDetailFragment(url);
			return ImageDetailFragment.newInstance(url);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList == null ? 0 : mList.size();
		}

	}
}
