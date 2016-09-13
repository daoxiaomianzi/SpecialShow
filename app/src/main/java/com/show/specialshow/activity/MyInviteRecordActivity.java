package com.show.specialshow.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.InviteRecord;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

public class MyInviteRecordActivity extends BaseActivity {
	private ListView invite_record_list_lv;
	private List<InviteRecord> mList;
	private InviteRecordAdapter mAdapter;
	private TextView invite_no_record_show_tv;

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_my_invite_record);
		invite_record_list_lv = (ListView) findViewById(R.id.invite_record_list_lv);
		invite_no_record_show_tv = (TextView) findViewById(R.id.invite_no_record_show_tv);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("邀请记录");
		getData();
	}

	@Override
	public void setListener() {

	}

	@Override
	public void onClick(View v) {

	}

	private void getData() {
		RequestParams params = TXApplication.getParams();
		String url = URLs.USER_INVITELIST;
		params.addBodyParameter("uid", SPUtils.get(mContext, "uid", "")
				.toString());
		TXApplication.post(null, mContext, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						loadIng("加载中...", false);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						UIHelper.ToastMessage(mContext, R.string.net_work_error);
						dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						dialog.dismiss();
						MessageResult result = MessageResult
								.parse(responseInfo.result);
						if (result == null) {
							return;
						}
						if (result.getSuccess() == 1) {
							String info = result.getData();
							try {
								mList = JSON.parseArray(info,
										InviteRecord.class);
								if (mList != null) {
									mAdapter = new InviteRecordAdapter();
									invite_record_list_lv.setAdapter(mAdapter);
									invite_no_record_show_tv
											.setVisibility(View.GONE);
								} else {
									invite_no_record_show_tv
											.setVisibility(View.VISIBLE);
								}
							} catch (Exception e) {
							}

						}
					}
				});
	}

	class InviteRecordAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mList != null) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mList != null) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (mList != null) {
				return position;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.view_invite_record_item, null);
				holder = new ViewHolder();
				holder.setPosition(position);
				holder.invite_user_name = (TextView) convertView
						.findViewById(R.id.invite_user_name);
				holder.invite_investor_capital = (TextView) convertView
						.findViewById(R.id.invite_investor_capital);
				holder.invite_add_time = (TextView) convertView
						.findViewById(R.id.invite_add_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.setPosition(position);
			}
			bindData(holder);
			return convertView;
		}

		private void bindData(ViewHolder holder) {
			InviteRecord record = mList.get(holder.getPosition());
			String user_name = record.getNickname();
//			if (user_name != null && user_name.length() > 7) {
//				user_name = user_name.replaceAll(user_name.substring(3, 7),
//						"****");
//			} else {
//				user_name = "";
//			}
			holder.invite_user_name.setText(user_name);
			holder.invite_investor_capital
					.setText(record.getScore());
			holder.invite_add_time.setText(record.getCreate_time());
		}
	}

	static class ViewHolder {
		TextView invite_user_name, invite_investor_capital, invite_add_time;
		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}

}
