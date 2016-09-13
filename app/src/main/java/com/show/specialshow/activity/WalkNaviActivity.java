package com.show.specialshow.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.show.specialshow.BaseMapActivity;
import com.show.specialshow.R;

/**
 * 创建时间：16/1/7 11:59
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class WalkNaviActivity extends BaseMapActivity {
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


    @Override
    public void onInitNaviSuccess() {
        mAMapNavi.calculateWalkRoute(sList.get(0), eList.get(0));
    }

    @Override
    public void onCalculateRouteSuccess() {
        mAMapNavi.startNavi(NaviType.GPS);

    }
}
