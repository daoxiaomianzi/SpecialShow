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

package com.umeng.commm.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.MessageCount;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.GuestStatusResponse;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.Log;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ResFinder.ResType;
import com.umeng.commm.ui.activities.FindActivity;

import com.umeng.common.ui.colortheme.ColorQueque;
import com.umeng.common.ui.configure.parseJson;
import com.umeng.common.ui.fragments.BaseFragment;
import com.umeng.common.ui.fragments.FeedListBaseFragment;
import com.umeng.common.ui.mvpview.MvpUnReadMsgView;
import com.umeng.common.ui.presenter.impl.NullPresenter;
import com.umeng.common.ui.widgets.MainIndicator;

import java.util.ArrayList;


/**
 * 社区首页，包含关注、推荐、话题三个tab的页面，通过ViewPager管理页面之间的切换.
 */
public class CommunityMainFragment extends BaseFragment<Void, NullPresenter> implements
        OnClickListener, MvpUnReadMsgView {

    private ViewPager mViewPager;
    private String[] mTitles;
    private Fragment mCurrentFragment;
    /**
     * Feed流页面
     */
//    private AllFeedsFragment mMainFeedFragment;
    /**
     * 推荐Feed页面
     */
//    private RecommendFeedFragment mRecommendFragment;
    /**
     * 话题页面
     */
//    private TopicMainFragment mTopicFragment;
    /**
     * viewpager adapter
     */
    private CommFragmentPageAdapter adapter;
    /**
     * 最热帖子界面
     */
//    private HotFeedsFragment mHotFeedsFragment;

    /**
     * 回退按钮的可见性
     */
    private int mBackButtonVisible = View.VISIBLE;
    /**
     * 跳转到话题搜索按钮的可见性
     */
    private int mTitleVisible = View.VISIBLE;
    /**
     * title的根布局
     */
    private View mTitleLayout;
    /**
     * 右上角的个人信息Button
     */
    private ImageView mProfileBtn;

    private String mContainerClass;
    /**
     * tab视图
     */
//    private SegmentView mSegmentView;
    private MainIndicator indicator;
    /**
     * 未读消息的数量
     */
    private MessageCount mUnreadMsg = CommConfig.getConfig().mMessageCount;
    /**
     * 含有未读消息时的红点视图
     */
    private View mBadgeView;
    //    private PopupWindow hotPop, topicPop;
    public int TopicType = 2;

    private IndicatorListerner mIndicatorListerner;

    private ArrayList<FeedListBaseFragment> mFeedListBaseFragments = new ArrayList<FeedListBaseFragment>();
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected int getFragmentLayout() {
        CommunitySDKImpl.getInstance().fetchCommunitystatus(new Listeners.SimpleFetchListener<GuestStatusResponse>() {
            @Override
            public void onComplete(GuestStatusResponse response) {
                CommonUtils.visitNum = response.guestStatus;
            }
        });
        return ResFinder.getLayout("umeng_comm_community_frag_layout");
    }

    protected void initWidgets() {
//        ColorQueque.init();
        mContainerClass = getActivity().getClass().getName();
        parseJson.getJson(getActivity(), "custom.json");
        initTitle(mRootView);
        initFragment();
        initViewPager(mRootView);
        registerInitSuccessBroadcast();
        CommunitySDKImpl.getInstance().upLoadUI("luntan");
    }

    /**
     * 初始化title</br>
     *
     * @param rootView
     */
    private void initTitle(View rootView) {
        mIndicatorListerner = new IndicatorListerner();
        if (parseJson.title.size() == 0) {
            mTitles = getResources().getStringArray(
                    ResFinder.getResourceId(ResType.ARRAY, "umeng_comm_feed_titles"));
        } else {
            mTitles = new String[parseJson.title.size()];
            parseJson.title.toArray(mTitles);
        }
        int titleLayoutResId = ResFinder.getId("topic_action_bar");
        mTitleLayout = rootView.findViewById(titleLayoutResId);
        mTitleLayout.setVisibility(View.GONE);

        int backButtonResId = ResFinder.getId("umeng_comm_back_btn");
        rootView.findViewById(backButtonResId).setOnClickListener(this);

        if (mBackButtonVisible != View.VISIBLE) {
            rootView.findViewById(backButtonResId).setVisibility(mBackButtonVisible);
        }

        mTitleLayout.setVisibility(mTitleVisible);

        mBadgeView = findViewById(ResFinder.getId("umeng_comm_badge_view"));
        mBadgeView.setVisibility(View.INVISIBLE);
        //
        mProfileBtn = (ImageView) rootView
                .findViewById(ResFinder.getId("umeng_comm_user_info_btn"));
        mProfileBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBadgeView != null) {
                            mBadgeView.setVisibility(View.INVISIBLE);
                        }
//                    CommunitySDKImpl.getInstance().fetchAccessToken(null);
                        gotoFindActivity(CommConfig.getConfig().loginedUser);
                    }
                }
//                new LoginOnViewClickListener() {
//            @Override
//            protected void doAfterLogin(View v) {
//                if (mBadgeView != null) {
//                    mBadgeView.setVisibility(View.INVISIBLE);
//                }
//
//                    gotoFindActivity(CommConfig.getConfig().loginedUser);
//
//            }
//        }
        );
        indicator = (MainIndicator) rootView.findViewById(ResFinder
                .getId("umeng_comm_segment_view"));
        // 设置tabs
//        String[] titles = new String[]{"热门", "推荐", "关注", "话题"};
        indicator.setTabItemTitles(mTitles);
        indicator.setVisibleTabCount(4);
        initPopwindow();
        initTopicPopWindow();
        indicator.SetIndictorClick(mIndicatorListerner);
    }

    /**
     * listerner for MainIndicator 热门，推荐，关注，话题
     */
    private class IndicatorListerner implements MainIndicator.IndicatorListener {
        @Override
        public void SetItemClick() {
            int cCount = indicator.getChildCount();
            for (int i = 0; i < cCount; i++) {
                final int j = i;
                View view = indicator.getChildAt(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (j == 0) {
//                            if (hotPop.isShowing()) {
//                                hotPop.dismiss();
//                            } else {
//                                hotPop.showAsDropDown(v,0,15);
//                            }
//                        } else if (j == 3) {
//                            if (hotPop.isShowing()) {
//                                topicPop.dismiss();
//                            } else {
//                                topicPop.showAsDropDown(v,0,15);
//                            }
//                        } else {
                        mViewPager.setCurrentItem(j);
//                        }
                    }
                });
            }
        }
    }

    /**
     * popwindow for 热门帖子
     */
    private void initPopwindow() {
//        RadioGroup hotGoup = new RadioGroup(getActivity());
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        ViewGroup view = (ViewGroup) inflater.inflate(ResFinder.getLayout("umeng_comm_hotness_btn_layout"), null, false);
//        if (view != null) {
//            int count = view.getChildCount();
//            for (int i = 0; i < count; i++) {
//                RadioButton tv = (RadioButton) view.getChildAt(0);
//                tv.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mCurrentFragment instanceof HotFeedsFragment) {
//                            if (v.getId() == ResFinder.getId("umeng_comm_oneday_btn")) {
//                                ((HotFeedsFragment) mCurrentFragment).mHottestFeedPresenter.loadDataFromServer(Constants.ONE_DAY);
//                            } else if (v.getId() == ResFinder.getId("umeng_comm_threedays_btn")) {
//                                ((HotFeedsFragment) mCurrentFragment).mHottestFeedPresenter.loadDataFromServer(Constants.THREE_DAYS);
//                            } else if (v.getId() == ResFinder.getId("umeng_comm_sevendays_btn")) {
//                                ((HotFeedsFragment) mCurrentFragment).mHottestFeedPresenter.loadDataFromServer(Constants.SEVEN_DAYS);
//                            } else if (v.getId() == ResFinder.getId("umeng_comm_thirtydays_btn")) {
//                                ((HotFeedsFragment) mCurrentFragment).mHottestFeedPresenter.loadDataFromServer(Constants.THIRTY_DAYS);
//                            }
//                            mViewPager.setCurrentItem(0);
//                        }
//                        hotPop.dismiss();
//                    }
//                });
//                view.removeView(tv);
//                hotGoup.addView(tv);
//            }
//        }
//        hotPop = new PopupWindow(hotGoup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        hotPop.setBackgroundDrawable(ResFinder.getDrawable("xialakuang"));
//        hotPop.setOutsideTouchable(true);
//        hotPop.update();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUnreadMsg.unReadTotal > 0 && CommonUtils.isLogin(getActivity())) {
            mBadgeView.setVisibility(View.VISIBLE);
        } else {
            mBadgeView.setVisibility(View.INVISIBLE);
        }
    }

    private void initTopicPopWindow() {

    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 跳转到发现Activity</br>
     *
     * @param user
     */
    public void gotoFindActivity(CommUser user) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        Intent intent = new Intent(getActivity(), FindActivity.class);
        if (user == null) {// 来自开发者外部调用的情况
            intent.putExtra(Constants.TAG_USER, CommConfig.getConfig().loginedUser);
        } else {
            intent.putExtra(Constants.TAG_USER, user);
        }
        intent.putExtra(Constants.TYPE_CLASS, mContainerClass);
        getActivity().startActivity(intent);
    }

    /**
     * 设置回退按钮的可见性
     *
     * @param visible
     */
    public void setBackButtonVisibility(int visible) {
        if (visible == View.VISIBLE || visible == View.INVISIBLE || visible == View.GONE) {
            this.mBackButtonVisible = visible;
        }
    }

    /**
     * 设置Title区域的可见性
     *
     * @param visible {@see View#VISIBLE},{@see View#INVISIBLE},{@see View#GONE}
     */
    public void setNavTitleVisibility(int visible) {
        if (visible == View.VISIBLE || visible == View.INVISIBLE || visible == View.GONE) {
            mTitleVisible = visible;
        }
    }

    /**
     * 初始化ViewPager VIew</br>
     *
     * @param rootView
     */
    private void initViewPager(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(ResFinder.getId("viewPager"));
        mViewPager.setOffscreenPageLimit(mTitles.length);
        adapter = new CommFragmentPageAdapter(getChildFragmentManager());

        mViewPager.setAdapter(adapter);

//        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View view, float v) {
//                com.umeng.comm.core.utils.Log.d("ani","v:"+v);
//                if(Build.VERSION.SDK_INT==Build.VERSION_CODES.HONEYCOMB){
//                    view.setScaleX(0.5f);
//                    view.setScaleY(0.5f);
//                    com.umeng.comm.core.utils.Log.d("ani","v2:"+v);
//                }
//
//            }
//        });
        indicator.setOnPageChangeListener(new MainIndicator.PageChangeListener() {

            @Override
            public void onPageSelected(int page) {
                mCurrentFragment = getFragment(page);
                if (!(mCurrentFragment instanceof HotFeedsFragment)) {

                }

                if (mCurrentFragment instanceof TopicMainFragment) {
//                    ((TopicMainFragment) mCurrentFragment).init();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        indicator.setViewPager(mViewPager, 0);
    }

    class CommFragmentPageAdapter extends FragmentPagerAdapter {

        public CommFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return getFragment(pos);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

    }

    /**
     * 初始化Fragment</br>
     */
    private void initFragment() {
//        mMainFeedFragment = new AllFeedsFragment();
//        mRecommendFragment = new RecommendFeedFragment();
//        mTopicFragment = new TopicMainFragment();
//        mHotFeedsFragment = new HotFeedsFragment();
//        mCurrentFragment = mHotFeedsFragment;// 默认是MainFeedFragment
        if (parseJson.tc.size() != 0) {
            for (String name : parseJson.tc) {
                if (!name.equals("topic")) {
                    mFeedListBaseFragments.add(converToFragment(name));
                } else {
                    fragments.add(new TopicMainFragment());
                }
            }
        } else {
            mFeedListBaseFragments.add(new HotFeedsFragment());
            mFeedListBaseFragments.add(new RecommendFeedFragment());
            mFeedListBaseFragments.add(new AllFeedsFragment());
            fragments.add(new TopicMainFragment());
            mCurrentFragment = mFeedListBaseFragments.get(0);
        }

    }

    /**
     * 获取当前页面被选中的Fragment</br>
     *
     * @return
     */
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    /**
     * </br>
     *
     * @param pos
     * @return
     */
    private Fragment getFragment(int pos) {
        Fragment fragment = null;
//        if (pos == 0) {
//            fragment = mHotFeedsFragment;
//        } else if (pos == 1) {
//            fragment = mRecommendFragment;
//        } else if (pos == 2) {
//            fragment = mMainFeedFragment;
//        } else if (pos == 3) {
//            fragment = mTopicFragment;
//        }
        if (pos < mFeedListBaseFragments.size()) {
            fragment = mFeedListBaseFragments.get(pos);
        } else {
            fragment = fragments.get(pos - mFeedListBaseFragments.size());
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ResFinder.getId("umeng_comm_back_btn")) {
            getActivity().finish();
        }
    }

    /**
     * 隐藏MianFeedFragment的输入法，当退出fragment or activity的时候</br>
     */
    public void hideCommentLayoutAndInputMethod() {
//        if (mMainFeedFragment != null) {
//            mMainFeedFragment.hideCommentLayoutAndInputMethod();
//        }
//        if (parseJson.tc.contains("allfocus")){
//            feedListBaseFragments.get(parseJson.tc.indexOf("allfocus")).hideCommentLayoutAndInputMethod();
//        }
//        for (FeedListBaseFragment temp : mFeedListBaseFragments) {
//            temp.hideCommentLayoutAndInputMethod();
//        }
    }

    /**
     * clean sub fragment data</br>
     */
    public void cleanAdapterData() {
//        if (mMainFeedFragment != null) {
//            mMainFeedFragment.clearListView();
//        }
//        if (mRecommendFragment != null) {
//            mRecommendFragment.cleanAdapterData();
//        }
        for (FeedListBaseFragment temp : mFeedListBaseFragments) {
            temp.clearListView();
        }
    }

    @Override
    public void onFetchUnReadMsg(MessageCount unreadMsg) {
        this.mUnreadMsg = unreadMsg;
        if (mUnreadMsg.unReadTotal > 0) {
            mBadgeView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 主动调用加载数据。 【注意】该接口仅仅在退出登录时，跳转到FeedsActivity清理数据后重新刷新数据</br>
     */
    public void repeatLoadDataFromServer() {
//        if (mMainFeedFragment != null) {
//            mMainFeedFragment.loadFeedFromServer();
//        }
//        if (mRecommendFragment != null) {
//            mRecommendFragment.loadDataFromServer();
//        }
        for (FeedListBaseFragment temp : mFeedListBaseFragments) {
            temp.loadDataFromServer();
        }
    }

    /**
     * 注册登录成功时的广播</br>
     */
    private void registerInitSuccessBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_INIT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mInitConfigReceiver,
                filter);
    }

    private BroadcastReceiver mInitConfigReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onFetchUnReadMsg(CommConfig.getConfig().mMessageCount);
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mInitConfigReceiver);
        super.onDestroy();
    }
/*hot = 最热feed流 realtime=实时feed流 recommend = 推荐feed流 allfocus = 关注feed流*/
    public FeedListBaseFragment converToFragment(String name) {

        if (name.equals("realtime")) {
            RealTimeFeedFragment mRealTimeFeedFragment = RealTimeFeedFragment.newRealTimeFeedRecommend();
            mRealTimeFeedFragment.setShowActionbar(false);
            return  mRealTimeFeedFragment;
        } else if (name.equals("recommend")) {
            return new RecommendFeedFragment();
        } else if (name.equals("hot")) {
            return new HotFeedsFragment();
        } else if (name.equals("allfocus")) {
            return new AllFeedsFragment();
        } else {
            return new AllFeedsFragment();
        }
    }
}
