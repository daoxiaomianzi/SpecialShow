package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ShowLaneFragment;

public class SearchResultActivity extends BaseActivity {
    private String key;

    @Override
    public void initData() {
        key = getIntent().getExtras().getString("key", "");
        setContentView(R.layout.activity_search_result);
    }

    @Override
    public void initView() {
        ShowLaneFragment showLaneFragment = ShowLaneFragment.newInstance(key);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_search_result_content, showLaneFragment).
                show(showLaneFragment).commit();
    }

    @Override
    public void fillView() {
        head_title_tv.setText("搜索结果");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
