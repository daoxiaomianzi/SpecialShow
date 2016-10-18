package com.show.specialshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.adapter.IndustryDynamicAdapter;
import com.show.specialshow.adapter.TeShowActAdapter;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class IndustryDynamicFragment extends BaseSearch {
    private List<TeShowActivitiesMess> mList = new ArrayList<>();


    private TextView industry_dynamic_nodata_tv;
    public static IndustryDynamicFragment newInstance() {

        Bundle args = new Bundle();

        IndustryDynamicFragment fragment = new IndustryDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_indusyry_dynamic, container,
                false);
        search_result_lv= (XListView) view.findViewById(R.id.search_result_lv);

        return view;
    }

    @Override
    protected void getData() {

    }

    @Override
    public void initData() {
        industry_dynamic_nodata_tv= (TextView) findViewById(R.id.industry_dynamic_nodata_tv);
        if(null!=mList){
            mList.clear();
        }
        for (int i = 0; i <2; i++) {
            TeShowActivitiesMess mess=new TeShowActivitiesMess();
            mList.add(mess);
        }
        adapter=new IndustryDynamicAdapter(mContext,mList);
    }

    @Override
    public void initView() {
            initListView();
            search_result_lv.setDividerHeight(DensityUtil.dip2px(mContext,12));
            search_result_lv.setBackgroundResource(R.color.app_bg);
            search_result_lv.setPullLoadEnable(false);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillView() {

    }

    @Override
    public void onClick(View v) {

    }
}
