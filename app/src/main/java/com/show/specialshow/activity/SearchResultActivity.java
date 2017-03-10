package com.show.specialshow.activity;

import android.view.View;
import android.widget.AdapterView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.ShowLaneFragment;
import com.show.specialshow.model.CenterButtonMess;
import com.show.specialshow.model.ConditionMess;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.view.ScreenWorkDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BaseActivity {
    private String key;
    private int tag_id = 0;
    private String tag;
    private ScreenWorkDialog screenWorkDialog;
    private List<CenterButtonMess> mDatas;

    @Override
    public void initData() {
        key = getIntent().getExtras().getString("key", "");
        tag_id = getIntent().getExtras().getInt("tag_id", 0);
        tag = getIntent().getExtras().getString("tag", "");
        mDatas = (List<CenterButtonMess>) getIntent().getSerializableExtra("mData");
        setContentView(R.layout.activity_search_result);
        if (mDatas != null) {
            List<ConditionMess> stafftypeList = new ArrayList<>();
            for (int i = 0; i < mDatas.size(); i++) {
                stafftypeList.add(new ConditionMess(mDatas.get(i).getTag_id(), mDatas.
                        get(i).getTag()));
            }
            screenWorkDialog = new ScreenWorkDialog(mContext, stafftypeList);
        }
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
            head_right_tv.setVisibility(View.VISIBLE);
            head_right_tv.setText("筛选");
        }
    }

    @Override
    public void setListener() {
        if (null != screenWorkDialog) {
            screenWorkDialog.getCityAdapter().setCheckItem(tag_id - 1);
            screenWorkDialog.getCityView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    screenWorkDialog.getCityAdapter().setCheckItem(i);
                    tag_id = mDatas.get(i).getTag_id();
                    screenWorkDialog.dismiss();
                    head_title_tv.setText(mDatas.get(i).getTag());
                    ShowLaneFragment showLaneFragment = ShowLaneFragment.newInstance(key, tag_id);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_search_result_content, showLaneFragment)
                            .commit();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.head_right_tv:
                if (null != screenWorkDialog) {
                    screenWorkDialog.show();
                }
                break;
        }

    }


}
