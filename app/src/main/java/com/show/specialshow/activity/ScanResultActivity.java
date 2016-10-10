package com.show.specialshow.activity;

import android.view.KeyEvent;
import android.view.View;

import com.show.specialshow.R;

/**
 * 扫描结果跳转页
 */
public class ScanResultActivity extends BaseBusCenWebActivity {
    @Override
    public void initData() {
        path=getIntent().getStringExtra("result");
    }
    @Override
    public void fillView() {
        head_title_tv.setText("成为手艺人");
        loadDetail();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && content.canGoBack()) {
            content.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void goBack(View v) {
        switch (v.getId()) {
            case R.id.head_left_tv:
                if (content.canGoBack()) {
                    content.goBack();
                } else {
                    mContext.finish();
                }
                break;
            case R.id.head_close_tv:
                mContext.finish();
                break;

            default:
                break;
        }
    }
}
