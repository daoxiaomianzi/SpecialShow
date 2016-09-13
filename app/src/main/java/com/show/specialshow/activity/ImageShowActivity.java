package com.show.specialshow.activity;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.view.PhotoView;
import com.show.specialshow.view.ViewPagerFixed;

public class ImageShowActivity extends BaseActivity {
	private ArrayList<ShopComcardStaPicsMess> mComcardStaPicsMesses;
	//相关控件
	private ViewPagerFixed imageshow_content_vpf;
	private ArrayList<View> listViews = null;
	private int position;
	//适配器
	private GalleryAdapter mGalleryAdapter;
	private int location=0;
	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		mComcardStaPicsMesses=(ArrayList<ShopComcardStaPicsMess>) getIntent().getSerializableExtra("imageshow");
		position=getIntent().getIntExtra("position", 0);
		for (int i = 0; i < mComcardStaPicsMesses.size(); i++) {
			initListViews(mComcardStaPicsMesses.get(i).getBig_pic());
		}
	}

	@Override
	public void initView() { 
		setContentView(R.layout.activity_image_show);
		
		imageshow_content_vpf=(ViewPagerFixed) findViewById(R.id.imageshow_content_vpf);
		mGalleryAdapter=new GalleryAdapter(listViews);
		imageshow_content_vpf.setAdapter(mGalleryAdapter);
		imageshow_content_vpf.setCurrentItem(position);
	}

	@Override
	public void fillView() {
	}

	@Override
	public void setListener() {
		imageshow_content_vpf.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				location = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		
	}
	private void initListViews(String string) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(mContext);
		img.setBackgroundColor(0xff000000);
		ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
		imageLoderutils.display(img, string);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	class GalleryAdapter extends PagerAdapter {
		private ArrayList<View> imageList;
		private int size;

		public GalleryAdapter(ArrayList<View> listView) {
			this.imageList = listView;
			size = (listView == null) ? 0 : listView.size();
		}

		public void setImageList(ArrayList<View> imageList) {
			this.imageList = imageList;
			size = imageList == null ? 0 : imageList.size();
		}

		public int getSize() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return size;
		}

		public void destroyItem(View view, int arg1, Object arg2) {
			((ViewPagerFixed) view).removeView(imageList.get(arg1 % size));
		}

		public void finishUpdate(View view) {

		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPagerFixed) container).addView(
						imageList.get(position % size), 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return imageList.get(position % size);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}
}
