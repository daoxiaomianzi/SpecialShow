package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseWebActivity;
import com.show.specialshow.model.TeShowActivitiesMess;

public class OfficialEventAndDynamicActivity extends BaseWebActivity {
    private TeShowActivitiesMess activitiesMess;

    @Override
    public void initData() {
        super.initData();
        activitiesMess = (TeShowActivitiesMess) getIntent().getSerializableExtra("event_dynamic");
        if (null!=activitiesMess) {
            html=activitiesMess.getPost_content();
        }
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("详情");
        loadNativeDetail();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
