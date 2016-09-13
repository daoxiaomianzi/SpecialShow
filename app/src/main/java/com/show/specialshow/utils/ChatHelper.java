package com.show.specialshow.utils;

import android.content.Context;

public class ChatHelper {
	private static ChatHelper instance = null;

	private Context appContext;

	private ChatHelper() {

	}

	public synchronized static ChatHelper getInstance() {
		if (instance == null) {
			instance = new ChatHelper();
		}
		return instance;
	}

/*	public void init(Context context) {
		EMOptions options = initChatOptions();
		appContext=context;
		EMClient.getInstance().init(context, options);
		//设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
		EMClient.getInstance().setDebugMode(true);

	}

	public EMOptions initChatOptions() {
		// 获取到EMChatOptions对象
		EMOptions options = new EMOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置是否需要已读回执
		options.setRequireAck(true);
		// 设置是否需要已送达回执
		options.setRequireDeliveryAck(false);
		// 设置从db初始化加载时, 每个conversation需要加载msg的个数
		options.setNumberOfMessagesLoaded(1);
		return options;
	}*/

}
