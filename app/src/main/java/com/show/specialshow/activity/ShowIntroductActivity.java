package com.show.specialshow.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;

import org.apache.commons.lang3.StringUtils;

public class ShowIntroductActivity extends BaseActivity {
    private TextView show_introduction_tv;
    private String introduce;//秀坊简介内容


    @Override
    public void initData() {
        introduce=getIntent().getExtras().getString("introduce","");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_show_introduct);
        show_introduction_tv= (TextView) findViewById(R.id.show_introduction_tv);
    }

    @Override
    public void fillView() {
        if(StringUtils.isEmpty(introduce)){
            show_introduction_tv.setText("暂无简介");
        }else{
            show_introduction_tv.setText(introduce);
        }
        head_title_tv.setText(R.string.show_fang_introduction);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
