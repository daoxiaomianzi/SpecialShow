package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.show.specialshow.adapter.MessageNoticeAdapter;
import com.show.specialshow.model.MessageNoticeMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class MessageNoticeActivity extends BaseSearchActivity {
    private int status = 0;//消息状态(传0得到未读消息,1为已读消息)
    private UserMessage userMessage;//用户个人信息
    private List<MessageNoticeMess> mlist = new ArrayList<>();
    // 相关控件
    private TextView message_notice_nodata_tv;


    @Override
    public void initData() {
        userMessage = TXApplication.getUserMess();
        setContentView(R.layout.activity_message_notice);
    }

    @Override
    public void initView() {
        message_notice_nodata_tv = (TextView) findViewById(R.id.message_notice_nodata_tv);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
        adapter = new MessageNoticeAdapter(mContext, mlist);
    }

    @Override
    public void fillView() {
        initListView();
        search_result_lv.setPullLoadEnable(false);
    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (0 == status) {
                    changeStatus(mlist.get(i - 1).getIdstr());

                }
                Bundle bundle = new Bundle();
                bundle.putString("content", mlist.get(i - 1).getContent());
                UIHelper.startActivity(mContext, MessageDetailActivity.class, bundle);
            }
        });
    }

    /**
     * 把未读消息变成已读信息
     */
    private void changeStatus(String idStr) {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_UPDATEMESSAGE;
        params.addBodyParameter("idStr", idStr);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_notice_unread_rb://未读消息
                status = 0;
                getData();
                break;
            case R.id.message_notice_read_rb://已读消息
                status = 1;
                getData();
                break;
            case R.id.message_notice_back://返回
                Intent data=new Intent();
                UIHelper.setResult(mContext, RESULT_OK, data);
                break;
        }

    }

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_MESSAGE;
        params.addBodyParameter("uid", userMessage.getUid());
        params.addBodyParameter("status", status + "");
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    message_notice_nodata_tv.setVisibility(View.VISIBLE);
                    changeListView(0);
                    return;
                }
                if (1 == result.getSuccess()) {
                    List<MessageNoticeMess> list = MessageNoticeMess.parse(result.getData());
                    if (null != mlist) {
                        mlist.clear();
                    }
                    mlist.addAll(list);
                    if (mlist.isEmpty() || null == mlist) {
                        message_notice_nodata_tv.setVisibility(View.VISIBLE);
                    } else {
                        message_notice_nodata_tv.setVisibility(View.GONE);
                    }
                    changeListView(0);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                onError(getResources().getString(R.string.net_work_error));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent data=new Intent();
            UIHelper.setResult(mContext, RESULT_OK, data);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
