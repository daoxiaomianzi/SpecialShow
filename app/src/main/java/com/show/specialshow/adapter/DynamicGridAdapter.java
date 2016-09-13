package com.show.specialshow.adapter;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.activity.DynamicImagePagerActivity;
import com.show.specialshow.model.DynamicImage;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.utils.XUtilsImageLoader;

public class DynamicGridAdapter extends BaseAdapter {

	OnItemViewClickListener listener;
	private List<DynamicImage> mList;
	private Context mContext;

	public DynamicGridAdapter(Context context, List<DynamicImage> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.gridview_item, null);
			holder.item_image = (ImageView) convertView
					.findViewById(R.id.dynamic_gv_item_iv);
			holder.setPosition(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.setPosition(position);
		}
		XUtilsImageLoader imageLoader = new XUtilsImageLoader(mContext);
		DynamicImage item = (DynamicImage) getItem(position);
		holder.item_image.getLayoutParams().width = (TXApplication.WINDOW_WIDTH - 32 - 2) / 3;
		holder.item_image.getLayoutParams().height = (TXApplication.WINDOW_WIDTH - 32 - 2) / 3;
		imageLoader.display(holder.item_image, item.getThumbnail_pic());
		// ImageLoader.getInstance().displayImage(item.getThumbnail_pic(),
		// holder.item_image);
		listener = new OnItemViewClickListener(holder);
		holder.item_image.setOnClickListener(listener);
		return convertView;
	}

	class OnItemViewClickListener implements OnClickListener {
		ViewHolder holder;

		public OnItemViewClickListener(ViewHolder holder) {
			super();
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			switch (v.getId()) {
			case R.id.dynamic_gv_item_iv:
				bundle.putSerializable(DynamicImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) mList);
				bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX,
						holder.getPosition());
				UIHelper.startActivity((Activity) mContext,
						DynamicImagePagerActivity.class, bundle);
				break;
			default:
				break;
			}
		}

	}

	private static class ViewHolder {
		private int position;
		ImageView item_image;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}

}
