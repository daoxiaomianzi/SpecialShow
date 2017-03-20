package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.beans.ShareContent;

public class CommonActivityActivity extends BaseBusCenWebActivity {
    private static final int SET_TRADING_PASSWORD = 251;
    private static final int SET_BINDING_PHONE = 252;

    @Override
    public void initData() {
        super.initData();
        path = getIntent().getStringExtra("status_url");
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
    public void offerPay(String shop_id, String service_id, String service_name) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }


}
