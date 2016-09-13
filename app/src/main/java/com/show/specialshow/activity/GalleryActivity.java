package com.show.specialshow.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.Bimp;
import com.show.specialshow.utils.PublicWay;
import com.show.specialshow.view.PhotoView;
import com.show.specialshow.view.ViewPagerFixed;

import java.util.ArrayList;

public class GalleryActivity extends BaseActivity {
	private RelativeLayout gallery_bottom_rll;
	private Button gallery_ok_btn;
	private ViewPagerFixed gallery_content_vpf;

	// 获取前一个activity传过来的position
	private int position;
	// 当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	private GalleryAdapter adapter;
	private boolean fromsend=false;

	@Override
	public void initData() {
		if (Bimp.onceSelectBitmap.isEmpty()) {
			fromsend=true;
			Bimp.onceSelectBitmap.addAll(Bimp.tempSelectBitmap);
		}

		for (int i = 0; i < Bimp.onceSelectBitmap.size(); i++) {
			initListViews(Bimp.onceSelectBitmap.get(i).getBitmap());
		}
		adapter = new GalleryAdapter(listViews);
		position = Integer.valueOf(getIntent().getExtras()
				.getString("position"));
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_gallery);
		gallery_bottom_rll = (RelativeLayout) findViewById(R.id.gallery_bottom_rll);
		gallery_ok_btn = (Button) findViewById(R.id.gallery_ok_btn);
		gallery_content_vpf = (ViewPagerFixed) findViewById(R.id.gallery_content_vpf);
		isShowOkBt();
		gallery_content_vpf.setPageMargin(getResources()
				.getDimensionPixelOffset(R.dimen.ui_10_dip));
	}

	@Override
	public void fillView() {
		gallery_content_vpf.setAdapter(adapter);
		int id = getIntent().getExtras().getInt("ID", 0);
		gallery_content_vpf.setCurrentItem(id);
		head_right_tv.setVisibility(View.VISIBLE);
		Drawable rightDrawable = getResources().getDrawable(
				R.drawable.plugin_camera_del_state);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
		head_title_tv.setText("图片预览");
	}

	@Override
	public void setListener() {
		gallery_content_vpf.setOnPageChangeListener(new OnPageChangeListener() {

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
		switch (v.getId()) {
		case R.id.gallery_ok_btn:
			finishSelect();
			break;
		case R.id.head_right_tv:
			delectSingle();
			break;
		case R.id.contest_cancel_tv:
			affirmDialog.cancel();
			break;
		case R.id.contest_confirm_tv:
			affirmDialog.cancel();
			delectChosenImage();
		default:
			break;
		}

	}

	private void delectChosenImage() {
		Bimp.tempSelectBitmap.clear();
		Bimp.onceSelectBitmap.clear();
		Bimp.max=0;
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(fromsend){
			Bimp.onceSelectBitmap.clear();
		}
	}

	private void delectSingle() {
		Intent intent = new Intent("data.broadcast.action");
		if (listViews.size() == 1) {
			if(fromsend){
				createAffirmDialog("您确定放弃已选择的图片么？", DIALOG_DEFAULT_STPE,true);
			}else{
				Bimp.onceSelectBitmap.clear();
				Bimp.max = 0;
				sendBroadcast(intent);
				finish();
			}
			gallery_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			
		} else {
			Bimp.onceSelectBitmap.remove(location);
			Bimp.max--;
			gallery_content_vpf.removeAllViews();
			listViews.remove(location);
			adapter.setImageList(listViews);
			gallery_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			adapter.notifyDataSetChanged();
			sendBroadcast(intent);
		}

	}

	private void finishSelect() {
		Bimp.tempSelectBitmap.clear();
		Bimp.tempSelectBitmap.addAll(Bimp.onceSelectBitmap);
		Bimp.max = Bimp.tempSelectBitmap.size();
//		UIHelper.startActivity(mContext, SendCardActivity.class);
		finish();
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(mContext);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	public void isShowOkBt() {
		if (Bimp.onceSelectBitmap.size() > 0) {
			gallery_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			gallery_ok_btn.setPressed(true);
			gallery_ok_btn.setClickable(true);
			gallery_ok_btn.setTextColor(Color.WHITE);
		} else {
			gallery_ok_btn.setPressed(false);
			gallery_ok_btn.setClickable(false);
			gallery_ok_btn.setTextColor(Color.parseColor("#E1E0DE"));
		}
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
