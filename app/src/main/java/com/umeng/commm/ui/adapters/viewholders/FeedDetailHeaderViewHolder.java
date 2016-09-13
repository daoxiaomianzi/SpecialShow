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

package com.umeng.commm.ui.adapters.viewholders;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.imageloader.ImgDisplayOption;
import com.umeng.comm.core.imageloader.UMImageLoader;
import com.umeng.comm.core.sdkmanager.ImageLoaderManager;
import com.umeng.comm.core.utils.CommonUtils;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.comm.core.utils.TimeUtils;
import com.umeng.commm.ui.activities.TopicDetailActivity;
import com.umeng.commm.ui.activities.UserInfoActivity;
import com.umeng.common.ui.colortheme.ColorQueque;
import com.umeng.common.ui.dialogs.ImageBrowser;
import com.umeng.common.ui.listener.FrinendClickSpanListener;
import com.umeng.common.ui.listener.TopicClickSpanListener;
import com.umeng.common.ui.util.FeedViewRender;
import com.umeng.common.ui.util.UserTypeUtil;
import com.umeng.common.ui.util.WebViewSettingUtil;
import com.umeng.common.ui.widgets.RoundImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 *
 */
public class FeedDetailHeaderViewHolder extends FeedItemViewHolder implements
        UMImageLoader.ImageLoadingListener {

    private RoundImageView mPortrait;
    private TextView mTitleView;
    private UMImageLoader mImageLoader;
//    private NetworkImageView mUserMedal;
    private LinearLayout mUserTypeIcon;
    private ImgDisplayOption mDisplayOption = new ImgDisplayOption();

    private ImgDisplayOption mMedalDisplayOption = new ImgDisplayOption();

    private int mScreenWidth;
    private WebView mContentWeb;
    private LinearLayout mImageContainer;
    private boolean mIsAddImg;

    private ImageBrowser mImageBrowser;

    public FeedDetailHeaderViewHolder(Context context, View rootView) {
        super(context, rootView);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenWidth -= CommonUtils.dip2px(mContext, 20);
        mDisplayOption.mLoadingResId = ResFinder.getResourceId(ResFinder.ResType.DRAWABLE,
                "umeng_comm_not_found");
        mDisplayOption.mLoadFailedResId = ResFinder.getResourceId(ResFinder.ResType.DRAWABLE,
                "umeng_comm_not_found");
        mDisplayOption.mDefaultImageSize = new Point(600, 600);
        mImageBrowser = new ImageBrowser(mContext);
    }

    @Override
    public boolean setupImageGridView() {
        if (mIsAddImg) {
            return true;
        }
        mIsAddImg = true;
        if (mFeedItem.imageUrls == null || mFeedItem.imageUrls.size() <= 0 ||mFeedItem.media_type ==1) {
            mImageContainer.setVisibility(View.GONE);
            return true;
        }
        for (int i = 0; i < mFeedItem.imageUrls.size(); i++) {
            final int position = i;
            ImageView imageView = new ImageView(mContext);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageBrowser.setImageList(mFeedItem.imageUrls, position);
                    mImageBrowser.show();
                }
            });
            mImageContainer.addView(imageView);
            imageView.setVisibility(View.GONE);
            mImageLoader.displayImage(mFeedItem.imageUrls.get(i).originImageUrl,
                    imageView, mDisplayOption, this);
        }
        mImageContainer.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    protected void setBaseFeeditemInfo() {
        mImageLoader = ImageLoaderManager.getInstance().getCurrentSDK();
        // 用户头像
        ImgDisplayOption option = ImgDisplayOption.getOptionByGender(mFeedItem.creator.gender);
        if (!TextUtils.isEmpty(mFeedItem.creator.iconUrl)) {
            mImageLoader.displayImage(mFeedItem.creator.iconUrl, mPortrait, option);
        } else {
            mPortrait.setImageResource(option.mLoadingResId);
        }

//        if (mFeedItem.creator.medals != null && !mFeedItem.creator.medals.isEmpty()) {
//            mUserMedal.setImageUrl(mFeedItem.creator.medals.get(0).imgUrl, mMedalDisplayOption);
//            mUserMedal.setVisibility(View.VISIBLE);
//        } else {
//            mUserMedal.setVisibility(View.GONE);
//        }
        UserTypeUtil.SetUserType(mContext, mFeedItem.creator, mUserTypeIcon);
        mPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, UserInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.TAG_USER, mFeedItem.creator);
                mContext.startActivity(intent);
            }
        });

        // 设置feed类型图标
        boolean hasTypeIcon = setTypeIcon();
        // 昵称
        mUserNameTv.setText(mFeedItem.creator.name);
        // 更新时间
        Date date = new Date(Long.parseLong(mFeedItem.publishTime));
        mTimeTv.setText(TimeUtils.format(date));
        // feed的文本内容
        FeedViewRender.parseTopicsAndFriends(mFeedTextTv, mFeedItem, new TopicClickSpanListener() {
            @Override
            public void onClick(Topic topic) {
                Intent intent = new Intent(mContext,
                        TopicDetailActivity.class);
                intent.putExtra(Constants.TAG_TOPIC, topic);
                mContext.startActivity(intent);
            }
        }, new FrinendClickSpanListener() {
            @Override
            public void onClick(CommUser user) {
                Intent intent = new Intent(mContext,
                        UserInfoActivity.class);
                intent.putExtra(Constants.TAG_USER, user);
                mContext.startActivity(intent);
            }
        });
        if (mFeedItem.title.equals("null") || TextUtils.isEmpty(mFeedItem.title)) {
            mTitleView.setText(ResFinder.getString("umeng_comm_content_detail"));
        } else {
            mTitleView.setText(mFeedItem.title);
        }
        mTitleView.setTextColor(ColorQueque.getColor("umeng_comm_find_user_nologin_text"));
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        RelativeLayout.LayoutParams feedItemParams = (RelativeLayout.LayoutParams) mTitleView.getLayoutParams();
        feedItemParams.leftMargin = CommonUtils.dip2px(mContext, hasTypeIcon ? 5 : 10);

        // 内容为空时Text隐藏布局,这种情况出现在转发时没有文本的情况
        if (mFeedItem.media_type == 0){

            if (TextUtils.isEmpty(mFeedItem.text)) {
                mFeedTextTv.setVisibility(View.GONE);
            } else {
                mFeedTextTv.setVisibility(View.VISIBLE);
                mFeedTextTv.setTextColor(ColorQueque.getColor("umeng_comm_find_user_nologin_text"));
                mFeedTextTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }
        }
        if (mFeedItem.media_type == 1){
            WebViewSettingUtil.SetWebiew(mContentWeb,1,mFeedItem,null,null,mContext);
        }
//        mFeedTextTv.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mPresenter.clickFeedItem();
//            }
//        });
    }
    private String fmtString(String str){
        String notice = "";
        try{
            notice = URLEncoder.encode(str, "utf-8");
        }catch(UnsupportedEncodingException ex){
        }
        return notice;
    }

    @Override
    protected boolean setTypeIcon() {
        Drawable drawable;
        if (mFeedItem.tag == 1) {
            drawable = ColorQueque.getDrawable("umeng_comm_detail_discuss_jing");
            mFeedTypeImgView.setVisibility(View.VISIBLE);
            mFeedTypeImgView.setImageDrawable(drawable);
        } else {
            mFeedTypeImgView.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    @Override
    protected void inflateFromXML() {
        int feedTypeResId = ResFinder.getId("feed_type_img_btn");
        int titleResId = ResFinder.getId("feed_detail_title");
        int timeResId = ResFinder.getId("umeng_comm_msg_time_tv");
        int userNameResId = ResFinder.getId("umeng_comm_msg_user_name");

        int textResId = ResFinder.getId("umeng_comm_msg_text");
        int PortraitResId = ResFinder.getId("user_portrait_img_btn");

        // 公告或者好友feed的图标
        mFeedTypeImgView = findViewById(feedTypeResId);

        mFeedTextTv = findViewById(textResId);
        // 发布该消息的昵称
        mUserNameTv = findViewById(userNameResId);

        mPortrait = findViewById(PortraitResId);

        mTimeTv = findViewById(timeResId);

        mTitleView = findViewById(titleResId);

        mImageContainer = findViewById(ResFinder.getId("umeng_comm_msg_images_gv_viewstub"));

        mUserTypeIcon = findViewById(ResFinder.getId("user_type_icon_container"));
        mContentWeb = findViewById(ResFinder.getId("umeng_comm_msg_textweb"));
    }

    private void adjustImgViewSize(int w, int h, ImageView v) {
        LinearLayout.LayoutParams mImageViewLayoutParams;
        int imgHeight = (int) (h * mScreenWidth / ((float) w));
        if (imgHeight > mScreenWidth * 3) {
            imgHeight = mScreenWidth * 3;
        }
        mImageViewLayoutParams = new LinearLayout.LayoutParams(mScreenWidth, imgHeight);
        mImageViewLayoutParams.bottomMargin = CommonUtils.dip2px(mContext, 8);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        v.setLayoutParams(mImageViewLayoutParams);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        adjustImgViewSize(loadedImage.getWidth(), loadedImage.getHeight(), (ImageView) view);
        ((ImageView) view).setImageBitmap(loadedImage);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view) {
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }
}
