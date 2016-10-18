package com.show.specialshow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.adapter.TeShowActAdapter;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;


public class TeShowActivitiesFragment extends BaseSearch {
    private TextView te_show_nodata_tv;
    //
    private List<TeShowActivitiesMess> mList = new ArrayList<>();

    public TeShowActivitiesFragment() {
    }

    public static TeShowActivitiesFragment newInstance() {

        Bundle args = new Bundle();

        TeShowActivitiesFragment fragment = new TeShowActivitiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_te_show_activities, container,
                false);
        search_result_lv= (XListView) view.findViewById(R.id.search_result_lv);

        return view;
    }

    @Override
    public void initData() {
        te_show_nodata_tv= (TextView) findViewById(R.id.te_show_nodata_tv);
        if(null!=mList){
            mList.clear();
        }
        for (int i = 0; i <2; i++) {
            TeShowActivitiesMess mess=new TeShowActivitiesMess();
            mList.add(mess);
        }
        adapter=new TeShowActAdapter(mContext,mList);
    }



    @Override
    public void initView() {
        initListView();
        search_result_lv.setDividerHeight(0);
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


    @Override
    protected void getData() {

    }
}
