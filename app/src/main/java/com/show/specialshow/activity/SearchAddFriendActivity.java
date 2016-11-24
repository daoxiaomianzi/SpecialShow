package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.AddFriendAdapter;
import com.show.specialshow.model.AddFriendMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SearchAddFriendActivity extends BaseSearchActivity {
    //数据相关
    private String keyword;
    private List<AddFriendMess> mList = new ArrayList<>();
    //相关控件
    private TextView search_add_friend_nodata_tv;

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_GETUSERBYKEYWORD;
        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
        params.addBodyParameter("keyword", keyword);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                onError(getResources().getString(
                        R.string.net_work_error));
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    changeListView(0);
                    return;
                }
                if (1 == result.getSuccess()) {
                    String info = result.getData();
                    List<AddFriendMess> list = AddFriendMess.parse(info);
                    if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    if (mList == null || mList.isEmpty()) {
                        search_result_lv.setVisibility(View.VISIBLE);
                        search_add_friend_nodata_tv.setVisibility(View.VISIBLE);
                    } else {
                        search_result_lv.setVisibility(View.VISIBLE);
                        search_add_friend_nodata_tv.setVisibility(View.GONE);
                    }
                    totalRecord = -1;
                    changeListView(0);
                } else {
                    search_result_lv.setVisibility(View.VISIBLE);
                    search_add_friend_nodata_tv.setVisibility(View.VISIBLE);
                    totalRecord = -1;
                    changeListView(0);
                }
            }
        });

    }

    @SuppressLint("NewApi")
    @Override
    public void initData() {
        keyword = getIntent().getExtras().getString(AddFriendActivity.keyword, "");
        setContentView(R.layout.activity_search_add_friend);
//		View head=findViewById(R.id.head_rl);
//		ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
        adapter = new AddFriendAdapter(mList, mContext);
    }

    @Override
    public void initView() {
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
        search_add_friend_nodata_tv = (TextView) findViewById(R.id.search_add_friend_nodata_tv);
    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.addfriend);
        initListView();
        search_result_lv.setPullLoadEnable(false);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


}
