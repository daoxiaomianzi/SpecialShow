package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.utils.CreateQRImage;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.Two_dimensionDialog;
import com.umeng.comm.core.beans.ShareContent;

public class MyInviteActivity extends BaseActivity {
    @Override
    public void initData() {
        setContentView(R.layout.activity_my_invite);
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.my_invite);
        head_right_tv.setVisibility(View.VISIBLE);
        head_right_tv.setText("邀请");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_right_tv://邀请
                inviteFriends();
                break;
            case R.id.rl_invite_record://邀请记录
                UIHelper.startActivity(mContext,MyInviteRecordActivity.class);
                break;
            case R.id.rl_my_two_code://我的专属二维码
                CreateQRImage cqri = new CreateQRImage(mContext);
                Two_dimensionDialog dialog;
                    dialog = new Two_dimensionDialog(
                            mContext,
                            cqri.createQRImage("http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid="
                                    + SPUtils.get(mContext, "uid", "")));
                if (dialog != null) {
                    dialog.cancel();
                }
                dialog.show();
                break;
        }
    }
    /**
     * 邀请好友
     */
    private void inviteFriends(){
        ShareContent shareItem = new ShareContent();
        shareItem.mText ="特秀美妆:美不曾离开，让你的美由此开始";
        shareItem.mTargetUrl = "http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid="+ TXApplication.getUserMess().getUid();
        shareItem.mTitle ="特秀美妆:美不曾离开，让你的美由此开始";
//                ShareSDKManager.getInstance().getCurrentSDK().share((Activity) mContext, shareItem);
        ShareServiceFactory.getShareService().share(this,shareItem);
    }
}
