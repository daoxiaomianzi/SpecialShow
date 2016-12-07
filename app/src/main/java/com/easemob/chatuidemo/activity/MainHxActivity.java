/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
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
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainHxActivity extends BaseActivity implements EMEventListener {

    protected static final String TAG = "MainActivity";
    // 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLable;

    private Button[] mTabs;
    private ContactlistFragment contactListFragment;
    // private ChatHistoryFragment chatHistoryFragment;
    private ChatAllHistoryFragment chatHistoryFragment;
    private SettingsFragment settingFragment;
    private Fragment[] fragments;
    private int index;
    // 当前fragment的index
    private int currentTabIndex = 0;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private MyConnectionListener connectionListener = null;
    protected TextView head_left_tv;
    protected TextView head_title_tv;
    protected TextView head_right_tv;
    //    // 添加pop相关
//    private TitlePopup titlePopup;
    public static final String INDEX = "currentTabIndex";

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTabIndex = getIntent().getIntExtra(INDEX, 0);
//		if (savedInstanceState != null
//				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
//						false)) {
//			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
//			// 三个fragment里加的判断同理
//			DemoHXSDKHelper.getInstance().logout(true, null);
//			finish();
//			startActivity(new Intent(this, MainActivity.class));
//			return;
//		} else if (savedInstanceState != null
//				&& savedInstanceState.getBoolean("isConflict", false)) {
//			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
//			// 三个fragment里加的判断同理
//			finish();
//			startActivity(new Intent(this, MainActivity.class));
//			return;
//		}
        setContentView(R.layout.activity_hx_main);
//        inint();
        initView();
        // MobclickAgent.setDebugMode( true );
        // --?--
        // MobclickAgent.updateOnlineConfig(this);
//
//		if (getIntent().getBooleanExtra("conflict", false)
//				&& !isConflictDialogShow) {
//			showConflictDialog();
//		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
//				&& !isAccountRemovedDialogShow) {
//			showAccountRemovedDialog();
//		}

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        // 这个fragment只显示好友和群组的聊天记录
        // chatHistoryFragment = new ChatHistoryFragment();
        // 显示所有人消息记录的fragment
        chatHistoryFragment = new ChatAllHistoryFragment();
        contactListFragment = new ContactlistFragment();
        settingFragment = new SettingsFragment();
        fragments = new Fragment[]{chatHistoryFragment, contactListFragment,
                settingFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, chatHistoryFragment)
                .add(R.id.fragment_container, contactListFragment)
                .hide(contactListFragment).show(fragments[currentTabIndex])
                .commit();

        init();
        // 异步获取当前用户的昵称和头像
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager()
                .asyncGetCurrentUserInfo();
    }

    private void init() {
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(
                new MyContactListener());
        // 注册一个监听连接状态的listener

        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);
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
     * 初始化组件
     */
    @SuppressLint("InlinedApi")
    private void initView() {
        View head = findViewById(R.id.head_rl);
        head.setFitsSystemWindows(false);
        head_left_tv = (TextView) findViewById(R.id.head_left_tv);
        head_title_tv = (TextView) findViewById(R.id.head_title_tv);
        head_right_tv = (TextView) findViewById(R.id.head_right_tv);
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(this, head);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        switch (currentTabIndex) {
            case 0:
                head.setVisibility(View.GONE);
//                head_left_tv.setVisibility(View.GONE);
//                head_right_tv.setVisibility(View.VISIBLE);
//                head_title_tv.setText("聊天");
//                UIHelper.leftDrawable(R.drawable.icon_add_craftsman
//                        , MainHxActivity.this, head_right_tv);
                break;
            case 1:
                head_title_tv.setText("我的好友");
                break;

            default:
                break;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.head_right_tv:
//                titlePopup.show(findViewById(R.id.head_right_tv));
//                break;

            default:
                break;
        }
    }

//    private void inint() {
//        // 实例化标题栏弹窗
//        titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        titlePopup.setItemOnClickListener(onitemClick);
//        // 给标题栏弹窗添加子类
//        titlePopup.addAction(new ActionItem(this, R.string.addfriend,
//                R.drawable.icon_menu_addfriend));
//        titlePopup.addAction(new ActionItem(this, R.string.myfriend,
//                R.drawable.icon_menu_myfriend));
//        titlePopup.addAction(new ActionItem(this, R.string.invite_friend,
//                R.drawable.icon_invite_friends));
//    }

//    private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {
//
//        @Override
//        public void onItemClick(ActionItem item, int position) {
//            Bundle bundle = new Bundle();
//            // mLoadingDialog.show();
//            switch (position) {
//                case 0:// 添加好友
//                    UIHelper.startActivity(MainHxActivity.this,
//                            AddFriendActivity.class);
//                    break;
//                case 1:// 我的好友
//                    bundle.putInt(INDEX, 1);
//                    UIHelper.startActivity(MainHxActivity.this,
//                            LoginActivity.class, bundle);
//                    break;
//                case 2://邀请好友
//                    inviteFriends();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 邀请回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareServiceFactory.getShareService().onActivityResult(MainHxActivity.this, requestCode, resultCode, data);
    }

//    /**
//     * 邀请好友
//     */
//    private void inviteFriends() {
//        ShareContent shareItem = new ShareContent();
//        shareItem.mText = "特秀美妆:美不曾离开，让你的美由此开始";
//        shareItem.mTargetUrl = "http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid=" + TXApplication.getUserMess().getUid();
//        shareItem.mTitle = "特秀美妆:美不曾离开，让你的美由此开始";
////                ShareSDKManager.getInstance().getCurrentSDK().share((Activity) mContext, shareItem);
//        ShareServiceFactory.getShareService().share(this, shareItem);
//    }

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
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (chatHistoryFragment != null) {
                        chatHistoryFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    public void goBack(View view) {
        finish();
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
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            // unreadLabel.setText(String.valueOf(count));
            // unreadLabel.setVisibility(View.VISIBLE);
        } else {
            // unreadLabel.setVisibility(View.INVISIBLE);
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

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private void refuseInvitation(final InviteMessage msg,
                                  final Dialog chatDialog) {
        final String str3 = getResources().getString(
                R.string.Agree_with_failure);
        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的拒绝方法
                try {
                    chatDialog.dismiss();
                    if (msg.getGroupId() == null) // 拒绝好友请求
                        EMChatManager.getInstance().refuseInvitation(
                                msg.getFrom());
                } catch (final Exception e) {
                    ((Activity) MainHxActivity.this)
                            .runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainHxActivity.this,
                                            str3 + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }).start();
    }

    /**
     * 同意好友请求或者群申请
     *
     * @param chatDialog
     * @param username
     * @param //button
     * @param username
     */
    private void acceptInvitation(final InviteMessage msg,
                                  final Dialog chatDialog, final String username) {
        final String str3 = getResources().getString(
                R.string.Agree_with_failure);
        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {
                    chatDialog.dismiss();
                    if (msg.getGroupId() == null) {
                        // 同意好友请求
                        EMChatManager.getInstance().acceptInvitation(
                                msg.getFrom());
                        // acceptMyServer(username);
                    }
                } catch (final Exception e) {
                    ((Activity) MainHxActivity.this)
                            .runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(MainHxActivity.this,
                                            str3 + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }).start();
    }

    /**
     * 连接自己的服务器
     *
     * @param username
     */
    private void acceptMyServer(String username) {
        RequestParams params = TXApplication.getParams();
        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
        params.addBodyParameter("fid", username);
        String url = URLs.SPACE_ADDFRIEND;
        TXApplication.post(null, MainHxActivity.this, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(MainHxActivity.this,
                                R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (null == result) {
                            return;
                        }
                        if (1 == result.getSuccess()) {
                        } else {
                            UIHelper.ToastMessage(MainHxActivity.this,
                                    R.string.net_work_error);
                        }
                    }
                });

    }

    private void chatDialog(String content, int dialogStyle, boolean isCancel,
                            final InviteMessage msg, final String username) {
        final Dialog chatDialog = new Dialog(MainHxActivity.this.getParent(),
                R.style.Theme_dialog);
        LayoutInflater inflater = LayoutInflater.from(MainHxActivity.this.getParent());
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
            case 10:
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
                acceptInvitation(msg, chatDialog, username);
            }
        });
    }

    private void getEmidData(final String username, final InviteMessage msg) {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_GETUSERBYID;
        params.addBodyParameter("emid", username);
        TXApplication.post(null, this, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(MainHxActivity.this,
                                R.string.net_work_error);
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
                                chatDialog(user.getNickname() + "请求加你为好友", 10,
                                        true, msg, username);
                            }
                        } else {
                            return;
                        }
                    }
                });
    }

    /***
     * 好友变化listener
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
            if (currentTabIndex == 1)
                contactListFragment.refresh();

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
                                MainHxActivity.this,
                                ChatActivity.activityInstance
                                        .getToChatUsername() + st10, 1).show();
                        ChatActivity.activityInstance.finish();
                    }
                    updateUnreadLabel();
                    // 刷新ui
                    contactListFragment.refresh();
                    chatHistoryFragment.refresh();
                }
            });

        }

        @Override
        public void onContactInvited(final String username, String reason) {

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
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
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
            Log.d(TAG, username + "同意了你的好友请求");
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
        }

    }

    /**
     * 连接监听listener
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
                    chatHistoryFragment.errorItem.setVisibility(View.GONE);
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
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {
                        chatHistoryFragment.errorItem
                                .setVisibility(View.VISIBLE);
                        if (NetUtils.hasNetwork(MainHxActivity.this))
                            chatHistoryFragment.errorText.setText(st1);
                        else
                            chatHistoryFragment.errorText.setText(st2);

                    }
                }

            });
        }
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
        if (currentTabIndex == 1)
            contactListFragment.refresh();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
                .getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[]{
                        EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventConversationListChanged});
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(true);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        if (!MainHxActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            MainHxActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                startActivity(new Intent(MainHxActivity.this,
                                        com.show.specialshow.activity.LoginActivity.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG,
                        "---------color conflictBuilder error" + e.getMessage());
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
        if (!MainHxActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            MainHxActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                accountRemovedBuilder = null;
                                finish();
                                startActivity(new Intent(MainHxActivity.this,
                                        com.show.specialshow.activity.LoginActivity.class));
                            }
                        });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG,
                        "---------color userRemovedBuilder error"
                                + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//		if (getIntent().getBooleanExtra("conflict", false)
//				&& !isConflictDialogShow) {
//			showConflictDialog();
//		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
//				&& !isAccountRemovedDialogShow) {
//			showAccountRemovedDialog();
//		}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // getMenuInflater().inflate(R.menu.context_tab_contact, menu);
    }

}
