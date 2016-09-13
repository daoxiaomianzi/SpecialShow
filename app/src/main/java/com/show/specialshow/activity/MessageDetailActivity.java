package com.show.specialshow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;

public class MessageDetailActivity extends BaseActivity {
    private TextView message_detail_tv;
    private  String content;//信息详情

    @Override
    public void initData() {
        content=getIntent().getStringExtra("content");
        setContentView(R.layout.activity_message_detail);
    }

    @Override
    public void initView() {
        message_detail_tv= (TextView) findViewById(R.id.message_detail_tv);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("信息详情");
        message_detail_tv.setText(content);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
