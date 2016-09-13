package com.show.specialshow.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.easemob.chatuidemo.activity.ChatAllHistoryFragment;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.ActionItem;
import com.show.specialshow.view.TitlePopup;
import com.show.specialshow.view.TitlePopup.OnItemOnClickListener;

public class ChatRecordActivity extends BaseActivity {
	// 环信相关
	private ChatAllHistoryFragment chatAllHistoryFragment;
	// 相关控件
	private RelativeLayout fragment_container;
	// 添加pop相关
	private TitlePopup titlePopup;


	@Override
	public void initData() {
		setContentView(R.layout.activity_chat_record);
		inint();
	}

	@Override
	public void initView() {
		fragment_container = (RelativeLayout) findViewById(R.id.fragment_container);
	}

	@Override
	public void fillView() {
		head_left_tv.setVisibility(View.GONE);
		head_right_tv.setVisibility(View.VISIBLE);
		head_right_tv.setText("好友");
		// head_right_tv.setLayoutParams(new
		// LayoutParams(DensityUtil.dip2px(mContext, 30),
		// DensityUtil.dip2px(mContext, 30)));
		// head_right_tv.setBackgroundResource(R.drawable.actionbar_add_icon);
		// Drawable rightDrawable = getResources()
		// .getDrawable(R.drawable.actionbar_add_icon);
		// rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
		// rightDrawable.getMinimumHeight());
		// head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
		// head_right_tv.setPadding(DensityUtil.dip2px(mContext, 20), 0,
		// DensityUtil.dip2px(mContext, 17), 0);
		chatAllHistoryFragment=new ChatAllHistoryFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, chatAllHistoryFragment)
				.show(chatAllHistoryFragment).commit();
	}

	@Override
	public void setListener() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv:
			titlePopup.show(findViewById(R.id.head_right_tv));
			break;

		default:
			break;
		}
	}

	private void inint() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.addfriend,
				R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.myfriend,
				R.drawable.icon_menu_myfriend));
		titlePopup.addAction(new ActionItem(this, R.string.invite_friend,
				R.drawable.icon_invite_friends));
	}

	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 添加好友
				UIHelper.startActivity(mContext, AddFriendActivity.class);
				break;
			case 1:// 我的好友
				UIHelper.startActivity(mContext, LoginActivity.class);
				break;
			default:
				break;
			}
		}
	};


}
