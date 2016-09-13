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

package com.umeng.commm.ui.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.constants.HttpProtocol;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listener.Listeners.LoginOnViewClickListener;
import com.umeng.comm.core.sdkmanager.ShareSDKManager;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.Log;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.commm.ui.dialogs.FeedActionDialog;
import com.umeng.commm.ui.fragments.FeedDetailFragment;
import com.umeng.common.ui.activities.BaseFragmentActivity;
import com.umeng.common.ui.mvpview.MvpFeedDetailActivityView;
import com.umeng.common.ui.mvpview.MvpFeedDetailView;
import com.umeng.common.ui.presenter.impl.FeedDetailActivityPresenter;
import com.umeng.common.ui.presenter.impl.TakePhotoPresenter;
import com.umeng.common.ui.util.BroadcastUtils;
import com.umeng.common.ui.widgets.BaseView;
import com.umeng.common.ui.widgets.CommentEditText;

/**
 * 某条Feed的详情页面,会根据feed id每次都会从服务器获取最新数据,暂时没有使用数据库缓存.
 */
public class FeedDetailActivity extends BaseFragmentActivity implements OnClickListener,
        MvpFeedDetailView, MvpFeedDetailActivityView {

    /**
     * 评论布局
     */
    private View mCommentLayout;
    /**
     * 评论ditText
     */
    private CommentEditText mCommentEditText;

    /**
     * 目标feed的id
     */
    private String mFeedId = "";
    /**
     * Feed详情Fragment
     */
    FeedDetailFragment mFeedFrgm;

    FeedItem mFeedItem;
    /**
     * 布局监听器,监听布局高度，用以计算评论时布局应该滚动的高度
     */
    private OnGlobalLayoutListener mGlobalLayoutListener;
    /**
     *
     */
    private View mRootView;

    /**
     * Presenter
     */
    FeedDetailActivityPresenter mActivityPresenter;
    /**
     * 更多操作Dialog
     */
    FeedActionDialog mActionDialog;

    private BaseView mBaseView;



    private View mFavoriteImg;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 【注意】如果来源于通知栏打开详情页，需要进行相关初始化操作，如果已经初始化，则方法内部会直接返回~
        CommunityFactory.getCommSDK(getApplicationContext());
        setContentView(ResFinder.getLayout("umeng_commm_feed_detail"));
        CommonUtils.injectComponentImpl(getApplicationContext());// 重新注入登录组件的实现，避免的推送启动时无自定义的登录组件实现

        // 设置Fragment的container id
        setFragmentContainerId(ResFinder.getId("umeng_comm_feed_container"));
        mActivityPresenter = new FeedDetailActivityPresenter(this);
        mActivityPresenter.attach(this);
        initViews();
        initFeed(getIntent());
        BroadcastUtils.registerFeedUpdateBroadcast(this, mReceiver);
//        initTopicPopWindow();
    }

    /**
     *
     */
    protected final void onNewIntent(Intent paramIntent) {
        Log.d("newintent", paramIntent.getExtras() + "");
        super.onNewIntent(paramIntent);
        initFeed(paramIntent);
    }

    private void initFeed(Intent intent) {
        intent.setExtrasClassLoader(ImageItem.class.getClassLoader());
        Bundle extraBundle = intent.getExtras();
        if (extraBundle == null) {
            return;
        }

        mActionDialog = new FeedActionDialog(this);
        if (extraBundle.containsKey(Constants.FEED_ID)) {
            mActivityPresenter.setExtraData(extraBundle);
            mFeedId = extraBundle.getString(Constants.FEED_ID);
            mActionDialog.setFeedId(mFeedId);
        } else if (extraBundle.containsKey(Constants.FEED)) {
            mFeedItem = extraBundle.getParcelable(Constants.FEED);
            mFeedId = mFeedItem.id;
            mActionDialog.setFeedItem(mFeedItem);
            // 传递评论的id
            if (extraBundle.containsKey(HttpProtocol.COMMENT_ID_KEY)) {
                mFeedItem.extraData.putString(HttpProtocol.COMMENT_ID_KEY,
                        extraBundle.getString(HttpProtocol.COMMENT_ID_KEY));
            }

            // 初始化fragment
            initFragment(mFeedItem);

            if (extraBundle.containsKey(Constants.TAG_IS_COMMENT)) {
                mFeedFrgm.mIsShowComment = true;
            }
        }
        fetchFeedInfo(mRootView);
        checkFeedItem();
        mActionDialog.attachView(this);
        mActivityPresenter.setFeedItem(mFeedItem);
        // TODO: 15/12/14  通过push进入，只有feedid的情况
        if(mFeedItem != null && mFeedItem.isCollected){
            mFavoriteImg.setSelected(true);
        }else{
            mFavoriteImg.setSelected(false);
        }
    }

    /**
     * 检查Feed Item的有效性,如果该Feed已经被删除,那么提示相关信息,并且退出该Activity
     */
    private void checkFeedItem() {
        if (mFeedItem != null && mFeedItem.status >= FeedItem.STATUS_SPAM&&mFeedItem.status != FeedItem.STATUS_LOCK) {
            ToastMsg.showShortMsgByResName("umeng_comm_feed_deleted");
            finish();
        }
    }

    /**
     * 初始化view</br>
     */
    private void initViews() {
        initTitleLayout();
        mRootView = findViewById(ResFinder.getId("umeng_comm_feed_detail_root"));
        mBaseView = (BaseView) findViewById(ResFinder.getId("umeng_comm_baseview"));
        mBaseView.forceLayout();

        findViewById(ResFinder.getId("umeng_comm_feed_container")).setOnTouchListener(
                new OnTouchListener() {

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mCommentLayout != null) {
                            mCommentLayout.setVisibility(View.GONE);
                            hideInputMethod(mCommentEditText);
                            return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * 根据feedid从server加载该条feed的信息,目前不需要登录。【该方法仅仅在推送的时候调用，其他地方直接传递Feed数据即可】 </br>
     */
    private void fetchFeedInfo(View view) {
        // 检查是否登录
        // mLoginOnViewClickListener.onClick(view);
        mActivityPresenter.fetchFeedWithId(mFeedId);
    }

    /**
     * 检测该条feed是否有效。由于在Response中构造了一个默认的Feed，此时需要验证其有效性。</br>
     *
     * @param feedItem
     * @return
     */
    private boolean isValidFeedItem(FeedItem feedItem) {
        return feedItem != null
                && !TextUtils.isEmpty(feedItem.id);
    }

    /**
     * 初始化feed detail fragment</br>
     *
     * @param feedItem
     */
    private void initFragment(FeedItem feedItem) {
        mFeedFrgm = FeedDetailFragment.newFeedDetailFragment(feedItem);
        mFeedFrgm.setArguments(getIntent().getExtras());
        replaceFragment(mFeedFrgm);
    }

    @SuppressWarnings("deprecation")
    private void initTitleLayout() {
        // back btn
        findViewById(ResFinder.getId("umeng_comm_title_back_btn")).setOnClickListener(this);
        findViewById(ResFinder.getId("umeng_comm_title_more_btn")).setOnClickListener(
                new LoginOnViewClickListener() {
                    @Override
                    protected void doAfterLogin(View v) {
                        mActionDialog.show();
                    }
                });
        findViewById(ResFinder.getId("umeng_comm_title_favorite_btn")).setOnClickListener(
                new LoginOnViewClickListener() {
                    @Override
                    protected void doAfterLogin(View v) {
                        if (mFeedItem.isCollected) {
                            mActivityPresenter.cancelFavoritesFeed();
                        } else {
                            mActivityPresenter.favoritesFeed();
                        }
                    }
                });

        mFavoriteImg = findViewById(ResFinder.getId("umeng_comm_favourite_img"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ResFinder.getId("umeng_comm_title_back_btn")) {
            this.finish();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onStop() {
        super.onStop();
        // 删除监听器,避免内存泄露
        mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(
                mGlobalLayoutListener);
    }

    @Override
    public void deleteFeedSuccess() {
        finish();
    }

    @Override
    public void fetchLikesComplete(String nextUrl) {

    }

    @Override
    public void fetchCommentsComplete() {

    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            // mBaseView.showLoadingView();
        } else {
            // mBaseView.hideLoadingView();
        }
    }

    @Override
    public void fetchDataComplete(FeedItem result) {
        if (isValidFeedItem(result)) { // 获取到feed并显示数据
            if (mFeedFrgm == null) {
                mFeedItem = result;
                mActionDialog.setFeedItem(mFeedItem);
                // 初始化fragment
                initFragment(mFeedItem);
            } else {
                mFeedItem = result;
                mFeedFrgm.updateFeedItem(result);
                mActionDialog.setFeedItem(mFeedItem);

            }


            if(result != null && result.isCollected){
                mFavoriteImg.setSelected(true);
            }else{
                mFavoriteImg.setSelected(false);
            }

        } else {
            // 获取到的数据无效，此时需要显示加载失败并可重新加载
//            mBaseView.showEmptyView();
        }
    }

    @Override
    public void fetchFeedFaild() {
//        finish();
    }

    /**
     * 此时仅仅关心详情页的收藏字段
     */
    private BroadcastUtils.DefalutReceiver mReceiver = new BroadcastUtils.DefalutReceiver() {
        public void onReceiveUpdateFeed(Intent intent) {
            FeedItem newFeedItem = getFeed(intent);
            if (newFeedItem.id.equals(mFeedItem.id)) {
                mFeedItem.category = newFeedItem.category;
                mFeedItem.isCollected = newFeedItem.isCollected;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("", "onBackPressed:" + this.getClass().getSimpleName());
        finish();
    }

    protected void onDestroy() {
        BroadcastUtils.unRegisterBroadcast(this, mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TakePhotoPresenter.REQUEST_IMAGE_CAPTURE) {
            if (mFeedFrgm != null) {
                mFeedFrgm.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            //ShareServiceFactory.getShareService().onActivityResult(this,requestCode,resultCode,data);
                ShareSDKManager.getInstance().getCurrentSDK().onActivityResult(this,requestCode,resultCode,data);
        }
    }

    @Override
    public void favoriteFeedComplete(String FeedId, boolean isFavorite) {
        mFavoriteImg.setSelected(isFavorite);
        mFeedItem.isCollected = isFavorite;
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void copyToClipboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipData data = ClipData.newPlainText("feed_text", mFeedItem.text);
            android.content.ClipboardManager mClipboard = (android.content.ClipboardManager) this
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboard.setPrimaryClip(data);
        } else {
            android.text.ClipboardManager mClipboard = (android.text.ClipboardManager) this
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboard.setText(mFeedItem.text);
        }
    }

    /**
     * 是否可删除该feed。可删除的条件是自己的feed、管理员有删除内容的权限</br>
     *
     * @return
     */
    private boolean isDeleteable() {
        CommUser loginedUser = CommConfig.getConfig().loginedUser;
        boolean deleteable = mFeedItem != null && loginedUser.id.equals(mFeedItem.creator.id); // 自己的feed情况
        boolean hasDeletePermission = mFeedItem.permission >= 100;
        return deleteable || hasDeletePermission;
    }

    private boolean isReportable(){
        if( mFeedItem == null || CommConfig.getConfig().loginedUser.id.equals(mFeedItem.creator.id) ) {
            return false;
        }
        return  true;
    }

    @Override
    public void showOwnerComment(boolean result) {

    }

    @Override
    public void showAllComment(boolean result) {

    }

    @Override
    public void forbidUserComplete() {

    }

    @Override
    public void cancelForbidUserComplete() {

    }
}
