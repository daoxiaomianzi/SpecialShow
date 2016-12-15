package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.DataCleanManager;
import com.show.specialshow.utils.JpushUtils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.utils.CommonUtils;

public class SetActivity extends BaseActivity {
    private TextView set_mb_num;
    private TextView finish_login_tv;
    private TextView tv_payment_code_modify;

    @Override
    public void initData() {
        setContentView(R.layout.activity_set);

    }

    @Override
    public void initView() {
        set_mb_num = (TextView) findViewById(R.id.set_mb_num);
        finish_login_tv = (TextView) findViewById(R.id.finish_login_tv);
        tv_payment_code_modify = (TextView) findViewById(R.id.tv_payment_code_modify);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("设置");
        finish_login_tv.setText("退出登录（" + SPUtils.get(mContext, "nickname", "") + "）");
        try {
            set_mb_num.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TXApplication.setTradingpassword) {
            tv_payment_code_modify.setText("修改");
        } else {
            tv_payment_code_modify.setText("设置");
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.rl_set_clean://清除缓存
                cleanDataDir();
                break;
            case R.id.rl_set_info://我的资料
                bundle.putInt("basic_mode", 1);
                UIHelper.startActivity(mContext, BasicInformationActivity.class, bundle);
                break;
            case R.id.rl_finish_login://退出登录
                finishLogin();
                break;
            case R.id.rl_set_informaton://系统消息
                UIHelper.startActivity(mContext, MessageNoticeActivity.class);
                break;
            case R.id.rl_set_abouts_us://关于我们
                UIHelper.startActivity(mContext, AboutUsActivity.class);
                break;
            case R.id.rl_login_password://登录密码
                break;
            case R.id.rl_payment_code://支付密码
                UIHelper.startActivity(mContext, SetTradingPasswordActivity.class);
                break;


            default:
                break;
        }
    }

    //退出登录
    private void finishLogin() {
        if (CommonUtils.isLogin(mContext)) {
            TXApplication.getInstance().umengLogout();
        }
        TXApplication.getInstance().logout(true, null);
        TXApplication.quitLogin();
        if (!(boolean) SPUtils.get(mContext, "setAlias", false)) {
            //调用JPush API设置Alias
            JpushUtils jpushUtils = new JpushUtils(mContext);
            jpushUtils.mHandler.sendMessage(jpushUtils.mHandler.
                    obtainMessage(JpushUtils.MSG_SET_ALIAS, ""));
        }
        startActivity();
    }

    /**
     * 页面跳转
     */
    private void startActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //清除缓存
    private void cleanDataDir() {
        DataCleanManager.clearAllCache(getApplicationContext());
        ;
        try {
            set_mb_num.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
            UIHelper.ToastMessage(mContext, "缓存清除");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
