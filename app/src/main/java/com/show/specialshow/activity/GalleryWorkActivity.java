package com.show.specialshow.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.view.PhotoView;
import com.show.specialshow.view.ViewPagerFixed;

import java.util.ArrayList;

public class GalleryWorkActivity extends BaseActivity {
	private ArrayList<Integer> myWorkPhoto;
	//相关控件
	private ViewPagerFixed gallerywork_content_vpf;
	private ArrayList<View> listViews = null;
	//适配器
	private GalleryAdapter mGalleryAdapter;
	@Override
	public void initData() {
		myWorkPhoto=getIntent().getIntegerArrayListExtra("workphotos");
		for (int i = 0; i < myWorkPhoto.size(); i++) {
			initListViews(myWorkPhoto.get(i));
		}
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_gallery_work);
		gallerywork_content_vpf=(ViewPagerFixed) findViewById(R.id.gallerywork_content_vpf);
		mGalleryAdapter=new GalleryAdapter(listViews);
		gallerywork_content_vpf.setAdapter(mGalleryAdapter);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("我的作品");
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
	private void initListViews(int resId) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(mContext);
		img.setBackgroundColor(0xff000000);
		img.setImageResource(resId);
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
