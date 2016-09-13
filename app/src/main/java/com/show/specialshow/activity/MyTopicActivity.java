package com.show.specialshow.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.umeng.comm.core.constants.Constants;
import com.umeng.commm.ui.fragments.FollowedTopicFragment;

public class MyTopicActivity extends BaseActivity {


    @Override
    public void initData() {
        setContentView(R.layout.activity_texiu_activities);
    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.my_topic);
        getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container,createFragment(getIntent())).commit();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    private Fragment createFragment(Intent intent) {
        String uid = intent.getStringExtra(Constants.USER_ID_KEY);
        return FollowedTopicFragment.newFollowedTopicFragment(uid);
    }
}
