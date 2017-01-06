package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.beans.ShareContent;

public class IntegralmMarketActivity extends BaseBusCenWebActivity {
    private static final int SET_TRADING_PASSWORD = 251;
    private static final int SET_BINDING_PHONE = 252;

    @Override
    public void initData() {
        super.initData();
        String uid = (String) SPUtils.get(mContext, "uid", "");
        path = URLs.INTEGRAL_MARKET + uid + "&" + ConstantValue.sign + "=" +
                MD5Utils.getMd5Str("22" + uid + ConstantValue.SIGN);
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("");
        loadDetail();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.contest_confirm_tv:
                affirmDialog.dismiss();
                break;
            case R.id.contest_cancel_tv:
                if (null != affirmDialog) {
                    affirmDialog.dismiss();
                }
                break;

            default:
                break;
        }
    }

    @JavascriptInterface
    public void startSetPhone() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                UIHelper.startActivityForResult(mContext, BindingPhoneActivity.class, SET_BINDING_PHONE, bundle);
            }
        });
    }

    @JavascriptInterface
    public void startSetPassword() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                UIHelper.startActivityForResult(mContext,
                        SetTradingPasswordActivity.class, SET_TRADING_PASSWORD, bundle);
            }
        });
    }

    @JavascriptInterface
    public void share() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                inviteFriends();
            }
        });
    }


    /**
     * 邀请好友
     */
    private void inviteFriends() {
        ShareContent shareItem = new ShareContent();
        shareItem.mText = "特秀美妆:美不曾离开，让你的美由此开始";
        shareItem.mTargetUrl = "http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid=" + TXApplication.getUserMess().getUid();
        shareItem.mTitle = "特秀美妆:美不曾离开，让你的美由此开始";
//                ShareSDKManager.getInstance().getCurrentSDK().share((Activity) mContext, shareItem);
        ShareServiceFactory.getShareService().share(this, shareItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SET_TRADING_PASSWORD && TXApplication.setTradingpassword) {
            content.reload();
        }
        if (requestCode != SET_TRADING_PASSWORD && requestCode != SET_BINDING_PHONE) {
            ShareServiceFactory.getShareService().onActivityResult(mContext, requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK && requestCode == SET_BINDING_PHONE) {
            content.reload();
        }
    }
}
