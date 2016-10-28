package com.show.specialshow.adapter;

import java.util.List;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.AddFriendMess;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendAdapter extends BaseAdapter {
	private List<AddFriendMess> mList;
	private Context mContext;
	private ProgressDialog progressDialog;

	public AddFriendAdapter(List<AddFriendMess> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return null == mList ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null == mList ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return null == mList ? 0 : position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (null == convertView) {
			vh = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.search_add_friend_item, null);
			vh.search_add_friend_item_riv = (RoundImageView) convertView
					.findViewById(R.id.search_add_friend_item_riv);
			vh.search_add_friend_item_nickname = (TextView) convertView
					.findViewById(R.id.search_add_friend_item_nickname);
			vh.search_add_friend_add = (TextView) convertView
					.findViewById(R.id.search_add_friend_add);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(mList.get(position).getIcon(),
				vh.search_add_friend_item_riv);
		vh.search_add_friend_item_nickname.setText(mList.get(position)
				.getNickname());
		vh.search_add_friend_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addContact(mList.get(position).getEmid());
			}
		});
		return convertView;
	}

	/**
	 * 添加contact
	 * 
	 * @param ／／i
	 * 
	 */
	public void addContact(final int emid) {
		if (TXApplication.getInstance().getUserName().equals(emid + "")) {
			String str = mContext.getString(R.string.not_add_myself);
			mContext.startActivity(new Intent(mContext, AlertDialog.class)
					.putExtra("msg", str));
			return;
		}

		if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList()
				.containsKey(emid + "")) {
			// 提示已在好友列表中，无需添加
			if (EMContactManager.getInstance().getBlackListUsernames()
					.contains(emid + "")) {
				mContext.startActivity(new Intent(mContext, AlertDialog.class)
						.putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
				return;
			}
			String strin = mContext
					.getString(R.string.This_user_is_already_your_friend);
			mContext.startActivity(new Intent(mContext, AlertDialog.class)
					.putExtra("msg", strin));
			return;
		}

		progressDialog = new ProgressDialog(mContext);
		String stri = mContext.getResources().getString(
				R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread(new Runnable() {
			public void run() {

				try {
					// demo写死了个reason，实际应该让用户手动填入
					String s = mContext.getResources().getString(
							R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(emid + "", s);
					((Activity) mContext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = mContext.getResources().getString(
									R.string.send_successful);
							UIHelper.ToastMessage(mContext, s1);
						}
					});
				} catch (final Exception e) {
					((Activity) mContext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = mContext.getResources().getString(
									R.string.Request_add_buddy_failure);
							UIHelper.ToastMessage(mContext, s2);
						}
					});
				}
			}
		}).start();
	}

	class ViewHolder {
		RoundImageView search_add_friend_item_riv;// 头像
		TextView search_add_friend_item_nickname;// 昵称
		TextView search_add_friend_add;// 添加好友
	}

}
