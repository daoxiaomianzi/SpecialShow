package com.show.specialshow.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends BaseSearchActivity {
    public static final String keyword = "keyword";
    //相关控件
    private EditText add_friend_et;
    private TextView search_add_friend_nodata_tv;

    private List<AddFriendMess> mList = new ArrayList<>();

    @Override
    public void initData() {
        setContentView(R.layout.activity_add_friend);
        adapter = new AddFriendAdapter(mList, mContext);
    }

    @Override
    public void initView() {
        add_friend_et = (EditText) findViewById(R.id.add_friend_et);
        search_add_friend_nodata_tv = (TextView) findViewById(R.id.search_add_friend_nodata_tv);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.addfriend);
        initListView();
        search_result_lv.setPullLoadEnable(false);
    }

    @Override
    public void setListener() {
        add_friend_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    UIHelper.isVisable(mContext, add_friend_et);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if (mList != null) {
                        mList.clear();
                    }
                    getData();
                }
                return false;
            }
        });
        add_friend_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.isEmpty(editable.toString().trim())) {
                    if (null != mList) {
                        mList.clear();
                    }
                    getData();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
//		case R.id.add_friend_btn:
////			if(TextUtils.isEmpty(add_friend_et.getText().toString().trim())){
////				createAffirmDialog("请输入名称或手机号", DIALOG_SINGLE_STPE,true);
////			}else{
//				Bundle bundle=new Bundle();
//				bundle.putString(keyword, add_friend_et.getText().toString().trim());
//				UIHelper.startActivity(mContext, SearchAddFriendActivity.class, bundle);
////			}
//			break;
            case R.id.contest_confirm_tv:
                if (null != affirmDialog) {
                    affirmDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_GETUSERBYKEYWORD;
        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
        params.addBodyParameter(keyword, add_friend_et.getText().toString().trim());
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
}
