package com.easemob.chatuidemo.activity;

import android.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.show.specialshow.*;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

public class MapActivity extends BaseActivity implements AMapLocationListener, LocationSource
        , AMap.OnMapLoadedListener {
    //相关控件
    private MapView chat_map_view;
    private AMap aMap;
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private OnLocationChangedListener mListener;

    boolean isFirstLoc = true; // 是否首次定位
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    private String address;//地址
    private MarkerOptions markerOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat_map_view.onCreate(savedInstanceState);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        if (aMap == null) {
            aMap = chat_map_view.getMap();
        }
        if (0 == latitude) {
            setMap();
        } else {
            double longtitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");
            LatLng marker1 = new LatLng(latitude, longtitude);
            //设置中心点和缩放比例
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            head_right_tv.setVisibility(View.GONE);
            markerOption = new MarkerOptions();
            markerOption.position(marker1);
            markerOption.title(address).snippet(address);

            markerOption.draggable(true);
            markerOption.icon(
                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),
                                    R.drawable.location_marker)));
            // 将Marker设置为贴地显示，可以双指下拉看效果
            markerOption.setFlat(true);
            aMap.addMarker(markerOption);
        }


    }

    private void setMap() {
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void initData() {
        setContentView(R.layout.activity_map);
    }

    @Override
    public void initView() {
        chat_map_view = (MapView) findViewById(R.id.chat_map_view);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("位置信息");
        head_right_tv.setVisibility(View.VISIBLE);
        head_right_tv.setText("发送");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right_tv:
                Intent intent = mContext.getIntent();
                intent.putExtra("latitude", mLat);
                intent.putExtra("longitude", mLon);
                intent.putExtra("address", address);
                UIHelper.setResult(mContext, RESULT_OK, intent);
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_in_from_right);
                break;
            default:
                break;
        }

    }

    /**
     * 高德地图回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (dialog != null) {
            dialog.dismiss();
        }
        // map view 销毁后不在处理新接收的位置
        if (aMapLocation == null || chat_map_view == null) {
            UIHelper.ToastMessage(mContext, "获取当前位置失败");
            return;
        }
        if (isFirstLoc) {
            isFirstLoc = false;
            mLat = aMapLocation.getLatitude();
            mLon = aMapLocation.getLongitude();
            address = aMapLocation.getAddress();
            LatLng marker1 = new LatLng(mLat, mLon);
            //设置中心点和缩放比例
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
            if (null != mListener) {
                mListener.onLocationChanged(aMapLocation);
            }
        }
    }

    /**
     * 初始化地图定位
     *
     * @param
     */

    private void InitLocation() {

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
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
        loadIng("正在定位位置...", false);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        chat_map_view.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        chat_map_view.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        chat_map_view.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chat_map_view.onDestroy();
    }

    private void permissionLocation() {
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
//                /*以下为自定义提示语、按钮文字
                        .setRationalMessage("定位功能需要您授权，否则App将不能正常使用")
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        InitLocation();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        UIHelper.ToastMessage(mContext, "定位功能取消授权");
                    }
                });
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (null == locationClient) {
            permissionLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;

    }

    @Override
    public void onMapLoaded() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }
}
