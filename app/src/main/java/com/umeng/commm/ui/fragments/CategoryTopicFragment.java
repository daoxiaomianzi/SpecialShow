package com.umeng.commm.ui.fragments;

import android.content.ComponentName;
import android.util.TypedValue;
import android.view.View;

import com.umeng.comm.core.utils.ResFinder;
import com.umeng.commm.ui.presenter.impl.CategoryTopicPresenter;
import com.umeng.common.ui.presenter.impl.TopicBasePresenter;

/**
 * Created by wangfei on 15/12/22.
 */
public class CategoryTopicFragment extends TopicFragment {

    public CategoryTopicFragment() {
        super();
    }

    public static CategoryTopicFragment newTopicFragment() {
        return new CategoryTopicFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return ResFinder.getLayout("umeng_comm_topic_search");
    }

    @Override
    protected TopicBasePresenter createPresenters() {
        return new CategoryTopicPresenter(this);
    }

    // dont impl searchView
    @Override
    protected void initSearchView(View rootView) {
    }

    @Override
    protected void initRefreshView(View rootView) {
        super.initRefreshView(rootView);
        mRefreshLvLayout.setProgressViewOffset(false, 60,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mRefreshLvLayout.setRefreshing(true);
        mBaseView.setEmptyViewText(ResFinder.getString("umeng_comm_no_topic"));
    }
}
