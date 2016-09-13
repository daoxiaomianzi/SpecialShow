package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.umeng.commm.ui.fragments.RecommendTopicSearchFragment;

public class TexiuActivitiesActivity extends BaseActivity {
    //相关控件

    @Override
    public void initData() {
        setContentView(R.layout.activity_texiu_activities);
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.texiu_activity);
        getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container,new RecommendTopicSearchFragment()).commit();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
