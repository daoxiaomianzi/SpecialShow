package com.show.specialshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.adapter.OfficialEventAdapter;
import com.show.specialshow.adapter.TeShowActAdapter;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class OfficialEventFragment extends BaseSearch {
    private List<TeShowActivitiesMess> mList = new ArrayList<>();


    private TextView official_event_nodata_tv;
    public static OfficialEventFragment newInstance() {

        Bundle args = new Bundle();

        OfficialEventFragment fragment = new OfficialEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_offical_event, container,
                false);
        search_result_lv= (XListView) view.findViewById(R.id.search_result_lv);

        return view;
    }

    @Override
    protected void getData() {

    }

    @Override
    public void initData() {
        official_event_nodata_tv= (TextView) findViewById(R.id.official_event_nodata_tv);
        if(null!=mList){
            mList.clear();
        }
        for (int i = 0; i <2; i++) {
            TeShowActivitiesMess mess=new TeShowActivitiesMess();
            mList.add(mess);
        }
        adapter=new OfficialEventAdapter(mContext,mList);
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
