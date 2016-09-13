package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopItem;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.text.MessageFormat;
import java.util.List;

public class SelectStoreActivity extends BaseSearchActivity {
	OnItemViewClickListener listener;
	private List<ShopItem> mList_Shop;

	@Override
	protected void getData() {
		RequestParams params = TXApplication.getParams();
		String url = URLs.SEARCH_SHOP;
		params.addBodyParameter("name", SPUtils.get(mContext,"send_city","").toString());
		TXApplication.post(null, mContext, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						UIHelper.ToastMessage(mContext, R.string.net_work_error);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						MessageResult result = MessageResult
								.parse(responseInfo.result);
						if (result == null) {
							return;
						}
						switch (result.getSuccess()) {
						case 1:
							totalRecord = -1;
							mList_Shop = JSONArray.parseArray(result.getData(),
									ShopItem.class);
							break;

						default:
							UIHelper.ToastLogMessage(mContext,
									result.getMessage());
							break;
						}
						changeListView(0);
					}
				});
	}

	@Override
	public void initData() {
		adapter = new StoreAdapter();
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_select_store);
		search_result_lv = (XListView) findViewById(R.id.search_result_lv);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("选择门店");
		initListView();
		search_result_lv.setPullLoadEnable(false);
		search_result_lv.setBackgroundResource(R.color.app_bg);
		search_result_lv.setDividerHeight(0);
	}

	@Override
	public void setListener() {
		search_result_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data=new Intent();
				data.putExtra("select_store", mList_Shop.get(position-1));
				UIHelper.setResult(mContext, RESULT_OK, data);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	class StoreAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mList_Shop != null) {
				return mList_Shop.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mList_Shop != null) {
				return mList_Shop.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (mList_Shop != null) {
				return position;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.view_list_store_item, null);
				holder = new ViewHolder();
				holder.setPosition(position);
				holder.store_item_logo_iv = (ImageView) convertView
						.findViewById(R.id.store_item_logo_iv);
				holder.store_item_name_tv = (TextView) convertView
						.findViewById(R.id.store_item_name_tv);
				holder.store_item_distance_tv = (TextView) convertView
						.findViewById(R.id.store_item_distance_tv);
				holder.store_item_show_count_tv = (TextView) convertView
						.findViewById(R.id.store_item_show_count_tv);
				holder.store_item_borrow_count_tv = (TextView) convertView
						.findViewById(R.id.store_item_borrow_count_tv);
				holder.store_item_popularity_count_tv = (TextView) convertView
						.findViewById(R.id.store_item_popularity_count_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.setPosition(position);
			}
			listener = new OnItemViewClickListener(holder);
			bindData(holder);
			return convertView;
		}

		private void bindData(ViewHolder holder) {
			ShopItem shopItem = mList_Shop.get(holder.getPosition());
			holder.store_item_name_tv.setText(MessageFormat.format("{0}",
					shopItem.getShop_names()));
		}
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

			}
		}
	}

	static class ViewHolder {
		ImageView store_item_logo_iv;
		TextView store_item_name_tv;
		TextView store_item_distance_tv;
		TextView store_item_show_count_tv;
		TextView store_item_borrow_count_tv;
		TextView store_item_popularity_count_tv;
		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}
}
