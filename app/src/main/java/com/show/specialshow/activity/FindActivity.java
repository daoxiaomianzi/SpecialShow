package com.show.specialshow.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.fragment.TeShowActivitiesFragment;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.Log;
import com.umeng.community.MainCommunityActivity;

public class FindActivity extends BaseActivity {

    @Override
    public void initData() {
        setContentView(R.layout.activity_find);
        View head = findViewById(R.id.head_rl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {
        head_left_tv.setVisibility(View.GONE);
        head_title_tv.setText("发现");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
//            case R.id.rll_texiu_community://特秀社区
//                if (TXApplication.login) {
//                    if(CommonUtils.isLogin(mContext)){
//                        UIHelper.startActivity(mContext, MainCommunityActivity.class);
//                    }else{
//                        //创建CommUser前必须先初始化CommunitySDK
//                        CommunitySDK sdk = CommunityFactory.getCommSDK(this);
//                        CommUser user = new CommUser();
//                        UserMessage userMessage = TXApplication.getUserMess();
//                        user.name = userMessage.getNickname().toString();
//                        user.id = userMessage.getUid().toString();
//                        sdk.loginToUmengServerBySelfAccount(this, user, new LoginListener() {
//                            @Override
//                            public void onStart() {
//                            }
//                            @Override
//                            public void onComplete(int stCode, CommUser commUser) {
//                                Log.d("tag", "login result is"+stCode);          //获取登录结果状态码
//                                if (ErrorCode.NO_ERROR == stCode) {
//                                    //登录成功，可以打开社区，也可以进行其他的操作，开发者自己定义
//                                    UIHelper.startActivity(mContext, MainCommunityActivity.class);
//                                }else{
//                                    UIHelper.ToastLogMessage(mContext,"出了点小问题，请稍后再试哦|");
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
//                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
//                }
//                break;
            case R.id.rll_texiu_activity://特秀活动
                UIHelper.startActivity(mContext, TeShowActivitiesActivity.class);
//                if (TXApplication.login) {
//                    if(CommonUtils.isLogin(mContext)){
//                        UIHelper.startActivity(mContext,TexiuActivitiesActivity.class);
//                    }else{
//                        //创建CommUser前必须先初始化CommunitySDK
//                        CommunitySDK sdk = CommunityFactory.getCommSDK(this);
//                        CommUser user = new CommUser();
//                        UserMessage userMessage = TXApplication.getUserMess();
//                        user.name = userMessage.getNickname().toString();
//                        user.id = userMessage.getUid().toString();
//                        sdk.loginToUmengServerBySelfAccount(this, user, new LoginListener() {
//                            @Override
//                            public void onStart() {
//                            }
//                            @Override
//                            public void onComplete(int stCode, CommUser commUser) {
//                                Log.d("tag", "login result is"+stCode);          //获取登录结果状态码
//                                if (ErrorCode.NO_ERROR == stCode) {
//                                    //登录成功，可以打开社区，也可以进行其他的操作，开发者自己定义
//                                        UIHelper.startActivity(mContext,TexiuActivitiesActivity.class);
//                                }else{
//                                    UIHelper.ToastLogMessage(mContext,"出了点小问题，请稍后再试哦|");
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
//                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
//                }
                break;
            case R.id.rll_scan://扫一扫
                if (TXApplication.login) {
                    UIHelper.startActivity(mContext, CaptureActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }
                break;
            case R.id.rll_nearly_show_fang://附近秀坊地图
                UIHelper.startActivity(mContext, NearbyShowFangMapActivity.class);
                break;

            default:
                break;
        }
    }

}
