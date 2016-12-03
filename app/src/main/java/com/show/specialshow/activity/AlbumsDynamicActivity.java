package com.show.specialshow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.AlbumsDynamicAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.CircleDynamicItem;
import com.show.specialshow.model.CircleDynamicList;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class AlbumsDynamicActivity extends BaseSearchActivity {
    private TextView albums_dynamic_nodata_tv;
    private List<CircleDynamicItem> mList = new ArrayList<CircleDynamicItem>();

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        UserMessage user = TXApplication.getUserMess();
        String url = URLs.MYPHOTO_INFO;
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("pageSize", "" + ConstantValue.PAGE_SIZE);
        params.addBodyParameter("pageNow", "" + pageIndex);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                changeListView(0);
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (result == null) {
                    return;
                }
                switch (result.getSuccess()) {
                    case 1:
                        String info = result.getData();
                        CircleDynamicList circleDynamicList = CircleDynamicList
                                .parse(info);
                        List<CircleDynamicItem> list = circleDynamicList
                                .getList();
                        if (null == list) {
                            search_result_lv.setVisibility(View.VISIBLE);
                            albums_dynamic_nodata_tv
                                    .setVisibility(View.VISIBLE);
                            search_result_lv.setPullLoadEnable(false);
                            changeListView(0);
                            return;
                        }
                        int size = list.size();
                        totalRecord = circleDynamicList.getTotal();
                        if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                            mList.clear();
                        }
                        mList.addAll(list);
                        for (int i = 0; i < mList.size(); i++) {
                            for (int j = mList.size() - 1; j > i; j--) {
                                if (mList.get(j).getIdStr()
                                        .equals(mList.get(i).getIdStr())) {
                                    mList.remove(j);
                                }
                            }
                        }
                        if (mList == null || mList.isEmpty()) {
                            search_result_lv.setVisibility(View.VISIBLE);
                            albums_dynamic_nodata_tv
                                    .setVisibility(View.VISIBLE);
                            search_result_lv.setPullLoadEnable(false);
                        } else {
                            search_result_lv.setVisibility(View.VISIBLE);
                            albums_dynamic_nodata_tv.setVisibility(View.GONE);
                            search_result_lv.setPullLoadEnable(true);
                        }
                        localRecord = mList.size();
                        changeListView(size);
//					 UIHelper.ToastLogMessage(mContext,
//					 result.getMessage());
                        break;
                    default:
                        changeListView(0);
                        UIHelper.ToastLogMessage(mContext,
                                result.getMessage());
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        setContentView(R.layout.activity_albums_dynamic);
    }

    @Override
    public void initView() {
        albums_dynamic_nodata_tv = (TextView) findViewById(R.id.albums_dynamic_nodata_tv);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
        adapter = new AlbumsDynamicAdapter(AlbumsDynamicActivity.this, mList);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("我的动态");
        initListView();
    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                CircleDynamicItem item = mList.get(position - 1);
                bundle.putString("idStr", item.getIdStr());
                UIHelper.startActivity(mContext,
                        CircleDynamicDetailActivity.class, bundle);
            }
        });
    }

    @Override
    public void onClick(View v) {
    }


}
