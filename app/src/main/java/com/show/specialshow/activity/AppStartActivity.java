package com.show.specialshow.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.listener.OnViewChangeListener;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.BannerPointUtils;
import com.show.specialshow.utils.MyBitmapUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyScrollLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AppStartActivity extends BaseActivity implements OnViewChangeListener{
    private MyScrollLayout mScrollLayout;//滑动器
    private int count;
    private int currentItem;
    private TextView start_iv;//点击开启
    private GestureDetector mGestureDetector;//手势识别器
    private Bitmap bitmap;

    int[] imgIds;
    int index = 0;
    @SuppressWarnings("rawtypes")
    private Class clazz = com.show.specialshow.activity.MainActivity.class;

    private LinearLayout ll_point;//点的布局
    private ArrayList<ImageView> pointviews = new ArrayList<>();
    private BannerPointUtils bannerPointUtils;//小点工具类


    static class MyHandler extends Handler {
        WeakReference<AppStartActivity> mActivity;

        MyHandler(AppStartActivity activity) {
            mActivity = new WeakReference<AppStartActivity>(activity);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = new Bundle();
            AppStartActivity activity = mActivity.get();
            if (activity == null)
                return;
            switch (msg.what) {
                case 0:
                    activity.showBanner();
                    break;
                case 1:
                    boolean isFirst = TXApplication.appConfig.getBoolean("isFirst",
                            true);
                    if (isFirst) {
                        activity.setContentView(R.layout.activity_app_start);
                        activity.initStartView();
                    } else {
                        activity.setHome(activity.clazz);
                        UIHelper.startActivity(activity,activity.clazz,bundle);
                        activity.finish();
                    }
                    break;
            }
        }

    };

    @SuppressWarnings("rawtypes")
    private void setHome(Class clazz) {
        String name = clazz.getName();
        AppManager.appStartName = name;
        SharedPreferences.Editor edit = TXApplication.appConfig.edit();
        edit.putString("homeName", name);
        edit.commit();
    }

    MyHandler mHandler = new MyHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        MyBitmapUtils.showBitmap(this, R.id.guide_pages, R.drawable.img_guide1);
        mHandler.sendEmptyMessageDelayed(1, 2000);
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {

                        setHome(clazz);
                        Bundle bundle = new Bundle();
                        UIHelper.startActivity(AppStartActivity.this, clazz,
                                bundle);
                        finish();
                        mHandler.removeMessages(0);
                        return super.onFling(e1, e2, velocityX, velocityY);

                    }
                });
    }

    private void initStartView() {
        mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
        // pointLLayout = (LinearLayout) findViewById(R.id.llayout);
        start_iv = (TextView) findViewById(R.id.start_iv);
        start_iv.setOnClickListener(onClick);
        ll_point = (LinearLayout) findViewById(R.id.adddot);
        count = mScrollLayout.getChildCount();
        currentItem = 0;
        mScrollLayout.SetOnViewChangeListener(this);
        bannerPointUtils=new BannerPointUtils(mContext,ll_point,pointviews);
        bannerPointUtils.initPoint(2);
        bannerPointUtils.draw_Point(0);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.start_iv:
                    setHome(clazz);
                    UIHelper.startActivity(AppStartActivity.this,clazz
                    ,bundle);
                    SharedPreferences.Editor editor = TXApplication.appConfig.edit();
                    editor.putBoolean("isFirst",false);
                    editor.commit();
                    finish();
                    break;
            }
        }
    };

    @Override
    public void OnViewChange(int position) {
        setcurrentPoint(position);
    }

    private void setcurrentPoint(int position) {
        if (position < 0 || position > count - 1 || currentItem == position) {
            return;
        }
        // imgs[currentItem].setEnabled(true);
        // imgs[position].setEnabled(false);
        currentItem = position;
        bannerPointUtils.draw_Point(position);
    }

    private void showBanner() {
        if (imgIds != null && index < imgIds.length) {
            MyBitmapUtils.showBitmap(this, R.id.guide_pages, imgIds[index]);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            TXApplication.setNew();
            mHandler.sendEmptyMessageDelayed(1, 0);
        }
        index++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBitmapUtils.recycleBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { // 让手势识别器 生效
        if (mGestureDetector == null)
            return false;
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // mHandler.removeMessages(1);
    }


    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void fillView() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


}
