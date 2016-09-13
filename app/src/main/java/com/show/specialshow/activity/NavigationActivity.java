package com.show.specialshow.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.show.specialshow.BaseMapActivity;
import com.show.specialshow.R;

/**
 * 高德地图驾车导航页
 */

public class NavigationActivity extends BaseMapActivity {
    // 当前定位坐标(起点)
    private double mLat1=0.0d;//纬度
   private  double mLon1=0.0d;//经度
    // 要到达的坐标(终点)
   private  double mLat2=0.0d;//纬度
   private  double mLon2=0.0d;//经度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLat1=getIntent().getExtras().getDouble("mLat1");
        mLon1=getIntent().getExtras().getDouble("mLon1");
        mLat2=getIntent().getExtras().getDouble("mLat2");
        mLon2=getIntent().getExtras().getDouble("mLon2");
        mStartLatlng = new NaviLatLng(mLat1, mLon1);
        mEndLatlng = new NaviLatLng(mLat2, mLon2);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }
//
//    /**
//     * 如果使用无起点算路，请这样写
//     */
//    private void noStartCalculate() {
//        //无起点算路须知：
//        //AMapNavi在构造的时候，会startGPS，但是GPS启动需要一定时间
//        //在刚构造好AMapNavi类之后立刻进行无起点算路，会立刻返回false
//        //给人造成一种等待很久，依然没有算路成功 算路失败回调的错觉
//        //因此，建议，提前获得AMapNavi对象实例，并判断GPS是否准备就绪
//
//
//        if (mAMapNavi.isGpsReady())
//            mAMapNavi.calculateDriveRoute(eList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
//    }


    @Override
    public void onCalculateRouteSuccess() {
        mAMapNavi.startNavi(NaviType.GPS);
    }

}
