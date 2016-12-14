package com.show.specialshow.utils;

import android.content.Context;
import android.os.Handler;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushUtils {
	public Context context;
	public static final int MSG_SET_ALIAS = 1001;
    public JpushUtils(Context context) {
		super();
		this.context = context;
	}
	public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                JPushInterface.setAlias(context,(String) msg.obj, mAliasCallback);
                break;
                
            default:
            }
        }
    };
	public final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
            case 0:
                break;
            case 6002:
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                break;
            default:
            	break;
            }
        }
	    
	};
}
