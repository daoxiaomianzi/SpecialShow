package com.show.specialshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.OfficialEventAndDynamicActivity;
import com.show.specialshow.adapter.IndustryDynamicAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.TeShowActivitiesList;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.UIHelper;
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
        RequestParams params = TXApplication.getParams();
        String url= URLs.POSTS_POSTSLIST;
        params.addBodyParameter("term_id","3");
        params.addBodyParameter("pageSize", ConstantValue.PAGE_SIZE+"");
        params.addBodyParameter("pageNow",pageIndex+"");
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                loadIng("加载中....",true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if(null!=dialog){
                    dialog.dismiss();
                }
                MessageResult result=MessageResult.parse(responseInfo.result);
                if (null==result) {

                    onError(getResources().getString(R.string.net_server_error));
                    return;
                }
                if(1==result.getSuccess()){
                    if(null!=dialog){
                        dialog.dismiss();
                    }
                    TeShowActivitiesList teShowActivitiesList=TeShowActivitiesList.parse(result.getData());
                    List<TeShowActivitiesMess> list=teShowActivitiesList.getList();
                    if (null==teShowActivitiesList||null==list) {
                        changeListView(0);
                        search_result_lv.setVisibility(View.VISIBLE);
                        industry_dynamic_nodata_tv.setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                        return;
                    }
                    int size = list.size();
                    totalRecord=teShowActivitiesList.getTotal();
                    if (search_result_lv.getState()== XListView.LOAD_REFRESH) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    for(int i = mList.size() - 1; i > 0; i--) {
                        for(int j = i - 1; j >= 0; j--) {
                            if(mList.get(j).getPost_id()==mList.get(i).getPost_id()) {
                                mList.remove(j);
                                break;
                            }
                        }
                    }
                    if (mList == null || mList.isEmpty()) {
                        search_result_lv.setVisibility(View.VISIBLE);
                        industry_dynamic_nodata_tv
                                .setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                    } else {
                        search_result_lv.setVisibility(View.VISIBLE);
                        industry_dynamic_nodata_tv.setVisibility(View.GONE);
                        search_result_lv.setPullLoadEnable(true);
                    }
                    localRecord = mList.size();
                    changeListView(size);
                }else{
                    changeListView(0);
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if(null!=dialog){
                    dialog.dismiss();
                }
                onError(getResources().getString(R.string.net_work_error));
            }
        });
    }

    @Override
    public void initData() {
        industry_dynamic_nodata_tv= (TextView) findViewById(R.id.industry_dynamic_nodata_tv);
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
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("event_dynamic",mList.get(position-1));
                UIHelper.startActivity(getActivity(), OfficialEventAndDynamicActivity.class,bundle);
            }
        });
    }

    @Override
    public void fillView() {

    }

    @Override
    public void onClick(View v) {

    }
}
