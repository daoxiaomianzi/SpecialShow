package com.show.specialshow.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.show.specialshow.R;
import com.show.specialshow.listener.Shareable;
import com.umeng.comm.core.beans.ShareContent;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.utils.Log;
import com.umeng.comm.core.utils.ToastMsg;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.regex.Pattern;

/**
 * Created by admin on 2016/8/1.
 */
public class ShareServiceImpl implements Shareable {

    private static Pattern mDoubleBytePattern = Pattern.compile("[^\\x00-\\xff]");

    ShareServiceImpl() {
    }

    public void share(final Activity activity, final ShareContent shareItem) {
        UMImage image = null;
        String title = shareItem.mTitle;
        Log.e("xxxxxx", "share!!!!!!!!!!");
        if (!TextUtils.isEmpty(title) && title.length() > 16) {
            title = title.substring(0, 16);
        }

        if (shareItem.mImageItem != null) {
            String drawable = shareItem.mImageItem.thumbnail;
//            image = new UMImage(activity, drawable);
            image = new UMImage(activity, R.drawable.icon_share_logo);
        } else {
            try {
                Drawable drawable1 = activity.getPackageManager().getApplicationIcon(activity.getPackageName());
                if (drawable1 == null) {
                    return;
                }
                image = new UMImage(activity, R.drawable.icon_share_logo);
//                image = new UMImage(activity, ((BitmapDrawable)drawable1).getBitmap());
            } catch (PackageManager.NameNotFoundException var7) {
                var7.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = shareItem.mText.substring(0, 20);
        }

        (new ShareAction(activity)).withText(shareItem.mText).withTitle(title).withMedia(image).withTargetUrl(shareItem.mTargetUrl).setCallback(new UMShareListener() {
            public void onResult(SHARE_MEDIA platform) {
                ToastMsg.showShortMsgByResName("umeng_comm_share_success");
                String platformKeyWorld = "";
                if (platform == SHARE_MEDIA.WEIXIN) {
                    platformKeyWorld = "wxsession";
                } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    platformKeyWorld = "wxtimeline";
                } else if (platform == SHARE_MEDIA.GOOGLEPLUS) {
                    platformKeyWorld = "google_plus";
                } else {
                    platformKeyWorld = platform.toString();
                }

                CommunityFactory.getCommSDK(activity).sendFeedShareAnalysis(shareItem.mFeedId, platformKeyWorld);
            }

            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).setDisplayList(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE}).open();
    }

    public void share(final String id, final Activity activity, final ShareContent shareItem) {
        UMImage image = null;
        String title = shareItem.mTitle;
        Log.e("xxxxxx", "share!!!!!!!!!!");
        if (!TextUtils.isEmpty(title) && title.length() > 16) {
            title = title.substring(0, 16);
        }

        if (shareItem.mImageItem != null) {
            String drawable = shareItem.mImageItem.thumbnail;
//            image = new UMImage(activity, drawable);
            image = new UMImage(activity, R.drawable.icon_share_logo);
        } else {
            try {
                Drawable drawable1 = activity.getPackageManager().getApplicationIcon(activity.getPackageName());
                if (drawable1 == null) {
                    return;
                }
                image = new UMImage(activity, R.drawable.icon_share_logo);
//                image = new UMImage(activity, ((BitmapDrawable)drawable1).getBitmap());
            } catch (PackageManager.NameNotFoundException var7) {
                var7.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = shareItem.mText.substring(0, 20);
        }

        (new ShareAction(activity)).withText(shareItem.mText).withTitle(title).withMedia(image).withTargetUrl(shareItem.mTargetUrl).setCallback(new UMShareListener() {
            public void onResult(SHARE_MEDIA platform) {
//                ToastMsg.showShortMsgByResName("umeng_comm_share_success");
                UIHelper.ToastMessage(activity, id);
                String platformKeyWorld = "";
                if (platform == SHARE_MEDIA.WEIXIN) {
                    platformKeyWorld = "wxsession";
                } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    platformKeyWorld = "wxtimeline";
                } else if (platform == SHARE_MEDIA.GOOGLEPLUS) {
                    platformKeyWorld = "google_plus";
                } else {
                    platformKeyWorld = platform.toString();
                }

                CommunityFactory.getCommSDK(activity).sendFeedShareAnalysis(shareItem.mFeedId, platformKeyWorld);
            }

            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).setDisplayList(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE}).open();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
