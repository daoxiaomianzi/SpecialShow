package com.show.specialshow.activity;


import com.show.specialshow.BaseWebActivity;

public class BannerWebActivity extends BaseWebActivity {

    @Override
    public void initView() {
        super.initView();
        path=getIntent().getStringExtra("banner_path");
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("活动");
        loadDetail();
    }
}
