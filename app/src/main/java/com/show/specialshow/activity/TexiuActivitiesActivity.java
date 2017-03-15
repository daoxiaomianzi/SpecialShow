package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.IndustryDynamicFragment;
import com.show.specialshow.fragment.OfficialEventFragment;
import com.show.specialshow.fragment.TeShowActivitiesFragment;
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
        switch (getIntent().getIntExtra("type", 1)) {
            case 1:
                head_title_tv.setText(R.string.texiu_activity);

                getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container, TeShowActivitiesFragment.newInstance()).commit();
                break;
            case 2:
                head_title_tv.setText(R.string.industry_dynamic);

                getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container, IndustryDynamicFragment.newInstance()).commit();
                break;
            case 3:
                head_title_tv.setText(R.string.makeup_tutorial);
                getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container, OfficialEventFragment.newInstance()).commit();
                break;
            default:
                break;
        }
//        getSupportFragmentManager().beginTransaction().add(R.id.view_activity_container,new RecommendTopicSearchFragment()).commit();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
