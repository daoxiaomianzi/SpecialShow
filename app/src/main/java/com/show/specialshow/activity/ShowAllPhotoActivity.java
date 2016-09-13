package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.AlbumGridViewAdapter;
import com.show.specialshow.adapter.AlbumGridViewAdapter.OnItemClickListener;
import com.show.specialshow.model.Bimp;
import com.show.specialshow.model.ImageItem;
import com.show.specialshow.utils.PublicWay;
import com.show.specialshow.utils.UIHelper;

public class ShowAllPhotoActivity extends BaseActivity {
	private GridView show_all_photo_gv;
	private ProgressBar show_all_photo_progressbar;
	private Button show_all_ok_btn;
	private Button show_all_preview_btn;
	private AlbumGridViewAdapter mAdapter;

	public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mAdapter.notifyDataSetChanged();
			isShowOkBt();
		}

	};

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	};

	@Override
	public void initData() {
		mAdapter = new AlbumGridViewAdapter(mContext, dataList,
				Bimp.onceSelectBitmap);
		IntentFilter filter = new IntentFilter("data.broadcast.action");  
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_show_all_photo);
		show_all_photo_gv = (GridView) findViewById(R.id.show_all_photo_gv);
		show_all_photo_progressbar = (ProgressBar) findViewById(R.id.show_all_photo_progressbar);
		show_all_ok_btn = (Button) findViewById(R.id.show_all_ok_btn);
		show_all_preview_btn = (Button) findViewById(R.id.show_all_preview_btn);
		show_all_photo_progressbar.setVisibility(View.GONE);
		isShowOkBt();
	}

	@Override
	public void fillView() {
		String folderName = getIntent().getStringExtra("folderName");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		head_title_tv.setText(folderName);
		head_left_tv.setText("相册");
		show_all_photo_gv.setAdapter(mAdapter);
	}

	@Override
	public void setListener() {
		mAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final ToggleButton toggleButton,
					int position, boolean isChecked, Button chooseBt) {
				if (Bimp.onceSelectBitmap.size() >= PublicWay.num && isChecked) {
					toggleButton.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					Toast.makeText(mContext,
							getResources().getString(R.string.only_choose_num),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					Bimp.onceSelectBitmap.add(dataList.get(position));
					show_all_ok_btn.setText(getResources().getString(
							R.string.finish)
							+ "("
							+ Bimp.onceSelectBitmap.size()
							+ "/"
							+ PublicWay.num + ")");
				} else {
					Iterator<ImageItem> iterator = Bimp.onceSelectBitmap
							.iterator();
					while (iterator.hasNext()) {
						ImageItem imageItem = (ImageItem) iterator.next();
						if (imageItem.getImagePath().equals(
								dataList.get(position).getImagePath())) {
							iterator.remove();
							Bimp.onceSelectBitmap.remove(imageItem);

						}
					}
					// Bimp.tempSelectBitmap.remove(dataList.get(position));
					chooseBt.setVisibility(View.GONE);
					show_all_ok_btn.setText(getResources().getString(
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_all_ok_btn:
			finishSelect();
			break;
		case R.id.show_all_preview_btn:
			preView();
			break;
		default:
			break;
		}
	}

	private void finishSelect() {
		Bimp.tempSelectBitmap.clear();
		Bimp.tempSelectBitmap.addAll(Bimp.onceSelectBitmap);
		Bimp.max = Bimp.tempSelectBitmap.size();
		overridePendingTransition(R.anim.activity_translate_in,
				R.anim.activity_translate_out);
		UIHelper.startActivity(mContext, SendCardActivity.class);
		finish();
	}

	private void preView() {
		Bundle bundle = new Bundle();
		if (Bimp.onceSelectBitmap.size() > 0) {
			bundle.putString("position", "2");
			UIHelper.startActivity(mContext, GalleryActivity.class, bundle);
		}
	}

	public void isShowOkBt() {
		if (Bimp.onceSelectBitmap.size() > 0) {
			show_all_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			show_all_preview_btn.setPressed(true);
			show_all_ok_btn.setPressed(true);
			show_all_preview_btn.setClickable(true);
			show_all_ok_btn.setClickable(true);
			show_all_ok_btn.setTextColor(Color.WHITE);
			show_all_preview_btn.setTextColor(Color.WHITE);
		} else {
			show_all_ok_btn.setText(getResources().getString(R.string.finish)
					+ "(" + Bimp.onceSelectBitmap.size() + "/" + PublicWay.num
					+ ")");
			show_all_preview_btn.setPressed(false);
			show_all_preview_btn.setClickable(false);
			show_all_ok_btn.setPressed(false);
			show_all_ok_btn.setClickable(false);
			show_all_ok_btn.setTextColor(Color.parseColor("#E1E0DE"));
			show_all_preview_btn.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}
}
