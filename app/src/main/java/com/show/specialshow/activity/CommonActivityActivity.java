package com.show.specialshow.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

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
    public void offerPay(final String shop_id, final String service_id, final String service_name, final String service_price_now
            , final String service_price_old, final String service_price_discount, final String staff_list) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putInt(CraftsmandetailsActivity.WHERR_FROM, 4);
                bundle.putString("shop_id", shop_id);
                bundle.putString("service_id", service_id);
                bundle.putString("service_name", service_name);
                bundle.putString("service_price_now", service_price_now);
                bundle.putString("service_price_old", service_price_old);
                bundle.putString("service_price_discount", service_price_discount);
                bundle.putString("staff_list", staff_list);
                UIHelper.startActivity(mContext, OrderActivity.class, bundle);
            }
        });
    }

    @JavascriptInterface
    public void inviteStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                UIHelper.startActivity(mContext, MyInviteActivity.class);
            }
        });
    }


}
