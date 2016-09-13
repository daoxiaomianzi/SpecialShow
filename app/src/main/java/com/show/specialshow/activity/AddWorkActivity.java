package com.show.specialshow.activity;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.adapter.AlbumGridViewAdapter;
import com.show.specialshow.utils.UIHelper;

public class AddWorkActivity extends BaseActivity {
	// 显示手机里的所有图片的列表控件
	private GridView addwork_content_gv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView addwork_none_tv;
	// 预览按钮
	private Button addwork_preview_btn;
	@Override
	public void initData() {
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_add_work);
		addwork_content_gv=(GridView) findViewById(R.id.addwork_content_gv);
		addwork_none_tv=(TextView) findViewById(R.id.addwork_none_tv);
		addwork_preview_btn=(Button) findViewById(R.id.addwork_preview_btn);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("相机相册");
		head_right_tv.setVisibility(View.VISIBLE);
		head_left_tv.setText("取消");
		head_right_tv.setText("下一步");
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_tv:
			
			break;
		case R.id.head_right_tv:
			UIHelper.startActivity(mContext, UploadWorkActivity.class);
			break;
		case R.id.addwork_preview_btn:
			
			break;

		default:
			break;
		}
	}



}
