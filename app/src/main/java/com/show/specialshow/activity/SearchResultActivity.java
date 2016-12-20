package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ShowLaneFragment;

public class SearchResultActivity extends BaseActivity {
    private String key;
    private int tag_id = 0;
    private String tag;

    @Override
    public void initData() {
        key = getIntent().getExtras().getString("key", "");
        tag_id = getIntent().getExtras().getInt("tag_id", 0);
        tag = getIntent().getExtras().getString("tag", "");
        setContentView(R.layout.activity_search_result);
    }

    @Override
    public void initView() {
        ShowLaneFragment showLaneFragment = ShowLaneFragment.newInstance(key, tag_id);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_search_result_content, showLaneFragment).
                show(showLaneFragment).commit();
    }

    @Override
    public void fillView() {
        if (0 == tag_id) {
            head_title_tv.setText("搜索结果");
        } else {
            head_title_tv.setText(tag);
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


}
