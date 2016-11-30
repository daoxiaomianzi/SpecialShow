package com.show.specialshow.activity;


import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ShowLaneFragment;
import com.show.specialshow.fragment.ShowVisitorFragment;

public class ShowLaneActivity extends BaseActivity {


    @Override
    public void initData() {
        setContentView(R.layout.activity_show_lane);
    }

    @Override
    public void initView() {
        ShowLaneFragment showLaneFragment = new ShowLaneFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_show_lane, showLaneFragment).
                show(showLaneFragment).commit();
    }

    @Override
    public void fillView() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
