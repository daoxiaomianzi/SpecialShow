/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.umeng.commm.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.listeners.Listeners.FetchListener;
import com.umeng.comm.core.nets.responses.TopicResponse;
import com.umeng.comm.core.nets.uitls.NetworkUtils;
import com.umeng.comm.core.utils.DeviceUtils;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.common.ui.adapters.SelectTopicAdapter;
import com.umeng.common.ui.colortheme.ColorQueque;
import com.umeng.common.ui.dialogs.PickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布Feed时选择话题的Dialog
 */
public class SelectTopicDialog extends PickerDialog<Topic> {
    /**
     * 话题下一页url地址。每次从server获取列表时，都能够拿到该url，因此不cache到DB
     */
    private Drawable drawableLine;
    private TextView allBtn, focusBtn;

    private List<Topic> mFocusTopics = new ArrayList();
    private List<Topic> mAllTopics = new ArrayList();

    private String mFocusTopicsUrl;
    private String mAllTopicsUrl;

    private int mCurrentTab;
    private boolean isLoading;

    public SelectTopicDialog(Context context) {
        this(context, 0);
        setContentView(this.createContentView());
    }

    public SelectTopicDialog(Context context, int theme) {
        super(context, theme);
        setContentView(this.createContentView());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        loadDataFromServer();
    }

    @Override
    protected void setupAdater() {
        drawableLine = ColorQueque.getDrawable("blue_line_for_tv");
        drawableLine.setBounds(0, 0, DeviceUtils.dp2px(getContext(), 70), DeviceUtils.dp2px(getContext(), 2));
        (mRootView.findViewById(ResFinder.getId("umeng_comm_line"))).setVisibility(View.VISIBLE);
        allBtn = (TextView) mRootView.findViewById(ResFinder.getId("umeng_comm_alltopic"));
        focusBtn = (TextView) mRootView.findViewById(ResFinder.getId("umeng_comm_focustopic"));
        allBtn.setTextColor(ColorQueque.getColor("umeng_comm_comment_action_dialog_text"));
        allBtn.setCompoundDrawables(null, null, null, drawableLine);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBtn.setTextColor(ColorQueque.getColor("umeng_comm_find_user_login_text"));
                allBtn.setCompoundDrawables(null, null, null, drawableLine);
                focusBtn.setTextColor(ColorQueque.getColor("umeng_comm_small_title_color"));
                focusBtn.setCompoundDrawables(null, null, null, null);
                mCurrentTab = 0;

                if(!mAllTopics.isEmpty()){
                    mAdapter.getDataSource().clear();
                    mAdapter.addData(mAllTopics);
                }else {
                    loadAllDataFromServer();
                }
            }
        });
        focusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusBtn.setTextColor(ColorQueque.getColor("umeng_comm_find_user_login_text"));
                focusBtn.setCompoundDrawables(null, null, null, drawableLine);
                allBtn.setTextColor(ColorQueque.getColor("umeng_comm_small_title_color"));
                allBtn.setCompoundDrawables(null, null, null, null);
                mCurrentTab = 1;
                if(!mFocusTopics.isEmpty()){
                    mAdapter.getDataSource().clear();
                    mAdapter.addData(mFocusTopics);
                }else {
                    loadFocusDataFromServer();
                }
            }
        });
        mAdapter = new SelectTopicAdapter(getContext());
        mRefreshLvLayout.setAdapter(mAdapter);
        topicButtonGroup.setVisibility(View.VISIBLE);
        String title = ResFinder.getString("umeng_comm_topic");
        mTitleTextView.setText(title);
        mListView.setFooterDividersEnabled(true);
        mListView.setOverscrollFooter(null);
    }

    @Override
    protected void setupLvOnItemClickListener() {
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickItemAtPosition(position);
            }
        });
    }

    @Override
    public void loadDataFromServer() {
        if(mCurrentTab == 0){
            loadAllDataFromServer();
        }else {
            loadFocusDataFromServer();
        }
    }

    public void loadAllDataFromServer() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mSdkImpl.fetchTopics(new FetchListener<TopicResponse>() {

            @Override
            public void onStart() {
                mRefreshLvLayout.setRefreshing(true);
            }

            @Override
            public void onComplete(TopicResponse response) {
                isLoading = false;
                mRefreshLvLayout.setRefreshing(false);
                if (NetworkUtils.handleResponseAll(response)) {
                    return;
                }
                mAllTopicsUrl = response.nextPageUrl;
                handleResultData(response.result, true, false);
            }
        });
    }

    public void loadFocusDataFromServer() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        String uid = CommConfig.getConfig().loginedUser.id;
        mSdkImpl.fetchFollowedTopics(uid, new FetchListener<TopicResponse>() {

            @Override
            public void onStart() {
                mRefreshLvLayout.setRefreshing(true);
            }

            @Override
            public void onComplete(TopicResponse response) {
                isLoading = false;
                mRefreshLvLayout.setRefreshing(false);
                if (NetworkUtils.handleResponseAll(response)) {
                    return;
                }
                mFocusTopicsUrl = response.nextPageUrl;
                handleResultData(response.result, true, true);
            }
        });
    }

    @Override
    public void loadMore() {
        if (isLoading) {
            return;
        }
        if (mCurrentTab == 0) {
            loadMoreAllTopics();
        } else {
            loadMoreFocusTopics();
        }
    }

    private void loadMoreFocusTopics() {
        if (TextUtils.isEmpty(mFocusTopicsUrl)) {
            mRefreshLvLayout.setLoading(false);
            return;
        }
        isLoading = true;
        mSdkImpl.fetchNextPageData(mFocusTopicsUrl, TopicResponse.class,
                new FetchListener<TopicResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(TopicResponse response) {
                        isLoading = false;
                        mRefreshLvLayout.setLoading(false);
                        if (NetworkUtils.handleResponseAll(response)) {
                            return;
                        }
                        mFocusTopicsUrl = response.nextPageUrl;
                        handleResultData(response.result, false, true);
                    }
                });
    }

    private void loadMoreAllTopics() {
        if (TextUtils.isEmpty(mAllTopicsUrl)) {
            mRefreshLvLayout.setLoading(false);
            return;
        }
        isLoading = true;
        mSdkImpl.fetchNextPageData(mAllTopicsUrl, TopicResponse.class,
                new FetchListener<TopicResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(TopicResponse response) {
                        isLoading = false;
                        mRefreshLvLayout.setLoading(false);
                        if (NetworkUtils.handleResponseAll(response)) {
                            return;
                        }
                        mAllTopicsUrl = response.nextPageUrl;
                        handleResultData(response.result, false, false);
                    }
                });
    }

    @Override
    protected void pickItemAtPosition(int position) {
        super.pickItemAtPosition(position);
        mSelectedItem = null;
    }

    private void handleResultData(List<Topic> topics, boolean isRefresh, boolean isFocusTopics) {
        if (topics == null || topics.isEmpty()) {
            return;
        }

        if (isFocusTopics) {
            if (isRefresh) {
                mFocusTopics.clear();
            }
            mFocusTopics.addAll(topics);
        } else {
            if (isRefresh) {
                mAllTopics.clear();
            }
            mAllTopics.addAll(topics);
        }

        mAdapter.getDataSource().clear();
        if (mCurrentTab == 0) {
            mAdapter.addData(mAllTopics);
        } else {
            mAdapter.addData(mFocusTopics);
        }
    }
}
