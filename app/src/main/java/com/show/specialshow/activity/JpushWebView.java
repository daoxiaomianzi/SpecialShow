package com.show.specialshow.activity;

import android.view.View;


public class JpushWebView extends BaseBusCenWebActivity {
    @Override
    public void initData() {
        super.initData();
        path = getIntent().getExtras().getString("url");
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("载入中...");
        head_left_tv.setVisibility(View.VISIBLE);
        loadDetail();
//		switch (which_banner) {
//		case 0:
//			head_title_tv.setText("注册有礼");
//			break;
//		case 1:
//			head_title_tv.setText("风险备付金");
//			break;
//		}
    }

}
