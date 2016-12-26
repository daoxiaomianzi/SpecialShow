package com.show.specialshow.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.show.specialshow.BaseWebActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.utils.ShareServiceFactory;
import com.umeng.comm.core.beans.ShareContent;

public class OfficialEventAndDynamicActivity extends BaseWebActivity {
    private TeShowActivitiesMess activitiesMess;

    @Override
    public void initData() {
        super.initData();
        activitiesMess = (TeShowActivitiesMess) getIntent().getSerializableExtra("event_dynamic");
        if (null != activitiesMess) {
            html = activitiesMess.getPost_content();
        }
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("详情");
        head_right_tv.setVisibility(View.VISIBLE);
        Drawable rightDrawable = getResources()
                .getDrawable(R.drawable.icon_share);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                rightDrawable.getMinimumHeight());
        head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
        loadNativeDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right_tv:
                share();
                break;
        }
    }

    /**
     * 分享回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareServiceFactory.getShareService().onActivityResult(mContext, requestCode, resultCode, data);
    }

    /**
     * 分享
     */
    private void share() {
        ShareContent shareItem = new ShareContent();
        shareItem.mText = activitiesMess.getPost_excerpt();
        shareItem.mTargetUrl = activitiesMess.getPost_share_url();
        shareItem.mTitle = activitiesMess.getPost_title();
        ShareServiceFactory.getShareService().share(this, shareItem);
    }
}
