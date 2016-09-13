package com.show.specialshow.activity;

import android.content.Intent;
import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.Log;

public class LatestDynamicActivity extends BaseActivity {

    private CommUser mUser;


    @Override
    public void initData() {
        setContentView(R.layout.activity_latest_dynamic);
        mUser=CommonUtils.getLoginUser(mContext);

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.latest_dynamic);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_my_topic://我的话题
                myTopic();
                break;
            case R.id.rl_photo_albums_dynamic://我的动态
                UIHelper.startActivity(mContext, AlbumsDynamicActivity.class);
                break;
            case R.id.rl_my_collect://我的收藏
                UIHelper.startActivity(mContext, MyCollectActivity.class);
                break;
        }
    }

    /**
     * 我的话题
     */
    private void myTopic() {
        if(CommonUtils.isLogin(mContext)){
            Intent intent = new Intent(mContext, MyTopicActivity.class);
            intent.putExtra(Constants.USER_ID_KEY, mUser.id);
            startActivity(intent);
        }else{
            //创建CommUser前必须先初始化CommunitySDK
            CommunitySDK sdk = CommunityFactory.getCommSDK(this);
            CommUser user = new CommUser();
            UserMessage userMessage = TXApplication.getUserMess();
            user.name = userMessage.getNickname().toString();
            user.id = userMessage.getUid().toString();
            sdk.loginToUmengServerBySelfAccount(this, user, new LoginListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onComplete(int stCode, CommUser commUser) {
                    Log.d("tag", "login result is"+stCode);          //获取登录结果状态码
                    if (ErrorCode.NO_ERROR == stCode) {
                        //登录成功，可以打开社区，也可以进行其他的操作，开发者自己定义
                        Intent intent = new Intent(mContext, MyTopicActivity.class);
                        intent.putExtra(Constants.USER_ID_KEY, mUser.id);
                        startActivity(intent);
                    }else{
                        UIHelper.ToastLogMessage(mContext,"出了点小问题，请稍后再试哦|");
                    }
                }
            });
        }
    }
}
