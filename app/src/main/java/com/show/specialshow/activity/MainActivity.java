package com.show.specialshow.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.ChatAllHistoryFragment;
import com.easemob.chatuidemo.activity.MainHxActivity;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.util.HanziToPinyin;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.OnTabActivityResultListener;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity implements EMEventListener {
	private TabHost tabHost;// tabhost对象
	private LocalActivityManager localActivityManager;
	private int index = 0;//
	private long exitTime;
	private View[] views;
	private View menu_special_show_circle_rll;// 特秀圈
	private View menu_find_rll;// 发现
	private View menu_chat_rll;// 聊天
	private View menu_my_rll;// 我的
	private ImageView my_circle_red_small;//小红点
	// 未读消息textview
	private TextView unreadLabel;

	private MyConnectionListener connectionListener = null;
	private InviteMessgeDao inviteMessgeDao;

	private UserDao userDao;

	private ChatAllHistoryFragment chatHistoryFragment;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	/**
	 * 点击事件
	 */
	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.menu_special_show_circle_rll:
			index = 0;
			changeTab();
			break;
		case R.id.menu_find_rll:
			index = 1;
			changeTab();
			break;
		case R.id.menu_chat_rll:
			if (TXApplication.login) {
				if((Boolean)SPUtils.get(mContext,"ichange",true)){
					index = 2;
					changeTab();
				}else{
					UIHelper.ToastMessage(mContext,"请先完善资料");
					bundle.putInt("from_mode",1);
					UIHelper.startActivity(mContext,PerfectDataActivity.class,bundle);
				}

			} else {
				bundle.putInt(LoginActivity.FROM_LOGIN,
						LoginActivity.FROM_OTHER);
				UIHelper.startActivity(mContext, LoginActivity.class, bundle);
			}
			break;
		case R.id.menu_my_rll:
			if (TXApplication.login) {
				if((Boolean) SPUtils.get(mContext,"ichange",true)){
					index = 3;
					changeTab();
				}else{
					UIHelper.ToastMessage(mContext,"请先完善资料");
					bundle.putInt("from_mode",1);
					UIHelper.startActivity(mContext,PerfectDataActivity.class,bundle);
				}

			} else {
				bundle.putInt(LoginActivity.FROM_LOGIN,
						LoginActivity.FROM_OTHER);
				UIHelper.startActivity(mContext, LoginActivity.class, bundle);
			}
			break;
		case R.id.menu_add_rll:
//			if (TXApplication.login) {
				startActivity(new Intent(this, SelectSendTypeActivity.class));
//			} else {
//				bundle.putInt(LoginActivity.FROM_LOGIN,
//						LoginActivity.FROM_OTHER);
//				UIHelper.startActivity(mContext, LoginActivity.class, bundle);
//			}
			break;
		case R.id.contest_confirm_tv:

			break;
		case R.id.contest_cancel_tv:

			break;
		default:
			break;
		}
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param chatDialog
	 * @param username 
	 * 
	 * @param //button
	 * @param username
	 */
	private void acceptInvitation(final InviteMessage msg,
			final Dialog chatDialog, final String username) {
		final String str3 = mContext.getResources().getString(
				R.string.Agree_with_failure);
		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					chatDialog.dismiss();
					if (msg.getGroupId() == null){
						// 同意好友请求
						EMChatManager.getInstance().acceptInvitation(
								msg.getFrom());
//						acceptMyServer(username);
					}
				} catch (final Exception e) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, str3 + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			}
		}).start();
	}
	
	/**
	 * 连接自己的服务器
	 * @param username 
	 */
	private void acceptMyServer(String username){
		RequestParams params=TXApplication.getParams();
		params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
		params.addBodyParameter("fid", username);
		String url=URLs.SPACE_ADDFRIEND;
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					return;
				}
				if(1==result.getSuccess()){
				}else{
					UIHelper.ToastMessage(mContext, R.string.net_work_error);
				}
			}
		});
		
	}

	/**
	 * 界面的切换
	 * 
	 * @param //index
	 */
	private void changeTab() {
		tabHost.setCurrentTabByTag(index+"");
		for (int i = 0; i < views.length; i++) {
			views[i].setSelected(false);
		}
		views[index].setSelected(true);
	}

	@Override
	public void initData() {
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		menu_special_show_circle_rll = findViewById(R.id.menu_special_show_circle_rll);
		menu_find_rll = findViewById(R.id.menu_find_rll);
		menu_chat_rll = findViewById(R.id.menu_chat_rll);
		menu_my_rll = findViewById(R.id.menu_my_rll);
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		my_circle_red_small= (ImageView) findViewById(R.id.my_circle_red_small);
		views = new View[] { menu_special_show_circle_rll, menu_find_rll,
				menu_chat_rll, menu_my_rll };
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchResume();
		tabHost.setup(localActivityManager);
		Intent specialShowIntent = new Intent(MainActivity.this,
				SpecialShowCircleActivity.class);
		Intent findIntent = new Intent(MainActivity.this, FindActivity.class);
		Intent chatIntent = new Intent(MainActivity.this, MainHxActivity.class);
		Intent myIntent = new Intent(MainActivity.this, MyActivity.class);
		Intent[] intents = new Intent[] { specialShowIntent, findIntent,
				chatIntent, myIntent };
		for (int i = 0; i < intents.length; i++) {
			tabHost.addTab(tabHost.newTabSpec(i+"").setIndicator("")
					.setContent(intents[i]));
		}
		tabHost.setCurrentTab(index);
		views[index].setSelected(true);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		index = intent.getIntExtra("index", 0);
		changeTab();
		initView();
//		if (getIntent().getBooleanExtra("conflict", false)
//				&& !isConflictDialogShow) {
//			showConflictDialog();
//		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
//				&& !isAccountRemovedDialogShow) {
//			showAccountRemovedDialog();
//		}
	}

	@Override
	public void fillView() {
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		chatHistoryFragment = new ChatAllHistoryFragment();
		if(TXApplication.login){
			init();
			isMessage();
		}
	}

	@Override
	public void setListener() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取当前活动的Activity实例
		Activity subActivity = localActivityManager.getCurrentActivity();
		// 判断是否实现返回值接口
		if (subActivity instanceof OnTabActivityResultListener) {
			// 获取返回值接口实例
			OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void init() {
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener

		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	public void chatDialog(String content, int dialogStyle, boolean isCancel,
			final InviteMessage msg, final String username) {
		final Dialog chatDialog = new Dialog(mContext, R.style.Theme_dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_contest_dialog, null);
		View content_ll = view.findViewById(R.id.content_ll);
		TextView contest_cancel_tv = (TextView) view
				.findViewById(R.id.contest_cancel_tv);
		ImageView split_iv_vertical = (ImageView) view
				.findViewById(R.id.split_iv_vertical);
		TextView contest_confirm_tv = (TextView) view
				.findViewById(R.id.contest_confirm_tv);
		TextView confirm_dialog_content_tv = (TextView) view
				.findViewById(R.id.confirm_dialog_content_tv);
		switch (dialogStyle) {
		case DIALOG_AGREE_CANCEL:
			contest_cancel_tv.setText("拒绝");
			contest_confirm_tv.setText("同意");
			break;
		default:
			break;
		}
		confirm_dialog_content_tv.setText(content);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) (TXApplication.WINDOW_WIDTH * 0.7),
				LayoutParams.WRAP_CONTENT);
		content_ll.setLayoutParams(params);
		chatDialog.setContentView(view);
		chatDialog.setCancelable(isCancel);
		chatDialog.show();
		contest_cancel_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refuseInvitation(msg, chatDialog);
			}
		});
		contest_confirm_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				acceptInvitation(msg, chatDialog,username);
			}
		});
	}

	private void refuseInvitation(final InviteMessage msg, final Dialog chatDialog) {
		final String str3 = mContext.getResources().getString(
				R.string.Agree_with_failure);
		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					chatDialog.dismiss();
					if (msg.getGroupId() == null) // 同意好友请求
						EMChatManager.getInstance().refuseInvitation(
								msg.getFrom());
				} catch (final Exception e) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, str3 + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			}
		}).start();
	}

	private void getEmidData(final String username, final InviteMessage msg) {
		RequestParams params = TXApplication.getParams();
		String url = URLs.SPACE_GETUSERBYID;
		params.addBodyParameter("emid", username);
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
						if (null == result) {
							return;
						}
						if (1 == result.getSuccess()) {
							User user = User.parse(result.getData());
							if (user == null) {
								return;
							} else {
								chatDialog(user.getNickname() + "请求加你为好友",
										DIALOG_AGREE_CANCEL, true, msg,username);
							}
						} else {
							return;
						}
					}
				});
	}

	/***
	 * 好友变化listener
	 * 
	 */
	public class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					String st10 = getResources().getString(
							R.string.have_you_removed);
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast.makeText(
								mContext,
								ChatActivity.activityInstance
										.getToChatUsername() + st10, Toast.LENGTH_SHORT).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// 刷新ui
					// contactListFragment.refresh();
					chatHistoryFragment.refresh();
				}
			});

		}

		@Override
		public void onContactInvited(final String username, final String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			final InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d("nishi shabi", username + "请求加你为好友,reason: " + reason);
			runOnUiThread(new Runnable() {
				public void run() {
					getEmidData(username, msg);
				}
			});
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d("TAG", username + "同意了你的好友请求");
			acceptMyServer(username);
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(username, username + "拒绝了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEREFUSED);
//			notifyNewIviteMessage(msg);

		}

	}

	/**
	 * 连接监听listener
	 * 
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance()
					.isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance()
					.isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				// asyncFetchContactsFromServer();
				new Thread() {
					@Override
					public void run() {
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
				if (!groupSynced) {
					asyncFetchGroupsFromServer();
				}

				if (!contactSynced) {
					asyncFetchContactsFromServer();
				}

				if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
					asyncFetchBlackListFromServer();
				}
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// chatHistoryFragment.errorItem.setVisibility(View.GONE);
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(
					R.string.can_not_connect_chat_server_connection);
			final String st2 = getResources().getString(
					R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						// showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						// showConflictDialog();
					} else {
						// chatHistoryFragment.errorItem
						// .setVisibility(View.VISIBLE);
						// if (NetUtils.hasNetwork(mContext))
						// chatHistoryFragment.errorText.setText(st1);
						// else
						// chatHistoryFragment.errorText.setText(st2);
					}
				}

			});
		}
	}

	static void asyncFetchGroupsFromServer() {
		HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

			@Override
			public void onSuccess() {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

				if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}

			@Override
			public void onError(int code, String message) {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

		});
	}

	static void asyncFetchContactsFromServer() {
		HXSDKHelper.getInstance().asyncFetchContactsFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> usernames) {
						final Context context = HXSDKHelper.getInstance()
								.getAppContext();
						final Map<String, User> userlist = new HashMap<String, User>();
						for (String username : usernames) {
							User user = new User();
							user.setUsername(username);
							setUserHearder(username, user);
							userlist.put(username, user);
						}
						// 存入内存
						((DemoHXSDKHelper) HXSDKHelper.getInstance())
								.setContactList(userlist);
						// 存入db
						UserDao dao = new UserDao(context);
						List<User> users = new ArrayList<User>(userlist
								.values());
						dao.saveContactList(users);

						HXSDKHelper.getInstance().notifyContactsSyncListener(
								true);

						if (HXSDKHelper.getInstance()
								.isGroupsSyncedWithServer()) {
							HXSDKHelper.getInstance().notifyForRecevingEvents();
						}

						((DemoHXSDKHelper) HXSDKHelper.getInstance())
								.getUserProfileManager()
								.asyncFetchContactInfosFromServer(usernames,
										new EMValueCallBack<List<User>>() {

											@Override
											public void onSuccess(
													List<User> uList) {
												((DemoHXSDKHelper) HXSDKHelper
														.getInstance())
														.updateContactList(uList);
												((DemoHXSDKHelper) HXSDKHelper
														.getInstance())
														.getUserProfileManager()
														.notifyContactInfosSyncListener(
																true);
											}

											@Override
											public void onError(int error,
													String errorMsg) {
											}
										});
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyContactsSyncListener(
								false);
					}

				});
	}

	static void asyncFetchBlackListFromServer() {
		HXSDKHelper.getInstance().asyncFetchBlackListFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> value) {
						EMContactManager.getInstance().saveBlackList(value);
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								true);
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								false);
					}

				});
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	private static void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{
			EMMessage message = (EMMessage) event.getData();

			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			refreshUI();
			break;
		}

		case EventOfflineMessage: {
			refreshUI();
			break;
		}

		case EventConversationListChanged: {
			refreshUI();
			break;
		}

		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				updateUnreadLabel();
				if (index == 2) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(
					connectionListener);
		}
		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					// unreadAddressLable.setText(String.valueOf(count));
					// unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					// unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(
				Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		// if (currentTabIndex == 1)
		// contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}
	/**
	 * 判断是否有未读信息通知
	 */
	private void isMessage(){
		RequestParams params=TXApplication.getParams();
		String url= URLs.USER_ISMESSAGE;
		params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result = MessageResult.parse(responseInfo.result);
				if(null==result){
					return;
				}
				if(1==result.getSuccess()){
					try {
						JSONObject obj = new JSONObject(result.getData());
						int count = obj.getInt("count");
						if(count>0){
							my_circle_red_small.setVisibility(View.VISIBLE);
						}else{
							my_circle_red_small.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					UIHelper.ToastMessage(mContext,R.string.net_work_error);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext,R.string.net_work_error);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		localActivityManager.dispatchResume();
		if (TXApplication.login) {
			isMessage();
			if (!isConflict && !isCurrentAccountRemoved) {
				updateUnreadLabel();
				updateUnreadAddressLable();
				EMChatManager.getInstance().activityResumed();
			}
		}
		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN&&
				(System.currentTimeMillis() - exitTime) > 2000
				){
				UIHelper.ToastMessage(this, R.string.toast_dblclick_back_pressed);
				exitTime = System.currentTimeMillis();
			return  true;
		}
		return super.dispatchKeyEvent(event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!mContext.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							mContext);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
//								finish();
								if(CommonUtils.isLogin(mContext)){
									TXApplication.getInstance().umengLogout();
								}
								TXApplication.quitLogin();
								startActivity(new Intent(mContext,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				// EMLog.e(TAG,
				// "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}
	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(true, null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!mContext.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							mContext);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
//								finish();
								if(CommonUtils.isLogin(mContext)){
									TXApplication.getInstance().umengLogout();
								}
								TXApplication.quitLogin();
								startActivity(new Intent(mContext,
										LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				// EMLog.e(TAG,
				// "---------color userRemovedBuilder error"
				// + e.getMessage());
			}

		}

	}
}
