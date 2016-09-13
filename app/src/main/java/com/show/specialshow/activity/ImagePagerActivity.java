package com.show.specialshow.activity;

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ImageDetailFragment;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.view.HackyViewPager;

public class ImagePagerActivity extends BaseActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<ShopComcardStaPicsMess> mComcardStaPicsMess;//图片
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			getWindow().clearFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		mComcardStaPicsMess = (List<ShopComcardStaPicsMess>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), mComcardStaPicsMess);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initView() {

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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		private List<ShopComcardStaPicsMess> mComcardStaPicsMess;//图片

		public ImagePagerAdapter(FragmentManager fm, List<ShopComcardStaPicsMess> mComcardStaPicsMess) {
			super(fm);
			this.mComcardStaPicsMess = mComcardStaPicsMess;
		}

		@Override
		public int getCount() {
			return mComcardStaPicsMess == null ? 0 : mComcardStaPicsMess.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url =mComcardStaPicsMess.get(position).getBig_pic();
			return ImageDetailFragment.newInstance(url);
		}

	}
}