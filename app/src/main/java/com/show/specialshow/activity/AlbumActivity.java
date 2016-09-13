package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.AlbumGridViewAdapter;
import com.show.specialshow.model.Bimp;
import com.show.specialshow.model.ImageBucket;
import com.show.specialshow.model.ImageItem;
import com.show.specialshow.utils.AlbumHelper;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.PublicWay;
import com.show.specialshow.utils.UIHelper;

public class AlbumActivity extends BaseActivity {
	// 显示手机里的所有图片的列表控件
	private GridView album_content_gv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView album_none_tv;
	// 完成按钮
	private Button album_ok_btn;
	// 预览按钮
	private Button album_preview_btn;

	public static Bitmap bitmap;

	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;

	@Override
	public void initData() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		contentList = helper.getImagesBucketList(true);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}
		Bimp.onceSelectBitmap.clear();
		Bimp.onceSelectBitmap.addAll(Bimp.tempSelectBitmap);
		// gridImageAdapter = new AlbumGridViewAdapter(mContext, dataList,
		// Bimp.tempSelectBitmap);
		gridImageAdapter = new AlbumGridViewAdapter(mContext, dataList,
				Bimp.onceSelectBitmap);

		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			gridImageAdapter.notifyDataSetChanged();
			isShowOkBt();
		}
	};

	@Override
	public void initView() {
		setContentView(R.layout.activity_album);
		album_content_gv = (GridView) findViewById(R.id.album_content_gv);
		album_none_tv = (TextView) findViewById(R.id.album_none_tv);
		album_ok_btn = (Button) findViewById(R.id.album_ok_btn);
		album_preview_btn = (Button) findViewById(R.id.album_preview_btn);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.plugin_camera_no_pictures);
		isShowOkBt();

	}

	@Override
	public void fillView() {
		album_content_gv.setAdapter(gridImageAdapter);
		album_content_gv.setEmptyView(album_none_tv);
		head_right_tv.setVisibility(View.VISIBLE);
		head_left_tv.setText("相册");
		head_right_tv.setText("取消");
		head_title_tv.setText("全部照片");
	}

	@Override
	public void setListener() {
		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						if (Bimp.onceSelectBitmap.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(
										AlbumActivity.this,
										getResources().getString(
												R.string.only_choose_num), Toast.LENGTH_SHORT)
										.show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.onceSelectBitmap.add(dataList.get(position));
							album_ok_btn.setText(getResources().getString(
									R.string.finish)
									+ "("
									+ Bimp.onceSelectBitmap.size()
									+ "/"
									+ PublicWay.num + ")");
						} else {
							Iterator<ImageItem> iterator = Bimp.onceSelectBitmap
									.iterator();
							while (iterator.hasNext()) {
								ImageItem imageItem = (ImageItem) iterator
										.next();
								if (imageItem.getImagePath().equals(
										dataList.get(position).getImagePath())) {
									iterator.remove();
									Bimp.onceSelectBitmap.remove(imageItem);
									Bimp.max--;
								}
							}

							// Bimp.tempSelectBitmap.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							album_ok_btn.setText(getResources().getString(
									R.string.finish)
									+ "("
									+ Bimp.onceSelectBitmap.size()
									+ "/"
									+ PublicWay.num + ")");
						}
						isShowOkBt();
					}
				});

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv:
			cancelSelect();
			break;
		case R.id.album_ok_btn:
			finishSelect();
			break;
		case R.id.album_preview_btn:
			preView();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		UIHelper.startActivity(mContext, ImageFileActivity.class);
		super.onBackPressed();
	}
	
	@Override
	public void goBack(View v) {
		UIHelper.startActivity(mContext, ImageFileActivity.class);
		mContext.finish();
	}

	private void preView() {
		Bundle bundle = new Bundle();
		if (Bimp.onceSelectBitmap.size() > 0) {
			bundle.putString("position", "1");
			UIHelper.startActivity(mContext, GalleryActivity.class, bundle);
		}
	}

	private void finishSelect() {
		Bimp.tempSelectBitmap.clear();
		Bimp.tempSelectBitmap.addAll(Bimp.onceSelectBitmap);
		Bimp.max = Bimp.tempSelectBitmap.size();
		Bimp.onceSelectBitmap.clear();
		overridePendingTransition(R.anim.activity_translate_in,
				R.anim.activity_translate_out);
//		UIHelper.startActivity(mContext, SendCardActivity.class);
		finish();
	}

	private void cancelSelect() {
		Bimp.onceSelectBitmap.clear();
		Bimp.max = Bimp.tempSelectBitmap.size();
//		UIHelper.startActivity(mContext, SendCardActivity.class);
		finish();
	}

	public void isShowOkBt() {
		if (Bimp.onceSelectBitmap.size() > 0) {
			album_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			album_preview_btn.setPressed(true);
			album_ok_btn.setPressed(true);
			album_preview_btn.setClickable(true);
			album_ok_btn.setClickable(true);
			album_ok_btn.setTextColor(Color.WHITE);
			album_preview_btn.setTextColor(Color.WHITE);
		} else {
			album_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			album_preview_btn.setPressed(false);
			album_preview_btn.setClickable(false);
			album_ok_btn.setPressed(false);
			album_ok_btn.setClickable(false);
			album_ok_btn.setTextColor(Color.parseColor("#E1E0DE"));
			album_preview_btn.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.onceSelectBitmap.contains(imageItem)) {
			Bimp.onceSelectBitmap.remove(imageItem);
			album_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			return true;
		}
		return false;
	}

}
