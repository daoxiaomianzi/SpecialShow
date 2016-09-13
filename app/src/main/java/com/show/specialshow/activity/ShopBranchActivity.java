package com.show.specialshow.activity;

import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.MyCollectAdapter;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class ShopBranchActivity extends BaseSearchActivity {
    private TextView shop_branch_nodata_tv;
    private List<ShopListMess> mList=new ArrayList<>();
    private String shop_id;



    @Override
    public void initData() {
        shop_id=getIntent().getStringExtra("shop_id");
        setContentView(R.layout.activity_shop_branch);
    }

    @Override
    public void initView() {
        shop_branch_nodata_tv= (TextView) findViewById(R.id.shop_branch_nodata_tv);
        search_result_lv= (XListView) findViewById(R.id.search_result_lv);
        adapter=new MyCollectAdapter(mList,mContext,2);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("分店列表");
        initListView();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void getData() {
        RequestParams params= TXApplication.getParams();
        String url = URLs.SHOP_SHOPBRANCH;
        params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
        params.addBodyParameter("shop_id",shop_id);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if(null==result){
                    changeListView(0);
                    return;
                }
                switch (result.getSuccess()) {
                    case 1:
                        String info = result.getData();
                        List<ShopListMess> list=ShopListMess.parse(info);
                        if (null == list) {
                            search_result_lv.setVisibility(View.VISIBLE);
                            shop_branch_nodata_tv
                                    .setVisibility(View.VISIBLE);
                            changeListView(0);
                            search_result_lv.setPullLoadEnable(false);
                            return;
                        }
                        int size = list.size();
//					totalRecord = circleDynamicList.getTotal();
                        if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                            mList.clear();
                        }
                        mList.addAll(list);
                        for (int i = 0; i < mList.size(); i++) {
                            for (int j = mList.size() - 1; j > i; j--) {
                                if (mList.get(j).getShop_id()
                                        .equals(mList.get(i).getShop_id())) {
                                    mList.remove(j);
                                }
                            }
                        }
                        if (mList == null || mList.isEmpty()) {
                            search_result_lv.setVisibility(View.VISIBLE);
                            shop_branch_nodata_tv
                                    .setVisibility(View.VISIBLE);
                            search_result_lv.setPullLoadEnable(false);
                        } else {
                            search_result_lv.setVisibility(View.VISIBLE);
                            shop_branch_nodata_tv.setVisibility(View.GONE);
                            search_result_lv.setPullLoadEnable(false);
                        }
                        localRecord = mList.size();
                        changeListView(size);
//					 UIHelper.ToastLogMessage(mContext,
//					 result.getMessage());
                        break;
                    default:
                        changeListView(0);
                        mList.clear();
                        search_result_lv.setVisibility(View.VISIBLE);
                        shop_branch_nodata_tv
                                .setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
//					UIHelper.ToastLogMessage(mContext,
//							result.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext,R.string.net_work_error);
            }
        });
    }
}
