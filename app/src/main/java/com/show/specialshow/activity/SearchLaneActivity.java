package com.show.specialshow.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.db.SearchDatabaseUtil;
import com.show.specialshow.model.SearchHistoryMess;
import com.show.specialshow.utils.ClearEditText;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchLaneActivity extends BaseActivity {
    @ViewInject(R.id.search_lane_et)
    private ClearEditText search_lane_et;

    @ViewInject(R.id.search_history)
    private TextView search_history;

    @ViewInject(R.id.search_history_list)
    private MyListView search_history_list;

    @ViewInject(R.id.clear_search_history)
    private TextView clear_search_history;

    @ViewInject(R.id.search_history_vi)
    private View search_history_vi;
    @ViewInject(R.id.clear_search_history_vi)
    private View clear_search_history_vi;

    private List<SearchHistoryMess> searchHistoryList = new ArrayList<>();
    private SearchDatabaseUtil mDatabaseUtil;
    private SearchHistoryAdapter adapter;

    @Override
    public void initData() {
        setContentView(R.layout.activity_search_lane);
        activityFlag = 1;
        ViewUtils.inject(mContext);
        mDatabaseUtil = new SearchDatabaseUtil(mContext);
    }

    @Override
    public void initView() {
    }

    @Override
    public void fillView() {

    }

    private void viewChange() {
        if (null == searchHistoryList || searchHistoryList.isEmpty()) {
            search_history.setVisibility(View.GONE);
            clear_search_history.setVisibility(View.GONE);
            search_history_vi.setVisibility(View.GONE);
            clear_search_history_vi.setVisibility(View.GONE);
        } else {
            search_history.setVisibility(View.VISIBLE);
            clear_search_history.setVisibility(View.VISIBLE);
            search_history_vi.setVisibility(View.VISIBLE);
            clear_search_history_vi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
        search_lane_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    UIHelper.isVisable(mContext, search_lane_et);
                    if (StringUtils.isEmpty(
                            search_lane_et.getText().toString().trim())) {
                        UIHelper.ToastMessage(mContext, "请输入搜索关键词！");
                    } else {
                        SearchHistoryMess searchHistoryMess = new SearchHistoryMess();
                        searchHistoryMess.setSearch_history(search_lane_et.getText()
                                .toString().trim());
                        insertSearch(searchHistoryMess);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", search_lane_et.getText().toString().trim());
                        UIHelper.startActivity(mContext, SearchResultActivity.class, bundle);
                    }
                }
                return false;
            }
        });

    }

    private void insertSearch(SearchHistoryMess searchHistoryMess) {
        mDatabaseUtil.Insert(searchHistoryMess);
        List<SearchHistoryMess> list = mDatabaseUtil.queryAll();
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getSearch_history().equals(
                    searchHistoryMess.getSearch_history())) {
                mDatabaseUtil.Delete(searchHistoryMess.getSearch_history());
                mDatabaseUtil.Insert(searchHistoryMess);
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_lane_cancel:
                UIHelper.isVisable(mContext, search_lane_et);
                finish();
                break;
            case R.id.clear_search_history:
                mDatabaseUtil.deleteAll();
                searchHistoryList.clear();
                viewChange();
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchHistoryList = mDatabaseUtil.queryAll();
        Collections.reverse(searchHistoryList);
        adapter = new SearchHistoryAdapter(searchHistoryList, mContext);
        search_history_list.setAdapter(adapter);
        viewChange();
    }

    class SearchHistoryAdapter extends BaseAdapter {
        private List<SearchHistoryMess> searchHistoryList = new ArrayList<>();
        private Context mContext;

        public SearchHistoryAdapter(List<SearchHistoryMess> searchHistoryList,
                                    Context mContext) {
            this.searchHistoryList = searchHistoryList;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return null == searchHistoryList ? 0 : searchHistoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return null == searchHistoryList ? null : searchHistoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return null == searchHistoryList ? 0 : i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_search_lane, null);
                viewHolder.item_search_keyword_tv = (TextView)
                        convertView.findViewById(R.id.item_search_keyword_tv);
                viewHolder.item_clear_keyword_iv = (ImageView)
                        convertView.findViewById(R.id.item_clear_keyword_iv);
                viewHolder.rll_search_keyword = (RelativeLayout) convertView
                        .findViewById(R.id.rll_search_keyword);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.item_search_keyword_tv.setText(searchHistoryList.get(position)
                    .getSearch_history());
            viewHolder.item_clear_keyword_iv.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabaseUtil.Delete(searchHistoryList.get(position).
                                    getSearch_history());
                            searchHistoryList.remove(position);
                            viewChange();
                            notifyDataSetChanged();

                        }
                    });
            viewHolder.rll_search_keyword.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UIHelper.isVisable(mContext, search_lane_et);
                            insertSearch(searchHistoryList.get(position));
                            Bundle bundle = new Bundle();
                            bundle.putString("key", searchHistoryList.get(position)
                                    .getSearch_history());
                            UIHelper.startActivity((Activity) mContext, SearchResultActivity.class, bundle);
                        }
                    });
            return convertView;
        }

        class ViewHolder {
            RelativeLayout rll_search_keyword;
            TextView item_search_keyword_tv;
            ImageView item_clear_keyword_iv;
        }
    }
}
