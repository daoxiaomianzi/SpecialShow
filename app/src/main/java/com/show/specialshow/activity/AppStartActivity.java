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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.listener.OnViewChangeListener;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.BannerPointUtils;
import com.show.specialshow.utils.MyBitmapUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyScrollLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AppStartActivity extends BaseActivity implements OnViewChangeListener,AMapLocationListener{
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
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat=0.0d;//纬度
    private double mLon=0.0d;//经度


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

    }

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
        if(TXApplication.login){
            InitLocation();
        }

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

    /**
     * 更新用户当前所在坐标
     */
    private void updateXy(){
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_UPDATEXY;
        if(0.0d==mLat||0.0d==mLon){
            InitLocation();
            return;
        }else{
            params.addBodyParameter("x",mLon+"");//经度
            params.addBodyParameter("y",mLat+"");//纬度
        }
        params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    /**
     * 初始化地图定位
     * @param
     */
    private void InitLocation() {
        locationClient = new AMapLocationClient(mContext.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }
    /**
     * 高德定位回调
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(null==aMapLocation){
            UIHelper.ToastMessage(mContext,"获取当前位置失败");
            return;
        }
        mLat=aMapLocation.getLatitude();
        mLon=aMapLocation.getLongitude();
        locationClient.stopLocation();
        updateXy();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        MyBitmapUtils.recycleBitmap(bitmap);

        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


}
