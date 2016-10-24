package com.show.specialshow.activity;

import android.view.View;

import com.show.specialshow.BaseWebActivity;
import com.show.specialshow.R;
import com.show.specialshow.URLs;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.SPUtils;

public class SignInActivity extends BaseWebActivity {
    @Override
    public void initData() {
        super.initData();
        path= URLs.SIGN_IN+ SPUtils.get(mContext,"uid","");
    }

    @Override
    public void fillView() {
        super.fillView();
        head_title_tv.setText("签到");
        loadDetail();
    }

    @Override
    public void onClick(View v) {
        if(!BtnUtils.getInstance().isFastDoubleClick()){
            return;
        }
        switch (v.getId()){
            case R.id.contest_confirm_tv:
                if(null!=affirmDialog){
                    affirmDialog.dismiss();
                }
                break;
            case R.id.contest_cancel_tv:
                if(null!=affirmDialog){
                    affirmDialog.dismiss();
                }
                break;
        }

    }
}
