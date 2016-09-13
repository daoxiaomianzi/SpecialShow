package com.show.specialshow.activity;

import android.view.View;
import android.widget.GridView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.FolderAdapter;
import com.show.specialshow.model.Bimp;

public class ImageFileActivity extends BaseActivity {
	private GridView image_file_content_gv;
	private FolderAdapter mAdapter;

	@Override
	public void initData() {
		mAdapter = new FolderAdapter(mContext);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_image_file);
		image_file_content_gv = (GridView) findViewById(R.id.image_file_content_gv);
	}

	@Override
	public void fillView() {
		image_file_content_gv.setAdapter(mAdapter);
		head_title_tv.setText("选择相册");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		Bimp.onceSelectBitmap.clear();
		super.onDestroy();
	}
}
