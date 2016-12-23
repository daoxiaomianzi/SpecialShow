package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ShowLaneFragment;
import com.show.specialshow.fragment.ShowVisitorFragment;

public class ShowVisitorActivity extends BaseActivity {


    @Override
    public void initData() {
        setContentView(R.layout.activity_show_visitor);
    }

    @Override
    public void initView() {
        ShowVisitorFragment showVisitorFragment = new ShowVisitorFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_show_visitor, showVisitorFragment).
                show(showVisitorFragment).commit();
    }

    @Override
    public void fillView() {
        head_title_tv.setText("附近秀客");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}

